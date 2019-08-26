package com.meishe.sdkdemo.edit.timelineEditor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meishe.sdkdemo.R;

/**
 * Created by admin on 2018/6/8.
 */

public class NvsTimelineTimeSpanExt extends RelativeLayout {
    private String TAG = "NvsTimelineTimeSpanExt";
    private static final long preciseAdjustValue = 100000;//0.1秒
    private float prevRawX = 0;
    private OnTrimInChangeListener mOnTrimInChangeListener;
    private OnTrimOutChangeListener mOnTrimOutChangeListener;
    private OnMarginChangeListener mMarginChangeListener;
    //
    private long mInPoint = 0;
    private long mOutPoint = 0;
    private double mPixelPerMicrosecond = 0D;
    private int mTotalWidth = 0;
    //
    private long minDraggedTimeSpanDuration = 1000000;
    private int minDraggedTimeSpanPixel = 0;
    private int originLeft = 0;
    private int originRight = 0;

    NvsMultiThumbnailSequenceView multiThumbnailSequenceView;

    private int mLTopHandleHeight = 0;
    private int mLLeftHandleWidth = 0;

    private View timeSpanshadowView;
    private RelativeLayout mLTopHandle;
    private RelativeLayout mLBottomLayout;
    private RelativeLayout mLLeftLayout;
    private RelativeLayout mLRightLayout;
    private ImageView mLLeftHandle;
    private ImageView mLRightHandle;
    private ImageView mLeftHandleImage;
    private RelativeLayout mRTopHandle;
    private RelativeLayout mRBottomLayout;
    private RelativeLayout mRLeftLayout;
    private RelativeLayout mRRightLayout;
    private ImageView mRLeftHandle;
    private ImageView mRRightHandle;
    private ImageView mRightHandleImage;
    public NvsTimelineTimeSpanExt(Context context){
        super(context);
        init(context);
        initListener();
    }

