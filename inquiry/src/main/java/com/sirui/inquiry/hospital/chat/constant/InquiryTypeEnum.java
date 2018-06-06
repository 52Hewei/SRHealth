package com.sirui.inquiry.hospital.chat.constant;

import java.io.Serializable;

/**
 * 问诊类型枚举
 * Created by xiepc on 2017/3/16 20:05
 */

public enum InquiryTypeEnum implements Serializable {

    typeText("1"),
    typeVideo("2");

    private String value;

    InquiryTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
