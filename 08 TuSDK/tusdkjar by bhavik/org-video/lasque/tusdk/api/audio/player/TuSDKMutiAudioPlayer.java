package org.lasque.tusdk.api.audio.player;

import android.media.AudioTrack;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixer;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixer.OnAudioMixerDelegate;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixer.State;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAverageAudioMixer;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManagaer;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManagaer.State;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManagaer.TuSDKAudioDecoderTaskMangaerDelegate;
import org.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import org.lasque.tusdk.core.utils.TLog;

public class TuSDKMutiAudioPlayer
{
  private List<TuSDKAudioEntry> a;
  private AudioTrack b;
  private TuSDKAudioMixer c = new TuSDKAverageAudioMixer();
  private TuSDKMutiAudioPlayerDelegate d;
  private boolean e = false;
  private State f = State.Idle;
  private TuSDKAudioDecoderTaskManagaer g = new TuSDKAudioDecoderTaskManagaer();
  private TuSDKAudioMixer.OnAudioMixerDelegate h = new TuSDKAudioMixer.OnAudioMixerDelegate()
  {
    public void onMixed(byte[] paramAnonymousArrayOfByte)
    {
      TuSDKMutiAudioPlayer.this.write(paramAnonymousArrayOfByte);
    }
    
    public void onMixingError(int paramAnonymousInt) {}
    
    public void onReayTrunkTrackInfo(TuSDKAudioInfo paramAnonymousTuSDKAudioInfo)
    {
      TuSDKMutiAudioPlayer.a(TuSDKMutiAudioPlayer.this, TuSDKMutiAudioPlayer.a(TuSDKMutiAudioPlayer.this, paramAnonymousTuSDKAudioInfo));
    }
    
    public void onStateChanged(TuSDKAudioMixer.State paramAnonymousState)
    {
      if (paramAnonymousState == TuSDKAudioMixer.State.Mixing) {
        TuSDKMutiAudioPlayer.b(TuSDKMutiAudioPlayer.this);
      } else if (paramAnonymousState == TuSDKAudioMixer.State.Complete) {
        TuSDKMutiAudioPlayer.c(TuSDKMutiAudioPlayer.this);
      }
    }
  };
  
  private AudioTrack a(TuSDKAudioInfo paramTuSDKAudioInfo)
  {
    int i = AudioTrack.getMinBufferSize(paramTuSDKAudioInfo.sampleRate, paramTuSDKAudioInfo.channelConfig, paramTuSDKAudioInfo.audioFormat);
    return new AudioTrack(3, paramTuSDKAudioInfo.sampleRate, paramTuSDKAudioInfo.channelConfig, paramTuSDKAudioInfo.audioFormat, i, 1);
  }
  
  public void asyncPrepare(List<TuSDKAudioEntry> paramList)
  {
    if ((this.f == State.Playing) || (this.f == State.PreParing)) {
      return;
    }
    stop();
    this.a = new ArrayList(paramList);
    if ((this.a == null) || (this.a.size() == 0))
    {
      TLog.e("%s : Please set a valid file path", new Object[] { this });
      return;
    }
    this.g.setAudioEntry(this.a);
    this.g.setDelegate(new TuSDKAudioDecoderTaskManagaer.TuSDKAudioDecoderTaskMangaerDelegate()
    {
      public void onStateChanged(TuSDKAudioDecoderTaskManagaer.State paramAnonymousState)
      {
        if (paramAnonymousState == TuSDKAudioDecoderTaskManagaer.State.Complete)
        {
          if (TuSDKMutiAudioPlayer.a(TuSDKMutiAudioPlayer.this) == TuSDKMutiAudioPlayer.State.PreParing) {
            TuSDKMutiAudioPlayer.a(TuSDKMutiAudioPlayer.this, TuSDKMutiAudioPlayer.State.PrePared);
          }
        }
        else if (paramAnonymousState == TuSDKAudioDecoderTaskManagaer.State.Decoding) {
          TuSDKMutiAudioPlayer.a(TuSDKMutiAudioPlayer.this, TuSDKMutiAudioPlayer.State.PreParing);
        } else if (paramAnonymousState == TuSDKAudioDecoderTaskManagaer.State.Cancelled) {
          TuSDKMutiAudioPlayer.a(TuSDKMutiAudioPlayer.this, TuSDKMutiAudioPlayer.State.Idle);
        }
      }
    });
    this.g.start();
  }
  
  public TuSDKMutiAudioPlayer setLooping(boolean paramBoolean)
  {
    this.e = paramBoolean;
    return this;
  }
  
  public State getState()
  {
    return this.f;
  }
  
  private void a()
  {
    if ((this.a == null) || (this.a.size() == 0))
    {
      TLog.e("%s : Please set a valid file path", new Object[] { this });
      return;
    }
    this.c.setOnAudioMixDelegate(this.h);
    this.c.mixAudios(this.a);
  }
  
  public void start()
  {
    if ((this.f == State.Playing) || (this.f == State.PreParing)) {
      return;
    }
    a();
  }
  
  private void b()
  {
    if (d().getState() != 0)
    {
      d().play();
      a(State.Playing);
    }
  }
  
  public void stop()
  {
    boolean bool = this.e;
    this.e = false;
    c();
    this.e = bool;
  }
  
  private void c()
  {
    if (this.c != null) {
      this.c.cancel();
    }
    if (this.b != null)
    {
      if (this.b.getState() != 0)
      {
        this.b.stop();
        this.b.release();
      }
      this.b = null;
    }
    if (this.g != null) {
      this.g.cancel();
    }
    if (this.e)
    {
      a();
      return;
    }
    if (this.f == State.Playing) {
      a(State.Complete);
    }
    this.f = State.Idle;
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((paramArrayOfByte == null) || (this.b == null) || (this.f != State.Playing)) {
      return;
    }
    this.b.write(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public void write(byte[] paramArrayOfByte)
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  private void a(State paramState)
  {
    if (this.f == paramState) {
      return;
    }
    this.f = paramState;
    if (this.d != null) {
      this.d.onStateChanged(paramState);
    }
  }
  
  private AudioTrack d()
  {
    if ((this.b == null) || (this.f == State.Complete) || (this.b.getState() == 0)) {
      this.b = a(TuSDKAudioInfo.defaultAudioInfo());
    }
    return this.b;
  }
  
  public TuSDKMutiAudioPlayer setAudioMixer(TuSDKAudioMixer paramTuSDKAudioMixer)
  {
    this.c = paramTuSDKAudioMixer;
    return this;
  }
  
  public TuSDKMutiAudioPlayerDelegate getDelegate()
  {
    return this.d;
  }
  
  public TuSDKMutiAudioPlayer setDelegate(TuSDKMutiAudioPlayerDelegate paramTuSDKMutiAudioPlayerDelegate)
  {
    this.d = paramTuSDKMutiAudioPlayerDelegate;
    return this;
  }
  
  public static enum State
  {
    private State() {}
  }
  
  public static abstract interface TuSDKMutiAudioPlayerDelegate
  {
    public abstract void onStateChanged(TuSDKMutiAudioPlayer.State paramState);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\player\TuSDKMutiAudioPlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */