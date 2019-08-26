package org.lasque.tusdk.core.media.suit;

import android.graphics.RectF;
import android.media.MediaFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuter;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuterImpl;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorImpl;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorPlayer;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorPlayerImpl;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFilePlayer;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFilePlayerImpl;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileTranscoder;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileTranscoderImpl;
import org.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkComposeItem;
import org.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkMediaVideoComposer;
import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaFilesCuter;
import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaFilesCuterImpl;
import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaMutableFilePlayerImpl;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkMediaSuit
{
  public static TuSdkMediaFileDirectorPlayer directorPlayer(TuSdkMediaDataSource paramTuSdkMediaDataSource, boolean paramBoolean, TuSdkMediaPlayerListener paramTuSdkMediaPlayerListener)
  {
    TuSdkMediaFileDirectorPlayerImpl localTuSdkMediaFileDirectorPlayerImpl = new TuSdkMediaFileDirectorPlayerImpl();
    localTuSdkMediaFileDirectorPlayerImpl.setMediaDataSource(paramTuSdkMediaDataSource);
    localTuSdkMediaFileDirectorPlayerImpl.setListener(paramTuSdkMediaPlayerListener);
    if (!localTuSdkMediaFileDirectorPlayerImpl.load(paramBoolean)) {
      return null;
    }
    return localTuSdkMediaFileDirectorPlayerImpl;
  }
  
  public static TuSdkMediaFilePlayer playMedia(TuSdkMediaDataSource paramTuSdkMediaDataSource, boolean paramBoolean, TuSdkMediaPlayerListener paramTuSdkMediaPlayerListener)
  {
    TuSdkMediaFilePlayerImpl localTuSdkMediaFilePlayerImpl = new TuSdkMediaFilePlayerImpl();
    localTuSdkMediaFilePlayerImpl.setMediaDataSource(paramTuSdkMediaDataSource);
    localTuSdkMediaFilePlayerImpl.setListener(paramTuSdkMediaPlayerListener);
    if (!localTuSdkMediaFilePlayerImpl.load(paramBoolean)) {
      return null;
    }
    return localTuSdkMediaFilePlayerImpl;
  }
  
  public static TuSdkMediaFilePlayer playMedia(List<TuSdkMediaDataSource> paramList, boolean paramBoolean, TuSdkMediaPlayerListener paramTuSdkMediaPlayerListener)
  {
    TuSdkMediaMutableFilePlayerImpl localTuSdkMediaMutableFilePlayerImpl = new TuSdkMediaMutableFilePlayerImpl();
    localTuSdkMediaMutableFilePlayerImpl.setMediaDataSources(paramList);
    localTuSdkMediaMutableFilePlayerImpl.setListener(paramTuSdkMediaPlayerListener);
    if (!localTuSdkMediaMutableFilePlayerImpl.load(paramBoolean)) {
      return null;
    }
    return localTuSdkMediaMutableFilePlayerImpl;
  }
  
  public static TuSdkMediaFileTranscoder transcoding(TuSdkMediaDataSource paramTuSdkMediaDataSource, String paramString, MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2, TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    ArrayList localArrayList = new ArrayList(1);
    localArrayList.add(paramTuSdkMediaDataSource);
    return merge(localArrayList, paramString, paramMediaFormat1, paramMediaFormat2, paramTuSdkMediaProgress);
  }
  
  public static TuSdkMediaFileTranscoder merge(List<TuSdkMediaDataSource> paramList, String paramString, MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2, TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    TuSdkMediaFileTranscoderImpl localTuSdkMediaFileTranscoderImpl = new TuSdkMediaFileTranscoderImpl();
    localTuSdkMediaFileTranscoderImpl.addInputDataSouces(paramList);
    localTuSdkMediaFileTranscoderImpl.setOutputFilePath(paramString);
    localTuSdkMediaFileTranscoderImpl.setOutputVideoFormat(paramMediaFormat1);
    localTuSdkMediaFileTranscoderImpl.setOutputAudioFormat(paramMediaFormat2);
    boolean bool = localTuSdkMediaFileTranscoderImpl.run(paramTuSdkMediaProgress);
    if (!bool) {
      return null;
    }
    return localTuSdkMediaFileTranscoderImpl;
  }
  
  public static TuSdkMediaFileCuter cuter(TuSdkMediaDataSource paramTuSdkMediaDataSource, String paramString, MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2, ImageOrientation paramImageOrientation, RectF paramRectF1, RectF paramRectF2, TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice, TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    TuSdkMediaFileCuterImpl localTuSdkMediaFileCuterImpl = new TuSdkMediaFileCuterImpl();
    localTuSdkMediaFileCuterImpl.setMediaDataSource(paramTuSdkMediaDataSource);
    localTuSdkMediaFileCuterImpl.setOutputFilePath(paramString);
    localTuSdkMediaFileCuterImpl.setOutputVideoFormat(paramMediaFormat1);
    localTuSdkMediaFileCuterImpl.setOutputAudioFormat(paramMediaFormat2);
    localTuSdkMediaFileCuterImpl.setOutputOrientation(paramImageOrientation);
    localTuSdkMediaFileCuterImpl.setCanvasRect(paramRectF1);
    localTuSdkMediaFileCuterImpl.setCropRect(paramRectF2);
    localTuSdkMediaFileCuterImpl.setTimeSlice(paramTuSdkMediaTimeSlice);
    boolean bool = localTuSdkMediaFileCuterImpl.run(paramTuSdkMediaProgress);
    if (!bool) {
      return null;
    }
    return localTuSdkMediaFileCuterImpl;
  }
  
  public static TuSdkMediaFileCuter cuter(TuSdkMediaDataSource paramTuSdkMediaDataSource, String paramString, MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2, ImageOrientation paramImageOrientation, RectF paramRectF1, RectF paramRectF2, TuSdkMediaTimeline paramTuSdkMediaTimeline, TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    TuSdkMediaFileCuterImpl localTuSdkMediaFileCuterImpl = new TuSdkMediaFileCuterImpl();
    localTuSdkMediaFileCuterImpl.setMediaDataSource(paramTuSdkMediaDataSource);
    localTuSdkMediaFileCuterImpl.setOutputFilePath(paramString);
    localTuSdkMediaFileCuterImpl.setOutputVideoFormat(paramMediaFormat1);
    localTuSdkMediaFileCuterImpl.setOutputAudioFormat(paramMediaFormat2);
    localTuSdkMediaFileCuterImpl.setOutputOrientation(paramImageOrientation);
    localTuSdkMediaFileCuterImpl.setCanvasRect(paramRectF1);
    localTuSdkMediaFileCuterImpl.setCropRect(paramRectF2);
    localTuSdkMediaFileCuterImpl.setTimeline(paramTuSdkMediaTimeline);
    boolean bool = localTuSdkMediaFileCuterImpl.run(paramTuSdkMediaProgress);
    if (!bool) {
      return null;
    }
    return localTuSdkMediaFileCuterImpl;
  }
  
  public static TuSdkMediaFilesCuter cuter(List<TuSdkMediaDataSource> paramList, String paramString, TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice, MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2, ImageOrientation paramImageOrientation, RectF paramRectF1, RectF paramRectF2)
  {
    TuSdkMediaFilesCuterImpl localTuSdkMediaFilesCuterImpl = new TuSdkMediaFilesCuterImpl();
    localTuSdkMediaFilesCuterImpl.setMediaDataSources(paramList);
    localTuSdkMediaFilesCuterImpl.setOutputFilePath(paramString);
    localTuSdkMediaFilesCuterImpl.setOutputVideoFormat(paramMediaFormat1);
    localTuSdkMediaFilesCuterImpl.setOutputAudioFormat(paramMediaFormat2);
    localTuSdkMediaFilesCuterImpl.setOutputOrientation(paramImageOrientation);
    localTuSdkMediaFilesCuterImpl.setCanvasRect(paramRectF1);
    localTuSdkMediaFilesCuterImpl.setCropRect(paramRectF2);
    localTuSdkMediaFilesCuterImpl.setTimeSlice(paramTuSdkMediaTimeSlice);
    return localTuSdkMediaFilesCuterImpl;
  }
  
  public static TuSdkMediaFileCuter director(TuSdkMediaDataSource paramTuSdkMediaDataSource, String paramString, MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2, TuSdkMediaTimeline paramTuSdkMediaTimeline, TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    TuSdkMediaFileDirectorImpl localTuSdkMediaFileDirectorImpl = new TuSdkMediaFileDirectorImpl();
    localTuSdkMediaFileDirectorImpl.setMediaDataSource(paramTuSdkMediaDataSource);
    localTuSdkMediaFileDirectorImpl.setOutputFilePath(paramString);
    localTuSdkMediaFileDirectorImpl.setOutputVideoFormat(paramMediaFormat1);
    localTuSdkMediaFileDirectorImpl.setOutputAudioFormat(paramMediaFormat2);
    localTuSdkMediaFileDirectorImpl.setTimeline(paramTuSdkMediaTimeline);
    boolean bool = localTuSdkMediaFileDirectorImpl.run(paramTuSdkMediaProgress);
    if (!bool) {
      return null;
    }
    return localTuSdkMediaFileDirectorImpl;
  }
  
  public static TuSdkMediaVideoComposer imageToVideo(LinkedList<TuSdkComposeItem> paramLinkedList, String paramString, MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2, TuSdkMediaProgress paramTuSdkMediaProgress, boolean paramBoolean, TuSdkSurfaceRender paramTuSdkSurfaceRender)
  {
    TuSdkMediaVideoComposer localTuSdkMediaVideoComposer = new TuSdkMediaVideoComposer();
    localTuSdkMediaVideoComposer.setInputComposList(paramLinkedList);
    localTuSdkMediaVideoComposer.setOutputFilePath(paramString);
    localTuSdkMediaVideoComposer.setOutputVideoFormat(paramMediaFormat1);
    localTuSdkMediaVideoComposer.setOutputAudioFormat(paramMediaFormat2);
    localTuSdkMediaVideoComposer.setSurfaceRender(paramTuSdkSurfaceRender);
    localTuSdkMediaVideoComposer.setIsAllKeyFrame(paramBoolean);
    boolean bool = localTuSdkMediaVideoComposer.run(paramTuSdkMediaProgress);
    if (!bool) {
      return null;
    }
    return localTuSdkMediaVideoComposer;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\suit\TuSdkMediaSuit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */