package com.sirui.inquiry.hospital.avchat;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.net.client.event.MainEvent;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.utils.SRLog;
import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.avchat.constants.AVChatExitCode;
import com.sirui.inquiry.hospital.avchat.event.EventType;
import com.sirui.inquiry.hospital.cache.IMCache;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.netease.nimlib.sdk.msg.constant.SystemMessageStatus.init;

/**
 * Created by xiepc on  2017-08-27 17:03
 */

public class AVChatUI {

    private Context context;

    private AVChatData avChatData;

    private String videoAccount; // 发送视频请求，onUserJoin回调的user account

    private boolean destroyRTC = false;

    private AVChatCameraCapturer mVideoCapturer;

    public AtomicBoolean isCallEstablish = new AtomicBoolean(false);

    private AVChatStateListener stateListener;

    //render
    //render
    private AVChatSurfaceViewRenderer localRender;
    private AVChatSurfaceViewRenderer remoteRender;

    // data
    private boolean localRenderSetup = false;
    private boolean remoteRenderSetup = false;

    private boolean localPreviewInSmallSize = true;


    public AVChatUI(Context context) {
        this.context = context;
        this.localRender = new AVChatSurfaceViewRenderer(context);
        this.remoteRender = new AVChatSurfaceViewRenderer(context);
    }

    public void setAVChatStateListener(AVChatStateListener stateListener){
        this.stateListener  = stateListener;
    }

    /**
     * 关闭本地音视频各项功能
     *
     * @param exitCode 音视频类型
     */
    public void closeSessions(int exitCode) {

        if( stateListener != null){
            stateListener.closeSessions(exitCode);
        }
         showQuitToast(exitCode);
         isCallEstablish.set(false);
         closeRender(exitCode);
         setAVChatStateListener(null);
         EventBus.getDefault().post(new MainEvent(EventType.CLOSE_AVCHAT));
    }

    public void closeRender(int exitCode) {
        Log.i("AVChatUI", "closeSession,init->" + init);
            if (localRenderSetup && localRender != null && localRender.getParent() != null) {
                ((ViewGroup) localRender.getParent()).removeView(localRender);
                localRender = null;
            }
            if (remoteRenderSetup &&  remoteRender != null && remoteRender.getParent() != null) {
                ((ViewGroup) remoteRender.getParent()).removeView(remoteRender);
                remoteRender = null;
            }
        }


    public void closeRtc() {
        if(destroyRTC) {
            return;
        }
//        if (callingState == CallStateEnum.OUTGOING_VIDEO_CALLING || callingState == CallStateEnum.VIDEO) {
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();
//        }
        AVChatManager.getInstance().disableRtc();
//        DialogMaker.dismissProgressDialog();
        AVChatSoundPlayer.instance().stop();

        destroyRTC = true;
    }

