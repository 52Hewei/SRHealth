package com.sirui.basiclib.widget;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sirui.basiclib.R;

/**
 * Created by xiepc on 2018/3/28 17:32
 */

public class DialogUtil {
    /**
     *  两个按钮的对话框
     */
       public static void both(Context context,boolean isShowTitle,String title, String msg, View.OnClickListener confirmListener, View.OnClickListener cancelListener){
           new BaseDialog.Builder(context)
                   .setTitle(title)
                   .setTitleShow(isShowTitle)
                   .setMessage(msg)
                   .setPositiveButton(confirmListener)
                   .setNegativeButton(cancelListener)
                   .create()
                   .show();
       }

       public static void both(Context context,String msg, View.OnClickListener confirmListener){
           both(context,true,"温馨提示",msg,confirmListener,null);
       }

    /**
     * 一个确定按钮的对话框
     */
       public static void confirm(Context context,boolean isShowTitle,String title, String msg, View.OnClickListener confirmListener){
           new BaseDialog.Builder(context)
                   .setTitle(title)
                   .setTitleShow(isShowTitle)
                   .setMessage(msg)
                   .setPositiveButton(confirmListener)
                   .setNegativeBtnShow(false)
                   .create()
                   .show();
       }

       public static void confirm(Context context,String msg, View.OnClickListener confirmListener){
           confirm(context,true,"温馨提示",msg,confirmListener);
       }

    /**
     *  两个按钮的自定义视图对话框
     */
    public static void bothCustomView(Context context, boolean isShowTitle, String title, View customView, View.OnClickListener confirmListener, View.OnClickListener cancelListener){
        new BaseDialog.Builder(context)
                .setTitle(title)
                .setTitleShow(isShowTitle)
                .setView(customView)
                .setPositiveButton(confirmListener)
                .setNegativeButton(cancelListener)
                .create()
                .show();
    }
    /**
     * 问诊界面弹出的底部对话框菜单
     *
     * @param context
     */
    public static void showSexDialog(final Activity context, final onSexSelectedListener listener) {
        final Dialog mCameraDialog = new Dialog(context, R.style.dialog_bottom);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_bottom_menu, null);
        TextView tvMale = root.findViewById(R.id.tv_male);
        TextView tvFemale = root.findViewById(R.id.tv_female);
        TextView tvCancel = root.findViewById(R.id.tv_cancel);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDialog.dismiss();
                int id = v.getId();
                if(id == R.id.tv_male){
                    listener.onSelected(1);
                }else if(id == R.id.tv_female){
                    listener.onSelected(0);
                }
            }
        };
        tvMale.setOnClickListener(onClickListener);
        tvFemale.setOnClickListener(onClickListener);
        tvCancel.setOnClickListener(onClickListener);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.dimAmount = 0.3f;
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
//        lp.alpha = 5f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    public interface onSexSelectedListener{
        void onSelected(int index);
    }
}
