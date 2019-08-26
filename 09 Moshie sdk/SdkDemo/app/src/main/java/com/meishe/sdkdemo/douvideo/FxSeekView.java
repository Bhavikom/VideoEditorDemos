package com.meishe.sdkdemo.douvideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.douvideo.bean.FilterFxInfo;
import com.meishe.sdkdemo.edit.data.AssetItem;
import com.meishe.sdkdemo.utils.ScreenUtils;

import java.util.ArrayList;

public class FxSeekView extends View {
    private final int REVERSE_BKG = 0xb200abfc;
    private final int MINVALUE = 0;
    private final int MAXVALUE = 100;

    float m_firstValue = 0;
    float m_maxValue = 100;

    float m_secondValue = 70;
    boolean m_isTwoProgressIndicator = true;

    RectF m_rectf = new RectF();

    int bgColor = Color.TRANSPARENT;

    int controlRadius;

    int leftRightSpace;

    int topBottomSpace;
    float len;

    ArrayList<RectInfo> rectInfoList = new ArrayList<RectInfo>();
    boolean filterMode = false;
    int tlFxMode = DouVideoEditActivity.TIMELINE_FX_MODE_NONE;
    boolean addingFilter = false;
    float curStartValue = 0;
    int curFxColor = Color.TRANSPARENT;

    private Paint m_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF m_first_indicator_rectf;
    private RectF m_second_indicator_rectf;
    private Bitmap m_second_indicator_img_repeat = BitmapFactory.decodeResource(getResources(), R.mipmap.repeat_effect_handle);
    private Bitmap m_second_indicator_img_slow = BitmapFactory.decodeResource(getResources(), R.mipmap.slow_effect_handle);
    private int m_2_px = ScreenUtils.dip2px(getContext(), 2);
    private int m_3_px = ScreenUtils.dip2px(getContext(), 3);
    private int m_5_px = ScreenUtils.dip2px(getContext(), 3);
    private float curPos;
    private float m_width = 0;

    private ArrayList<AssetItem> mFilterDataInfoList;//滤镜特效数据信息

    private OnDataChangedListener OnDataChangedListener;

    public FxSeekView(Context context) {
        super(context);
    }

    public FxSeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFilterDataInfoList(ArrayList<AssetItem> filterDataInfoList) {
        this.mFilterDataInfoList = filterDataInfoList;
    }

    private void initViews() {
        dip5 = ScreenUtils.dip2px(getContext(), 5);
        leftRightSpace = dip5 * 3;
        topBottomSpace = dip5 * 2;
        len = getWidth() - 2 * leftRightSpace;
        m_paint.setStyle(Paint.Style.FILL);
        controlRadius = dip5 * 5;
        m_width = getWidth();
    }

    public void setFxMode(int fxMode) {
        filterMode = false;
        switch (fxMode) {
            case DouVideoEditActivity.TIMELINE_FX_MODE_NONE:
                m_isTwoProgressIndicator = false;
                bgColor = Color.TRANSPARENT;
                break;

            case DouVideoEditActivity.TIMELINE_FX_MODE_SLOW:
                m_isTwoProgressIndicator = true;
                bgColor = Color.TRANSPARENT;
                break;

            case DouVideoEditActivity.TIMELINE_FX_MODE_REPEAT:
                m_isTwoProgressIndicator = true;
                bgColor = Color.TRANSPARENT;
                break;

            case DouVideoEditActivity.TIMELINE_FX_MODE_REVERSE:
                m_isTwoProgressIndicator = false;
                bgColor = Color.parseColor("#bfbd10e0");
                break;
            default:
                break;
        }

        tlFxMode = fxMode;

        invalidate();
    }

    public void setFilterMode() {
        filterMode = true;
        m_isTwoProgressIndicator = false;
        bgColor = Color.TRANSPARENT;
        invalidate();
    }

    public void addingFilter(String fxName) {
        addingFilter = true;
        curFxColor = getRectColor(fxName);
        curStartValue = m_firstValue;
    }

