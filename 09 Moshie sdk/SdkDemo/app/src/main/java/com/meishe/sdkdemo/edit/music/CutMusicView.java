package com.meishe.sdkdemo.edit.music;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.TimeFormatUtil;

/**
 * Created by ms on 2018/7/13 0027.
 * note: 此控件中的所有时间，均是微妙
 */

public class CutMusicView extends RelativeLayout {
    private final String TAG = "CutMusicView";
    private Context mContext;
    private RelativeLayout mMainLayout, mHandleLayout;
    private ImageView mLeftHandle, mRightHandle;
    private View mIndicatorView;
    private OnSeekBarChanged mListener;
    private boolean mCanTouchCenter = true;

    private int mCurrentTouch = -1;
    private final int TOUCH_LEFT = 1, TOUCH_CENTER = 2, TOUCH_RIGHT = 3;

    private int prevRawX = 0;
    private int mTotalWidth = 0, mHandleWidth = 0, mIndicatorWidth, mLeftHandleWidth, mRightHandleWidth;
    private int originLeft = 0, originRight = 0, leftToRight = 0;

    private long mMinDuration = 0, mMaxDuration = 0;
    private int mMinSpan = 0, mDurationWidth = 0, mPerSecondWidth = 0, m_touchWidth = 20;
    private long mInPoint = 0, mOutPoint = 0;

