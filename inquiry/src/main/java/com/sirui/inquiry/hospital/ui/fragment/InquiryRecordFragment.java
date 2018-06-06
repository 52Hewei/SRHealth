package com.sirui.inquiry.hospital.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.net.client.HttpDialogListener;
import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sirui.basiclib.BaseFragment;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.data.bean.User;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.basiclib.widget.MyToast;
import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.chat.PatientAVChatActivity;
import com.sirui.inquiry.hospital.chat.constant.InquiryTypeEnum;
import com.sirui.inquiry.hospital.config.InquiryNetUrl;
import com.sirui.inquiry.hospital.manager.LoginManager;
import com.sirui.inquiry.hospital.ui.activity.InquiryDetailActivity;
import com.sirui.inquiry.hospital.ui.adapter.RecordAskListAdapter;
import com.sirui.inquiry.hospital.ui.model.DoctorInfo;
import com.sirui.inquiry.hospital.ui.model.InquiryOrderDetail;
import com.sirui.inquiry.hospital.ui.model.InquiryRecordInfo;
import com.sirui.inquiry.hospital.ui.model.RequestQueueResult;
import com.sirui.inquiry.hospital.util.ClickLimitController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xiepc on 2017/3/6 18:31
 */

public class InquiryRecordFragment extends BaseFragment implements RecordAskListAdapter.AdapterCallback , AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener{
    /**
     * Fragment TAG 可用于 LOG 以及 Manager 查找 Fragment
     */
    public static final String TAG = "ASK_RECORD_FRAGMENT";

    private ListView recordListView;
    /**
     * 所有问诊记录
     */
    private List<InquiryRecordInfo> recordAskList = new ArrayList<>();
    /**
     * 图文问诊记录
     */
    private List<InquiryRecordInfo> recordTxtAskList = new ArrayList<>();
    /**
     * 视频问诊记录
     */
    private List<InquiryRecordInfo> recordVideoAskList = new ArrayList<>();

    private RecordAskListAdapter adapter;

//    private RelativeLayout allRecordRtLayout;
//    private RelativeLayout txtRecordRtLayout;
//    private RelativeLayout videoRecordRtLayout;
    private RelativeLayout rlConsultNoData;
    private TextView tvTitle;
    private TextView tvLeftBack;
    private SmartRefreshLayout srlToTreat;
    private View rootView;
    private ClickLimitController clickLimitController;
    private int iPageSize = 20;
    private int iPageNum = 1;
    private int itotalPage;
    private InquiryRecordFragment.BackPress backPress;

    public InquiryRecordFragment() {
    }

