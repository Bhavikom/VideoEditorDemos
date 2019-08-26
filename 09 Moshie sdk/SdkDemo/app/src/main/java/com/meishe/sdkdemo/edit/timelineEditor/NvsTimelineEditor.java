package com.meishe.sdkdemo.edit.timelineEditor;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.Constants;

import java.util.ArrayList;

/**
 * Created by admin on 2018/6/8.
 * NvsTimelineEditor控件，封装NvsMultiThumbnailSequenceView与NvsTimelineTimeSpan。NvsMultiThumbnailSequenceView显示视频序列图片，
 * 可以在NvsMultiThumbnailSequenceView上添加多个NvsTimelineTimeSpan，从而方便编辑贴纸跟字幕，
 */
public class NvsTimelineEditor extends RelativeLayout {
    private String TAG = "NvsTimelineEditor";
    private static final float TIMEBASE = 1000000f;
    private double m_pixelPerMicrosecond = 0D;//每微秒显示的像素值
    private double m_maxPixelPerMicrosecond = 0D;//每微秒显示的最大像素值
    private double m_minPixelPerMicrosecond = 0D;//每微秒显示的最小像素值
    private Context m_context;
    private RelativeLayout m_timeSpanRelativeLayout;
    private LinearLayout m_sequenceLinearLayout;

    private OnScrollChangeListener m_scrollchangeListener;
    private ArrayList<NvsTimelineTimeSpan> m_timelineTimeSpanArray;//添加的TimeSpan集合
    private long m_timelineDuration = 0;//时间线timeline上的时长
    private NvsMultiThumbnailSequenceView m_multiThumbnailSequenceView;
    private int m_screenCentral = 0;

    private int m_sequencLeftPadding = 0;//m_multiThumbnailSequenceView左边填充值
    private int m_sequencRightPadding = 0;//m_multiThumbnailSequenceView右边填充值
    private int m_timeSpanLeftPadding = 0;//TimeSpan左边填充值

    private String m_timeSpanType = "NvsTimelineTimeSpan";//目前有两种timeSpan:NvsTimelineTimeSpan,NvsTimelineTimeSpanExt

    //m_multiThumbnailSequenceView滑动需要的回调API
    public interface OnScrollChangeListener {
        void onScrollX(long timeStamp);
    }

    public void setOnScrollListener(OnScrollChangeListener listener) {
        m_scrollchangeListener = listener;
    }


