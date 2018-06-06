package com.sirui.inquiry.hospital.avchat.floatshow;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.sirui.basiclib.utils.SRLog;
import com.sirui.basiclib.utils.ScreenUtil;
import com.sirui.basiclib.utils.StatusBarUtil;
import com.sirui.inquiry.R;


/**
 * Created by xiepc on 2017/8/30 16:29
 */

public class FloatWindow implements View.OnTouchListener{

    private View floatView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowManagerParams;

    // move
    private int lastX, lastY;
    private int inX, inY;
    private Rect paddingRect;

    private static final int TOUCH_SLOP = 10;
    /**状态栏高度*/
    private int statusBarHeight;

    private FrameLayout largeSizeFrameLayout;

    private OnClickCallback callback;

    private ValueAnimator moveToEdgeAnim;

    private final int rectTop = 0;
    private final int rectLeft = 0;
    private final int rectRight= 0;
    private final int rectBottom= 10;
    /**窗口是否正在移动*/
    private boolean isMoving = false;
    /**视图是否已被移除*/
    private boolean isRemove = false;

    private ObserveAnimComplete observeAnimComplete;

    public FloatWindow(Context context){
        createFloatView(context);
        statusBarHeight = StatusBarUtil.getStatusBarHeight(context);
    }

    public void setOnClickCallback(OnClickCallback callback){
         this.callback = callback;
    }

    private void createFloatView(Context context) {
        floatView = LayoutInflater.from(context).inflate(R.layout.layout_avchat_float_window,null);
        largeSizeFrameLayout = (FrameLayout) floatView.findViewById(R.id.largeSizeFrameLayout);
//        floatView.setOnClickListener(listener);
//        floatView.setImageResource(R.drawable.ic_return); // 这里简单的用自带的icon来做演示
        floatView.setOnTouchListener(this);
        // 获取WindowManager
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 设置LayoutParams(全局变量）相关参数
        windowManagerParams = new WindowManager.LayoutParams();
        windowManagerParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 设置window type
        // 设置Window flag
        windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 注意，flag的值可以为：
		 * 下面的flags属性的效果形同“锁定”。
		 * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件
		 * LayoutParams.FLAG_NOT_FOCUSABLE  不可聚焦
		 * LayoutParams.FLAG_NOT_TOUCHABLE 不可触摸
		 */
        // 调整悬浮窗口至左上角，便于调整坐标
        windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值
        windowManagerParams.x = ScreenUtil.screenWidth - (int) context.getResources().getDimension(R.dimen.float_window_width);
        windowManagerParams.y = ScreenUtil.dip2px(rectTop);
        // 设置悬浮窗口长宽数据
        windowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 显示myFloatView图像
        windowManager.addView(floatView, windowManagerParams);
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        if(isMoving){ //窗口移动时防止点击
            return false;
        }
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();  //这个值计算不包括状态栏

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                int[] p = new int[2];
                floatView.getLocationOnScreen(p); //在整个屏幕的位置
                inX = x - p[0]; //
                inY = y - p[1] + statusBarHeight;  //由于两值的参考点不一样，需要减去状态栏的高

                break;
            case MotionEvent.ACTION_MOVE:
                final int diff = Math.max(Math.abs(lastX - x), Math.abs(lastY - y));
                if (diff < TOUCH_SLOP)
                    break;

                if (paddingRect == null) {
                    paddingRect = new Rect(ScreenUtil.dip2px(rectLeft), ScreenUtil.dip2px(rectTop), ScreenUtil.dip2px(rectRight),
                            ScreenUtil.dip2px(rectBottom));
                }

                int destX, destY;
                if (x - inX <= paddingRect.left) {
                    destX = paddingRect.left;
                } else if (x - inX + v.getWidth() >= ScreenUtil.screenWidth - paddingRect.right) {
                    destX = ScreenUtil.screenWidth - v.getWidth() - paddingRect.right;
                } else {
                    destX = x - inX;
                }

                if (y - inY <= paddingRect.top) {
                    destY = paddingRect.top;
                } else if (y - inY + v.getHeight() >= ScreenUtil.screenHeight - paddingRect.bottom) {
                    destY = ScreenUtil.screenHeight - v.getHeight() - paddingRect.bottom;
                } else {
                    destY = y - inY;
                }

//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
//                params.gravity = Gravity.NO_GRAVITY;
//                params.leftMargin = destX;
//                params.topMargin = destY;
                //v.setLayoutParams(params);

                windowManagerParams.x = destX;
                windowManagerParams.y = destY;

                windowManager.updateViewLayout(floatView, windowManagerParams);

                break;
            case MotionEvent.ACTION_UP:
                if (Math.max(Math.abs(lastX - x), Math.abs(lastY - y)) <= 5) {
                    //点击响应
                    if(callback != null){
                        callback.onClick();
                    }
                }else{
                     judgeStopFloatWindow(event);  //判断停靠在哪一边
                }
                break;
        }
        return true;
    }

    public void removeView(){
         SRLog.i("removeView ----- isMoving=" + isMoving);
         if(!isMoving){ //必须是在没有移动的情况下移除
               isRemove = true;
               windowManager.removeView(floatView);
           }else{
              observeAnimComplete = new ObserveAnimComplete() {
                 @Override
                 public void onComplete() {
                     isRemove = true;
                     windowManager.removeView(floatView);

                 }
             };
        }
    }


    public ViewGroup getContainerView(){
       return largeSizeFrameLayout;
    }

    public interface OnClickCallback{
         void onClick();
    }


    /**
     * 判断悬浮窗停止拖拽后是否靠边<br>
     * 如果靠边，判断靠左还是靠右<br>
     *
     * @param event        event
     */
    private void judgeStopFloatWindow(MotionEvent event) {
            if (event.getRawX() < ScreenUtil.screenWidth / 2) {//判断悬浮窗在屏幕左半边
                 startAnimaToEdge(0);
            } else if (event.getRawX() >= ScreenUtil.screenWidth / 2) {//判断悬浮窗在屏幕右半边
                startAnimaToEdge(ScreenUtil.screenWidth - floatView.getWidth());
            }
    }

    /**
     * 移动到边缘的动画
     * @param to
     */
    private void startAnimaToEdge(final int to){
        if(isMoving){
            return;
        }
        isMoving = true;
        moveToEdgeAnim = ValueAnimator.ofInt(windowManagerParams.x, to);
        moveToEdgeAnim.setDuration(500);
//        moveToEdgeAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        moveToEdgeAnim.setInterpolator(new DecelerateInterpolator());
        moveToEdgeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(isRemove){ //是否被移除了
                    SRLog.i("-----floatView已经被移除----");
                    return;
                }
                 windowManagerParams.x = (int) animation.getAnimatedValue();
                 windowManager.updateViewLayout(floatView, windowManagerParams);
                 if (windowManagerParams.x == to){
                     SRLog.i("移动完成--isMoving ="+ isMoving);
                     isMoving = false;
                     if(observeAnimComplete != null){
                         SRLog.i("---observeAnimComplete.onComplete() --isMoving ="+ isMoving);
                         observeAnimComplete.onComplete();
                     }
                 }
            }
        });
        moveToEdgeAnim.start();
    }

    /**观察动画完成*/
    interface ObserveAnimComplete{
        void onComplete();
    }
}
