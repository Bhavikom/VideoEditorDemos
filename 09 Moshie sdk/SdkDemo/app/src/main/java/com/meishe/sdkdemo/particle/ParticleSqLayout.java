package com.meishe.sdkdemo.particle;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.music.HandImageView;
import com.meishe.sdkdemo.edit.record.SqAreaInfo;
import com.meishe.sdkdemo.edit.record.SqView;
import com.meishe.sdkdemo.utils.ColorUtil;
import com.meishe.sdkdemo.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ms on 2018/8/8 0008.
 */

public class ParticleSqLayout extends RelativeLayout {
    private Context m_context;
    private RelativeLayout m_fxAreasLayout;
    private SqView m_sqView;
    private Map<Long, SqAreaInfo> m_areasInfo;
    private int m_sqHeight, m_lastX, m_LeftMargin;
    private HandImageView m_indicatorView;
    private int m_currentSequenceColor;
    private int[] m_sequenceColor = {ColorUtil.SOUL_COLOR, ColorUtil.IMAGE_COLOR,ColorUtil.SHAKE_COLOR,
            ColorUtil.WAVE_COLOR,ColorUtil.BLACK_MAGIC_COLOR,ColorUtil.HALLUCINATION_COLOR,ColorUtil.ZOOM_COLOR,
            ColorUtil.NEON_COLOR,ColorUtil.FLICKER_AND_WHITE_COLOR};
    private OnDataChangedListener OnDataChangedListener;


    public void setOndataChanged(OnDataChangedListener listener) {
        this.OnDataChangedListener = listener;
    }

    public interface OnDataChangedListener {
        void onDataChange(long time);
    }

    public ParticleSqLayout(Context context) {
        super(context);
        init(context);
    }

    public ParticleSqLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        m_context = context;
        m_areasInfo = new HashMap<>();
        m_sqHeight = ScreenUtils.dip2px(context, 30);