    public NvsTimelineEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_context = context;
        m_timeSpanRelativeLayout = new RelativeLayout(context);
        m_sequenceLinearLayout = new LinearLayout(context);
        m_sequenceLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        m_multiThumbnailSequenceView = new NvsMultiThumbnailSequenceView(context);
        init();
    }

    public void setPixelPerMicrosecond(double pixelPerMicrosecond) {
        this.m_pixelPerMicrosecond = pixelPerMicrosecond;
    }

    public void setSequencLeftPadding(int sequencLeftPadding) {
        this.m_sequencLeftPadding = sequencLeftPadding;
    }

    public void setSequencRightPadding(int sequencRightPadding) {
        this.m_sequencRightPadding = sequencRightPadding;
    }

    public void setTimeSpanLeftPadding(int timeSpanLeftPadding) {
        this.m_timeSpanLeftPadding = timeSpanLeftPadding;
    }

    //添加timeSpan之前必须设置这个参数
    public void setTimeSpanType(String timeSpanType) {
        this.m_timeSpanType = timeSpanType;
    }

    //初始化TimelineEditor
    public void initTimelineEditor(ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> sequenceDesc, final long timelineDuration) {
        int len = sequenceDesc.size();
        if (0 == len)
            return;
        m_timelineDuration = timelineDuration;
        //删除所有布局
        removeAllLayout();
        m_sequenceLinearLayout.scrollTo(0, 0);
        m_multiThumbnailSequenceView.setThumbnailSequenceDescArray(sequenceDesc);
        m_multiThumbnailSequenceView.setPixelPerMicrosecond(m_pixelPerMicrosecond);
        m_multiThumbnailSequenceView.setStartPadding(m_sequencLeftPadding);
        m_multiThumbnailSequenceView.setEndPadding(m_sequencRightPadding);
        RelativeLayout.LayoutParams sequenceViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        this.addView(m_multiThumbnailSequenceView, sequenceViewParams);
        m_multiThumbnailSequenceView.setOnScrollChangeListenser(new NvsMultiThumbnailSequenceView.OnScrollChangeListener() {

            @Override
            public void onScrollChanged(NvsMultiThumbnailSequenceView nvsMultiThumbnailSequenceView, int i, int i1) {
                if (m_timeSpanType.compareTo("NvsTimelineTimeSpanExt") == 0)
                    return;//不作处理
                long tmpTimeStamp = (long) Math.floor(i / m_pixelPerMicrosecond + 0.5D);
                ;
                if (m_scrollchangeListener != null) {
                    m_scrollchangeListener.onScrollX(tmpTimeStamp);
                }
                m_sequenceLinearLayout.scrollTo(i, 0);
                int timeSpanCount = m_timelineTimeSpanArray.size();
                for (int index = 0; index < timeSpanCount; ++index) {
                    boolean add = true;
                    NvsTimelineTimeSpan timeSpan = m_timelineTimeSpanArray.get(index);
                    long inPoint = timeSpan.getInPoint();
                    long outPoint = timeSpan.getOutPoint();
                    if (outPoint <= m_multiThumbnailSequenceView.mapTimelinePosFromX(0))
                        add = false;
                    if (inPoint >= m_multiThumbnailSequenceView.mapTimelinePosFromX(2 * m_screenCentral))
                        add = false;
                    if (add) {
                        if (timeSpan.getParent() == null) {
                            m_timeSpanRelativeLayout.addView(timeSpan);
                        }
                        //updateTimeSpanShadow(timeSpan);
                    } else {
                        if (timeSpan.getParent() != null) {
                            m_timeSpanRelativeLayout.removeView(timeSpan);
                        }
                    }
                }
            }
        });
    }

    //添加timeSpan
    public NvsTimelineTimeSpan addTimeSpan(long inPoint, long outPoint) {
        NvsTimelineTimeSpan timelineTimeSpan = createTimeSpan(inPoint, outPoint);
        if (timelineTimeSpan != null) {
            long minDuaration = 2 * Constants.NS_TIME_BASE;
            long maxDuaration = m_timelineDuration;
            int minTimeSpanPixel = (int) Math.floor(minDuaration * m_pixelPerMicrosecond + 0.5D);
            int maxTimeSpanPixel = (int) Math.floor(maxDuaration * m_pixelPerMicrosecond + 0.5D);
            timelineTimeSpan.setMinTimeSpanPixel(minTimeSpanPixel);
            timelineTimeSpan.setMaxTimeSpanPixel(maxTimeSpanPixel);
        }
        return timelineTimeSpan;
    }

    //添加douyin timeSpan
    public NvsTimelineTimeSpan addDouyinTimeSpan(long inPoint, long outPoint) {
        NvsTimelineTimeSpan timelineTimeSpan = createTimeSpan(inPoint, outPoint);
        if (timelineTimeSpan != null) {
            //
            timelineTimeSpan.getLeftHandleView().setBackgroundResource(R.mipmap.trimline_select_left);
            timelineTimeSpan.getRightHandleView().setBackgroundResource(R.mipmap.trimline_select_right);
            timelineTimeSpan.getTimeSpanshadowView().setBackgroundColor(Color.TRANSPARENT);
            //
            long minDuaration = 3 * Constants.NS_TIME_BASE;
            long maxDuaration = 15 * Constants.NS_TIME_BASE;
            if (maxDuaration > m_timelineDuration) {
                maxDuaration = m_timelineDuration;
            }
            int minTimeSpanPixel = (int) Math.floor(minDuaration * m_pixelPerMicrosecond + 0.5D);
            int maxTimeSpanPixel = (int) Math.floor(maxDuaration * m_pixelPerMicrosecond + 0.5D);
            timelineTimeSpan.setMinTimeSpanPixel(minTimeSpanPixel);
            timelineTimeSpan.setMaxTimeSpanPixel(maxTimeSpanPixel);

            //
            final int tmpTotalWidth = getSequenceWidth();
            ;
            double xLeft = inPoint * m_pixelPerMicrosecond;
            double width = (outPoint - inPoint) * m_pixelPerMicrosecond;
            int spanWidth = (int) Math.floor(width + 0.5D);
            int shadowMarginWidth = (int) Math.floor(xLeft + 0.5D);
            final View viewLeftMargin = new View(m_context);
            int backgrdColor = Color.parseColor("#bf000000");
            viewLeftMargin.setBackgroundColor(backgrdColor);
            RelativeLayout.LayoutParams viewLeftMarginParams = new RelativeLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            viewLeftMarginParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            viewLeftMarginParams.width = shadowMarginWidth;
            viewLeftMarginParams.setMargins(0, 0, tmpTotalWidth - shadowMarginWidth, 0);
            m_timeSpanRelativeLayout.addView(viewLeftMargin, viewLeftMarginParams);

            final View viewRightMargin = new View(m_context);
            viewRightMargin.setBackgroundColor(backgrdColor);
            RelativeLayout.LayoutParams viewRightMarginParams = new RelativeLayout.LayoutParams(tmpTotalWidth - (shadowMarginWidth + spanWidth), ViewGroup.LayoutParams.MATCH_PARENT);
            viewRightMarginParams.width = tmpTotalWidth - (shadowMarginWidth + spanWidth);
            viewRightMarginParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            viewRightMarginParams.setMargins(shadowMarginWidth + spanWidth, 0, 0, 0);
            m_timeSpanRelativeLayout.addView(viewRightMargin, viewRightMarginParams);

            timelineTimeSpan.setMarginChangeListener(new NvsTimelineTimeSpan.OnMarginChangeListener() {
                @Override
                public void onChange(int leftMargin, int timeSpanWidth) {
                    RelativeLayout.LayoutParams viewLeftMarginParams = (RelativeLayout.LayoutParams) viewLeftMargin.getLayoutParams();
                    viewLeftMarginParams.width = leftMargin;
                    viewLeftMarginParams.setMargins(0, 0, tmpTotalWidth - leftMargin, 0);
                    viewLeftMargin.setLayoutParams(viewLeftMarginParams);

                    RelativeLayout.LayoutParams viewRightMarginParams = (RelativeLayout.LayoutParams) viewRightMargin.getLayoutParams();
                    viewRightMarginParams.width = tmpTotalWidth - (timeSpanWidth + leftMargin);
                    viewRightMarginParams.setMargins(leftMargin + timeSpanWidth, 0, 0, 0);
                    viewRightMargin.setLayoutParams(viewRightMarginParams);
                }
            });
        }

        return timelineTimeSpan;
    }

    //添加扩展的timeSpan，可以微调时间值
    public NvsTimelineTimeSpanExt addTimeSpanExt(long inPoint, long outPoint) {
        if (inPoint < 0 || outPoint < 0 || inPoint >= outPoint)
            return null;
        if (m_timeSpanType.compareTo("NvsTimelineTimeSpanExt") != 0)
            return null;
        if(outPoint > m_timelineDuration)
            outPoint = m_timelineDuration;
        NvsTimelineTimeSpanExt timeSpanExt = new NvsTimelineTimeSpanExt(m_context);
        timeSpanExt.setMultiThumbnailSequenceView(m_multiThumbnailSequenceView);
        timeSpanExt.setInPoint(inPoint);
        timeSpanExt.setOutPoint(outPoint);
        timeSpanExt.setPixelPerMicrosecond(m_pixelPerMicrosecond);
        int lLeftHandleWidth = timeSpanExt.getLLeftHandleWidth();
        int totalWidth = getSequenceWidth() + 2 * lLeftHandleWidth;
        timeSpanExt.setTotalWidth(totalWidth);

        //
        addTimeSpanLayout(lLeftHandleWidth);
        //
        double xLeft = inPoint * m_pixelPerMicrosecond;
        double width = (outPoint - inPoint) * m_pixelPerMicrosecond;
        width += 2 * lLeftHandleWidth;
        int spanWidth = (int) Math.floor(width + 0.5D);
        int leftMargin = (int) Math.floor(xLeft + 0.5D);
        RelativeLayout.LayoutParams spanItemParams = new RelativeLayout.LayoutParams(spanWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
        spanItemParams.setMargins(leftMargin, RelativeLayout.LayoutParams.MATCH_PARENT, totalWidth - (leftMargin + spanWidth), 0);
        timeSpanExt.setLayoutParams(spanItemParams);

        final int tmpTotalWidth = totalWidth;
        final int tmpLLeftHandleWidth = lLeftHandleWidth;
        final View viewLeftMargin = new View(m_context);
        int backgrdColor = Color.parseColor("#bf000000");
        viewLeftMargin.setBackgroundColor(backgrdColor);
        int tmpMarginHeight = timeSpanExt.getLTopHandleHeight();
        RelativeLayout.LayoutParams viewLeftMarginParams = new RelativeLayout.LayoutParams(0, tmpMarginHeight);
        viewLeftMarginParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        viewLeftMarginParams.width = leftMargin;
        viewLeftMarginParams.setMargins(tmpLLeftHandleWidth, 0, tmpTotalWidth - tmpLLeftHandleWidth - leftMargin, 0);
        m_timeSpanRelativeLayout.addView(viewLeftMargin, viewLeftMarginParams);

        final View viewRightMargin = new View(m_context);
        viewRightMargin.setBackgroundColor(backgrdColor);
        RelativeLayout.LayoutParams viewRightMarginParams = new RelativeLayout.LayoutParams(tmpTotalWidth - (leftMargin + spanWidth), tmpMarginHeight);
        viewRightMarginParams.width = tmpTotalWidth - (leftMargin + spanWidth);
        viewRightMarginParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        viewRightMarginParams.setMargins(leftMargin + spanWidth - lLeftHandleWidth, 0, lLeftHandleWidth, 0);
        m_timeSpanRelativeLayout.addView(viewRightMargin, viewRightMarginParams);

        timeSpanExt.setMarginChangeListener(new NvsTimelineTimeSpanExt.OnMarginChangeListener() {
            @Override
            public void onChange(int leftMargin, int timeSpanWidth) {
                RelativeLayout.LayoutParams viewLeftMarginParams = (RelativeLayout.LayoutParams) viewLeftMargin.getLayoutParams();
                viewLeftMarginParams.width = leftMargin - tmpLLeftHandleWidth;
                viewLeftMarginParams.setMargins(tmpLLeftHandleWidth, 0, tmpTotalWidth - leftMargin, 0);
                viewLeftMargin.setLayoutParams(viewLeftMarginParams);

                RelativeLayout.LayoutParams viewRightMarginParams = (RelativeLayout.LayoutParams) viewRightMargin.getLayoutParams();
                viewRightMarginParams.width = tmpTotalWidth - (timeSpanWidth + leftMargin) - tmpLLeftHandleWidth;
                viewRightMarginParams.setMargins(leftMargin + timeSpanWidth, 0, tmpLLeftHandleWidth, 0);
                viewRightMargin.setLayoutParams(viewRightMarginParams);
            }
        });

        m_timeSpanRelativeLayout.addView(timeSpanExt);
        return timeSpanExt;
    }

    //放大MultiThumbnailSequence序列
    public void ZoomInSequence() {
        if (m_pixelPerMicrosecond > m_maxPixelPerMicrosecond)
            return;
        double scalFactor = 1.25;
        updateSequence(scalFactor);
    }

    //缩小MultiThumbnailSequence序列
    public void ZoomOutSequence() {
        if (m_pixelPerMicrosecond < m_minPixelPerMicrosecond)
            return;
        double scalFactor = 0.8;
        updateSequence(scalFactor);
    }

    private void updateSequence(double scaleFactor) {
        int m_curAnchor = m_sequencLeftPadding;
        m_multiThumbnailSequenceView.scaleWithAnchor(scaleFactor, m_curAnchor);
        m_pixelPerMicrosecond = m_multiThumbnailSequenceView.getPixelPerMicrosecond();

        // layoutTimeSpan ZoomIn and ZoomOut
        int totalWidth = getSequenceWidth();
        LinearLayout.LayoutParams timeSpanRelativeParams = (LinearLayout.LayoutParams) m_timeSpanRelativeLayout.getLayoutParams();
        if (timeSpanRelativeParams == null)
            return;

        timeSpanRelativeParams.width = totalWidth;
        m_timeSpanRelativeLayout.setLayoutParams(timeSpanRelativeParams);
        int count = m_timelineTimeSpanArray.size();
        for (int index = 0; index < count; ++index) {
            NvsTimelineTimeSpan timelineTimeSpan = m_timelineTimeSpanArray.get(index);
            double xLeft = timelineTimeSpan.getInPoint() * m_pixelPerMicrosecond;
            double width = (timelineTimeSpan.getOutPoint() - timelineTimeSpan.getInPoint()) * m_pixelPerMicrosecond;
            timelineTimeSpan.setTotalWidth(totalWidth);
            timelineTimeSpan.setPixelPerMicrosecond(m_pixelPerMicrosecond);
            int spanWidth = (int) Math.floor(width + 0.5D);
            int leftValue = (int) Math.floor(xLeft + 0.5D);

            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) timelineTimeSpan.getLayoutParams();
            relativeParams.width = spanWidth;
            relativeParams.setMargins(leftValue, 0, totalWidth - (leftValue + spanWidth), 0);
            timelineTimeSpan.setLayoutParams(relativeParams);
        }
    }

    private NvsTimelineTimeSpan createTimeSpan(long inPoint, long outPoint) {
        if (inPoint < 0 || outPoint < 0 || inPoint >= outPoint)
            return null;
        if (outPoint > m_timelineDuration)
            outPoint = m_timelineDuration;
        NvsTimelineTimeSpan timelineTimeSpan = new NvsTimelineTimeSpan(m_context);
        timelineTimeSpan.setInPoint(inPoint);
        timelineTimeSpan.setOutPoint(outPoint);
        timelineTimeSpan.setPixelPerMicrosecond(m_pixelPerMicrosecond);
        int totalWidth = getSequenceWidth();
        timelineTimeSpan.setTotalWidth(totalWidth);

        //
        addTimeSpanLayout(0);
        //
        double xLeft = inPoint * m_pixelPerMicrosecond;
        double width = (outPoint - inPoint) * m_pixelPerMicrosecond;
        int spanWidth = (int) Math.floor(width + 0.5D);
        int leftMargin = (int) Math.floor(xLeft + 0.5D);
        RelativeLayout.LayoutParams spanItemParams = new RelativeLayout.LayoutParams(spanWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
        spanItemParams.setMargins(leftMargin, RelativeLayout.LayoutParams.MATCH_PARENT, totalWidth - (leftMargin + spanWidth), 0);
        timelineTimeSpan.setLayoutParams(spanItemParams);

        boolean add = true;
        if (outPoint <= m_multiThumbnailSequenceView.mapTimelinePosFromX(0)) {
            add = false;
        }
        if (inPoint >= m_multiThumbnailSequenceView.mapTimelinePosFromX(2 * m_screenCentral)) {
            add = false;
        }

        if (add) {
            m_timeSpanRelativeLayout.addView(timelineTimeSpan);
            //updateTimeSpanShadow(mTimelineTimeSpan);
        }
        m_timelineTimeSpanArray.add(timelineTimeSpan);
        return timelineTimeSpan;
    }

    //通过用户选择timeSpan选中
    public void selectTimeSpan(NvsTimelineTimeSpan timeSpan) {
        unSelectAllTimeSpan();
        timeSpan.setHasSelected(true);
        timeSpan.requestLayout();
        timeSpan.bringToFront();
    }

    public int getSequenceWidth() {
        return (int) Math.floor(m_timelineDuration * m_pixelPerMicrosecond + 0.5D);
    }

    public NvsMultiThumbnailSequenceView getMultiThumbnailSequenceView() {
        return m_multiThumbnailSequenceView;
    }

    public int timeSpanCount() {
        return m_timeSpanRelativeLayout.getChildCount();
    }

    public void deleteSelectedTimeSpan(NvsTimelineTimeSpan timeSpan) {
        int count = m_timeSpanRelativeLayout.getChildCount();
        if (count > 0) {
            m_timeSpanRelativeLayout.removeView(timeSpan);
            m_timelineTimeSpanArray.remove(timeSpan);
        }
    }

    public void deleteAllTimeSpan() {
        int countTimeSpan = m_timeSpanRelativeLayout.getChildCount();
        if (countTimeSpan > 0) {
            m_timeSpanRelativeLayout.removeAllViews();
        }
        int arraySize = m_timelineTimeSpanArray.size();
        if (arraySize > 0) {
            m_timelineTimeSpanArray.clear();
        }
    }

    private void updateTimeSpanShadow(NvsTimelineTimeSpan timeSpan) {
        long inPoint = timeSpan.getInPoint();
        long outPoint = timeSpan.getOutPoint();
        long newIn = inPoint;
        long newOut = outPoint;
        long left = m_multiThumbnailSequenceView.mapTimelinePosFromX(0);
        if (inPoint < left) {
            newIn = left;
        }

        long right = m_multiThumbnailSequenceView.mapTimelinePosFromX(2 * m_screenCentral);
        if (outPoint > right) {
            newOut = right;
        }
        double xLeft = newIn * m_pixelPerMicrosecond;
        double width = (newOut - newIn) * m_pixelPerMicrosecond;
        int spanWidth = (int) Math.floor(width + 0.5D);
        int leftValue = (int) Math.floor(xLeft + 0.5D);
        int timeSpanShadowWidth = timeSpan.getLayoutParams().width;
        int subViewScrollValue = (int) Math.floor(inPoint * m_pixelPerMicrosecond + 0.5D);
        leftValue = leftValue - subViewScrollValue;
        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) timeSpan.getTimeSpanshadowView().getLayoutParams();
        relativeParams.width = spanWidth;
        relativeParams.setMargins(leftValue, RelativeLayout.LayoutParams.MATCH_PARENT, timeSpanShadowWidth - (leftValue + spanWidth), 0);
        timeSpan.getTimeSpanshadowView().setLayoutParams(relativeParams);
    }

    private void addTimeSpanLayout(int beyondHandleVal) {
        if (m_sequenceLinearLayout.getParent() != null) {
            return;
        }
        // start padding
        addPadding(m_timeSpanLeftPadding - beyondHandleVal);
        int totalWidth = getSequenceWidth() + 2 * beyondHandleVal;
        LinearLayout.LayoutParams timeSpanRelativeParams = new LinearLayout.LayoutParams(totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        m_sequenceLinearLayout.addView(m_timeSpanRelativeLayout, timeSpanRelativeParams);
        RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        this.addView(m_sequenceLinearLayout, itemParams);
    }

    public void unSelectAllTimeSpan() {
        for (int index = 0; index < m_timelineTimeSpanArray.size(); ++index) {
            NvsTimelineTimeSpan timeSpan = m_timelineTimeSpanArray.get(index);
            if (timeSpan.isHasSelected()) {
                timeSpan.setHasSelected(false);
                timeSpan.requestLayout();
            }
        }
    }

    private void removeAllLayout() {
        deleteAllTimeSpan();
        int countLinearChild = m_sequenceLinearLayout.getChildCount();
        if (countLinearChild > 0) {
            m_sequenceLinearLayout.removeAllViews();
        }
        int countEditorChild = this.getChildCount();
        if (countEditorChild > 0) {
            this.removeAllViews();
        }
    }

    private void init() {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        m_pixelPerMicrosecond = screenWidth / 20 / TIMEBASE;
        m_maxPixelPerMicrosecond = m_pixelPerMicrosecond * Math.pow(1.25, 5);//NvsMultiThumbnailSequenceView放大的m_pixelPerMicrosecond最大值
        m_minPixelPerMicrosecond = m_pixelPerMicrosecond * Math.pow(0.8, 5);//NvsMultiThumbnailSequenceView缩小的m_pixelPerMicrosecond最小值
        m_screenCentral = (int) Math.floor(screenWidth / 2 + 0.5D);
        m_timelineTimeSpanArray = new ArrayList<>();
    }

    private void addPadding(int padding) {
        LinearLayout.LayoutParams paddinParams = new LinearLayout.LayoutParams(padding, LinearLayout.LayoutParams.MATCH_PARENT);
        View view = new View(m_context);
        m_sequenceLinearLayout.addView(view, paddinParams);
    }
}
