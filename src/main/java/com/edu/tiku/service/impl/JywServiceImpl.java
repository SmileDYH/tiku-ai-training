package com.edu.tiku.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.edu.tiku.common.utils.DES3;
import com.edu.tiku.model.request.JywLoginRequest;
import com.edu.tiku.model.request.JywRegisterRequest;
import com.edu.tiku.service.JywService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * description:
 * author: dyh
 * date: 2023/2/1 16:01
 */
@Service
@Slf4j
public class JywServiceImpl implements JywService {

    @Override
    public String register(JywRegisterRequest request){
        try {
            //菁优网注册
//            JSONObject jo = new JSONObject();
//            jo.put("ApiID", "630ea04a-5dc6-4dd0-9bb9-e9806255dda3");
//            jo.put("ApiPwd", "cd7cc8");
//            jo.put("School", "zhixue");
//            jo.put("UserID", "18600358957");
//            jo.put("UserPwd", "e86beb");
//            jo.put("UserName", "zkhy");
//            jo.put("UserRole", "1");
//            jo.put("UserSex", "1");
            JSONObject jo = new JSONObject();
            jo.put("ApiID", request.getApiID());
            jo.put("ApiPwd", request.getApiPwd());
            jo.put("School", request.getSchool());
            jo.put("UserID", request.getUserID());
            jo.put("UserPwd", request.getUserPwd());
            jo.put("UserName", request.getUserName());
            jo.put("UserRole", request.getUserRole());
            jo.put("UserSex", request.getUserSex());
            String json = jo.toString();

            String encryptJson = DES3.encrypt(json, request.getApiKey());
            String url = String.format("http://api.jyeoo.com/v1/register?id=%s&v=%s", request.getApiID(), encryptJson);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, null, String.class);
            log.info("菁优网注册：code=={},massage=={},body=={}",
                    responseEntity.getStatusCode(), responseEntity.getStatusCodeValue(), responseEntity.getBody());
//            return "注册成功：userKey == " + responseEntity.getBody();
            return responseEntity.getBody();
        } catch (Exception e) {
            log.info("菁优网注册异常！", e);
        }
        return null;
    }

    @Override
    public String login(JywLoginRequest request){
        try {
            //菁优网登录
            String s = String.format("%s#@@#%s#@@#%s", request.getApiID(), request.getUserID(), request.getUserPwd());
            String v = DES3.encrypt(s, request.getApiKey());
            System.out.println("s==" + s);
            System.out.println("v==" + v);

            String url = String.format("http://api.jyeoo.com/v1/user?id=%s&v=%s", request.getApiID(), v);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, null, String.class);
            log.info("菁优网登录：code=={},massage=={},body=={}",
                    responseEntity.getStatusCode(), responseEntity.getStatusCodeValue(), responseEntity.getBody());
//            return "登录成功：token == " + responseEntity.getBody();
            return responseEntity.getBody();
        } catch (Exception e) {
            log.info("菁优网登录异常！", e);
        }
        return null;
    }
}
