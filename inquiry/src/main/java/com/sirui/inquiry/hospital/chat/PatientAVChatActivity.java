package com.sirui.inquiry.hospital.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.net.client.event.MainEvent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.data.bean.User;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.utils.ActivityUtils;
import com.sirui.basiclib.utils.FragmentUtil;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.utils.RegexUtil;
import com.sirui.basiclib.utils.RepeatTaskTimer;
import com.sirui.basiclib.utils.SRLog;
import com.sirui.basiclib.utils.TextViewUtil;
import com.sirui.basiclib.utils.Utils;
import com.sirui.basiclib.utils.ViewUtil;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.basiclib.widget.MyToast;
import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.avchat.AVChatActivity;
import com.sirui.inquiry.hospital.avchat.AVChatClientManager;
import com.sirui.inquiry.hospital.avchat.AVChatStateListener;
import com.sirui.inquiry.hospital.avchat.event.EventType;
import com.sirui.inquiry.hospital.avchat.floatshow.FloatWindowService;
import com.sirui.inquiry.hospital.cache.IMCache;
import com.sirui.inquiry.hospital.chat.constant.Extras;
import com.sirui.inquiry.hospital.chat.constant.InquiryTypeEnum;
import com.sirui.inquiry.hospital.chat.module.OnMsgSendCallback;
import com.sirui.inquiry.hospital.chat.util.MessageUtil;
import com.sirui.inquiry.hospital.chat.widget.StartAskPanelController;
import com.sirui.inquiry.hospital.config.InquiryNetUrl;
import com.sirui.inquiry.hospital.ui.activity.DoctorDetailPhoneActivity;
import com.sirui.inquiry.hospital.ui.activity.InquiryRecordActivity;
import com.sirui.inquiry.hospital.ui.model.DoctorInfo;
import com.sirui.inquiry.hospital.ui.model.RequestQueueResult;
import com.sirui.inquiry.hospital.util.InquiryDialogUtil;
import com.sirui.inquiry.hospital.widgets.CancelChatDialog;
import com.sirui.inquiry.hospital.widgets.DiagnoseResultDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.List;

import static com.sirui.inquiry.hospital.chat.constant.Extras.EXTRA_SESSIONID;


/**
 * 文字视频通话界面
 * Create by xiepc on 2017/2/8 18:51
 */
public class PatientAVChatActivity extends BaseActivity implements P2PChatStateListener , AVChatStateListener{

    private static final int STATUS_UNSTART = 10;

    private static final int STATUS_ASKING = 11;

    private static final int STATUS_FINISH = 12;
//    private AVChatModule avChatModule;
    LinearLayout remindContainer;
    protected String sessionId;
    protected RequestQueueResult queueResult;
    protected DoctorInfo doctorInfo;
    private ChatFragment chatFragment;
    /**
     * 问诊状态
     */
    private int askStatus;

    /**
     * 问诊类型
     */
    private int consultType;

    /**
     * 开始问诊时界面等待提示面板
     */
    private StartAskPanelController startAskPanelController;
    boolean isFirst = true;
//    private View chatInfoView;
//    protected RelativeLayout avPanelLayout;
    /**
     * 患者主诉
     */
//    private TextView chiefcomplaintText;
//    private TextView doctorLineUpText;
    private String chiefComplaintStr;
    /**
     * 退出登录横幅
     */
//    private TextView tvLogoutHint;
//    private RelativeLayout rlVideoChatStatusContainer;
//    @BindView(R.id.tv_video_chat_timer)
//    TextView tvVideoChatTimer;
//    @BindView(R.id.tv_video_chat_cancel)
//    TextView btnVideoChatCancel;
//    TextView btnNormalChatCancel;

    private RepeatTaskTimer taskTimer;
//    private RepeatTaskTimer videoTimer;

    private ForegroundColorSpan orangeSpan = new ForegroundColorSpan(Utils.getContext()
            .getResources().getColor(R.color.color_f60));
    /**
     * 返回首页
     */
//    private Button goHomeBtn;
//    /**
//     * 回到
//     */
//    private Button personCenterBtn;
    /**
     * 握手是否有回复
     */
    private boolean hasResponse;
    /**
     * 处理延迟事件
     */
    private Handler mHandler;
    /**
     * 断线重连标识位
     */
    private boolean reconnected;
    // view
    private LinearLayout largeSizePreviewLayout;
    private LinearLayout smallSizePreviewLayout;
    /**小窗口视图容器*/
    private FrameLayout smallSizeFrameLayout;
    private LinearLayout rootLl;

