package com.sirui.inquiry.hospital.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.data.bean.User;
import com.sirui.basiclib.widget.MyToast;
import com.sirui.inquiry.R;
import com.sirui.inquiry.R2;
import com.sirui.inquiry.hospital.bean.MedicineData;
import com.sirui.inquiry.hospital.bean.PrescriptionData;
import com.sirui.inquiry.hospital.config.InquiryNetUrl;
import com.sirui.inquiry.hospital.ui.adapter.DiagnoseResultAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的问诊结果 Fragment
 */
public class DiagnoseResultDialogFragment extends DialogFragment {

    /**
     * Fragment TAG 可用于 LOG 以及 Manager 查找 Fragment
     */
    public static final String TAG = "DIAGNOSE_RESULT_FRAGMENT";

    /**
     * 问诊订单传值 KEY
     */
    public static final String ORDER_ID = "ORDER_ID";

    /**
     * 初步诊断内容
     */
    @BindView(R2.id.tv_first_diagnose_content)
    TextView tvFirstDiagnoseContent;
    /**
     * 处理意见内容
     */
    @BindView(R2.id.tv_advice_content)
    TextView tvAdviceContent;

    /**
     * 药品处方列表容器
     */
    @BindView(R2.id.rv_prescribe)
    RecyclerView rvPrescribe;
    @BindView(R2.id.tv_patient_name_content)
    TextView tvPatientName;
    @BindView(R2.id.tv_patient_gender_content)
    TextView tvPatientGender;
    @BindView(R2.id.tv_patient_age_content)
    TextView tvPatientAge;
    @BindView(R2.id.ll_consult_record_no_data)
    LinearLayout llChecking;
    @BindView(R2.id.tv_consult_record_no_data)
    TextView tvChecking;

    /**
     * 问诊订单 ID
     */
    private String orderNo;

    /**
     * 处方对象
     */
    private PrescriptionData prescription;
    private DiagnoseResultAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderNo = bundle.getString(ORDER_ID);
            getDataFromNet(orderNo);
        }
        super.onCreate(savedInstanceState);
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
        Resources r = getResources();
        int widthInPixel = r.getDimensionPixelSize(R.dimen.dialog_result_width_out);
        int heightInPixel = r.getDimensionPixelSize(R.dimen.dialog_result_height_out);
        getDialog().getWindow().setLayout(widthInPixel, heightInPixel);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnose_result, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ButterKnife.bind(this, view);
        User user = DataManager.getInstance().getUser();
        if (user != null) {
            tvPatientName.setText(user.getRealName());
            tvPatientGender.setText(user.getSex().equals("1") ? "男" : "女");
            tvPatientAge.setText(user.getAge());
        }
        initRV();
        return view;

    }

    private void initRV() {
        rvPrescribe.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPrescribe.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DiagnoseResultAdapter(getContext(), null);
        rvPrescribe.addItemDecoration(new SpaceItemDecoration(0));
        rvPrescribe.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

/*    *//**
     * 点击返回到上层问诊记录
     *//*
    @OnClick(R.id.iv_close)
    public void backToAskRecord() {
        dismiss();
    }*/

    /**
     * 获取问诊结果数据
     *
     * @param id 问诊条目 ID
     */
    public void getDataFromNet(String id) {
        HttpGo.get(InquiryNetUrl.URL_PRESCRIPTION_DETAIL)
                .params("orderNo", id)
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject object = JsonUtil.checkResult(s);
                            if (object != null) {
                                if (!object.isNull("data")) {
                                    JSONObject dataJson = object.optJSONObject("data");
                                    prescription = new PrescriptionData();
                                    prescription.setAdvice(dataJson.optString("doctorAdvice"));
                                    prescription.setFirstDiagnose(dataJson.getString("preliminaryDiagnosis"));
                                    prescription.setPrescriptionStatus(dataJson.getString("prescriptionStatus"));
                                    JSONArray drugJsonArray = dataJson.optJSONArray("prescriptionDrugList");
                                    List<MedicineData> medicineList = new ArrayList<>();
                                    for (int i = 0; i < drugJsonArray.length(); i++) {
                                        JSONObject mJson = drugJsonArray.optJSONObject(i);
                                        MedicineData mData = new MedicineData();
                                        mData.setCreateDate(mJson.optString("createDate"));
                                        mData.setCreateId(mJson.optString("createId"));
                                        mData.setDrugId(mJson.optString("drugId"));
                                        mData.setGenericName(mJson.optString("drugName"));
                                        mData.setMedicaFrequency(mJson.optString("frequency"));
                                        mData.setOrderNo(mJson.optString("orderNo"));
                                        mData.setStatus(mJson.optString("status"));
                                        mData.setSingleDose(mJson.optString("unitDose"));
                                        mData.setSingleDoseUnit(mJson.optString("unitDoseUnits"));
                                        mData.setUpdateDate(mJson.optString("updateDate"));
                                        mData.setUpdateId(mJson.optString("updateId"));
                                        mData.setSpecifications(mJson.optString("specifications"));
                                        mData.setUsage(mJson.optString("usage"));
//                                    mData.setNumber(mJson.optString("number"));
                                        mData.setTotallAmount(mJson.optString("number"));
                                        mData.setTotallUnit(mJson.optString("numberUnits"));
                                        medicineList.add(mData);
                                    }
                                    prescription.setPrescriptionDrugList(medicineList);
                                }
                                showDetail(prescription);
                            }
                        } catch (Exception e) {
                            MyToast.show("获取就诊处方单失败");
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 展示问诊结果订单详情
     *
     * @param prescription 处方信息
     */
    public void showDetail(final PrescriptionData prescription) {
        tvAdviceContent.post(new Runnable() {
            @Override
            public void run() {
                tvAdviceContent.setText(prescription.getAdvice());
                tvFirstDiagnoseContent.setText(prescription.getFirstDiagnose());

                switch (prescription.getPrescriptionStatus()) {
                    case "3"://审核通过
                        rvPrescribe.setVisibility(View.VISIBLE);
                        llChecking.setVisibility(View.GONE);
                        mAdapter.setNewData(prescription.getPrescriptionDrugList());
                        break;
                    case "5"://关闭
                        tvChecking.setText("处方审核未通过");
                    default:
                        rvPrescribe.setVisibility(View.GONE);
                        llChecking.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            }
        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

}
