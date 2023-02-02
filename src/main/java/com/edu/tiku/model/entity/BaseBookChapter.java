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
 * 教材章节目录
 * </p>
 *
 * @author dyh
 * @since 2023-02-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BaseBookChapter对象", description="教材章节目录")
public class BaseBookChapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "学科code")
    private String subject;

    @ApiModelProperty(value = "学科name")
    private String subjectName;

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

    @ApiModelProperty(value = "一级章节code")
    private String chapter1;

    @ApiModelProperty(value = "一级章节name")
    private String chapterName1;

    @ApiModelProperty(value = "二级章节code")
    private String chapter2;

    @ApiModelProperty(value = "二级章节name")
    private String chapterName2;

    @ApiModelProperty(value = "三级章节code")
    private String chapter3;

    @ApiModelProperty(value = "三级章节name")
    private String chapterName3;


}
