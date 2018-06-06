package com.sirui.main.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sirui.basiclib.config.SRConstant;
import com.sirui.main.R;
import com.sirui.main.config.ResConfig;
import com.zhy.changeskin.SkinManager;

/**
 * Created by xiepc on 2018/4/9 10:52
 */

public class TabBottomBar extends LinearLayout {

    private ImageView ivTabs[] = new ImageView[3];
    private TextView tvTabs[] = new TextView[3];
    private String[] tabIvSelectedNames = {ResConfig.TAB_HOME_IV_S, ResConfig.TAB_HOME_IV_S, ResConfig.TAB_PERSONAL_IV_S};
    private String[] tabIvNormalNames = {ResConfig.TAB_HOME_IV_N, ResConfig.TAB_HOME_IV_N,ResConfig.TAB_PERSONAL_IV_N};
    private RelativeLayout rlHome;
    private RelativeLayout rlStore;
    private RelativeLayout rlPersonalCenter;

    private OnTabBarClickListener tabBarClickListener;

    public TabBottomBar(Context context) {
        super(context);
        initView(context);
    }

    public TabBottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabBottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_bottom_tab, this, true);
        ivTabs[0] = view.findViewById(R.id.iv_home);
        ivTabs[1] = view.findViewById(R.id.iv_store);
        ivTabs[2] = view.findViewById(R.id.iv_personal_center);
        tvTabs[0] = view.findViewById(R.id.tv_home);
        tvTabs[1] = view.findViewById(R.id.tv_store);
        tvTabs[2] = view.findViewById(R.id.tv_personal_center);
        rlHome = view.findViewById(R.id.rl_home);
        rlStore = view.findViewById(R.id.rl_store);
        rlPersonalCenter = view.findViewById(R.id.rl_personal_center);
        rlHome.setOnClickListener(listener);
        rlHome.setTag(0);
        rlStore.setOnClickListener(listener);
        rlStore.setTag(1);
        rlPersonalCenter.setOnClickListener(listener);
        rlPersonalCenter.setTag(2);
        if(!SRConstant.hasStore){
            rlStore.setVisibility(GONE);
        }
    }


    public void refreshTabBar(){
        for (int i = 0; i < ivTabs.length; i++) {
             updateTab(ivTabs[i].isSelected(),i);
        }
    }

    public void updateTab(boolean isSelected, int index) {
        ivTabs[index].setSelected(isSelected);
        ivTabs[index].setImageDrawable(SkinManager.getInstance().getResourceManager().getDrawableByName(getResNameByStatus(isSelected, index)));
        tvTabs[index].setTextColor(SkinManager.getInstance().getResourceManager().getColor(getColorNameByStatus(isSelected)));
    }

    private String getResNameByStatus(boolean isSelected, int index) {
        if (isSelected) {
            return tabIvSelectedNames[index];
        } else {
            return tabIvNormalNames[index];
        }
    }

    private String getColorNameByStatus(boolean isSelected) {
        if (isSelected) {
            return ResConfig.TAB_TV_COLOR_S;
        } else {
            return ResConfig.TAB_TV_COLOR_N;
        }
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
             int index = (int) v.getTag();
             for (int i = 0; i < ivTabs.length; i++) {
                  if(index == i){
                      updateTab(true,i);
                  }else{
                      updateTab(false,i);
                  }
             }
             if (tabBarClickListener != null) {
                tabBarClickListener.onClick(index);
            }
        }
    };


    public interface OnTabBarClickListener {
        void onClick(int index);
    }

    public void setOnTabBarClickListener(OnTabBarClickListener tabBarClickListener) {
        this.tabBarClickListener = tabBarClickListener;
    }
}
