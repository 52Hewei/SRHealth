package com.sirui.inquiry.hospital.chat.constant;

public interface Extras {

	// 参数
	String EXTRA_SESSIONID = "sessionid";
	String EXTRA_TYPE = "type";
	String EXTRA_ANCHOR = "anchor";

	/**发送的tip消息内容字典,结束问诊并且不开处方*/
	String  TIP_TYPE_OVER_INQUIRY_NO_PRESCRIPTION = "over_inquiry_no_prescription";
	/**发送的tip消息内容字典,结束问诊并且开处方*/
	String  TIP_TYPE_OVER_INQUIRY_HAS_PRESCRIPTION = "over_inquiry_has_prescription";
	/**发送的tip消息内容取消问诊消息*/
	String  TIP_TYPE_CANCEL_INQUIRY = "cancel_inquiry";
	/**发送的tip消息开始问诊*/
	String  TIP_TYPE_START_INQUIRY = "start_inquiry";
	/**请求握手消息*/
	String  TIP_TYPE_REQUEST_HAND_SHAKE = "request_hand_shake";
	/**回复握手消息*/
	String  TIP_TYPE_RESPONSE_HAND_SHAKE = "response_hand_shake";

	String EXTRA_REQUEST_QUEUE = "requestQueueResult";
	String EXTRA_DOCTOR_INFO = "doctorInfo";

	String  ONLINE = "1";
	/**没有处方查看*/
	String  NO_PRESCRIPTION = "0";
	/** 患者主诉 */
	String EXTRA_CHIEF_COMPLAINT = "EXTRA_CHIEF_COMPLAINT";
}
