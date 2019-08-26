package org.lasque.tusdk.core.utils.image;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import java.io.FileDescriptor;
import java.io.IOException;
import org.lasque.tusdk.core.secret.SdkValid;

public final class GifHelper
{
  private static final String TAG = "GifHelper";
  private static final boolean isInit = SdkValid.isInit;
  private volatile long gifInfoPtr;
  private int mWidth;
  private int mHeight;
  private int mFrameCount;
  
  private GifHelper(long paramLong, int paramInt1, int paramInt2, int paramInt3)
  {
    this.gifInfoPtr = paramLong;
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
    this.mFrameCount = paramInt3;
  }
  
  public int getWidth()
  {
    return this.mWidth;
  }
  
  public int getHeight()
  {
    return this.mHeight;
  }
  
  public int getFrameCount()
  {
    return this.mFrameCount;
  }
  
  public static GifHelper parseFile(String paramString)
  {
    return openGifFileJNI(paramString);
  }
  
  public static GifHelper parseFd(FileDescriptor paramFileDescriptor)
  {
    return openGifFdJNI(paramFileDescriptor, 0L);
  }
  
  public static GifHelper openAssetFileDescriptor(AssetFileDescriptor paramAssetFileDescriptor)
  {
    try
    {
      GifHelper localGifHelper = openGifFdJNI(paramAssetFileDescriptor.getFileDescriptor(), paramAssetFileDescriptor.getStartOffset());
      return localGifHelper;
    }
    finally
    {
      paramAssetFileDescriptor.close();
    }
  }
  
  public static GifHelper openURI(ContentResolver paramContentResolver, Uri paramUri)
  {
    if ("file".equals(paramUri.getScheme())) {
      return openGifFileJNI(paramUri.getPath());
    }
    return openAssetFileDescriptor(paramContentResolver.openAssetFileDescriptor(paramUri, "r"));
  }
  
  public synchronized long renderFrame(Bitmap paramBitmap)
  {
    return renderGifFrameJNI(this.gifInfoPtr, paramBitmap);
  }
  
  public synchronized void recycle()
  {
    freeGifJNI(this.gifInfoPtr);
    this.gifInfoPtr = 0L;
  }
  
  public synchronized boolean isRecycled()
  {
    return this.gifInfoPtr == 0L;
  }
  
  public synchronized boolean reset()
  {
    return resetGifJNI(this.gifInfoPtr);
  }
  
  public synchronized long restoreRemainder()
  {
    return restoreGifRemainderJNI(this.gifInfoPtr);
  }
  
  public synchronized void saveRemainder()
  {
    saveGifRemainderJNI(this.gifInfoPtr);
  }
  
  public void setSpeed(float paramFloat)
  {
    if ((paramFloat <= 0.0F) || (Float.isNaN(paramFloat))) {
      throw new IllegalArgumentException("Speed factor is not positive");
    }
    if (paramFloat < 4.656613E-10F) {
      paramFloat = 4.656613E-10F;
    }
    synchronized (this)
    {
      setGifSpeedFactorJNI(this.gifInfoPtr, paramFloat);
    }
  }
  
  public synchronized int getDuration()
  {
    return getGifDurationJNI(this.gifInfoPtr);
  }
  
  public synchronized int getCurrentPosition()
  {
    return getGifCurrentPositionJNI(this.gifInfoPtr);
  }
  
  public synchronized boolean isAnimationCompleted()
  {
    return isGifAnimationCompletedJNI(this.gifInfoPtr);
  }
  
