// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.detector;

import android.graphics.PointF;
//import org.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesOffscreenRotate;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

import org.lasque.tusdk.core.face.TuSdkFaceDetector;

import java.nio.Buffer;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.face.TuSdkFaceDetector;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.output.SelesOffscreenRotate;
//import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

public class FrameDetectProcessor
{
    private InterfaceOrientation a;
    private TuSdkOrientationEventListener b;
    private SelesOffscreenRotate c;
    private boolean d;
    private TuSdkSize e;
    private boolean f;
    private boolean g;
    private FrameDetectProcessorDelegate h;
    private TuSdkOrientationEventListener.TuSdkOrientationDelegate i;
    private SelesOffscreenRotate.SelesOffscreenRotateDelegate j;
    
    public FrameDetectProcessor() {
        this(0);
    }
    
    public FrameDetectProcessor(final int n) {
        this.a = InterfaceOrientation.Portrait;
        this.d = false;
        this.i = (TuSdkOrientationEventListener.TuSdkOrientationDelegate)new TuSdkOrientationEventListener.TuSdkOrientationDelegate() {
            public void onOrientationChanged(final InterfaceOrientation interfaceOrientation) {
                if (FrameDetectProcessor.this.h != null) {
                    FrameDetectProcessor.this.h.onOrientationChanged(interfaceOrientation);
                }
            }
        };
        this.j = (SelesOffscreenRotate.SelesOffscreenRotateDelegate)new SelesOffscreenRotate.SelesOffscreenRotateDelegate() {
            public boolean onFrameRendered(final SelesOffscreenRotate selesOffscreenRotate) {
                if (!FrameDetectProcessor.this.f) {
                    return true;
                }
                final float angle = selesOffscreenRotate.getAngle();
                selesOffscreenRotate.setAngle((float)FrameDetectProcessor.this.b());
                FrameDetectProcessor.this.a(selesOffscreenRotate.outputFrameSize(), angle, selesOffscreenRotate.readBuffer());
                return true;
            }
        };
        (this.b = new TuSdkOrientationEventListener(TuSdkContext.context())).setDelegate(this.i, (TuSdkOrientationEventListener.TuSdkOrientationDegreeDelegate)null);
        this.b.enable();
        ThreadHelper.runThread((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSdkFaceDetector.init();
                FrameDetectProcessor.this.g = true;
            }
        });
    }
    
    public FrameDetectProcessorDelegate getDelegate() {
        return this.h;
    }
    
    public void setDelegate(final FrameDetectProcessorDelegate h) {
        this.h = h;
    }
    
    public boolean inited() {
        return this.g;
    }
    
    public static void setDetectScale(final float detectScale) {
        TuSdkFaceDetector.setDetectScale(detectScale);
    }
    
    public void destroy() {
        this.h = null;
        if (this.b != null) {
            this.b.disable();
            this.b = null;
        }
        this.destroyOutput();
    }
    
    public void destroyOutput() {
        if (this.c == null) {
            return;
        }
        this.c.setDelegate((SelesOffscreenRotate.SelesOffscreenRotateDelegate)null);
        this.c.destroy();
        this.c = null;
    }
    
    public SelesOffscreenRotate getSelesRotateShotOutput() {
        if (this.c != null) {
            return this.c;
        }
        (this.c = new SelesOffscreenRotate()).setSyncOutput(this.d);
        this.a();
        this.c.setDelegate(this.j);
        return this.c;
    }
    
    public void setSyncOutput(final boolean b) {
        this.d = b;
        if (this.c != null) {
            this.c.setSyncOutput(b);
        }
    }
    
    public void setInputTextureSize(final TuSdkSize e) {
        if (e == null || e.equals((Object)this.e)) {
            return;
        }
        this.e = e;
        this.a();
    }
    
    private void a() {
        if (this.c == null) {
            return;
        }
        final int n = (this.e == null) ? 256 : this.e.maxSide();
        final int n2 = (n > 256) ? 256 : n;
        this.c.forceProcessingAtSize(TuSdkSize.create(n2, n2));
    }
    
    public void setEnabled(final boolean b) {
        this.f = b;
        if (this.c != null) {
            this.c.setEnabled(b);
        }
    }
    
    public void setInterfaceOrientation(final InterfaceOrientation a) {
        this.a = a;
    }
    
    public int getDeviceAngle() {
        return this.b.getDeviceAngle();
    }
    
    private int b() {
        if (this.a == null) {
            return this.getDeviceAngle();
        }
        return this.getDeviceAngle() + this.a.getDegree();
    }
    
    private void a(final TuSdkSize tuSdkSize, final float n, final Buffer buffer) {
        if (buffer == null) {
            return;
        }
        this.a();
    }
    
    private void a(final FaceAligment[] array, final TuSdkSize tuSdkSize, final float n, final boolean b) {
        if (this.h == null || this.e == null) {
            return;
        }
        if (array != null) {
            if (array.length >= 1) {
                final float ratioFloat = this.e.getRatioFloat();
                final TuSdkSize create = TuSdkSize.create(tuSdkSize);
                if (ratioFloat < 1.0f) {
                    create.width = (int)(tuSdkSize.height * ratioFloat);
                }
                else {
                    create.height = (int)(tuSdkSize.width / ratioFloat);
                }
                final float n2 = (tuSdkSize.width - create.width) * 0.5f;
                final float n3 = (tuSdkSize.height - create.height) * 0.5f;
                for (final FaceAligment faceAligment : array) {
                    faceAligment.rect.left = (faceAligment.rect.left * tuSdkSize.width - n2) / create.width;
                    faceAligment.rect.top = (faceAligment.rect.top * tuSdkSize.height - n3) / create.height;
                    faceAligment.rect.right = (faceAligment.rect.right * tuSdkSize.width - n2) / create.width;
                    faceAligment.rect.bottom = (faceAligment.rect.bottom * tuSdkSize.height - n3) / create.height;
                    if (faceAligment.getOrginMarks() != null) {
                        for (final PointF pointF : faceAligment.getOrginMarks()) {
                            if (ratioFloat < 1.0f) {
                                pointF.x = (pointF.x * tuSdkSize.width - n2) / create.width;
                            }
                            else {
                                pointF.y = (pointF.y * tuSdkSize.height - n3) / create.height;
                            }
                        }
                        faceAligment.setOrginMarks(faceAligment.getOrginMarks());
                    }
                }
            }
        }
        if (this.getDelegate() != null) {
            this.getDelegate().onFrameDetectedResult(array, tuSdkSize, n, b);
        }
    }

   /* public FaceAligment[] syncProcessFrameData(byte[] var1, TuSdkSize var2, int var3, double var4, boolean var6) {
        return !this.inited() ? null : TuSdkFaceDetector.markFaceGrayImage(var2.width, var2.height, var3, var4, var6, var1);
    }*/
    
    public interface FrameDetectProcessorDelegate extends TuSdkOrientationEventListener.TuSdkOrientationDelegate
    {
        void onFrameDetectedResult(final FaceAligment[] p0, final TuSdkSize p1, final float p2, final boolean p3);
    }
}