    public static void start(Context context, DoctorInfo doctorInfo, RequestQueueResult queueResult, String chiefComplaint) {
        closeAVChatActivity(); //如果已经存在当前聊天界面，则先关闭再打开。
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SESSIONID, doctorInfo.getImId());
        intent.putExtra(Extras.EXTRA_REQUEST_QUEUE, queueResult);
        intent.putExtra(Extras.EXTRA_DOCTOR_INFO, doctorInfo);
        intent.putExtra(Extras.EXTRA_CHIEF_COMPLAINT, chiefComplaint);
        intent.setClass(context, PatientAVChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void start(Context context, DoctorInfo doctorInfo, RequestQueueResult queueResult, String chiefComplaint, int consultType) {
        closeAVChatActivity(); //如果已经存在当前聊天界面，则先关闭再打开。
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SESSIONID, doctorInfo.getImId());
        intent.putExtra(Extras.EXTRA_REQUEST_QUEUE, queueResult);
        intent.putExtra(Extras.EXTRA_DOCTOR_INFO, doctorInfo);
        intent.putExtra(Extras.EXTRA_CHIEF_COMPLAINT, chiefComplaint);
        intent.putExtra(DoctorDetailPhoneActivity.CONSULT_TYPE, consultType);
        intent.setClass(context, PatientAVChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    private void parseIntent() {
        sessionId = getIntent().getStringExtra(EXTRA_SESSIONID);
        queueResult = (RequestQueueResult) getIntent().getSerializableExtra(Extras.EXTRA_REQUEST_QUEUE);
        doctorInfo = (DoctorInfo) getIntent().getSerializableExtra(Extras.EXTRA_DOCTOR_INFO);
        chiefComplaintStr = getIntent().getStringExtra(Extras.EXTRA_CHIEF_COMPLAINT);
        consultType = getIntent().getIntExtra(DoctorDetailPhoneActivity.CONSULT_TYPE, DoctorDetailPhoneActivity.TYPE_NORMAL);
        saveImCache();
    }

    private void saveImCache() {
        IMCache.getInstance().setToAccount(sessionId);
        IMCache.getInstance().setToNickName(doctorInfo.getDoctorName());
    }

    /**
     * 如果还有其它的视频聊天界面存在，先关掉
     */
    private static void closeAVChatActivity() {
        List<Activity> activities = ActivityUtils.getActivityList();
        for (Activity activity : activities) {
            if (activity instanceof PatientAVChatActivity) {
                activity.finish();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askStatus = STATUS_UNSTART;
        initRepeatTask();//
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_avchat;
    }

    @Override
    protected void iniData() {
        EventBus.getDefault().register(this);
        mHandler = new Handler();
        parseIntent();
    }

    @Override
    protected void iniView() {
        setView();
        initFragment();
        initAvChat();
    }

    private void initAvChat() {
        if (queueResult.getInquiryType() == InquiryTypeEnum.typeVideo) {
            AVChatManager.getInstance().observeIncomingCall(inComingObserver, true);
            AVChatClientManager.getInstance().initAVChat(PatientAVChatActivity.this);
        }
    }

    Observer<AVChatData> inComingObserver = new Observer<AVChatData>() {
        @Override
        public void onEvent(AVChatData data) {
            MyLog.i("---有来电---ChatId="+data.getChatId());
            AVChatActivity.inComingStart(PatientAVChatActivity.this, data);
        }
    };

    @Subscribe
    public void onEvent(MainEvent event){
        int type = event.getEventType();
        if (type == EventType.SMALL_TO_LARGE_WINDOW){
            AVChatActivity.start(this, EventType.SMALL_TO_LARGE_WINDOW, AVChatClientManager.getInstance().getToAccount(), false);
        }else if (type == EventType.LARGE_TO_SMALL_WINDOW){
            rootLl.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FloatWindowService.start(PatientAVChatActivity.this);
                }
            } , 300);
        }
    }

    private void setAVChatStateListener(){
        if(AVChatClientManager.getInstance().getAVChatClient() != null){
            AVChatClientManager.getInstance().getAVChatClient().setAVChatStateListener(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

//    private Runnable mChangeDoctorRunnable = new Runnable() {
//        @Override
//        public void run() {
//             DialogUtil.both(PatientAVChatActivity.this, "该医生候诊人较多，系统将自动为您分派其他医生", new View.OnClickListener() {
//                 @Override
//                 public void onClick(View v) {
//                     if (startAskPanelController != null)
//                        startAskPanelController.changeDoctor();
//                }
//             });
//        }
//    };


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        initUnTouchTask();
        return super.dispatchTouchEvent(ev);
    }



    private void initFragment() {
        chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SESSIONID, sessionId);
        chatFragment.setArguments(bundle);
        FragmentUtil.switchContent(this,R.id.message_fragment_container, chatFragment);
    }

    /**
     * 展示问诊消息
     */
    private void showHistoryMsg() {
        if (queueResult.getImList() != null) {
            chatFragment.loadMessages(queueResult.getImList(), true);
        }
    }

    /**
     * 发送一个握手确认对方是否还在线
     */
    private void sendHandshakeMsg() {

        hasResponse = false;
        mHandler.postDelayed(new Runnable() {   //延时500毫秒发送一个tip消息
            @Override
            public void run() {
                // MessageUtil.sendTipMessage(sessionId, Extras.TIP_VALUE_REQUEST_HAND_SHAKE); //发送握手消息
                sendTipMessage(Extras.TIP_TYPE_REQUEST_HAND_SHAKE, null);  //发送握手消息
            }
        }, 500);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasResponse) {
//                    btnNormalChatCancel.setEnabled(true);
//                    btnNormalChatCancel.setVisibility(View.VISIBLE);
                }
            }
        }, DateUtils.MINUTE_IN_MILLIS);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirst) {
            if ("1".equals(queueResult.getStartType()) || "1".equals(queueResult.getOrderStatus())) {  //如果是正常的单,或者是正在排队恢复的单
                startAskPanelController = new StartAskPanelController(remindContainer, queueResult, doctorInfo, chiefComplaintStr,consultType);
                chatFragment.addHeadView(startAskPanelController.getView());
            } else { //问诊中状态恢复问诊
                sendHandshakeMsg();
                showHistoryMsg(); //恢复聊天消息
            }
            chatFragment.enableSendMsg(false);
            isFirst = false;
            ViewUtil.requestFocus(remindContainer);
        }
    }

    private void setView() {
        initTitle("问诊对话");
        remindContainer = (LinearLayout) findViewById(R.id.remindContainer);
        smallSizePreviewLayout = (LinearLayout) findViewById(R.id.small_size_preview);
        largeSizePreviewLayout = (LinearLayout) findViewById(R.id.large_size_preview);
        smallSizeFrameLayout = (FrameLayout) findViewById(R.id.smallSizeFrameLayout);
        rootLl = findViewById(R.id.message_fragment_container);
        tvRightButton.setText("取消问诊");
        tvRightButton.setOnClickListener(listener);
//        avPanelLayout = (RelativeLayout) findViewById(R.id.avPanelLayout);
//        tvLogoutHint = (TextView) findViewById(R.id.tv_logout_hint);
        if (DoctorDetailPhoneActivity.TYPE_QUICK == consultType) {
//            findViewById(R.id.stepCircle2Text).setVisibility(View.GONE);
//            findViewById(R.id.stepLine2).setVisibility(View.GONE);
//            findViewById(R.id.stepName2Text).setVisibility(View.GONE);
//            TextView stepName3Text = (TextView) findViewById(R.id.stepName3Text);
//            TextView stepName1Text = (TextView) findViewById(R.id.stepName1Text);
//            stepName3Text.setText("提交问诊");
//            stepName1Text.setText("极速问诊");
        }
//        findViewById(R.id.stepCircle4Text).setSelected(true);
//        findViewById(R.id.stepName4Text).setSelected(true);
//        TextView tvLeftBack =  (TextView)findViewById(R.id.tv_left_back);
//        User user = MyApplication.getInstance().getUser();
//        if (user != null && user.isVisitor()) {
//            tvLeftBack.setVisibility(View.GONE);
//        } else {
//            tvLeftBack.setVisibility(View.VISIBLE);
//            tvLeftBack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    quit();
//                }
//            });
//        }
//        createChatInfoView();
    }

