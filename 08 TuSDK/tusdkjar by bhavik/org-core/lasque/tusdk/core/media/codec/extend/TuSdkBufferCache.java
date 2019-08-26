package org.lasque.tusdk.core.media.codec.extend;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;

public class TuSdkBufferCache
{
  public ByteBuffer buffer;
  public MediaCodec.BufferInfo info;
  
  public TuSdkBufferCache() {}
  
  public TuSdkBufferCache(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    this.buffer = paramByteBuffer;
    this.info = paramBufferInfo;
  }
  
  public void clear()
  {
    this.buffer.clear();
    this.info.flags = 0;
    this.info.size = 0;
    this.info.offset = 0;
    this.info.presentationTimeUs = -1L;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkBufferCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */