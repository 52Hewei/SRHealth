package com.net.client.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.http.R;


/**
 * Created by xiepc on  2017-09-16 21:59
 */

public class LoadingDialog extends DialogFragment {

    private TextView tvLoading;

    private String msg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.loading_dialog);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading,null);
        tvLoading = (TextView) view.findViewById(R.id.tv_loading);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();//获取Dialog
        if(dialog == null){
            return;
        }
        WindowManager.LayoutParams attr = dialog.getWindow().getAttributes();//获取Dialog属性
        WindowManager wm= (WindowManager) dialog.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetric=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetric);
        attr.width= (int) (outMetric.widthPixels*0.618f);
        dialog.getWindow().setAttributes(attr);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(msg)){
            tvLoading.setText(msg);
        }
    }

    public void setMessage(String msg){
        this.msg = msg;
        if(tvLoading != null){
            tvLoading.setText(msg);
        }
    }
}
