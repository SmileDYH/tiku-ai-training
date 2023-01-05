package com.edu.tiku.model.entity.jyw;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/// <summary>
/// 试题类
/// </summary>
@Data
public class Ques {
    /// <summary>
    /// 试题学科
    /// </summary>
    public String Subject;

    /// <summary>
    /// 试题标识 -- ID 之前是UUID类型
    /// </summary>
    //public UUID ID;
    public String ID;

    /// 试题标识 --- 用于替换之前的试题标识 ID
    /// </summary>
    public String SID;

    /// <summary>
    /// 题干
    /// </summary>
    public String Content;

    /// <summary>
    /// 解答
    /// </summary>
    public String Method;

    /// <summary>
    /// 分析
    /// </summary>
    public String Analyse;

    /// <summary>
    /// 点评
    /// </summary>
    public String Discuss;

    /// <summary>
    /// 难度（范围：0.1到1.0，由难到易）
    /// </summary>
    public String Degree;

    /// <summary>
    /// 题型（1：选择题；2：填空题；9：解答题等，参考获取题型接口）
    /// </summary>
    public String Cate;

    /// <summary>
    /// 试题来源（例如“2012?重庆”）
    /// </summary>
    public String Label;

    /// <summary>
    /// 来源试卷
    /// </summary>
    public String LabelReportID;

    /// <summary>
    /// 答题老师
    /// </summary>
    public String Teacher;

    /// <summary>
    /// 题型名称
    /// </summary>
    public String CateName;

    /// <summary>
    /// 考点
    /// </summary>
    public List<KeyValuePair<String, String>> Points;

    /// <summary>
    /// 专题
    /// </summary>
    public List<KeyValuePair<String, String>> Topics;

    /// <summary>
    /// 选项
    /// </summary>
    public List<String> Options;

    /// <summary>
    /// 收藏次数
    /// </summary>
    public int FavTime;

    /// <summary>
    /// 浏览次数
    /// </summary>
    public int ViewCount;

    /// <summary>
    /// 下载次数
    /// </summary>
    public int DownCount;

    /// <summary>
    /// 真题系数
    /// </summary>
    public int RealCount;

    /// <summary>
    /// 组卷次数
    /// </summary>
    public int PaperCount;

    /// <summary>
    /// 标准答案（选择题为以0开始的选项序号，其他题型为标准答案文本；选择题以外的题型，标准答案对应于题干中的“<div class="quizPutTag">&nbsp;</div>”）
    /// </summary>
    public List<String> Answers;

    /// <summary>
    /// 题号
    /// </summary>
    public int Seq;

    /// <summary>
    /// 分值
    /// </summary>
    public float Score;

    /// <summary>
    /// 学生答案
    /// </summary>
    public List<String> UserAnswers;

    /// <summary>
    /// 答案得分（0：错误；1：正确；2：未判）
    /// </summary>
    public int[] UserScores;

    /// <summary>
    /// 试题文件
    /// </summary>
    public List<QuesFile> QuesFiles;

    /// <summary>
    /// 试题子题（只返回子母题关系）
    /// </summary>
    public List<QuesChild> QuesChilds;

    /// <summary>
    /// 父题标识
    /// </summary>
    public UUID ParentID;

    /// <summary>
    /// 父题内容
    /// </summary>
    public String ParentContent;

    public String json;
}
