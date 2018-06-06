package com.sirui.inquiry.hospital.ui.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.sirui.basiclib.BaseActivity;
import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.ui.fragment.InquiryRecordFragment;

public class InquiryRecordActivity extends BaseActivity implements InquiryRecordFragment.BackPress{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inquiry_record;
    }
    @Override
    protected void iniData() {

    }

    @Override
    protected void iniView() {
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.container,new InquiryRecordFragment(this)).commit();
    }

    @Override
    public void back() {
        onBackPressed();
    }
}