//    protected void initTitle(String title) {
//        super.initTitle(title);
//    }

    /**
     * 创建结束问诊后提示信息
     */
    private View createChatOverPanel(final boolean hasPrescription, final String diagnose, final String advice) {
        View overContent = LayoutInflater.from(this).inflate(R.layout.layout_remind_panel_over, null);
        TextView firstDiagnosisText = (TextView) overContent.findViewById(R.id.firstDiagnosisText);
        TextView adviseText = (TextView) overContent.findViewById(R.id.adviseText);
        firstDiagnosisText.setText(String.format("%s%s", getString(R.string.first_diagnosis_colon), diagnose));
        adviseText.setText(String.format("%s%s", getString(R.string.parse_advise_colon), advice));
        TextView tvLookDetail = (TextView) overContent.findViewById(R.id.tv_look_detail);
//        if (!hasPrescription) {
//            tvLookDetail.setText(R.string.diagnosis_detail);
//        }
        tvLookDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasPrescription) {
                    if (DataManager.getInstance().getUser().isVisitor())
                        showResultDialog();
                    else
                        goAskRecord();
                } else {
                    InquiryDialogUtil.diagnosis(PatientAVChatActivity.this,diagnose, advice);
                }
            }
        });
        return overContent;
    }

    /**
     * 退出问诊，前往个人中心问诊纪录页面
     */
    private void goAskRecord() {
//        Bundle bundle = new Bundle();
//        bundle.putInt(MainActivity.SHOW_SPECIFIC_TAB_KEY, MainActivity.PERSONAL_CENTER_INDEX);
//        bundle.putInt(PersonalFragment.SHOW_SPECIFIC_TAB_KEY, PersonalFragment.ASK_RECORD_INDEX);
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtras(bundle);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        PersonalActivity.startPersonalAct(getApplicationContext(), 2); //跳转到问诊问诊记录
         startActivity(new Intent(this,InquiryRecordActivity.class));
         finish();
    }

    private void showResultDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(DiagnoseResultDialogFragment.ORDER_ID, queueResult.getOrderNo());
        DiagnoseResultDialogFragment resultFragment = new DiagnoseResultDialogFragment();
        resultFragment.setArguments(bundle);

        resultFragment.show(getSupportFragmentManager(), "");
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.tv_right_button){
                cancelChat();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
//        if (queueResult.getInquiryType() == InquiryTypeEnum.typeVideo) {
//            avChatModule.onPause();
//        }
    }

