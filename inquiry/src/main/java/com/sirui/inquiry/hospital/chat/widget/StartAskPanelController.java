package com.sirui.inquiry.hospital.chat.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.net.client.HttpDialogListener;
import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.net.client.HttpRequest;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.data.AccountManager;
import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.data.bean.User;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.utils.ViewUtil;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.chat.PatientAVChatActivity;
import com.sirui.inquiry.hospital.config.InquiryNetUrl;
import com.sirui.inquiry.hospital.manager.LoginManager;
import com.sirui.inquiry.hospital.ui.model.DoctorInfo;
import com.sirui.inquiry.hospital.ui.model.RequestQueueResult;

import org.json.JSONObject;

import okhttp3.Response;


/**
 * Created by xiepc on 2017/2/23 11:29
 */

public class StartAskPanelController {

    private final String mChiefComplaintStr;
    private ViewGroup containerLayout;
    private View contentView;
    private TextView remindText;
    private TextView timeText;
    private TextView cancelInquestText;
    private TextView changeDoctorText;
    private RequestQueueResult requestQueueResult;
    private DoctorInfo mDoctorInfo;
    private HttpRequest mChangeDoctorReq;
    private boolean isChangingDoctor;
    private int consultType;

    public StartAskPanelController(ViewGroup containerLayout, RequestQueueResult requestQueueResult, DoctorInfo doctorInfo, String chiefComplaintStr,int consultType) {
        this.containerLayout = containerLayout;
        this.requestQueueResult = requestQueueResult;
        this.mDoctorInfo = doctorInfo;
        this.mChiefComplaintStr = chiefComplaintStr;
        this.consultType = consultType;
        initView();
    }

    public void initView() {
        contentView = LayoutInflater.from(containerLayout.getContext()).inflate(R.layout.layout_remind_panel_start, null);
        remindText = (TextView) contentView.findViewById(R.id.startRemindText);
        timeText = (TextView) contentView.findViewById(R.id.timeText);
        cancelInquestText = (TextView) contentView.findViewById(R.id.cancelInquestText);
        changeDoctorText = (TextView) contentView.findViewById(R.id.changeDoctorText);
        cancelInquestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelQueue();
                ViewUtil.setPostEnableClick(cancelInquestText, 5000);
            }
        });
        changeDoctorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDoctor();
            }
        });
        setRequestSum(Integer.valueOf(requestQueueResult.getCount()));
    }

    public void changeDoctor() {
        final Activity activity = (Activity) containerLayout.getContext();
        DialogUtil.both(activity, "是否同意系统为您自动匹配医生", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postChangeDoctor(activity);
            }
        });
        ViewUtil.setPostEnableClick(changeDoctorText, 5000);
    }


    private void postChangeDoctor(Activity activity){
        mChangeDoctorReq = HttpGo.post(InquiryNetUrl.CHANGE_DOCTOR)
                .params("patientId", DataManager.getInstance().getUser().getPatientId())
                .params("storeId", SRConstant.STORE_ID)
                .params("departmentId", mDoctorInfo.getDepartmentId())
                .params("orderNo", requestQueueResult.getOrderNo())
                .params("doctorId", mDoctorInfo.getDoctorId())
                .params("inqMethod",String.valueOf(consultType));

        mChangeDoctorReq.execute(new HttpDialogListener(activity) {

            @Override
            public void onSuccess(String s) {
                DoctorInfo info = JsonUtil.checkToGetData(s, DoctorInfo.class);
                if (info == null) {
                    return;
                }
                isChangingDoctor = true;
                getDoctorDetail(info.getDoctorId());
            }

            @Override
            public void onFailure(Response response, Exception e) {
                isChangingDoctor = false;
                super.onFailure(response, e);
            }
        });
    }

    private void getDoctorDetail(String doctorId) {
        HttpGo.get(InquiryNetUrl.URL_DOCTOR_DETAIL)
                .tag(this)
                .params("doctorId", doctorId)
                .execute(new HttpDialogListener((Activity) containerLayout.getContext()) {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject obj = JsonUtil.checkResult(s);
                        if (obj != null && !obj.isNull("data")) {
                            mDoctorInfo = new DoctorInfo(obj.optJSONObject("data"));
                            Activity activity = (Activity) containerLayout.getContext();
                            PatientAVChatActivity.start(activity, mDoctorInfo, requestQueueResult, mChiefComplaintStr);
                        }
                    }
                });
    }

    public View getView() {
        return contentView;
    }

    /**
     * 取消排队
     */
    public void cancelQueue() {
        HttpGo.get(InquiryNetUrl.URL_CANCEL_REQUEST_QUEUE)
                .params("orderNo", requestQueueResult.getOrderNo())
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject obj = JsonUtil.checkResult(s);
                        if (obj != null) {
                            MyLog.i("取消排队成功");
                            Activity activity = (Activity) containerLayout.getContext();
                            User user = DataManager.getInstance().getUser();
                            if (user != null
                                    && user.isVisitor()) {
                                AccountManager.getInstance().loginOutMember();
                            }
                            activity.finish();
                        }
                    }
                });
    }

    public void setRequestSum(int sum) {
        requestQueueResult.setCount(String.valueOf(sum));
        String queueRemind = String.format(containerLayout.getContext().getString(R.string.queue_number_remind), requestQueueResult.getCount());
        String timeRemind;
        if (sum != 0) {
            int time = sum * 3;
            timeRemind = "预计等待时长:" + (time > 20 ? 20 : time) + "分钟";
        } else {
            timeRemind = "医生马上为您接诊，请稍候";
        }
        remindText.setText(queueRemind);
        timeText.setText(timeRemind);
    }

    public void destroy() {
        if (mChangeDoctorReq != null)
            mChangeDoctorReq.cancel();
        if (!isChangingDoctor) {
            User user = DataManager.getInstance().getUser();
            if (user != null && user.isVisitor()) {
                AccountManager.getInstance().loginOutMember();
            }
            new LoginManager().loginOut();
        }
    }
}
