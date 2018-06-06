package com.sirui.basiclib.widget.upgrade;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sirui.basiclib.R;


/**
 * @author hw
 *         Created on 2018/4/12.
 */

public class UpgradeDialog extends Dialog implements View.OnClickListener {

    private ImageView btnClose;
    private TextView tvUpgradeContent;
    private Button btUpgradeConfirm;
    private NumberProgressBar numberProgressBar;

    private UpgradeBean.Upgrade mUpgrade;

    private Activity mContext;

    int i =1;

    public UpgradeDialog(Activity context, UpgradeBean.Upgrade upgrade) {
        super(context);
        mContext = context;
        mUpgrade = upgrade;
        init(context);
    }

    private void init(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_upgrade, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(contentView);
        setCancelable(false);
        WindowManager.LayoutParams wlp;
        if (getWindow() != null) {
            wlp = getWindow().getAttributes();
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(wlp);
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        btnClose = (ImageView) contentView.findViewById(R.id.upgrade_cancle);
//        if (mUpgrade.getRemark1().equals("Y"))
//        btnClose.setVisibility(View.GONE);
        tvUpgradeContent = (TextView) contentView.findViewById(R.id.tv_upgrade_content);
        btUpgradeConfirm = (Button) contentView.findViewById(R.id.bt_upgrade_confirm);
        numberProgressBar = contentView.findViewById(R.id.npb);

//        String content = "当前版本号是a，最新版本是b，请升级到最新的版本";

//        tvUpgradeContent.setText(content.replace("a", VersionUtils.getVersionCode(context)+"").replace("b",mUpgrade.getVersionCompareNo()));
        btUpgradeConfirm.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    public void setProgress(int progress){
        numberProgressBar.setProgress(progress);
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.bt_upgrade_confirm){
            //                DownloadAppUtils.downloadForAutoInstall(mContext, mUpgrade.getVersionUrl(), "doctor.apk", "");
            btUpgradeConfirm.setBackgroundResource(R.color.transparent);
            btUpgradeConfirm.setTextColor(ContextCompat.getColor(mContext, R.color.gray_text));
            btUpgradeConfirm.setText("正在更新...");
            btUpgradeConfirm.setEnabled(false);
            btUpgradeConfirm.setVisibility(View.GONE);
            numberProgressBar.setVisibility(View.VISIBLE);
            new DownloadManager().downapk(mContext , this);
        }else if (resId == R.id.upgrade_cancle){
            dismiss();
        }
    }
}