//    @Override
//    public void onAVChatFinished() { //视频通话结束
//        MyLog.i("--视频通话结束--");
//        avPanelLayout.removeAllViews();
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
////        View view = (View) chatInfoView.getTag(); //视频正在连接中的提示关闭掉
////        view.setVisibility(View.GONE);
//        avPanelLayout.addView(chatInfoView, params);
//    }

//    @Override
//    public void onAVChatStarted() {//开启视频问诊
//        MyLog.i("---开启视频问诊回调---");
//        if (queueResult.getInquiryType() == InquiryTypeEnum.typeVideo) {
//            /**展示正在连接中*/
//            View avchatView = LayoutInflater.from(this).inflate(R.layout.layout_avchat_connecting, null);
//            avPanelLayout.removeAllViews();
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            avPanelLayout.addView(avchatView, params);
//
//        }
//        if(rlVideoChatStatusContainer.getParent() == null){
//            avPanelLayout.addView(rlVideoChatStatusContainer);
//        }
//    }

    @Override
    public void onBackPressed() {
        quit();
    }

    private void quit() {
        if (askStatus == STATUS_FINISH) {
            finish();
        } else {
//            SpannableString span = TextViewUtil.getSpannableString("未完成诊断的问诊，您可以在个人中心-问诊记录中恢复。\n是否确定退出本次问诊？"
//                    , "个人中心-问诊记录"
//                    , orangeSpan);
//            DialogUtil.both(span, new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    finish();
//                }
//            }, null);
            MyToast.show("正在问诊中不可退出。");
        }
    }

    @Override
    public void onChatStart() {
        MyLog.i("-------------------问诊开始--------------");
        hasResponse = true;
        chatFragment.enableSendMsg(true);
        tvRightButton.setVisibility(View.VISIBLE);
        tvRightButton.setEnabled(false);
        tvRightButton.setTextColor(getResources().getColor(R.color.gray_c2c2c2));
        if (startAskPanelController != null) {
            chatFragment.removeHeaderView(startAskPanelController.getView());
        }
        remindContainer.removeAllViews();
        if (askStatus == STATUS_UNSTART && "1".equals(queueResult.getStartType())) {
            MessageUtil.sendP2PMessage(sessionId, buildComplaintStr());
//               MessageUtil.sendP2PMessage(sessionId, "患者描述："+queueResult.getChiefComplaint() );
        }
//        mHandler.removeCallbacks(mUnTouchRunnable);
//        mHandler.removeCallbacks(mRefreshUnTouchRunnable);
//        mHandler.removeCallbacks(mChangeDoctorRunnable);
        askStatus = STATUS_ASKING;
//        btnNormalChatCancel.setVisibility(View.VISIBLE);
        initTextChatTimer();
        stopRepeatTask();
    }

    /**
     * 拼接患者主诉的文字描述
     *
     * @return 主诉
     */
    private String buildComplaintStr() {
        //%s医生，您好。我是%s，%s，%s岁。/n我的病情是：%s
        User user = DataManager.getInstance().getUser();
        String sex = null;
        if (SRConstant.SEX_BOY.equals(user.getSex())) {
            sex = getString(R.string.boy);
        } else {
            sex = getString(R.string.girl);
        }
        String complaint = String.format(getString(R.string.patient_chief_complaint_msg), doctorInfo.getDoctorName(), user.getRealName(), sex, user.getAge(), queueResult.getChiefComplaint());
        return complaint;
    }

    /**
     * 发送tip消息
     *
     * @param operateType 操作类型
     * @param data        附加数据  如果没有则为空
     */
    private void sendTipMessage(String operateType, JSONObject data) {
        sendTipMessage(operateType, data, null);
    }

    private void sendTipMessage(String operateType, JSONObject data, OnMsgSendCallback callback) {
        String content = JsonUtil.createTipMsg(operateType, data);
        MessageUtil.sendTipMessage(sessionId, content, callback);
    }


    @Override
    public void onChatFinish(boolean hasPrescription, String diagnose, String advice) { //聊天结束
        MyLog.i("-------------------问诊结束--------------");
        if (askStatus == STATUS_ASKING) {
            chatFragment.addFooterView(createChatOverPanel(hasPrescription, diagnose, advice));
            setFinishStatus();
        }
        tvRightButton.setVisibility(View.GONE);
    }

    /**
     * 问诊结束UI显示
     */
    private void setFinishStatus() {
        chatFragment.enableSendMsg(false);
        askStatus = STATUS_FINISH;
    }

    @Override
    public void onCancelInquiry() {
        setFinishStatus();
        DialogUtil.confirm(this, "对方取消了问诊，是否关闭当前界面？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onHandShake(int type) {
        if (type == 1) { //如果是请求握手
            //MessageUtil.sendTipMessage(sessionId,Extras.TIP_VALUE_RESPONSE_HAND_SHAKE);
            sendTipMessage(Extras.TIP_TYPE_RESPONSE_HAND_SHAKE, null);
        } else if (type == 2) {//应答握手
            hasResponse = true;
            onChatStart();
            User user = DataManager.getInstance().getUser();
            if (user != null) {
                if (InquiryTypeEnum.typeVideo == queueResult.getInquiryType()) {
                    MessageUtil.sendP2PMessage(sessionId, user.getRealName() + "已重新连接，请医生及时发起视频问诊");
                } else if (InquiryTypeEnum.typeText == queueResult.getInquiryType()) {
                    MessageUtil.sendP2PMessage(sessionId, user.getRealName() + "已重新连接，请医生及时进行问诊操作");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);

        MyLog.i("---注销来电监听---");
        if (queueResult.getInquiryType() == InquiryTypeEnum.typeVideo) {
//            avChatModule.hangUp();
//            avChatModule.enableAVChat(false);
//            avChatModule = null;
        }

        if (startAskPanelController != null) {
            startAskPanelController.destroy();
        }

        stopRepeatTask();

        super.onDestroy();
    }

    /**
     * 执行重复任务
     */
    private void initRepeatTask() {
        taskTimer = new RepeatTaskTimer();
        taskTimer.startTask(new RepeatTaskTimer.TaskCallback() {
            @Override
            public void onExecute(int time) {
                postQueueNumber();
            }
        }, 5000, 10 * 1000);
    }

    private void stopRepeatTask(){
        if (taskTimer != null) {
            taskTimer.stopTask();
            taskTimer = null;
        }
    }

    /**
     * 初始化图文聊天倒计时
     */
    private void initTextChatTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvRightButton.setEnabled(true);
                tvRightButton.setTextColor(getResources().getColor(R.color.white));
            }
        }, DateUtils.MINUTE_IN_MILLIS);
    }

    /**
     * 查询当前医生排队数
     */
    private void postQueueNumber() {
        HttpGo.get(InquiryNetUrl.URL_QUEUE_NUMBER)
                .params("doctorId", doctorInfo.getDoctorId())
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject obj = JsonUtil.checkResult(s);
                        if (obj != null && !obj.isNull("data")) {
                            JSONObject dataObj = obj.optJSONObject("data");
                            int sum = 0;
                            if (!dataObj.isNull("inquirySum")) {
                                String inquirySum = dataObj.optString("inquirySum");
                                if (RegexUtil.isInteger(inquirySum)) {
                                    sum += Integer.parseInt(inquirySum);
                                }
                            }
                            if (!dataObj.isNull("precriptionSum")) {
                                String precriptionSum = dataObj.optString("precriptionSum");
                                if (RegexUtil.isInteger(precriptionSum)) {
                                    sum += Integer.parseInt(precriptionSum);
                                }
                            }
                            updateSum(sum);
                        }
                    }
                });
    }


    private void updateSum(int sum) {
        String sumStr = String.valueOf(sum);
        SpannableString consultSumOrange = TextViewUtil.getSpannableString(String.format(getString(R.string.doctor_status), sumStr), sumStr, orangeSpan);
//        doctorLineUpText.setText(consultSumOrange);
        if (askStatus == STATUS_UNSTART && startAskPanelController != null) {
            startAskPanelController.setRequestSum(sum);
        }
    }

    private void cancelChat(){
            DialogUtil.both(this,getString(R.string.dialog_content_cancel_consult), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelChatDialog.getInstance(doctorInfo
                        , queueResult
                        , new CancelChatDialog.ConfirmListener() {
                            @Override
                            public void onClick() {
//                                if (queueResult.getInquiryType() == InquiryTypeEnum.typeVideo && avChatModule != null) {
//                                    avChatModule.hangUp();
//                                }
                                sendTipMessage(Extras.TIP_TYPE_CANCEL_INQUIRY, null);
                                if (!DataManager.getInstance().getUser().isVisitor())
                                    goAskRecord();
                                else
                                    finish();
                            }
                        }).show(getSupportFragmentManager(), "");
            }
        });
    }

    @Override
    public void closeSessions(int exitCode) {
        SRLog.i("-------------AVChatActivity视频关闭-------------");
//        ctAvchatTime.stop();
        finish();
    }

    @Override
    public void onCalling(AVChatData data) {
        SRLog.i("-------------AVChatActivity呼叫中-------------");
//        displayByState(true);
    }

    @Override
    public void onUserJoined(String account) {
        SRLog.i("-------------AVChatActivity用户接通-------------");
        AVChatClientManager.getInstance().getAVChatClient().addRemoteIntoPreviewLayout(largeSizePreviewLayout,false);
    }

    @Override
    public void onUserLeave(String account, int event) {
        SRLog.i("-------------AVChatActivity用户离开-------------");
        AVChatClientManager.getInstance().getAVChatClient().HangUp();
    }

    @Override
    public void onCallEstablished() {
        SRLog.i("-------------AVChatActivity双方建立通话成功-------------");
//        displayByState(false);
        AVChatClientManager.getInstance().getAVChatClient().addLocalIntoPreviewLayout(smallSizePreviewLayout,true);
//        startAVChatTime();
    }

    @Override
    public void onRemoteCamerChange(int stateCode) {
        SRLog.i("-------------AVChatActivity对方摄像头状态改变-------------stateCode = "+stateCode);
    }
}
