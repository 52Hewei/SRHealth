package com.sirui.basiclib.utils;


import android.os.CountDownTimer;
import android.widget.Button;

/* 定义一个倒计时的内部类 */
public class TimeCount extends CountDownTimer {
    Button gcode;
    private boolean isRuning;
    private FinishListener mListener;

    public TimeCount(long millisInFuture, long countDownInterval, Button gcode) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.gcode = gcode;
    }

    public TimeCount(long millisInFuture, long countDownInterval, Button gcode, FinishListener listener) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.gcode = gcode;
        this.mListener = listener;
    }


    @Override
    public void onFinish() {//计时完毕时触发
        gcode.setText("获取验证码");
        //gcode.setBackgroundResource(R.drawable.login_shape2);
        //gcode.setTextColor(context.getResources().getColor(R.color.gray_text));
        gcode.setEnabled(true);
        if (mListener != null) {
            mListener.onFinish();
        }
        isRuning = false;
    }

    @Override
    public void onTick(long millisUntilFinished) {//计时过程显示
        gcode.setEnabled(false);
        gcode.setText(String.format("%d秒", millisUntilFinished / 1000));
        isRuning = true;
        //	gcode.setTextColor(context.getResources().getColor(R.color.gray_text));
    }

    public boolean isRuning() {
        return isRuning;
    }

    public void setRuning(boolean runing) {
        isRuning = runing;
    }

    public void stop() {
        this.cancel();
        gcode.setEnabled(true);
        isRuning = false;
    }

    public interface FinishListener {
        void onFinish();
    }
}
