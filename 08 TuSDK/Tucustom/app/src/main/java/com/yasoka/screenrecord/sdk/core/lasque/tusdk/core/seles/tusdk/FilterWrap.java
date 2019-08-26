// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.graphics.Bitmap;
import android.graphics.PointF;
//import org.lasque.tusdk.core.seles.tusdk.particle.TuSDKParticleFilterInterface;
//import org.lasque.tusdk.core.seles.sources.SelesOutput;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.particle.TuSDKParticleFilterInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.util.Collection;
import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.utils.ReflectUtils;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.List;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;

public class FilterWrap
{
    private FilterOption a;
    protected SelesOutInput mFilter;
    protected SelesOutInput mLastFilter;
    private List<SelesPicture> b;
    private boolean c;
    
    public FilterOption getOption() {
        return this.a;
    }
    
    public String getCode() {
        if (this.a == null) {
            return null;
        }
        return this.a.code;
    }
    
    public SelesOutInput getFilter() {
        return this.mFilter;
    }
    
    public SelesOutInput getLastFilter() {
        return this.mLastFilter;
    }
    
    public static FilterWrap creat(final FilterOption filterOption) {
        if (filterOption == null) {
            TLog.e("Can not found FilterOption", new Object[0]);
            return null;
        }
        return new FilterWrap(filterOption);
    }
    
    protected FilterWrap() {
    }
    