  /* Error */
  public int getFrameDuration(int paramInt)
  {
    // Byte code:
    //   0: iload_1
    //   1: iflt +11 -> 12
    //   4: iload_1
    //   5: aload_0
    //   6: invokevirtual 40	org/lasque/tusdk/core/utils/image/GifHelper:getFrameCount	()I
    //   9: if_icmplt +13 -> 22
    //   12: new 14	java/lang/IndexOutOfBoundsException
    //   15: dup
    //   16: ldc 3
    //   18: invokespecial 35	java/lang/IndexOutOfBoundsException:<init>	(Ljava/lang/String;)V
    //   21: athrow
    //   22: aload_0
    //   23: dup
    //   24: astore_2
    //   25: monitorenter
    //   26: aload_0
    //   27: getfield 22	org/lasque/tusdk/core/utils/image/GifHelper:gifInfoPtr	J
    //   30: iload_1
    //   31: invokestatic 46	org/lasque/tusdk/core/utils/image/GifHelper:getGifFrameDurationJNI	(JI)I
    //   34: aload_2
    //   35: monitorexit
    //   36: ireturn
    //   37: astore_3
    //   38: aload_2
    //   39: monitorexit
    //   40: aload_3
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	GifHelper
    //   0	42	1	paramInt	int
    //   24	15	2	Ljava/lang/Object;	Object
    //   37	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   26	36	37	finally
    //   37	40	37	finally
  }
  
  public synchronized int getLoopCount()
  {
    return getGifLoopCountJNI(this.gifInfoPtr);
  }
  
  public void setLoopCount(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 65535)) {
      throw new IllegalArgumentException("Loop count of range <0, 65535>");
    }
    synchronized (this)
    {
      setGifLoopCountJNI(this.gifInfoPtr, paramInt);
    }
  }
  
  public synchronized int getCurrentFrameIndex()
  {
    return getGifCurrentFrameIndexJNI(this.gifInfoPtr);
  }
  
  public synchronized int getCurrentLoop()
  {
    return getGifCurrentLoopJNI(this.gifInfoPtr);
  }
  
  public synchronized int getErrorCode()
  {
    return getGifErrorCodeJNI(this.gifInfoPtr);
  }
  
  /* Error */
  protected void finalize()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 52	org/lasque/tusdk/core/utils/image/GifHelper:recycle	()V
    //   4: aload_0
    //   5: invokespecial 37	java/lang/Object:finalize	()V
    //   8: goto +10 -> 18
    //   11: astore_1
    //   12: aload_0
    //   13: invokespecial 37	java/lang/Object:finalize	()V
    //   16: aload_1
    //   17: athrow
    //   18: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	19	0	this	GifHelper
    //   11	6	1	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   0	4	11	finally
  }
  
  static native GifHelper openGifFileJNI(String paramString);
  
  static native GifHelper openGifFdJNI(FileDescriptor paramFileDescriptor, long paramLong);
  
  private static native long renderGifFrameJNI(long paramLong, Bitmap paramBitmap);
  
  private static native boolean resetGifJNI(long paramLong);
  
  private static native void setGifSpeedFactorJNI(long paramLong, float paramFloat);
  
  private static native void saveGifRemainderJNI(long paramLong);
  
  private static native long restoreGifRemainderJNI(long paramLong);
  
  private static native void freeGifJNI(long paramLong);
  
  private static native String getGifCommentJNI(long paramLong);
  
  private static native boolean isGifAnimationCompletedJNI(long paramLong);
  
  private static native int getGifLoopCountJNI(long paramLong);
  
  private static native void setGifLoopCountJNI(long paramLong, int paramInt);
  
  private static native int getGifDurationJNI(long paramLong);
  
  private static native int getGifCurrentPositionJNI(long paramLong);
  
  private static native int getGifErrorCodeJNI(long paramLong);
  
  private static native int getGifCurrentLoopJNI(long paramLong);
  
  private static native int getGifCurrentFrameIndexJNI(long paramLong);
  
  private static native int getGifFrameDurationJNI(long paramLong, int paramInt);
  
  public class GifIOException
    extends IOException
  {
    public final GifHelper.GifError reason;
  }
  
  public static enum GifError
  {
    public final String description;
    int a;
    
    private GifError(int paramInt, String paramString)
    {
      this.a = paramInt;
      this.description = paramString;
    }
    
    public static GifError fromCode(int paramInt)
    {
      for (GifError localGifError : ) {
        if (localGifError.a == paramInt) {
          return localGifError;
        }
      }
      ??? = UNKNOWN;
      ((GifError)???).a = paramInt;
      return (GifError)???;
    }
    
    public int getErrorCode()
    {
      return this.a;
    }
    
    public String toString()
    {
      String str = String.format("GifError %d: %s", new Object[] { Integer.valueOf(this.a), this.description });
      return str;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\image\GifHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */