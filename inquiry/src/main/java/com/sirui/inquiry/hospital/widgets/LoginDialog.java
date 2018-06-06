package com.sirui.inquiry.hospital.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.sirui.basiclib.config.NetUrl;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.data.bean.User;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.utils.SPUtil;
import com.sirui.basiclib.utils.TextViewUtil;
import com.sirui.basiclib.utils.TimeCount;
import com.sirui.basiclib.utils.Utils;
import com.sirui.inquiry.R;
import com.sirui.inquiry.R2;
import com.sirui.inquiry.hospital.config.InquiryNetUrl;
import com.sirui.inquiry.hospital.util.string.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * 登录 Dialog
 * <p>
 * Created by yellow on 2017/10/21.
 */

public class LoginDialog extends Dialog implements OnClickListener {

    private Context mContext;
    private String mPhone;

    @BindView(R2.id.tv_dialog_content)
    TextView tvDialogContent;
    @BindView(R2.id.et_dialog_input)
    EditText etDialogInput;
    @BindView(R2.id.btn_send_code)
    Button btnSendCode;
    @BindView(R2.id.button_confirm)
    Button btnConfirm;

    TimeCount timer;

    private String VCODE_TYPE_USER = "1";
    private String LOGIN_TYPE_USER = "1";

    private LoginStatusListener mLoginStatusListener;

    public LoginDialog(Context context, String phone, LoginStatusListener listener) {
        super(context, R.style.Dialog);
        mContext = context;
        mPhone = phone;
        mLoginStatusListener = listener;
        init();
    }

    private void init() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_login, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Resources r = Utils.getContext().getResources();
        int widthInPixel = r.getDimensionPixelSize(R.dimen.dialog_hint_width_out);
        int heightInPixel = r.getDimensionPixelSize(R.dimen.dialog_hint_height_lager);

        setContentView(contentView, new ViewGroup.LayoutParams(
                widthInPixel,
                heightInPixel));

        ButterKnife.bind(this);
        String contentStr = mContext.getString(R.string.dialog_phone_hint, mPhone);
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.black));
        SpannableString phoneHint = TextViewUtil.getSpannableString(contentStr, mPhone, blackSpan);
        btnSendCode.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        tvDialogContent.setText(phoneHint);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mLoginStatusListener != null) {
                    mLoginStatusListener.onCancel(DataManager.getInstance().getUser() != null);
                }
            }
        });

    }

    private void getCode() {
        HttpGo.post(InquiryNetUrl.URL_SMS_SEND2)
                .params("mobileNo", mPhone)
                .params("type", VCODE_TYPE_USER)
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        JsonUtil.checkResult(s);
                        timer = new TimeCount(2 * DateUtils.MINUTE_IN_MILLIS,
                                DateUtils.SECOND_IN_MILLIS, btnSendCode,
                                null);
                        timer.start();
                        btnSendCode.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Response response, Exception e) {
                        btnSendCode.setEnabled(true);
                        super.onFailure(response, e);
                    }

                    @Override
                    public void onStart() {
                        btnSendCode.setEnabled(false);
                        super.onStart();
                    }
                });
    }

    private void loginWithVCode() {
        String vcode = etDialogInput.getText().toString();
        if (!StringUtil.isEmpty(vcode)) {
            HttpGo.post(NetUrl.VIPURL)
                    .params("mobile", mPhone)
                    .params("clientType", LOGIN_TYPE_USER)
//                    .params("storeId", MyApplication.getInstance().getLoginBean().getStoreId())
//                    .params("eid", MyApplication.getInstance().getLoginBean().getEid())
                    .params("smsCode", vcode)
                    .execute(new HttpListener() {
                        @Override
                        public void onSuccess(String s) {
                            JsonUtil.checkResult(s);

                            User user = JsonUtil.checkToGetData(s, User.class);
                            if (user == null) {
                                dismiss();
                                return;
                            }
                            if (timer != null) {
                                timer.onFinish();
                                timer.stop();
                                timer = null;
                            }
                            MyLog.i("用户--" + user.toString());
                            DataManager.getInstance().setUser(user);
                            SPUtil.putString(SRConstant.SP_USER_PHONE, user.getPatientMobile());
                            HttpParams params = new HttpParams();
                            params.put("token", DataManager.getInstance().getUser().getToken());
                            HttpGo.addCommonParams(params);
                            mLoginStatusListener.onSuccess(user);
                            dismiss();
                        }

                        @Override
                        public void onFailure(Response response, Exception e) {
                            super.onFailure(response, e);
                            dismiss();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
         if(resId == R.id.button_confirm){
             loginWithVCode();
         }else if(resId == R.id.btn_send_code){
             getCode();
         }
    }

    public interface LoginStatusListener {
        void onSuccess(User loginUser);

        void onCancel(boolean isSuccess);
    }

}
