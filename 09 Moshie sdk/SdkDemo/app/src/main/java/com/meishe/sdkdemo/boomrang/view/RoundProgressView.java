package com.meishe.sdkdemo.boomrang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.meishe.sdkdemo.R;

/**
 * Created by CaoZhiChao on 2018/12/14 10:18
 * 这是一个外面是一个大圆显示progress，里面是一个固定图片的view。
 */
public class RoundProgressView extends View {
    int progressColor;
    int noneProgressColor;
    int progressWidth;
    int drawableWidth;
    int drawableHeight;
    Drawable drawable;
    int viewWidth;
    int viewHeight;
    /**
     * 0 - 100
     */
    int progress = 0;
    Paint drawPaint;
    Paint progressPaint;
    Paint noneProgressPaint;

    public RoundProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressView);
        progressColor = mTypedArray.getColor(R.styleable.RoundProgressView_progressColor, Color.BLUE);
        noneProgressColor = mTypedArray.getColor(R.styleable.RoundProgressView_noneProgressColor, Color.WHITE);
        progressWidth = (int) mTypedArray.getDimension(R.styleable.RoundProgressView_progressWidth, 0);
        drawableWidth = (int) mTypedArray.getDimension(R.styleable.RoundProgressView_drawableWidth, 0);
        drawableHeight = (int) mTypedArray.getDimension(R.styleable.RoundProgressView_drawableHeight, 0);
        drawable = mTypedArray.getDrawable(R.styleable.RoundProgressView_drawable);
        mTypedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setStyle(Paint.Style.STROKE);

        noneProgressPaint = new Paint();
        noneProgressPaint.setColor(noneProgressColor);
        noneProgressPaint.setAntiAlias(true);
        noneProgressPaint.setStrokeWidth(progressWidth);
        noneProgressPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDrawable(canvas);
        drawProgress(canvas);
    }

    private void drawDrawable(Canvas canvas) {
        Bitmap bitmap = drawableToBitmap(drawable);
        Rect rect = new Rect((viewWidth - drawableWidth) / 2, (viewHeight - drawableHeight) / 2,
                (viewWidth - drawableWidth) / 2 + drawableWidth, (viewWidth - drawableWidth) / 2 + drawableHeight);
        canvas.drawBitmap(bitmap, null, rect, drawPaint);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        // drawable 转换成bitmap
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    private void drawProgress(Canvas canvas) {
        RectF oval1 = new RectF(progressWidth, progressWidth, viewWidth - progressWidth, viewHeight - progressWidth);
        if (progress >= 100) {
            progress = 100;
        }
        int degree = progress * 360 / 100;
        canvas.drawArc(oval1, -90, degree, false, progressPaint);
        canvas.drawArc(oval1, -90 + degree, 360 - degree, false, noneProgressPaint);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 父容器传过来的宽度的值
        viewWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        // 父容器传过来的高度的值
        viewHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        //应该progress是圆的所以必须相等，不等取最大值
        if (viewHeight != viewWidth) {
            viewWidth = Math.max(viewWidth, viewHeight);
            viewHeight = Math.max(viewWidth, viewHeight);
        }
    }
}
