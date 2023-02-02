package com.edu.tiku.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.edu.tiku.common.enums.SubjectEnum;
import com.edu.tiku.common.utils.SnowFlakeIDGenerator;
import com.edu.tiku.mapper.*;
import com.edu.tiku.model.entity.*;
import com.edu.tiku.model.entity.jyw.KeyValuePair;
import com.edu.tiku.model.entity.jyw.Ques;
import com.edu.tiku.model.request.JywLoginRequest;
import com.edu.tiku.model.request.JywQueryQuestionRequest;
import com.edu.tiku.model.request.JywRegisterRequest;
import com.edu.tiku.service.BaseBookChapterService;
import com.edu.tiku.service.JywService;
import com.edu.tiku.service.KnowledgeService;
import com.edu.tiku.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: 菁优网controller
 * author: dyh
 * date: 2022/12/29 16:02
 */
@RestController
@RequestMapping("/jyw")
@Slf4j
public class JywController {

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionOriginalDataMapper questionOriginalDataMapper;

    @Resource
    private OptionMapper optionMapper;

    @Resource
    private KnowledgeMapper knowledgeMapper;

    @Resource
    private BookChapterMapper bookChapterMapper;

    @Resource
    private OptionService optionService;

    @Resource
    private KnowledgeService knowledgeService;

    @Resource
    private JywService jywService;

    @Resource
    private BaseBookChapterService baseBookChepterService;

    @PostMapping("register")
    public String register(@RequestBody JywRegisterRequest request) {
        return jywService.register(request);
    }

    @PostMapping("login")
    public String login(@RequestBody JywLoginRequest request) {
        return jywService.login(request);
    }

    @PostMapping("queryQuestion")
    @Transactional
    public String queryQuestion(@RequestBody JywQueryQuestionRequest request) {
        log.info("request=={}", JSON.toJSONString(request));
        //记录并获取页数 TODO
        //通过redis获取 p1+p2+页码，不存在则保存，存在+1后保存，通过这个页码获取数据

        //TODO 获取教材章节，入参赋值
        //id+页数存redis

        //查询试题
        String questionUrl = String.format("http://api.jyeoo.com/v1/%s/counter/QuesQuery?tp=%s&p1=%s&p2=%s&p3=%s&ct=%s&dg=%s&sc=%s&gc=%s&rc=%s" +
                        "&yc=%s&ec=%s&er=%s&so=%s&yr=%s&rg=%s&po=%s&pd=%s&pi=%s&ps=%s&onlyNos=%s"
                , request.getSubject(), request.getTp(), request.getP1(), request.getP2(), request.getP3(),
                request.getCt(), request.getDg(), request.getSc(), request.getGc(), request.getRc(),
                request.getYc(), request.getEc(), request.getEr(), request.getSo(), request.getYr(), request.getRg(),
                request.getPo(), request.getPd(), request.getPi(), request.getPs(), request.getOnlyNos());
        log.info("questionUrl:{}", questionUrl);

        String questionJson = getBody(questionUrl, request.getToken());
        List<Ques> questionList = getQuestionList(questionJson);
//        System.out.println("aaa--->" + JSON.toJSONString(questionList));
        questionList.forEach(entity -> {
            //入库
            Question question = getQuestion(entity);
            Question ques = questionMapper.selectOne(Wrappers.<Question>lambdaQuery().eq(Question::getSid, question.getSid()));
            //判断库里是否有，是否是重复的题
            if (ques != null) {
                return;
            }
            questionMapper.insert(question);
            Long questionNumber = question.getQuestionNumber();
//            log.info("question入库成功：questionNumber=={}", question.getQuestionNumber());

            //保存json
            QuestionOriginalData questionOriginalData = new QuestionOriginalData();
            questionOriginalData.setQuestionNumber(questionNumber);
            questionOriginalData.setQuestionJson(entity.getJson());
            questionOriginalDataMapper.insert(questionOriginalData);

            //查询解析
            String analysisUrl = String.format("http://api.jyeoo.com/v1/%s/counter/QuesGet?id=%s"
                    , request.getSubject(), question.getSid());
            String analysisJson = getBody(analysisUrl, request.getToken());
            List<Ques> analysisList = getAnalysisList(analysisJson);
            question = questionMapper.selectOne(Wrappers.<Question>lambdaQuery().eq(Question::getQuestionNumber, questionNumber));
            Ques analysis = analysisList.get(0);
            setAnalysis(analysis, question);
            questionMapper.updateById(question);
            //原始数据
            questionOriginalData = questionOriginalDataMapper.selectOne(Wrappers.<QuestionOriginalData>lambdaQuery()
                    .eq(QuestionOriginalData::getQuestionNumber, questionNumber));
            questionOriginalData.setAnalysisJson(analysisJson);
            questionOriginalDataMapper.updateById(questionOriginalData);
            //选项 TODO 每个学科不一样
            if (entity.getCate().equals("1") || entity.getCate().equals("3")) {
                List<QuestionOption> optionList = getOptionList(analysis.getOptions(), analysis.getAnswers(), questionNumber);
                optionService.saveBatch(optionList);
            }
            //知识点
            List<Knowledge> knowledgeList = getKnowledgeList(analysis.getPoints(), questionNumber);
            if (!knowledgeList.isEmpty()) {
                knowledgeService.saveBatch(knowledgeList);
            }
            //教材章节
            BookChapter bookChapter = new BookChapter();
            bookChapter.setQuestionNumber(questionNumber);
            BeanUtils.copyProperties(request, bookChapter);
            bookChapterMapper.insert(bookChapter);
            log.info("试题入库成功：questionNumber=={}", question.getQuestionNumber());
        });

        return "succeed";
    }

