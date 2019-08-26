package com.meishe.sdkdemo.edit.record;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ms on 2018/8/8 0008.
 */

public class SqLayout extends RelativeLayout {
    private Context m_context;
    private LinearLayout m_allViewsLayout;
    private RelativeLayout m_recordAreasLayout;
    private SqView m_sqView;
    private Map<Long, SqAreaInfo> m_areasInfo;
    private HorizontalScrollListener m_horizontalScrollListener;
    private boolean m_touchEnabled = false;

    public SqLayout(Context context) {
        super(context);
        init(context);
    }

    public SqLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public void setHorizontalScrollListener(HorizontalScrollListener listener){
        m_horizontalScrollListener = listener;
    }

    public interface HorizontalScrollListener{
        void horizontalScrollStoped();
        void horizontalScrollChanged(long inPoint, boolean isDraging, long cur_audio_inpoint);
    }

    private void init(Context context) {
        m_context = context;
        m_areasInfo = new HashMap<>();

        m_sqView = new SqView(context);
        RelativeLayout.LayoutParams sqParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(m_sqView, sqParams);

        m_sqView.setHorizontalScrollListener(new SqView.HorizontalScrollListener() {
            @Override
            public void horizontalScrollStoped() {
                if(m_horizontalScrollListener != null) {
                    m_horizontalScrollListener.horizontalScrollStoped();
                }
            }

            @Override
            public void horizontalScrollChanged(int pos, long inPoint, boolean isDraging) {
                m_allViewsLayout.scrollTo(pos, 0);
                if(m_horizontalScrollListener != null) {
                    long cur_audio_inpoint = -1;
                    for (Map.Entry<Long, SqAreaInfo> entry : m_areasInfo.entrySet()) {
                        SqAreaInfo areaInfo = entry.getValue();
                        if(areaInfo == null) {
                            continue;
                        }
                        // Log.e("===>", "area in: " + areaInfo.getInPoint() + " " + inPoint + " area out: " + areaInfo.getOutPoint());
                        if(inPoint >= areaInfo.getInPoint() && inPoint <= areaInfo.getOutPoint()) {
                            cur_audio_inpoint = areaInfo.getInPoint();
                            break;
                        }
                    }
                    m_horizontalScrollListener.horizontalScrollChanged(inPoint, isDraging, cur_audio_inpoint);
                }
            }
        });

        m_sqView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return m_touchEnabled;
            }
        });
    }

    public SqView getSqView() {
        return m_sqView;
    }

    public void initData(long total_duration, ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> infoDescArray) {
        m_sqView.initData(total_duration, infoDescArray);

        m_allViewsLayout = new LinearLayout(m_context);
        LinearLayout.LayoutParams allViewsParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(m_allViewsLayout, allViewsParams);

        m_recordAreasLayout = new RelativeLayout(m_context);
        LinearLayout.LayoutParams recordAreaParams = new LinearLayout.LayoutParams(m_sqView.getSequenceWidth(), ViewGroup.LayoutParams.MATCH_PARENT);

        View leftMarginView = new View(m_context);
        LinearLayout.LayoutParams leftMarginViewParams = new LinearLayout.LayoutParams(m_sqView.getStartPadding(), LayoutParams.MATCH_PARENT);
        View reghtMarginView = new View(m_context);
        LinearLayout.LayoutParams reghtMarginViewParams = new LinearLayout.LayoutParams(m_sqView.getEndPadding(), LayoutParams.MATCH_PARENT);
        m_allViewsLayout.addView(leftMarginView, leftMarginViewParams);
        m_allViewsLayout.addView(m_recordAreasLayout, recordAreaParams);
        m_allViewsLayout.addView(reghtMarginView, reghtMarginViewParams);
    }

    public void reLayoutAllViews() {
        LinearLayout.LayoutParams recordAreaParams = (LinearLayout.LayoutParams) m_recordAreasLayout.getLayoutParams();
        recordAreaParams.width = m_sqView.getSequenceWidth();
        m_recordAreasLayout.setLayoutParams(recordAreaParams);

        for(int i = 0; i < m_recordAreasLayout.getChildCount(); ++i) {
            View child = m_recordAreasLayout.getChildAt(i);
            if(child == null) {
                continue;
            }
            SqAreaInfo areaInfo = (SqAreaInfo) child.getTag();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) child.getLayoutParams();
            if(layoutParams != null && areaInfo != null) {
                int begin_point = (int) (areaInfo.getInPoint() * m_sqView.getPixelPerMicrosecond());
                int view_width = (int) ((areaInfo.getOutPoint() - areaInfo.getInPoint()) * m_sqView.getPixelPerMicrosecond());
                layoutParams.setMargins(begin_point, 0, 0 , 0);
                layoutParams.width = view_width;
                child.setLayoutParams(layoutParams);
            }
        }
        m_recordAreasLayout.requestLayout();
    }

    public void setTouchEnabled(boolean enabled) {
        m_touchEnabled = enabled;
    }

    public void addRecordView(long inPoint, long outPoint) {
        int begin_point = (int) (inPoint * m_sqView.getPixelPerMicrosecond());
        View view = new View(m_context);
        view.setBackgroundColor(Color.parseColor("#994a90e2"));

        int view_width = 0;
        if(outPoint > 0) {
            view_width = (int) ((outPoint - inPoint) * m_sqView.getPixelPerMicrosecond());
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(view_width, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(begin_point, 0, 0 , 0);
        m_recordAreasLayout.addView(view, layoutParams);

        SqAreaInfo areaInfo = new SqAreaInfo();
        areaInfo.setInPoint(inPoint);
        areaInfo.setOutPoint(outPoint);
        areaInfo.setInPosition(begin_point);
        areaInfo.setAreaView(view);
        view.setTag(areaInfo);
        m_areasInfo.put(inPoint, areaInfo);
    }

    public void deleteRecordView(long inPoint) {
        for (Map.Entry<Long, SqAreaInfo> entry : m_areasInfo.entrySet()) {
            SqAreaInfo areaInfo = entry.getValue();
            if(areaInfo == null) {
                continue;
            }
            if(inPoint == areaInfo.getInPoint()) {
                if(areaInfo.getAreaView() != null) {
                    m_recordAreasLayout.removeView(areaInfo.getAreaView());
                    m_areasInfo.remove(inPoint);
                    break;
                }
            }
        }
        m_recordAreasLayout.requestLayout();
    }

    public void updateRecordView(long inPoint, long outPoint) {
        SqAreaInfo areaInfo = m_areasInfo.get(inPoint);
        if(areaInfo == null || areaInfo.getAreaView() == null) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) areaInfo.getAreaView().getLayoutParams();
        if(layoutParams != null) {
            int begin_point = areaInfo.getInPosition();
            int view_width = (int) ((outPoint - inPoint) * m_sqView.getPixelPerMicrosecond());
            layoutParams.setMargins(begin_point, 0, 0 , 0);
            layoutParams.width = view_width;
            areaInfo.getAreaView().setLayoutParams(layoutParams);
            areaInfo.setOutPoint(outPoint);
            m_recordAreasLayout.requestLayout();
        }
    }

    public void clearAllAreas() {
        m_areasInfo.clear();
        m_recordAreasLayout.removeAllViews();
    }
}
