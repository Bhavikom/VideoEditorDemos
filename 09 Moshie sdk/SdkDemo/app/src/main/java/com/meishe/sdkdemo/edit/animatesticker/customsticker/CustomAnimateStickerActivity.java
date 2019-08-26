package com.meishe.sdkdemo.edit.animatesticker.customsticker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.view.CustomStickerDrawRect;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.ScreenUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

public class CustomAnimateStickerActivity extends BaseActivity {
    private static final String TAG = "CustomASActivity";
    private static final int SAVEBITMAP_FINISHED = 1002;
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private ImageView mCustomAnimateImage;
    private RelativeLayout mFreeModeButton;
    private RelativeLayout mCircleModeButton;
    private RelativeLayout mSquareModeButton;
    private ImageView mImageFree;
    private ImageView mImageCircle;
    private ImageView mImageSquare;
    private TextView mFreeText;
    private TextView mCircleText;
    private TextView mSquareText;

    private CustomStickerDrawRect mCustomDrawRect;

    private int mImageViewWidth;
    private int mImageViewHeight;
    private RectF mShapeDrawRectF;
    private ImageView mCustomStickerFinish;
    private int mShapeMode = Constants.CUSTOMSTICKER_EDIT_FREE_MODE;
    private NvsStreamingContext mStreamingContext;

    private String mImageSrcFilePath;
    private String mImageDstFilePath;
    private CustomAnimateStickerActivity.SaveBitmapHandler m_handler = new CustomAnimateStickerActivity.SaveBitmapHandler(this);

    Runnable mSaveBitmapRunnable = new Runnable(){
        @Override
        public void run() {
            if(mShapeMode == Constants.CUSTOMSTICKER_EDIT_FREE_MODE
                    || mShapeMode == Constants.CUSTOMSTICKER_EDIT_SQUARE_MODE){
                Bitmap result = getRectBitmap(mImageSrcFilePath);
                saveBitmapToLocal(result);
                if(!result.isRecycled())
                    result.recycle();
            }else if(mShapeMode == Constants.CUSTOMSTICKER_EDIT_CIRCLE_MODE) {
                Bitmap result = getCircleBitmap(mImageSrcFilePath);
                saveBitmapToLocal(result);
                if(!result.isRecycled())
                    result.recycle();
            }
            m_handler.sendEmptyMessage(SAVEBITMAP_FINISHED);
        }
    };

