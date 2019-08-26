// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine;

import android.graphics.PointF;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
//import org.lasque.tusdk.core.seles.sources.SelesOutput;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.api.video.preproc.filter.TuSDKFilterPicture;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter.TuSDKFilterPicture;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
//import org.lasque.tusdk.core.face.FaceAligment;

public class TuSdkImageEngineImpl implements TuSdkImageEngine
{
    private FaceAligment[] a;
    private TuSdkEngineOrientation b;
    private FilterWrap c;
    private boolean d;
    private TuSdkPictureDataCompletedListener e;
    private static final int[][] f;
    private static final int[][] g;
    
    @Override
    public void setFaceAligments(final FaceAligment[] a) {
        this.a = a;
    }
    
    @Override
    public void setEngineRotation(final TuSdkEngineOrientation b) {
        if (b == null) {
            return;
        }
        this.b = b;
    }
    
    @Override
    public void setFilter(final FilterWrap c) {
        if (c == null) {
            return;
        }
        this.c = c;
    }
    
    @Override
    public void setListener(final TuSdkPictureDataCompletedListener e) {
        this.e = e;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    protected void finalize() {
        this.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    private TuSdkSize a(final ImageOrientation imageOrientation, final TuSdkSize tuSdkSize) {
        final TuSdkSize create = TuSdkSize.create(tuSdkSize);
        if (imageOrientation != null && imageOrientation.isTransposed()) {
            create.width = tuSdkSize.height;
            create.height = tuSdkSize.width;
        }
        return create;
    }
    
    private ImageOrientation a(final ImageOrientation imageOrientation) {
        if (imageOrientation == null) {
            return ImageOrientation.Up;
        }
        switch (imageOrientation.ordinal()) {
            case 1: {
                return ImageOrientation.UpMirrored;
            }
            case 2: {
                return ImageOrientation.Up;
            }
            case 3: {
                return ImageOrientation.DownMirrored;
            }
            case 4: {
                return ImageOrientation.Down;
            }
            case 5: {
                return ImageOrientation.RightMirrored;
            }
            case 6: {
                return ImageOrientation.Right;
            }
            case 7: {
                return ImageOrientation.LeftMirrored;
            }
            case 8: {
                return ImageOrientation.Left;
            }
            default: {
                return ImageOrientation.Up;
            }
        }
    }
    
    private ImageOrientation a(InterfaceOrientation portrait, final int n) {
        if (this.b == null) {
            return ImageOrientation.Up;
        }
        if (portrait == null) {
            portrait = InterfaceOrientation.Portrait;
        }
        if (this.b.getCameraFacing() == CameraConfigs.CameraFacing.Front) {
            return ImageOrientation.getValue(TuSdkImageEngineImpl.f[portrait.getDegree() / 90][n / 90], true);
        }
        if (this.b.getCameraFacing() == CameraConfigs.CameraFacing.Back) {
            return ImageOrientation.getValue(TuSdkImageEngineImpl.g[portrait.getDegree() / 90][n / 90], false);
        }
        return ImageOrientation.Up;
    }
    
    @Override
    public void asyncProcessPictureData(final byte[] array) {
        this.asyncProcessPictureData(array, InterfaceOrientation.Portrait, 0);
    }
    
    @Override
    public void asyncProcessPictureData(final byte[] array, final InterfaceOrientation interfaceOrientation) {
        this.asyncProcessPictureData(array, interfaceOrientation, 0);
    }
    
    public void asyncProcessPictureData(final byte[] array, final InterfaceOrientation interfaceOrientation, final int i) {
        if (i != 0 && i != 90 && i != 180 && i != 270) {
            throw new IllegalStateException("Invalid rotation=" + i);
        }
        this.asyncProcessPictureData(array, interfaceOrientation, this.a(interfaceOrientation, i));
    }
    
    public final void asyncProcessPictureData(final byte[] array, final InterfaceOrientation interfaceOrientation, final ImageOrientation imageOrientation) {
        if (this.d || array == null) {
            return;
        }
        final InterfaceOrientation interfaceOrientation2 = (interfaceOrientation == null) ? InterfaceOrientation.Portrait : interfaceOrientation;
        final FaceAligment[] array2 = new FaceAligment[(this.a != null) ? this.a.length : 0];
        if (this.a != null) {
            System.arraycopy(this.a, 0, array2, 0, array2.length);
        }
        this.d = true;
        ThreadHelper.runThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final FilterWrap clone = TuSdkImageEngineImpl.this.c.clone();
                if (clone instanceof TuSDKComboFilterWrapChain) {
                    clone.processImage();
                }
                clone.processImage();
                ThreadHelper.runThread((Runnable)new Runnable() {
                    final /* synthetic */ Bitmap a = BitmapHelper.imageDecode(array, true);
                    
                    @Override
                    public void run() {
                        TuSdkImageEngineImpl.this.a(this.a, array2, clone, interfaceOrientation2, imageOrientation);
                        clone.destroy();
                    }
                });
            }
        });
    }
    
    private void a(final Bitmap bitmap, final FaceAligment[] array, final FilterWrap filterWrap, final InterfaceOrientation interfaceOrientation, final ImageOrientation imageOrientation) {
        if (filterWrap == null || bitmap == null || this.b == null) {
            this.a((IntBuffer)null, null);
            return;
        }
        if (this.b.isOriginalCaptureOrientation()) {
            this.a(array, this.b.getOutputSize(), this.a(this.b.getInputRotation(), TuSdkSize.create(bitmap.getWidth(), bitmap.getHeight())));
        }
        final TuSDKFilterPicture tuSDKFilterPicture = new TuSDKFilterPicture(bitmap, false, true);
        ImageOrientation a = (imageOrientation == null) ? ImageOrientation.Up : imageOrientation;
        boolean b = false;
        if (this.b.isOutputCaptureMirrorEnabled() && this.b.getCameraFacing() == CameraConfigs.CameraFacing.Front) {
            a = this.a(a);
            b = true;
        }
        tuSDKFilterPicture.setOutputRotation(a);
        this.a(array, interfaceOrientation.getDegree(), b);
        tuSDKFilterPicture.mountAtGLThread(new Runnable() {
            @Override
            public void run() {
                if (filterWrap instanceof SelesParameters.FilterFacePositionInterface) {
                    ((SelesParameters.FilterFacePositionInterface)filterWrap).updateFaceFeatures(array, TuSdkImageEngineImpl.this.b.getDeviceAngle());
                }
            }
        });
        filterWrap.addOrgin((SelesOutput)tuSDKFilterPicture);
        tuSDKFilterPicture.processImage();
        this.a(tuSDKFilterPicture.bufferFromCurrentlyProcessedOutput(), tuSDKFilterPicture.outputImageSize());
    }
    
    private void a(final IntBuffer intBuffer, final TuSdkSize tuSdkSize) {
        this.d = false;
        ThreadHelper.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (TuSdkImageEngineImpl.this.e != null) {
                    TuSdkImageEngineImpl.this.e.onPictureDataCompleted(intBuffer, tuSdkSize);
                }
            }
        });
    }
    
    public int getExifOrientation(final InterfaceOrientation interfaceOrientation) {
        if (interfaceOrientation == InterfaceOrientation.Portrait) {
            return interfaceOrientation.isTransposed() ? 2 : 1;
        }
        if (interfaceOrientation == InterfaceOrientation.LandscapeLeft) {
            return interfaceOrientation.isTransposed() ? 7 : 8;
        }
        if (interfaceOrientation == InterfaceOrientation.LandscapeRight) {
            return interfaceOrientation.isTransposed() ? 5 : 6;
        }
        return interfaceOrientation.isTransposed() ? 4 : 3;
    }
    
    private void a(final FaceAligment[] array, final TuSdkSize tuSdkSize, final TuSdkSize tuSdkSize2) {
        if (array == null || array.length == 0) {
            return;
        }
        final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(tuSdkSize, new Rect(0, 0, tuSdkSize2.width, tuSdkSize2.height));
        for (int i = 0; i < array.length; ++i) {
            final FaceAligment faceAligment = array[i];
            for (int j = 0; j < faceAligment.getMarks().length; ++j) {
                final PointF pointF = faceAligment.getMarks()[j];
                pointF.x = (pointF.x * rectWithAspectRatioInsideRect.width() + rectWithAspectRatioInsideRect.left) / tuSdkSize2.width;
                pointF.y = (pointF.y * rectWithAspectRatioInsideRect.height() + rectWithAspectRatioInsideRect.top) / tuSdkSize2.height;
            }
        }
    }
    
    private void a(final FaceAligment[] array, final int n, final boolean b) {
        if (array == null || array.length == 0) {
            return;
        }
        if (n <= 0 && !b) {
            return;
        }
        final PointF pointF = new PointF(0.0f, 0.0f);
        for (int i = 0; i < array.length; ++i) {
            final FaceAligment faceAligment = array[i];
            for (int j = 0; j < faceAligment.getMarks().length; ++j) {
                final PointF pointF2 = faceAligment.getMarks()[j];
                pointF.x = pointF2.x;
                pointF.y = pointF2.y;
                if (n == 180) {
                    pointF2.x = 1.0f - pointF.x;
                    pointF2.y = 1.0f - pointF.y;
                }
                else if (n == 90) {
                    pointF2.x = 1.0f - pointF.y;
                    pointF2.y = pointF.x;
                }
                else if (n == 270) {
                    pointF2.x = pointF.y;
                    pointF2.y = 1.0f - pointF.x;
                }
                if (b) {
                    pointF2.x = 1.0f - pointF2.x;
                }
            }
        }
    }
    
    static {
        f = new int[][] { { 270, 180, 90, 0 }, { 180, 90, 0, 270 }, { 90, 0, 270, 180 }, { 0, 270, 180, 90 } };
        g = new int[][] { { 90, 0, 270, 180 }, { 180, 90, 0, 270 }, { 270, 180, 90, 0 }, { 0, 270, 180, 90 } };
    }
}
