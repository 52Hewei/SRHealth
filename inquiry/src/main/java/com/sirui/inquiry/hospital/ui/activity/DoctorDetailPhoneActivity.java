package com.sirui.inquiry.hospital.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.flexbox.FlexboxLayout;
import com.net.client.HttpDialogListener;
import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.config.NetUrl;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.data.AccountManager;
import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.data.bean.PayBean;
import com.sirui.basiclib.data.bean.User;
import com.sirui.basiclib.envet.EventType;
import com.sirui.basiclib.envet.InquiryEvent;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.utils.RegexUtil;
import com.sirui.basiclib.utils.ViewUtil;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.basiclib.widget.MyToast;
import com.sirui.inquiry.R;
import com.sirui.inquiry.R2;
import com.sirui.inquiry.hospital.chat.PatientAVChatActivity;
import com.sirui.inquiry.hospital.chat.constant.InquiryTypeEnum;
import com.sirui.inquiry.hospital.config.InquiryNetUrl;
import com.sirui.inquiry.hospital.manager.LoginManager;
import com.sirui.inquiry.hospital.ui.model.DoctorInfo;
import com.sirui.inquiry.hospital.ui.model.RequestQueueResult;
import com.sirui.inquiry.hospital.util.string.StringUtil;
import com.sirui.inquiry.hospital.widgets.REditText;
import com.sirui.inquiry.hospital.widgets.RObject;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IPayProvider;
import com.sirui.router.provider.IWebProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * 医生详情界面
 * Create by xiepc on 2017/3/9 15:34
 */
public class DoctorDetailPhoneActivity extends BaseActivity{
    private static final String CHAT_TYPE = "CHAT_TYPE";
    public static final String CONSULT_TYPE = "CONSULT_TYPE";

    public static final int TYPE_QUICK = 1;
    public static final int TYPE_NORMAL = 2;
    public static final String QUICK_DEP_NAME = "QUICK_DEP_NAME";
    public static final String QUICK_DEP_ID = "QUICK_DEP_ID";
    public static final String QUICK_PATIENT_DESCRIPTION = "QUICK_PATIENT_DESCRIPTION";
    /**
     * 发起问诊第一页
     */
    public static final int STAGE_INFO = 1;
    /**
     * 发起问诊第二页
     */
    public static final int STAGE_DETAIL = 2;

    /**
     * 患者用药协议 TYPE
     */
    public static final String MEDICINE_AGREEMENT = "13";

    /**
     * 发起问诊步骤
     */
    private int currentStage;

    /**
     * 前一个界面传过来的医生信息
     */
    private DoctorInfo preDoctorInfo;

    /**
     * 问诊类型
     */
    private int consultType;

    private String quickDepId;
    private String quickDepName;
    private String quickPatientDescription;

    /**
     * 当前界面医生详情
     */
    private DoctorInfo mDoctorInfo;
//    private TextView stepCircle3Text;
//    private TextView stepName3Text;
    /**
     * 问诊类型
     */
    private InquiryTypeEnum askType = InquiryTypeEnum.typeVideo;

    /**
     * 是否处于编辑用户信息状态
     */
    private boolean isEditing = false;