    protected FilterWrap(final FilterOption filterOption) {
        this.changeOption(filterOption);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this.a == null) {
            return super.equals(obj);
        }
        return obj != null && obj instanceof FilterWrap && this.a.equals(((FilterWrap)obj).getOption());
    }
    
    public boolean equalsCode(final String s) {
        return s != null && this.getCode() != null && s.equalsIgnoreCase(this.getCode());
    }
    
    public void changeFilter(final String s) {
        if (s == null || this.equalsCode(s)) {
            TLog.e("The filterCode exist or empty: %s", s);
            return;
        }
        this.changeOption(FilterLocalPackage.shared().option(s));
    }
    
    protected void changeOption(final FilterOption a) {
        if (a == null) {
            TLog.e("Can not found FilterOption", new Object[0]);
            return;
        }
        this.a();
        this.a = a;
        this.b();
    }
    
    public void destroy() {
        this.a();
        this.c = true;
        if (this.mLastFilter != null) {
            this.mLastFilter.removeAllTargets();
        }
    }
    
    private void a() {
        this.c = false;
        if (this.b != null) {
            final Iterator<SelesPicture> iterator = this.b.iterator();
            while (iterator.hasNext()) {
                iterator.next().destroy();
            }
            this.b.clear();
        }
        this.b = null;
        if (this.mFilter != null) {
            this.mFilter.removeAllTargets();
        }
        if (this.a != null) {
            this.a.destroy();
        }
    }
    
    private void b() {
        final SelesOutInput filter = this.a.getFilter();
        this.mFilter = filter;
        this.mLastFilter = filter;
        if (this.mFilter == null) {
            TLog.e("Can not found Filter class: %s", ReflectUtils.trace(this.a));
            return;
        }
        if (this.a.args != null && !this.a.args.isEmpty()) {
            this.setFilterParameter(new SelesParameters(this.a.args));
        }
        final ArrayList<SelesPicture> b = new ArrayList<SelesPicture>();
        final List<SelesPicture> loadTextures = FilterLocalPackage.shared().loadTextures(this.a.code);
        if (loadTextures != null) {
            b.addAll(loadTextures);
        }
        final List<SelesPicture> loadInternalTextures = FilterLocalPackage.shared().loadInternalTextures(this.a.internalTextures);
        if (loadInternalTextures != null) {
            b.addAll(loadInternalTextures);
        }
        if (this.a.getRuntimeTextureDelegate() != null) {
            final List<SelesPicture> runTimeTextures = this.a.getRuntimeTextureDelegate().getRunTimeTextures();
            if (runTimeTextures != null && runTimeTextures.size() > 0) {
                b.addAll(runTimeTextures);
            }
        }
        if (!b.isEmpty()) {
            this.b = b;
        }
    }
    
    public void processImage() {
        if (this.c) {
            return;
        }
        this.c = true;
        if (this.b == null) {
            return;
        }
        if (this.mFilter instanceof SelesParameters.FilterTexturesInterface) {
            ((SelesParameters.FilterTexturesInterface)this.mFilter).appendTextures(this.b);
            return;
        }
        int n = 1;
        for (final SelesPicture selesPicture : this.b) {
            selesPicture.processImage();
            selesPicture.addTarget(this.mFilter, n);
            ++n;
        }
    }
    
    public void rotationTextures(final InterfaceOrientation interfaceOrientation) {
        if (this.b == null || !this.a.texturesKeepInput || interfaceOrientation == null) {
            return;
        }
        ImageOrientation imageOrientation = ImageOrientation.Up;
        switch (interfaceOrientation.ordinal()) {
            case 1: {
                imageOrientation = ImageOrientation.Down;
                break;
            }
            case 2: {
                imageOrientation = ImageOrientation.Left;
                break;
            }
            case 3: {
                imageOrientation = ImageOrientation.Right;
                break;
            }
        }
        for (int i = 1; i < this.b.size() + 1; ++i) {
            this.mFilter.setInputRotation(imageOrientation, i);
        }
    }
    
    public void addTarget(final SelesContext.SelesInput selesInput, final int n) {
        if (this.mLastFilter == null) {
            return;
        }
        this.mLastFilter.addTarget(selesInput, n);
    }
    
    public void removeTarget(final SelesContext.SelesInput selesInput) {
        if (this.mLastFilter == null) {
            return;
        }
        this.mLastFilter.removeTarget(selesInput);
    }
    
    public void bindWithCameraView(final SelesContext.SelesInput selesInput) {
        if (selesInput == null) {
            return;
        }
        this.addTarget(selesInput, 0);
    }
    
    public void addOrgin(final SelesOutput selesOutput) {
        if (selesOutput == null || this.mFilter == null) {
            return;
        }
        selesOutput.addTarget(this.mFilter, 0);
    }
    
    public void removeOrgin(final SelesOutput selesOutput) {
        if (selesOutput == null || this.mFilter == null) {
            return;
        }
        selesOutput.removeTarget(this.mFilter);
    }
    
    public boolean hasFilterParameter() {
        return this.mFilter != null && this.mFilter instanceof SelesParameters.FilterParameterInterface;
    }
    
    public void setFilterParameter(final SelesParameters parameter) {
        if (!this.hasFilterParameter()) {
            return;
        }
        ((SelesParameters.FilterParameterInterface)this.mFilter).setParameter(parameter);
    }
    
    public SelesParameters getFilterParameter() {
        if (!this.hasFilterParameter()) {
            return null;
        }
        return ((SelesParameters.FilterParameterInterface)this.mFilter).getParameter();
    }
    
    public void submitFilterParameter() {
        if (!this.hasFilterParameter()) {
            return;
        }
        ((SelesParameters.FilterParameterInterface)this.mFilter).submitParameter();
    }
    
    public boolean hasParticleFilter() {
        return this.mFilter != null && this.mFilter instanceof TuSDKParticleFilterInterface;
    }
    
    public void updateParticleEmitPosition(final PointF pointF) {
        if (!this.hasParticleFilter()) {
            return;
        }
        ((TuSDKParticleFilterInterface)this.mFilter).updateParticleEmitPosition(pointF);
    }
    
    public void setParticleSize(final float particleSize) {
        if (!this.hasParticleFilter()) {
            return;
        }
        ((TuSDKParticleFilterInterface)this.mFilter).setParticleSize(particleSize);
    }
    
    public void setParticleColor(final int particleColor) {
        if (!this.hasParticleFilter()) {
            return;
        }
        ((TuSDKParticleFilterInterface)this.mFilter).setParticleColor(particleColor);
    }
    
    public FilterWrap clone() {
        final FilterWrap creat = creat(this.getOption());
        if (creat != null) {
            creat.setFilterParameter(this.getFilterParameter());
        }
        return creat;
    }
    
    public Bitmap process(final Bitmap bitmap) {
        return this.process(bitmap, ImageOrientation.Up);
    }
    
    public Bitmap process(final Bitmap bitmap, final ImageOrientation imageOrientation) {
        return this.process(bitmap, imageOrientation, 0.0f);
    }
    
    public Bitmap process(Bitmap imageRotaing, final ImageOrientation imageOrientation, final float n) {
        if (imageRotaing == null || imageRotaing.isRecycled()) {
            return null;
        }
        float n2;
        Bitmap bitmap;
        for (imageRotaing = BitmapHelper.imageRotaing(imageRotaing, imageOrientation), n2 = 1.0f, bitmap = this.a(imageRotaing); bitmap == null && n2 > 0.0f; bitmap = this.clone().a(BitmapHelper.imageScale(imageRotaing, n2)), n2 -= n) {}
        return bitmap;
    }
    
    private Bitmap a(Bitmap imageScale) {
        if (imageScale == null || imageScale.isRecycled() || this.c) {
            return null;
        }
        final TuSdkSize create = TuSdkSize.create(imageScale);
        this.processImage();
        Bitmap imageByFilteringImage = null;
        try {
            imageScale = BitmapHelper.imageScale(imageScale, create.limitScale());
            this.processImage();
            imageByFilteringImage = this.mFilter.imageByFilteringImage(imageScale);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            TLog.e(outOfMemoryError, "appliedFilter OutOfMemoryError: %s ", create);
        }
        catch (Exception ex) {
            TLog.e(ex, "appliedFilter Exception: %s ", create);
        }
        return imageByFilteringImage;
    }
    
    public Bitmap captureVideoImage() {
        final SelesOutInput lastFilter = this.getLastFilter();
        if (lastFilter == null) {
            return null;
        }
        lastFilter.useNextFrameForImageCapture();
        return lastFilter.imageFromCurrentlyProcessedOutput();
    }
    
    private static class TempResult
    {
    }
}
