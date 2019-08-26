// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity;

//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.modules.components.ComponentErrorType;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
//import org.lasque.tusdk.core.utils.image.ExifHelper;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import java.io.File;
//import org.lasque.tusdk.core.TuSdk;
import android.graphics.RectF;
import android.content.Context;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerFactory;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerResult;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.utils.FontUtils;
import java.util.ArrayList;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerText;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import android.graphics.Rect;
//import org.lasque.tusdk.core.TuSdkContext;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FontUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ExifHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentErrorType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerFactory;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerText;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;

public abstract class TuResultFragment extends TuComponentFragment
{
    private boolean a;
    private boolean b;
    private String c;
    private int d;
    private TuSdkWaterMarkOption e;
    
    public TuResultFragment() {
        this.d = 95;
    }
    
    public boolean isSaveToTemp() {
        return this.a;
    }
    
    public void setSaveToTemp(final boolean a) {
        this.a = a;
    }
    
    public boolean isSaveToAlbum() {
        return this.b;
    }
    
    public void setSaveToAlbum(final boolean b) {
        this.b = b;
    }
    
    public String getSaveToAlbumName() {
        return this.c;
    }
    
    public void setSaveToAlbumName(final String c) {
        this.c = c;
    }
    
    public int getOutputCompress() {
        if (this.d < 0) {
            this.d = 0;
        }
        else if (this.d > 100) {
            this.d = 100;
        }
        return this.d;
    }
    
    public void setOutputCompress(final int d) {
        this.d = d;
    }
    
    public void setWaterMarkOption(final TuSdkWaterMarkOption e) {
        if (this.e != null) {
            this.e.destroy();
        }
        this.e = e;
    }
    
    public TuSdkWaterMarkOption getWaterMarkOption() {
        return this.e;
    }
    
    protected abstract void notifyProcessing(final TuSdkResult p0);
    
    protected abstract boolean asyncNotifyProcessing(final TuSdkResult p0);
    
    protected void asyncProcessingIfNeedSave(final TuSdkResult tuSdkResult) {
        if (ThreadHelper.isMainThread()) {
            this.notifyProcessing(tuSdkResult);
            return;
        }
        if (!this.asyncNotifyProcessing(tuSdkResult)) {
            if (this.isSaveToTemp()) {
                this.saveToTemp(tuSdkResult);
            }
            else if (this.isSaveToAlbum()) {
                this.saveToAlbum(tuSdkResult);
            }
        }
        this.backUIThreadNotifyProcessing(tuSdkResult);
        StatisticsManger.appendComponent(ComponentActType.editPhotoAction);
    }
    
