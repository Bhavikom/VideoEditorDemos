package com.meishe.sdkdemo.douvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimeFormatUtil;

/**
 * Created by admin on 2018/11/16.
 * 倒计时拍摄，滑动滑块设置倒计时拍摄的时长
 */

public class CountDownDurationAdjustView extends RelativeLayout {
    private View mCountDownBackgourd;
    //已经拍摄的视频时长
    private View mCaptureFinishedDura;
    private TextView mDuraStartVal;
    private TextView mDuraEndVal;
    private TextView mShowCurTime;
    private ImageView mOperateHandle;

    private int mTotalWidth = 0;
    private int mHandleWidth = 0;
    private int mLeftMargin = 0;
    private float mPrevRawX;
    private int mHandleLeft = 0;
    private int mRightTextViewShowPos;
    private int mLeftTextViewShowPos;

    private StringBuilder mStrShowDuration = new StringBuilder("0s");
    //最大拍摄时长值
    public long mMaxCaptureDuration = 0;
    //mHasFinishedDuraViewWidth = 0即未进行过倒计时拍摄；不为0，则已进行一次或者多次倒计时拍摄
    private int mHasFinishedDuraViewWidth = 0;
    //
    private long mNewCaptureDuration = 0;
    private OnCaptureDurationChangeListener mNewDurationChangeListener;

    public interface OnCaptureDurationChangeListener {
        void onNewDurationChange(long newDuration,boolean isDragEnd);
    }
    public void setNewDurationChangeListener(OnCaptureDurationChangeListener newDurationChangeListener) {
        this.mNewDurationChangeListener = newDurationChangeListener;
    }

    public CountDownDurationAdjustView(Context context){
        this(context, null);
    }
    public CountDownDurationAdjustView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    //设置当前倒计时拍摄时长
    public void setNewCaptureDuration(long newCaptureDuration) {
        this.mNewCaptureDuration = newCaptureDuration;
    }

    //对象创建后需要设置最大拍摄时长
    public void setMaxCaptureDuration(long maxCaptureDuration) {
        mMaxCaptureDuration = maxCaptureDuration;
    }
    //对象创建后需要设置当前已拍摄时长
    public void setCurCaptureDuaration(long finishedDuaration){
        float percent = finishedDuaration / (float)mMaxCaptureDuration;
        mHasFinishedDuraViewWidth = (int)Math.floor(percent * mTotalWidth);
        ViewGroup.LayoutParams params = mCaptureFinishedDura.getLayoutParams();
        params.width = mHasFinishedDuraViewWidth;
        mCaptureFinishedDura.setLayoutParams(params);
    }
    public void resetHandleViewPos(){
        mHandleLeft = mRightTextViewShowPos;
        updateHandleViewPos();
        mShowCurTime.setVisibility(View.GONE);
        mDuraEndVal.setVisibility(View.VISIBLE);
    }

    private void init(Context context){
        View rootView = LayoutInflater.from(context).inflate(R.layout.countdown_capture, this);
        mCountDownBackgourd = rootView.findViewById(R.id.countDownBackgourd);
        mCaptureFinishedDura = rootView.findViewById(R.id.captureFinishedDura);
        mDuraStartVal = (TextView)rootView.findViewById(R.id.duraStartVal);
        mDuraEndVal = (TextView)rootView.findViewById(R.id.duraEndVal);
        mShowCurTime = (TextView)rootView.findViewById(R.id.showCurTime);
        mOperateHandle = (ImageView)rootView.findViewById(R.id.operateHandle);
        mOperateHandle.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            getParent().requestDisallowInterceptTouchEvent(true);
                            mHandleLeft = mOperateHandle.getLeft();
                            mPrevRawX = (int) event.getRawX();
                            updateShowCurTimeTextView();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            getParent().requestDisallowInterceptTouchEvent(true);
                            float curRawX = event.getRawX();
                            int dx = (int) Math.floor(curRawX - mPrevRawX + 0.5D);
                            mPrevRawX = curRawX;
                            moveHandleOperaView(dx);
                            break;
                        case MotionEvent.ACTION_UP:
                            getParent().requestDisallowInterceptTouchEvent(false);
                            updateShowCurTimeTextView();
                            setNewDurationChangeCallback(true);
                            break;
                    }
                    return true;
                }
         });

        mHandleWidth = mOperateHandle.getLayoutParams().width;
        RelativeLayout.LayoutParams backgroudParams = (RelativeLayout.LayoutParams)mCountDownBackgourd.getLayoutParams();
        mLeftMargin = backgroudParams.leftMargin;
        int screenWidth = ScreenUtils.getScreenWidth(context);
        mTotalWidth = screenWidth - 2 * mLeftMargin;
        mLeftTextViewShowPos = mHandleWidth - mLeftMargin;
        mRightTextViewShowPos = screenWidth - mHandleWidth;
        mHandleLeft = mRightTextViewShowPos;
    }

    private void moveHandleOperaView(int dx){
        mHandleLeft += dx;
        if (mHandleLeft <= mHasFinishedDuraViewWidth) {
            mHandleLeft = mHasFinishedDuraViewWidth;
        }

        if (mHandleLeft + mHandleWidth >= mTotalWidth + 2 * mLeftMargin) {
            mHandleLeft = mTotalWidth + 2 * mLeftMargin - mHandleWidth;
        }
        updateHandleViewPos();

        int newPixelPos = mHandleLeft + mHandleWidth -  2 * mLeftMargin;
        float resPercent = (newPixelPos -  mHasFinishedDuraViewWidth) / (float)mTotalWidth;
        mNewCaptureDuration = (long) Math.floor(resPercent * mMaxCaptureDuration);
        setNewDurationChangeCallback(false);
        updateShowCurTimeTextView();
    }
    private void updateShowCurTimeTextView(){
        mShowCurTime.setVisibility(View.VISIBLE);
        int newPixelPos = mHandleLeft + mHandleWidth -  2 * mLeftMargin;
        float percent = newPixelPos / (float)mTotalWidth;
        long curDuration = (long)Math.floor(percent * mMaxCaptureDuration);
        String strTimeDuration = TimeFormatUtil.formatUsToString3(curDuration);
        int index = mStrShowDuration.indexOf("s");
        mStrShowDuration.replace(0,index,strTimeDuration);
        mShowCurTime.setText(mStrShowDuration.toString());
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mShowCurTime.getLayoutParams();
        lp.leftMargin = mHandleLeft ;
        mShowCurTime.setLayoutParams(lp);
        if(mHandleLeft + mHandleWidth <= mRightTextViewShowPos){
            mDuraEndVal.setVisibility(View.VISIBLE);
        }else {
            mDuraEndVal.setVisibility(View.GONE);
        }
        if(mHandleLeft >= mLeftTextViewShowPos){
            mDuraStartVal.setVisibility(View.VISIBLE);
        }else {
            mDuraStartVal.setVisibility(View.GONE);
        }
    }

    private void updateHandleViewPos(){
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mOperateHandle.getLayoutParams();
        lp.leftMargin = mHandleLeft;
        mOperateHandle.setLayoutParams(lp);

    }

    private void setNewDurationChangeCallback(boolean isDragEnd){
        if(mNewDurationChangeListener != null) {
            mNewDurationChangeListener.onNewDurationChange(mNewCaptureDuration,isDragEnd);
        }
    }
}
