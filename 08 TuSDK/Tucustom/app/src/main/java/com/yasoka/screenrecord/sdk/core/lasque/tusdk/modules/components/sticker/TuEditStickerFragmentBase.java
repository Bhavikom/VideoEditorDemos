// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.sticker;

//import org.lasque.tusdk.modules.view.widget.sticker.StickerFactory;
import android.graphics.Rect;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.TuSdkResult;
import android.graphics.Bitmap;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
//import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuMaskRegionView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuImageResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.sticker.StickerView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerFactory;
//import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
//import org.lasque.tusdk.impl.components.widget.sticker.StickerView;
//import org.lasque.tusdk.impl.activity.TuImageResultFragment;

public abstract class TuEditStickerFragmentBase extends TuImageResultFragment
{
    public abstract StickerView getStickerView();
    
    public abstract TuMaskRegionView getCutRegionView();
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        StatisticsManger.appendComponent(ComponentActType.editStickerFragment);
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
    }
    
    protected void handleBackButton() {
        this.navigatorBarBackAction(null);
    }
    
    public final void appendStickerItem(final StickerData stickerData) {
        if (stickerData == null || this.getStickerView() == null) {
            return;
        }
        this.getStickerView().appendSticker(stickerData);
    }
    
    public final void appendStickerItem(final Bitmap bitmap) {
        if (bitmap == null || this.getStickerView() == null) {
            return;
        }
        this.getStickerView().appendSticker(bitmap);
    }
    
    protected void handleCompleteButton() {
        if (this.getStickerView() == null) {
            this.handleBackButton();
            return;
        }
        final TuSdkResult tuSdkResult = new TuSdkResult();
        Rect regionRect = null;
        if (this.getCutRegionView() != null) {
            regionRect = this.getCutRegionView().getRegionRect();
        }
        tuSdkResult.stickers = this.getStickerView().getResults(regionRect);
        if (tuSdkResult.stickers == null || tuSdkResult.stickers.size() == 0) {
            this.handleBackButton();
            return;
        }
        this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                TuEditStickerFragmentBase.this.asyncEditWithResult(tuSdkResult);
            }
        }).start();
    }
    
    protected void asyncEditWithResult(final TuSdkResult tuSdkResult) {
        this.loadOrginImage(tuSdkResult);
        if (tuSdkResult.stickers != null) {
            tuSdkResult.image = StickerFactory.megerStickers(tuSdkResult.image, tuSdkResult.stickers, null, !this.isShowResultPreview());
            tuSdkResult.stickers = null;
        }
        this.asyncProcessingIfNeedSave(tuSdkResult);
    }
}