    protected void backUIThreadNotifyProcessing(final TuSdkResult tuSdkResult) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TuResultFragment.this.notifyProcessing(tuSdkResult);
            }
        });
    }
    
    protected Bitmap addWaterMarkToImage(final Bitmap bitmap) {
        if (bitmap == null || this.e == null || !this.e.isValid()) {
            return bitmap;
        }
        final Context context = TuSdkContext.ins().getContext();
        StickerData stickerData = null;
        StickerData stickerData2 = null;
        final Rect rect = new Rect(0, 0, 0, 0);
        final Bitmap markImage = this.e.getMarkImage();
        if (markImage != null) {
            stickerData = new StickerData();
            stickerData.stickerType = 1;
            stickerData.stickerId = 0L;
            stickerData.groupId = 0L;
            stickerData.setImage(markImage);
            stickerData.width = markImage.getWidth();
            stickerData.height = markImage.getHeight();
            rect.right = stickerData.width;
            rect.bottom = stickerData.height;
        }
        final String markText = this.e.getMarkText();
        if (StringHelper.isNotEmpty(markText) && StringHelper.isNotBlank(markText)) {
            stickerData2 = new StickerData();
            stickerData2.stickerType = 2;
            stickerData2.stickerId = 0L;
            stickerData2.groupId = 0L;
            final StickerText e = new StickerText();
            e.content = markText;
            e.color = this.e.getMarkTextColor();
            e.textSize = (float)this.e.getMarkTextSize();
            e.shadowColor = this.e.getMarkTextShadowColor();
            e.alignment = 0;
            e.rectTop = 0.0f;
            e.rectLeft = 0.0f;
            e.rectWidth = 1.0f;
            e.rectHeight = 1.0f;
            final ArrayList<StickerText> texts = new ArrayList<StickerText>();
            texts.add(e);
            stickerData2.texts = texts;
            final Rect textBounds = FontUtils.getTextBounds(e.content, e.textSize);
            stickerData2.width = textBounds.width();
            stickerData2.height = textBounds.height();
            if (rect.right > 0) {
                rect.right = rect.right + stickerData2.width + ContextUtils.dip2px(context, (float)this.e.getMarkTextPadding());
                rect.bottom = Math.max(rect.bottom, stickerData2.height);
            }
            else {
                rect.right = stickerData2.width;
                rect.bottom = stickerData2.height;
            }
        }
        final ArrayList<StickerResult> list = new ArrayList<StickerResult>();
        if (stickerData != null) {
            list.add(this.a(stickerData, bitmap.getWidth(), bitmap.getHeight(), rect, TuSdkWaterMarkOption.TextPosition.Right));
        }
        if (stickerData2 != null) {
            list.add(this.a(stickerData2, bitmap.getWidth(), bitmap.getHeight(), rect, TuSdkWaterMarkOption.TextPosition.Right));
        }
        return StickerFactory.megerStickers(bitmap, list);
    }
    
    private StickerResult a(final StickerData stickerData, final int n, final int n2, final Rect rect, final TuSdkWaterMarkOption.TextPosition textPosition) {
        final int dip2px = ContextUtils.dip2px(TuSdkContext.ins().getContext(), this.e.getMarkMargin());
        final RectF rectF = new RectF((n - stickerData.width / 2.0f - dip2px) / n, (n2 - rect.bottom / 2.0f - dip2px) / n2, stickerData.width * 1.0f / n, stickerData.height * 1.0f / n2);
        final TuSdkWaterMarkOption.WaterMarkPosition markPosition = this.e.getMarkPosition();
        if (stickerData.stickerType == 1) {
            if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomRight) {
                rectF.left = (n - rect.right - dip2px + stickerData.width / 2.0f) / n;
            }
            else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft) {
                rectF.left = (stickerData.width / 2.0f + dip2px) / n;
            }
            else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopLeft) {
                rectF.left = (stickerData.width / 2.0f + dip2px) / n;
                rectF.top = (rect.bottom / 2.0f + dip2px) / n2;
            }
            else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopRight) {
                rectF.left = (n - rect.right - dip2px + stickerData.width / 2.0f) / n;
                rectF.top = (rect.bottom / 2.0f + dip2px) / n2;
            }
            else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.Center) {
                rectF.left = (n / 2.0f - rect.right / 2.0f + stickerData.width / 2.0f) / n;
                rectF.top = 0.5f;
            }
        }
        else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft) {
            rectF.left = (rect.right - stickerData.width / 2.0f + dip2px) / n;
        }
        else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopLeft) {
            rectF.left = (rect.right - stickerData.width / 2.0f + dip2px) / n;
            rectF.top = (rect.bottom / 2.0f + dip2px) / n2;
        }
        else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopRight) {
            rectF.top = (rect.bottom / 2.0f + dip2px) / n2;
        }
        else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.Center) {
            rectF.left = (n / 2.0f + rect.right / 2.0f - stickerData.width / 2.0f) / n;
            rectF.top = 0.5f;
        }
        if (rect.right > stickerData.width && this.e.getMarkTextPosition() != textPosition) {
            rectF.left = (n - rect.right + stickerData.width / 2.0f - dip2px) / n;
            if (stickerData.stickerType == 1) {
                if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomRight) {
                    rectF.left = (n - stickerData.width / 2.0f - dip2px) / n;
                }
                else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft) {
                    rectF.left = (rect.right - stickerData.width / 2.0f + dip2px) / n;
                }
                else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopLeft) {
                    rectF.left = (rect.right - stickerData.width / 2.0f + dip2px) / n;
                    rectF.top = (rect.bottom / 2.0f + dip2px) / n2;
                }
                else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopRight) {
                    rectF.left = (n - stickerData.width / 2.0f - dip2px) / n;
                    rectF.top = (rect.bottom / 2.0f + dip2px) / n2;
                }
                else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.Center) {
                    rectF.left = (n / 2.0f + rect.right / 2.0f - stickerData.width / 2.0f) / n;
                    rectF.top = 0.5f;
                }
            }
            else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft) {
                rectF.left = (stickerData.width / 2.0f + dip2px) / n;
            }
            else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopLeft) {
                rectF.left = (stickerData.width / 2.0f + dip2px) / n;
                rectF.top = (rect.bottom / 2.0f + dip2px) / n2;
            }
            else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopRight) {
                rectF.top = (rect.bottom / 2.0f + dip2px) / n2;
            }
            else if (markPosition == TuSdkWaterMarkOption.WaterMarkPosition.Center) {
                rectF.left = (n / 2.0f - rect.right / 2.0f + stickerData.width / 2.0f) / n;
                rectF.top = 0.5f;
            }
        }
        final RectF rectF2 = rectF;
        rectF2.right += rectF.left;
        final RectF rectF3 = rectF;
        rectF3.bottom += rectF.top;
        final StickerResult stickerResult = new StickerResult();
        (stickerResult.item = stickerData.copy()).setImage(stickerData.getImage());
        stickerResult.degree = 0.0f;
        stickerResult.center = new RectF(rectF);
        return stickerResult;
    }
    
    protected void saveToTemp(final TuSdkResult tuSdkResult) {
        if (tuSdkResult == null || tuSdkResult.image == null || !this.a(tuSdkResult)) {
            return;
        }
        this.hubStatus(TuSdkContext.getString("lsq_save_saveToTemp"));
        BitmapHelper.saveBitmap(tuSdkResult.imageFile = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", StringHelper.timeStampString())), tuSdkResult.image, this.getOutputCompress());
        tuSdkResult.image = null;
        if (tuSdkResult.imageFile.exists()) {
            ExifHelper.writeExifInterface(tuSdkResult.metadata, tuSdkResult.imageFile);
            this.hubSuccess(TuSdkContext.getString("lsq_save_saveToTemp_completed"));
        }
        else {
            this.hubError(TuSdkContext.getString("lsq_save_saveToTemp_failed"));
        }
    }
    
    protected void saveToAlbum(final TuSdkResult tuSdkResult) {
        if (tuSdkResult == null || tuSdkResult.image == null || !this.a(tuSdkResult)) {
            return;
        }
        this.hubStatus(TuSdkContext.getString("lsq_save_saveToAlbum"));
        File albumFile = null;
        if (StringHelper.isNotBlank(this.getSaveToAlbumName())) {
            albumFile = AlbumHelper.getAlbumFile(this.getSaveToAlbumName());
        }
        tuSdkResult.imageSqlInfo = ImageSqlHelper.saveJpgToAblum((Context)this.getActivity(), tuSdkResult.image, this.getOutputCompress(), albumFile);
        if (tuSdkResult.imageSqlInfo != null) {
            albumFile = new File(tuSdkResult.imageSqlInfo.path);
        }
        tuSdkResult.image = null;
        if (tuSdkResult.imageSqlInfo != null && albumFile != null && albumFile.exists()) {
            ExifHelper.writeExifInterface(tuSdkResult.metadata, tuSdkResult.imageSqlInfo.path);
            if (tuSdkResult.imageSqlInfo.length == 0L) {
                tuSdkResult.imageSqlInfo.length = albumFile.length();
            }
            ImageSqlHelper.notifyRefreshAblum((Context)this.getActivity(), tuSdkResult.imageSqlInfo);
            this.hubSuccess(TuSdkContext.getString("lsq_save_saveToAlbum_succeed"));
        }
        else {
            this.hubError(TuSdkContext.getString("lsq_save_saveToAlbum_failed"));
        }
    }
    
    private boolean a(final TuSdkResult tuSdkResult) {
        final ComponentErrorType canSaveFile = this.canSaveFile();
        if (canSaveFile == null) {
            return true;
        }
        this.notifyError(tuSdkResult, canSaveFile);
        return false;
    }
    
    public ComponentErrorType canSaveFile() {
        ComponentErrorType componentErrorType = null;
        if (!FileHelper.mountedExternalStorage()) {
            componentErrorType = ComponentErrorType.TypeNotFoundSDCard;
            this.hubError(TuSdkContext.getString("lsq_save_not_found_sdcard"));
        }
        else if (!FileHelper.hasAvailableExternal((Context)this.getActivity())) {
            componentErrorType = ComponentErrorType.TypeStorageSpace;
            this.hubError(TuSdkContext.getString("lsq_save_insufficient_storage_space"));
        }
        return componentErrorType;
    }
    
    @Override
    public void onDestroyView() {
        if (this.e != null) {
            this.e.destroy();
            this.e = null;
        }
        super.onDestroyView();
    }
}
