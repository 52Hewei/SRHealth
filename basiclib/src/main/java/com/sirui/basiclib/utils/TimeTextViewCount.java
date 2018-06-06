package com.sirui.basiclib.utils;


import android.os.CountDownTimer;
import android.widget.TextView;

/* 定义一个倒计时的内部类 */
public class TimeTextViewCount extends CountDownTimer {
	TextView gcode;
	private boolean isRuning;
	public TimeTextViewCount(long millisInFuture, long countDownInterval, TextView gcode) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		this.gcode = gcode;
	}


	@Override
	public void onFinish() {//计时完毕时触发
		gcode.setText("获取验证码");
		//gcode.setBackgroundResource(R.drawable.login_shape2);
		//gcode.setTextColor(context.getResources().getColor(R.color.gray_text));
		gcode.setClickable(true);
		gcode.setAlpha(1.0f);
		isRuning = false;
	}
	@Override
	public void onTick(long millisUntilFinished){//计时过程显示
		gcode.setClickable(false);
		gcode.setAlpha(0.5f);
		gcode.setText(millisUntilFinished/1000+"s后重发");
		isRuning = true;
	//	gcode.setTextColor(context.getResources().getColor(R.color.gray_text));
	}

	public boolean isRuning() {
		return isRuning;
	}

	public void setRuning(boolean runing) {
		isRuning = runing;
	}

	public void stop(){
		this.cancel();
		gcode.setClickable(true);
		isRuning = false;
	}
}
