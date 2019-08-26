// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.combo;

import android.graphics.RectF;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMap2DFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSdkPlasticFace;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.List;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.seles.tusdk.FilterManager;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutput;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMap2DFilter;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSdkPlasticFace;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

public class Face2DComboFilterWrap extends FilterWrap implements SelesParameters.FilterStickerInterface
{
    private SelesParameters a;
    private boolean b;
    private TuSdkPlasticFace c;
    private TuSDKMap2DFilter d;
    private SelesPointDrawFilter e;
    protected SelesOutInput mFirstFilter;
    private boolean f;
    
    public static Face2DComboFilterWrap creat() {
        return creat(FilterLocalPackage.shared().option(null));
    }
    
    public static Face2DComboFilterWrap creat(final FilterOption filterOption) {
        if (filterOption == null) {
            TLog.e("Can not found FilterOption", new Object[0]);
            return null;
        }
        return new Face2DComboFilterWrap(filterOption);
    }
    
    public boolean isEnablePlastic() {
        return this.b;
    }
    
    public void setIsEnablePlastic(final boolean b) {
        if (this.b == b) {
            return;
        }
        this.b = b;
        this.a();
    }
    
    protected Face2DComboFilterWrap(final FilterOption filterOption) {
        this.f = false;
        this.c = new TuSdkPlasticFace();
        this.d = new TuSDKMap2DFilter();
        if (this.f) {
            this.e = new SelesPointDrawFilter();
        }
        this.changeOption(filterOption);
    }
    
    @Override
    public Face2DComboFilterWrap clone() {
        final Face2DComboFilterWrap creat = creat(this.getOption());
        if (creat != null) {
            creat.setFilterParameter(this.getFilterParameter());
            creat.setIsEnablePlastic(this.isEnablePlastic());
            creat.setStickerVisibility(this.isStickerVisibility());
        }
        return creat;
    }
    
    @Override
    protected void changeOption(final FilterOption filterOption) {
        super.changeOption(filterOption);
        this.a();
    }
    
    @Override
    public void addOrgin(final SelesOutput selesOutput) {
        if (selesOutput == null || this.mFirstFilter == null) {
            return;
        }
        selesOutput.addTarget(this.mFirstFilter, 0);
    }
    
    @Override
    public void removeOrgin(final SelesOutput selesOutput) {
        if (selesOutput == null || this.mFirstFilter == null) {
            return;
        }
        selesOutput.removeTarget(this.mFirstFilter);
    }
    
    private void a() {
        if (this.mFilter == null) {
            return;
        }
        (this.a = new SelesParameters()).merge(super.getFilterParameter());
        this.a.merge(this.c.getParameter());
        this.mFilter.removeAllTargets();
        this.c.removeAllTargets();
        this.d.removeAllTargets();
        if (this.e != null) {
            this.e.removeAllTargets();
        }
        if (FilterManager.shared().isConmicEffectFilter(this.getOption().code)) {
            if (this.isEnablePlastic()) {
                this.c.addTarget(this.d, 0);
                this.d.addTarget(this.mFilter, 0);
                this.mFirstFilter = this.c;
            }
            else {
                this.d.addTarget(this.mFilter, 0);
                this.mFirstFilter = this.d;
            }
            this.mLastFilter = this.mFilter;
        }
        else {
            if (this.isEnablePlastic()) {
                this.mFilter.addTarget(this.c, 0);
                this.c.addTarget(this.d, 0);
            }
            else {
                this.mFilter.addTarget(this.d, 0);
            }
            this.mFirstFilter = this.mFilter;
            this.mLastFilter = this.d;
        }
        if (this.f) {
            this.mLastFilter.addTarget(this.e, 0);
            this.mLastFilter = this.e;
        }
    }
    
    @Override
    public void setFilterParameter(final SelesParameters selesParameters) {
        if (selesParameters == null) {
            return;
        }
        final SelesParameters filterParameter = super.getFilterParameter();
        if (filterParameter != null) {
            filterParameter.syncArgs(selesParameters);
            super.setFilterParameter(filterParameter);
        }
        final SelesParameters parameter = this.c.getParameter();
        parameter.syncArgs(selesParameters);
        this.c.setParameter(parameter);
    }
    
    @Override
    public SelesParameters getFilterParameter() {
        return this.a;
    }
    
    @Override
    public void submitFilterParameter() {
        super.submitFilterParameter();
        this.c.submitParameter();
    }
    
    @Override
    public void updateFaceFeatures(final FaceAligment[] array, final float n) {
        this.c.updateFaceFeatures(array, n);
        this.d.updateFaceFeatures(array, n);
        if (this.f) {
            this.e.updateFaceFeatures(array, n);
        }
    }
    
    @Override
    public void updateStickers(final List<TuSDKLiveStickerImage> list) {
        this.d.updateStickers(list);
    }
    
    public int[] getMap2DCurrentStickerIndexs() {
        return this.d.getCurrentStickerIndexs();
    }
    
    public void setMap2DCurrentStickerIndex(final int[] currentStickerIndexs) {
        this.d.setCurrentStickerIndexs(currentStickerIndexs);
    }
    
    @Override
    public void setDisplayRect(final RectF rectF, final float n) {
        this.d.setDisplayRect(rectF, n);
    }
    
    @Override
    public void setEnableAutoplayMode(final boolean enableAutoplayMode) {
        this.d.setEnableAutoplayMode(enableAutoplayMode);
    }
    
    @Override
    public void seekStickerToFrameTime(final long n) {
        this.d.seekStickerToFrameTime(n);
    }
    
    @Override
    public void setBenchmarkTime(final long benchmarkTime) {
        this.d.setBenchmarkTime(benchmarkTime);
    }
    
    @Override
    public void setStickerVisibility(final boolean stickerVisibility) {
        this.d.setStickerVisibility(stickerVisibility);
    }
    
    public boolean isStickerVisibility() {
        return this.d.isStickerVisibility();
    }
}
