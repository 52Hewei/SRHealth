package com.sirui.inquiry.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sirui.inquiry.hospital.ui.activity.DoctorDetailPhoneActivity;
import com.sirui.inquiry.hospital.ui.activity.InquiryRecordActivity;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IInquiryProvider;

/**
 * author: hewei
 * created on: 2018/4/8 11:07
 * description:
 */
@Route(path = RouterPath.ROUTER_PATH_TO_INQUIRY_SERVICE, name = "问诊界面")
public class InquiryService implements IInquiryProvider {

    @Override
    public void goToInquiry(Activity activity) {
        DoctorDetailPhoneActivity.quickStart(activity, "全科", "1", "");
    }

    @Override
    public void goToInquiryRecord(Activity activity) {
        Intent intent = new Intent(activity, InquiryRecordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void init(Context context) {

    }
}