    /**
     * 230新控件
     */
    @BindView(R2.id.tv_start_consult_name)
    TextView tvStartConsultName;
    @BindView(R2.id.tv_start_consult_gender)
    TextView tvStartConsultGender;
    @BindView(R2.id.tv_start_consult_age)
    TextView tvStartConsultAge;
    @BindView(R2.id.et_start_consult_phone_content)
    EditText etStartConsultPhoneContent;
    @BindView(R2.id.et_start_consult_name_content)
    EditText etStartConsultNameContent;
/*    @BindView(R2.id.btn_male)
    Button btnMale;
    @BindView(R2.id.btn_female)
    Button btnFemale;*/
    @BindView(R2.id.tv_sex_value)
    TextView tvSexValue;
    @BindView(R2.id.et_start_consult_age_content)
    EditText etStartConsultAgeContent;
//    @BindView(R2.id.ll_start_consult_login_switch)
//    LinearLayout llStartConsultLoginSwitch;
//    @BindView(R2.id.ll_start_consult_save_patient_info)
//    LinearLayout llStartConsultSavePatientInfo;
//    @BindView(R2.id.tv_start_consult_save_patient_info)
//    TextView tvStartConsultSavePatientInfo;
    @BindView(R2.id.flow_layout)
    FlexboxLayout flowLayout;
    @BindView(R2.id.et_start_consult_description_content)
    REditText etStartConsultDescriptionContent;
    @BindView(R2.id.iv_check_box)
    ImageView ivCheckBox;
    @BindView(R2.id.tv_medicine_agreement)
    TextView tvMedicineAgreement;
    @BindView(R2.id.tv_left_back)
    View ivLeftBack;
    @BindView(R2.id.btn_start_inquiry)
    Button btnStartInquiry;

    @BindView(R2.id.tv_doctor_name)
    TextView tvDoctorName;
    @BindView(R2.id.tv_department)
    TextView tvDepartment;
    @BindView(R2.id.tv_doctor_position)
    TextView tvDoctorPosition;
    @BindView(R2.id.tv_hospital)
    TextView tvHospital;
    @BindView(R2.id.tv_history_inquiry_sum)
    TextView tvHistoryInquirySum;
    @BindView(R2.id.fl_doctor_info)
    FrameLayout flDoctorInfo;
    @BindView(R2.id.rootLayout)
    LinearLayout rootLayout;
    PayBean mPayBean;


//    LoginDialog.LoginStatusListener mLoginStatusListener = new LoginDialog.LoginStatusListener() {
//        @Override
//        public void onSuccess(User loginUser) {
////            llStartConsultLoginSwitch.setVisibility(View.VISIBLE);
//            fillData();
//            canStartConsult(false);
//        }
//
//        @Override
//        public void onCancel(boolean isSuccess) {
//            etStartConsultPhoneContent.setText("");
//        }
//    };

    public static void start(Context context, DoctorInfo info, InquiryTypeEnum type) {
        Intent intent = new Intent(context, DoctorDetailPhoneActivity.class);
        intent.putExtra("doctorInfo", info);
        intent.putExtra(CONSULT_TYPE, TYPE_NORMAL);
        intent.putExtra(CHAT_TYPE, type);
        context.startActivity(intent);
    }

    public static void quickStart(Context context, String depName, String depId, String description) {
        Intent intent = new Intent(context, DoctorDetailPhoneActivity.class);
//        intent.putExtra(CHAT_TYPE, type);
        intent.putExtra(CONSULT_TYPE, TYPE_QUICK);
        intent.putExtra(QUICK_DEP_NAME, depName);
        intent.putExtra(QUICK_DEP_ID, depId);
        intent.putExtra(QUICK_PATIENT_DESCRIPTION, description);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_start_inquiry;
    }

    @Override
    protected void iniData() {
        parseIntent();
    }

    @Override
    protected void iniView() {
        setView();
        fillData();
        ViewUtil.requestFocus(rootLayout);
        getDoctorDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Debug.stopMethodTracing();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 处理通过弹框完善患者信息后返回页面，提交按钮状态不刷新
        canStartConsult(false);
    }

    private void parseIntent() {
        preDoctorInfo = (DoctorInfo) getIntent().getSerializableExtra("doctorInfo");
        askType = (InquiryTypeEnum) getIntent().getSerializableExtra(CHAT_TYPE);
        if (InquiryTypeEnum.typeText == askType) {
            askType = InquiryTypeEnum.typeText;
        } else if (InquiryTypeEnum.typeVideo == askType) {
            askType = InquiryTypeEnum.typeVideo;
        } else {
            MyLog.w("未知的聊天类型，默认使用视频聊天");
        }
        consultType = getIntent().getIntExtra(CONSULT_TYPE, TYPE_NORMAL);
        if(consultType == TYPE_QUICK){
            quickDepName = getIntent().getStringExtra(QUICK_DEP_NAME);
            quickDepId = getIntent().getStringExtra(QUICK_DEP_ID);
            quickPatientDescription = getIntent().getStringExtra(QUICK_PATIENT_DESCRIPTION);
        }
    }

