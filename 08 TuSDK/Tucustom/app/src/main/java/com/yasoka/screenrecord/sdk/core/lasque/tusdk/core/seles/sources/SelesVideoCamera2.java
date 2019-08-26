// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import android.annotation.SuppressLint;
import android.hardware.camera2.CameraDevice;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.hardware.camera2.CameraAccessException;
import android.graphics.SurfaceTexture;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.TLog;
import android.content.Context;
//import org.lasque.tusdk.core.utils.hardware.Camera2Helper;
import android.view.Surface;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
import android.os.HandlerThread;
import android.os.Handler;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.Camera2Helper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(21)
public abstract class SelesVideoCamera2 extends SelesVideoCamera2Base
{
    private CameraManager b;
    private String c;
    private CameraCharacteristics d;
    protected final Handler mHandler;
    private HandlerThread e;
    private CameraConfigs.CameraFacing f;
    private Surface g;
    private SelesVideoCamera2Engine h;
    
    public String getCameraId() {
        return this.c;
    }
    
    public CameraCharacteristics getCameraCharacter() {
        return this.d;
    }
    
    public Surface getPreviewSurface() {
        return this.g;
    }
    
    public CameraConfigs.CameraFacing cameraPosition() {
        return Camera2Helper.cameraPosition(this.getCameraCharacter());
    }
    
    public boolean isFrontFacingCameraPresent() {
        return this.cameraPosition() == CameraConfigs.CameraFacing.Front;
    }
    
    public boolean isBackFacingCameraPresent() {
        return this.cameraPosition() == CameraConfigs.CameraFacing.Back;
    }
    
    public SelesVideoCamera2(final Context context, final CameraConfigs.CameraFacing cameraFacing) {
        super(context);
        this.h = new SelesVideoCamera2Engine() {
            @Override
            public boolean canInitCamera() {
                SelesVideoCamera2.this.c = Camera2Helper.firstCameraId(SelesVideoCamera2.this.getContext(), SelesVideoCamera2.this.f);
                if (SelesVideoCamera2.this.c == null) {
                    TLog.e("The device can not find any camera2 info: %s", SelesVideoCamera2.this.f);
                    return false;
                }
                return true;
            }
            
            @Override
            public boolean onInitCamera() {
                SelesVideoCamera2.this.d = Camera2Helper.cameraCharacter(SelesVideoCamera2.this.b, SelesVideoCamera2.this.c);
                if (SelesVideoCamera2.this.d == null) {
                    TLog.e("The device can not find init camera2: %s", SelesVideoCamera2.this.c);
                    return false;
                }
                SelesVideoCamera2.this.onInitConfig(SelesVideoCamera2.this.d);
                return true;
            }
            
            @Override
            public TuSdkSize previewOptimalSize() {
                return SelesVideoCamera2.this.computerPreviewOptimalSize();
            }
            
            @SuppressLint("MissingPermission")
            @Override
            public void onCameraWillOpen(SurfaceTexture var1) {
                if (var1 != null) {
                    SelesVideoCamera2.this.g = new Surface(var1);

                    try {
                        SelesVideoCamera2.this.b.openCamera(SelesVideoCamera2.this.c, SelesVideoCamera2.this.getCameraStateCallback(), SelesVideoCamera2.this.mHandler);
                    } catch (CameraAccessException var3) {
                        TLog.e(var3, "SelesVideoCamera2 asyncInitCamera", new Object[0]);
                    }

                }
            }
            
            @Override
            public void onCameraStarted() {
            }
            
            @Override
            public ImageOrientation previewOrientation() {
                return SelesVideoCamera2.this.b();
            }
        };
        this.f = ((cameraFacing == null) ? CameraConfigs.CameraFacing.Back : cameraFacing);
        this.b = Camera2Helper.cameraManager(context);
        (this.e = new HandlerThread("TuSDK_L_Camera")).start();
        this.mHandler = new Handler(this.e.getLooper());
        super.setCameraEngine(this.h);
    }
    
