package com.sirui.basiclib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.net.client.HttpGo;
import com.sirui.basiclib.utils.ActivityUtils;

import butterknife.ButterKnife;

/**
 * Created by xiepc on 2018/3/28 11:30
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        iniData();
        iniView();
        ActivityUtils.addActivity(this);
    }

    protected abstract int getLayoutId();
    /**初始化数据*/
    protected abstract void iniData();
    /**初始化视图*/
    protected abstract void iniView();


    protected TextView tvRightButton;
    protected TextView tvLeftBack;

    protected void initTitle(String title){
        TextView tvTitle = findViewById(R.id.tv_title);
        tvLeftBack = findViewById(R.id.tv_left_back);
        tvRightButton = findViewById(R.id.tv_right_button);
        //ImageView ivActionMoreMenu = findViewById(R.id.iv_action_more_menu);
        tvTitle.setText(title);
        tvLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpGo.cancelByTag(this);
        ActivityUtils.removeActivity(this);
    }
}
