// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.os.Build;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.api.TuSDKPostProcessJNI;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaMetadataRetriever;
//import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
//import org.lasque.tusdk.core.TuSdkContext;
import android.text.TextUtils;
import android.media.MediaFormat;
import java.util.Map;
import android.net.Uri;
import android.content.Context;
import android.media.MediaExtractor;
import java.io.IOException;
import android.media.MediaMuxer;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.TuSDKPostProcessJNI;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKVideoInfo;

@TargetApi(18)
public class TuSDKMediaUtils
{
    public static final String HEVC_MIMETYPE = "video/hevc";
    
    public static boolean isHEVCSupported() {
        for (int codecCount = MediaCodecList.getCodecCount(), i = 0; i < codecCount; ++i) {
            final MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (codecInfo.isEncoder()) {
                final String[] supportedTypes = codecInfo.getSupportedTypes();
                for (int j = 0; j < supportedTypes.length; ++j) {
                    if (supportedTypes[j].equalsIgnoreCase("video/hevc")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static MediaMuxer createMuxer(final String s, final int n) {
        try {
            return new MediaMuxer(s, n);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static MediaExtractor createExtractor(final String dataSource) {
        final MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(dataSource);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return mediaExtractor;
    }
    
    public static MediaExtractor createExtractor(final Context context, final Uri uri) {
        if (context == null || uri == null) {
            return null;
        }
        final MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(context, uri, (Map)null);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return mediaExtractor;
    }
    
    public static MediaFormat getVideoFormat(final MediaExtractor mediaExtractor) {
        for (int i = 0; i < mediaExtractor.getTrackCount(); ++i) {
            if (isVideoFormat(mediaExtractor.getTrackFormat(i))) {
                return mediaExtractor.getTrackFormat(i);
            }
        }
        return null;
    }
    
    public static MediaFormat getVideoFormat(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        if (tuSDKMediaDataSource == null || !tuSDKMediaDataSource.isValid()) {
            return null;
        }
        final MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            if (!TextUtils.isEmpty((CharSequence)tuSDKMediaDataSource.getFilePath())) {
                mediaExtractor.setDataSource(tuSDKMediaDataSource.getFilePath());
            }
            else {
                mediaExtractor.setDataSource(TuSdkContext.context(), tuSDKMediaDataSource.getFileUri(), (Map)null);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        final MediaFormat videoFormat = getVideoFormat(mediaExtractor);
        mediaExtractor.release();
        return videoFormat;
    }
    
    public static MediaFormat getAudioFormat(final MediaExtractor mediaExtractor) {
        for (int i = 0; i < mediaExtractor.getTrackCount(); ++i) {
            if (isAudioFormat(mediaExtractor.getTrackFormat(i))) {
                return mediaExtractor.getTrackFormat(i);
            }
        }
        return null;
    }
    
    public static TuSDKVideoInfo getVideoInfo(final String s) {
        return getVideoInfo(new TuSDKMediaDataSource(s));
    }
    
    public static TuSDKVideoInfo getVideoInfo(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        if (tuSDKMediaDataSource == null || !tuSDKMediaDataSource.isValid()) {
            return null;
        }
        final TuSDKVideoInfo withMediaFormat = TuSDKVideoInfo.createWithMediaFormat(getVideoFormat(tuSDKMediaDataSource), true);
        if (withMediaFormat == null) {
            return null;
        }
        final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        if (!TextUtils.isEmpty((CharSequence)tuSDKMediaDataSource.getFilePath())) {
            mediaMetadataRetriever.setDataSource(tuSDKMediaDataSource.getFilePath());
        }
        else {
            mediaMetadataRetriever.setDataSource(TuSdkContext.context(), tuSDKMediaDataSource.getFileUri());
        }
        if (withMediaFormat.degree <= 0) {
            final String metadata = mediaMetadataRetriever.extractMetadata(24);
            if (!TextUtils.isEmpty((CharSequence)metadata)) {
                withMediaFormat.setVideoRotation(Integer.parseInt(metadata));
            }
        }
        if (withMediaFormat.bitrate <= 0) {
            final String metadata2 = mediaMetadataRetriever.extractMetadata(20);
            final int int1 = Integer.parseInt(metadata2);
            if (!TextUtils.isEmpty((CharSequence)metadata2)) {
                withMediaFormat.bitrate = ((int1 == 0) ? 3000 : int1);
            }
        }
        if (withMediaFormat.fps <= 0) {
            TLog.e(" Video frame rate is invalid. | %s", new Object[] { tuSDKMediaDataSource });
        }
        if (tuSDKMediaDataSource.getFile() != null && tuSDKMediaDataSource.getFile().exists()) {
            TuSDKPostProcessJNI.readVideoInfo(tuSDKMediaDataSource.getFile().getAbsolutePath(), withMediaFormat);
        }
        else {
            TuSDKPostProcessJNI.readVideoInfo(tuSDKMediaDataSource.getFilePath(), withMediaFormat);
        }
        final String metadata3 = mediaMetadataRetriever.extractMetadata(16);
        withMediaFormat.existAudioTrack = (!TextUtils.isEmpty((CharSequence)metadata3) && metadata3.equalsIgnoreCase("yes"));
        return withMediaFormat;
    }
    
    public static TuSDKVideoInfo getVideoInfo(final TuSdkMediaDataSource tuSdkMediaDataSource) {
        if (tuSdkMediaDataSource == null || !tuSdkMediaDataSource.isValid()) {
            return null;
        }
        final TuSDKVideoInfo withMediaFormat = TuSDKVideoInfo.createWithMediaFormat(getVideoFormat(tuSdkMediaDataSource), true);
        if (withMediaFormat == null) {
            return null;
        }
        final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        switch (tuSdkMediaDataSource.getMediaDataType().ordinal()) {
            case 1: {
                mediaMetadataRetriever.setDataSource(tuSdkMediaDataSource.getContext(), tuSdkMediaDataSource.getUri());
                break;
            }
            case 2: {
                if (tuSdkMediaDataSource.getRequestHeaders() == null) {
                    mediaMetadataRetriever.setDataSource(tuSdkMediaDataSource.getPath());
                    break;
                }
                mediaMetadataRetriever.setDataSource(tuSdkMediaDataSource.getPath(), tuSdkMediaDataSource.getRequestHeaders());
                break;
            }
            case 3: {
                mediaMetadataRetriever.setDataSource(tuSdkMediaDataSource.getFileDescriptor(), tuSdkMediaDataSource.getFileDescriptorOffset(), tuSdkMediaDataSource.getFileDescriptorLength());
                break;
            }
            case 4: {
                if (Build.VERSION.SDK_INT >= 23) {
                    mediaMetadataRetriever.setDataSource(tuSdkMediaDataSource.getMediaDataSource());
                    break;
                }
                TLog.e("Use MediaDataSource system version must be >= M", new Object[0]);
                break;
            }
        }
        if (withMediaFormat.degree <= 0) {
            final String metadata = mediaMetadataRetriever.extractMetadata(24);
            if (!TextUtils.isEmpty((CharSequence)metadata)) {
                withMediaFormat.setVideoRotation(Integer.parseInt(metadata));
            }
        }
        if (withMediaFormat.bitrate <= 0) {
            final String metadata2 = mediaMetadataRetriever.extractMetadata(20);
            if (!TextUtils.isEmpty((CharSequence)metadata2)) {
                withMediaFormat.bitrate = Integer.parseInt(metadata2);
            }
        }
        if (withMediaFormat.fps <= 0) {
            TLog.e(" Video frame rate is invalid. | %s", new Object[] { tuSdkMediaDataSource });
        }
        if (tuSdkMediaDataSource.isValid()) {
            TuSDKPostProcessJNI.readVideoInfo(tuSdkMediaDataSource.getPath(), withMediaFormat);
        }
        else {
            TuSDKPostProcessJNI.readVideoInfo(tuSdkMediaDataSource.getPath(), withMediaFormat);
        }
        final String metadata3 = mediaMetadataRetriever.extractMetadata(16);
        withMediaFormat.existAudioTrack = (!TextUtils.isEmpty((CharSequence)metadata3) && metadata3.equalsIgnoreCase("yes"));
        return withMediaFormat;
    }
    
    public static MediaFormat getVideoFormat(final TuSdkMediaDataSource tuSdkMediaDataSource) {
        if (tuSdkMediaDataSource == null || !tuSdkMediaDataSource.isValid()) {
            return null;
        }
        final MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            switch (tuSdkMediaDataSource.getMediaDataType().ordinal()) {
                case 1: {
                    mediaExtractor.setDataSource(tuSdkMediaDataSource.getContext(), tuSdkMediaDataSource.getUri(), tuSdkMediaDataSource.getRequestHeaders());
                    break;
                }
                case 2: {
                    if (tuSdkMediaDataSource.getRequestHeaders() == null) {
                        mediaExtractor.setDataSource(tuSdkMediaDataSource.getPath());
                        break;
                    }
                    mediaExtractor.setDataSource(tuSdkMediaDataSource.getPath(), tuSdkMediaDataSource.getRequestHeaders());
                    break;
                }
                case 3: {
                    mediaExtractor.setDataSource(tuSdkMediaDataSource.getFileDescriptor(), tuSdkMediaDataSource.getFileDescriptorOffset(), tuSdkMediaDataSource.getFileDescriptorLength());
                    break;
                }
                case 4: {
                    if (Build.VERSION.SDK_INT >= 23) {
                        mediaExtractor.setDataSource(tuSdkMediaDataSource.getMediaDataSource());
                        break;
                    }
                    TLog.e("Use MediaDataSource system version must be >= M", new Object[0]);
                    break;
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        final MediaFormat videoFormat = getVideoFormat(mediaExtractor);
        mediaExtractor.release();
        return videoFormat;
    }
    
    public static MediaFormat getAudioFormat(final String dataSource) {
        if (TextUtils.isEmpty((CharSequence)dataSource)) {
            return null;
        }
        final MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(dataSource);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        final MediaFormat audioFormat = getAudioFormat(mediaExtractor);
        mediaExtractor.release();
        return audioFormat;
    }
    
    public static MediaFormat getAudioFormat(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        if (tuSDKMediaDataSource == null || !tuSDKMediaDataSource.isValid()) {
            return null;
        }
        final MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            if (!TextUtils.isEmpty((CharSequence)tuSDKMediaDataSource.getFilePath())) {
                mediaExtractor.setDataSource(tuSDKMediaDataSource.getFilePath());
            }
            else {
                mediaExtractor.setDataSource(TuSdkContext.context(), tuSDKMediaDataSource.getFileUri(), (Map)null);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        final MediaFormat audioFormat = getAudioFormat(mediaExtractor);
        mediaExtractor.release();
        return audioFormat;
    }
    
    public static MediaFormat getAudioFormat(final TuSdkMediaDataSource tuSdkMediaDataSource) {
        if (tuSdkMediaDataSource == null || !tuSdkMediaDataSource.isValid()) {
            return null;
        }
        final MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            if (!TextUtils.isEmpty((CharSequence)tuSdkMediaDataSource.getPath())) {
                mediaExtractor.setDataSource(tuSdkMediaDataSource.getPath());
            }
            else {
                mediaExtractor.setDataSource(TuSdkContext.context(), tuSdkMediaDataSource.getUri(), (Map)null);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        final MediaFormat audioFormat = getAudioFormat(mediaExtractor);
        mediaExtractor.release();
        return audioFormat;
    }
    
    public static int getAndSelectVideoTrackIndex(final MediaExtractor mediaExtractor) {
        for (int i = 0; i < mediaExtractor.getTrackCount(); ++i) {
            if (isVideoFormat(mediaExtractor.getTrackFormat(i))) {
                mediaExtractor.selectTrack(i);
                return i;
            }
        }
        return -1;
    }
    
    public static int getAndSelectAudioTrackIndex(final MediaExtractor mediaExtractor) {
        for (int i = 0; i < mediaExtractor.getTrackCount(); ++i) {
            if (isAudioFormat(mediaExtractor.getTrackFormat(i))) {
                mediaExtractor.selectTrack(i);
                return i;
            }
        }
        return -1;
    }
    
    public static int getVideoTrack(final MediaExtractor mediaExtractor) {
        for (int trackCount = mediaExtractor.getTrackCount(), i = 0; i < trackCount; ++i) {
            if (mediaExtractor.getTrackFormat(i).getString("mime").startsWith("video/")) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getAudioTrack(final MediaExtractor mediaExtractor) {
        for (int trackCount = mediaExtractor.getTrackCount(), i = 0; i < trackCount; ++i) {
            if (mediaExtractor.getTrackFormat(i).getString("mime").startsWith("audio/")) {
                return i;
            }
        }
        return -1;
    }
    
    public static MediaFormat createVideoFormat(final String s, final int n, final int n2) {
        return MediaFormat.createVideoFormat(s, n, n2);
    }
    
    public static MediaFormat createAudioFormat(final String s, final int n, final int n2) {
        return MediaFormat.createAudioFormat(s, n, n2);
    }
    
    public static boolean isVideoFormat(final MediaFormat mediaFormat) {
        return getMimeTypeFor(mediaFormat).startsWith("video/");
    }
    
    public static boolean isAudioFormat(final MediaFormat mediaFormat) {
        return getMimeTypeFor(mediaFormat).startsWith("audio/");
    }
    
    public static String getMimeTypeFor(final MediaFormat mediaFormat) {
        return mediaFormat.getString("mime");
    }
    
    public static boolean isSameVideoFormat(final MediaFormat mediaFormat, final MediaFormat mediaFormat2) {
        if (mediaFormat == null || mediaFormat2 == null) {
            return false;
        }
        final String string = mediaFormat.getString("mime");
        final int integer = mediaFormat.getInteger("width");
        final int integer2 = mediaFormat.getInteger("height");
        final String string2 = mediaFormat2.getString("mime");
        final int integer3 = mediaFormat2.getInteger("width");
        final int integer4 = mediaFormat2.getInteger("height");
        return string.equals(string2) && integer == integer3 && integer2 == integer4;
    }
    
    public static boolean isSameAudioFormat(final MediaFormat mediaFormat, final MediaFormat mediaFormat2) {
        if (mediaFormat == null || mediaFormat2 == null) {
            return false;
        }
        final String string = mediaFormat.getString("mime");
        final int integer = mediaFormat.getInteger("sample-rate");
        final String string2 = mediaFormat2.getString("mime");
        final int integer2 = mediaFormat2.getInteger("sample-rate");
        return string.equals(string2) && integer == integer2;
    }
    
    public static long getSafePts(final long l, final long i) {
        if (l >= i) {
            TLog.e("Calibrate the video timestamp prevTime : %d  nextTime : %d   After the calibration : %d", new Object[] { l, i, l + 9643L });
            return l + 9643L;
        }
        return i;
    }
    
    public static boolean containsKeyFrameRate(final MediaFormat mediaFormat) {
        return mediaFormat != null && mediaFormat.containsKey("frame-rate");
    }
    
    public static int getVideoFps(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return 0;
        }
        if (!isVideoFormat(mediaFormat)) {
            TLog.e("Is not a video format", new Object[0]);
        }
        if (mediaFormat.containsKey("frame-rate")) {
            return mediaFormat.getInteger("frame-rate");
        }
        return 0;
    }
    
    public static int getAudioSampleRate(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return 0;
        }
        if (!isAudioFormat(mediaFormat)) {
            TLog.e("Is not a audio format", new Object[0]);
        }
        if (mediaFormat.containsKey("sample-rate")) {
            return mediaFormat.getInteger("sample-rate");
        }
        return 0;
    }
    
    public static long getVideoDefaultInterval() {
        return getVideoInterval(10);
    }
    
    public static long getVideoInterval(final int n) {
        if (n <= 0) {
            return 0L;
        }
        return 1000000 / n;
    }
    
    public static long getVideoInterval(final MediaFormat mediaFormat) {
        return getVideoInterval(getVideoFps(mediaFormat));
    }
    
    public static long getVideoFrameIntervalTimeUs(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        if (tuSDKMediaDataSource == null || !tuSDKMediaDataSource.isValid()) {
            return 0L;
        }
        boolean b = false;
        final MediaExtractor mediaExtractor = new MediaExtractor();
        long n = 0L;
        long n2 = 0L;
        try {
            if (!TextUtils.isEmpty((CharSequence)tuSDKMediaDataSource.getFilePath())) {
                mediaExtractor.setDataSource(tuSDKMediaDataSource.getFilePath());
            }
            else {
                mediaExtractor.setDataSource(TuSdkContext.context(), tuSDKMediaDataSource.getFileUri(), (Map)null);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        final int trackCount = mediaExtractor.getTrackCount();
        int capacity = -1;
        for (int i = 0; i < trackCount; ++i) {
            final MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
            if (trackFormat.getString("mime").startsWith("video/")) {
                mediaExtractor.selectTrack(i);
                if (trackFormat.containsKey("max-input-size")) {
                    final int integer = trackFormat.getInteger("max-input-size");
                    capacity = ((integer > capacity) ? integer : capacity);
                }
                b = true;
            }
        }
        if (!b) {
            return 0L;
        }
        if (capacity < 0) {
            capacity = 1048576;
        }
        final ByteBuffer allocate = ByteBuffer.allocate(capacity);
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        long n3 = 0L;
        while (true) {
            bufferInfo.offset = 0;
            bufferInfo.size = mediaExtractor.readSampleData(allocate, 0);
            if (bufferInfo.size < 0) {
                break;
            }
            final long sampleTime = mediaExtractor.getSampleTime();
            if (n == 0L) {
                n = sampleTime;
            }
            n2 = sampleTime;
            ++n3;
            mediaExtractor.advance();
        }
        bufferInfo.size = 0;
        if (n3 == 0L) {
            return 0L;
        }
        return (n2 - n) / n3;
    }
    
    public static long getAudioDefaultInterval() {
        return getAudioInterval(1024, 44100);
    }
    
    public static long getAudioInterval(final int n, final int n2) {
        if (n <= 0 || n2 <= 0) {
            return getAudioDefaultInterval();
        }
        return n * 1000000 / n2;
    }
    
    public static long getAudioInterval(final int n, final MediaFormat mediaFormat) {
        return getAudioInterval(n, getAudioSampleRate(mediaFormat));
    }
    
    public static MediaCodecInfo getEncoderCodecInfo(final String anotherString) {
        for (int codecCount = MediaCodecList.getCodecCount(), i = 0; i < codecCount; ++i) {
            final MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (codecInfo.isEncoder()) {
                final String[] supportedTypes = codecInfo.getSupportedTypes();
                for (int j = 0; j < supportedTypes.length; ++j) {
                    if (supportedTypes[j].equalsIgnoreCase(anotherString)) {
                        return codecInfo;
                    }
                }
            }
        }
        return null;
    }
    
    @TargetApi(21)
    public static boolean isVideoSizeSupported(final TuSdkSize tuSdkSize, final String s) {
        if (Build.VERSION.SDK_INT < 21) {
            return false;
        }
        final MediaCodecInfo encoderCodecInfo = getEncoderCodecInfo(s);
        if (encoderCodecInfo == null) {
            return false;
        }
        final String[] supportedTypes = encoderCodecInfo.getSupportedTypes();
        for (int length = supportedTypes.length, i = 0; i < length; ++i) {
            final MediaCodecInfo.VideoCapabilities videoCapabilities = encoderCodecInfo.getCapabilitiesForType(supportedTypes[i]).getVideoCapabilities();
            if (videoCapabilities != null && videoCapabilities.isSizeSupported(tuSdkSize.width, tuSdkSize.height)) {
                return true;
            }
        }
        final TuSdkSize displaySize = TuSdkContext.getDisplaySize();
        return tuSdkSize.width <= displaySize.width && tuSdkSize.height <= displaySize.height;
    }
}
