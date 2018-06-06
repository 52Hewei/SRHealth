package com.sirui.inquiry;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sirui.basiclib.BaseFragment;
import com.sirui.basiclib.config.NetUrl;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.inquiry.adapter.DoctorListAdapter;
import com.sirui.inquiry.hospital.chat.constant.InquiryTypeEnum;
import com.sirui.inquiry.hospital.ui.activity.DoctorDetailPhoneActivity;
import com.sirui.inquiry.hospital.ui.model.DoctorInfo;
import com.sirui.router.RouterPath;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Response;

/**
 * 科室医生列表
 * Created by xiepc on 2018/4/23 13:49
 */
@Route(path = RouterPath.ROUTER_PATH_TO_INQUIRY_FRAGMENT)
public class InquiryFragment extends BaseFragment {
    private Unbinder unbinder;

    @BindView(R2.id.tv_left_back)
    TextView tvBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
     @BindView(R2.id.refreshLayout)
     SmartRefreshLayout refreshLayout;
     @BindView(R2.id.recyclerView)
     RecyclerView recyclerView;

     int[] ivDepartmentIds = {R.id.quick_status_1,R.id.quick_status_2,R.id.quick_status_3,R.id.quick_status_4,R.id.quick_status_5,R.id.quick_status_6};
     int[] rlDepartmentIds = {R.id.quick_rl_1,R.id.quick_rl_2,R.id.quick_rl_3,R.id.quick_rl_4,R.id.quick_rl_5,R.id.quick_rl_6};
     ImageView[] ivDepartments = new ImageView[6];
     RelativeLayout[] rlDepartments = new RelativeLayout[6];

     BaseQuickAdapter baseQuickAdapter;

    List<DoctorInfo> dataList = new ArrayList<>();

    public static final int INTERNAL = 0;//内科
    public static final int OBSTETRICS = 1;//妇产科
    public static final int ENT = 2;//五官科
    public static final int TCM = 3;//中医科
    public static final int PEDIATRICS = 4;//儿科
    public static final int OTHERS = 5;//其他科室
    /**
     * 加载页码
     */
    private int pageNum = 1;

    private View headView;

    private  int  currentIndex = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inquiry,null,false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        selectedDepartment(0);
        return view;
    }

    private void initView(){
        tvBack.setVisibility(View.GONE);
        tvTitle.setText("思瑞健康");
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                 getDoctorList(indexMapDepID(currentIndex),false);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                 getDoctorList(indexMapDepID(currentIndex),true);
            }
        });
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_department,null);
        for (int i = 0; i < ivDepartments.length; i++) {
            ivDepartments[i] = headView.findViewById(ivDepartmentIds[i]);
            rlDepartments[i] = headView.findViewById(rlDepartmentIds[i]);
            rlDepartments[i].setOnClickListener(onClickListener);
            rlDepartments[i].setTag(i);
            ivDepartments[i].setTag(i);
        }

    }

    private void getDoctorList(String depId, final boolean isLoadmore) {
        if(!isLoadmore){
            pageNum = 1;
        }
        HttpGo.get(NetUrl.URL_DOCTOR_LIST)
                .params("deptId", depId)
                .params("pageNum", String.valueOf(pageNum))
                .params("storeId", SRConstant.STORE_ID)
                .params("enterpriseId",SRConstant.E_ID)
//                .params("docInqType","2")
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        finishRefresh(isLoadmore);
                        parseSuccessData(s, isLoadmore);
                    }
                    @Override
                    public void onFailure(Response response, Exception e) {
                        finishRefresh(isLoadmore);
                    }
                });
    }

    private void notifyDataSetChanged(){
        if(baseQuickAdapter ==  null){
            baseQuickAdapter = new DoctorListAdapter(getActivity(),R.layout.item_list_doctor,dataList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            baseQuickAdapter.bindToRecyclerView(recyclerView);
            baseQuickAdapter.addHeaderView(headView);
            baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if(position == 0){ //极速问诊
                        DoctorDetailPhoneActivity.quickStart(getActivity(),getDepartNameByIndex(currentIndex) , indexMapDepID(currentIndex), "");
                     }else{
                        DoctorDetailPhoneActivity.start(getActivity(),dataList.get(position), InquiryTypeEnum.typeVideo); //普通问诊都是视频问诊
                    }
                }
            });
        }else{
            baseQuickAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 解析成功返回的数据
     * @param s
     * @param isLoadmore 是否为加载更多
     */
    @NonNull
    private void parseSuccessData(String s, boolean isLoadmore) {
        JSONObject object = JsonUtil.checkResult(s);
        if (object != null && !object.isNull("data")) {
            JSONObject dataObj = object.optJSONObject("data");
            JSONArray jsonArray = dataObj.optJSONArray("list");
            int pageTotall = dataObj.optInt("pageCount");
            if(!isLoadmore){
                 dataList.clear();
                 DoctorInfo doctorInfo = new DoctorInfo();
                 doctorInfo.setDeptName(getDepartNameByIndex(currentIndex));
                 dataList.add(doctorInfo); //添加一个空字段的医生对象，来填充第一个极速问诊的item
            }
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    DoctorInfo info = new DoctorInfo(jsonArray.optJSONObject(i));
                    dataList.add(info);
                }
            }
            notifyDataSetChanged();;
            pageNum++;
        }
    }


    private void finishRefresh(boolean isLoadmore){
        if(isLoadmore){
            refreshLayout.finishLoadMore();

        }else{
            refreshLayout.finishRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String indexMapDepID(int data) {
        String map;
        switch (data) {
            case INTERNAL:
                map = "2";
                break;
            case OBSTETRICS:
                map = "4";
                break;
            case ENT:
                map = "6";
                break;
            case TCM:
                map = "9";
                break;
            case PEDIATRICS:
                map = "5";
                break;
            case OTHERS:
                map = "99";
                break;
            default:
                map = "2";
                break;
        }
        return map;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
              selectedDepartment((Integer) v.getTag());
        }
    };

    private void selectedDepartment(int index){
        ivDepartments[currentIndex].setVisibility(View.INVISIBLE);
        currentIndex = index;
        ivDepartments[index].setVisibility(View.VISIBLE);
        getDoctorList(indexMapDepID(index),false);
    }

    private String getDepartNameByIndex(int index){
        String departName = "";
        switch(index){
          case 0:
              departName = "内科";
           break;
           case 1:
              departName = "妇产科";
           break;
           case 2:
              departName = "五官科";
           break;
           case 3:
              departName = "儿科";
           break;
           case 4:
              departName = "中医科";
           break;
           case 5:
              departName = "其他科室";
           break;
        }
        return departName;
    }
}
