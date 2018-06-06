package com.sirui.inquiry.hospital.widgets;

import android.widget.TextView;

public class RObject {

    private String objectRule = ";";// 匹配规则
    private String objectText;// 高亮文本
    private TextView disease;

    public String getObjectRule() {
        return objectRule;
    }

    public void setObjectRule(String objectRule) {
        this.objectRule = objectRule;
    }

    public String getObjectText() {
        return objectText;
    }

    public void setObjectText(String objectText) {
        this.objectText = objectText;
    }

    @Override
    public String toString() {
        return objectText + objectRule;
    }

    public TextView getDisease() {
        return disease;
    }

    public void setDisease(TextView disease) {
        this.disease = disease;
    }
}
