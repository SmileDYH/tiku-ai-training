package com.edu.tiku.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description:
 * author: dyh
 * date: 2023/1/5 15:53
 */
@AllArgsConstructor
@Getter
public enum SubjectEnum {

    JUNIOR_CHEMISTRY("22","初中化学"),
    SENIOR_CHEMISTRY("32","高中化学");

    /**
     * 状态码
     */
    private String code;
    /**
     * 状态描述
     */
    private String desc;

    public static String getDesc(String code) {
        for (SubjectEnum examOperateTypeEnum : values()) {
            if (examOperateTypeEnum.getCode().equals(code)){
                return examOperateTypeEnum.getDesc();
            }
        }
        return null;
    }
}
