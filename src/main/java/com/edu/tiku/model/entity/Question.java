package com.edu.tiku.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 试题表
 * </p>
 *
 * @author dyh
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Question对象", description="试题表")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "题号")
    private Long questionNumber;

    @ApiModelProperty(value = "试题标识（菁优网）")
    private String sid;

    @ApiModelProperty(value = "学科code")
    private String subject;

    @ApiModelProperty(value = "学科name")
    private String subjectName;

    @ApiModelProperty(value = "题型code")
    private String cate;

    @ApiModelProperty(value = "题型name")
    private String cateName;

    @ApiModelProperty(value = "试题来源")
    private String label;

    @ApiModelProperty(value = "题干")
    private String content;

    @ApiModelProperty(value = "答案")
    private String answers;

    @ApiModelProperty(value = "解答")
    private String method;

    @ApiModelProperty(value = "分析")
    private String analyse;

    @ApiModelProperty(value = "点评")
    private String discuss;

    @ApiModelProperty(value = "难度code")
    private String degree;

    @ApiModelProperty(value = "难度name")
    private String degreeName;


}
