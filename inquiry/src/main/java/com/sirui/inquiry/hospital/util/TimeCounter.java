package com.sirui.inquiry.hospital.util;


import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

/* 定义一个倒计时的内部类 */
public class TimeCounter extends CountDownTimer {
	TextView timeText;
	private Context context;
	private boolean isRuning;
	public TimeCounter(Context context, long millisInFuture, long countDownInterval, TextView timeText) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		this.timeText = timeText;
		this.context = context;
	}


	@Override
	public void onFinish() {//计时完毕时触发
		 timeText.setText("计时完成");
		 isRuning = false;
	}
	@Override
	public void onTick(long millisUntilFinished){//计时过程显示
		//timeText.setText(millisUntilFinished/1000+"秒");
		timeText.setText("预计等待时长:"+TimeUtil.millis2String(millisUntilFinished,"mm分ss秒"));
		isRuning = true;
	}

	public boolean isRuning() {
		return isRuning;
	}

	public void setRuning(boolean runing) {
		isRuning = runing;
	}

	public void stop(){
		this.cancel();
		isRuning = false;
	}
}
