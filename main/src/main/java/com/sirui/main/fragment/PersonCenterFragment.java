package com.sirui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sirui.basiclib.BaseFragment;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.main.PersonalActivity;
import com.sirui.main.R;
import com.sirui.main.R2;
import com.sirui.main.SettingsActivity;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IInquiryProvider;
import com.sirui.router.provider.IWebProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by xiepc on 2018/3/12 16:27
 */

public class PersonCenterFragment extends BaseFragment {

    @BindView(R2.id.rl_person_info)
    RelativeLayout rlPersonInfo;
    @BindView(R2.id.rl_my_order)
    RelativeLayout rlMyOrder;
    @BindView(R2.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R2.id.rl_mall)
    RelativeLayout rlMall;
    @BindView(R2.id.tv_user_name)
    TextView tvUserName;
    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_center, null, false);
        MyLog.i(getClass().getSimpleName() + "--onCreateView");
        unbinder = ButterKnife.bind(this, view);
        tvUserName.setText(DataManager.getInstance().getUser().getRealName());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick({R2.id.rl_person_info,R2.id.rl_mall,R2.id.rl_my_order,R2.id.rl_setting})
    public void click(View view) {
        int id = view.getId();
        if (id == R.id.rl_mall) {//商城web
            IWebProvider provider = (IWebProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_WEB_SERVICE).navigation();
            if (provider != null){
                provider.goToWeb(getActivity(), SRConstant.STORE_URL);
            }
        }else if(id == R.id.rl_my_order){
            IInquiryProvider provider = (IInquiryProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_INQUIRY_SERVICE).navigation();
            if(provider != null){
                provider.goToInquiryRecord(getActivity());
            }
        }else if(id == R.id.rl_person_info){
             getActivity().startActivity(new Intent(getActivity(), PersonalActivity.class));
        }else if(id == R.id.rl_setting){
             getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