    @SuppressLint("ValidFragment")
    public InquiryRecordFragment(InquiryRecordFragment.BackPress backPress) {
        this.backPress = backPress;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_ask_record, container, false);
            recordListView = (ListView) rootView.findViewById(R.id.recordListView);
            initView(rootView);
            clickLimitController = new ClickLimitController(rootView);
        }
        ViewGroup parentView = (ViewGroup) rootView.getParent();
        if (parentView != null) {
            parentView.removeView(rootView);
        }
        return rootView;
    }

    private void initView(View view) {
        rlConsultNoData = (RelativeLayout) view.findViewById(R.id.rl_consult_record_no_data);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        srlToTreat = (SmartRefreshLayout) view.findViewById(R.id.srl_to_treat);
        tvLeftBack = (TextView) view.findViewById(R.id.tv_left_back);
        tvTitle.setText("问诊记录");
        tvLeftBack.setOnClickListener(listener);
        recordListView.setOnItemClickListener(this);
        recordListView.setOnItemLongClickListener(this);
        initRefreshView();
    }

    public void refresh() {
        iPageNum = 1;
        getRecordList(iPageSize,iPageNum);
    }

    private void initRefreshView() {
        srlToTreat.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (itotalPage <= iPageNum){//无更多加载
                    srlToTreat.finishLoadmoreWithNoMoreData();
                }else {
                    iPageNum++;
                    getRecordList(iPageSize, iPageNum);
                }
            }
        });
        srlToTreat.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refresh();
            }
        });
    }

    private void overRefreshAndLoad(boolean isSuccess) {
        if (!isAdded())
            return;

        if (iPageNum == 1)
            srlToTreat.finishRefresh(isSuccess);
        else
            srlToTreat.finishLoadmore(isSuccess);
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecordList(iPageSize,iPageNum);
    }

    /**
     * 跳转问诊结果点击监听
     */
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.tv_left_back){
                backPress.back();
            }
        }
    };

    private void setListData(List<InquiryRecordInfo> baseModels) {
        if (adapter != null) {
            adapter.setDoctorInfoList(baseModels);
        }
        refreshListView();
    }

    private void refreshListView() {
        if (adapter == null) {
            adapter = new RecordAskListAdapter(getActivity(), recordAskList);
            adapter.setCallback(this);
            recordListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void getRecordList(int pageSize , int pageNum) {
        if (DataManager.getInstance().getUser() == null) {
            MyLog.i("MyApplication.getInstance().getUser() == null");
            return;
        }
        HttpGo.get(InquiryNetUrl.URL_RECORD_LIST)
                .params("patientId", DataManager.getInstance().getUser().getPatientId())
                .params("pageSize",String.valueOf(pageSize))
                .params("pageNum",String.valueOf(pageNum))
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        overRefreshAndLoad(true);
                        JSONObject obj = JsonUtil.checkResult(s);
//                        recordAskList.clear();
                        recordTxtAskList.clear();
                        recordVideoAskList.clear();
                        if (obj != null && !obj.isNull("data")) {
                            JSONObject data = obj.optJSONObject("data");
                            if (iPageNum==1){//下拉刷新
                                recordAskList.clear();
                            }
                            try {
                                itotalPage=data.getInt("totalPage");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (itotalPage<=1){//无更多数据
                                srlToTreat.finishLoadmoreWithNoMoreData();
                            }else {
                                srlToTreat.resetNoMoreData();
                            }
                            JSONArray dataList = data.optJSONArray("list");
                            if (dataList != null && dataList.length() > 0) {
                                rlConsultNoData.setVisibility(View.GONE);
                                for (int i = 0; i < dataList.length(); i++) {
                                    InquiryRecordInfo recordAskInfo = new InquiryRecordInfo(dataList.optJSONObject(i));
                                    recordAskList.add(recordAskInfo);
                                    if (InquiryTypeEnum.typeText.getValue().equals(recordAskInfo.getInquiryType())) {
                                        recordTxtAskList.add(recordAskInfo);
                                    }
                                    if (InquiryTypeEnum.typeVideo.getValue().equals(recordAskInfo.getInquiryType())) {
                                        recordVideoAskList.add(recordAskInfo);
                                    }
                                }
                            } else {
                                rlConsultNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        refreshListView();
                    }
                });
    }

    /**
     * 分类问诊记录
     */
    private void classifyRecord() {
        recordTxtAskList.clear();
        recordVideoAskList.clear();
        for (InquiryRecordInfo recordAskInfo : recordAskList) {
            if (InquiryTypeEnum.typeText.getValue().equals(recordAskInfo.getInquiryType())) {
                recordTxtAskList.add(recordAskInfo);
            }
            if (InquiryTypeEnum.typeVideo.getValue().equals(recordAskInfo.getInquiryType())) {
                recordVideoAskList.add(recordAskInfo);
            }
        }
    }

    @Override
    public void onRecoverOrder(String orderNo) {  //恢复订单
        queryUnfinishedOrderDetail(orderNo);
    }

    /**
     * 查询未完成的订单详情
     *
     * @param orderNo 订单号
     */
    private void queryUnfinishedOrderDetail(String orderNo) {
        HttpGo.get(InquiryNetUrl.URL_INQUIRY_ORDER_DETAIL)
                .params("orderNo", orderNo)
                .execute(new HttpDialogListener(getActivity()) {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject obj = JsonUtil.checkResult(s);
                        if (obj != null) {
                            if (!obj.isNull("data")) {
                                InquiryOrderDetail inquiryOrderDetail = new InquiryOrderDetail(obj.optJSONObject("data"));
                                startAVChatActivity(inquiryOrderDetail);
                            }
                        }
                    }
                });
    }

    private void startAVChatActivity(InquiryOrderDetail orderDetail) {
        DoctorInfo info = new DoctorInfo();
        info.setDoctorName(orderDetail.getDoctorName());
        info.setDoctorId(orderDetail.getDoctorId());
        info.setImId(orderDetail.getDoctorIMId());
        info.setDeptName(orderDetail.getDeptName());
        info.setDepartmentId(orderDetail.getDeptId());
        info.setHistoryInquirySum(orderDetail.getHistoryInquirySum());
        RequestQueueResult requestQueueResult = new RequestQueueResult();
        requestQueueResult.setImId(orderDetail.getPatientImId());
        requestQueueResult.setToken(orderDetail.getPatientImToken());
        requestQueueResult.setOrderNo(orderDetail.getOrderNo());
        requestQueueResult.setStartType("2"); //为恢复问诊
        requestQueueResult.setInquiryType(orderDetail.getInquiryType());
        requestQueueResult.setImList(orderDetail.getImList());
        requestQueueResult.setOrderStatus(orderDetail.getOrderStatus());
        requestQueueResult.setCount(orderDetail.getQueueCount());
        requestQueueResult.setWaitSumDate(orderDetail.getWaitSumDate());
        imLogin(info, requestQueueResult, orderDetail.getChiefComplaint());
    }

    /**
     * 云信IM帐号登录
     */
    private void imLogin(final DoctorInfo info, final RequestQueueResult requestQueueResult, final String chiefComplaint) {
        LoginManager loginManager = new LoginManager();
        loginManager.login(requestQueueResult.getImId(), requestQueueResult.getToken(), new LoginManager.LoginCallback() {
            @Override
            public void onSucess(LoginInfo param) {
                MyLog.i("im帐号登录成功");
                DataManager.getInstance().getUser().setImNo(requestQueueResult.getImId());
                PatientAVChatActivity.start(getActivity(), info, requestQueueResult, chiefComplaint);
            }

            @Override
            public void onFailed(int code) {
                MyLog.i("im帐号登录失败" + code);
                //DialogUtil.confirm("IM服务登录失败", null);
                MyToast.show("IM服务登录失败");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null!=recordAskList && recordAskList.size() > 0) {
            Intent intent = new Intent(getActivity(), InquiryDetailActivity.class);
//            intent.putExtra("orderNo", recordAskList.get(position).getOrderNo());
            intent.putExtra("InquiryInfo",recordAskList.get(position));
            getActivity().startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (null!=recordAskList && recordAskList.size() > 0) {
            InquiryRecordInfo inquiryRecordInfo = recordAskList.get(position);
            if (inquiryRecordInfo!=null) {
                showDeleteConfirmDialog(inquiryRecordInfo);
            }
        }
        return true;
    }

    private void showDeleteConfirmDialog(final InquiryRecordInfo info) {
          DialogUtil.both(getActivity(), "确认删除？", new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  deleteRecord(info);
              }
          });
    }

    private void deleteRecord(final InquiryRecordInfo info) {
        User user = DataManager.getInstance().getUser();
        if (user != null) {
            String userId = user.getPatientId();
            HttpGo.post(InquiryNetUrl.DEL_INQUIRY_INFO)
                    .params("orderNo", info.getOrderNo())
                    .params("userId", userId)
                    .execute(new HttpDialogListener(getActivity(), "删除中") {
                        @Override
                        public void onSuccess(String s) {
                            JSONObject result = JsonUtil.checkResult(s);
                            if (result != null &&
                                    SRConstant.SUCCESS_CODE.equals(result.optString(SRConstant.ERR_CODE_KEY))) {
                                recordAskList.remove(info);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    public interface BackPress{
        void back();
    }
}
