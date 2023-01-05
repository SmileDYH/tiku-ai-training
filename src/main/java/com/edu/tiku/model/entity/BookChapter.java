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
 * 教材章节表
 * </p>
 *
 * @author dyh
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BookChapter对象", description="教材章节表")
public class BookChapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "题号")
    private Long questionNumber;

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


}
