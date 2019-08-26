package com.meishe.sdkdemo.edit.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.ImageConverter;

import java.util.List;

import static com.meishe.sdkdemo.utils.Constants.HANDCLICK_DURATION;
import static com.meishe.sdkdemo.utils.Constants.HANDMOVE_DISTANCE;

/**
 * Created by Administrator on 2018/6/20.
 */

public class DrawRect extends View {
    private final static String TAG = "DrawRect";
    private OnTouchListener mListener;
    private onDrawRectClickListener mDrawRectClickListener;
    private onStickerMuteListenser mStickerMuteListenser;
    private PointF prePointF = new PointF(0, 0);
    private RectF alignRectF = new RectF();
    private RectF horizFlipRectF = new RectF();
    private RectF rotationRectF = new RectF();
    private RectF deleteRectF = new RectF();
    private RectF muteRectF = new RectF();
    private List<PointF> mListPointF;
    private List<List<PointF>> mSubListPointF;
    private Path rectPath = new Path();
    private boolean canScalOrRotate = false;
    private boolean canHorizFlipClick = false;
    private boolean canMuteClick = false;
    private boolean isInnerDrawRect = false;
    private boolean canDel = false;
    private boolean canAlignClick = false;
    private int mIndex = 0;
    private int viewMode = 0;
    private int mStickerMuteIndex = 0;
    private boolean mHasAudio = false;
    private Bitmap rotationImgBtn = BitmapFactory.decodeResource(getResources(), R.mipmap.scale);
    private Bitmap alignImgArray[] = {BitmapFactory.decodeResource(getResources(), R.mipmap.left_align), BitmapFactory.decodeResource(getResources(), R.mipmap.center_align), BitmapFactory.decodeResource(getResources(), R.mipmap.right_align)};
    private Bitmap deleteImgBtn = BitmapFactory.decodeResource(getResources(), R.mipmap.delete);
    private Bitmap horizontalFlipImgBtn = BitmapFactory.decodeResource(getResources(), R.mipmap.horizontal_flip);
    private Bitmap muteImgArray[] = {BitmapFactory.decodeResource(getResources(), R.mipmap.stickerunmute), BitmapFactory.decodeResource(getResources(), R.mipmap.stickermute)};
    private long mPrevMillionSecond = 0;
    private double mClickMoveDistance = 0.0D;
    private Paint mRectPaint = new Paint();
    private Paint mSubRectPaint = new Paint();
    private boolean mMoveOutScreen = false;
    //在方框内绘制的图片路径
    private String filePath;
    private Bitmap waterMarkBitmap;

    private int subCaptionIndex = -1;
    public DrawRect(Context context) {
        this(context, null);
    }

