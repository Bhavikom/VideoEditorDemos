package org.lasque.tusdk.api.audio.preproc.mixer;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManagaer;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManagaer.State;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManagaer.TuSDKAudioDecoderTaskMangaerDelegate;
import org.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public abstract class TuSDKAudioMixer
  implements TuSDKAudioMixerInterface
{
  public static final int ERROR_CODE_UNKNOW = -1;
  private List<TuSDKAudioEntry> a;
  private RawAudioTrack b;
  private OnAudioMixerDelegate c;
  private volatile State d = State.Idle;
  private TuSDKAudioDecoderTaskManagaer e = new TuSDKAudioDecoderTaskManagaer();
  private AsyncMixTask f = null;
  
  public TuSDKAudioMixer setOnAudioMixDelegate(OnAudioMixerDelegate paramOnAudioMixerDelegate)
  {
    this.c = paramOnAudioMixerDelegate;
    return this;
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
  
  private void a(State paramState)
  {
    if (this.d == paramState) {
      return;
    }
    this.d = paramState;
    if (this.c != null) {
      this.c.onStateChanged(paramState);
    }
    if (paramState == State.Complete) {
      StatisticsManger.appendComponent(9449473L);
    }
  }
  
  public State getState()
  {
    return this.d;
  }
  
  public void clearDecodeCahceInfo()
  {
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSDKAudioEntry localTuSDKAudioEntry = (TuSDKAudioEntry)localIterator.next();
      localTuSDKAudioEntry.clearDecodeCahceInfo();
    }
  }
  
  protected void onMixComplete() {}
  
  protected void onMixed(byte[] paramArrayOfByte)
  {
    if (this.c != null) {
      this.c.onMixed(paramArrayOfByte);
    }
  }
  
  protected void onMixingError(int paramInt)
  {
    if (this.c != null) {
      this.c.onMixingError(paramInt);
    }
  }
  
  protected byte[] processPCMData(RawAudioTrack paramRawAudioTrack, byte[] paramArrayOfByte)
  {
    if (RawAudioTrack.a(paramRawAudioTrack).getVolume() != 1.0F) {
      return a(paramArrayOfByte, RawAudioTrack.a(paramRawAudioTrack).getVolume());
    }
    return paramArrayOfByte;
  }
  
  public void cancel()
  {
    if ((this.d != State.Decoding) && (this.d != State.Mixing)) {
      return;
    }
    a();
  }
  
  private void a()
  {
    if ((this.d != State.Decoding) && (this.d != State.Mixing)) {
      return;
    }
    this.b = null;
    this.d = State.Cancelled;
    if (this.e != null) {
      this.e.cancel();
    }
    if (this.f != null) {
      this.f.cancel();
    }
  }
  
  private void b()
  {
    this.f = new AsyncMixTask(null);
    this.f.execute(new Void[0]);
  }
  
  public void mixAudios(List<TuSDKAudioEntry> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return;
    }
    if ((this.d == State.Mixing) || (this.d == State.Decoding)) {
      return;
    }
    a();
    this.a = new ArrayList(paramList);
    a(State.Decoding);
    this.e.setAudioEntry(paramList);
    this.e.setDelegate(new TuSDKAudioDecoderTaskManagaer.TuSDKAudioDecoderTaskMangaerDelegate()
    {
      public void onStateChanged(TuSDKAudioDecoderTaskManagaer.State paramAnonymousState)
      {
        if (paramAnonymousState == TuSDKAudioDecoderTaskManagaer.State.Complete)
        {
          TuSDKAudioMixer.a(TuSDKAudioMixer.this, TuSDKAudioMixer.State.Decoded);
          TuSDKAudioMixer.a(TuSDKAudioMixer.this);
        }
        else if (paramAnonymousState == TuSDKAudioDecoderTaskManagaer.State.Cancelled)
        {
          TuSDKAudioMixer.a(TuSDKAudioMixer.this, TuSDKAudioMixer.State.Cancelled);
        }
      }
    });
    this.e.start();
  }
  
  private List<RawAudioTrack> a(List<TuSDKAudioEntry> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSDKAudioEntry localTuSDKAudioEntry = (TuSDKAudioEntry)localIterator.next();
      if ((localTuSDKAudioEntry.isValid()) && (localTuSDKAudioEntry.getRawInfo() != null) && (localTuSDKAudioEntry.getRawInfo().isValid()))
      {
        RawAudioTrack localRawAudioTrack = new RawAudioTrack(localTuSDKAudioEntry, null);
        if ((this.b == null) && (localTuSDKAudioEntry.isTrunk()))
        {
          this.b = localRawAudioTrack;
          RawAudioTrack.a(localRawAudioTrack, true);
        }
        localArrayList.add(localRawAudioTrack);
      }
    }
    return localArrayList;
  }
  
  private int c()
  {
    if ((this.b == null) || (!RawAudioTrack.a(this.b).validateTimeRange())) {
      return 0;
    }
    return RawAudioTrack.a(this.b).bytesSizeOfTimeRangeStartPosition();
  }
  
  private int d()
  {
    if ((this.b != null) && (RawAudioTrack.a(this.b).validateTimeRange())) {
      return RawAudioTrack.a(this.b).getRawInfo().bytesCountOfTime(Math.round(RawAudioTrack.a(this.b).getTimeRange().getEndTime()));
    }
    return Integer.MAX_VALUE;
  }
  
  private void b(List<RawAudioTrack> paramList)
  {
    if (this.c != null) {
      if (this.b != null)
      {
        this.c.onReayTrunkTrackInfo(RawAudioTrack.a(this.b).getRawInfo());
      }
      else
      {
        if (paramList.size() == 0)
        {
          TLog.e("Trunk audio track is null !!! ", new Object[0]);
          return;
        }
        this.c.onReayTrunkTrackInfo(RawAudioTrack.a((RawAudioTrack)paramList.get(0)).getRawInfo());
      }
    }
    a(State.Mixing);
    int i = paramList.size();
    byte[][] arrayOfByte = new byte[i][];
    byte[] arrayOfByte1 = new byte['Ȁ'];
    int j = -1;
    int k = d();
    int m = c();
    try
    {
      while (this.d == State.Mixing)
      {
        for (int n = 0; n < i; n++)
        {
          localObject1 = (RawAudioTrack)paramList.get(n);
          if (RawAudioTrack.b((RawAudioTrack)localObject1))
          {
            j = -1;
          }
          else if (RawAudioTrack.c((RawAudioTrack)localObject1))
          {
            j = RawAudioTrack.d((RawAudioTrack)localObject1).read(arrayOfByte1);
            if ((j == -1) || (m >= k))
            {
              RawAudioTrack.b((RawAudioTrack)localObject1, true);
              break;
            }
          }
          else if ((this.b != null) && (RawAudioTrack.a((RawAudioTrack)localObject1).validateTimeRange()) && ((m < RawAudioTrack.a((RawAudioTrack)localObject1).bytesSizeOfTimeRangeStartPosition()) || (m >= RawAudioTrack.a((RawAudioTrack)localObject1).bytesSizeOfTimeRangeEndPosition())))
          {
            j = -1;
          }
          else
          {
            j = RawAudioTrack.d((RawAudioTrack)localObject1).read(arrayOfByte1);
            if (j == -1) {
              if ((this.b != null) && ((RawAudioTrack.a((RawAudioTrack)localObject1).validateTimeRange()) || (RawAudioTrack.a((RawAudioTrack)localObject1).isLooping())))
              {
                RawAudioTrack.d((RawAudioTrack)localObject1).seek(0L);
                j = RawAudioTrack.d((RawAudioTrack)localObject1).read(arrayOfByte1);
              }
              else
              {
                RawAudioTrack.b((RawAudioTrack)localObject1, true);
              }
            }
          }
          if (j != -1) {
            arrayOfByte1 = processPCMData((RawAudioTrack)localObject1, arrayOfByte1);
          }
          arrayOfByte[n] = (j == -1 ? new byte['Ȁ'] : Arrays.copyOf(arrayOfByte1, arrayOfByte1.length));
          if (RawAudioTrack.c((RawAudioTrack)localObject1)) {
            m += arrayOfByte[n].length;
          }
        }
        n = 1;
        localObject1 = paramList.iterator();
        while (((Iterator)localObject1).hasNext())
        {
          RawAudioTrack localRawAudioTrack1 = (RawAudioTrack)((Iterator)localObject1).next();
          if ((RawAudioTrack.c(localRawAudioTrack1)) && (RawAudioTrack.b(localRawAudioTrack1)))
          {
            n = 1;
            break;
          }
          if (!RawAudioTrack.b(localRawAudioTrack1)) {
            n = 0;
          }
        }
        if (n != 0) {
          break;
        }
        localObject1 = mixRawAudioBytes(arrayOfByte);
        onMixed((byte[])localObject1);
      }
      Iterator localIterator1 = paramList.iterator();
      while (localIterator1.hasNext())
      {
        localObject1 = (RawAudioTrack)localIterator1.next();
        if (localObject1 != null) {
          RawAudioTrack.e((RawAudioTrack)localObject1);
        }
      }
    }
    catch (IOException localIOException)
    {
      Object localObject1;
      localIOException.printStackTrace();
      onMixingError(-1);
      Iterator localIterator2 = paramList.iterator();
      while (localIterator2.hasNext())
      {
        localObject1 = (RawAudioTrack)localIterator2.next();
        if (localObject1 != null) {
          RawAudioTrack.e((RawAudioTrack)localObject1);
        }
      }
    }
    finally
    {
      Iterator localIterator3 = paramList.iterator();
      while (localIterator3.hasNext())
      {
        RawAudioTrack localRawAudioTrack2 = (RawAudioTrack)localIterator3.next();
        if (localRawAudioTrack2 != null) {
          RawAudioTrack.e(localRawAudioTrack2);
        }
      }
    }
  }
  
  private class AsyncMixTask
    extends AsyncTask<Void, Double, Void>
  {
    private AsyncMixTask() {}
    
    protected Void doInBackground(Void... paramVarArgs)
    {
      TuSDKAudioMixer.b(TuSDKAudioMixer.this, TuSDKAudioMixer.a(TuSDKAudioMixer.this, TuSDKAudioMixer.b(TuSDKAudioMixer.this)));
      return null;
    }
    
    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      TuSDKAudioMixer.a(TuSDKAudioMixer.this, null);
      TuSDKAudioMixer.a(TuSDKAudioMixer.this, TuSDKAudioMixer.State.Complete);
      TuSDKAudioMixer.this.onMixComplete();
    }
    
    public boolean isDone()
    {
      return getStatus() == AsyncTask.Status.FINISHED;
    }
    
    public void cancel()
    {
      if ((isCancelled()) || (isDone())) {
        return;
      }
      TuSDKAudioMixer.b(TuSDKAudioMixer.this, TuSDKAudioMixer.State.Cancelled);
      cancel(true);
    }
    
    protected void onCancelled(Void paramVoid)
    {
      super.onCancelled(paramVoid);
      TuSDKAudioMixer.a(TuSDKAudioMixer.this, TuSDKAudioMixer.State.Cancelled);
    }
  }
  
  protected class RawAudioTrack
  {
    private RandomAccessFile b;
    private TuSDKAudioEntry c;
    private boolean d;
    private boolean e = false;
    
    private RawAudioTrack(TuSDKAudioEntry paramTuSDKAudioEntry)
    {
      this.c = paramTuSDKAudioEntry;
    }
    
    private boolean a()
    {
      return this.e;
    }
    
    private RandomAccessFile b()
    {
      if (this.b == null) {
        try
        {
          this.b = new RandomAccessFile(this.c.getRawInfo().getFilePath(), "r");
          if ((a()) && (this.c.validateTimeRange())) {
            this.b.seek(this.c.bytesSizeOfTimeRangeStartPosition());
          }
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
      return this.b;
    }
    
    private void c()
    {
      if (this.b != null) {
        try
        {
          this.b.close();
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
        finally
        {
          this.b = null;
        }
      }
    }
  }
  
  public static abstract interface OnAudioMixerDelegate
  {
    public abstract void onReayTrunkTrackInfo(TuSDKAudioInfo paramTuSDKAudioInfo);
    
    public abstract void onMixed(byte[] paramArrayOfByte);
    
    public abstract void onMixingError(int paramInt);
    
    public abstract void onStateChanged(TuSDKAudioMixer.State paramState);
  }
  
  public static enum State
  {
    private State() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\mixer\TuSDKAudioMixer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */