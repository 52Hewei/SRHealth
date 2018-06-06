package com.sirui.main.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.net.client.event.EventType;
import com.net.client.event.MainEvent;
import com.sirui.basiclib.BaseFragment;
import com.sirui.main.R;
import com.sirui.main.R2;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * create by hw
 * on 2018-4-10
 */
public class AdvertFragment extends BaseFragment {


    @BindView(R2.id.skip)
    Button skip;
    Unbinder unbinder;

    public AdvertFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advert, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R2.id.skip})
    public void click(View view){
        EventBus.getDefault().post(new MainEvent(EventType.SKIP_AD));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
