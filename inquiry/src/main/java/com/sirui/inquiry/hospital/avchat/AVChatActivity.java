package com.sirui.inquiry.hospital.avchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.net.client.event.MainEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.sirui.basiclib.utils.NetWorkUtil;
import com.sirui.basiclib.utils.SRLog;
import com.sirui.basiclib.widget.SRToast;
import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.avchat.event.EventType;
import com.sirui.inquiry.hospital.cache.IMCache;

import org.greenrobot.eventbus.EventBus;


public class AVChatActivity extends AVChatBaseActivity implements View.OnClickListener,AVChatStateListener {

    /**
     * 来自广播
     */
    public static final int FROM_BROADCASTRECEIVER = 0;
    /**
     * 来自发起方
     */
    public static final int FROM_INTERNAL = 1;
    /**
     * 来自通知栏
     */
    public static final int FROM_NOTIFICATION = 2;
    /**
     * 未知的入口
     */
    public static final int FROM_UNKNOWN = -1;

//    private AVChatModule avChatModule;

    private View rootView;

    private String toAccount;

    private FrameLayout avChatContainer;

    // view
    private LinearLayout largeSizePreviewLayout;
    private LinearLayout smallSizePreviewLayout;
    /**小窗口视图容器*/
    private FrameLayout smallSizeFrameLayout;
    /**小窗口显示对方摄像头关闭的状态*/
    private ImageView smallNotificationImg;
    /**大窗对方关闭摄像头时提示语*/
    private TextView largeNotificationText;
    /**挂断*/
    private TextView tvHangUp;
    private TextView tvCall;
    private TextView tvMicSwicth;
    private TextView tvCarmreSwitch;
    private TextView tvCallingName;
    private ImageView ivSwitchWindow;

    private Chronometer ctAvchatTime;
    /**是开始发起视频，还是恢复大窗口模式*/
    private int type;
    /**视频通话的布局*/
//    private RelativeLayout rtAvchatSurface;
    private RelativeLayout rtAvchatCallingHead;
    private RelativeLayout rtAvchatRoot;

    private AVChatData avChatData;

    private boolean isFirst;


