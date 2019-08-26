// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
//import org.lasque.tusdk.core.face.FaceAligment;

public interface TuSdkImageEngine
{
    void release();
    
    void setFaceAligments(final FaceAligment[] p0);
    
    void setEngineRotation(final TuSdkEngineOrientation p0);
    
    void setFilter(final FilterWrap p0);
    
    void setListener(final TuSdkPictureDataCompletedListener p0);
    
    void asyncProcessPictureData(final byte[] p0);
    
    void asyncProcessPictureData(final byte[] p0, final InterfaceOrientation p1);
    
    public interface TuSdkPictureDataCompletedListener
    {
        void onPictureDataCompleted(final IntBuffer p0, final TuSdkSize p1);
    }
}