    private void setView() {
        if (DataManager.getInstance().getUser() == null)return;

        if (TYPE_QUICK == consultType) {
             flDoctorInfo.setVisibility(View.GONE);
//            TextView stepName2Text = (TextView) findViewById(R.id.stepName2Text);
//            layoutDoctorDetailRight.setVisibility(View.GONE);
            etStartConsultDescriptionContent.setText(quickPatientDescription);
//            ViewGroup.LayoutParams layoutParams = layoutDoctorDetailLeft.getLayoutParams();
//            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.quick_start_consult_height);
//            layoutParams.width = getResources().getDimensionPixelSize(R.dimen.quick_start_consult_width);
//            layoutDoctorDetailLeft.setLayoutParams(layoutParams);
//            stepCircle2Text.setVisibility(View.GONE);
//            stepLine2.setVisibility(View.GONE);
//            stepName2Text.setVisibility(View.GONE);
//            stepName3Text.setText("提交问诊");
//            stepName1Text.setText("极速问诊");
//            flowLayout.setVisibility(View.GONE);
        }
        addSymptom("呼吸道感染");
        addSymptom("急性支气管炎");
        addSymptom("扁桃腺炎");
        addSymptom("高血压");
        addSymptom("胃溃疡");
        addSymptom("咽炎");
        addSymptom("冠心病");
        addSymptom("糖尿病");
        addSymptom("感冒");
        addSymptom("痛风");
        ivCheckBox.setSelected(true);
//        stepCircle3Text.setSelected(true);
//        stepName3Text.setSelected(true);
        tvMedicineAgreement.setClickable(true);
//        llStartConsultLoginSwitch.setClickable(true);
//        llStartConsultLoginSwitch.setOnClickListener(this);
//        tvStartConsultName.setText(TextViewUtil.getSpannableString(getString(R.string.patient_name_consult), "*", orangeSpan));
//        tvStartConsultAge.setText(TextViewUtil.getSpannableString(getString(R.string.patient_age_consult), "*", orangeSpan));
//        tvStartConsultGender.setText(TextViewUtil.getSpannableString(getString(R.string.patient_gender_consult), "*", orangeSpan));
//        findViewById(R.id.iv_return).setOnClickListener(this);
        etStartConsultDescriptionContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                canStartConsult(false);
            }
        });


        etStartConsultNameContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = s.toString();
