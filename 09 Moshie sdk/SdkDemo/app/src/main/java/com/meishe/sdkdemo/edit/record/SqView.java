package com.meishe.sdkdemo.edit.record;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meishe.sdkdemo.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by ms on 2018/8/7.
 */

public class SqView extends NvsMultiThumbnailSequenceView {
    private static final String TAG = "SqView";
    private Context m_context;
    private final long TIMEBASE = 1000000;
    private final double WHOLE_SCREEN_DURATION = 20 * TIMEBASE;
    private double m_pixelPerMicrosecond, m_maxPixelPerMicrosecond, m_minPixelPerMicrosecond;
    private long m_totalDuration;

    // 滑动停止监听
    private int m_lastX = 0, m_touchEventId = -9983761;
    private HorizontalScrollListener m_horizontalScrollListener;
    private boolean m_isDraging = false;

    @SuppressLint("HandlerLeak")
    private Handler m_handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View scroller = (View) msg.obj;
            if (msg.what == m_touchEventId) {
                if (m_lastX == scroller.getScrollX()) {
                    if(m_horizontalScrollListener != null){
                        m_horizontalScrollListener.horizontalScrollStoped();
                    }
                } else {
                    m_handler.sendMessageDelayed(m_handler.obtainMessage(m_touchEventId, scroller), 5);
                    m_lastX = scroller.getScrollX();
                }
            }
        }
    };

    public SqView(Context context) {
        super(context);
        init(context);
    }

    public SqView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        m_context = context;
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        m_pixelPerMicrosecond = getScreenWidth() / WHOLE_SCREEN_DURATION;
        m_maxPixelPerMicrosecond = m_pixelPerMicrosecond * Math.pow(1.25,5);
        m_minPixelPerMicrosecond = m_pixelPerMicrosecond * Math.pow(0.8,5);
    }

    public void setPixelPerMicrosecond2(double pixelPerMicrosecond) {
        m_pixelPerMicrosecond = pixelPerMicrosecond;
        m_maxPixelPerMicrosecond = m_pixelPerMicrosecond * Math.pow(1.25,5);
        m_minPixelPerMicrosecond = m_pixelPerMicrosecond * Math.pow(0.8,5);
    }

    public void setHorizontalScrollListener(HorizontalScrollListener listener){
        m_horizontalScrollListener = listener;
    }

    public interface HorizontalScrollListener{
        void horizontalScrollStoped();
        void horizontalScrollChanged(int pos, long inPoint, boolean isDraging);
    }

    public void initData(long total_duration, ArrayList<ThumbnailSequenceDesc> infoDescArray) {
        m_totalDuration = total_duration;

        this.setPixelPerMicrosecond(m_pixelPerMicrosecond);
        this.setThumbnailSequenceDescArray(infoDescArray);
    }

    public void zoomOutSequence(){
        if(m_pixelPerMicrosecond < m_minPixelPerMicrosecond)
            return;
        this.scaleWithAnchor(0.8, this.getStartPadding());
        m_pixelPerMicrosecond = this.getPixelPerMicrosecond();
    }

    public void zoomInSequence(){
        if(m_pixelPerMicrosecond > m_maxPixelPerMicrosecond)
            return;
        this.scaleWithAnchor(1.25, this.getStartPadding());
        m_pixelPerMicrosecond = this.getPixelPerMicrosecond();
    }

    public int getSequenceWidth(){
        return (int)Math.floor(m_totalDuration * m_pixelPerMicrosecond + 0.5D);
    }

    private int getScreenWidth() {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        return metrics.widthPixels - ScreenUtils.dip2px(m_context, 20);
    }

    public void scrollTo(long time_position) {
        int x_pos = (int)Math.floor(time_position * m_pixelPerMicrosecond + 0.5D);
        smoothScrollTo(x_pos, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                m_isDraging = false;
                break;

            case MotionEvent.ACTION_MOVE:
                m_isDraging = true;
                break;

            case MotionEvent.ACTION_UP:
                m_isDraging = false;
                m_handler.sendMessageDelayed(m_handler.obtainMessage(m_touchEventId, this), 5);
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        long tmpTimeStamp = (long) Math.floor(l / m_pixelPerMicrosecond + 0.5D);
        if (m_horizontalScrollListener != null) {
            m_horizontalScrollListener.horizontalScrollChanged(l, tmpTimeStamp, m_isDraging);
        }
    }
}
