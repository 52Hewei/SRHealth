package com.sirui.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.just.agentweb.AgentWeb;
import com.sirui.basiclib.BaseFragment;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.router.RouterPath;


/**
 * fragment的web界面展示
 * Created by xiepc on 2016/11/23 14:45
 */
@Route(path = RouterPath.ROUTER_PATH_TO_WEB_FRAGMENT)
public class WebFragment extends BaseFragment {
    /**调用链接*/
    private String url = "http://www.baidu.com";
    private FrameLayout containerLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        Bundle bundle = getArguments();
        if(bundle != null){
            url = bundle.getString(SRConstant.URL_WEB);
        }
        containerLayout = view.findViewById(R.id.containerLayout);
        AgentWeb mAgentWeb = AgentWeb.with(getActivity())
                .setAgentWebParent((FrameLayout) containerLayout, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(getResources().getColor(R.color.transparent),0)
                .createAgentWeb()
                .ready()
                .go(url);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