    static class SaveBitmapHandler extends Handler
    {
        WeakReference<CustomAnimateStickerActivity> mWeakReference;
        public SaveBitmapHandler(CustomAnimateStickerActivity activity)
        {
            mWeakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final CustomAnimateStickerActivity activity = mWeakReference.get();
            if(activity != null)
            {
                switch (msg.what) {
                    case SAVEBITMAP_FINISHED:
                        Log.e(TAG,"mImageDstFilePath = " + activity.mImageDstFilePath);
                        Bundle bundle = new Bundle();
                        bundle.putString("imageDstFilePath",activity.mImageDstFilePath);
                        AppManager.getInstance().jumpActivity(AppManager.getInstance().currentActivity(), CustomAnimateStickerEffectActivity.class, bundle);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_custom_animate_sticker;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottomLayout);
        mCustomAnimateImage = (ImageView)findViewById(R.id.customAnimateImage);
        mFreeModeButton = (RelativeLayout)findViewById(R.id.freeMode);
        mCircleModeButton = (RelativeLayout)findViewById(R.id.circleMode);
        mSquareModeButton = (RelativeLayout)findViewById(R.id.squareMode);
        mImageFree = (ImageView)findViewById(R.id.imageFree);
        mImageCircle = (ImageView)findViewById(R.id.imageCircle);
        mImageSquare = (ImageView)findViewById(R.id.imageSquare);
        mFreeText = (TextView)findViewById(R.id.freeText);
        mCircleText = (TextView)findViewById(R.id.circleText);
        mSquareText = (TextView)findViewById(R.id.squareText);
        mCustomDrawRect = (CustomStickerDrawRect) findViewById(R.id.customDrawRect);
        mCustomStickerFinish = (ImageView)findViewById(R.id.customStickerFinish);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.select_content);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null)
                mImageSrcFilePath = bundle.getString("imageSrcFilePath");
        }
        if(!setDisplayImage(mImageSrcFilePath))
            return;
        initDrawRect();
    }

    @Override
    protected void initListener() {
        mFreeModeButton.setOnClickListener(this);
        mCircleModeButton.setOnClickListener(this);
        mSquareModeButton.setOnClickListener(this);
        mCustomStickerFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.freeMode:
                if(mShapeMode == Constants.CUSTOMSTICKER_EDIT_FREE_MODE)
                    return;
                mImageFree.setImageResource(R.mipmap.custom_free_select);
                mFreeText.setTextColor(Color.parseColor("#ff4a90e2"));
                mImageCircle.setImageResource(R.mipmap.custom_circle);
                mCircleText.setTextColor(Color.parseColor("#ff909293"));
                mImageSquare.setImageResource(R.mipmap.custom_square);
                mSquareText.setTextColor(Color.parseColor("#ff909293"));
                mShapeMode = Constants.CUSTOMSTICKER_EDIT_FREE_MODE;
                resetDrawRectF();
                mCustomDrawRect.setDrawRect(mShapeDrawRectF,mShapeMode);
                break;
            case R.id.circleMode:
                if(mShapeMode == Constants.CUSTOMSTICKER_EDIT_CIRCLE_MODE)
                    return;
                mImageFree.setImageResource(R.mipmap.custom_free);
                mFreeText.setTextColor(Color.parseColor("#ff909293"));
                mImageCircle.setImageResource(R.mipmap.custom_circle_select);
                mCircleText.setTextColor(Color.parseColor("#ff4a90e2"));
                mImageSquare.setImageResource(R.mipmap.custom_square);
                mSquareText.setTextColor(Color.parseColor("#ff909293"));
                mShapeMode = Constants.CUSTOMSTICKER_EDIT_CIRCLE_MODE;
                resetDrawRectF();
                mCustomDrawRect.setDrawRect(mShapeDrawRectF,mShapeMode);
                break;
            case R.id.squareMode:
                if(mShapeMode == Constants.CUSTOMSTICKER_EDIT_SQUARE_MODE)
                    return;
                mImageFree.setImageResource(R.mipmap.custom_free);
                mFreeText.setTextColor(Color.parseColor("#ff909293"));
                mImageCircle.setImageResource(R.mipmap.custom_circle);
                mCircleText.setTextColor(Color.parseColor("#ff909293"));
                mImageSquare.setImageResource(R.mipmap.custom_square_select);
                mSquareText.setTextColor(Color.parseColor("#ff4a90e2"));
                mShapeMode = Constants.CUSTOMSTICKER_EDIT_SQUARE_MODE;
                resetDrawRectF();
                mCustomDrawRect.setDrawRect(mShapeDrawRectF,mShapeMode);
                break;
            case R.id.customStickerFinish:
                mCustomStickerFinish.setClickable(false);
                //开启线程存储裁剪的图片
                Thread saveBitmapThread = new Thread(mSaveBitmapRunnable);
                saveBitmapThread.start();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCustomStickerFinish.setClickable(true);
    }

    private String getBitmapFilePath(){
        File compileDir = new File(Environment.getExternalStorageDirectory(), "NvStreamingSdk" + File.separator + "Asset" + File.separator + "CustomAnimatedStickerImage");
        if (!compileDir.exists() && !compileDir.mkdirs()) {
            return null;
        }
        return compileDir.toString();
    }
    private void saveBitmapToLocal(Bitmap result){
        if(result == null)
            return;
        String targetFileDir = getBitmapFilePath();
        if (targetFileDir == null)
            return;
        StringBuilder targetFilePath = new StringBuilder(targetFileDir);
        targetFilePath.append("/");
        targetFilePath.append(String.valueOf(System.currentTimeMillis()));
        targetFilePath.append(".png");
        mImageDstFilePath = targetFilePath.toString();
        File file = new File(mImageDstFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            //将bitmap保存为file时，格式选择png，不然会出现黑色背景
            result.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Bitmap getCircleBitmap(String filePath){
        Bitmap scaledBitmap = getRectBitmap(filePath);
        float newRadius1 = (mShapeDrawRectF.right - mShapeDrawRectF.left)/2;
        float newRadius2 = (mShapeDrawRectF.bottom - mShapeDrawRectF.top)/2;
        float newRadius = newRadius1 >= newRadius2 ? newRadius2 : newRadius1;
        int newWidth = (int)Math.floor(2 * newRadius + 0.5D);
        Bitmap result = Bitmap.createBitmap(newWidth, newWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
        paint.setAntiAlias(true);// 设置画笔无锯齿
        //绘制圆
        canvas.drawCircle(newRadius, newRadius, newRadius, paint);
        //重置画笔
        paint.reset();
        //设置图像合成模式,该模式为只在源图像和目标图像相交的地方绘制源图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledBitmap, 0, 0, paint);
        if(!scaledBitmap.isRecycled())
            scaledBitmap.recycle();
        return result;
    }

    private Bitmap getRectBitmap(String filePath){
        Bitmap srcBitmap = BitmapFactory.decodeFile(filePath);
        if(srcBitmap == null)
            return null;
        int scaleWidth = mImageViewWidth;
        int scaleHeight = mImageViewHeight;
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(srcBitmap, scaleWidth, scaleHeight, true);
        if(scaleBitmap == null)
            return null;
        int x = (int)Math.floor(mShapeDrawRectF.left + 0.5D);
        int y = (int)Math.floor(mShapeDrawRectF.top + 0.5D);
        int newWidth = (int)(mShapeDrawRectF.right - mShapeDrawRectF.left);
        int newHeight = (int)(mShapeDrawRectF.bottom - mShapeDrawRectF.top);

        Bitmap result = Bitmap.createBitmap(scaleBitmap,x,y,newWidth,newHeight);
        if(!srcBitmap.isRecycled())
            srcBitmap.recycle();
        if(!scaleBitmap.isRecycled())
            scaleBitmap.recycle();
        return result;
    }

    private void resetDrawRectF(){
        int offset = 200;
        int minValue = mImageViewWidth > mImageViewHeight ? mImageViewHeight : mImageViewWidth;
        int newOffset = 2 * offset;
        if(minValue >= newOffset){
            mShapeDrawRectF.set(mImageViewWidth/2 - offset,
                    mImageViewHeight/2 - offset,
                    mImageViewWidth/2 + offset,
                    mImageViewHeight/2 + offset);
        }else {
            float left = (minValue == mImageViewWidth) ? 0 : (mImageViewWidth * 0.5f - mImageViewHeight * 0.5f);
            float right = (minValue == mImageViewWidth) ? mImageViewWidth : (mImageViewWidth * 0.5f + mImageViewHeight * 0.5f);
            float top = (minValue == mImageViewWidth) ? (mImageViewHeight * 0.5f - mImageViewWidth * 0.5f) : 0;
            float bottom = (minValue == mImageViewWidth) ? (mImageViewHeight * 0.5f + mImageViewWidth * 0.5f) : mImageViewHeight;
            mShapeDrawRectF.set(left,top,right,bottom);
        }
    }
    private boolean setDisplayImage( String imageFilePath){
        if(imageFilePath.isEmpty())
            return false;
        Glide.with(CustomAnimateStickerActivity.this).load(imageFilePath).into(mCustomAnimateImage);
        setDispalyImageSize(imageFilePath);
        return true;
    }
    private void setDispalyImageSize(String imagePath){
        int statusHeight = ScreenUtils.getStatusBarHeight(this);//状态栏高度
        int screenWidth = ScreenUtils.getScreenWidth(this);//屏宽
        int screenHeight = ScreenUtils.getScreenHeight(this);//屏高
        int titleHeight = mTitleBar.getLayoutParams().height;
        int bottomHeight = mBottomLayout.getLayoutParams().height;
        NvsAVFileInfo avFileInfo = mStreamingContext.getAVFileInfo(imagePath);
        int offset = 200;
        if(avFileInfo != null) {
            ViewGroup.LayoutParams layoutParams = mCustomAnimateImage.getLayoutParams();
            NvsSize videoSize = avFileInfo.getVideoStreamDimension(0);
            float newRatio = videoSize.width / (videoSize.height * 1.0f);
            if(newRatio >= 1.0f){
                int newWidth = screenWidth - offset;
                layoutParams.width = newWidth;
                layoutParams.height = (int)Math.floor(newWidth / newRatio + 0.5D);
            }else {
                int newHeight = screenHeight - statusHeight - titleHeight - bottomHeight - offset;
                layoutParams.width = (int)Math.floor(newHeight * newRatio + 0.5D);
                layoutParams.height = newHeight;
            }
            mImageViewWidth = layoutParams.width;
            mImageViewHeight = layoutParams.height;
            mCustomAnimateImage.setLayoutParams(layoutParams);
        }
    }

    private void setDrawRectLayoutParams(CustomStickerDrawRect customDrawRect){
        RelativeLayout.LayoutParams imgLayoutParams = (RelativeLayout.LayoutParams)mCustomAnimateImage.getLayoutParams();
        if(imgLayoutParams == null)
            return;
        RelativeLayout.LayoutParams drawRectlayoutParams = (RelativeLayout.LayoutParams)customDrawRect.getLayoutParams();
        if(drawRectlayoutParams != null){
            drawRectlayoutParams.leftMargin = imgLayoutParams.leftMargin;
            drawRectlayoutParams.topMargin = imgLayoutParams.topMargin;
            drawRectlayoutParams.width = imgLayoutParams.width + customDrawRect.getScaleImgBtnWidth();
            drawRectlayoutParams.height = imgLayoutParams.height + customDrawRect.getScaleImgBtnHeight();
            customDrawRect.setLayoutParams(drawRectlayoutParams);
        }
    }
    private void initDrawRect(){
        mShapeDrawRectF = new RectF();
        resetDrawRectF();
        mCustomDrawRect.setImgSize(mImageViewWidth,mImageViewHeight);
        mCustomDrawRect.setDrawRect(mShapeDrawRectF,Constants.CUSTOMSTICKER_EDIT_FREE_MODE);
        setDrawRectLayoutParams(mCustomDrawRect);
        mCustomDrawRect.setOnDrawRectListener(new CustomStickerDrawRect.OnDrawRectListener() {
            @Override
            public void onDrawRect(RectF rectF) {
                mShapeDrawRectF = rectF;
            }
        });
    }
}
