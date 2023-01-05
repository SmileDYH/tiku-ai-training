package com.edu.tiku.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description:
 * author: dyh
 * date: 2023/1/4 17:44
 */
@Data
public class JywQueryQuestionRequest {
    //subject、p1、p2、dg、ct、pi 必传
    public String subject;//学科英文名称（参考获取学科接口）；
    public String tp="1";//类别（1;//教材；2;//考点）；
    public String p1;//教材标识（tp=2时传0或空）；
    public String p2;//教材下的章节标识（tp=2时传0或空）；
    public String p3="";//考点编号（多个以逗号分隔）；
    public String dg;//难度（0;//不限；11;//易（0.8到1.0）；12;//较易（0.6到0.8）；13;//中档（0.4到0.6）；14;//较难;//（0.2到0.4）；15;//难;//（0.0到0.2）（大于等于；小于））
    public String ct;//题型（0;//全部；1;//选择题；2;//填空题；9;//解答题等，具体获取题型API，多个以逗号分隔）；
    public String sc="False";//真题集（True;//真题；False;//不限）；
    public String gc="False";//好题集（True;//好题；False;//不限）；
    public String rc="False";//常考题（True;//常考题；False;//不限）；
    public String yc="False";//压轴题（True;//压轴题；False;//不限）；
    public String ec="False";//易错题（True;//易错题；False;//不限）；
    public String er="False";//用户错题（True;//用户错题；False;//不限）；
    public String rg="";//地区编码（参考获取地区API，“空”不限地区）[可选]；
    public String so="0";//试题来源（参考获取试题来源API，“空”或“0”不限来源，多个以逗号分隔）[可选]；
    public String yr="0";//试题年份（“空”或“0”不限年份，多个以逗号分隔）[可选]；
    public String po="0";//0;//综合排序；1;//组卷次数；2;//真题次数；3;//试题难度（默认0）[可选]；
    public String pd="1";//0;//升序；1;//降序（默认1）[可选]；
    public String pi;//当前页（以1开始）；
    public String ps="10";//每页记录数（小于等于10）;
    public String onlyNos="0";//（默认0）[可选]


    @ApiModelProperty(value = "教材code")
    private String book;

    @ApiModelProperty(value = "教材name")
    private String bookName;

    @ApiModelProperty(value = "版本name")
    private String editionName;

    @ApiModelProperty(value = "年级name")
    private String gradeName;

    @ApiModelProperty(value = "学期name")
    private String termName;

    @ApiModelProperty(value = "类型name")
    private String typeName;

    @ApiModelProperty(value = "章节code")
    private String chapter;

    @ApiModelProperty(value = "章节name")
    private String chapterName;

    private String token;
}
