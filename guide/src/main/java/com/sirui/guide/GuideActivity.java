package com.sirui.guide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.utils.SPUtil;
import com.sirui.guide.adapter.GuideAdapter;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.ILoginProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    @BindView(R2.id.boot_page_viewpager)
    ViewPager bootPageViewpager;
    @BindView(R2.id.immediate_experience_btn)
    Button immediateExperienceBtn;

    private ArrayList<ImageView> viewList = new ArrayList<>();
    private int[] imageIds = new int[]{
            R.mipmap.guide_image_one,
            R.mipmap.guide_image_two,
            R.mipmap.guide_image_three};

    private GuideAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void iniData() {
        for (int i = 0; i < imageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(imageIds[i]);
            viewList.add(view);
        }
        mAdapter = new GuideAdapter(viewList);
        bootPageViewpager.setAdapter(mAdapter);
        bootPageViewpager.addOnPageChangeListener(this);
        SPUtil.putBoolean(SRConstant.FIRST_ENTER_GUIDE,false);
    }

    @Override
    protected void iniView() {
    }

    @OnClick({R2.id.immediate_experience_btn})
    public void click(){
        enterLogin();
    }

    private void enterLogin() {
        ILoginProvider loginService = (ILoginProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_LOGIN_SERVICE).navigation();
        if(loginService != null){
            loginService.goToLogin(this);
        }
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position==2){
            immediateExperienceBtn.setVisibility(View.VISIBLE);
        }else {
            immediateExperienceBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
