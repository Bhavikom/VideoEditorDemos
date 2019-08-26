package com.meishe.sdkdemo.picinpic.data;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.meishe.sdkdemo.utils.ScreenUtils;

/**
 * Created by czl on 2018/8/6.
 */
public class PicInPicDrawRect extends View {
    private RectF mViewRectF = new RectF();
    private Paint mRectPaint = new Paint();
    private Context mContext;
    public PicInPicDrawRect(Context context) {
        this(context, null);
    }

    public PicInPicDrawRect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initRectPaint();
    }

    public void setDrawRect(RectF rectF) {
        mViewRectF = rectF;
        invalidate();
    }

    private void initRectPaint(){
        // 设置颜色
        mRectPaint.setColor(Color.parseColor("#4A90E2"));
        // 设置抗锯齿
        mRectPaint.setAntiAlias(true);
        // 设置线宽
        mRectPaint.setStrokeWidth(ScreenUtils.dip2px(mContext,3));
        // 设置非填充
        mRectPaint.setStyle(Paint.Style.STROKE);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画矩形框
        canvas.drawRect(mViewRectF, mRectPaint);
    }
}
