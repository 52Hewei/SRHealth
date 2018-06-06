package com.sirui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.utils.VersionUtils;

import butterknife.BindView;

public class AboutUsActivity extends BaseActivity {

    @BindView(R2.id.versionText)
    TextView versionText;
    @BindView(R2.id.appName)
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    public static void goToAbout(Activity activity){
        Intent intent = new Intent(activity , AboutUsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void iniData() {
    }

    @Override
    protected void iniView() {
        initTitle("关于我们");
        String localVersionName = VersionUtils.getVersionName(this);
        String app = VersionUtils.getAppName(this);
//        appName.setText(app);
        versionText.setText(localVersionName);
    }
}
