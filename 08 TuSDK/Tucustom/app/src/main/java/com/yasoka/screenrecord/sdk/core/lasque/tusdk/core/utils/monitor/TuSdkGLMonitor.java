// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.monitor;

import android.os.Environment;
import java.util.Date;
import java.text.SimpleDateFormat;
//import org.lasque.tusdk.core.utils.JVMUtils;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.graphics.Bitmap;
import java.nio.Buffer;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.TLog;
import android.opengl.GLES20;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.JVMUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkThreadExecutor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
//import org.lasque.tusdk.core.utils.TuSdkThreadExecutor;
import java.io.FileOutputStream;

public class TuSdkGLMonitor
{
    private static final String a;
    private boolean b;
    private boolean c;
    private FileOutputStream d;
    private TuSdkThreadExecutor e;
    private volatile AtomicLong f;
    private int g;
    private int h;
    private String i;
    private Object j;
    
    public TuSdkGLMonitor(final TuSdkThreadExecutor e) {
        this.b = false;
        this.c = false;
        this.f = new AtomicLong();
        this.g = 60;
        this.h = 30;
        this.j = new Object();
        this.e = e;
        if (this.e == null) {
            this.e = new TuSdkThreadExecutor();
        }
    }
    
    public void setEnableCheckGLError(final boolean b) {
        if (this.b == b) {
            return;
        }
        this.b = b;
        this.f.set(0L);
        if (this.d != null) {
            try {
                this.d.flush();
                this.d.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (b) {
            try {
                if (this.i == null) {
                    this.i = TuSdkGLMonitor.a + this.b();
                }
                final File file = new File(this.i);
                if (!file.exists()) {
                    file.mkdirs();
                }
                this.d = new FileOutputStream(new File(this.i, this.b() + ".log"));
            }
            catch (FileNotFoundException ex2) {
                ex2.printStackTrace();
            }
        }
    }
    
    public void setEnableCheckFrameImage(final boolean c) {
        this.c = c;
        if (this.i == null) {
            this.i = TuSdkGLMonitor.a + this.b();
        }
        final File file = new File(this.i + "/IMG/");
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    
    public void checkGL(final String s) {
        if (!this.c) {
            return;
        }
        this.a(s);
        this.b(s);
    }
    
    private void a(final String s) {
        final int glGetError = GLES20.glGetError();
        if (glGetError == 0) {
            return;
        }
        try {
            this.c(String.format("%s : %s  glError: 0x%s  \n", this.b(), s, Integer.toHexString(glGetError)));
        }
        catch (Exception ex) {}
    }
    
    private void b(final String s) {
        final int glCheckFramebufferStatus = GLES20.glCheckFramebufferStatus(36160);
        if (glCheckFramebufferStatus == 36053) {
            return;
        }
        this.c(String.format("%s : %s framebuffer error:[0x%s]", this.b(), s, Integer.toHexString(glCheckFramebufferStatus)));
    }
    
    public void checkGLFrameImage(final String str, final int n, final int n2) {
        if (!this.c) {
            return;
        }
        try {
            if (this.f.getAndIncrement() == Long.MAX_VALUE) {
                this.f.set(0L);
            }
            if (this.f.get() % this.g != 0L) {
                return;
            }
            final long value = this.f.get();
            synchronized (this.j) {
                this.checkGL("[checkFramImage] :" + str);
                TLog.dump("[checkFramImage]  capture filter image : [%s]  MemoryUse: %s", str, a());
                final IntBuffer allocate = IntBuffer.allocate(n * n2);
                GLES20.glReadPixels(0, 0, n, n2, 6408, 5121, (Buffer)allocate);
                this.e.exec(new Runnable() {
                    final /* synthetic */ boolean f = TuSdkGLMonitor.this.c();
                    
                    @Override
                    public void run() {
                        synchronized (TuSdkGLMonitor.this.j) {
                            try {
                                final Bitmap bitmap = Bitmap.createBitmap(n, n2, Bitmap.Config.ARGB_8888);
                                if (bitmap == null) {
                                    TuSdkGLMonitor.this.c(String.format(" %s :%s checkGLFrameImage return null \n", str, TuSdkGLMonitor.this.b()));
                                    return;
                                }
                                bitmap.copyPixelsFromBuffer((Buffer)allocate);
                                final File file = new File(new File(TuSdkGLMonitor.this.i + "/IMG/"), str + "_" + value + "_" + TuSdkGLMonitor.this.b() + ".png");
                                BitmapHelper.saveBitmap(file, bitmap, TuSdkGLMonitor.this.h);
                                bitmap.recycle();
                                if (this.f) {
                                    final String format = String.format(" %s :%s  File: %s \n", str, StringHelper.timeStampString(), file.getAbsolutePath());
                                    TuSdkGLMonitor.this.checkGL(str);
                                    TuSdkGLMonitor.this.c(format);
                                }
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        catch (Exception ex) {
            if (ex != null) {
                TLog.e(ex);
            }
        }
    }
    
    private static String a() {
        final float[] memoryInfo = JVMUtils.getMemoryInfo();
        return "Memory : " + memoryInfo[0] + "_" + memoryInfo[1] + "_" + memoryInfo[2];
    }
    
    private String b() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss-SSS").format(new Date());
    }
    
    private boolean c() {
        return GLES20.glGetError() != 0 || GLES20.glCheckFramebufferStatus(36160) != 36053;
    }
    
    private void c(final String s) {
        if (s == null || this.d == null) {
            return;
        }
        this.e.exec(new Runnable() {
            @Override
            public void run() {
                synchronized (TuSdkGLMonitor.this.j) {
                    final String a = s;
                    try {
                        TuSdkGLMonitor.this.d.write(a.getBytes());
                        TuSdkGLMonitor.this.d.write("\n".getBytes());
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    
    static {
        a = Environment.getExternalStorageDirectory().getPath() + "/log/TuSdk/GLMonitor/";
    }
}
