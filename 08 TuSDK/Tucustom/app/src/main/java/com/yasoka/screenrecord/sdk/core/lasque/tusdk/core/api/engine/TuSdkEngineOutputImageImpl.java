// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine;

//import org.lasque.tusdk.core.secret.ColorSpaceConvert;
//import org.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.ColorSpaceConvert;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesOffscreen;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesTerminalFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ColorFormatType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.nio.IntBuffer;
import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.type.ColorFormatType;
import java.util.List;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.output.SelesOffscreen;
//import org.lasque.tusdk.core.seles.output.SelesTerminalFilter;

public class TuSdkEngineOutputImageImpl implements TuSdkEngineOutputImage
{
    private TuSdkEngineOrientation a;
    private SelesTerminalFilter b;
    private SelesOffscreen c;
    private boolean d;
    private TuSdkSize e;
    private ByteBuffer f;
    private List<ByteBuffer> g;
    private Object h;
    private ColorFormatType i;
    private SelesOffscreen.SelesOffscreenDelegate j;
    
    @Override
    public void setEngineRotation(final TuSdkEngineOrientation a) {
        this.a = a;
    }
    
    @Override
    public List<SelesContext.SelesInput> getInputs() {
        ArrayList var1 = new ArrayList(2);
        if (this.c != null) {
            var1.add(this.c);
        }

        if (this.b != null) {
            var1.add(this.b);
        }

        return var1;
    }

    public TuSdkEngineOutputImageImpl() {
        this.i = ColorFormatType.NV21;
        this.j = new SelesOffscreen.SelesOffscreenDelegate() {
            public boolean onFrameRendered(SelesOffscreen var1) {
                if (!TuSdkEngineOutputImageImpl.this.a()) {
                    return false;
                } else {
                    IntBuffer var2 = var1.renderBuffer();
                    List var3;
                    ByteBuffer var4;
                    synchronized(TuSdkEngineOutputImageImpl.this.h) {
                        var3 = TuSdkEngineOutputImageImpl.this.g;
                        var4 = TuSdkEngineOutputImageImpl.this.f;
                    }

                    if (var2 != null && var3 != null && var3.size() >= 1) {
                        ByteBuffer var5 = (ByteBuffer)var3.remove(0);
                        var5.position(0);
                        byte[] var6 = var5.array();
                        TuSdkEngineOutputImageImpl.this.a(var2.array(), var6, TuSdkEngineOutputImageImpl.this.i);
                        synchronized(TuSdkEngineOutputImageImpl.this.h) {
                            TuSdkEngineOutputImageImpl.this.f = var5;
                            if (var4 != null) {
                                var3.add(var4);
                            }

                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        };
        this.b = new SelesTerminalFilter();
    }
    
    @Override
    public void release() {
        if (this.b != null) {
            this.b.destroy();
            this.b = null;
        }
        if (this.c != null) {
            this.c.setDelegate(null);
            this.c.setEnabled(false);
            this.c.destroy();
            this.c = null;
        }
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
    
    @Override
    public void willProcessFrame(final long n) {
        if (this.a == null || this.b == null) {
            return;
        }
        this.b.setInputRotation(this.a.getOutputOrientation(), 0);
    }
    
    @Override
    public int getTerminalTexture() {
        if (this.b == null || this.b.framebufferForOutput() == null) {
            return -1;
        }
        return this.b.framebufferForOutput().getTexture();
    }
    
    @Override
    public void setYuvOutputImageFormat(final ColorFormatType i) {
        if (i == null) {
            return;
        }
        this.i = i;
    }
    
    @Override
    public void setEnableOutputYUVData(final boolean d) {
        this.d = d;
        if (this.d && this.c == null) {
            (this.c = new SelesOffscreen()).setDelegate(this.j);
        }
        if (this.d) {
            this.c.startWork();
        }
        else {
            this.c.setEnabled(false);
        }
    }
    
    @Override
    public void snatchFrame(final byte[] dst) {
        if (!this.a()) {
            return;
        }
        final ByteBuffer f;
        synchronized (this.h) {
            f = this.f;
        }
        if (f == null || dst.length != f.capacity()) {
            return;
        }
        this.c.setInputRotation(this.a.getYuvOutputOrienation(), 0);
        f.position(0);
        f.get(dst);
    }
    
    private boolean a() {
        if (!this.d || this.c == null) {
            return false;
        }
        if (this.a == null) {
            TLog.w("%s checkBuffer need setEngineRotation first.", "TuSdkEngineOutputImageImpl");
            return false;
        }
        if (this.a.getInputSize().equals(this.e) && this.g != null) {
            return true;
        }
        synchronized (this.h) {
            this.g = null;
            this.f = null;
            this.e = this.a.getInputSize();
            final ArrayList<ByteBuffer> g = new ArrayList<ByteBuffer>(3);
            final int capacity = this.e.width * this.e.height * 3 / 2;
            for (int i = 0; i < 3; ++i) {
                g.add(ByteBuffer.allocate(capacity));
            }
            this.g = g;
        }
        return true;
    }
    
    private void a(final int[] array, final byte[] array2, final ColorFormatType colorFormatType) {
        if (this.c == null) {
            return;
        }
        final TuSdkSize sizeOfFBO = this.c.sizeOfFBO();
        if (colorFormatType == ColorFormatType.NV21) {
            ColorSpaceConvert.rgbaToNv21(array, sizeOfFBO.width, sizeOfFBO.height, array2);
        }
        else if (colorFormatType == ColorFormatType.I420) {
            ColorSpaceConvert.rgbaToI420(array, sizeOfFBO.width, sizeOfFBO.height, array2);
        }
        else if (colorFormatType == ColorFormatType.YV12) {
            ColorSpaceConvert.rgbaToYv12(array, sizeOfFBO.width, sizeOfFBO.height, array2);
        }
        else {
            TLog.e("%s Unsupported image format", "TuSdkEngineOutputImageImpl");
        }
    }
}
