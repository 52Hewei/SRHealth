package com.sirui.main;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sirui.basiclib.BaseThemeActivity;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.basiclib.widget.MyToast;
import com.sirui.basiclib.widget.upgrade.UpgradeDialog;
import com.sirui.main.fragment.AdvertFragment;
import com.sirui.main.fragment.PersonCenterFragment;
import com.sirui.main.utils.EventResolver;
import com.sirui.main.widget.TabBottomBar;
import com.sirui.router.RouterPath;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.transformer.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseThemeActivity {

    public static boolean isRed = false;
    EventResolver eventResolver;
    private Fragment currentFragment;
    private Fragment inquiryFragment;
    private Fragment storeFragment;
    private PersonCenterFragment personCenterFragment;
    private AdvertFragment advertFragment;//全屏广告
    /*    @BindView(R2.id.rl_home)
        RelativeLayout rlHome;
        @BindView(R2.id.tv_home)
        TextView tvHome;
        @BindView(R2.id.iv_home)
        ImageView ivHome;
        @BindView(R2.id.rl_personal_center)
        RelativeLayout rlPersonCenter;*/
    @BindView(R2.id.tab_bar)
    TabBottomBar tabBottomBar;
    private List<AdInfo> advList;

    public static final int REQUEST_PERMISSION_CODE = 12;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void iniData() {
        eventResolver = new EventResolver(this);
        eventResolver.register();
//        showAdDialog();
//        update();
    }

    private void update() {
        new UpgradeDialog(this,null).show();
    }

    private void showAdDialog() {
        advList = new ArrayList<>();
        AdInfo adInfo = new AdInfo();
        adInfo.setActivityImg("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage1.png");
        advList.add(adInfo);

        adInfo = new AdInfo();
        adInfo.setActivityImg("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage2.png");
        advList.add(adInfo);
         /**
        * 创建广告活动管理对象
         */
        AdManager adManager = new AdManager(MainActivity.this, advList);
        adManager.setOverScreen(true)
                .setPageTransformer(new DepthPageTransformer());
        /**
         * 执行弹窗的显示操作
         */
        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);
    }

    @Override
    protected void iniView() {

        storeFragment = (Fragment) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_WEB_FRAGMENT).navigation();
        if(storeFragment != null){
//            Bundle bundle=new Bundle();
//            bundle.putString(SRConstant.URL_WEB,SRConstant.STORE_URL);
//            storeFragment.setArguments(bundle);
        }
        inquiryFragment = (Fragment) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_INQUIRY_FRAGMENT).navigation();
        personCenterFragment = new PersonCenterFragment();
        selectedTab(0);
        tabBottomBar.updateTab(true,0);
        tabBottomBar.setOnTabBarClickListener(new TabBottomBar.OnTabBarClickListener() {
            @Override
            public void onClick(int index) {
                selectedTab(index);
            }
        });

        postDelayCheckPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventResolver.unregister();
    }

    private void selectedTab(int index) {
        switch (index) {
            case 0:
                 switchFragment(inquiryFragment);
                break;
            case 1:
                 switchFragment(storeFragment);
                break;
            case 2:
                switchFragment(personCenterFragment);
                break;
        }
    }

    //切换fragment方法
    private void switchFragment(Fragment targetFragment) {
        if(targetFragment == null){
            return;
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fl_main_container, targetFragment).commit();
        } else {
            transaction.hide(currentFragment).show(targetFragment).commit();
        }
        currentFragment = targetFragment;
    }

    private void postDelayCheckPermission(){
        tabBottomBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermission();
            }
        },2000);
    }
    private void checkPermission(){
        if(Build.VERSION.SDK_INT < 23){
            return;
        }
        //如果是小米手机，需要悬浮窗权限才能弹出系统级别的对话框
        if ("Xiaomi".equals(Build.MANUFACTURER) && ! Settings.canDrawOverlays(this)) {
//            new AlertDialog.Builder(this)
//                    .setCancelable(true)
//                    .setTitle("温馨提示")
//                    .setMessage("请授予悬浮窗权限，否则部分功能将不可用！")
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                                    Uri.parse("package:" + getPackageName()));
//                            startActivityForResult(intent,REQUEST_PERMISSION_CODE);
//                        }
//                    })
//                    .show();
            DialogUtil.both(this, "请授予悬浮窗权限，否则部分功能将不可用！", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent,REQUEST_PERMISSION_CODE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if(Build.VERSION.SDK_INT >= 23){
                    if (!Settings.canDrawOverlays(this)){
                        MyToast.show("授权失败");
                    }else{
                        MyToast.show("授权成功");
                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
