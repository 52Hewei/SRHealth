package com.sirui.inquiry.hospital.widgets;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.widget.MyToast;
import com.sirui.inquiry.R;
import com.sirui.inquiry.R2;
import com.sirui.inquiry.hospital.config.InquiryNetUrl;
import com.sirui.inquiry.hospital.ui.model.DoctorInfo;
import com.sirui.inquiry.hospital.ui.model.RequestQueueResult;
import com.sirui.inquiry.hospital.util.string.StringUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 取消问诊原因选择 Dialog
 * <p>
 * Created by yellow on 17/5/31.
 */

public class CancelChatDialog extends DialogFragment {

//    @BindView(R.id.civ_doc_avatar)
//    CircleImageView civDocAvatar;
//    @BindView(R.id.tv_doc_name)
//    TextView tvDocName;
//    @BindView(R.id.tv_doc_title)
//    TextView tvDocTitle;
//    @BindView(R.id.tv_submit)
//    TextView tvSubmit;

    @BindView(R2.id.rl_reason1)
    RelativeLayout rlReason1;
    @BindView(R2.id.rl_reason2)
    RelativeLayout rlReason2;
    @BindView(R2.id.rl_reason3)
    RelativeLayout rlReason3;
    @BindView(R2.id.rl_reason4)
    RelativeLayout rlReason4;
    @BindView(R2.id.rl_reason5)
    RelativeLayout rlReason5;
    @BindView(R2.id.rl_reason6)
    RelativeLayout rlReason6;

    private SparseArrayCompat<String> reasonArray;
    private DoctorInfo doctorInfo;
    private RequestQueueResult consultInfo;
    private ConfirmListener confirmListener;

    private int[] iconsId = {R.id.iv_reason_icon1,R.id.iv_reason_icon2,R.id.iv_reason_icon3,R.id.iv_reason_icon4,R.id.iv_reason_icon5,R.id.iv_reason_icon6};
    private int[] contentId = {R.id.tv_reason_content1,R.id.tv_reason_content2,R.id.tv_reason_content3,R.id.tv_reason_content4,R.id.tv_reason_content5,R.id.tv_reason_content6};

    public CancelChatDialog() {

    }

    public static CancelChatDialog getInstance(@NonNull DoctorInfo doctorInfo,
                                               @NonNull RequestQueueResult consultInfo, ConfirmListener listener) {
        CancelChatDialog dialog = new CancelChatDialog();
        dialog.setDoctorInfo(doctorInfo);
        dialog.setConsultInfo(consultInfo);
        dialog.setConfirmListener(listener);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        View rootView = inflater.inflate(R.layout.layout_video_chat_cancel_reason, container);
        ButterKnife.bind(this, rootView);
        reasonArray = new SparseArrayCompat<>();
//        if (doctorInfo != null) {
//            tvDocName.setText(doctorInfo.getDoctorName());
//            tvDocTitle.setText(doctorInfo.getTitleName());
//            ImageLoader.loadImg(doctorInfo.getHeadImageUrl(), civDocAvatar, R.drawable.icon_avatar_doctor);
//        }
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (window != null) {
            // 去除默认自带的 TITLE
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }

    @Override
    public void onResume() {
//        Resources r = getResources();
//        int widthInPixel = (int) r.getDimension(R.dimen.chat_cancel_dialog_width);
//        getDialog().getWindow().setLayout(widthInPixel, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.onResume();
    }

    @OnClick({R2.id.rl_reason1, R2.id.rl_reason2, R2.id.rl_reason3,
            R2.id.rl_reason4, R2.id.rl_reason5, R2.id.rl_reason6})
    public void onReasonClick(View v) {
//        ImageView icon = ButterKnife.findById(v, R.id.iv_reason_icon);
//        TextView content = ButterKnife.findById(v, R.id.tv_reason_content);
        int index = Integer.parseInt((String)v.getTag());
        ImageView icon = ButterKnife.findById(v,iconsId[index]);
        TextView content = ButterKnife.findById(v,contentId[index]);
        if (v.isSelected()) {
            icon.setVisibility(View.GONE);
            content.setSelected(false);
            reasonArray.delete(v.getId());
            v.setSelected(false);
        } else {
            if (reasonArray.size() < 2) {
                icon.setVisibility(View.VISIBLE);
                content.setSelected(true);
                reasonArray.put(v.getId(), content.getText().toString());
                v.setSelected(true);
            }
        }
    }

    @OnClick({R2.id.tv_submit, R2.id.tv_cancel})
    public void onSubmitClick(View v) {
        if(v.getId() == R.id.tv_submit){
            String reasonStr = "";
            for (int i = 0; i < reasonArray.size(); i++) {
                String s = reasonArray.valueAt(i);
                if (!StringUtil.isEmpty(s)) {
                    reasonStr = reasonStr + s + ";";
                }
            }
            if (!StringUtil.isEmpty(reasonStr)) {
                HttpGo.get(InquiryNetUrl.URL_ORDER_CANCEL)
                        .params("orderNo", consultInfo.getOrderNo())
                        .params("cancelType", "2")
                        .params("reason", reasonStr)
                        .execute(new HttpListener() {
                            @Override
                            public void onSuccess(String s) {
                                JSONObject obj = JsonUtil.checkResult(s);
                                if (obj != null) {
                                    confirmListener.onClick();
                                    dismiss();
                                }
                            }
                        });
            } else {
              //  DialogUtil.confirm("请选择至少一个取消原因", null);
                 MyToast.show("请选择至少一个取消原因");
            }
        }else if(v.getId() == R.id.tv_cancel){
            this.dismissAllowingStateLoss();
        }
    }

    public void setDoctorInfo(DoctorInfo info) {
        this.doctorInfo = info;
    }

    public void setConsultInfo(RequestQueueResult info) {
        this.consultInfo = info;
    }

    public void setConfirmListener(ConfirmListener listener) {
        this.confirmListener = listener;
    }

    public interface ConfirmListener {
        void onClick();
    }

}
