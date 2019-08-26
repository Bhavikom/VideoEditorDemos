package com.meishe.sdkdemo.edit.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.ScreenUtils;

/**
 * Created by czl on 2018/8/6.
 */
public class CustomStickerDrawRect extends View {
    private RectF mViewRectF = new RectF();
    private RectF scaleRectF = new RectF();
    private Paint mRectPaint = new Paint();
    private Context mContext;
    private int mViewMode = 0;
    private Bitmap scaleImgBtn;
    private int mScaleImgBtnWidth;
    private int mScaleImgBtnHeight;
    private int mImgWidth;
    private int mImgHeight;

    private float prevRawX = 0;
    private float prevRawY = 0;
    private OnDrawRectListener mDrawRectListener;
    private int mMinPixelWidthValue = 200; //最小宽度值
    private int mMinPixelHeightValue = 200; //最小高度值
    private boolean canScaleRect = false;
    private boolean canMoveRect = false;

    public void setImgSize(int imgWidth,int imgHeight) {
        this.mImgWidth = imgWidth;
        this.mImgHeight = imgHeight;
    }

    public CustomStickerDrawRect(Context context) {
        this(context, null);
    }

    public CustomStickerDrawRect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        scaleImgBtn = BitmapFactory.decodeResource(getResources(), R.mipmap.custom_sticker_scale);
        mScaleImgBtnWidth = scaleImgBtn.getWidth();
        mScaleImgBtnHeight = scaleImgBtn.getHeight();
        initRectPaint();
    }
    public int getScaleImgBtnWidth() {
        return mScaleImgBtnWidth;
    }
    public int getScaleImgBtnHeight() {
        return mScaleImgBtnHeight;
    }
    public void setDrawRect(RectF rectF,int mode) {
        mViewRectF = rectF;
        mViewMode = mode;
        invalidate();
    }

    public void setOnDrawRectListener(OnDrawRectListener drawRectListener) {
        mDrawRectListener = drawRectListener;
    }

    private void initRectPaint(){
        // 设置颜色
        mRectPaint.setColor(Color.parseColor("#D0021B"));
        // 设置抗锯齿
        mRectPaint.setAntiAlias(true);
        // 设置线宽
        mRectPaint.setStrokeWidth(ScreenUtils.dip2px(mContext,3));
        // 设置非填充
        mRectPaint.setStyle(Paint.Style.STROKE);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mViewMode == Constants.CUSTOMSTICKER_EDIT_FREE_MODE
                ||mViewMode == Constants.CUSTOMSTICKER_EDIT_SQUARE_MODE){//自由模式或者正方模式
            // 画矩形框
            canvas.drawRect(mViewRectF, mRectPaint);
            // 画放缩按钮
            canvas.drawBitmap(scaleImgBtn,
                    mViewRectF.right - mScaleImgBtnWidth/2,
                    mViewRectF.bottom - mScaleImgBtnHeight/2 ,
                    mRectPaint);
            scaleRectF.set(mViewRectF.right - mScaleImgBtnWidth/2,
                    mViewRectF.bottom - mScaleImgBtnHeight/2,
                    mViewRectF.right + mScaleImgBtnWidth/2,
                    mViewRectF.bottom + mScaleImgBtnHeight/2);
        }else if(mViewMode == Constants.CUSTOMSTICKER_EDIT_CIRCLE_MODE){//圆形模式
            //handle heres
            float cx = (mViewRectF.right - mViewRectF.left)/2;
            float cy = (mViewRectF.bottom - mViewRectF.top)/2;
            float radius = cx >= cy ? cy : cx;
            float newCenterX = mViewRectF.left + cx;
            float newCenterY = mViewRectF.top + cy;
            canvas.drawCircle(newCenterX,newCenterY,radius,mRectPaint);
            float scaleCx = newCenterX + (float) (radius * Math.cos(2 * Math.PI * 45 / 360.0D));
            float scaleCy = newCenterY + (float) (radius * Math.sin(2 * Math.PI * 45 / 360.0D));
            // 画放缩按钮
            canvas.drawBitmap(scaleImgBtn,
                    scaleCx - mScaleImgBtnWidth/2,
                    scaleCy - mScaleImgBtnHeight/2 ,
                    mRectPaint);
            scaleRectF.set(scaleCx - mScaleImgBtnWidth/2,
                    scaleCy - mScaleImgBtnHeight/2,
                    scaleCx + mScaleImgBtnWidth/2,
                    scaleCy + mScaleImgBtnHeight/2);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                canScaleRect = scaleRectF.contains(event.getX(),event.getY());
                canMoveRect = mViewRectF.contains(event.getX(),event.getY());
                prevRawX = event.getRawX();
                prevRawY = event.getRawY();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                float tempRawX = event.getRawX();
                float tempRawY = event.getRawY();
                int dx = (int) Math.floor(tempRawX - prevRawX + 0.5D);
                int dy = (int) Math.floor(tempRawY - prevRawY + 0.5D);
                prevRawX = tempRawX;
                prevRawY = tempRawY;
                if(mViewMode == Constants.CUSTOMSTICKER_EDIT_FREE_MODE){
                    if(canScaleRect){
                        scaleRec(dx,dy);
                    }else {
                        if(canMoveRect){
                            moveView(dx,dy);
                        }
                    }
                }else if(mViewMode == Constants.CUSTOMSTICKER_EDIT_CIRCLE_MODE
                        || mViewMode == Constants.CUSTOMSTICKER_EDIT_SQUARE_MODE){
                    //handle here
                    if(canScaleRect){
                        if(dx > dy) {//等比例位移
                            dx = dy;
                        } else {
                            dy = dx;
                        }
                        scaleRec(dx,dy);
                    }else {
                        if(canMoveRect){
                            moveView(dx,dy);
                        }
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP:{
                canScaleRect = false;
                canMoveRect = false;
                if(mDrawRectListener != null){
                    mDrawRectListener.onDrawRect(new RectF(mViewRectF));
                }
                break;
            }
        }
        invalidate();
        return true;
    }

    private void moveView(int dx,int dy){
        float drawRectWidth = mViewRectF.right - mViewRectF.left;
        float drawRectHeight = mViewRectF.bottom - mViewRectF.top;
        mViewRectF.left += dx;
        mViewRectF.right += dx;
        mViewRectF.top += dy;
        mViewRectF.bottom += dy;

        if (mViewRectF.left <= 0) {
            mViewRectF.left = 0;
            mViewRectF.right = mViewRectF.left + drawRectWidth;
        }
        if (mViewRectF.right >= mImgWidth) {
            mViewRectF.right = mImgWidth;
            mViewRectF.left = mImgWidth - drawRectWidth;
        }
        if (mViewRectF.top <= 0) {
            mViewRectF.top = 0;
            mViewRectF.bottom = mViewRectF.top + drawRectHeight;
        }
        if (mViewRectF.bottom >= mImgHeight) {
            mViewRectF.bottom = mImgHeight;
            mViewRectF.top = mImgHeight - drawRectHeight;
        }
    }

    private void scaleRec(int dx,int dy){
        mViewRectF.right += dx;
        mViewRectF.bottom += dy;

        if (mViewRectF.right >= mImgWidth) {
            mViewRectF.right = mImgWidth;
        }

        if (mViewRectF.bottom >= mImgHeight) {
            mViewRectF.bottom = mImgHeight;
        }
        if (mViewRectF.right - mViewRectF.left <= mMinPixelWidthValue) {
            mViewRectF.right = mViewRectF.left + mMinPixelWidthValue;
        }
        if (mViewRectF.bottom - mViewRectF.top <= mMinPixelHeightValue) {
            mViewRectF.bottom = mViewRectF.top + mMinPixelHeightValue;
        }
    }

    public interface OnDrawRectListener {
        void onDrawRect(RectF rectF);
    }
}
