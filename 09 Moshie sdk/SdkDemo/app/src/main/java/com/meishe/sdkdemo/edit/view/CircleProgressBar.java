package com.meishe.sdkdemo.edit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.meishe.sdkdemo.utils.ScreenUtils;

public class CircleProgressBar extends View {
    private int spacePadding = ScreenUtils.dip2px(getContext(),2);
    private int mWidth;
    private int mHeight;
    private int mOutRadius;
    private float mProgressAngle = 0.0f;
    private Paint mOutCirclePaint;
    private Paint mProgressPaint;
    private RectF mArcOval = new RectF();
    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }
    /**
     * 初始化画笔
     */
    private void initPaint() {
        //外圆
        mOutCirclePaint = new Paint();
        mOutCirclePaint.setAntiAlias(true);
        mOutCirclePaint.setColor(Color.parseColor("#ff4a90e2"));
        mOutCirclePaint.setStyle(Paint.Style.FILL);

        // 圆弧背景
        mProgressPaint = new Paint();
        mOutCirclePaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.parseColor("#b3ffffff"));
        mProgressPaint.setStyle(Paint.Style.FILL);
    }

    public void drawProgress(int progress){
        mProgressAngle = (float) 360.0 * progress / 100.0f;
        Log.e("Logg","drawProgress" + mProgressAngle);
        invalidate();
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth();// 获取控件宽度
            mHeight = getHeight();// 获取控件高度
            int minValue = Math.min(mWidth, mHeight);// 获取控件宽高最小值
            mOutRadius = minValue / 2;
            mArcOval.set(spacePadding,spacePadding,minValue - spacePadding,minValue - spacePadding);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth/2,mHeight/2,mOutRadius,mOutCirclePaint);
        // 弧形绘制进度条
        canvas.drawArc(mArcOval,270,mProgressAngle,true,mProgressPaint);
    }
}
