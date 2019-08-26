// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import java.util.Iterator;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.nio.IntBuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;

public class SelesFilterGroup extends SelesOutInput
{
    protected List<SelesOutInput> mFilters;
    protected boolean mIsEndProcessing;
    private SelesOutInput a;
    private List<SelesOutInput> b;
    private SelesOutInput c;
    
    public SelesOutInput getTerminalFilter() {
        return this.a;
    }
    
    public void setTerminalFilter(final SelesOutInput a) {
        this.a = a;
    }
    
    public List<SelesOutInput> getInitialFilters() {
        return this.b;
    }
    
    public void setInitialFilters(final SelesOutInput... a) {
        if (a == null) {
            return;
        }
        this.b = new ArrayList<SelesOutInput>(Arrays.asList(a));
    }
    
    public SelesOutInput getInputFilterToIgnoreForUpdates() {
        return this.c;
    }
    
    public void setInputFilterToIgnoreForUpdates(final SelesOutInput c) {
        this.c = c;
    }
    
    public SelesFilterGroup() {
        this.mFilters = new ArrayList<SelesOutInput>();
    }
    
    @Override
    protected void onDestroy() {
    }
    
    public void addFilter(final SelesOutInput selesOutInput) {
        if (selesOutInput == null || this.mFilters.contains(selesOutInput)) {
            return;
        }
        this.mFilters.add(selesOutInput);
    }
    
    public SelesOutInput filterAtIndex(final int n) {
        return this.mFilters.get(n);
    }
    
    public int filterCount() {
        return this.mFilters.size();
    }
    
    @Override
    public void useNextFrameForImageCapture() {
        this.a.useNextFrameForImageCapture();
    }
    
    @Override
    public IntBuffer imageBufferFromCurrentlyProcessedOutput(final TuSdkSize tuSdkSize) {
        return this.a.imageBufferFromCurrentlyProcessedOutput(tuSdkSize);
    }
    
    @Override
    public Bitmap imageFromCurrentlyProcessedOutput() {
        return this.a.imageFromCurrentlyProcessedOutput();
    }
    
    @Override
    public void setTargetToIgnoreForUpdates(final SelesContext.SelesInput targetToIgnoreForUpdates) {
        this.a.setTargetToIgnoreForUpdates(targetToIgnoreForUpdates);
    }
    
    @Override
    public void addTarget(final SelesContext.SelesInput selesInput, final int n) {
        this.a.addTarget(selesInput, n);
    }
    
    @Override
    public void removeTarget(final SelesContext.SelesInput selesInput) {
        this.a.removeTarget(selesInput);
    }
    
    @Override
    public void removeAllTargets() {
        this.a.removeAllTargets();
    }
    
    @Override
    public List<SelesContext.SelesInput> targets() {
        return this.a.targets();
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        for (final SelesOutInput selesOutInput : this.b) {
            if (selesOutInput != this.getInputFilterToIgnoreForUpdates()) {
                selesOutInput.newFrameReady(n, n2);
            }
        }
    }
    
    @Override
    public void setInputFramebuffer(final SelesFramebuffer selesFramebuffer, final int n) {
        final Iterator<SelesOutInput> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            iterator.next().setInputFramebuffer(selesFramebuffer, n);
        }
    }
    
    @Override
    public int nextAvailableTextureIndex() {
        return 0;
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        final Iterator<SelesOutInput> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            iterator.next().setInputSize(tuSdkSize, n);
        }
    }
    
    @Override
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
        final Iterator<SelesOutInput> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            iterator.next().setInputRotation(imageOrientation, n);
        }
    }
    
    @Override
    public void forceProcessingAtSize(final TuSdkSize tuSdkSize) {
        final Iterator<SelesOutInput> iterator = this.mFilters.iterator();
        while (iterator.hasNext()) {
            iterator.next().forceProcessingAtSize(tuSdkSize);
        }
    }
    
    @Override
    public void forceProcessingAtSizeRespectingAspectRatio(final TuSdkSize tuSdkSize) {
        final Iterator<SelesOutInput> iterator = this.mFilters.iterator();
        while (iterator.hasNext()) {
            iterator.next().forceProcessingAtSizeRespectingAspectRatio(tuSdkSize);
        }
    }
    
    @Override
    public TuSdkSize maximumOutputSize() {
        return new TuSdkSize();
    }
    
    @Override
    public void endProcessing() {
        if (!this.mIsEndProcessing) {
            this.mIsEndProcessing = true;
            final Iterator<SelesOutInput> iterator = this.b.iterator();
            while (iterator.hasNext()) {
                iterator.next().endProcessing();
            }
        }
    }
    
    @Override
    public boolean wantsMonochromeInput() {
        boolean b = true;
        for (final SelesOutInput selesOutInput : this.b) {
            b = (b && selesOutInput.wantsMonochromeInput());
        }
        return b;
    }
    
    @Override
    public void setCurrentlyReceivingMonochromeInput(final boolean currentlyReceivingMonochromeInput) {
        final Iterator<SelesOutInput> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            iterator.next().setCurrentlyReceivingMonochromeInput(currentlyReceivingMonochromeInput);
        }
    }
}
