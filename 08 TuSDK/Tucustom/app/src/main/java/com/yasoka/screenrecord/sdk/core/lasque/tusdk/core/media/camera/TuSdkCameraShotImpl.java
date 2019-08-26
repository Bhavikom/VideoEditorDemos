// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

//import org.lasque.tusdk.core.face.FaceAligment;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.exif.ExifInterface;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.utils.RectHelper;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.utils.image.ExifHelper;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.core.TuSdkContext;
import android.hardware.Camera;
import android.media.MediaPlayer;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif.ExifInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ExifHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkCameraShotImpl implements TuSdkCameraShot
{
    private boolean a;
    private boolean b;
    private boolean c;
    private int d;
    private MediaPlayer e;
    private TuSdkCameraShotListener f;
    private TuSdkCameraShotFaceFaceAligment g;
    private TuSdkCameraShotFilter h;
    private Camera.ShutterCallback i;
    private TuSdkCameraBuilder j;
    
    public TuSdkCameraShotImpl() {
        this.i = (Camera.ShutterCallback)new Camera.ShutterCallback() {
            public void onShutter() {
            }
        };
    }
    
    @Override
    public boolean isAutoReleaseAfterCaptured() {
        return this.a;
    }
    
    public void setAutoReleaseAfterCaptured(final boolean a) {
        this.a = a;
    }
    
    public boolean isOutputImageData() {
        return this.b;
    }
    
    public void setOutputImageData(final boolean b) {
        this.b = b;
    }
    
    public boolean isDisableCaptureSound() {
        return this.c;
    }
    
    public void setDisableCaptureSound(final boolean c) {
        this.c = c;
    }
    
    public int getCaptureSoundRawId() {
        return this.d;
    }
    
    public void setCaptureSoundRawId(final int d) {
        this.d = d;
        if (this.e != null) {
            this.e.release();
            this.e = null;
        }
        if (this.d != 0) {
            this.setDisableCaptureSound(true);
            this.e = MediaPlayer.create(TuSdkContext.context(), this.d);
        }
    }
    
    private void a() {
        if (this.e == null || !this.c) {
            return;
        }
        this.e.start();
    }
    
    public void setShotListener(final TuSdkCameraShotListener f) {
        this.f = f;
    }
    
    public void setShutterCallback(final Camera.ShutterCallback i) {
        this.i = i;
    }
    
    public Camera.ShutterCallback getShutterCallback() {
        if (this.isDisableCaptureSound()) {
            return null;
        }
        return this.i;
    }
    

    
    @Override
    public void processData(final TuSdkResult tuSdkResult) {
        if (tuSdkResult.imageData == null) {
            if (this.f != null) {
                this.f.onCameraShotFailed(tuSdkResult);
            }
            return;
        }
        tuSdkResult.metadata = ExifHelper.getExifInterface(tuSdkResult.imageData);
        if (this.b) {
            if (this.f != null) {
                this.f.onCameraShotData(tuSdkResult);
            }
            return;
        }
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                final Bitmap imageResize = BitmapHelper.imageResize(BitmapHelper.imageDecode(tuSdkResult.imageData, true), tuSdkResult.outputSize, false, tuSdkResult.imageOrientation);
                if (imageResize == null) {
                    TLog.e("%s conver bitmap failed, result: %s", "TuSdkCameraShotImpl", tuSdkResult);
                    if (TuSdkCameraShotImpl.this.f != null) {
                        TuSdkCameraShotImpl.this.f.onCameraShotFailed(tuSdkResult);
                    }
                    return;
                }
                if (tuSdkResult.imageOrientation != ImageOrientation.Up || tuSdkResult.imageOrientation != ImageOrientation.UpMirrored) {
                    tuSdkResult.image = BitmapHelper.imageRotaing(tuSdkResult.image, ImageOrientation.Up);
                    tuSdkResult.imageOrientation = ImageOrientation.Up;
                }
                tuSdkResult.imageData = null;
                tuSdkResult.image = imageResize;
                FaceAligment[] detectionImageFace = null;
                if (TuSdkCameraShotImpl.this.g != null) {
                    detectionImageFace = TuSdkCameraShotImpl.this.g.detectionImageFace(imageResize, tuSdkResult.imageOrientation);
                }
                TuSdkSize scaleSize = BitmapHelper.computerScaleSize(imageResize, tuSdkResult.outputSize, false, false);
                if (tuSdkResult.imageOrientation != ImageOrientation.Up || tuSdkResult.imageOrientation != ImageOrientation.UpMirrored || tuSdkResult.imageOrientation != ImageOrientation.Down || tuSdkResult.imageOrientation != ImageOrientation.DownMirrored) {
                    scaleSize = TuSdkSize.create(scaleSize.height, scaleSize.width);
                }
                final SelesPicture selesPicture = new SelesPicture(imageResize, false);
                selesPicture.setScaleSize(scaleSize);
                tuSdkResult.imageSizeRatio = scaleSize.minMaxRatio();
                selesPicture.setOutputRect(RectHelper.computerMinMaxSideInRegionRatio(selesPicture.getScaleSize(), tuSdkResult.imageSizeRatio));
                selesPicture.setInputRotation(tuSdkResult.imageOrientation);
                if (TuSdkCameraShotImpl.this.h != null) {
                    selesPicture.addTarget(TuSdkCameraShotImpl.this.h.getFilters(detectionImageFace, selesPicture), 0);
                }
                selesPicture.processImage();
                tuSdkResult.image = selesPicture.imageFromCurrentlyProcessedOutput();
                tuSdkResult.outputSize = TuSdkSize.create(tuSdkResult.image);
                if (tuSdkResult.metadata == null) {
                    tuSdkResult.metadata.setTagValue(ExifInterface.TAG_IMAGE_WIDTH, tuSdkResult.outputSize.width);
                    tuSdkResult.metadata.setTagValue(ExifInterface.TAG_IMAGE_LENGTH, tuSdkResult.outputSize.height);
                    tuSdkResult.metadata.setTagValue(ExifInterface.TAG_ORIENTATION, tuSdkResult.imageOrientation.getExifOrientation());
                }
                if (TuSdkCameraShotImpl.this.f != null) {
                    TuSdkCameraShotImpl.this.f.onCameraShotBitmap(tuSdkResult);
                }
            }
        });
    }
    
    @Override
    public void configure(final TuSdkCameraBuilder j) {
        if (j == null) {
            TLog.e("%s configure builder is empty.", "TuSdkCameraShotImpl");
            return;
        }
        this.j = j;
    }
    
    @Override
    public void changeStatus(final TuSdkCamera.TuSdkCameraStatus tuSdkCameraStatus) {
    }
    
    @Override
    public void setDetectionImageFace(final TuSdkCameraShotFaceFaceAligment g) {
        this.g = g;
    }
    
    @Override
    public void setDetectionShotFilter(final TuSdkCameraShotFilter h) {
        this.h = h;
    }

    @Override
    public void takeJpegPicture(final TuSdkResult tuSdkResult, final TuSdkCameraShotResultListener tuSdkCameraShotResultListener) {
        if (this.j == null || this.j.getOrginCamera() == null) {
            TLog.w("%s takeJpegPicture need OrginCamera.", "TuSdkCameraShotImpl");
            return;
        }
        final Camera orginCamera = this.j.getOrginCamera();
        if (this.f != null) {
            this.f.onCameraWillShot(tuSdkResult);
        }
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                try {
                    orginCamera.takePicture(TuSdkCameraShotImpl.this.getShutterCallback(), (Camera.PictureCallback)null, (Camera.PictureCallback)new Camera.PictureCallback() {
                        public void onPictureTaken(final byte[] array, final Camera camera) {
                            TuSdkCameraShotImpl.this.a();
                            tuSdkCameraShotResultListener.onShotResule(array);
                        }
                    });
                }
                catch (RuntimeException ex) {
                    TLog.e(ex, "%s takeJpegPicture failed.", "TuSdkCameraShotImpl");
                    tuSdkCameraShotResultListener.onShotResule(null);
                }
            }
        });
    }
}
