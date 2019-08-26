package org.lasque.tusdk.core.seles.sources;

import android.media.MediaCodec.BufferInfo;
import java.io.File;
import java.nio.ByteBuffer;

public abstract interface TuSDKMovieCreatorInterface
{
  public abstract boolean isRecording();
  
  public abstract boolean isSaveToAlbum();
  
  public abstract void setSaveToAlbum(boolean paramBoolean);
  
  public abstract String getSaveToAlbumName();
  
  public abstract void setSaveToAlbumName(String paramString);
  
  public abstract File getMovieOutputPath();
  
  public static class ByteDataFrame
  {
    public int trackIndex;
    public ByteBuffer buffer;
    public MediaCodec.BufferInfo bufferInfo;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSDKMovieCreatorInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */