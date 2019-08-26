package com.meishe.sdkdemo.douvideo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.meishe.sdkdemo.utils.ScreenUtils;

import java.util.Timer;
import java.util.TimerTask;


public class TimeDownView extends android.support.v7.widget.AppCompatTextView {
    private Timer timer;
    private DownTimerTask downTimerTask;
    private int downCount;
    private int lastDown;
    private long intervalMills = 1000;
    private long delayMills;
    ScaleAnimation scaleAnimation;

    public TimeDownView(Context context) {
        this(context, null);
    }

    public TimeDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        this.setTextSize(ScreenUtils.sp2px(context,60));
    }

    private void init() {
        if (timer == null) {
            timer = new Timer();
        }
        if (downHandler == null){
            downHandler = new DownHandler();
        }
        setGravity(Gravity.CENTER);
        initDefaultAnimate();

    }

    /**
     * 开始计时
     *
     * @param seconds
     */
    public void downSecond(int seconds) {
        downTime(seconds, 0, 0);
    }

    /**
     * 倒计时开启方法
     *
     * @param downCount     倒计时总数
     * @param lastDown      显示的倒计时的最后一个数
     * @param delayMills    延迟启动倒计时（毫秒数）
     */
    public void downTime(int downCount, int lastDown, long delayMills) {
        this.downCount = downCount;
        this.lastDown = lastDown;
        this.delayMills = delayMills;

        if (downTimerTask == null) {
            downTimerTask = new DownTimerTask();
        }
        timer.schedule(downTimerTask, delayMills, intervalMills);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            drawTextFlag = DRAW_TEXT_YES;//默认绘制
        } else {
            drawTextFlag = DRAW_TEXT_NO;
            if(downTimerTask != null) {
                downTimerTask.cancel();
                downTimerTask = null;
            }
        }
    }

    public void destroy() {
        if(downTimerTask != null) {
            downTimerTask.cancel();
            downTimerTask = null;
        }
        if(timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if(downHandler != null)
            downHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawTextFlag == DRAW_TEXT_NO) {
            return;
        }
        super.onDraw(canvas);
    }

    private class DownTimerTask extends TimerTask {

        @Override
        public void run() {
            if (downCount >= (lastDown - 1)) {
                Message msg = Message.obtain();
                msg.what = 1;
                downHandler.sendMessage(msg);
            }
        }
    }

    public interface DownTimeWatcher {
        void onTime(int num);

        void onLastTime(int num);

        void onLastTimeFinish(int num);
    }

    private DownTimeWatcher downTimeWatcher = null;

    /**
     * 监听倒计时的变化
     *
     * @param downTimeWatcher
     */
    public void setOnTimeDownListener(DownTimeWatcher downTimeWatcher) {
        this.downTimeWatcher = downTimeWatcher;
    }
    private DownHandler downHandler;

    private class DownHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (downTimeWatcher != null) {
                    downTimeWatcher.onTime(downCount);
                }
                if (downCount >= (lastDown - 1)) {
                    //未到结束时
                    if (downCount >= lastDown) {
                        setText(downCount + 1 + "");
                        startDefaultAnimate();
                        if (downCount == lastDown && downTimeWatcher != null) {
                            downTimeWatcher.onLastTime(downCount);
                        }
                    }

                    //到结束时
                    else if (downCount == (lastDown - 1)) {// 若lastDown为0，downCount == -1时是倒计时真正结束之时。
                        if (downTimeWatcher != null) {
                            downTimeWatcher.onLastTimeFinish(downCount);
                        }
                        //倒计时结束，虽然setText()方法触发onDraw，但重写使之不进行绘制
                        //设置不绘制标记
                        if (afterDownDimissFlag == AFTER_LAST_TIME_DIMISS) {
                            drawTextFlag = DRAW_TEXT_NO;
                        }
                        invalidate();//刷新一下
                    }
                    downCount--;
                }
                //
            }
        }
    }
    private final int DRAW_TEXT_YES = 1;
    private final int DRAW_TEXT_NO = 0;
    /**
     * 是否执行onDraw的标识，默认绘制
     */
    private int drawTextFlag = DRAW_TEXT_YES;

    private final int AFTER_LAST_TIME_DIMISS = 1;
    private final int AFTER_LAST_TIME_NODIMISS = 0;
    /**
     * 在倒计时结束之后文字是否消失的标志，默认消失
     */
    private int afterDownDimissFlag = AFTER_LAST_TIME_DIMISS;

    /**
     * 设置倒计时结束后文字不消失
     */
    public void setAfterDownNoDimiss() {
        afterDownDimissFlag = AFTER_LAST_TIME_NODIMISS;
    }

    /**
     * 设置倒计时结束后文字消失
     */
    public void setAferDownDimiss() {
        afterDownDimissFlag = AFTER_LAST_TIME_DIMISS;
    }

    private boolean startDefaultAnimFlag = true;

    //关闭默认动画
    public void closeDefaultAnimate() {
        clearAnimation();
    }
    //开启默认动画
    private void startDefaultAnimate() {
        if (startDefaultAnimFlag) {
            startAnimation(scaleAnimation);
        }
    }

    private void initDefaultAnimate() {
        if(scaleAnimation == null) {
            scaleAnimation = new ScaleAnimation(1f, 0.6f, 1f, 0.6f, Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
            scaleAnimation.setDuration(intervalMills);
        }
    }

}