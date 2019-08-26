// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterFace;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterSquareFace;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterSnakeFace;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterNoseFallFace;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterFace;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterNoseFallFace;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterSnakeFace;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterSquareFace;

public class TuSDKMonsterFaceWrap extends FilterWrap implements SelesParameters.FilterFacePositionInterface
{
    private TuSDKMonsterFaceType a;
    
    public TuSDKMonsterFaceWrap(final TuSDKMonsterFaceType tuSDKMonsterFaceType) {
        this.a = TuSDKMonsterFaceType.TuSDKMonsterFaceTypeBigNose;
        final SelesFilter a = this.a(tuSDKMonsterFaceType);
        this.mLastFilter = a;
        this.mFilter = a;
    }
    
    private TuSDKMonsterFaceWrap() {
    }
    
    private SelesFilter a(final TuSDKMonsterFaceType tuSDKMonsterFaceType) {
        switch (tuSDKMonsterFaceType.ordinal()) {
            case 1: {
                return new TuSDKMonsterNoseFallFace();
            }
            case 2: {
                final TuSDKMonsterSnakeFace tuSDKMonsterSnakeFace = new TuSDKMonsterSnakeFace();
                tuSDKMonsterSnakeFace.setMonsterFaceType(2);
                return tuSDKMonsterSnakeFace;
            }
            case 3: {
                final TuSDKMonsterSnakeFace tuSDKMonsterSnakeFace2 = new TuSDKMonsterSnakeFace();
                tuSDKMonsterSnakeFace2.setMonsterFaceType(1);
                return tuSDKMonsterSnakeFace2;
            }
            case 4: {
                return new TuSDKMonsterSquareFace();
            }
            case 5: {
                final TuSDKMonsterFace tuSDKMonsterFace = new TuSDKMonsterFace();
                tuSDKMonsterFace.setMonsterFaceType(1);
                return tuSDKMonsterFace;
            }
            case 6: {
                final TuSDKMonsterFace tuSDKMonsterFace2 = new TuSDKMonsterFace();
                tuSDKMonsterFace2.setMonsterFaceType(3);
                return tuSDKMonsterFace2;
            }
            default: {
                final TuSDKMonsterFace tuSDKMonsterFace3 = new TuSDKMonsterFace();
                tuSDKMonsterFace3.setMonsterFaceType(2);
                return tuSDKMonsterFace3;
            }
        }
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
        return new TuSDKMonsterFaceWrap(this.a);
    }
    
    public enum TuSDKMonsterFaceType
    {
        TuSDKMonsterFaceTypeBigNose, 
        TuSDKMonsterFaceTypePieFace, 
        TuSDKMonsterFaceTypeSquareFace, 
        TuSDKMonsterFaceTypeThickLips, 
        TuSDKMonsterFaceTypeSmallEyes, 
        TuSDKMonsterFaceTypePapayaFace, 
        TuSDKMonsterFaceTypeSnakeFace;
    }
}
