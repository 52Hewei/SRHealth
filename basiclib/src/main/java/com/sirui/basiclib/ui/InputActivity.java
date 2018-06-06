package com.sirui.basiclib.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.R;
import com.sirui.basiclib.R2;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.utils.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class InputActivity extends BaseActivity {

     @BindView(R2.id.et_content)
     EditText etContent;
     private String title;
     private String content;

    public static void start(Activity context,int requestCode, String title, String content){
        Intent intent = new Intent(context,InputActivity.class);
        intent.putExtra("title",title);
        intent.putExtra(SRConstant.CONTENT_KEY,content);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_input;
    }

    @Override
    protected void iniData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra(SRConstant.CONTENT_KEY);
    }
    @Override
    protected void iniView() {
        if(TextUtils.isEmpty(title)){
            initTitle("输入内容");
        }else{
            initTitle(title);
        }
       if(!TextUtils.isEmpty(content)){
           etContent.setText(content);
           ViewUtil.setSelectionEnd(etContent);
       }
    }

    @Override
    protected void initTitle(String title) {
        super.initTitle(title);
        tvRightButton.setVisibility(View.VISIBLE);
        tvRightButton.setText(getString(R.string.confirm));
    }

    @OnClick({R2.id.tv_right_button})
    public void onClicked(View view){
         if(view.getId() == R.id.tv_right_button){
               String content = etContent.getText().toString().trim();
               Intent intent = new Intent();
               intent.putExtra(SRConstant.CONTENT_KEY,content);
               setResult(RESULT_OK,intent);
               finish();
         }
    }
}
