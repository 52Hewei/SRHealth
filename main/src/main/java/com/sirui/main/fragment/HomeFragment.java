package com.sirui.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sirui.basiclib.BaseFragment;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.main.R;
import com.sirui.main.R2;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IInquiryProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by xiepc on 2018/3/12 16:27
 */

public class HomeFragment extends BaseFragment{
    private Unbinder unbinder;


    @BindView(R2.id.tv_left_back)
    TextView tvBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null,false);
        MyLog.i(getClass().getSimpleName()+"--onCreateView");
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        tvBack.setVisibility(View.GONE);
        tvTitle.setText("思瑞健康");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R2.id.layout_quick_consult})
    public void onBtnClick(View view){
        if(view.getId() == R.id.layout_quick_consult){
            IInquiryProvider inquiryProvider = (IInquiryProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_INQUIRY_SERVICE).navigation();
            if(inquiryProvider != null){
                inquiryProvider.goToInquiry(getActivity());
            }
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
