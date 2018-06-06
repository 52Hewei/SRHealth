package com.sirui.inquiry.hospital.chat;

/**
 * Created by xiepc on 2017/2/21 11:53
 */

public interface P2PChatStateListener {

    void onChatStart();

    /**
     * 结束问诊回调
     * @param hasPrescription  是否需要开处方
     */
     void onChatFinish(boolean hasPrescription, String diagnose, String advice);

    /**
     * 对方取消问诊通知回调
     */
    void onCancelInquiry();

    /**1:请求握手  2：应答握手*/
    void onHandShake(int type);
}
