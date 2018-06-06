package com.sirui.inquiry.hospital.ui.module.loadmore;

import android.os.Handler;
import android.os.Message;

import com.sirui.basiclib.utils.MyLog;


/**
 * 计时控制器
 * @author xiepc
 * 2016-2-22
 */
public class TimeControler{
    private static OnCallBackListener listener;

	private int operation;

	private static boolean isDelaying;

	public boolean isDelaying() {
		return isDelaying;
	}

	public void setOnListener(OnCallBackListener listener){
    	TimeControler.listener = listener;
    }

	private static final Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			isDelaying = false;
			listener.onTimeFinish(msg.what);
			return false;
		}
	});
	
	public void startDelayTime(int delayMillis,int operation){
		this.operation = operation;
		MyLog.i("----开始计时----");
		if(isDelaying){
			handler.removeCallbacksAndMessages(null);
		}
		isDelaying = true;
		handler.sendEmptyMessageDelayed(operation, delayMillis);
	}

	public void removeDelayTime(){
		 MyLog.i("----移除计时----");
		//  handler.removeCallbacksAndMessages(null);
		  handler.removeMessages(this.operation);
		 isDelaying = false;
	}
	public interface OnCallBackListener{
		/**
		 * 倒计算完成
		 * @param operation
		 */
        void onTimeFinish(int operation);
	}
}