    public DrawRect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRectPaint();
        initSubRectPaint();
    }

    public void cleanUp(){
        bitmapRecycle(rotationImgBtn);
        rotationImgBtn = null;

        int alignImgCount = alignImgArray.length;
        for (int idx = 0;idx < alignImgCount;idx++){
            bitmapRecycle(alignImgArray[idx]);
            alignImgArray[idx] = null;
        }

        bitmapRecycle(deleteImgBtn);
        deleteImgBtn = null;

        bitmapRecycle(horizontalFlipImgBtn);
        horizontalFlipImgBtn = null;

        int muteImgCount = muteImgArray.length;
        for (int idx = 0;idx < muteImgCount;idx++){
            bitmapRecycle(muteImgArray[idx]);
            muteImgArray[idx] = null;
        }

        bitmapRecycle(waterMarkBitmap);
        waterMarkBitmap = null;
    }

    private void bitmapRecycle(Bitmap bitmap){
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
        }
    }

    private void setRectPath(List<PointF> listPointF){
        rectPath.reset();
        rectPath.moveTo(listPointF.get(0).x, listPointF.get(0).y);
        rectPath.lineTo(listPointF.get(1).x, listPointF.get(1).y);
        rectPath.lineTo(listPointF.get(2).x, listPointF.get(2).y);
        rectPath.lineTo(listPointF.get(3).x, listPointF.get(3).y);
        rectPath.close();
    }

    private void initRectPaint() {
        // 设置颜色
        mRectPaint.setColor(Color.parseColor("#4A90E2"));
        // 设置抗锯齿
        mRectPaint.setAntiAlias(true);
        // 设置线宽
        mRectPaint.setStrokeWidth(8);
        // 设置非填充
        mRectPaint.setStyle(Paint.Style.STROKE);
    }

    private void initSubRectPaint() {
        int dashWidth = 4;
        int dashGap = 2;
        // 设置颜色
        mSubRectPaint.setColor(Color.parseColor("#9B9B9B"));
        // 设置抗锯齿
        mSubRectPaint.setAntiAlias(true);
        // 设置线宽
        mSubRectPaint.setStrokeWidth(dashWidth);
        // 设置非填充
        mSubRectPaint.setStyle(Paint.Style.STROKE);
        //设置虚线效果
        mSubRectPaint.setPathEffect(new DashPathEffect(new float[] { dashWidth,dashGap }, 0));
    }

    private int getSubCaptionIndex(int xPos,int yPos ){
        if(mSubListPointF == null){
            return -1;
        }
        int subCount = mSubListPointF.size();
        for (int idx = 0;idx < subCount;idx++){
            List<PointF> pointList = mSubListPointF.get(idx);
            boolean isContain = clickPointIsInnerDrawRect(pointList,xPos,yPos);
            if(isContain){
                return idx;
            }
        }
        return -1;
    }
    public void setAlignIndex(int index) {
        mIndex = index;
        invalidate();
    }

    public void setStickerMuteIndex(int index) {
        mStickerMuteIndex = index;
        invalidate();
    }

    public void setMuteVisible(boolean hasAudio) {
        mHasAudio = hasAudio;
        invalidate();
    }

    public void setDrawRect(List<PointF> list, int mode) {
        mListPointF = list;
        viewMode = mode;
        invalidate();
    }

    public void setCompoundDrawRect(List<PointF> list,List<List<PointF>> subList, int mode) {
        mListPointF = list;
        mSubListPointF = subList;
        viewMode = mode;
        invalidate();
    }

    public List<PointF> getDrawRect() {
        return mListPointF;
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mListener = listener;
    }

    public void setDrawRectClickListener(onDrawRectClickListener drawRectClickListener) {
        this.mDrawRectClickListener = drawRectClickListener;
    }

    public void setStickerMuteListenser(onStickerMuteListenser stickerMuteListenser) {
        this.mStickerMuteListenser = stickerMuteListenser;
    }

    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mListPointF == null || mListPointF.size() != 4) {
            return;
        }
        setRectPath(mListPointF);
        canvas.drawPath(rectPath,mRectPaint);

        if (viewMode == Constants.EDIT_MODE_CAPTION) {//绘制字幕对其按钮
            canvas.drawBitmap(alignImgArray[mIndex], mListPointF.get(0).x - alignImgArray[mIndex].getHeight() / 2, mListPointF.get(0).y - alignImgArray[mIndex].getWidth() / 2, mRectPaint);
            alignRectF.set(mListPointF.get(0).x - alignImgArray[mIndex].getWidth() / 2, mListPointF.get(0).y - alignImgArray[mIndex].getHeight() / 2, mListPointF.get(0).x + alignImgArray[mIndex].getWidth() / 2, mListPointF.get(0).y + alignImgArray[mIndex].getWidth() / 2);
        } else if (viewMode == Constants.EDIT_MODE_STICKER) {//绘制水平翻转按钮
            canvas.drawBitmap(horizontalFlipImgBtn, mListPointF.get(0).x - horizontalFlipImgBtn.getHeight() / 2, mListPointF.get(0).y - horizontalFlipImgBtn.getWidth() / 2, mRectPaint);
            horizFlipRectF.set(mListPointF.get(0).x - horizontalFlipImgBtn.getWidth() / 2, mListPointF.get(0).y - horizontalFlipImgBtn.getHeight() / 2, mListPointF.get(0).x + horizontalFlipImgBtn.getWidth() / 2, mListPointF.get(0).y + horizontalFlipImgBtn.getHeight() / 2);
            if (mHasAudio) {
                canvas.drawBitmap(muteImgArray[mStickerMuteIndex], mListPointF.get(1).x - muteImgArray[mStickerMuteIndex].getHeight() / 2, mListPointF.get(1).y - muteImgArray[mStickerMuteIndex].getWidth() / 2, mRectPaint);
                muteRectF.set(mListPointF.get(1).x - muteImgArray[mStickerMuteIndex].getWidth() / 2, mListPointF.get(1).y - muteImgArray[mStickerMuteIndex].getHeight() / 2, mListPointF.get(1).x + muteImgArray[mStickerMuteIndex].getWidth() / 2, mListPointF.get(1).y + muteImgArray[mStickerMuteIndex].getHeight() / 2);
            } else {
                muteRectF.set(0, 0, 0, 0);
            }
        } else if (viewMode == Constants.EDIT_MODE_WATERMARK) {
            if (waterMarkBitmap != null) {
                canvas.drawBitmap(waterMarkBitmap, new Rect(0, 0, waterMarkBitmap.getWidth(), waterMarkBitmap.getHeight()), new RectF(mListPointF.get(0).x, mListPointF.get(0).y, mListPointF.get(2).x, mListPointF.get(2).y), null);
            }
        }else if(viewMode == Constants.EDIT_MODE_COMPOUND_CAPTION){
            //TODO
            if (mSubListPointF != null) {
                int subCount = mSubListPointF.size();
                for (int idx = 0;idx < subCount;idx++){
                    List<PointF> listPointF = mSubListPointF.get(idx);
                    if(listPointF == null || listPointF.size() != 4){
                        continue;
                    }
                    setRectPath(listPointF);
                    canvas.drawPath(rectPath,mSubRectPaint);
                }
            }
        }

        if(viewMode == Constants.EDIT_MODE_THEMECAPTION)
            return;//主题字幕，不绘制编辑按钮

        //绘制删除按钮
        canvas.drawBitmap(deleteImgBtn, mListPointF.get(3).x - deleteImgBtn.getWidth() / 2, mListPointF.get(3).y - deleteImgBtn.getHeight() / 2, mRectPaint);
        deleteRectF.set(mListPointF.get(3).x - deleteImgBtn.getWidth() / 2, mListPointF.get(3).y - deleteImgBtn.getHeight() / 2, mListPointF.get(3).x + deleteImgBtn.getWidth() / 2, mListPointF.get(3).y + deleteImgBtn.getHeight() / 2);

        // 绘制旋转放缩按钮
        canvas.drawBitmap(rotationImgBtn, mListPointF.get(2).x - rotationImgBtn.getHeight() / 2, mListPointF.get(2).y - rotationImgBtn.getWidth() / 2, mRectPaint);
        rotationRectF.set(mListPointF.get(2).x - rotationImgBtn.getWidth() / 2, mListPointF.get(2).y - rotationImgBtn.getHeight() / 2, mListPointF.get(2).x + rotationImgBtn.getWidth() / 2, mListPointF.get(2).y + rotationImgBtn.getHeight() / 2);
    }


    public boolean curPointIsInnerDrawRect(int xPos, int yPos) {
        // 判断手指是否在字幕框内
        return clickPointIsInnerDrawRect(mListPointF,xPos,yPos);
    }

    public boolean clickPointIsInnerDrawRect(List<PointF> pointFList,int xPos, int yPos){
        if(pointFList == null || pointFList.size() != 4){
            return false;
        }
        // 判断手指是否在编辑框内
        RectF r = new RectF();
        Path path = new Path();
        path.moveTo(pointFList.get(0).x, pointFList.get(0).y);
        path.lineTo(pointFList.get(1).x, pointFList.get(1).y);
        path.lineTo(pointFList.get(2).x, pointFList.get(2).y);
        path.lineTo(pointFList.get(3).x, pointFList.get(3).y);
        path.close();
        path.computeBounds(r, true);
        Region region = new Region();
        region.setPath(path, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
        return region.contains(xPos, yPos);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float targetX = event.getX();
        float targetY = event.getY();
        if (mListPointF != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    mPrevMillionSecond = System.currentTimeMillis();
                    canScalOrRotate = rotationRectF.contains(targetX, targetY);
                    canDel = deleteRectF.contains(targetX, targetY);
                    if (viewMode == Constants.EDIT_MODE_CAPTION) {
                        canAlignClick = alignRectF.contains(targetX, targetY);
                    } else if (viewMode == Constants.EDIT_MODE_STICKER) {
                        canHorizFlipClick = horizFlipRectF.contains(targetX, targetY);
                        canMuteClick = muteRectF.contains(targetX, targetY);
                    }

                    if (mListener != null) {
                        mListener.onTouchDown(new PointF(targetX, targetY));
                    }

                    if (mListPointF != null && mListPointF.size() == 4) {
                        // 判断手指是否在字幕框内
                        isInnerDrawRect = curPointIsInnerDrawRect((int) targetX, (int) targetY);
                    }
                    if(isInnerDrawRect){
                        subCaptionIndex = getSubCaptionIndex((int) targetX, (int) targetY);
                    }
                    prePointF.set(targetX,targetY);
                    break;
                }

                case MotionEvent.ACTION_UP: {
                    //作个缩放操作选中判断，优先执行缩放操作，再执行删除操作。因为操作框很小的时候，会出现操作混乱的现象（本身做缩放操作，却出现删除操作）
                    if (!canScalOrRotate && canDel && mListener != null){
                        //删除时，不作其他操作
                        isInnerDrawRect = false;;
                        mListener.onDel();
                    }

                    if (viewMode == Constants.EDIT_MODE_CAPTION) {
                        if (canAlignClick && mListener != null){
                            isInnerDrawRect = false;
                            mListener.onAlignClick();
                        }
                    } else if (viewMode == Constants.EDIT_MODE_STICKER) {
                        if (canHorizFlipClick && mListener != null)
                            mListener.onHorizFlipClick();
                        if (canMuteClick && mStickerMuteListenser != null)
                            mStickerMuteListenser.onStickerMute();
                    }

                    long moveTime_up = System.currentTimeMillis() - mPrevMillionSecond;
                    if (mClickMoveDistance < HANDMOVE_DISTANCE && moveTime_up <= HANDCLICK_DURATION) {
                        if (viewMode == Constants.EDIT_MODE_CAPTION) {
                            if (isInnerDrawRect && (!(canScalOrRotate || canDel || canAlignClick))) {
                                if (mDrawRectClickListener != null)
                                    mDrawRectClickListener.onDrawRectClick(0);//点击操作框
                            } else if (!(canScalOrRotate || canDel || canAlignClick)) {
                                if (mListener != null)
                                    mListener.onBeyondDrawRectClick();//点击操作框以外的区域
                            }
                        } else if (viewMode == Constants.EDIT_MODE_STICKER) {
                            if (!isInnerDrawRect && !(canScalOrRotate || canDel || canHorizFlipClick || canMuteClick)) {
                                if (mListener != null)
                                    mListener.onBeyondDrawRectClick();//点击操作框以外的区域
                            }
                        }else if(viewMode == Constants.EDIT_MODE_THEMECAPTION){
                            if (!isInnerDrawRect) {
                                if (mListener != null)
                                    mListener.onBeyondDrawRectClick();//点击操作框以外的区域
                            }
                        }else if(viewMode == Constants.EDIT_MODE_WATERMARK){
                            if (!isInnerDrawRect) {
                                if (mListener != null)
                                    mListener.onBeyondDrawRectClick();//点击操作框以外的区域
                            }
                        }else if (viewMode == Constants.EDIT_MODE_COMPOUND_CAPTION){//组合字幕操作
                            //TODO
                            if (isInnerDrawRect && (!(canScalOrRotate || canDel))) {
                                if (mDrawRectClickListener != null)
                                    mDrawRectClickListener.onDrawRectClick(subCaptionIndex);//点击操作框
                            } else if (!(canScalOrRotate || canDel || canAlignClick)) {
                                if (mListener != null)
                                    mListener.onBeyondDrawRectClick();//点击操作框以外的区域
                            }
                        }
                    }

                    canDel = false;
                    canScalOrRotate = false;
                    isInnerDrawRect = false;

                    canAlignClick = false;
                    canHorizFlipClick = false;
                    canMuteClick = false;
                    mClickMoveDistance = 0.0D;
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    mClickMoveDistance = Math.sqrt(Math.pow(targetX - prePointF.x, 2) + Math.pow(targetY - prePointF.y, 2));

                    // 防止移出屏幕
                    if(targetX <= 100 || targetX >= getWidth()  || targetY >= getHeight() || targetY <= 20 ){
                        mMoveOutScreen = true;
                        break;
                    }
                    if(mMoveOutScreen){
                        mMoveOutScreen = false;
                        break;
                    }

                    // 计算字幕框中心点
                    PointF centerPointF = new PointF();
                    if (mListPointF != null && mListPointF.size() == 4) {
                        centerPointF.x = (mListPointF.get(0).x + mListPointF.get(2).x) / 2;
                        centerPointF.y = (mListPointF.get(0).y + mListPointF.get(2).y) / 2;
                    }

                    if (mListener != null && canScalOrRotate) {
                        isInnerDrawRect = false;
                        // 计算手指在屏幕上滑动的距离比例
                        double temp = Math.pow(prePointF.x - centerPointF.x, 2) + Math.pow(prePointF.y - centerPointF.y, 2);
                        double preLength = Math.sqrt(temp);
                        double temp2 = Math.pow(targetX - centerPointF.x, 2) + Math.pow(targetY - centerPointF.y, 2);
                        double length = Math.sqrt(temp2);
                        float offset = (float) (length / preLength);

                        // 计算手指滑动的角度
                        float radian = (float) (Math.atan2(targetY - centerPointF.y, targetX - centerPointF.x)
                                - Math.atan2(prePointF.y - centerPointF.y, prePointF.x - centerPointF.x));
                        // 弧度转换为角度
                        float angle = (float) (radian * 180 / Math.PI);
                        mListener.onScaleAndRotate(offset, new PointF(centerPointF.x, centerPointF.y), -angle);
                    }

                    if (mListener != null && isInnerDrawRect) {
                        mListener.onDrag(prePointF, new PointF(targetX, targetY));
                    }
                    prePointF.set(targetX,targetY);
                }
                break;
            }
        }

        return true;
    }

    public interface OnTouchListener {
        void onDrag(PointF prePointF, PointF nowPointF);

        void onScaleAndRotate(float scaleFactor, PointF anchor, float rotation);

        void onDel();

        void onTouchDown(PointF curPoint);

        void onAlignClick();

        void onHorizFlipClick();

        void onBeyondDrawRectClick();//超出字幕或者贴纸框部分点击回调
    }

    public interface onDrawRectClickListener {
        void onDrawRectClick(int captionIndex);//矩形框点击,只是用于修改字幕
    }

    public interface onStickerMuteListenser {
        void onStickerMute();//贴纸静音回调
    }


    public void setPicturePath(String filePath) {
        this.filePath = filePath == null ? "" : filePath;
        waterMarkBitmap = ImageConverter.convertImage(getContext(),filePath);
    }

    public Point getPicturePoint(String filePath) {
        return ImageConverter.getPicturePoint(filePath,getContext());
    }
}
