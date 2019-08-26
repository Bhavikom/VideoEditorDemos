package org.lasque.tusdk.api.audio.preproc.mixer;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.os.Environment;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender.TuSdkAudioRenderCallback;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkMixerRender
  implements TuSdkAudioRender
{
  private List<TuSDKAudioMixerRender.RawAudioTrack> a = new ArrayList();
  private boolean b = true;
  private String c = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lsq_lastTemp_audio_PCM";
  private float d = 1.0F;
  private float e = 1.0F;
  private long f = -1L;
  private boolean g = true;
  
  public boolean onAudioSliceRender(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkAudioRender.TuSdkAudioRenderCallback paramTuSdkAudioRenderCallback)
  {
    if (!this.g) {
      return false;
    }
    if ((paramTuSdkAudioRenderCallback == null) || (paramTuSdkAudioRenderCallback.isEncodec())) {
      return false;
    }
    if ((paramBufferInfo == null) || (paramByteBuffer == null)) {
      return false;
    }
    byte[][] arrayOfByte = new byte[this.a.size() + 1][];
    arrayOfByte[0] = new byte[paramBufferInfo.size];
    paramByteBuffer.get(arrayOfByte[0]);
    arrayOfByte[0] = a(arrayOfByte[0], this.d);
    Object localObject;
    if (this.a.size() == 0)
    {
      byte[] arrayOfByte1 = a(arrayOfByte);
      localObject = ByteBuffer.wrap(arrayOfByte1);
      paramTuSdkAudioRenderCallback.returnRenderBuffer((ByteBuffer)localObject, paramBufferInfo);
      return true;
    }
    this.f = paramBufferInfo.presentationTimeUs;
    try
    {
      for (int i = 0; i < this.a.size(); i++)
      {
        localObject = new byte[paramBufferInfo.size];
        int j = ((TuSDKAudioMixerRender.RawAudioTrack)this.a.get(i)).a(paramBufferInfo.presentationTimeUs, (byte[])localObject);
        if (j != -1) {
          arrayOfByte[(i + 1)] = a((byte[])localObject, ((TuSDKAudioMixerRender.RawAudioTrack)this.a.get(i)).a.getVolume() == 1.0F ? this.e : ((TuSDKAudioMixerRender.RawAudioTrack)this.a.get(i)).a.getVolume());
        } else {
          arrayOfByte[(i + 1)] = localObject;
        }
      }
      byte[] arrayOfByte2 = a(arrayOfByte);
      localObject = ByteBuffer.wrap(arrayOfByte2);
      paramTuSdkAudioRenderCallback.returnRenderBuffer((ByteBuffer)localObject, paramBufferInfo);
      return true;
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException);
    }
    return false;
  }
  
  public void setRawAudioTrackList(List<TuSDKAudioMixerRender.RawAudioTrack> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return;
    }
    this.a.clear();
    this.a.addAll(paramList);
  }
  
  public void seekTo(long paramLong)
  {
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSDKAudioMixerRender.RawAudioTrack localRawAudioTrack = (TuSDKAudioMixerRender.RawAudioTrack)localIterator.next();
      localRawAudioTrack.seekUs(paramLong);
    }
  }
  
  public void setTrunkAudioVolume(float paramFloat)
  {
    this.d = paramFloat;
  }
  
  public float getTrunkVolume()
  {
    return this.d;
  }
  
  public void setSecondAudioTrack(float paramFloat)
  {
    this.e = paramFloat;
  }
  
  public float getSecondVolume()
  {
    return this.e;
  }
  
  private byte[] a(byte[] paramArrayOfByte, float paramFloat)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    for (int i = 0; i < arrayOfByte.length; i += 2)
    {
      int j = (short)paramArrayOfByte[(i + 1)];
      int k = (short)paramArrayOfByte[i];
      j = (short)((j & 0xFF) << 8);
      k = (short)(k & 0xFF);
      int m = (short)(j | k);
      m = (short)(int)(m * paramFloat);
      arrayOfByte[i] = ((byte)m);
      arrayOfByte[(i + 1)] = ((byte)(m >> 8));
    }
    return arrayOfByte;
  }
  
  private byte[] a(byte[][] paramArrayOfByte)
  {
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
      return null;
    }
    byte[] arrayOfByte = paramArrayOfByte[0];
    if (paramArrayOfByte.length == 1) {
      return arrayOfByte;
    }
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      if (paramArrayOfByte[i].length != arrayOfByte.length)
      {
        TLog.e("column of the road of audio + " + i + " is diffrent.", new Object[0]);
        return null;
      }
    }
    i = paramArrayOfByte.length;
    int j = arrayOfByte.length / 2;
    short[][] arrayOfShort = new short[i][j];
    int m;
    for (int k = 0; k < i; k++) {
      for (m = 0; m < j; m++) {
        arrayOfShort[k][m] = ((short)(paramArrayOfByte[k][(m * 2)] & 0xFF | (paramArrayOfByte[k][(m * 2 + 1)] & 0xFF) << 8));
      }
    }
    short[] arrayOfShort1 = new short[j];
    int n = 0;
    for (int i1 = 0; i1 < j; i1++)
    {
      m = 0;
      for (n = 0; n < i; n++) {
        m += arrayOfShort[n][i1];
      }
      arrayOfShort1[i1] = ((short)(m / i));
    }
    for (n = 0; n < j; n++)
    {
      arrayOfByte[(n * 2)] = ((byte)(arrayOfShort1[n] & 0xFF));
      arrayOfByte[(n * 2 + 1)] = ((byte)((arrayOfShort1[n] & 0xFF00) >> 8));
    }
    return arrayOfByte;
  }
  
  public void clearAllAudioData()
  {
    this.a.clear();
  }
  
  public void setEnable(boolean paramBoolean)
  {
    this.g = paramBoolean;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\mixer\TuSdkMixerRender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */