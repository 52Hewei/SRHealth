package com.sirui.inquiry.hospital.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.data.bean.User;
import com.sirui.basiclib.utils.TextViewUtil;
import com.sirui.inquiry.R;

/**
 * Created by xiepc on 2018/4/10 10:46
 */

public class InquiryDialogUtil {

    /**
     * 显示诊断详情dialog
     */
    public static void diagnosis(Context context,CharSequence diagnosis, CharSequence advise) {
        ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(R.color.black_333333));

        final Dialog dialog = new Dialog(context, R.style.Dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_diagnosis_detail_layout, null);
        TextView firstDiagnosisText = (TextView) view.findViewById(R.id.firstDiagnosisText);
        TextView adviseText = (TextView) view.findViewById(R.id.adviseText);
        TextView patientName = (TextView) view.findViewById(R.id.tv_patient_name);
        TextView patientGender = (TextView) view.findViewById(R.id.tv_patient_gender);
        TextView patientAge = (TextView) view.findViewById(R.id.tv_patient_age);
        Button closeBtn = (Button) view.findViewById(R.id.closeBtn);
//        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        firstDiagnosisText.setText(TextViewUtil.getSpannableString(String.format("%s%s", context.getString(R.string.first_diagnosis_colon)
                , diagnosis)
                , diagnosis
                , span));
        adviseText.setText(advise);
        User user = DataManager.getInstance().getUser();
        if (user != null) {
            patientName.setText(TextViewUtil.getSpannableString(String.format("%s%s", context.getString(R.string.patient_name)
                    , user.getRealName())
                    , user.getRealName()
                    , span));
            String gender = user.getSex().equals("1") ? "男" : "女";
            patientGender.setText(TextViewUtil.getSpannableString(String.format("%s%s", context.getString(R.string.patient_gender)
                    , gender)
                    , gender
                    , span));
            patientAge.setText(TextViewUtil.getSpannableString(String.format("%s%s", context.getString(R.string.patient_age)
                    , user.getAge())
                    , user.getAge()
                    , span));
        }
        Window window = dialog.getWindow();
        if (window != null) {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        Resources r = dialog.getContext().getResources();
        int widthInPixel = r.getDimensionPixelSize(R.dimen.dialog_diagnosis_out);
        window.setLayout(widthInPixel, lp.height);
        lp.gravity = Gravity.CENTER;
        dialog.show();
    }


}