//                if (StringUtil.isEmpty(name)) {
//                    tvStartConsultWarningName.setVisibility(View.INVISIBLE);
//                } else {
//                    tvStartConsultWarningName.setVisibility(
//                            !RegexUtil.checkName(name) ? View.VISIBLE : View.INVISIBLE);
//                }
                canStartConsult(false);
            }
        });
        // 解决设置 ScrollView scrollbars="none" 导致的不能滑动的问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initTitle("患者信息");
    }

    @Override
    protected void initTitle(String title) {
        super.initTitle(title);
        tvRightButton.setVisibility(View.GONE);
    }

    private void fillData() {
        if (InquiryTypeEnum.typeVideo.equals(askType)) {
//            stepName3Text.setText("视频问诊");
        } else if (InquiryTypeEnum.typeText.equals(askType)) {
//            stepName3Text.setText("图文问诊");
        } else {
            MyLog.w("聊天类型回显错误");
        }
        User user = DataManager.getInstance().getUser();
        if (user != null) { // 已登录
            // 首次注册默认允许完善信息，方便操作
            if (RegexUtil.checkName(user.getRealName())
                    && RegexUtil.checkAge(user.getAge())) {
                setEnableEditUserInfo(false);
                etStartConsultDescriptionContent.requestFocus();
            } else {
                setEnableEditUserInfo(true);
                tvStartConsultName.requestFocus();
            }
            etStartConsultPhoneContent.setText(user.getPatientMobile());
            etStartConsultNameContent.setText(user.getRealName());
            etStartConsultAgeContent.setText(user.getAge());
            if (SRConstant.SEX_GIRL.equals(user.getGender())) {
                tvSexValue.setText(getString(R.string.girl));
            } else {
                tvSexValue.setText(getString(R.string.boy));
            }
//            llStartConsultLoginSwitch.setVisibility(View.VISIBLE);
        } else {// 未登录
            setEnableEditUserInfo(true);
//            llStartConsultLoginSwitch.setVisibility(View.INVISIBLE);
            etStartConsultDescriptionContent.requestFocus();
        }
    }



    /**
     * 设置是否启用编辑用户信息
     *
     * @param isEnable 是否启用
     */
    private void setEnableEditUserInfo(boolean isEnable) {
        isEditing = isEnable;
        if (DataManager.getInstance().getUser() == null) {
            etStartConsultPhoneContent.setEnabled(true);
            etStartConsultPhoneContent.setText("");
            etStartConsultNameContent.setText("");
            etStartConsultAgeContent.setText("");
        } else {
            etStartConsultPhoneContent.setEnabled(false);
        }
        etStartConsultNameContent.setEnabled(isEnable);
        etStartConsultAgeContent.setEnabled(isEnable);
        if (isEnable) {
            etStartConsultNameContent.requestFocus();
            etStartConsultNameContent.setSelection(etStartConsultNameContent.getText().length());
        } else {
        }
    }

    /**
     * 添加病症
     */
    private void addSymptom(final String text) {
        final TextView disease = (TextView) getLayoutInflater().inflate(R.layout.item_patient_disease
                , flowLayout, false);
        disease.setText(text);
        disease.setClickable(true);

        final RObject rObject = new RObject();
        rObject.setObjectText(text);
        rObject.setDisease(disease);

        disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (etStartConsultDescriptionContent.contains(rObject)) {
                            etStartConsultDescriptionContent.remove(rObject);
                            rObject.getDisease().setSelected(false);
                        } else {
                            etStartConsultDescriptionContent.add(rObject);
                            rObject.getDisease().setSelected(true);
                        }
                    }
                });
            }
        });
        flowLayout.addView(disease);
    }

