// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;

public interface SelesStillCameraInterface extends SelesBaseCameraInterface
{
    void setAutoReleaseAfterCaptured(final boolean p0);
    
    void capturePhotoAsJPEGData(final CapturePhotoAsJPEGDataCallback p0);
    
    void capturePhotoAsBitmap(final SelesOutInput p0, final CapturePhotoAsBitmapCallback p1);
    
    void capturePhotoAsBitmap(final SelesOutInput p0, final ImageOrientation p1, final CapturePhotoAsBitmapCallback p2);
    
    public interface CapturePhotoAsBitmapCallback
    {
        void onCapturePhotoAsBitmap(final Bitmap p0);
    }
    
    public interface CapturePhotoAsJPEGDataCallback
    {
        void onCapturePhotoAsJPEGData(final byte[] p0);
    }
}
