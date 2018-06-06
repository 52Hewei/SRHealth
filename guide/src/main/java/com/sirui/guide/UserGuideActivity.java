package com.sirui.guide;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.utils.SPUtil;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IMainProvider;

import butterknife.BindView;
import butterknife.OnClick;

public class UserGuideActivity extends BaseActivity {

    @BindView(R2.id.ll_func_guide)
    LinearLayout llFuncGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_guide;
    }

    @OnClick({R2.id.ll_func_guide})
    public void click(){
        enterMain();
    }

    private void enterMain() {
        IMainProvider mainService = (IMainProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_MAIN_SERVICE).navigation();
        if (mainService != null) {
            mainService.goMain(this);
            overridePendingTransition(0,0);
        }
        finish();
    }

    @Override
    protected void iniData() {
        SPUtil.putBoolean(SRConstant.FIRST_ENTER_MAIN,false);
    }

    @Override
    protected void iniView() {
    }
}