//    /**
//     * 保存新的用户信息
//     */
//    private void updateUserInfo() {
//        User user = DataManager.getInstance().getUser();
//        String userInfoHint = userInfoComplete();
//        if (user != null && StringUtil.isEmpty(userInfoHint)) { // 信息完整
//            user.setAge(etStartConsultAgeContent.getText().toString());
//            user.setSex(btnFemale.isSelected() ? SRConstant.SEX_GIRL : SRConstant.SEX_BOY);
//            user.setRealName(etStartConsultNameContent.getText().toString());
//            String json = JsonUtil.Object2Json(user);
//            HttpGo.post(NetUrl.UPDATE_USER_INFO)
//                    .params("userInfo", json)
//                    .execute(new HttpDialogListener(this) {
//                        @Override
//                        public void onSuccess(String s) {
//                            JsonUtil.checkResult(s);
//                            User returnUser = JsonUtil.checkToGetData(s, User.class);
//                            if (returnUser != null) {
//                                DataManager.getInstance().getUser().setAge(returnUser.getAge());
//                                DataManager.getInstance().getUser().setBirthday(returnUser.getBirthday());
//                                DataManager.getInstance().getUser().setCartNo(returnUser.getCartNo());
//                                DataManager.getInstance().getUser().setRealName(returnUser.getRealName());
//                                DataManager.getInstance().getUser().setSex(returnUser.getSex());
//                                fillData();
//                            }
//
//                        }
//                    });
//        } else {
//            MyToast.show(userInfoHint);
//        }
//    }
@OnClick({R2.id.btn_start_inquiry,R2.id.iv_check_box,R2.id.tv_medicine_agreement})
  public void onClick(View view) {
        int resId = view.getId();
        if(resId == R.id.btn_start_inquiry){
            if (canStartConsult(true)) {
                if (DataManager.getInstance().getUser() != null) {
                    if(consultType == TYPE_QUICK){
                        quicklyConsult();
                    }else{
                        askDoctor();
                    }
                    ViewUtil.setPostEnableClick(btnStartInquiry, 5000);
                }
            }
        }else if(resId == R.id.iv_check_box){
            ivCheckBox.setSelected(!ivCheckBox.isSelected());
            canStartConsult(false);
        }else if(resId == R.id.tv_medicine_agreement){
            getAgreement();
        }
    }


    private void startQueue(){
        final User userInfo = DataManager.getInstance().getUser();
        final String chiefComplaint = etStartConsultDescriptionContent.getText().toString();
        if (userInfo == null) {
            MyToast.show("患者信息为空");
            return;
        }
        if (mDoctorInfo == null) {
            MyToast.show("医生信息为空");
            return;
        }
        HttpGo.get(InquiryNetUrl.URL_REQUEST_QUEUE)
                .tag(this)
                .params("patientId", userInfo.getPatientId())
                .params("mobileNo", userInfo.getPatientMobile())
                .params("patientName", userInfo.getRealName())
                .params("doctorId", mDoctorInfo.getDoctorId())
                .params("doctorName", mDoctorInfo.getDoctorName())
                .params("hospitalId", mDoctorInfo.getHospitalId())
                .params("hospitalName", mDoctorInfo.getHospitalName())
                .params("deptId", mDoctorInfo.getDepartmentId())
                .params("deptName", mDoctorInfo.getDeptName())
                .params("chiefComplaint", StringUtil.removeLineBreak(chiefComplaint))
                .params("age", userInfo.getAge())
                .params("sex", userInfo.getSex())
                .params("inquiryType", askType.getValue())
                .params("isVisitor", userInfo.getIsVisitor())
                .params("orderNo", mPayBean.getOrderNo()) //问诊单号
                .params("paymentNo", mPayBean.getPaymentNo())  //支付单号
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        parseQueueReuslt(userInfo,chiefComplaint,s);
                    }
                });
    }

    /**
     * 排队结果处理
     * @param userInfo
     * @param s
     */
    private void parseQueueReuslt(User userInfo,String chiefComplaint,String s){
        RequestQueueResult result = JsonUtil.checkToGetData(s, RequestQueueResult.class);
        if (result == null) {
            if ("Y".equals(userInfo.getIsVisitor())) {
                AccountManager.getInstance().loginOutMember();
            }
            return;
        }
        result.setInquiryType(askType);
        result.setStartType("1");
        result.setChiefComplaint(chiefComplaint);
        MyLog.i("目前排队=" + result.getCount());
        imLogin(this, mDoctorInfo, result, chiefComplaint, consultType);
    }
    /**
     * 请求问诊
     */
    public void askDoctor() {
        final User userInfo = DataManager.getInstance().getUser();
        final String chiefComplaint = etStartConsultDescriptionContent.getText().toString();
        if (userInfo == null) {
            MyToast.show("患者信息为空");
            return;
        }
        if (mDoctorInfo == null) {
            MyToast.show("医生信息为空");
            return;
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        HttpGo.get(InquiryNetUrl.URL_REQUEST_QUEUE)
                .tag(this)
                .params("patientId", userInfo.getPatientId())
                .params("mobileNo", userInfo.getPatientMobile())
                .params("patientName", userInfo.getRealName())
                .params("doctorId", mDoctorInfo.getDoctorId())
                .params("doctorName", mDoctorInfo.getDoctorName())
                .params("hospitalId", mDoctorInfo.getHospitalId())
                .params("hospitalName", mDoctorInfo.getHospitalName())
                .params("deptId", mDoctorInfo.getDepartmentId())
                .params("deptName", mDoctorInfo.getDeptName())
                .params("chiefComplaint", StringUtil.removeLineBreak(chiefComplaint))
                .params("age", userInfo.getAge())
                .params("sex", userInfo.getSex())
                .params("inquiryType", askType.getValue())
                .params("isVisitor", userInfo.getIsVisitor())
                .params("transctionNo", timestamp)
                .params("tradeType", "APP")
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                              JSONObject obj = new JSONObject(s);
                              if(SRConstant.SUCCESS_CODE.equals(obj.optString("errCode"))){ //无需支付
                                    parseQueueReuslt(userInfo,chiefComplaint,s);
                               }else if("120001".equals(obj.optString("errCode"))){ //需要支付
                                  JSONObject dataObj = obj.optJSONObject("data");
                                  if(dataObj != null){
                                      PayBean bean = JsonUtil.gsonToBean(dataObj.toString(),PayBean.class);
                                      enterPay(bean);
                                  }
                              }else{
                                  MyToast.show(obj.optString("errDesc"));
                              }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     *
     * @param bean
     */
    private void enterPay(final PayBean bean){
        DialogUtil.both(this, "您需要支付" + bean.getTotalAmount() + "元进行问诊，确认支付？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPayBean = bean;
                IPayProvider payProvider = (IPayProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_PAY_SERVICE).navigation();
                payProvider.goToPay(DoctorDetailPhoneActivity.this,bean);
            }
        });
    }

    /**
     * 云信IM帐号登录
     */
    public static void imLogin(final Activity activity, final DoctorInfo mDoctorInfo, final RequestQueueResult requestQueueResult, final String chiefComplaint, final int consultType) {
        LoginManager loginManager = new LoginManager();
        loginManager.login(requestQueueResult.getImId(), requestQueueResult.getToken(), new LoginManager.LoginCallback() {
            @Override
            public void onSucess(LoginInfo param) {
                MyLog.i("im帐号登录成功");
                DataManager.getInstance().getUser().setImNo(requestQueueResult.getImId());
                PatientAVChatActivity.start(activity, mDoctorInfo, requestQueueResult, chiefComplaint, consultType);
                activity.finish();
            }

            @Override
            public void onFailed(int code) {
                MyLog.i("im帐号登录失败" + code);
//                DialogUtil.confirm("IM服务登录失败", null);
//                DialogUtil.confirm(DoctorDetailPhoneActivity.this,"IM服务登录失败",null);
                 // DialogUtil.confirm(DoctorDetailPhoneActivity.this,"",null);
                MyToast.show("IM服务登录失败");
            }
        });
    }

    private void getAgreement() {
        HttpGo.post(NetUrl.GET_AGREEMENT_INFO)
                .params("agreementType", MEDICINE_AGREEMENT)
                .execute(new HttpListener() {

                    @Override
                    public void onStart() {
                        tvMedicineAgreement.setEnabled(false);
                    }

                    @Override
                    public void onSuccess(String s) {
                        tvMedicineAgreement.setEnabled(true);
                        JSONObject result = JsonUtil.checkResult(s);
                        if (result != null &&
                                SRConstant.SUCCESS_CODE.equals(result.optString(SRConstant.ERR_CODE_KEY))) {
                            JSONObject data = result.optJSONObject(SRConstant.DATA);
                            String url = "";
                            if (data != null) {
                                url = data.optString("agreementContent");
                            }
                            IWebProvider provider = (IWebProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_WEB_SERVICE).navigation();
                            if(provider != null){
                                provider.goToWeb(DoctorDetailPhoneActivity.this,url);
                             }
                        }
                    }

                    @Override
                    public void onFailure(Response response, Exception e) {
                        super.onFailure(response, e);
                        tvMedicineAgreement.setEnabled(true);
                    }
                });
    }

    /**
     * 判断信息是否完善，完善后才能进行问诊
     *
     * @param showNotify 是否显示错误提示
     * @return 是否可以进行问诊
     */
    private boolean canStartConsult(boolean showNotify) {
        String msg = userInfoComplete();
        boolean userInfoComplete = StringUtil.isEmpty(msg);

        if (!ivCheckBox.isSelected()) {
            msg += msg.length() > 0 ? "，" : "";
            msg += "同意《药物使用知情同意书》才能进行问诊";
        }
        if (DataManager.getInstance().getUser() == null) {
            msg = "填写手机号登录成功才能开始问诊";
        }
        if (showNotify && msg.length() > 0) {
            DialogUtil.confirm(this,msg, null);

        }
//        if (userInfoComplete && ivCheckBox.isSelected()) {
//            startAskBtn.setEnabled(true);
//        } else {
//            startAskBtn.setEnabled(false);
//        }
        if (showNotify && isEditing) {
            MyToast.show("请先保存个人信息，再提交问诊。");
        }
        return !isEditing && userInfoComplete && ivCheckBox.isSelected();
    }

    /**
     * 判断用户信息是否完整
     *
     * @return 提示信息，若为 "" 则代表用户信息完整
     */
    private String userInfoComplete() {
        String msg;
        String phone = etStartConsultPhoneContent.getText().toString();
        String name = etStartConsultNameContent.getText().toString();
        String age = etStartConsultAgeContent.getText().toString();
//        boolean isGenderSelected = btnFemale.isSelected() || btnMale.isSelected();

        boolean checkPhone = RegexUtil.isMobileNumber(phone);
        boolean checkName = RegexUtil.checkName(name);
        boolean checkAge = RegexUtil.checkAge(age);
        if (checkPhone
                && checkName
                && checkAge) {
            msg = "";
        } else if (!checkPhone) {
            msg = getString(R.string.patient_phone_warning1);
        } else if (!checkName) {
            msg = getString(R.string.patient_name_warning);
        } else if (!checkAge) {
            msg = getString(R.string.patient_age_warning);
        } else {
            msg = "请选择性别";
        }
        return msg;
    }

    /**
     * 获取医生详情（登录区外）
     */
    private void getDoctorDetail() {
        if (preDoctorInfo != null) {
            HttpGo.post(InquiryNetUrl.URL_DOCTOR_DETAIL_OPEN)
                    .tag(this)
                    .params("doctorId", preDoctorInfo.getDoctorId())
                    .execute(new HttpDialogListener(DoctorDetailPhoneActivity.this) {
                        @Override
                        public void onSuccess(String s) {
                            JSONObject obj = JsonUtil.checkResult(s);
                            if (obj != null && !obj.isNull("data")) {
                                 mDoctorInfo = new DoctorInfo(obj.optJSONObject("data"));
                                 showDoctorInfo(mDoctorInfo);
                                 preDoctorInfo.setImId(mDoctorInfo.getImId()); //把医生详情获取到的ImId赋值给前者
                            }
                        }
                    });
        }
    }

    private void showDoctorInfo(DoctorInfo info){
        if(consultType == TYPE_QUICK){
            return;
        }
        tvDoctorName.setText(info.getDoctorName());
        tvDepartment.setText(info.getDeptName());
        tvDoctorPosition.setText(info.getTitleName());
        tvHospital.setText(info.getHospitalName());
        tvHistoryInquirySum.setText(info.getHistoryInquirySum());
    }


    /**
     * 极速问诊
     */
    private void quicklyConsult() {
        final User user = DataManager.getInstance().getUser();
        final String chiefComplaint = etStartConsultDescriptionContent.getText().toString();
        String timestamp = String.valueOf(System.currentTimeMillis());
        HttpGo.post(InquiryNetUrl.URL_INQUIRY_QUICK)
                .params("inquiryType", InquiryTypeEnum.typeText.getValue())
                .params("patientId", user.getPatientId())
                .params("patientName", user.getRealName())
                .params("chiefComplaint", etStartConsultDescriptionContent.getText().toString())
                .params("sex", user.getSex())
                .params("age", user.getAge())
                .params("deptId", quickDepId)
                .params("deptName", quickDepName)
                .params("mobileNo", user.getPatientMobile())
                .params("isVisitor", user.getIsVisitor())
                .params("transctionNo", timestamp)
                .params("tradeType", "APP")
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(SRConstant.SUCCESS_CODE.equals(obj.optString("errCode"))){ //无需支付
                             parseQuicklyQueue(obj,chiefComplaint);
                        }else if("120001".equals(obj.optString("errCode"))){ //需要支付
                            JSONObject dataObj = obj.optJSONObject("data");
                            if(dataObj != null){
                                PayBean bean = JsonUtil.gsonToBean(dataObj.toString(),PayBean.class);
                                enterPay(bean);
                            }
                        }else{
                            MyToast.show(obj.optString("errDesc"));
                        }
                    }
                });
    }

    private void quicklyQueue() {
        final User user = DataManager.getInstance().getUser();
        final String chiefComplaint = etStartConsultDescriptionContent.getText().toString();
        HttpGo.post(InquiryNetUrl.URL_INQUIRY_QUICK)
                .params("inquiryType", InquiryTypeEnum.typeText.getValue())
                .params("patientId", user.getPatientId())
                .params("patientName", user.getRealName())
                .params("chiefComplaint", etStartConsultDescriptionContent.getText().toString())
                .params("sex", user.getSex())
                .params("age", user.getAge())
                .params("deptId", quickDepId)
                .params("deptName", quickDepName)
                .params("mobileNo", user.getPatientMobile())
                .params("isVisitor", user.getIsVisitor())
                .params("orderNo", mPayBean.getOrderNo()) //问诊单号
                .params("paymentNo", mPayBean.getPaymentNo())  //支付单号
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(SRConstant.SUCCESS_CODE.equals(obj.optString("errCode"))){
                            parseQuicklyQueue(obj,chiefComplaint);
                        }else{
                            MyToast.show(obj.optString("errDesc"));
                        }
                    }
                });
    }

    private void parseQuicklyQueue(JSONObject object,String chiefComplaint ){
        final User user = DataManager.getInstance().getUser();
        JSONObject data = object.optJSONObject(SRConstant.DATA);
        user.setImNo(data.optString("imId"));
        user.setToken(data.optString("imToken"));
        DoctorInfo doctor = new DoctorInfo();
        doctor.setQueueNumber(data.optString("count"));
        doctor.setDoctorId(data.optString("doctorId"));
        doctor.setDoctorName(data.optString("doctorName"));
        doctor.setDepartmentId(data.optString("departmentId"));
        doctor.setDeptName(data.optString("deptName"));
        doctor.setHospitalId(data.optString("hospitalId"));
        doctor.setHospitalName(data.optString("hospitalName"));
        doctor.setPersonalGood(data.optString("personGood"));
        doctor.setTitleName(data.optString("doctorTitle"));
        doctor.setHeadImageUrl(data.optString("headPortraitUrl"));
        doctor.setImId(data.optString("doctorImId"));
        RequestQueueResult rq = new RequestQueueResult();
        rq.setImId(user.getImNo());
        rq.setToken(user.getToken());
        rq.setChiefComplaint(chiefComplaint);
        rq.setCount(data.optString("count"));
        rq.setInquiryType(InquiryTypeEnum.typeText);
        rq.setStartType("1");// 正常问诊
        rq.setWaitSumDate(data.optString("waitSumDate"));
        rq.setOrderNo(data.optString("orderNo"));
        imLogin(DoctorDetailPhoneActivity.this, doctor,
                rq, etStartConsultDescriptionContent.getText().toString(), consultType);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(InquiryEvent event) {
         if(event.getType() == EventType.PAY_SUCCESS){
              if(consultType == TYPE_QUICK){
                  quicklyQueue(); //极速问诊
              }else{
                  startQueue(); //正常问诊
              }
         }
    }
}
