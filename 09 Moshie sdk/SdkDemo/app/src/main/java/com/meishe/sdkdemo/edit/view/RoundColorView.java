package com.meishe.sdkdemo.edit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.ScreenUtils;

/**
 * 字幕样式之颜色选择控件
 */

public class RoundColorView extends View {
    private final String TAG = "RoundColorView";
    private Context mContext;
    private Paint mPaintOut, mPaintIn;
    private float mRadius = 0;
    private int COLOR_CIRCLE = Color.TRANSPARENT;

    public RoundColorView(Context context) {
        super(context);
        init(context);
    }

    public RoundColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRoundColorView);
            COLOR_CIRCLE = a.getColor(R.styleable.CustomRoundColorView_color, Color.TRANSPARENT);
        }

        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        mPaintOut = new Paint();
        mPaintOut.setAntiAlias(true);
        mPaintIn = new Paint();
        mPaintIn.setAntiAlias(true);
        mRadius = ScreenUtils.dip2px(mContext, 12.5f);
    }

    public void setColor(int color) {
        COLOR_CIRCLE = color;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaintIn.setStyle(Paint.Style.FILL);
        mPaintIn.setColor(COLOR_CIRCLE);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaintIn);
    }
}