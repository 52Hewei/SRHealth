package com.sirui.basiclib.utils;


import android.os.Handler;
import android.os.Message;

/**
 * 重复任务执行计时器
 * Created by xiepc on 2016/12/24 11:43
 */

public class RepeatTaskTimer {

    private boolean isRunning;
    private int repeatTime;
    private TaskCallback task;
    /**执行任务的次数*/
    private int time = 0;


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message.what == 0){
//                MyLog.i("---执行定时任务---");
                time ++;
                if(task != null){
                    task.onExecute(time);
                }
                if(isRunning){
                    handler.sendEmptyMessageDelayed(0,repeatTime);
                }
            }
            return true;
        }
    });

    public void startTask(TaskCallback task,int delayTime,int repeatTime){
        this.task = task;
        this.repeatTime = repeatTime;
        this.isRunning = true;
        handler.sendEmptyMessageDelayed(0,delayTime);
    }

    public void stopTask(){
        handler.removeCallbacksAndMessages(null);
        this.isRunning = false;
    }

    public interface TaskCallback{
        /**
         * @param time 执行次数
         */
        void onExecute(int time);
    }
}