    private String getBody(String questionUrl, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);//application/json
//        headers.set("authorization", "Token 3E0E8B7B692C6253FABD00EEC28701509CBF37AFD2647E25B56590869CF1E48A41F0C20266716783E3CAADD2B959ADE859E376E6BFDAF1677E8521CED9DC1B18A0D3B979D9CCE1CDB15F72C7797A359E10FCDDD2D84D9E2650F3873C731788E504FC58E72129B3F5314AFEAB00CB9EB538B275A269ECD3EB8A13ADB1BB674569F8E4BCF3D3F39CF106B39714DEC1F7FD");
        headers.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> quesListResponseEntity = restTemplate.exchange(questionUrl, HttpMethod.GET, entity, String.class);
        return quesListResponseEntity.getBody();
    }

    private List<Ques> getQuestionList(String body) {
        JSONObject jObject = JSON.parseObject(body);
        String JsonStr = jObject.get("Data").toString();
        List<String> stringList = JSONObject.parseArray(JsonStr, String.class);
        List<Ques> quesList = JSONObject.parseArray(JsonStr, Ques.class);
        for (int i = 0; i < quesList.size(); i++) {
            quesList.get(i).setJson(stringList.get(i));
        }
//        log.info("getQuestionList：{}", JSON.toJSONString(quesList));
        return quesList;
    }

    private List<Ques> getAnalysisList(String body) {
        List<Ques> quesList = JSONObject.parseArray(body, Ques.class);
//        log.info("getAnalysisList：{}", JSON.toJSONString(quesList));
        return quesList;
    }

    private Question getQuestion(Ques ques) {
        Question question = new Question();
        question.setQuestionNumber(SnowFlakeIDGenerator.nextNumber());
        log.info("SID=={}", ques.getSID());
        question.setSid(ques.getSID());
        question.setSubject(ques.getSubject());
        question.setSubjectName(SubjectEnum.getDesc(ques.getSubject()));
        question.setCate(ques.getCate());
        question.setCateName(ques.getCateName());
        question.setLabel(ques.getLabel());
        question.setContent(ques.getContent());
        question.setAnswers(ques.getAnswers().toString());
//        question.setMethod(ques.getMethod());
//        question.setAnalyse(ques.getAnalyse());
//        question.setDiscuss(ques.getDiscuss());
        question.setDegree(ques.getDegree());
        question.setDegreeName(getDegreeName(ques.getDegree()));
        return question;
    }

    private void setAnalysis(Ques ques, Question question) {
        question.setAnswers(ques.getAnswers().toString());
        question.setMethod(ques.getMethod());
        question.setAnalyse(ques.getAnalyse());
        question.setDiscuss(ques.getDiscuss());
    }

    private List<QuestionOption> getOptionList(List<String> options, List<String> answers, Long qusetionNumber) {
        List<QuestionOption> optionList = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            QuestionOption option = new QuestionOption();
            option.setQuestionNumber(qusetionNumber);
            option.setSerialNumber(i + 1);
            option.setContent(options.get(i));
            if (answers.contains(String.valueOf(i))) {
                option.setCorrectFlag(true);
            } else {
                option.setCorrectFlag(false);
            }
            optionList.add(option);
        }
        return optionList;
    }

    private List<Knowledge> getKnowledgeList(List<KeyValuePair<String, String>> points, Long questionNumber) {
        List<Knowledge> knowledgeList = new ArrayList<>();
        points.forEach(entity -> {
            Knowledge knowledge = new Knowledge();
            knowledge.setCode(entity.Key);
            knowledge.setName(entity.Value);
            knowledge.setQuestionNumber(questionNumber);
            knowledgeList.add(knowledge);
        });
        return knowledgeList;
    }

    //难度（0：不限；11：易（0.8到1.0）；12：较易（0.6到0.8）；13：中档（0.4到0.6）；14：较难：（0.2到0.4）；15：难：（0.0到0.2）（大于等于；小于））
    private String getDegreeName(String degree) {
        float d = Float.valueOf(degree);
        String degreeName = "";
        if (d < 0.2) {
            degreeName = "难";
        } else if (d < 0.4) {
            degreeName = "较难";
        } else if (d < 0.6) {
            degreeName = "中档";
        } else if (d < 0.8) {
            degreeName = "较易";
        } else if (d < 1.0) {
            degreeName = "易";
        } else {
            degreeName = "未知";
        }
        return degreeName;
    }

    /**
     * 保存章节教材
     *
     * @return
     */
    @PostMapping("/book/save")
    public String saveBook() {
        //获取文件
        StringBuilder stringBuilder = getFeil();
        //转json
        JSONArray jsonArray = JSONArray.parseArray(stringBuilder.toString());

        //循环转实体   需要一个表
        int i = 0;
        List<BaseBookChapter> list = new ArrayList<>();
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(object);
            Map<String, String> map = new HashMap<>();

            JSONArray jsonArray1 = (JSONArray)jsonObject.get("Children");
            if (jsonArray1.size() == 0){
                addList(jsonObject, map, list);
            }
            for (Object object1 : jsonArray1){
                JSONObject jsonObject1 = (JSONObject) JSON.toJSON(object1);
                putMap(map, jsonObject1, 1);

                JSONArray jsonArray2 = (JSONArray)jsonObject1.get("Children");
                if (jsonArray2.size() == 0){
                    addList(jsonObject, map, list);
                }
                for (Object object2 : jsonArray2){
                    JSONObject jsonObject2 = (JSONObject) JSON.toJSON(object2);
                    putMap(map, jsonObject2, 2);

                    JSONArray jsonArray3 = (JSONArray)jsonObject2.get("Children");
                    if (jsonArray3.size() == 0){
                        addList(jsonObject, map, list);
                    }
                    for (Object object3 : jsonArray3) {
                        JSONObject jsonObject3 = (JSONObject) JSON.toJSON(object3);
                        putMap(map, jsonObject3, 3);

                        addList(jsonObject, map, list);
                    }
                }
            }
        }

        //保存
        baseBookChepterService.saveBatch(list);
        return null;
    }

    private StringBuilder getFeil(){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String filePath = "D:/章节教材.txt";
            FileInputStream fin = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fin);
            BufferedReader buffReader = new BufferedReader(reader);
            String strTmp = "";
            while ((strTmp = buffReader.readLine()) != null) {
                stringBuilder.append(strTmp);
//                System.out.println(strTmp);
            }
            buffReader.close();
        } catch (Exception e) {
            log.info("文件获取失败！", e);
        }
        return stringBuilder;
    }

    private void addList(JSONObject jsonObject, Map<String, String> map, List<BaseBookChapter> list){
        BaseBookChapter baseBookChepter = new BaseBookChapter();
        baseBookChepter.setSubject("32");
        baseBookChepter.setSubjectName("高中化学");
        baseBookChepter.setBook((String) jsonObject.get("ID"));
        baseBookChepter.setBookName((String) jsonObject.get("Name"));
        baseBookChepter.setEditionName((String) jsonObject.get("EditionName"));
        baseBookChepter.setGradeName((String) jsonObject.get("GradeName"));
        baseBookChepter.setTermName((String) jsonObject.get("TermName"));
        baseBookChepter.setTypeName((String) jsonObject.get("TypeName"));

        baseBookChepter.setChapter1(map.get("chapter1"));
        baseBookChepter.setChapterName1(map.get("chapterName1"));
        baseBookChepter.setChapter2(map.get("chapter2"));
        baseBookChepter.setChapterName2(map.get("chapterName2"));
        baseBookChepter.setChapter3(map.get("chapter3"));
        baseBookChepter.setChapterName3(map.get("chapterName3"));
        list.add(baseBookChepter);
    }

    private void putMap(Map<String, String> map, JSONObject jsonObject, int i){
        map.put("chapter" + i, (String)jsonObject.get("ID"));
        map.put("chapterName" + i, (String)jsonObject.get("Name"));
    }
}