        m_sqView = new SqView(context);
        LayoutParams sqParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, m_sqHeight);
        sqParams.addRule(CENTER_VERTICAL);
        addView(m_sqView, sqParams);
    }

    public SqView getSqView() {
        return m_sqView;
    }

    public void initData(long total_duration, ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> infoDescArray) {
        m_sqView.initData(total_duration, infoDescArray);

        m_fxAreasLayout = new RelativeLayout(m_context);
        LayoutParams fxAreaParams = new LayoutParams(m_sqView.getSequenceWidth(), m_sqHeight);
        fxAreaParams.addRule(CENTER_VERTICAL);
        addView(m_fxAreasLayout, fxAreaParams);

        m_indicatorView = new HandImageView(m_context);
        LayoutParams indicatorParams = new LayoutParams(ScreenUtils.dip2px(m_context, 5), ViewGroup.LayoutParams.MATCH_PARENT);
        m_indicatorView.setBackgroundColor(ContextCompat.getColor(m_context, R.color.ff4a90e2));
        addView(m_indicatorView, indicatorParams);

        m_indicatorView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                LayoutParams indicatorParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                if(indicatorParams == null) {
                    return true;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        m_lastX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int curMoveX = (int)event.getRawX();
                        int dx = curMoveX - m_lastX;
                        Object tag = view.getTag();
                        if(tag == null) {
                            m_LeftMargin = dx;
                        } else {
                            m_LeftMargin = (int) tag + dx;
                        }
                        if(m_LeftMargin < 0) {
                            m_LeftMargin = 0;
                        } else if(m_LeftMargin > m_sqView.getSequenceWidth() - m_indicatorView.getWidth()) {
                            m_LeftMargin = m_sqView.getSequenceWidth() - m_indicatorView.getWidth();
                        }
                        indicatorParams.setMargins(m_LeftMargin, 0, 0 , 0);
                        view.setLayoutParams(indicatorParams);

                        long curMoveTime = (long) (m_LeftMargin / m_sqView.getPixelPerMicrosecond());
                        OnDataChangedListener.onDataChange(curMoveTime);
                        break;
                    case MotionEvent.ACTION_UP:
                        long curUpTime = (long) (m_LeftMargin / m_sqView.getPixelPerMicrosecond());
                        m_indicatorView.setTag(m_LeftMargin);
                        OnDataChangedListener.onDataChange(curUpTime);

                        Log.e("===>", "-------up: " + curUpTime);
                        break;
                }
                return true;
            }
        });
    }

    public void updateIndicator(long time) {
        if(m_indicatorView == null)
            return;
        LayoutParams indicatorParams = (RelativeLayout.LayoutParams) m_indicatorView.getLayoutParams();
        if(indicatorParams == null) {
            return;
        }
        int x_pos = (int)Math.floor(time * m_sqView.getPixelPerMicrosecond() + 0.5D);
        if(x_pos < 0) {
            x_pos = 0;
        } else if(x_pos > m_sqView.getSequenceWidth() - m_indicatorView.getWidth()) {
            x_pos = m_sqView.getSequenceWidth() - m_indicatorView.getWidth();
        }
        m_LeftMargin = x_pos;
        m_indicatorView.setTag(m_LeftMargin);
        indicatorParams.setMargins(x_pos, 0, 0 , 0);
        m_indicatorView.setLayoutParams(indicatorParams);
    }

    public void addFxView(long inPoint, long outPoint, int pos) {
        int begin_point = (int) (inPoint * m_sqView.getPixelPerMicrosecond());
        View view = new View(m_context);
        m_currentSequenceColor = m_sequenceColor[pos % m_sequenceColor.length];
        view.setBackgroundColor(m_currentSequenceColor);

        int view_width = 0;
        if(outPoint > 0) {
            view_width = (int) ((outPoint - inPoint) * m_sqView.getPixelPerMicrosecond());
        }
        LayoutParams layoutParams = new LayoutParams(view_width, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(begin_point, 0, 0 , 0);
        m_fxAreasLayout.addView(view, layoutParams);

        SqAreaInfo areaInfo = new SqAreaInfo();
        areaInfo.setInPoint(inPoint);
        areaInfo.setOutPoint(outPoint);
        areaInfo.setInPosition(begin_point);
        areaInfo.setAreaView(view);
        view.setTag(areaInfo);
        m_areasInfo.put(inPoint, areaInfo);

        Log.e("===>", "keys: " + m_areasInfo.keySet());
    }

    public void deleteFxView(long inPoint) {
        for (Map.Entry<Long, SqAreaInfo> entry : m_areasInfo.entrySet()) {
            SqAreaInfo areaInfo = entry.getValue();
            if(areaInfo == null) {
                continue;
            }
            if(inPoint == areaInfo.getInPoint()) {
                if(areaInfo.getAreaView() != null) {
                    m_fxAreasLayout.removeView(areaInfo.getAreaView());
                    m_areasInfo.remove(inPoint);
                    break;
                }
            }
        }
        m_fxAreasLayout.requestLayout();
    }

    public void updateFxView(long inPoint, long outPoint) {
        SqAreaInfo areaInfo = m_areasInfo.get(inPoint);
        if(areaInfo == null || areaInfo.getAreaView() == null) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams) areaInfo.getAreaView().getLayoutParams();
        if(layoutParams != null) {
            int begin_point = areaInfo.getInPosition();
            int view_width = (int) ((outPoint - inPoint) * m_sqView.getPixelPerMicrosecond());
            layoutParams.setMargins(begin_point, 0, 0 , 0);
            layoutParams.width = view_width;
            areaInfo.getAreaView().setLayoutParams(layoutParams);
            areaInfo.setOutPoint(outPoint);
            m_fxAreasLayout.requestLayout();
        }
    }

    public void clearAllAreas() {
        m_areasInfo.clear();
        m_fxAreasLayout.removeAllViews();
    }
}