    private void init(Context context){
        View parentRoot = LayoutInflater.from(context).inflate(R.layout.timespanext, this);
        timeSpanshadowView = parentRoot.findViewById(R.id.timeSpanShadow);
        mLTopHandle = (RelativeLayout)parentRoot.findViewById(R.id.lTopHandle);
        mLTopHandleHeight = mLTopHandle.getLayoutParams().height;
        mLBottomLayout = (RelativeLayout)parentRoot.findViewById(R.id.lBottomLayout);
        mLLeftLayout = (RelativeLayout)parentRoot.findViewById(R.id.lLeftHandleLayout);
        mLRightLayout = (RelativeLayout)parentRoot.findViewById(R.id.lRightHandleLayout);
        mLLeftHandle = (ImageView)parentRoot.findViewById(R.id.lLeftHandle);
        mLRightHandle = (ImageView)parentRoot.findViewById(R.id.lRightHandle);
        mLeftHandleImage = (ImageView)parentRoot.findViewById(R.id.leftHandleImage);
        mLLeftHandleWidth = mLLeftHandle.getLayoutParams().width;
        mRTopHandle = (RelativeLayout)parentRoot.findViewById(R.id.rTopHandle);
        mRBottomLayout = (RelativeLayout)parentRoot.findViewById(R.id.rBottomLayout);
        mRLeftLayout = (RelativeLayout)parentRoot.findViewById(R.id.rLeftHandleLayout);
        mRRightLayout = (RelativeLayout)parentRoot.findViewById(R.id.rRightHandleLayout);
        mRLeftHandle = (ImageView)parentRoot.findViewById(R.id.rLeftHandle);
        mRRightHandle = (ImageView)parentRoot.findViewById(R.id.rRightHandle);
        mRightHandleImage = (ImageView)parentRoot.findViewById(R.id.rightHandleImage);
    }
    private void initListener(){
        mLTopHandle.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                minDraggedTimeSpanPixel = (int) Math.floor(minDraggedTimeSpanDuration * mPixelPerMicrosecond + 0.5D);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getParent().requestDisallowInterceptTouchEvent(true);
                        mLBottomLayout.setVisibility(View.VISIBLE);
                        mRBottomLayout.setVisibility(View.INVISIBLE);
                        mLeftHandleImage.setImageResource(R.mipmap.trimline_select_left);
                        mRightHandleImage.setImageResource(R.mipmap.trimline_default_right);

                        originLeft = getLeft();
                        originRight = getRight();
                        prevRawX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        getParent().requestDisallowInterceptTouchEvent(true);
                        float tempRawX = event.getRawX();
                        int dx = (int) Math.floor(tempRawX - prevRawX + 0.5D);
                        prevRawX = tempRawX;
                        leftHandleMove(dx);
                        setTrimInCallback(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        getParent().requestDisallowInterceptTouchEvent(false);
                        setTrimInCallback(true);
                        break;
                }
                return true;
            }
        });
        mRTopHandle.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                minDraggedTimeSpanPixel = (int) Math.floor(minDraggedTimeSpanDuration * mPixelPerMicrosecond + 0.5D);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getParent().requestDisallowInterceptTouchEvent(true);
                        mLBottomLayout.setVisibility(View.INVISIBLE);
                        mRBottomLayout.setVisibility(View.VISIBLE);
                        mLeftHandleImage.setImageResource(R.mipmap.trimline_default_left);
                        mRightHandleImage.setImageResource(R.mipmap.trimline_select_right);

                        originLeft = getLeft();
                        originRight = getRight();
                        prevRawX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        getParent().requestDisallowInterceptTouchEvent(true);
                        float tempRawX = (int)event.getRawX();
                        int dx = (int) Math.floor(tempRawX - prevRawX + 0.5D);
                        prevRawX = tempRawX;
                        rightHandleMove(dx);
                        setTrimOutCallback(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        getParent().requestDisallowInterceptTouchEvent(false);
                        setTrimOutCallback(true);
                        break;
                }
                return true;
            }
        });
        mLLeftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmpDx = getAdjustSpanValue();
                tmpDx = -tmpDx;
                leftHandleMove(tmpDx);
                setTrimInCallback(true);
            }
        });
        mLLeftHandle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLLeftLayout.callOnClick();
            }
        });

        mLRightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmpDx = getAdjustSpanValue();
                leftHandleMove(tmpDx);
                setTrimInCallback(true);
            }
        });

        mLRightHandle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLRightLayout.callOnClick();
            }
        });
        mRLeftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmpDx = getAdjustSpanValue();
                tmpDx = -tmpDx;
                rightHandleMove(tmpDx);
                setTrimOutCallback(true);
            }
        });
        mRLeftHandle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRLeftLayout.callOnClick();
            }
        });
        mRRightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmpDx = getAdjustSpanValue();
                rightHandleMove(tmpDx);
                setTrimOutCallback(true);
            }
        });
        mRRightHandle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRRightLayout.callOnClick();
            }
        });
    }

    public void setMultiThumbnailSequenceView(NvsMultiThumbnailSequenceView thumbnailSequenceView) {
        this.multiThumbnailSequenceView = thumbnailSequenceView;
    }
    public View getTimeSpanshadowView(){return timeSpanshadowView;}
    public long getInPoint() {
        return mInPoint;
    }

    public void setInPoint(long mInPoint) {
        this.mInPoint = mInPoint;
    }


    public long getOutPoint() {
        return mOutPoint;
    }

    public void setOutPoint(long mOutPoint) {
        this.mOutPoint = mOutPoint;
    }

    public void setPixelPerMicrosecond(double mPixelPerMicrosecond) {
        this.mPixelPerMicrosecond = mPixelPerMicrosecond;
    }
    public int getLTopHandleHeight() {
        return mLTopHandleHeight;
    }
    public int getLLeftHandleWidth() {
        return mLLeftHandleWidth;
    }

    public void setTotalWidth(int totalWidth) {
        this.mTotalWidth = totalWidth;
    }


    private int getAdjustSpanValue(){
        return (int) Math.floor(preciseAdjustValue * mPixelPerMicrosecond + 0.5D);
    }

    public void setOnChangeListener(OnTrimInChangeListener onChangeListener) {
        this.mOnTrimInChangeListener = onChangeListener;
    }

    public void setOnChangeListener(OnTrimOutChangeListener onChangeListener) {
        this.mOnTrimOutChangeListener = onChangeListener;
    }
    public void setMarginChangeListener(OnMarginChangeListener marginChangeListener) {
        this.mMarginChangeListener = marginChangeListener;
    }

    public interface OnTrimInChangeListener {
         void onChange(long timeStamp,boolean isDragEnd);
    }

    public interface OnTrimOutChangeListener {
         void onChange(long timeStamp,boolean isDragEnd);
    }
    public interface OnMarginChangeListener{
         void onChange(int leftMargin, int timeSpanWidth);
    }

    /**
     * 触摸点为右边缘
     */
    private void right(int dx) {
        originRight += dx;
        if (originRight >= mTotalWidth ) {
            originRight = mTotalWidth;
        }
        if (originRight - mLLeftHandleWidth - (originLeft + mLLeftHandleWidth) < minDraggedTimeSpanPixel) {
            originRight = originLeft + minDraggedTimeSpanPixel + 2 * mLLeftHandleWidth;
        }
    }

    /**
     * 触摸点为左边缘
     */
    private void left(int dx) {
        originLeft += dx;
        if (originLeft < 0) {
            originLeft = 0;
        }
        if (originRight - mLLeftHandleWidth - (originLeft + mLLeftHandleWidth) < minDraggedTimeSpanPixel) {
            originLeft = originRight - 2 * mLLeftHandleWidth - minDraggedTimeSpanPixel;
        }
    }

    private void leftHandleMove(int dx){
        left(dx);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
        lp.width = originRight - originLeft;
        lp.setMargins(originLeft, RelativeLayout.LayoutParams.MATCH_PARENT, mTotalWidth - originRight , 0);
        setLayoutParams(lp);
        int leftPixel = originLeft + mLLeftHandleWidth;
        int rightPixel = originRight - mLLeftHandleWidth;
        mInPoint = multiThumbnailSequenceView.mapTimelinePosFromX(leftPixel);
        if(mMarginChangeListener != null){
            mMarginChangeListener.onChange(leftPixel, rightPixel - leftPixel);
        }
    }
    private void rightHandleMove(int dx){
        right(dx);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
        lp.width = originRight - originLeft;
        lp.setMargins(originLeft, RelativeLayout.LayoutParams.MATCH_PARENT, mTotalWidth - originRight , 0);
        setLayoutParams(lp);
        int leftPixel = originLeft + mLLeftHandleWidth;
        int rightPixel = originRight - mLLeftHandleWidth;
        mOutPoint = multiThumbnailSequenceView.mapTimelinePosFromX(rightPixel);
        if(mMarginChangeListener != null){
            mMarginChangeListener.onChange(leftPixel, rightPixel - leftPixel);
        }
    }

    private void setTrimInCallback(boolean isDragEnd){
        if(mOnTrimInChangeListener != null) {
            mOnTrimInChangeListener.onChange(mInPoint, isDragEnd);
        }
    }
    private void setTrimOutCallback(boolean isDragEnd){
        if(mOnTrimOutChangeListener != null) {
            mOnTrimOutChangeListener.onChange(mOutPoint, isDragEnd);
        }
    }
}