    @Deprecated
    @Override
    public void setCameraEngine(final SelesVideoCamera2Engine selesVideoCamera2Engine) {
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.a();
    }
    
    private void a() {
        if (this.e == null) {
            return;
        }
        try {
            this.e.quitSafely();
            this.e.join();
            this.e = null;
        }
        catch (InterruptedException ex) {
            TLog.e(ex, "release Handler error", new Object[0]);
        }
    }
    
    protected void onInitConfig(final CameraCharacteristics cameraCharacteristics) {
    }
    
    @Override
    protected void onCameraStarted() {
        super.onCameraStarted();
    }
    
    public void rotateCamera() {
        final int cameraCounts = Camera2Helper.cameraCounts(this.getContext());
        if (!this.isCapturing() || cameraCounts < 2) {
            return;
        }
        this.f = ((this.f == CameraConfigs.CameraFacing.Front) ? CameraConfigs.CameraFacing.Back : CameraConfigs.CameraFacing.Front);
        this.startCameraCapture();
    }
    
    protected abstract CameraDevice.StateCallback getCameraStateCallback();
    
    protected abstract TuSdkSize computerPreviewOptimalSize();
    
    private ImageOrientation b() {
        return computerOutputOrientation(this.getContext(), this.d, this.isHorizontallyMirrorRearFacingCamera(), this.isHorizontallyMirrorFrontFacingCamera(), this.getOutputImageOrientation());
    }
    
    public static ImageOrientation computerOutputOrientation(final Context context, final CameraCharacteristics cameraCharacteristics, final boolean b, final boolean b2, final InterfaceOrientation interfaceOrientation) {
        return computerOutputOrientation(cameraCharacteristics, ContextUtils.getInterfaceRotation(context), b, b2, interfaceOrientation);
    }

    public static ImageOrientation computerOutputOrientation(CameraCharacteristics var0, InterfaceOrientation var1, boolean var2, boolean var3, InterfaceOrientation var4) {
        if (var1 == null) {
            var1 = InterfaceOrientation.Portrait;
        }

        if (var4 == null) {
            var4 = InterfaceOrientation.Portrait;
        }

        int var5 = 0;
        boolean var6 = true;
        if (var0 != null) {
            var5 = (Integer)var0.get(CameraCharacteristics.SENSOR_ORIENTATION);
            var6 = (Integer)var0.get(CameraCharacteristics.LENS_FACING) == 1;
        }

        int var7 = var1.getDegree();
        if (var4 != null) {
            var7 += var4.getDegree();
        }

        InterfaceOrientation var8;
        if (var6) {
            var8 = InterfaceOrientation.getWithDegrees(var5 - var7);
            if (var2) {
                switch(var8.ordinal()) {
                    case 1:
                        return ImageOrientation.DownMirrored;
                    case 2:
                        return ImageOrientation.LeftMirrored;
                    case 3:
                        return ImageOrientation.RightMirrored;
                    case 4:
                    default:
                        return ImageOrientation.UpMirrored;
                }
            } else {
                switch(var8.ordinal()) {
                    case 1:
                        return ImageOrientation.Up;
                    case 2:
                        return ImageOrientation.Left;
                    case 3:
                        return ImageOrientation.Right;
                    case 4:
                    default:
                        return ImageOrientation.Down;
                }
            }
        } else {
            var8 = InterfaceOrientation.getWithDegrees(var5 + var7);
            if (var3) {
                switch(var8.ordinal()) {
                    case 1:
                        return ImageOrientation.UpMirrored;
                    case 2:
                        return ImageOrientation.LeftMirrored;
                    case 3:
                        return ImageOrientation.RightMirrored;
                    case 4:
                    default:
                        return ImageOrientation.DownMirrored;
                }
            } else {
                switch(var8.ordinal()) {
                    case 1:
                        return ImageOrientation.Down;
                    case 2:
                        return ImageOrientation.Left;
                    case 3:
                        return ImageOrientation.Right;
                    case 4:
                    default:
                        return ImageOrientation.Up;
                }
            }
        }
    }
}
