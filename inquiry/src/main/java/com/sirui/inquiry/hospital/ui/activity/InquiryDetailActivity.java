package com.sirui.inquiry.hospital.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.net.client.HttpDialogListener;
import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.widget.MyToast;
import com.sirui.inquiry.R;
import com.sirui.inquiry.R2;
import com.sirui.inquiry.hospital.bean.MedicineData;
import com.sirui.inquiry.hospital.bean.PrescriptionData;
import com.sirui.inquiry.hospital.chat.constant.InquiryTypeEnum;
import com.sirui.inquiry.hospital.config.InquiryNetUrl;
import com.sirui.inquiry.hospital.ui.adapter.DiagnoseResultAdapter;
import com.sirui.inquiry.hospital.ui.model.InquiryRecordInfo;
import com.sirui.inquiry.hospital.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author hw
 *         问诊详情
 */
public class InquiryDetailActivity extends BaseActivity {

    @BindView(R2.id.nameText)
    TextView nameText;
    @BindView(R2.id.sexText)
    TextView sexText;
    @BindView(R2.id.ageText)
    TextView ageText;
    @BindView(R2.id.kemuText)
    TextView kemuText;
    @BindView(R2.id.inquiryText)
    TextView inquiryText;
    @BindView(R2.id.inquiryTimeText)
    TextView inquiryTimeText;
    @BindView(R2.id.inquiryStatusText)
    TextView inquiryStatusText;
    /**
     * 药品处方列表容器
     */
    @BindView(R2.id.rv_prescribe)
    RecyclerView rvPrescribe;
    @BindView(R2.id.ll_consult_record_no_data)
    LinearLayout llChecking;
    @BindView(R2.id.tv_consult_record_no_data)
    TextView tvChecking;
    @BindView(R2.id.rpInfo_rl)
    RelativeLayout rpInfoRl;
    //处方详情
    private PrescriptionData prescription;
    private DiagnoseResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inquiry_detail;
    }

    @Override
    protected void iniData() {

    }

    @Override
    protected void iniView() {
        initTitle("问诊详情");
        setData();
    }

    private void setData() {
        initRV();
        InquiryRecordInfo info = (InquiryRecordInfo) getIntent().getSerializableExtra("InquiryInfo");
        if (info != null) {
            nameText.setText(info.getPatientName());
            if (info.getSex().equals("1")) {
                sexText.setText("男");
            } else {
                sexText.setText("女");
            }
            ageText.setText(info.getAge());
            kemuText.setText(info.getDeptName());
            if (InquiryTypeEnum.typeText.getValue().equals(info.getInquiryType())) {
                inquiryText.setText("图文");
            } else if (InquiryTypeEnum.typeVideo.getValue().equals(info.getInquiryType())) {
                inquiryText.setText("视频");
            }
            inquiryTimeText.setText(parseTimeString(info.getQueueDate()));
            inquiryStatusText.setText(info.getOrderStatusDesc());
            getDataFromNet(info.getOrderNo());
        }
    }

    private void initRV() {
        rvPrescribe.setLayoutManager(new LinearLayoutManager(this));
        rvPrescribe.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DiagnoseResultAdapter(this, null);
        rvPrescribe.addItemDecoration(new SpaceItemDecoration(0));
        rvPrescribe.setAdapter(mAdapter);
    }

    private String parseTimeString(String millis) {
        String string;
        try {
            long timeMillis = Long.parseLong(millis);
            string = TimeUtil.millis2String(timeMillis, "MM-dd HH:mm");
        } catch (Exception e) {
            MyLog.e("日期转换出错：" + e.getMessage());
            return "";
        }
        return string;
    }

    /*
    * 获取处方详情
    * */
    public void getDataFromNet(String id) {
        HttpGo.get(InquiryNetUrl.URL_PRESCRIPTION_DETAIL)
                .params("orderNo", id)
                .execute(new HttpDialogListener(this) {
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
                                    switch (prescription.getPrescriptionStatus()) {
                                        case "3"://审核通过
                                            rpInfoRl.setVisibility(View.VISIBLE);
                                            rvPrescribe.setVisibility(View.VISIBLE);
                                            llChecking.setVisibility(View.GONE);
                                            mAdapter.setNewData(prescription.getPrescriptionDrugList());
                                            break;
                                        case "5"://关闭
                                            rpInfoRl.setVisibility(View.GONE);
                                            tvChecking.setText("处方审核未通过");
                                        default:
                                            rpInfoRl.setVisibility(View.GONE);
                                            rvPrescribe.setVisibility(View.GONE);
                                            llChecking.setVisibility(View.VISIBLE);
                                            break;
                                    }
                                }
                            }
                        } catch (Exception e) {
//                            DialogUtil.confirm("获取就诊处方单失败", null);
                            e.printStackTrace();
                            MyToast.show("获取就诊处方单失败");
                        }
                    }
                });
    }

    public static void startInquiryDetailActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, InquiryDetailActivity.class);
        context.startActivity(intent);
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

    /*
    * 获取问诊记录详情
    * */
    private void getInquiryDetail(String orderNo) {
        HttpGo.get(InquiryNetUrl.URL_RECORD_DETAIL)
                .params("orderNo", orderNo)
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject obj = JsonUtil.checkResult(s);
                        if (obj != null && !obj.isNull("data")) {
                            JSONObject dataObj = obj.optJSONObject("data");
                            try {
                                String patientName = dataObj.getString("patientName");
                                String sex = dataObj.getString("sex");
                                int age = dataObj.getInt("age");
                                String deptName = dataObj.getString("deptName");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
