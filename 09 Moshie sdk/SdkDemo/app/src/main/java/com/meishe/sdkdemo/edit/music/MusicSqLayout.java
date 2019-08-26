package com.meishe.sdkdemo.edit.music;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.record.SqAreaInfo;
import com.meishe.sdkdemo.edit.record.SqView;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ms on 2018/9/7 0008.
 */

public class MusicSqLayout extends RelativeLayout {
    private Context m_context;
    private LinearLayout m_allViewsLayout;
    private RelativeLayout m_recordAreasLayout;
    private SqView m_sqView;
    private Map<Long, SqAreaInfo> m_areasInfo;
    private HorizontalScrollListener m_horizontalScrollListener;
    private OnSeekValueListener m_seekValueListener;
    private boolean m_touchEnabled = false;
    private int m_handleWidth;
    private int m_lastX;
    private long m_duration, m_newInPoint, m_newOutPoint, m_minDuration = Constants.MUSIC_MIN_DURATION;

    public MusicSqLayout(Context context) {
        super(context);
        init(context);
    }

    public MusicSqLayout(Context context, AttributeSet attributeSet) {
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

    public void setOnSeekValueListener(OnSeekValueListener listener) {
        m_seekValueListener = listener;
    }

    public interface OnSeekValueListener {
        void onLeftValueChange(long var);
        void onRightValueChange(long var);
    }

    private void init(Context context) {
        m_context = context;
        m_areasInfo = new HashMap<>();
        m_handleWidth = ScreenUtils.dip2px(m_context, 12);

        m_sqView = new SqView(context);
        LayoutParams sqParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
                            if(areaInfo.getLeftHandle() != null && areaInfo.getRightHandle() != null) {
                                m_recordAreasLayout.bringChildToFront((LinearLayout) areaInfo.getLeftHandle().getParent());
                                areaInfo.getLeftHandle().setVisibility(VISIBLE);
                                areaInfo.getRightHandle().setVisibility(VISIBLE);
                            }
                        } else {
                            if(areaInfo.getLeftHandle() != null && areaInfo.getRightHandle() != null) {
                                areaInfo.getLeftHandle().setVisibility(INVISIBLE);
                                areaInfo.getRightHandle().setVisibility(INVISIBLE);
                            }
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
        m_duration = total_duration;

        m_allViewsLayout = new LinearLayout(m_context);
        LinearLayout.LayoutParams allViewsParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(m_allViewsLayout, allViewsParams);

        m_recordAreasLayout = new RelativeLayout(m_context);
        LinearLayout.LayoutParams recordAreaParams = new LinearLayout.LayoutParams(m_sqView.getSequenceWidth() + 2*m_handleWidth, ViewGroup.LayoutParams.MATCH_PARENT);

        View leftMarginView = new View(m_context);
        LinearLayout.LayoutParams leftMarginViewParams = new LinearLayout.LayoutParams(m_sqView.getStartPadding() - m_handleWidth, LayoutParams.MATCH_PARENT);
        View reghtMarginView = new View(m_context);
        LinearLayout.LayoutParams reghtMarginViewParams = new LinearLayout.LayoutParams(m_sqView.getEndPadding() - m_handleWidth, LayoutParams.MATCH_PARENT);
        m_allViewsLayout.addView(leftMarginView, leftMarginViewParams);
        m_allViewsLayout.addView(m_recordAreasLayout, recordAreaParams);
        m_allViewsLayout.addView(reghtMarginView, reghtMarginViewParams);
    }

    public void reLayoutAllViews() {
        LinearLayout.LayoutParams recordAreaParams = (LinearLayout.LayoutParams) m_recordAreasLayout.getLayoutParams();
        recordAreaParams.width = m_sqView.getSequenceWidth() + 2*m_handleWidth;
        m_recordAreasLayout.setLayoutParams(recordAreaParams);

        for(int i = 0; i < m_recordAreasLayout.getChildCount(); ++i) {
            LinearLayout oneAreaLayout = (LinearLayout) m_recordAreasLayout.getChildAt(i);
            if(oneAreaLayout == null) {
                continue;
            }
            SqAreaInfo areaInfo = (SqAreaInfo) oneAreaLayout.getTag();
            if(areaInfo == null || areaInfo.getAreaView() == null) {
                continue;
            }
            LinearLayout.LayoutParams areaViewParams = (LinearLayout.LayoutParams) areaInfo.getAreaView().getLayoutParams();
            if(areaViewParams != null) {
                int view_width = (int) ((areaInfo.getOutPoint() - areaInfo.getInPoint()) * m_sqView.getPixelPerMicrosecond());
                areaViewParams.width = view_width;
                areaInfo.getAreaView().setLayoutParams(areaViewParams);
            }
            LayoutParams layoutParams = (LayoutParams) oneAreaLayout.getLayoutParams();
            if(layoutParams != null) {
                int begin_point = (int) (areaInfo.getInPoint() * m_sqView.getPixelPerMicrosecond());
                layoutParams.setMargins(begin_point, 0, 0 , 0);
                oneAreaLayout.setLayoutParams(layoutParams);
            }
        }
        m_recordAreasLayout.requestLayout();
    }

    public void setTouchEnabled(boolean enabled) {
        m_touchEnabled = enabled;
    }

    public void addRecordView(long inPoint, long outPoint) {
        int begin_point = (int) (inPoint * m_sqView.getPixelPerMicrosecond());

        // 中间蒙层部分
        View view = new View(m_context);
        view.setBackgroundColor(Color.parseColor("#994a90e2"));
        int view_width = 0;
        if(outPoint > 0) {
            view_width = (int) ((outPoint - inPoint) * m_sqView.getPixelPerMicrosecond());
        }
        LayoutParams midParams = new LayoutParams(view_width, ViewGroup.LayoutParams.MATCH_PARENT);

        // 左把手
        HandImageView leftHand = new HandImageView(m_context);
        leftHand.setBackground(ContextCompat.getDrawable(m_context, R.mipmap.trimline));
        LayoutParams leftHandParams = new LayoutParams(m_handleWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        leftHand.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                LinearLayout oneAreaLayout = (LinearLayout) view.getParent();
                if(oneAreaLayout == null) {
                    return true;
                }
                LayoutParams layoutParams = (LayoutParams) oneAreaLayout.getLayoutParams();
                SqAreaInfo areaInfo = (SqAreaInfo) oneAreaLayout.getTag();
                if(areaInfo == null) {
                    return true;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        m_lastX = (int) event.getRawX();
                        Log.e("===>", "-------down: x: " + m_lastX);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int curMoveX = (int)event.getRawX();
                        int dx = curMoveX - m_lastX;

                        long inLimit = getLastOutPointByInPoint(areaInfo.getInPoint());
                        if(layoutParams != null) {
                            int begin_point = (int) (areaInfo.getInPoint() * m_sqView.getPixelPerMicrosecond());
                            int end_point = (int) ((areaInfo.getOutPoint() - m_minDuration) * m_sqView.getPixelPerMicrosecond());
                            long dTime = (long) (dx / m_sqView.getPixelPerMicrosecond());
                            int leftMargins = begin_point + dx;
                            if(areaInfo.getInPoint() + dTime < inLimit) {
                                m_newInPoint = inLimit;
                                leftMargins = (int) (inLimit * m_sqView.getPixelPerMicrosecond());;
                            } else if(areaInfo.getInPoint() + dTime + m_minDuration > areaInfo.getOutPoint()){
                                m_newInPoint = areaInfo.getOutPoint() - m_minDuration;
                                leftMargins = end_point;
                            } else {
                                m_newInPoint = areaInfo.getInPoint() + dTime;
                            }
                            layoutParams.setMargins(leftMargins, 0, 0 , 0);
                            oneAreaLayout.setLayoutParams(layoutParams);
                            Log.e("===>", "-------move: x: " + curMoveX + " dx: " + dx + " left: " + (begin_point + dx));

                            dx = leftMargins - begin_point;
                            View midView = areaInfo.getAreaView();
                            if(midView != null) {
                                int view_width =  (int) ((areaInfo.getOutPoint() - areaInfo.getInPoint()) * m_sqView.getPixelPerMicrosecond());
                                LinearLayout.LayoutParams midParams = (LinearLayout.LayoutParams) midView.getLayoutParams();
                                midParams.width = view_width - dx;
                                midView.setLayoutParams(midParams);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        long old_inpoint = areaInfo.getInPoint();
                        areaInfo.setInPoint(m_newInPoint);
                        SqAreaInfo newSqAreaInfo = areaInfo.clone();
                        m_areasInfo.remove(old_inpoint);
                        m_areasInfo.put(m_newInPoint, newSqAreaInfo);
                        areaInfo.setInPoint(m_newInPoint);

                        if(m_seekValueListener != null) {
                            m_seekValueListener.onLeftValueChange(m_newInPoint);
                        }

                        Log.e("===>", "-------up");
                        break;
                }
                return true;
            }
        });

        // 右把手
        HandImageView rightHand = new HandImageView(m_context);
        rightHand.setBackground(ContextCompat.getDrawable(m_context, R.mipmap.trimline));
        LayoutParams rightHandParams = new LayoutParams(m_handleWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        rightHand.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                LinearLayout oneAreaLayout = (LinearLayout) view.getParent();
                if(oneAreaLayout == null) {
                    return true;
                }
                SqAreaInfo areaInfo = (SqAreaInfo) oneAreaLayout.getTag();
                if(areaInfo == null) {
                    return true;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        m_lastX = (int) event.getRawX();
                        Log.e("===>", "-------down: x: " + m_lastX);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int curMoveX = (int)event.getRawX();
                        int dx = curMoveX - m_lastX;
                        Log.e("===>", "-------move: x: " + curMoveX + " dx: " + dx);

                        View midView = areaInfo.getAreaView();
                        long outLimit = getNextInPointByInPoint(areaInfo.getInPoint());
                        if(midView != null) {
                            int view_width =  (int) ((areaInfo.getOutPoint() - areaInfo.getInPoint()) * m_sqView.getPixelPerMicrosecond());
                            LinearLayout.LayoutParams midParams = (LinearLayout.LayoutParams) midView.getLayoutParams();
                            long dTime = (long) (dx / m_sqView.getPixelPerMicrosecond());
                            if(areaInfo.getOutPoint() + dTime - m_minDuration < areaInfo.getInPoint()) {
                                m_newOutPoint = areaInfo.getInPoint() + m_minDuration;
                                midParams.width = (int) (m_minDuration * m_sqView.getPixelPerMicrosecond());
                            } else if(areaInfo.getOutPoint() + dTime > outLimit) {
                                m_newOutPoint = outLimit;
                                midParams.width = (int) ((outLimit - areaInfo.getInPoint()) * m_sqView.getPixelPerMicrosecond());
                            } else {
                                m_newOutPoint = areaInfo.getOutPoint() + dTime;
                                midParams.width = view_width + dx;
                            }
                            midView.setLayoutParams(midParams);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        areaInfo.setOutPoint(m_newOutPoint);

                        if(m_seekValueListener != null) {
                            m_seekValueListener.onRightValueChange(m_newOutPoint);
                        }
                        Log.e("===>", "-------up");
                        break;
                }
                return true;
            }
        });

        LinearLayout oneAreaLayout = new LinearLayout(m_context);
        LayoutParams oneAreaParams = new LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        oneAreaParams.setMargins(begin_point, 0, 0 , 0);
        oneAreaLayout.addView(leftHand, leftHandParams);
        oneAreaLayout.addView(view, midParams);
        oneAreaLayout.addView(rightHand, rightHandParams);
        m_recordAreasLayout.addView(oneAreaLayout, oneAreaParams);
        oneAreaLayout.bringToFront();

        SqAreaInfo areaInfo = new SqAreaInfo();
        areaInfo.setInPoint(inPoint);
        areaInfo.setOutPoint(outPoint);
        areaInfo.setInPosition(begin_point);
        areaInfo.setAreaView(view);
        areaInfo.setLeftHandle(leftHand);
        areaInfo.setRightHandle(rightHand);
        oneAreaLayout.setTag(areaInfo);
        leftHand.setVisibility(INVISIBLE);
        rightHand.setVisibility(INVISIBLE);
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
                    m_recordAreasLayout.removeView((LinearLayout)areaInfo.getAreaView().getParent());
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
        LayoutParams layoutParams = (LayoutParams) areaInfo.getAreaView().getLayoutParams();
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

    public void selectAreaByInPoint(long inPoint) {
        for (Map.Entry<Long, SqAreaInfo> entry : m_areasInfo.entrySet()) {
            SqAreaInfo areaInfo = entry.getValue();
            if(areaInfo == null) {
                continue;
            }
            if(inPoint >= areaInfo.getInPoint() && inPoint <= areaInfo.getOutPoint()) {
                if(areaInfo.getLeftHandle() != null && areaInfo.getRightHandle() != null) {
                    m_recordAreasLayout.bringChildToFront((LinearLayout) areaInfo.getLeftHandle().getParent());
                    areaInfo.getLeftHandle().setVisibility(VISIBLE);
                    areaInfo.getRightHandle().setVisibility(VISIBLE);
                }
            } else {
                if(areaInfo.getLeftHandle() != null && areaInfo.getRightHandle() != null) {
                    areaInfo.getLeftHandle().setVisibility(INVISIBLE);
                    areaInfo.getRightHandle().setVisibility(INVISIBLE);
                }
            }
        }
    }

    private long getNextInPointByInPoint(long curInPoint) {
        List<SqAreaInfo> areaInfoList = new ArrayList<>(m_areasInfo.values());
        Collections.sort(areaInfoList, new Util.AreaInComparator()); // 升序
        for (SqAreaInfo areaInfo: areaInfoList) {
            if(areaInfo == null) {
                continue;
            }
            if(areaInfo.getInPoint() > curInPoint) {
                return areaInfo.getInPoint();
            }
        }
        return m_duration;
    }

    private long getLastOutPointByInPoint(long curInPoint) {
        List<SqAreaInfo> areaInfoList = new ArrayList<>(m_areasInfo.values());
        Collections.sort(areaInfoList, new Util.AreaOutComparator()); // 降序
        for (SqAreaInfo areaInfo: areaInfoList) {
            if(areaInfo == null) {
                continue;
            }
            if(areaInfo.getOutPoint() <= curInPoint) {
                return areaInfo.getOutPoint();
            }
        }
        return 0;
    }
}