    /**
     * 给出结束的提醒
     *
     * @param code
     */
    public void showQuitToast(int code) {
        switch (code) {
            case AVChatExitCode.NET_CHANGE: // 网络切换
            case AVChatExitCode.NET_ERROR: // 网络异常
            case AVChatExitCode.CONFIG_ERROR: // 服务器返回数据错误
                Toast.makeText(context, R.string.avchat_net_error_then_quit, Toast.LENGTH_SHORT).show();
                break;
            case AVChatExitCode.PEER_HANGUP:
            case AVChatExitCode.HANGUP:
                if (isCallEstablish.get()) {
                    Toast.makeText(context, R.string.avchat_call_finish, Toast.LENGTH_SHORT).show();
                }
                break;
            case AVChatExitCode.PEER_BUSY:
                Toast.makeText(context, R.string.avchat_peer_busy, Toast.LENGTH_SHORT).show();
                break;
            case AVChatExitCode.PROTOCOL_INCOMPATIBLE_PEER_LOWER:
                Toast.makeText(context, R.string.avchat_peer_protocol_low_version, Toast.LENGTH_SHORT).show();
                break;
            case AVChatExitCode.PROTOCOL_INCOMPATIBLE_SELF_LOWER:
                Toast.makeText(context, R.string.avchat_local_protocol_low_version, Toast.LENGTH_SHORT).show();
                break;
            case AVChatExitCode.INVALIDE_CHANNELID:
                Toast.makeText(context, R.string.avchat_invalid_channel_id, Toast.LENGTH_SHORT).show();
                break;
            case AVChatExitCode.LOCAL_CALL_BUSY:
                Toast.makeText(context, R.string.avchat_local_call_busy, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    /**
     *   拨打音视频
     * @param account
     * @param callTypeEnum
     */
    public void outGoingCalling(final String account, final AVChatType callTypeEnum) {

//        DialogMaker.showProgressDialog(context, null);

       // AVChatSoundPlayer.instance().play(AVChatSoundPlayer.RingerTypeEnum.CONNECTING);


        AVChatNotifyOption notifyOption = new AVChatNotifyOption();
        notifyOption.extendMessage = "extra_data";
        notifyOption.webRTCCompat = true;
//        默认forceKeepCalling为true，开发者如果不需要离线持续呼叫功能可以将forceKeepCalling设为false
//        notifyOption.forceKeepCalling = false;

        AVChatManager.getInstance().enableRtc();  //1  ------------>
        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();        //2  ------------>
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
        }
        if (callTypeEnum == AVChatType.VIDEO) {
            AVChatManager.getInstance().enableVideo();                      //3  ------------>
            AVChatManager.getInstance().startVideoPreview();              //4  ------------>
        }
//        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);   //开启预处理（可以美颜），视频会被截断在onVideoFrameFilter回调中输出
        AVChatManager.getInstance().call2(account, callTypeEnum, notifyOption, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData data) {
                avChatData = data;
//                DialogMaker.dismissProgressDialog();
                //如果需要使用视频预览功能，在此进行设置，调用setupLocalVideoRender
                //如果不需要视频预览功能，那么删掉下面if语句代码即可
                if (callTypeEnum == AVChatType.VIDEO) {
//                    List<String> deniedPermissions = BaseMPermission.getDeniedPermissions((Activity) context, BASIC_PERMISSIONS);
//                    if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
//                        //avChatVideo.showNoneCameraPermissionView(true);
//                        MyLog.i("---无摄像头权限---");
//                        return;
//                    }
                    Log.i("xpc","onSuccess--initLargeSurfaceView"+ IMCache.getInstance().getAccount());
                        if(stateListener !=  null){
                            stateListener.onCalling(data);
                       }
                }
            }

            @Override
            public void onFailed(int code) {
                SRLog.i(SRConstant.TAG, "avChat call failed code->" + code);

                if (code == ResponseCode.RES_FORBIDDEN) {
                    Toast.makeText(context, R.string.avchat_no_permission, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.avchat_call_failed, Toast.LENGTH_SHORT).show();
                }
                closeRtc();
                closeSessions(-1);
            }

            @Override
            public void onException(Throwable exception) {
                SRLog.i(SRConstant.TAG, "avChat call onException->" + exception);
                closeRtc();
                closeSessions(-1);
            }
        });
    }
    /**
     * 接听来电
     */
    public void receiveInComingCall(final AVChatData avData) {
        //接听，告知服务器，以便通知其他端
        AVChatManager.getInstance().enableRtc();
        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
        }
//        AVChatManager.getInstance().setParameters(avChatParameters);
            AVChatManager.getInstance().enableVideo();
            AVChatManager.getInstance().startVideoPreview();

//        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);
        AVChatManager.getInstance().accept2(avData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                  avChatData = avData;
                 isCallEstablish.set(true);
//                canSwitchCamera = true;
            }

