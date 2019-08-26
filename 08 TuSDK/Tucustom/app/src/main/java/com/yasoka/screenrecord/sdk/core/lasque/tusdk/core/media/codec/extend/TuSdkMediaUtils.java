// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import android.media.MediaFormat;
import android.media.MediaCodec;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;

@TargetApi(18)
public class TuSdkMediaUtils
{
    public static final long CODEC_TIMEOUT_US = 10000L;
    
    public static int getMediaTrackIndex(final TuSdkMediaExtractor tuSdkMediaExtractor, String lowerCase) {
        int n = -1;
        if (lowerCase == null) {
            return n;
        }
        lowerCase = lowerCase.toLowerCase();
        for (int i = 0; i < tuSdkMediaExtractor.getTrackCount(); ++i) {
            if (tuSdkMediaExtractor.getTrackFormat(i).getString("mime").toLowerCase().startsWith(lowerCase)) {
                n = i;
                break;
            }
        }
        return n;
    }
    
    public static boolean putBufferToCoderUntilEnd(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
        return putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, true);
    }
    
    public static boolean putBufferToCoderUntilEnd(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec, final boolean b) {
        return putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, b, -1L);
    }
    
    public static boolean putBufferToCoderUntilEnd(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec, final boolean b, final long n) {
        if (tuSdkMediaExtractor == null || tuSdkMediaCodec == null) {
            return true;
        }
        ByteBuffer[] inputBuffers;
        try {
            inputBuffers = tuSdkMediaCodec.getInputBuffers();
        }
        catch (Exception ex) {
            return true;
        }
        return putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, inputBuffers, 10000L, b, n);
    }
    
    public static boolean putBufferToCoderUntilEnd(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec, final ByteBuffer[] array, final long n) {
        return putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, array, n, true);
    }
    
    public static boolean putBufferToCoderUntilEnd(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec, final ByteBuffer[] array, final long n, final boolean b) {
        return putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, array, n, b, -1L);
    }
    
    public static boolean putBufferToCoderUntilEnd(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec, final ByteBuffer[] array, final long n, final boolean b, long n2) {
        final int dequeueInputBuffer = tuSdkMediaCodec.dequeueInputBuffer(n);
        if (dequeueInputBuffer < 0) {
            return false;
        }
        final int sampleData = tuSdkMediaExtractor.readSampleData(array[dequeueInputBuffer], 0);
        if (sampleData > 0) {
            n2 = ((n2 < 0L) ? tuSdkMediaExtractor.getSampleTime() : n2);
            tuSdkMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, sampleData, n2, 0);
            tuSdkMediaExtractor.advance();
            return false;
        }
        if (b) {
            tuSdkMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
        }
        else {
            tuSdkMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 0);
        }
        return true;
    }
    
    public static boolean putEosToCoder(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
        if (tuSdkMediaExtractor == null || tuSdkMediaCodec == null) {
            return true;
        }
        final int dequeueInputBuffer = tuSdkMediaCodec.dequeueInputBuffer(10000L);
        if (dequeueInputBuffer < 0) {
            return false;
        }
        tuSdkMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
        return true;
    }
    
    public static void processOutputBuffer(final TuSdkMediaMuxer tuSdkMediaMuxer, final int n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if ((bufferInfo.flags & 0x2) != 0x0) {
            return;
        }
        byteBuffer.position(bufferInfo.offset);
        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
        tuSdkMediaMuxer.writeSampleData(n, byteBuffer, bufferInfo);
    }
    
    public static void addADTStoPacket(final byte[] array, final int n, final MediaFormat mediaFormat) {
        if (array == null || mediaFormat == null) {
            return;
        }
        addADTStoPacket(array, n, TuSdkMediaFormat.getInteger(mediaFormat, "aac-profile", 0), samplingFrequencyIndex(TuSdkMediaFormat.getInteger(mediaFormat, "sample-rate", 0)), TuSdkMediaFormat.getInteger(mediaFormat, "channel-count", 0));
    }
    
    public static void addADTStoPacket(final byte[] array, final int n, final int n2, final int n3, final int n4) {
        array[0] = -1;
        array[1] = -7;
        array[2] = (byte)((n2 - 1 << 6) + (n3 << 2) + (n4 >> 2));
        array[3] = (byte)(((n4 & 0x3) << 6) + (n >> 11));
        array[4] = (byte)((n & 0x7FF) >> 3);
        array[5] = (byte)(((n & 0x7) << 5) + 31);
        array[6] = -4;
    }
    
    public static byte samplingFrequencyIndex(final int n) {
        switch (n) {
            case 96000: {
                return 0;
            }
            case 88200: {
                return 1;
            }
            case 64000: {
                return 2;
            }
            case 48000: {
                return 3;
            }
            case 44100: {
                return 4;
            }
            case 32000: {
                return 5;
            }
            case 24000: {
                return 6;
            }
            case 22050: {
                return 7;
            }
            case 16000: {
                return 8;
            }
            case 12000: {
                return 9;
            }
            case 11025: {
                return 10;
            }
            case 8000: {
                return 11;
            }
            case 7350: {
                return 12;
            }
            default: {
                return 15;
            }
        }
    }
    
    public static MediaCodec.BufferInfo cloneBufferInfo(final MediaCodec.BufferInfo bufferInfo) {
        if (bufferInfo == null) {
            return null;
        }
        final MediaCodec.BufferInfo bufferInfo2 = new MediaCodec.BufferInfo();
        bufferInfo2.set(bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
        return bufferInfo2;
    }
}
