package debug;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.net.client.HttpGo;
import com.sirui.basiclib.utils.Utils;
import com.uuch.adlibrary.AdInit;
import com.zhy.changeskin.SkinManager;

/**
 * Created by xiepc on 2018/3/26 16:09
 */

public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        if (true) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
        HttpGo.init(this);
        Utils.init(this);

        SkinManager.getInstance().init(this); //主题换肤
        AdInit.initDisplayOpinion(this);//广告弹窗初始化
    }
}
