// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.suit;

//import org.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkMediaVideoComposer;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
//import org.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkComposeItem;
import java.util.LinkedList;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorImpl;
//import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaFilesCuterImpl;
//import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaFilesCuter;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuterImpl;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuter;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import android.graphics.RectF;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileTranscoderImpl;
import java.util.ArrayList;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileTranscoder;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaMutableFilePlayerImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuterImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorPlayer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorPlayerImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFilePlayer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFilePlayerImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileTranscoder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileTranscoderImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkComposeItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkMediaVideoComposer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaFilesCuter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaFilesCuterImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaMutableFilePlayerImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.util.List;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFilePlayerImpl;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFilePlayer;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorPlayerImpl;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorPlayer;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public class TuSdkMediaSuit
{
    public static TuSdkMediaFileDirectorPlayer directorPlayer(final TuSdkMediaDataSource mediaDataSource, final boolean b, final TuSdkMediaPlayerListener listener) {
        final TuSdkMediaFileDirectorPlayerImpl tuSdkMediaFileDirectorPlayerImpl = new TuSdkMediaFileDirectorPlayerImpl();
        tuSdkMediaFileDirectorPlayerImpl.setMediaDataSource(mediaDataSource);
        tuSdkMediaFileDirectorPlayerImpl.setListener(listener);
        if (!tuSdkMediaFileDirectorPlayerImpl.load(b)) {
            return null;
        }
        return tuSdkMediaFileDirectorPlayerImpl;
    }
    
    public static TuSdkMediaFilePlayer playMedia(final TuSdkMediaDataSource mediaDataSource, final boolean b, final TuSdkMediaPlayerListener listener) {
        final TuSdkMediaFilePlayerImpl tuSdkMediaFilePlayerImpl = new TuSdkMediaFilePlayerImpl();
        tuSdkMediaFilePlayerImpl.setMediaDataSource(mediaDataSource);
        tuSdkMediaFilePlayerImpl.setListener(listener);
        if (!tuSdkMediaFilePlayerImpl.load(b)) {
            return null;
        }
        return tuSdkMediaFilePlayerImpl;
    }
    
    public static TuSdkMediaFilePlayer playMedia(final List<TuSdkMediaDataSource> mediaDataSources, final boolean b, final TuSdkMediaPlayerListener listener) {
        final TuSdkMediaMutableFilePlayerImpl tuSdkMediaMutableFilePlayerImpl = new TuSdkMediaMutableFilePlayerImpl();
        tuSdkMediaMutableFilePlayerImpl.setMediaDataSources(mediaDataSources);
        tuSdkMediaMutableFilePlayerImpl.setListener(listener);
        if (!tuSdkMediaMutableFilePlayerImpl.load(b)) {
            return null;
        }
        return tuSdkMediaMutableFilePlayerImpl;
    }
    
    public static TuSdkMediaFileTranscoder transcoding(final TuSdkMediaDataSource tuSdkMediaDataSource, final String s, final MediaFormat mediaFormat, final MediaFormat mediaFormat2, final TuSdkMediaProgress tuSdkMediaProgress) {
        final ArrayList<TuSdkMediaDataSource> list = new ArrayList<TuSdkMediaDataSource>(1);
        list.add(tuSdkMediaDataSource);
        return merge(list, s, mediaFormat, mediaFormat2, tuSdkMediaProgress);
    }
    
    public static TuSdkMediaFileTranscoder merge(final List<TuSdkMediaDataSource> list, final String outputFilePath, final MediaFormat outputVideoFormat, final MediaFormat outputAudioFormat, final TuSdkMediaProgress tuSdkMediaProgress) {
        final TuSdkMediaFileTranscoderImpl tuSdkMediaFileTranscoderImpl = new TuSdkMediaFileTranscoderImpl();
        tuSdkMediaFileTranscoderImpl.addInputDataSouces(list);
        tuSdkMediaFileTranscoderImpl.setOutputFilePath(outputFilePath);
        tuSdkMediaFileTranscoderImpl.setOutputVideoFormat(outputVideoFormat);
        tuSdkMediaFileTranscoderImpl.setOutputAudioFormat(outputAudioFormat);
        if (!tuSdkMediaFileTranscoderImpl.run(tuSdkMediaProgress)) {
            return null;
        }
        return tuSdkMediaFileTranscoderImpl;
    }
    
    public static TuSdkMediaFileCuter cuter(final TuSdkMediaDataSource mediaDataSource, final String outputFilePath, final MediaFormat outputVideoFormat, final MediaFormat outputAudioFormat, final ImageOrientation outputOrientation, final RectF canvasRect, final RectF cropRect, final TuSdkMediaTimeSlice timeSlice, final TuSdkMediaProgress tuSdkMediaProgress) {
        final TuSdkMediaFileCuterImpl tuSdkMediaFileCuterImpl = new TuSdkMediaFileCuterImpl();
        tuSdkMediaFileCuterImpl.setMediaDataSource(mediaDataSource);
        tuSdkMediaFileCuterImpl.setOutputFilePath(outputFilePath);
        tuSdkMediaFileCuterImpl.setOutputVideoFormat(outputVideoFormat);
        tuSdkMediaFileCuterImpl.setOutputAudioFormat(outputAudioFormat);
        tuSdkMediaFileCuterImpl.setOutputOrientation(outputOrientation);
        tuSdkMediaFileCuterImpl.setCanvasRect(canvasRect);
        tuSdkMediaFileCuterImpl.setCropRect(cropRect);
        tuSdkMediaFileCuterImpl.setTimeSlice(timeSlice);
        if (!tuSdkMediaFileCuterImpl.run(tuSdkMediaProgress)) {
            return null;
        }
        return tuSdkMediaFileCuterImpl;
    }
    
    public static TuSdkMediaFileCuter cuter(final TuSdkMediaDataSource mediaDataSource, final String outputFilePath, final MediaFormat outputVideoFormat, final MediaFormat outputAudioFormat, final ImageOrientation outputOrientation, final RectF canvasRect, final RectF cropRect, final TuSdkMediaTimeline timeline, final TuSdkMediaProgress tuSdkMediaProgress) {
        final TuSdkMediaFileCuterImpl tuSdkMediaFileCuterImpl = new TuSdkMediaFileCuterImpl();
        tuSdkMediaFileCuterImpl.setMediaDataSource(mediaDataSource);
        tuSdkMediaFileCuterImpl.setOutputFilePath(outputFilePath);
        tuSdkMediaFileCuterImpl.setOutputVideoFormat(outputVideoFormat);
        tuSdkMediaFileCuterImpl.setOutputAudioFormat(outputAudioFormat);
        tuSdkMediaFileCuterImpl.setOutputOrientation(outputOrientation);
        tuSdkMediaFileCuterImpl.setCanvasRect(canvasRect);
        tuSdkMediaFileCuterImpl.setCropRect(cropRect);
        tuSdkMediaFileCuterImpl.setTimeline(timeline);
        if (!tuSdkMediaFileCuterImpl.run(tuSdkMediaProgress)) {
            return null;
        }
        return tuSdkMediaFileCuterImpl;
    }
    
    public static TuSdkMediaFilesCuter cuter(final List<TuSdkMediaDataSource> mediaDataSources, final String outputFilePath, final TuSdkMediaTimeSlice timeSlice, final MediaFormat outputVideoFormat, final MediaFormat outputAudioFormat, final ImageOrientation outputOrientation, final RectF canvasRect, final RectF cropRect) {
        final TuSdkMediaFilesCuterImpl tuSdkMediaFilesCuterImpl = new TuSdkMediaFilesCuterImpl();
        tuSdkMediaFilesCuterImpl.setMediaDataSources(mediaDataSources);
        tuSdkMediaFilesCuterImpl.setOutputFilePath(outputFilePath);
        tuSdkMediaFilesCuterImpl.setOutputVideoFormat(outputVideoFormat);
        tuSdkMediaFilesCuterImpl.setOutputAudioFormat(outputAudioFormat);
        tuSdkMediaFilesCuterImpl.setOutputOrientation(outputOrientation);
        tuSdkMediaFilesCuterImpl.setCanvasRect(canvasRect);
        tuSdkMediaFilesCuterImpl.setCropRect(cropRect);
        tuSdkMediaFilesCuterImpl.setTimeSlice(timeSlice);
        return tuSdkMediaFilesCuterImpl;
    }
    
    public static TuSdkMediaFileCuter director(final TuSdkMediaDataSource mediaDataSource, final String outputFilePath, final MediaFormat outputVideoFormat, final MediaFormat outputAudioFormat, final TuSdkMediaTimeline timeline, final TuSdkMediaProgress tuSdkMediaProgress) {
        final TuSdkMediaFileDirectorImpl tuSdkMediaFileDirectorImpl = new TuSdkMediaFileDirectorImpl();
        tuSdkMediaFileDirectorImpl.setMediaDataSource(mediaDataSource);
        tuSdkMediaFileDirectorImpl.setOutputFilePath(outputFilePath);
        tuSdkMediaFileDirectorImpl.setOutputVideoFormat(outputVideoFormat);
        tuSdkMediaFileDirectorImpl.setOutputAudioFormat(outputAudioFormat);
        tuSdkMediaFileDirectorImpl.setTimeline(timeline);
        if (!tuSdkMediaFileDirectorImpl.run(tuSdkMediaProgress)) {
            return null;
        }
        return tuSdkMediaFileDirectorImpl;
    }
    
    public static TuSdkMediaVideoComposer imageToVideo(final LinkedList<TuSdkComposeItem> inputComposList, final String outputFilePath, final MediaFormat outputVideoFormat, final MediaFormat outputAudioFormat, final TuSdkMediaProgress tuSdkMediaProgress, final boolean isAllKeyFrame, final TuSdkSurfaceRender surfaceRender) {
        final TuSdkMediaVideoComposer tuSdkMediaVideoComposer = new TuSdkMediaVideoComposer();
        tuSdkMediaVideoComposer.setInputComposList(inputComposList);
        tuSdkMediaVideoComposer.setOutputFilePath(outputFilePath);
        tuSdkMediaVideoComposer.setOutputVideoFormat(outputVideoFormat);
        tuSdkMediaVideoComposer.setOutputAudioFormat(outputAudioFormat);
        tuSdkMediaVideoComposer.setSurfaceRender(surfaceRender);
        tuSdkMediaVideoComposer.setIsAllKeyFrame(isAllKeyFrame);
        if (!tuSdkMediaVideoComposer.run(tuSdkMediaProgress)) {
            return null;
        }
        return tuSdkMediaVideoComposer;
    }
}
