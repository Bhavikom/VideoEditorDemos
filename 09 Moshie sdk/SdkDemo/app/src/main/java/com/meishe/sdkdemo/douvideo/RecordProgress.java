package com.meishe.sdkdemo.douvideo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.meishe.sdkdemo.douvideo.bean.RecordClipsInfo;

public class RecordProgress extends View {

    private static final String TAG = "RecordProgress";
    private final int mProgressBackgroundColor = Color.parseColor("#66A3A3A3");
    private final int mProgressColor = Color.parseColor("#F8E71c");
    private Paint mPaint;
    private Rect mBackgroundRect;
    private Rect mProgressRect;
    private Rect mSegmentLineRet;
    private int mWidth;
    private int mHeignt;
    private RecordClipsInfo mClipsInfo;

    private long mCaptureMaxDuration = 15 * 1000 * 1000;  //in us

    public RecordProgress(Context context) {
        super(context);

        init();
    }

    public RecordProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public RecordProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeignt = h;
        mBackgroundRect.set(0, 0, w, h);
        mSegmentLineRet.top = 0;
        mSegmentLineRet.bottom = mHeignt;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawProgress(canvas);
        drawSegmentLine(canvas);
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mBackgroundRect = new Rect();
        mProgressRect = new Rect();
        mSegmentLineRet = new Rect();
    }


    private void drawBackground(Canvas canvas){
        mPaint.setColor(mProgressBackgroundColor);
        canvas.drawRect(mBackgroundRect, mPaint);
    }

    private void drawProgress(Canvas canvas){
        mPaint.setColor(mProgressColor);
        canvas.drawRect(mProgressRect, mPaint);
    }

    private void drawSegmentLine(Canvas canvas){
        if(mClipsInfo == null){
            return;
        }
        mPaint.setColor(Color.WHITE);
        int size = mClipsInfo.getClipList().size();
        long duration = 0;
        int left = 0;
        int right = 0;
        for(int i = 0; i < size; i++){
            duration += mClipsInfo.getClipList().get(i).getDurationBySpeed();
            left = (int)((float)(duration)/mCaptureMaxDuration * mWidth) - 5;
            right = left + 10;
            mSegmentLineRet.left = left;
            mSegmentLineRet.right = right;
            canvas.drawRect(mSegmentLineRet, mPaint);
        }

    }

    public void setCaptureMaxDuration(long maxDuration){
        mCaptureMaxDuration = maxDuration;
    }

    public void setCurVideoDuration(long duration){
        int progressWidth = (int)((float)(duration)/mCaptureMaxDuration * mWidth);
        mProgressRect.set(0, 0, progressWidth, mHeignt);
        invalidate();
    }

    public void updateRecordClipsInfo(RecordClipsInfo clipsInfo){
        mClipsInfo = clipsInfo;
        setCurVideoDuration(mClipsInfo.getClipsDurationBySpeed());
        invalidate();
    }
}