    public static void inComingStart(Context context,AVChatData data){
        Intent intent = new Intent(context, AVChatActivity.class);
        intent.putExtra("type", EventType.START_INCOMING);
        intent.putExtra("avChatData",data);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void start(Context context,int type,String toAccount,boolean isFirst){
        Intent intent = new Intent(context, AVChatActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("toAccount",toAccount);
        intent.putExtra("isFirst",isFirst);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
        findViews();
        checkCalling();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_avchat_call;
    }

    @Override
    protected void iniData() {

    }

    @Override
    protected void iniView() {

    }

    @Override
    protected void switchFloatWindow() {
        EventBus.getDefault().post(new MainEvent(EventType.LARGE_TO_SMALL_WINDOW));
        finish();
    }

    private void parseIntent(){
        type = getIntent().getIntExtra("type",0);
        toAccount = getIntent().getStringExtra("toAccount");
        isFirst = getIntent().getBooleanExtra("isFirst",false);
        if (type == EventType.START_INCOMING){
            avChatData = (AVChatData) getIntent().getSerializableExtra("avChatData");
        }
    }

    private void checkCalling(){
        if(!NetWorkUtil.isNetworkConnected()){
            SRToast.show("无网络状态");
            finish();
            return;
        }
        if(type == EventType.SMALL_TO_LARGE_WINDOW){
                if(AVChatClientManager.getInstance().getAVChatClient().isLocalPreviewInSmallSize()){//如果本地视频是在小窗口中
                    AVChatClientManager.getInstance().getAVChatClient().addRemoteIntoPreviewLayout(largeSizePreviewLayout,false);
                    AVChatClientManager.getInstance().getAVChatClient().addLocalIntoPreviewLayout(smallSizePreviewLayout,true);
                }else{
                    AVChatClientManager.getInstance().getAVChatClient().addRemoteIntoPreviewLayout(smallSizePreviewLayout,true);
                    AVChatClientManager.getInstance().getAVChatClient().addLocalIntoPreviewLayout(largeSizePreviewLayout,false);
                }
               displayByState(false);
               startAVChatTime();
              delayAVChatStateListener();
        }else if(type == EventType.START_INCOMING){ //来电接听
            // tvCall.setVisibility(View.VISIBLE);
             setAVChatStateListener();

             AVChatClientManager.getInstance().incomingAVChat(avChatData); //自动接听了
             tvCall.setVisibility(View.GONE);
        }else if(type == EventType.START_OUT_CALL){ //去电接听
            setAVChatStateListener();
            AVChatClientManager.getInstance().outGoingAVChat(toAccount);
            AVChatClientManager.getInstance().setToAccount(toAccount);
//            if(isFirst){
//                tvHangUp.setVisibility(View.GONE);
//            }
        }
    }

    private void  delayAVChatStateListener(){
        //加载完再注册监听，否则会被Service中onDestroy中空注册覆盖
        smallSizeFrameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                 setAVChatStateListener();
            }
        });
    }

    private void setAVChatStateListener(){
        if(AVChatClientManager.getInstance().getAVChatClient() != null){
            AVChatClientManager.getInstance().getAVChatClient().setAVChatStateListener(AVChatActivity.this);
        }
    }

    private void  findViews(){
        rtAvchatRoot = (RelativeLayout) findViewById(R.id.rt_avchat_root);
        smallSizePreviewLayout = (LinearLayout) findViewById(R.id.small_size_preview);
        largeSizePreviewLayout = (LinearLayout) findViewById(R.id.large_size_preview);
        smallSizeFrameLayout = (FrameLayout) findViewById(R.id.smallSizeFrameLayout);
        largeNotificationText = (TextView) findViewById(R.id.largeNotificationText);
        smallNotificationImg = (ImageView) findViewById(R.id.smallNotificationImg);
        ctAvchatTime = (Chronometer) findViewById(R.id.ct_avchat_time);
        tvCallingName = (TextView) findViewById(R.id.tv_calling_name);
        tvHangUp = (TextView) findViewById(R.id.tv_hangUp);
        tvCall = findViewById(R.id.tv_call);
        tvMicSwicth = (TextView) findViewById(R.id.tv_mic_swicth);
        tvCarmreSwitch = (TextView) findViewById(R.id.tv_carmre_switch);
        ivSwitchWindow = (ImageView) findViewById(R.id.iv_switch_window);
        rtAvchatCallingHead = (RelativeLayout) findViewById(R.id.rt_avchat_calling_head);
        tvHangUp.setOnClickListener(this);
        tvCall.setOnClickListener(this);
        ivSwitchWindow.setOnClickListener(this);
        tvMicSwicth.setOnClickListener(this);
        tvCarmreSwitch.setOnClickListener(this);
        smallSizeFrameLayout.setOnClickListener(this);
        tvCallingName.setText(IMCache.getInstance().getToNickName());
    }

    private void startAVChatTime(){
        ctAvchatTime.setBase(AVChatClientManager.getInstance().getAVChatClient().getTimeBase());
        ctAvchatTime.start();
    }

    private void displayByState(boolean isCalling){
        if(isCalling){
            ivSwitchWindow.setVisibility(View.GONE);
            ctAvchatTime.setVisibility(View.GONE);
            rtAvchatCallingHead.setVisibility(View.VISIBLE);
            tvMicSwicth.setVisibility(View.GONE);
            tvCarmreSwitch.setVisibility(View.GONE);
        }else{
            ivSwitchWindow.setVisibility(View.VISIBLE);
            ctAvchatTime.setVisibility(View.VISIBLE);
            rtAvchatRoot.setBackgroundColor(getResources().getColor(R.color.black));
            rtAvchatCallingHead.setVisibility(View.GONE);
            tvHangUp.setVisibility(View.VISIBLE);
            tvMicSwicth.setVisibility(View.VISIBLE);
            tvCarmreSwitch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_hangUp){
            AVChatClientManager.getInstance().getAVChatClient().HangUp();
            finish();
        }else if (id == R.id.tv_mic_swicth){
            tvMicSwicth.setSelected(!tvMicSwicth.isSelected());
            AVChatClientManager.getInstance().getAVChatClient().toggleMute();
        }else if (id == R.id.tv_carmre_switch){
            if( AVChatClientManager.getInstance().getAVChatClient().hasMultipleCameras()){
                tvCarmreSwitch.setSelected(!tvCarmreSwitch.isSelected());
                AVChatClientManager.getInstance().getAVChatClient().switchCamera();
            }else{
                SRToast.show("该设备未有多个摄像头");
            }
        }else if (id == R.id.iv_switch_window){
            checkPermission();
        }else if (id == R.id.smallSizeFrameLayout){
            AVChatClientManager.getInstance().getAVChatClient().swichPreviewLayout();
        }else if (id == R.id.tv_call){
//            setAVChatStateListener();
            AVChatClientManager.getInstance().incomingAVChat(avChatData);
            tvCall.setVisibility(View.GONE);
        }
    }

    @Override
    public void closeSessions(int exitCode) {
           SRLog.i("-------------AVChatActivity视频关闭-------------");
           ctAvchatTime.stop();
           finish();
    }

    @Override
    public void onCalling(AVChatData data) {
        SRLog.i("-------------AVChatActivity呼叫中-------------");
        displayByState(true);
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
        displayByState(false);
        AVChatClientManager.getInstance().getAVChatClient().addLocalIntoPreviewLayout(smallSizePreviewLayout,true);
        startAVChatTime();
    }

    @Override
    public void onRemoteCamerChange(int stateCode) {
        SRLog.i("-------------AVChatActivity对方摄像头状态改变-------------stateCode = "+stateCode);
    }

    @Override
    public void onBackPressed() {
    }
}
