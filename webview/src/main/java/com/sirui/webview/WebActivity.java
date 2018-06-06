package com.sirui.webview;


import android.content.Intent;

import com.sirui.basiclib.config.SRConstant;

public class WebActivity extends BaseWebActivity {

    @Override
    public String getUrl() {
         Intent intent = getIntent();
         if(intent != null){
             String url =  intent.getStringExtra(SRConstant.URL_WEB);
             return url;
         }
        return "";
    }
}
