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
 * 选项表
 * </p>
 *
 * @author dyh
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="QuestionOption", description="选项表")
public class QuestionOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "题号")
    private Long questionNumber;

    @ApiModelProperty(value = "选项内容")
    private String content;

    @ApiModelProperty(value = "是否正确答案(0否1是)")
    private Boolean correctFlag;

    @ApiModelProperty(value = "序号")
    private Integer serialNumber;


}