    public void addingFilter(int fxColor) {
        addingFilter = true;
        curFxColor = fxColor;
        curStartValue = m_firstValue;
    }

    public boolean isAddingFilter() {
        return addingFilter;
    }

    public void endAddingFilter(float curTime) {
        addingFilter = false;
        RectInfo rectInfo = new RectInfo();
        rectInfo.color = curFxColor;
        float curX = getX(curStartValue);
        float x = getX(curTime);
        float left = curX;
        float right = x;
        if (tlFxMode == DouVideoEditActivity.TIMELINE_FX_MODE_REVERSE) {
            left = x;
            right = curX;
        }
        if(left < 0)
            left = 0;
        if(left > right)
            left = right;

        rectInfo.rect = new RectF(left, m_5_px, right, getHeight() - m_5_px);
        rectInfoList.add(rectInfo);
        invalidate();
    }

    public void removeLastFilter() {
        if (rectInfoList.isEmpty())
            return;
        rectInfoList.remove(rectInfoList.size()-1);
        invalidate();
    }

    public void endAddingFilter(ArrayList<FilterFxInfo> tlFilterInfos, long tlDuration) {
        addingFilter = false;
        rectInfoList.clear();
        for (int i=0; i<tlFilterInfos.size(); i++) {
            FilterFxInfo info = tlFilterInfos.get(i);
            RectInfo rectInfo = new RectInfo();
            rectInfo.color = getRectColor(info.getName());
            long inPoint = info.getInPoint();
            long outPoint = info.getOutPoint();
            float leftX = getX( inPoint*1f / tlDuration * 100);
            if(leftX < 0)
                leftX = 0;
            if(leftX > m_width)
                leftX = m_width;
            float rightX = getX( outPoint*1f / tlDuration * 100);
            if(rightX < leftX)
                rightX = leftX;
            if(rightX > m_width)
                rightX = m_width;

            rectInfo.rect = new RectF(leftX, m_5_px, rightX, getHeight() - m_5_px);
            rectInfoList.add(rectInfo);
        }
        invalidate();
    }

    private void reverseRect() {
        ArrayList<RectInfo> tmp = new ArrayList<RectInfo>();

        float width = getWidth();

        for (int i=0; i<rectInfoList.size(); i++) {
            RectInfo info = rectInfoList.get(i);
            RectInfo newInfo = new RectInfo();
            newInfo.color = info.color;

            float left = width - info.rect.right;
            float right = width - info.rect.left;
            newInfo.rect = new RectF(left, m_5_px, right, getHeight() - m_5_px);
            tmp.add(newInfo);
        }

        rectInfoList.clear();
        rectInfoList = tmp;
    }

    private int getRectColor(String fxName) {
        int count = mFilterDataInfoList.size();
        for (int index = 0;index < count;++index){
            AssetItem assetItem = mFilterDataInfoList.get(index);
            if (assetItem.getAsset().uuid.equals(fxName)) {//
                return Color.parseColor(assetItem.getFilterColor());
            }
        }
        return Color.TRANSPARENT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getWidth() == 0) {
            return;
        }
        //画背景
        m_paint.setColor(bgColor);
        m_rectf.left = 0;
        m_rectf.top = m_5_px;
        m_rectf.right = getWidth();
        m_rectf.bottom = getHeight()-m_5_px;
        canvas.drawRect(m_rectf, m_paint);

        firstPointX = getX(m_firstValue);
        float x = firstPointX;
        if(x < m_3_px) {
            x = m_3_px;
        }
        if(x > m_width - m_3_px) {
            x = m_width - m_3_px;
        }

        // 画滤镜
        if (filterMode) {
            drawRects(canvas);
            if (addingFilter) {
                float curX = getX(curStartValue);

                float left = curX;
                float right = x;
                if (tlFxMode == DouVideoEditActivity.TIMELINE_FX_MODE_REVERSE) {
                    left = x;
                    right = curX;
                }
                if(left < 0)
                    left = 0;
                if(left > right)
                    left = right;

                RectF curRec = new RectF(left, m_5_px, right, getHeight() - m_5_px);
                m_paint.setColor(curFxColor);
                canvas.drawRect(curRec, m_paint);
            }
        }

