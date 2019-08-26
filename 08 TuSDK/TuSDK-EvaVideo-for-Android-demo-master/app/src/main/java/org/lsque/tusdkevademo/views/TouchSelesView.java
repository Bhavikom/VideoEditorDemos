/**
 *  TuSDK
 *  TuSDKEVADemo
 *  TouchSelesView.java
 *  @author  H.ys
 *  @Date    2019/7/3 11:38
 *  @Copyright 	(c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import org.lasque.tusdk.core.seles.output.SelesSmartView;
import org.lasque.tusdk.core.seles.output.SelesView;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.view.TouchImageView;

/**
 * TuSDK
 * $desc$
 *
 * @author H.ys
 * @Date $data$ $time$
 * @Copyright (c) 2019 tusdk.com. All rights reserved.
 */
public class TouchSelesView extends SelesView {


    private static enum State { NONE, DRAG, ZOOM, FLING, ANIMATE_ZOOM }
    private State state;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private GestureDetector.OnDoubleTapListener doubleTapListener = null;

    //
    // Size of view and previous view size (ie before rotation)
    //
    private int viewWidth, viewHeight, prevViewWidth, prevViewHeight;


    public TouchSelesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TouchSelesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchSelesView(Context context) {
        super(context);
    }

    @Override
    protected void initView(Context context, AttributeSet attrs) {
        super.initView(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context, new GestureListener());
        setOnTouchListener(new PrivateOnTouchListener());
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleSeles(detector.getScaleFactor(),detector.getFocusX(),detector.getFocusY());
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            setState(State.ZOOM);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            setState(State.NONE);
        }
    }

    private void scaleSeles(float scaleFactor, float focusX, float focusY) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private class PrivateOnTouchListener implements OnTouchListener {

        //
        // Remember last point position for dragging
        //
        private PointF last = new PointF();

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mScaleDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);
            PointF curr = new PointF(event.getX(), event.getY());

            if (state == State.NONE || state == State.DRAG || state == State.FLING) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        last.set(curr);
                        setState(State.DRAG);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (state == State.DRAG) {
                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float top,left,right,bottom;
                            RectF currentRect = getDisplayRect();
                            top = currentRect.top;
                            bottom = currentRect.bottom;
                            if (top != 0 && bottom!=1){
                                top = top + (deltaY / getHeight());
                                top = Math.max(0f,top);
                                bottom = bottom - (deltaY / getHeight());
                                bottom = Math.min(1f,bottom);
                            }
                            left = currentRect.left;
                            right = currentRect.right;
                            if (left !=0f && right !=1){
                                left = left + (deltaX / getWidth());
                                left = Math.max(0f,left);
                                right = right + (deltaX / getWidth());
                                right = Math.min(1f,right);
                            }
                            last.set(curr.x, curr.y);
                            RectF dragRect = new RectF(left,top,right,bottom);
                            setDisplayRect(dragRect);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        setState(State.NONE);
                        break;
                }
            }
            return true;
        }
    }

    private void setState(State state) {
        this.state = state;
    }

}