            @Override
            public void onFailed(int code) {
                if (code == -1) {
                    Toast.makeText(context, "本地音视频启动失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "建立连接失败", Toast.LENGTH_SHORT).show();
                }
//                LogUtil.e(TAG, "accept onFailed->" + code);
//                handleAcceptFailed();
            }

            @Override
            public void onException(Throwable exception) {
                Log.i("sirui","---视频接通异常--");
            }
        });
        AVChatSoundPlayer.instance().stop();
    }


    public void onHangUp() {
        if (isCallEstablish.get()) {
            hangUp(AVChatExitCode.HANGUP);   //如果是对方发起的视频
        } else {
            hangUp(AVChatExitCode.CANCEL);   //如果是自己发起的视频，则叫取消
        }
    }
    /**
     * 挂断
     * @param type 音视频类型
     */
    public void hangUp(final int type) {
        if(destroyRTC) {
            return;
        }
         AVChatManager.getInstance().stopVideoPreview();
        if ((type == AVChatExitCode.HANGUP || type == AVChatExitCode.PEER_NO_RESPONSE || type == AVChatExitCode.CANCEL) && avChatData != null) {
            AVChatManager.getInstance().hangUp2(avChatData.getChatId(), new AVChatCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }

                @Override
                public void onFailed(int code) {
                    Log.d(SRConstant.TAG, "hangup onFailed->" + code);
                }

                @Override
                public void onException(Throwable exception) {
                    Log.d(SRConstant.TAG, "hangup onException->" + exception);
                }
            });
        }
        AVChatManager.getInstance().disableRtc();
        destroyRTC = true;
        closeSessions(type);
        AVChatSoundPlayer.instance().stop();
    }

    /**
     * 前后摄像头切换开关
     */
    public void switchCamera(){
        mVideoCapturer.switchCamera();
    }

    public boolean hasMultipleCameras(){
       return mVideoCapturer.hasMultipleCameras();
    }

    /**
     * 音频开关
     */
   public void toggleMute(){
        if (!isCallEstablish.get()) { // 连接未建立，在这里记录静音状态
            return;
        } else { // 连接已经建立
            if (!AVChatManager.getInstance().isLocalAudioMuted()) { // isMute是否处于静音状态
                // 关闭音频
                AVChatManager.getInstance().muteLocalAudio(true);
            } else {
                // 打开音频
                AVChatManager.getInstance().muteLocalAudio(false);
            }
        }
    }


    public void addLocalIntoPreviewLayout(ViewGroup view,boolean isSmallSize) {
        SRLog.i("------addLocalIntoPreviewLayout------");
        if(!localRenderSetup){
//             AVChatManager.getInstance().setupLocalVideoRender(localRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
             AVChatManager.getInstance().setupLocalVideoRender(localRender, false, AVChatVideoScalingType.SCALE_ASPECT_FILL);
             localRenderSetup = true;
        }
        if (localRender.getParent() != null) {
             ((ViewGroup) localRender.getParent()).removeView(localRender);
        }
         view.addView(localRender);
        if(isSmallSize){
            localRender.setZOrderMediaOverlay(true);     //将其放在其它surfaceView的上层
        }else{
            localRender.setZOrderMediaOverlay(false);
        }
    }

    public void addRemoteIntoPreviewLayout(ViewGroup view,boolean isSmallSize) {
        SRLog.i("------addRemoteIntoPreviewLayout------");
        if(!remoteRenderSetup){
//               AVChatManager.getInstance().setupRemoteVideoRender(videoAccount, remoteRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
               AVChatManager.getInstance().setupRemoteVideoRender(videoAccount, remoteRender, false, AVChatVideoScalingType.SCALE_ASPECT_FILL);
               remoteRenderSetup = true;
        }
        if (remoteRender.getParent() != null) {
            ((ViewGroup) remoteRender.getParent()).removeView(remoteRender);
        }
        view.addView(remoteRender);
        if(isSmallSize){
            remoteRender.setZOrderMediaOverlay(true);
        }else{
            remoteRender.setZOrderMediaOverlay(false);
        }
    }

    /**
     * 大小窗口视图切换
     */
//    public void swichPreviewLayout(){
//         ViewGroup localView = (ViewGroup) localRender.getParent();
//         ViewGroup remoteView = (ViewGroup) remoteRender.getParent();
//         if(localView != null ){
//              localView.removeView(localRender);
//         }
//        if(remoteView != null ){
//              remoteView.removeView(remoteRender);
//         }
//        localView.addView(remoteRender);
//        remoteView.addView(localRender);
//    }
    public void swichPreviewLayout() {

//        //先取消用户的画布
//            AVChatManager.getInstance().setupLocalVideoRender(null, false, 0);
//            AVChatManager.getInstance().setupRemoteVideoRender(receiverId,null, false, 0);
//        //交换画布
//        //如果存在多个用户,建议用Map维护account,render关系.
//        //目前只有两个用户,并且认为这两个account肯定是对的
//        AVChatSurfaceViewRenderer render1;
//        AVChatSurfaceViewRenderer render2;
//        //重新设置上画布
//        if (localPreviewInSmallSize) {
//                 render1 = remoteRender;
//                 render2 = localRender;
//
//        } else {
//            render1 = remoteRender;
//            render2 = localRender;
//        }
//        AVChatManager.getInstance().setupLocalVideoRender(render1, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
//        AVChatManager.getInstance().setupRemoteVideoRender(receiverId, render2, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        ViewGroup containerView1 = (ViewGroup) remoteRender.getParent();
        ViewGroup  containerView2 = (ViewGroup) localRender.getParent();
         if(containerView1 == null || containerView2 == null){
               SRLog.i("----视频容器为空-----");
                return;
         }
          containerView1.removeView(remoteRender);
          containerView2.removeView(localRender);
          containerView2.addView(remoteRender);
          containerView1.addView(localRender);
          if(localPreviewInSmallSize){
              remoteRender.setZOrderMediaOverlay(true);
              localRender.setZOrderMediaOverlay(false);
          }else{
              remoteRender.setZOrderMediaOverlay(false);
              localRender.setZOrderMediaOverlay(true);
          }
          localPreviewInSmallSize =!localPreviewInSmallSize;
    }

    public String getVideoAccount() {
        return videoAccount;
    }

    public void setVideoAccount(String videoAccount) {
        this.videoAccount = videoAccount;
    }

    public AVChatData getAvChatData() {
        return avChatData;
    }

    public void setAvChatData(AVChatData avChatData) {
        this.avChatData = avChatData;
    }

    public boolean isLocalPreviewInSmallSize() {
        return localPreviewInSmallSize;
    }
}
