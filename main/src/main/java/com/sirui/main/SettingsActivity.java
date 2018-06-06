package com.sirui.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.data.AccountManager;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.basiclib.widget.MyToast;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.ILoginProvider;
import com.zhy.changeskin.SkinManager;
import com.zhy.changeskin.callback.ISkinChangingCallback;

import java.io.File;

import butterknife.OnClick;

/**
 * 设置界面
 */
public class SettingsActivity extends BaseActivity {

    private String mSkinPkgPath = Environment.getExternalStorageDirectory() + File.separator + "res.skin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void iniData() {
    }

    @Override
    protected void iniView() {
        initTitle(getString(R.string.setting));
    }

    @OnClick({R2.id.rl_alter_pwd,
            R2.id.rl_clear_cache,
            R2.id.rl_about,
            R2.id.rl_pay,
            R2.id.btn_logout,
            R2.id.rl_change_theme})
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.rl_alter_pwd){

        }else if(id == R.id.rl_pay){
//            IPayProvider provider = (IPayProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_PAY_SERVICE).navigation();
//            if(provider != null){
//                provider.goToPay(SettingsActivity.this);
//            }
        }else if(id == R.id.rl_clear_cache) {
            DialogUtil.both(this, "确定需要清除缓存？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyToast.show("清除成功!");
                }
            });
        }else if(id == R.id.rl_change_theme){
            changeTheme();
        }else if(id == R.id.rl_about){
            AboutUsActivity.goToAbout(this);
        }else if(id == R.id.btn_logout){
            DialogUtil.both(this, "确定退出当前账号？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // exitLogin();
                    AccountManager.getInstance().loginOutMember();
                    ILoginProvider provider = (ILoginProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_LOGIN_SERVICE).navigation();
                    if(provider != null){
                        provider.goToLogin(SettingsActivity.this);
                    }
                }
            });
        }
    }

    private void changeTheme(){
        String[] items = new String[]{"更改主题","恢复主题"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("换肤");
        //设置图标
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 if(i == 0){
                     changColor();
                 }else if(i == 1){
                     SkinManager.getInstance().removeAnySkin();
                 }
            }
        });
        builder.create();
        builder.show();
    }

    private void changColor(){
        File file = new File(mSkinPkgPath);
        if(!file.exists()){
             MyToast.show("没有找到皮肤包");
            return;
        }
        SkinManager.getInstance().changeSkin(
                mSkinPkgPath,
                "com.sirui.plugin",
                new ISkinChangingCallback()
                {
                    @Override
                    public void onStart()
                    {
                    }
                    @Override
                    public void onError(Exception e)
                    {
                        MyToast.show("换肤失败");
                    }
                    @Override
                    public void onComplete()
                    {
                        MyToast.show("换肤成功");
                    }
                });
    }

//    private void goToLoginActivity(){
//
//        ActivityUtils.finishAllActivityExcept(this);//先把其他Activity清除掉
//        Intent intent = new Intent(this,LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }

//    /**
//     * 退出登陆
//     */
//    private void exitLogin() {
//        HttpRequest request = HttpGo.get(NetUrl.URL_LOGIN_OUT);
//        request.params("clientType", "5");
//        request.params("mobileNo", AccountManager.getInstance().getAccount());
//        request.execute(new HttpDialogListener(this) {
//            @Override
//            public void onSuccess(String s) {
//
//                JSONObject obj = JsonUtil.checkResult(s);
//                if (obj != null) {
//                    SRToast.show("退出登录成功");
//                    SPUtil.putBoolean(SRConstant.NEED_LOGIN_APP, true);
//                    LoginActivity.start(SettingsActivity.this);
//                }
//            }
//
//        });
//    }

//    /**
//     * 请求协议数据
//     */
//    private void requestAgreement() {
//        HttpRequest request = HttpGo.post(NetUrl.URL_GET_AGREEMENT_INFO);
//        request.params("agreementType", "10");//医师登录
//        request.execute(new HttpListener() {
//
//            @Override
//            public void onSuccess(String s) {
//                AgreementBean agreementBean = JsonUtil.checkToGetData(s, AgreementBean.class);
//                if (agreementBean != null) {
//                    String url = agreementBean.getAgreementContent();
//                    if (TextUtils.isEmpty(url)) {
//                        SRToast.show("协议地址为空");
//                        return;
//                    }
//                    SPUtil.putBoolean(SRConstant.IS_AGREE_AGREEMENT, true);
//                    Intent intent = new Intent(SettingsActivity.this, AgreementActivity.class);
//                    intent.putExtra("agreementUrl", url);
//                    startActivity(intent);
//                } else {
//                    SRToast.show("获取协议失败");
//                }
//            }
//        });
//    }

}
