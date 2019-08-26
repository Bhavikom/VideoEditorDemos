// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSdkPlasticFace;
//import org.lasque.tusdk.core.seles.SelesParameters;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSdkPlasticFace;

public class TuSDKPlasticFaceWrap extends FilterWrap implements SelesParameters.FilterFacePositionInterface
{
    public TuSDKPlasticFaceWrap() {
        final TuSdkPlasticFace tuSdkPlasticFace = new TuSdkPlasticFace();
        this.mLastFilter = tuSdkPlasticFace;
        this.mFilter = tuSdkPlasticFace;
    }
    
    public static TuSDKPlasticFaceWrap creat() {
        return new TuSDKPlasticFaceWrap();
    }
    
    @Override
    protected void changeOption(final FilterOption filterOption) {
    }
    
    @Override
    public void updateFaceFeatures(final FaceAligment[] array, final float n) {
        if (this.mFilter == null || !(this.mFilter instanceof SelesParameters.FilterFacePositionInterface)) {
            return;
        }
        ((SelesParameters.FilterFacePositionInterface)this.mFilter).updateFaceFeatures(array, n);
    }
    
    @Override
    public FilterWrap clone() {
        final TuSDKPlasticFaceWrap tuSDKPlasticFaceWrap = new TuSDKPlasticFaceWrap();
        tuSDKPlasticFaceWrap.mFilter.setParameter(this.mFilter.getParameter());
        return tuSDKPlasticFaceWrap;
    }
}
