package com.meishe.sdkdemo.edit.clipEdit.speed;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import com.meishe.sdkdemo.R;

/**
 * Created by admin on 2018/7/6.
 * 自定义SeekBar,解决SeekBar里面tickMark盖住thumb的bug
 */

public class CustomSpeedSeekBar extends AppCompatSeekBar {

    private Drawable mTickMark;

    public CustomSpeedSeekBar(Context context) {
        this(context, null);
    }

    public CustomSpeedSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.seekBarStyle);
    }

    public CustomSpeedSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttributes(attrs, defStyleAttr);
    }

    private void applyAttributes(AttributeSet rawAttrs, int defStyleAttr)
    {
        TypedArray attrs = getContext().obtainStyledAttributes(rawAttrs, R.styleable.CustomSpeedSeekBar, defStyleAttr, 0);
        try {
            mTickMark = attrs.getDrawable(R.styleable.CustomSpeedSeekBar_tickMarkFixed);
        } finally {
            attrs.recycle();
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTickMarks(canvas);
    }

    @Override
    public int getThumbOffset() {
        return super.getThumbOffset();
    }

    void drawTickMarks(Canvas canvas) {
        if (mTickMark != null) {
            final int count = getMax();
            if (count > 1) {
                final int w = mTickMark.getIntrinsicWidth();
                final int h = mTickMark.getIntrinsicHeight();
                final int halfThumbW = getThumb().getIntrinsicWidth() / 2;
                final int halfW = w >= 0 ? w / 2 : 1;
                final int halfH = h >= 0 ? h / 2 : 1;
                mTickMark.setBounds(-halfW, -halfH, halfW, halfH);
                final float spacing = (getWidth() - getPaddingLeft() - getPaddingRight() + getThumbOffset() * 2 - halfThumbW * 2) / (float) count;
                final int saveCount = canvas.save();
                canvas.translate(getPaddingLeft() - getThumbOffset() + halfThumbW, getHeight() / 2);
                for (int i = 0; i <= count; i++) {
                    if(i!=getProgress())
                        mTickMark.draw(canvas);
                    canvas.translate(spacing, 0);
                }
                canvas.restoreToCount(saveCount);
            }
        }
    }
}