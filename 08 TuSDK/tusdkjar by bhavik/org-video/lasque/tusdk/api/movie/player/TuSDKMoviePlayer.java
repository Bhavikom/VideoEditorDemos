package org.lasque.tusdk.api.movie.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import org.lasque.tusdk.core.utils.TLog;

public class TuSDKMoviePlayer
  implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener
{
  private PlayerState a = PlayerState.UNINITIALIZED;
  private MediaPlayer b;
  private boolean c = false;
  private boolean d = true;
  private TuSDKMoviePlayerOption e;
  private boolean f;
  private SurfaceHolder g;
  private TuSDKMoviePlayerDelegate h;
  private boolean i;
  private Handler j = new Handler();
  private int k = 20;
  private int l;
  private OnSeekToPreviewListener m;
  private Handler n = new Handler();
  private SurfaceHolder.Callback o = new SurfaceHolder.Callback()
  {
    public void surfaceCreated(SurfaceHolder paramAnonymousSurfaceHolder)
    {
      TuSDKMoviePlayer.a(TuSDKMoviePlayer.this, false);
      TuSDKMoviePlayer.a(TuSDKMoviePlayer.this, paramAnonymousSurfaceHolder);
      TuSDKMoviePlayer.b(TuSDKMoviePlayer.this, paramAnonymousSurfaceHolder);
    }
    
    public void surfaceChanged(SurfaceHolder paramAnonymousSurfaceHolder, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    
    public void surfaceDestroyed(SurfaceHolder paramAnonymousSurfaceHolder)
    {
      TuSDKMoviePlayer.a(TuSDKMoviePlayer.this, true);
    }
  };
  private Runnable p = new Runnable()
  {
    public void run()
    {
      TuSDKMoviePlayer.c(TuSDKMoviePlayer.this).removeCallbacks(TuSDKMoviePlayer.b(TuSDKMoviePlayer.this));
      if ((TuSDKMoviePlayer.a(TuSDKMoviePlayer.this) != null) && (TuSDKMoviePlayer.a(TuSDKMoviePlayer.this).isPlaying()) && (TuSDKMoviePlayer.e(TuSDKMoviePlayer.this) != null)) {
        TuSDKMoviePlayer.e(TuSDKMoviePlayer.this).onProgress(100 * TuSDKMoviePlayer.a(TuSDKMoviePlayer.this).getCurrentPosition() / TuSDKMoviePlayer.a(TuSDKMoviePlayer.this).getDuration());
      }
      TuSDKMoviePlayer.c(TuSDKMoviePlayer.this).postDelayed(TuSDKMoviePlayer.b(TuSDKMoviePlayer.this), TuSDKMoviePlayer.f(TuSDKMoviePlayer.this));
    }
  };
  
  public static TuSDKMoviePlayer createMoviePlayer()
  {
    return new TuSDKMoviePlayer();
  }
  
  public void initVideoPlayer(Context paramContext, Uri paramUri, SurfaceView paramSurfaceView)
  {
    if ((paramContext == null) || (paramUri == null) || (paramSurfaceView == null))
    {
      TLog.e("Parameter is not valid", new Object[0]);
      return;
    }
    TuSDKMoviePlayerOption localTuSDKMoviePlayerOption = new TuSDKMoviePlayerOption();
    localTuSDKMoviePlayerOption.context = paramContext;
    localTuSDKMoviePlayerOption.movieUri = paramUri;
    localTuSDKMoviePlayerOption.surfaceView = paramSurfaceView;
    setOption(localTuSDKMoviePlayerOption);
  }
  
  public void initAudioPlayer(Context paramContext, Uri paramUri)
  {
    if ((paramContext == null) || (paramUri == null))
    {
      TLog.e("Parameter is not valid", new Object[0]);
      return;
    }
    TuSDKMoviePlayerOption localTuSDKMoviePlayerOption = new TuSDKMoviePlayerOption();
    localTuSDKMoviePlayerOption.context = paramContext;
    localTuSDKMoviePlayerOption.movieUri = paramUri;
    setOption(localTuSDKMoviePlayerOption);
  }
  
  private void a()
  {
    a(PlayerState.UNINITIALIZED);
    this.f = false;
    if (c() != null) {
      c().getHolder().addCallback(this.o);
    } else {
      a(this.g);
    }
  }
  
  private void a(SurfaceHolder paramSurfaceHolder)
  {
    a(PlayerState.INITIALIZING);
    this.b = new MediaPlayer();
    this.c = false;
    this.b.setScreenOnWhilePlaying(true);
    this.b.setOnPreparedListener(this);
    this.b.setOnBufferingUpdateListener(this);
    this.b.setOnCompletionListener(this);
    this.b.setOnVideoSizeChangedListener(this);
    this.b.setOnSeekCompleteListener(this);
    this.b.setOnErrorListener(this);
    b(paramSurfaceHolder);
  }
  
  private void b(SurfaceHolder paramSurfaceHolder)
  {
    if (this.b == null) {
      return;
    }
    this.b.reset();
    this.b.setAudioStreamType(3);
    try
    {
      if (paramSurfaceHolder != null) {
        this.b.setDisplay(paramSurfaceHolder);
      }
      this.b.setDataSource(b(), d());
      this.b.prepareAsync();
      this.b.setLooping(this.d);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void setVolume(float paramFloat)
  {
    if (this.b == null) {
      return;
    }
    this.b.setVolume(paramFloat, paramFloat);
  }
  
  public int getCurrentPosition()
  {
    if (this.b == null) {
      return 0;
    }
    return this.b.getCurrentPosition();
  }
  
  public int getDuration()
  {
    if (this.b == null) {
      return 0;
    }
    return this.b.getDuration();
  }
  
  public void setOption(TuSDKMoviePlayerOption paramTuSDKMoviePlayerOption)
  {
    if ((paramTuSDKMoviePlayerOption == null) || (paramTuSDKMoviePlayerOption.movieUri == null)) {
      return;
    }
    this.e = paramTuSDKMoviePlayerOption;
    a();
  }
  
  public void setDelegate(TuSDKMoviePlayerDelegate paramTuSDKMoviePlayerDelegate)
  {
    this.h = paramTuSDKMoviePlayerDelegate;
  }
  
  private void a(OnSeekToPreviewListener paramOnSeekToPreviewListener)
  {
    this.m = paramOnSeekToPreviewListener;
  }
  
  public void setLooping(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }
  
  private Context b()
  {
    if (this.e == null) {
      return null;
    }
    return this.e.context;
  }
  
  private SurfaceView c()
  {
    if (this.e == null) {
      return null;
    }
    return this.e.surfaceView;
  }
  
  private Uri d()
  {
    if (this.e == null) {
      return null;
    }
    return this.e.movieUri;
  }
  
  public void onPrepared(MediaPlayer paramMediaPlayer)
  {
    a(PlayerState.INITIALIZED);
    this.c = true;
    if (this.f)
    {
      this.f = false;
      start();
    }
  }
  
  public void onSeekComplete(MediaPlayer paramMediaPlayer)
  {
    if (this.m != null) {
      this.m.onSeekToComplete();
    }
    if (this.h != null) {
      this.h.onSeekComplete();
    }
  }
  
  public void onVideoSizeChanged(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2)
  {
    if (this.h != null) {
      this.h.onVideSizeChanged(paramMediaPlayer, paramInt1, paramInt2);
    }
  }
  
  public void onCompletion(MediaPlayer paramMediaPlayer)
  {
    if (this.h != null) {
      this.h.onCompletion();
    }
  }
  
  public void onBufferingUpdate(MediaPlayer paramMediaPlayer, int paramInt) {}
  
  public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2)
  {
    return false;
  }
  
  private void a(PlayerState paramPlayerState)
  {
    this.a = paramPlayerState;
    if (this.h != null) {
      this.h.onStateChanged(this.a);
    }
  }
  
  public boolean isUninitialized()
  {
    return this.a == PlayerState.UNINITIALIZED;
  }
  
  public boolean isInitializing()
  {
    return this.a == PlayerState.INITIALIZING;
  }
  
  public boolean isInitialized()
  {
    return this.a == PlayerState.INITIALIZED;
  }
  
  public boolean isPlaying()
  {
    return this.a == PlayerState.PLAYING;
  }
  
  public boolean isPaused()
  {
    return this.a == PlayerState.PAUSED;
  }
  
  public boolean isStoped()
  {
    return this.a == PlayerState.STOPED;
  }
  
  public void start()
  {
    if (isPlaying()) {
      return;
    }
    if ((c() != null) && (this.i))
    {
      this.f = true;
      c().getHolder().removeCallback(this.o);
      c().getHolder().addCallback(this.o);
      return;
    }
    if ((isInitializing()) || (!this.c))
    {
      this.f = true;
      return;
    }
    if ((isStoped()) || (isUninitialized()))
    {
      this.f = true;
      a(this.g);
      return;
    }
    if (this.b == null) {
      return;
    }
    if (this.b.isPlaying()) {
      this.b.stop();
    }
    this.a = PlayerState.PLAYING;
    this.b.start();
    this.j.postDelayed(this.p, this.k);
  }
  
  public void pause()
  {
    if (!isPlaying()) {
      return;
    }
    this.f = false;
    this.b.pause();
    a(PlayerState.PAUSED);
  }
  
  public void resume()
  {
    if (!isPaused()) {
      return;
    }
    start();
  }
  
  public void restart()
  {
    b(this.g);
    start();
  }
  
  public void seekTo(long paramLong)
  {
    if (this.b == null) {
      return;
    }
    this.b.seekTo((int)paramLong);
  }
  
  public void seekToPreview(int paramInt, final OnSeekToPreviewListener paramOnSeekToPreviewListener)
  {
    this.l = Math.min(paramInt, getDuration());
    this.n.removeCallbacks(null);
    this.n.postDelayed(new Runnable()
    {
      public void run()
      {
        if (TuSDKMoviePlayer.a(TuSDKMoviePlayer.this) == null) {
          return;
        }
        TuSDKMoviePlayer.c(TuSDKMoviePlayer.this).removeCallbacks(TuSDKMoviePlayer.b(TuSDKMoviePlayer.this));
        TuSDKMoviePlayer.this.start();
        TuSDKMoviePlayer.b(TuSDKMoviePlayer.this, new TuSDKMoviePlayer.OnSeekToPreviewListener()
        {
          public void onSeekToComplete()
          {
            TuSDKMoviePlayer.this.pause();
            if (TuSDKMoviePlayer.1.this.a != null) {
              TuSDKMoviePlayer.1.this.a.onSeekToComplete();
            }
            TuSDKMoviePlayer.a(TuSDKMoviePlayer.this, null);
          }
        });
        TuSDKMoviePlayer.this.seekTo(TuSDKMoviePlayer.d(TuSDKMoviePlayer.this));
      }
    }, 10L);
  }
  
  public void stop()
  {
    if ((isStoped()) || (isUninitialized()) || (this.b == null)) {
      return;
    }
    this.j.removeCallbacks(this.p);
    this.f = false;
    this.c = false;
    this.b.stop();
    e();
    a(PlayerState.STOPED);
  }
  
  public void destory()
  {
    stop();
    e();
  }
  
  private void e()
  {
    if (this.b == null) {
      return;
    }
    this.b.release();
    this.b = null;
    a(PlayerState.UNINITIALIZED);
  }
  
  public static class TuSDKMoviePlayerOption
  {
    public Context context;
    public Uri movieUri;
    public SurfaceView surfaceView;
  }
  
  public static abstract interface TuSDKMoviePlayerDelegate
  {
    public abstract void onStateChanged(TuSDKMoviePlayer.PlayerState paramPlayerState);
    
    public abstract void onVideSizeChanged(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2);
    
    public abstract void onProgress(int paramInt);
    
    public abstract void onSeekComplete();
    
    public abstract void onCompletion();
  }
  
  public static abstract interface OnSeekToPreviewListener
  {
    public abstract void onSeekToComplete();
  }
  
  public static enum PlayerState
  {
    private PlayerState() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\movie\player\TuSDKMoviePlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */