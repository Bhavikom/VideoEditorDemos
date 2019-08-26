// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaEffectData;

import java.util.List;
//import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;

public interface TuSdkEditorEffector
{
    void setInputImageOrientation(final ImageOrientation p0);
    
    void setOutputImageOrientation(final ImageOrientation p0);
    
    void setFilterChangeListener(final TuSdkEffectorFilterChangeListener p0);
    
    boolean addMediaEffectData(final TuSdkMediaEffectData p0);
    
     <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType p0);
    
    List<TuSdkMediaEffectData> getAllMediaEffectData();
    
    void removeMediaEffectData(final TuSdkMediaEffectData p0);
    
    void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType p0);
    
    void removeAllMediaEffect();
    
    void destroy();
    
    public interface TuSdkEffectorFilterChangeListener
    {
        void onFilterChanged(final FilterWrap p0);
    }
}
