package com.meishe.sdkdemo.edit.clipEdit.spilt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.ScreenUtils;

//片段分割时码线,可以微调分割值
public class SpiltTimeSpan extends RelativeLayout {
    private static final String TAG = "SpiltTimeSpan";
    private static final long preciseAdjustValue = 100000;//0.1秒
    private float prevRawX = 0;
    private OnSpiltPointChangeListener mSpiltPointChangeListener;
    private long mSpiltPoint = 0;
    private double mPixelPerMicrosecond = 0D;
    private int originLeft = 0;
    private RelativeLayout mLeftHandle;
    private RelativeLayout mRightHandle;
    private ImageView mLeftHandleImage;
    private ImageView mRightHandleImage;
    private int mSpanWidth = 0;
    private int mScreenWidth = 0;
    private NvsMultiThumbnailSequenceView mMultiThumbnailSequenceView = null;

    public SpiltTimeSpan(Context context){
        this(context,null);
    }
    public SpiltTimeSpan(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public void setMultiThumbnailSequenceView(NvsMultiThumbnailSequenceView multiThumbnailSequenceView) {
        this.mMultiThumbnailSequenceView = multiThumbnailSequenceView;
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.splittimespan, this);
        mLeftHandle = (RelativeLayout) view.findViewById(R.id.leftHandle);
        mRightHandle = (RelativeLayout) view.findViewById(R.id.rightHandle);
        mLeftHandleImage = (ImageView) view.findViewById(R.id.leftHandleImage);
        mRightHandleImage = (ImageView) view.findViewById(R.id.rightHandleImage);

        mLeftHandle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmpDx = (int) Math.floor(preciseAdjustValue * mPixelPerMicrosecond + 0.5D);
                tmpDx = -tmpDx;
                moveSpiltTimeSpan(tmpDx);
            }
        });
        mLeftHandleImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftHandle.callOnClick();
            }
        });
        mRightHandle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmpDx = (int) Math.floor(preciseAdjustValue * mPixelPerMicrosecond + 0.5D);
                moveSpiltTimeSpan(tmpDx);
            }
        });

        mRightHandleImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRightHandle.callOnClick();
            }
        });
        mSpanWidth = ScreenUtils.dip2px(context,40);
        mScreenWidth = ScreenUtils.getScreenWidth(context);
    }

    public void updateSpiltTimeSpan(int moveValue){
        int dx = moveValue - mSpanWidth / 2;
        moveSpiltTimeSpan(dx);
    }
    public void setPixelPerMicrosecond(double mPixelPerMicrosecond) {
        this.mPixelPerMicrosecond = mPixelPerMicrosecond;
    }

    private void setSpiltPointCallback(){
        if(mSpiltPointChangeListener != null){
            mSpiltPointChangeListener.onChange(mSpiltPoint);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                originLeft = getLeft();
                prevRawX = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                float tempRawX = ev.getRawX();
                int dx = (int) Math.floor(tempRawX - prevRawX + 0.5D);
                prevRawX = tempRawX;
                moveSpiltTimeSpan(dx);
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                setSpiltPointCallback();
                break;
        }
        return true;
    }

    public void setOnSpiltPointChangeListener(OnSpiltPointChangeListener listener) {
        this.mSpiltPointChangeListener = listener;
    }

    public interface OnSpiltPointChangeListener {
        void onChange(long timeStamp);
    }

    private void moveSpiltTimeSpan(int dx){
        originLeft += dx;
        if (originLeft < 0) {
            originLeft = 0;
        }
        if (originLeft >= mScreenWidth - mSpanWidth ) {
            originLeft = mScreenWidth - mSpanWidth;
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
        lp.leftMargin = originLeft;
        setLayoutParams(lp);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)mMultiThumbnailSequenceView.getLayoutParams();
        int pixelSpiltPoint = originLeft + mSpanWidth / 2 - layoutParams.leftMargin;
        mSpiltPoint = mMultiThumbnailSequenceView.mapTimelinePosFromX(pixelSpiltPoint);
        setSpiltPointCallback();
    }
}
