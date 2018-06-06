package com.sirui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lzy.okgo.model.HttpParams;
import com.net.client.HttpDialogListener;
import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.net.client.HttpRequest;
import com.net.client.event.EventType;
import com.net.client.event.MainEvent;
import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.config.NetUrl;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.data.bean.User;
import com.sirui.basiclib.http.HttpKeyManager;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.utils.ActivityUtils;
import com.sirui.basiclib.utils.DevicesInfoUtils;
import com.sirui.basiclib.utils.KeyboardUtils;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.utils.RegexUtil;
import com.sirui.basiclib.utils.SPUtil;
import com.sirui.basiclib.utils.TimeTextViewCount;
import com.sirui.basiclib.utils.ViewUtil;
import com.sirui.basiclib.utils.string.StringUtil;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.basiclib.widget.MyToast;
import com.sirui.login.bean.AgreementBean;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IMainProvider;
import com.sirui.router.provider.IUserGuideProvider;
import com.sirui.router.provider.IWebProvider;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R2.id.tv_service_address)
    TextView tvServiceAddress;
    @BindView(R2.id.verification_code_login)
    TextView verificationCodeLogin;
    @BindView(R2.id.password_login)
    TextView passwordLogin;
    @BindView(R2.id.login_delete_user_btn)
    ImageView loginDeleteUserBtn;
    @BindView(R2.id.login_user_edit)
    EditText loginUserEdit;
    @BindView(R2.id.get_verification)
    TextView getVerification;
    @BindView(R2.id.parting_line)
    View partingLine;
    @BindView(R2.id.login_verification_code)
    EditText loginVerificationCode;
    @BindView(R2.id.login_forget_pwd)
    TextView loginForgetPwd;
    @BindView(R2.id.login_button)
    Button loginButton;
    @BindView(R2.id.agreement_layout)
    LinearLayout agreementLayout;
    private TimeTextViewCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void iniData() {
        getPublicKey();

        ActivityUtils.finishAllActivityExcept(this); //关掉除登录以外的所有界面
    }

    @Override
    protected void iniView() {
        loginUserEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    loginDeleteUserBtn.setVisibility(View.INVISIBLE);
                } else {
                    loginDeleteUserBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        loginUserEdit.setText(SPUtil.getString(SRConstant.SP_USER_PHONE));
        ViewUtil.setSelectionEnd(loginUserEdit);
    }

    @OnClick({R2.id.get_verification , R2.id.login_button , R2.id.login_delete_user_btn , R2.id.agreement_layout})
    public void onBtnClicked(View view) {
        int id = view.getId();
        if (id == R.id.get_verification){
            checkToGetVCode();
        }else if (id == R.id.login_button){
            login();
        }else if (id == R.id.login_delete_user_btn){
            loginUserEdit.setText("");
        }else if (id == R.id.agreement_layout){
            requestAgreement();
        }
    }

    /**
     * 请求协议数据
     */
    private void requestAgreement() {
        HttpRequest request = HttpGo.post(NetUrl.GET_AGREEMENT_INFO);
        request.params("agreementType", "12");//药云医
        request.execute(new HttpListener() {

            @Override
            public void onStart() {
                agreementLayout.setEnabled(false);
                super.onStart();
            }

            @Override
            public void onSuccess(String s) {
                agreementLayout.setEnabled(true);
                AgreementBean agreementBean = JsonUtil.checkToGetData(s, AgreementBean.class);
                if (agreementBean == null)
                    return;

                enterDeal(agreementBean.getAgreementContent());
            }

            @Override
            public void onFailure(Response response, Exception e) {
                agreementLayout.setEnabled(true);
                super.onFailure(response, e);
            }
        });
    }

    private void enterDeal(String url) {
//        Intent intent = new Intent(LoginActivity.this, AgreementActivity.class);
//        intent.putExtra("agreementUrl", url);
//        startActivity(intent);
        IWebProvider provider = (IWebProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_WEB_SERVICE).navigation();
        if(provider != null){
            provider.goToWeb(this,url);
        }
    }

    private void getPublicKey() {
        HttpKeyManager.getInstance().getRsaKey(this, new HttpKeyManager.OnGetRsaKeyListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "获得公钥成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(LoginActivity.this, "获得公钥失败", Toast.LENGTH_SHORT).show();
            }
        });
        HttpKeyManager.getInstance().getRsaKey2();
    }

    /**
     * 登录
     */
    private void login() {
        ViewUtil.setPostEnableClick(loginButton);
        KeyboardUtils.hideSoftInput(this); //隐藏键盘
        String phone = StringUtil.removeBlanks(loginUserEdit.getText().toString().trim());
        String vcode = StringUtil.removeBlanks(loginVerificationCode.getText().toString().trim());
        if (TextUtils.isEmpty(phone) || !RegexUtil.isMobileNumber(phone)) {
            MyToast.show("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(vcode)) {
            MyToast.show("请输入验证码");
            return;
        }
        vipLogin(phone, vcode);  //登录
    }

    /**
     * 检验输入并获取验证码
     */
    private void checkToGetVCode() {
        String phone = StringUtil.removeBlanks(loginUserEdit.getText().toString().trim());
        if (!TextUtils.isEmpty(phone) && RegexUtil.isMobileNumber(phone)) {
            getVcode(phone);
        } else {
            MyToast.show("请输入正确的手机号");
        }
    }

    private void getVcode(String phone) {
        HttpGo.get(NetUrl.VCODE)
                .params("mobileNo", phone)
                .params("type", "1") //患者登录
                .execute(new HttpListener() {

                    @Override
                    public void onStart() {//灰色
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(String s) {
                        JSONObject obj = JsonUtil.checkResult(s);
                        if (obj != null) {
                            MyToast.show("验证码已发送,请注意查收!");
                            verificationCodeTimer();
                        }
                    }

                    @Override
                    public void onFailure(Response response, Exception e) {
                        super.onFailure(response, e);
                    }
                });
    }

    /**
     * 验证码的有效时间计时器
     */
    private void verificationCodeTimer() {
        timeCount = new TimeTextViewCount(60 * 1000, 1000, getVerification);
        timeCount.start();
    }

    private void vipLogin(String phone, String vcode) {
        HttpGo.post(NetUrl.VIPURL)
                .params("mobile", phone)
                .params("clientType", "1")
                .params("storeId", SRConstant.STORE_ID)
                .params("eid", SRConstant.E_ID)
                .params("smsCode", vcode)
                .params("appId", SRConstant.APP_ID)
                .execute(new HttpDialogListener(this) {
                    @Override
                    public void onSuccess(String s) {
                        parseLoginResult(s);
                    }
                    @Override
                    public void onFailure(Response response, Exception e) {
                        DialogUtil.confirm(LoginActivity.this, "登陆失败", null);
                        super.onFailure(response, e);
                    }
                });
    }

    private void addCommonParams() {
        HttpParams params = new HttpParams();
        params.put("token", DataManager.getInstance().getUser().getToken());
        params.put("deviceType", "1");
        params.put("deviceId", "");
        params.put("deviceName", DevicesInfoUtils.getPhoneModel());
        params.put("requestIp", "");
        params.put("appId", SRConstant.APP_ID);
        HttpGo.addCommonParams(params);
    }

    private void enterUserGuide() {
        IUserGuideProvider guideService = (IUserGuideProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_USERGUIDE_SERVICE).navigation();
        if(guideService != null){
            guideService.goUserGuide(this);
        }
        finish();
    }

    private void enterMain() {
        IMainProvider mainService = (IMainProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_MAIN_SERVICE).navigation();
        if (mainService != null) {
            mainService.goMain(this);
        }
    }

    /**
     * 登录结果处理
     */
    private void parseLoginResult(String result) {
        User user = JsonUtil.checkToGetData(result, User.class);
        if (user == null) {
            return;
        }
        EventBus.getDefault().post(new MainEvent(EventType.LOGIN_SUCCESS));
        MyToast.show("登录成功");
        SPUtil.putBoolean(SRConstant.NEED_LOGIN_APP, false);
        if (SPUtil.getBoolean(SRConstant.FIRST_ENTER_MAIN, true)){
            enterUserGuide();
        }else {
            enterMain();
        }
        //关掉倒计时
//        if (timeCount != null) timeCount.stop();
        MyLog.i("用户--" + user.toString());
        DataManager.getInstance().setUser(user);
        addCommonParams();
        SPUtil.putObject(user);
        SPUtil.putString(SRConstant.SP_USER_PHONE, user.getPatientMobile());
        setResult(RESULT_OK);
        finish();
    }
}
