package com.meishe.sdkdemo.edit.timelineEditor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meishe.sdkdemo.R;

/**
 * Created by admin on 2018/6/8.
 * 添加字幕或者贴纸时添加到NvsMultiThumbnailSequenceView上的蒙层，可以修改所填字幕或者贴纸入点和出点
 */

public class NvsTimelineTimeSpan extends RelativeLayout {
    private String TAG = "NvsTimelineTimeSpan";
    private float prevRawX = 0;
    private boolean mCanMoveHandle = false;
    private OnTrimInChangeListener mOnTrimInChangeListener;
    private OnTrimOutChangeListener mOnTrimOutChangeListener;
    private OnMarginChangeListener mMarginChangeListener;
    //
    private int mHandleWidth = 0;
    private long mInPoint = 0;//裁剪入点
    private long mOutPoint = 0;//裁剪出点
    private double mPixelPerMicrosecond = 0D;//每秒所显示的像素值
    private boolean hasSelected = true;
    private int mTotalWidth = 0;
    private int mMinTimeSpanPixel = 0;//最小timespan宽度
    private int mMaxTimeSpanPixel = 0;//最大timespan宽度
    private int originLeft = 0;
    private int originRight = 0;
    private int dragDirection = 0;
    private static final int LEFT = 0x16;
    private static final int CENTER = 0x17;
    private static final int RIGHT = 0x18;

    private View timeSpanshadowView;
    private ImageView leftHandleView;
    private ImageView rightHandleView;

    public ImageView getLeftHandleView() {
        return leftHandleView;
    }

    public ImageView getRightHandleView() {
        return rightHandleView;
    }

    public NvsTimelineTimeSpan(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.timespan, this);
        leftHandleView = (ImageView) view.findViewById(R.id.leftHandle);
        rightHandleView = (ImageView) view.findViewById(R.id.rightHandle);
        mHandleWidth = leftHandleView.getLayoutParams().width;
        timeSpanshadowView = view.findViewById(R.id.timeSpanShadow);
    }

    public View getTimeSpanshadowView() {
        return timeSpanshadowView;
    }

    public void setMinTimeSpanPixel(int minTimeSpanPixel) {
        this.mMinTimeSpanPixel = minTimeSpanPixel;
    }

    public void setMaxTimeSpanPixel(int maxTimeSpanPixel) {
        this.mMaxTimeSpanPixel = maxTimeSpanPixel;
    }

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

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    public void setTotalWidth(int totalWidth) {
        this.mTotalWidth = totalWidth;
    }

    private void IsSelectedTimeSpan() {
        ImageView mLeftView = (ImageView) findViewById(R.id.leftHandle);
        ImageView mRightView = (ImageView) findViewById(R.id.rightHandle);
        if (mLeftView == null || mRightView == null) {
            return;
        }

        if (hasSelected) {
            mLeftView.setVisibility(View.VISIBLE);
            mRightView.setVisibility(View.VISIBLE);
        } else {
            mLeftView.setVisibility(View.INVISIBLE);
            mRightView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!hasSelected) {
            return false;//未被选中，不作编辑操作
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                mCanMoveHandle = !(mHandleWidth < ev.getX() && ev.getX() < getWidth() - mHandleWidth);
                originLeft = getLeft();
                originRight = getRight();
                prevRawX = (int) ev.getRawX();
                dragDirection = getDirection((int) ev.getX(), (int) ev.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                float tempRawX = ev.getRawX();
                int dx = (int) Math.floor(tempRawX - prevRawX + 0.5D);
                prevRawX = tempRawX;
                if (dragDirection == LEFT) {
                    left(dx);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
                    lp.width = originRight - originLeft;
                    lp.setMargins(originLeft, RelativeLayout.LayoutParams.MATCH_PARENT, mTotalWidth - originRight, 0);
                    setLayoutParams(lp);
                    mInPoint = (long) Math.floor(originLeft / mPixelPerMicrosecond + 0.5D);
                    if (mOnTrimInChangeListener != null) {
                        mOnTrimInChangeListener.onChange(mInPoint, false);
                    }
                }
                if (dragDirection == RIGHT) {
                    right(dx);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
                    lp.width = originRight - originLeft;
                    lp.setMargins(originLeft, RelativeLayout.LayoutParams.MATCH_PARENT, mTotalWidth - originRight, 0);
                    setLayoutParams(lp);
                    mOutPoint = (long) Math.floor(originRight / mPixelPerMicrosecond + 0.5D);
                    if (mOnTrimOutChangeListener != null) {
                        mOnTrimOutChangeListener.onChange(mOutPoint, false);
                    }
                }
                if (mMarginChangeListener != null) {
                    mMarginChangeListener.onChange(originLeft, originRight - originLeft);
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                if ((dragDirection == LEFT) && mOnTrimInChangeListener != null) {
                    mOnTrimInChangeListener.onChange(mInPoint, true);
                }
                if ((dragDirection == RIGHT) && mOnTrimOutChangeListener != null) {
                    mOnTrimOutChangeListener.onChange(mOutPoint, true);
                }
                break;
        }
        return mCanMoveHandle;
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

    public interface OnMarginChangeListener {
        void onChange(int leftMargin, int timeSpanWidth);
    }

    public interface OnTrimInChangeListener {
        void onChange(long timeStamp, boolean isDragEnd);
    }

    public interface OnTrimOutChangeListener {
        void onChange(long timeStamp, boolean isDragEnd);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        IsSelectedTimeSpan();
    }

    //判断触摸方向是左边还是右边
    private int getDirection(int x, int y) {
        int left = getLeft();
        int right = getRight();

        if (x < mHandleWidth) {
            return LEFT;
        }
        if (right - left - x < mHandleWidth) {
            return RIGHT;
        }
        return CENTER;
    }

    /**
     * 触摸点为右边缘
     */
    private void right(int dx) {
        originRight += dx;
        if (originRight >= mTotalWidth) {
            originRight = mTotalWidth;
        }
        if (originRight - originLeft <= mMinTimeSpanPixel) {
            originRight = originLeft + mMinTimeSpanPixel;
        }
        if (originRight - originLeft >= mMaxTimeSpanPixel) {
            originRight = originLeft + mMaxTimeSpanPixel;
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
        if (originRight - originLeft <= mMinTimeSpanPixel) {
            originLeft = originRight - mMinTimeSpanPixel;
        }
        if (originRight - originLeft >= mMaxTimeSpanPixel) {
            originLeft = originRight - mMaxTimeSpanPixel;
        }
    }
}
