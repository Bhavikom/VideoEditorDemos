// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.TuSdkResult;

public interface TuSdkCameraShot
{
    void configure(final TuSdkCameraBuilder p0);
    
    void changeStatus(final TuSdkCamera.TuSdkCameraStatus p0);
    
    void setDetectionImageFace(final TuSdkCameraShotFaceFaceAligment p0);
    
    void setDetectionShotFilter(final TuSdkCameraShotFilter p0);
    
    void takeJpegPicture(final TuSdkResult p0, final TuSdkCameraShotResultListener p1);
    
    boolean isAutoReleaseAfterCaptured();
    
    void processData(final TuSdkResult p0);
    
    public interface TuSdkCameraShotFilter
    {
        SelesOutInput getFilters(final FaceAligment[] p0, final SelesPicture p1);
    }
    
    public interface TuSdkCameraShotFaceFaceAligment
    {
        FaceAligment[] detectionImageFace(final Bitmap p0, final ImageOrientation p1);
    }
    
    public interface TuSdkCameraShotResultListener
    {
        void onShotResule(final byte[] p0);
    }
    
    public interface TuSdkCameraShotListener
    {
        void onCameraWillShot(final TuSdkResult p0);
        
        void onCameraShotFailed(final TuSdkResult p0);
        
        void onCameraShotData(final TuSdkResult p0);
        
        void onCameraShotBitmap(final TuSdkResult p0);
    }
}
