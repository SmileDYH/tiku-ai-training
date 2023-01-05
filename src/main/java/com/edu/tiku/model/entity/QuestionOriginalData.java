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
 * 试题原始数据
 * </p>
 *
 * @author dyh
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="QuestionOriginalData对象", description="试题原始数据")
public class QuestionOriginalData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "题号")
    private Long questionNumber;

    @ApiModelProperty(value = "试题原始json")
    private String questionJson;

    @ApiModelProperty(value = "解析原始json")
    private String analysisJson;


}