    public CutMusicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CutMusicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CutMusicView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.cut_music_view, this);
        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mHandleLayout = (RelativeLayout) findViewById(R.id.handle_layout);
        mLeftHandle = (ImageView) findViewById(R.id.leftHandle);
        mRightHandle = (ImageView) findViewById(R.id.rightHandle);
        mIndicatorView = findViewById(R.id.indicator_view);
        mLeftHandleWidth = mLeftHandle.getLayoutParams().width;
        mRightHandleWidth = mRightHandle.getLayoutParams().width;
        mHandleWidth = mLeftHandleWidth + mRightHandleWidth;
        mIndicatorWidth = mIndicatorView.getLayoutParams().width;
    }

    public void setCutLayoutWidth(int width) {
        mTotalWidth = width;
        mDurationWidth = mTotalWidth - mHandleWidth;
    }

    public void setMinDuration(long min_duration) {
        mMinDuration = min_duration;
    }

    public long getMinDuration() {
        return mMinDuration;
    }

    public void setMaxDuration(long max_duration) {
        mMaxDuration = max_duration;
        mOutPoint = mMaxDuration;

        double per_second_w = 1000000 / mMaxDuration * mDurationWidth;
        mPerSecondWidth = (int) per_second_w;
    }

    public long getInPoint() {
        return mInPoint;
    }

    public void setInPoint(long inPoint) {
        this.mInPoint = inPoint;
    }

    public long getOutPoint() {
        return mOutPoint;
    }

    public void setOutPoint(long outPoint) {
        this.mOutPoint = outPoint;
    }

    public void reLayout() {
        originRight = mTotalWidth;
        originLeft = 0;
        LayoutParams lp = (LayoutParams) mHandleLayout.getLayoutParams();
        lp.width = originRight - originLeft;
        lp.setMargins(originLeft, 0, mTotalWidth - originRight , 0);
        mHandleLayout.setLayoutParams(lp);
    }

    public void setIndicator(long inPoint) {
        double per_second_w = (double) inPoint / mMaxDuration * mDurationWidth;

        LayoutParams lp = (LayoutParams) mIndicatorView.getLayoutParams();
        lp.width = mIndicatorWidth;
        lp.setMargins((int) per_second_w + mLeftHandleWidth,0, 0 , 0);
        mIndicatorView.setLayoutParams(lp);
    }

    public void setCanTouchCenterMove(boolean can_move) {
        mCanTouchCenter = can_move;
    }

    public void setOnSeekBarChangedListener(OnSeekBarChanged mListener) {
        this.mListener = mListener;
    }

    public void setRightHandleVisiable(boolean visiable) {
        if(visiable) {
            mRightHandle.setVisibility(VISIBLE);
            mRightHandleWidth = mRightHandle.getLayoutParams().width;
        } else {
            mRightHandle.setVisibility(INVISIBLE);
            mRightHandleWidth = 0;
        }
        mHandleWidth = mLeftHandleWidth + mRightHandleWidth;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float cur_x = event.getX();
        float cur_y = event.getY();
        Log.e("===>", "x: " + cur_x + " " + " y: " + cur_y);

        // 避免误差
        String min_duration_text = TimeFormatUtil.formatUsToString2(mMinDuration);
        String max_duration_text = TimeFormatUtil.formatUsToString2(mMaxDuration);
        if(min_duration_text.equals(max_duration_text) || mMinDuration >= mMaxDuration) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                originLeft = mHandleLayout.getLeft();
                originRight = mHandleLayout.getRight();
                prevRawX = (int) event.getRawX();

                mCurrentTouch = getTouchMode(cur_x, cur_y);
                break;
            case MotionEvent.ACTION_MOVE:
                int tempRawX = (int)event.getRawX();
                int dx = tempRawX - prevRawX;
                prevRawX = tempRawX;

                if(mCurrentTouch == TOUCH_LEFT) {
                    left(dx);
                    LayoutParams lp = (LayoutParams) mHandleLayout.getLayoutParams();
                    lp.width = originRight - originLeft;
                    lp.setMargins(originLeft, 0, mTotalWidth - originRight , 0);
                    mHandleLayout.setLayoutParams(lp);

                    mInPoint = (long) Math.floor((float) originLeft / mDurationWidth * mMaxDuration + 0.5D);
                    if (mListener != null)
                        mListener.onLeftValueChange(mInPoint);

                } else if(mCurrentTouch == TOUCH_RIGHT) {
                    right(dx);
                    LayoutParams lp = (LayoutParams) mHandleLayout.getLayoutParams();
                    lp.width = originRight - originLeft;
                    lp.setMargins(originLeft, 0, mTotalWidth - originRight , 0);
                    mHandleLayout.setLayoutParams(lp);

                    mOutPoint = (long) Math.floor((float) (originRight - mHandleWidth) / mDurationWidth * mMaxDuration + 0.5D);
                    if (mListener != null)
                        mListener.onRightValueChange(mOutPoint);

                } else if(mCurrentTouch == TOUCH_CENTER) {
                    if(!mCanTouchCenter) {
                        break;
                    }
                    leftToRight = mMainLayout.getWidth();

                    center(dx);

                    LayoutParams lp = (LayoutParams) mHandleLayout.getLayoutParams();
                    lp.setMargins(originLeft, 0, mTotalWidth - originRight , 0);
                    mHandleLayout.setLayoutParams(lp);

                    mInPoint = (long) Math.floor((float) originLeft / mDurationWidth * mMaxDuration + 0.5D);
                    mOutPoint = (long) Math.floor((float) (originRight - mHandleWidth) / mDurationWidth * mMaxDuration + 0.5D);
                    if (mListener != null)
                        mListener.onCenterTouched(mInPoint, mOutPoint);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mListener != null) {
                    if(mCurrentTouch == TOUCH_LEFT) {
                        mListener.onUpTouched(true, mInPoint, mOutPoint);
                    } else {
                        mListener.onUpTouched(false, mInPoint, mOutPoint);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private int getTouchMode(float x, float y) {
        int left = mHandleLayout.getLeft();
        int right = mHandleLayout.getRight();

        Log.e("===>", "left: " + left + " right: " + right + " x: " + x + " handle_width: " + mHandleWidth);
        // 此处m_touchWidth是为了增加touch区间，使触摸更加敏感
        if (x - left < mLeftHandleWidth + m_touchWidth && x - left > 0) {
            return TOUCH_LEFT;
        }
        if (right - x < mRightHandleWidth + m_touchWidth && right - x > 0) {
            return TOUCH_RIGHT;
        }
        return TOUCH_CENTER;
    }

    /**
     * 触摸点为右边缘
     */
    private void right(int dx) {
        mMinSpan = (int) Math.floor((float) mMinDuration / mMaxDuration * mDurationWidth + 0.5D);

        originRight += dx;
        if (originRight > mTotalWidth ) {
            originRight = mTotalWidth;
        }
        if (originRight - originLeft - mHandleWidth  < mMinSpan) {
            originRight = originLeft  + mMinSpan + mHandleWidth;
        }
    }

    /**
     * 触摸点为左边缘
     */
    private void left(int dx) {
        mMinSpan = (int) Math.floor((float) mMinDuration / mMaxDuration * mDurationWidth + 0.5D);

        originLeft += dx;
        if (originLeft < 0) {
            originLeft = 0;
        }
        if (originRight - originLeft - mHandleWidth  < mMinSpan) {
            originLeft = originRight - mHandleWidth - mMinSpan;
        }
    }

    /**
     * 触摸点为中心
     */
    private void center(int dx) {
        originLeft += dx;
        originRight += dx;
        if (originLeft <= 0) {
            originLeft = 0;
            originRight  = originLeft + leftToRight;
        }
        if (originRight > mTotalWidth ) {
            originRight = mTotalWidth;
            originLeft = originRight - leftToRight;
        }
    }

    public interface OnSeekBarChanged {
        void onLeftValueChange(long var);
        void onRightValueChange(long var);
        void onCenterTouched(long left, long right);
        void onUpTouched(boolean touch_left, long left, long right);
    }
}
