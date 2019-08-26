// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
//import org.lasque.tusdk.core.utils.ReflectUtils;
//import org.lasque.tusdk.core.utils.TLog;

public class TuSdkAudioConvertFactory
{
    public static TuSdkAudioConvert build(final TuSdkAudioInfo tuSdkAudioInfo, final TuSdkAudioInfo tuSdkAudioInfo2) {
        final TuSdkAudioConvertBase a = a(tuSdkAudioInfo);
        if (a == null) {
            TLog.d("%s build unsupport inputInfo: %s", "TuSdkAudioConvertFactory", tuSdkAudioInfo);
            return null;
        }
        final TuSdkAudioConvertBase a2 = a(tuSdkAudioInfo2);
        if (a2 == null) {
            TLog.d("%s build unsupport outputInfo: %s", "TuSdkAudioConvertFactory", tuSdkAudioInfo2);
        }
        else {
            a2.a(a, true);
        }
        return a2;
    }
    
    public static TuSdkAudioConvert build(final TuSdkAudioInfo tuSdkAudioInfo) {
        final TuSdkAudioConvertBase a = a(tuSdkAudioInfo);
        if (a == null) {
            TLog.d("%s build unsupport inputInfo: %s", "TuSdkAudioConvertFactory", tuSdkAudioInfo);
            return null;
        }
        return a;
    }
    
    private static TuSdkAudioConvertBase a(final TuSdkAudioInfo tuSdkAudioInfo) {
        if (tuSdkAudioInfo == null) {
            return null;
        }
        TuSdkAudioConvertBase tuSdkAudioConvertBase = null;
        Label_0153: {
            switch (tuSdkAudioInfo.bitWidth) {
                case 16: {
                    switch (tuSdkAudioInfo.channelCount) {
                        case 1: {
                            tuSdkAudioConvertBase = new TuSdkAudioConvertPCM16Mono();
                            break Label_0153;
                        }
                        case 2: {
                            tuSdkAudioConvertBase = new TuSdkAudioConvertPCM16Stereo();
                            break Label_0153;
                        }
                        default: {
                            break Label_0153;
                        }
                    }
                }
                case 8: {
                    switch (tuSdkAudioInfo.channelCount) {
                        case 1: {
                            tuSdkAudioConvertBase = new TuSdkAudioConvertPCM8Mono();
                            break Label_0153;
                        }
                        case 2: {
                            tuSdkAudioConvertBase = new TuSdkAudioConvertPCM8Stereo();
                            break Label_0153;
                        }
                        default: {
                            break Label_0153;
                        }
                    }
                }
            }
        }
        return tuSdkAudioConvertBase;
    }
    
    public abstract static class TuSdkAudioConvertBase implements TuSdkAudioConvert
    {
        protected TuSdkAudioConvert mInputConvert;
        protected boolean mNeedRestore;
        
        public TuSdkAudioConvertBase() {
            this.mNeedRestore = false;
        }
        
        private void a(final TuSdkAudioConvert tuSdkAudioConvert) {
            this.a(tuSdkAudioConvert, false);
        }
        
        private void a(TuSdkAudioConvert var1, boolean var2) {
            this.mInputConvert = var1;
            this.mNeedRestore = var2;
            if (var2 && var1 instanceof TuSdkAudioConvertFactory.TuSdkAudioConvertBase) {
                TuSdkAudioConvert var3 = (TuSdkAudioConvert)ReflectUtils.classInstance(this.getClass());
                ((TuSdkAudioConvertFactory.TuSdkAudioConvertBase)var1).a(var3);
            }

        }
        
        @Override
        public byte[] outputBytes(final byte[] array, final ByteOrder byteOrder, final int n, final int n2) {
            final byte[] array2 = new byte[n2];
            System.arraycopy(array, n, array2, 0, n2);
            return this.outputBytes(array2, byteOrder);
        }
        
        @Override
        public void inputReverse(final ByteBuffer byteBuffer, final ByteBuffer byteBuffer2) {
            this.mInputConvert.reverse(byteBuffer, byteBuffer2);
        }
        
        @Override
        public void restoreBytes(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
            this.mInputConvert.outputBytes(shortBuffer, byteBuffer, byteOrder);
        }
    }
}