        //画进度条
        m_paint.setColor(ContextCompat.getColor(getContext(), R.color.ff4a90e2));
        m_first_indicator_rectf = new RectF(x - m_3_px, 0, x  + m_3_px, getHeight());
        canvas.drawRoundRect(m_first_indicator_rectf, m_2_px, m_2_px, m_paint);

        if (m_isTwoProgressIndicator) {
            secondPointX = getX(m_secondValue);
            x = secondPointX;
            if(x < (m_second_indicator_img_repeat.getWidth() / 2)) {
                x = (m_second_indicator_img_repeat.getWidth() / 2);
            }
            if(x > m_width - (m_second_indicator_img_repeat.getWidth() / 2)) {
                x = m_width - (m_second_indicator_img_repeat.getWidth() / 2);
            }

            m_second_indicator_rectf = new RectF(x - m_second_indicator_img_repeat.getWidth() / 2, 0,
                    x + m_second_indicator_img_repeat.getWidth() / 2, getHeight());
            if(tlFxMode == DouVideoEditActivity.TIMELINE_FX_MODE_SLOW){
                canvas.drawBitmap(m_second_indicator_img_slow, null, m_second_indicator_rectf, m_paint);
            }else if(tlFxMode == DouVideoEditActivity.TIMELINE_FX_MODE_REPEAT){
                canvas.drawBitmap(m_second_indicator_img_repeat, null, m_second_indicator_rectf, m_paint);
            }

        }
    }

    private void drawRects(Canvas canvas) {
        for (int i = 0; i < rectInfoList.size(); i++) {
            RectInfo rectInfo = rectInfoList.get(i);
            m_paint.setColor(rectInfo.color);
            canvas.drawRect(rectInfo.rect, m_paint);

        }
    }

    private float getX(float value) {
        return value * m_width * 1.0f / 100f;
    }


    int dip5;

    float firstPointX, secondPointX;


    private boolean isInArea(float lastPosition, float x) {
        return x < lastPosition + controlRadius && lastPosition - controlRadius < x;
    }

    boolean isMovedFirst = false;
    boolean isMovedSecond = false;

    public void setFirstValue(float firstValue) {
        m_firstValue = firstValue;
        curPos = getX(firstValue);
        invalidate();
    }

    public void setSecondValue(float secondValue) {
        m_secondValue = secondValue;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(OnDataChangedListener == null)
            return false;
        curPos = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (m_isTwoProgressIndicator && isInArea(secondPointX, event.getX())) {
                    isMovedSecond = true;
                } else {
                    if (isInArea(firstPointX, event.getX())) {
                        isMovedFirst = true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float newValue = m_maxValue * getRatio(event.getX());
                if(newValue < MINVALUE)
                    newValue = MINVALUE;

                if(newValue > MAXVALUE)
                    newValue = MAXVALUE;
                if (isMovedFirst) {
                    OnDataChangedListener.onFirstDataChange(newValue);
                    setFirstValue(newValue);
                    invalidate();
                } else {
                    if (isMovedSecond) {
                        setSecondValue(newValue);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                float value = m_maxValue * getRatio(event.getX());
                if(value < MINVALUE)
                    value = MINVALUE;
                if (isMovedSecond) {
                    OnDataChangedListener.onSecondDataChange(value);
                    invalidate();
                }
                isMovedSecond = false;
                isMovedFirst = false;
                break;
        }
        return true;
    }

    private float getRatio(float x) {
        float f = x / getWidth();
        return f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViews();
    }

    public void setOndataChanged(OnDataChangedListener listener) {
        this.OnDataChangedListener = listener;
    }

    private class RectInfo {
        RectF rect = new RectF(0, ScreenUtils.dip2px(getContext(), 4), 0, ScreenUtils.dip2px(getContext(), 28));
        int color;
    }

    public interface OnDataChangedListener {

        void onFirstDataChange(float var);

        void onSecondDataChange(float var);
    }
}

