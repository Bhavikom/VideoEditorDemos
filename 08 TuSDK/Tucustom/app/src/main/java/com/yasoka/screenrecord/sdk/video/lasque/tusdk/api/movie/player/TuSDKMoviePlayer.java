// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.movie.player;

//import org.lasque.tusdk.core.utils.TLog;
import android.view.SurfaceView;
import android.net.Uri;
import android.content.Context;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.media.MediaPlayer;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class TuSDKMoviePlayer implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener
{
    private PlayerState a;
    private MediaPlayer b;
    private boolean c;
    private boolean d;
    private TuSDKMoviePlayerOption e;
    private boolean f;
    private SurfaceHolder g;
    private TuSDKMoviePlayerDelegate h;
    private boolean i;
    private Handler j;
    private int k;
    private int l;
    private OnSeekToPreviewListener m;
    private Handler n;
    private SurfaceHolder.Callback o;
    private Runnable p;
    
    public TuSDKMoviePlayer() {
        this.a = PlayerState.UNINITIALIZED;
        this.c = false;
        this.d = true;
        this.j = new Handler();
        this.k = 20;
        this.n = new Handler();
        this.o = (SurfaceHolder.Callback)new SurfaceHolder.Callback() {
            public void surfaceCreated(final SurfaceHolder surfaceHolder) {
                TuSDKMoviePlayer.this.i = false;
                TuSDKMoviePlayer.this.g = surfaceHolder;
                TuSDKMoviePlayer.this.a(surfaceHolder);
            }
            
            public void surfaceChanged(final SurfaceHolder surfaceHolder, final int n, final int n2, final int n3) {
            }
            
            public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
                TuSDKMoviePlayer.this.i = true;
            }
        };
        this.p = new Runnable() {
            @Override
            public void run() {
                TuSDKMoviePlayer.this.j.removeCallbacks(TuSDKMoviePlayer.this.p);
                if (TuSDKMoviePlayer.this.b != null && TuSDKMoviePlayer.this.b.isPlaying() && TuSDKMoviePlayer.this.h != null) {
                    TuSDKMoviePlayer.this.h.onProgress(100 * TuSDKMoviePlayer.this.b.getCurrentPosition() / TuSDKMoviePlayer.this.b.getDuration());
                }
                TuSDKMoviePlayer.this.j.postDelayed(TuSDKMoviePlayer.this.p, (long)TuSDKMoviePlayer.this.k);
            }
        };
    }
    
    public static TuSDKMoviePlayer createMoviePlayer() {
        return new TuSDKMoviePlayer();
    }
    
    public void initVideoPlayer(final Context context, final Uri movieUri, final SurfaceView surfaceView) {
        if (context == null || movieUri == null || surfaceView == null) {
            TLog.e("Parameter is not valid", new Object[0]);
            return;
        }
        final TuSDKMoviePlayerOption option = new TuSDKMoviePlayerOption();
        option.context = context;
        option.movieUri = movieUri;
        option.surfaceView = surfaceView;
        this.setOption(option);
    }
    
    public void initAudioPlayer(final Context context, final Uri movieUri) {
        if (context == null || movieUri == null) {
            TLog.e("Parameter is not valid", new Object[0]);
            return;
        }
        final TuSDKMoviePlayerOption option = new TuSDKMoviePlayerOption();
        option.context = context;
        option.movieUri = movieUri;
        this.setOption(option);
    }
    
    private void a() {
        this.a(PlayerState.UNINITIALIZED);
        this.f = false;
        if (this.c() != null) {
            this.c().getHolder().addCallback(this.o);
        }
        else {
            this.a(this.g);
        }
    }
    
    private void a(final SurfaceHolder surfaceHolder) {
        this.a(PlayerState.INITIALIZING);
        this.b = new MediaPlayer();
        this.c = false;
        this.b.setScreenOnWhilePlaying(true);
        this.b.setOnPreparedListener((MediaPlayer.OnPreparedListener)this);
        this.b.setOnBufferingUpdateListener((MediaPlayer.OnBufferingUpdateListener)this);
        this.b.setOnCompletionListener((MediaPlayer.OnCompletionListener)this);
        this.b.setOnVideoSizeChangedListener((MediaPlayer.OnVideoSizeChangedListener)this);
        this.b.setOnSeekCompleteListener((MediaPlayer.OnSeekCompleteListener)this);
        this.b.setOnErrorListener((MediaPlayer.OnErrorListener)this);
        this.b(surfaceHolder);
    }
    
    private void b(final SurfaceHolder display) {
        if (this.b == null) {
            return;
        }
        this.b.reset();
        this.b.setAudioStreamType(3);
        try {
            if (display != null) {
                this.b.setDisplay(display);
            }
            this.b.setDataSource(this.b(), this.d());
            this.b.prepareAsync();
            this.b.setLooping(this.d);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setVolume(final float n) {
        if (this.b == null) {
            return;
        }
        this.b.setVolume(n, n);
    }
    
    public int getCurrentPosition() {
        if (this.b == null) {
            return 0;
        }
        return this.b.getCurrentPosition();
    }
    
    public int getDuration() {
        if (this.b == null) {
            return 0;
        }
        return this.b.getDuration();
    }
    
    public void setOption(final TuSDKMoviePlayerOption e) {
        if (e == null || e.movieUri == null) {
            return;
        }
        this.e = e;
        this.a();
    }
    
    public void setDelegate(final TuSDKMoviePlayerDelegate h) {
        this.h = h;
    }
    
    private void a(final OnSeekToPreviewListener m) {
        this.m = m;
    }
    
    public void setLooping(final boolean d) {
        this.d = d;
    }
    
    private Context b() {
        if (this.e == null) {
            return null;
        }
        return this.e.context;
    }
    
    private SurfaceView c() {
        if (this.e == null) {
            return null;
        }
        return this.e.surfaceView;
    }
    
    private Uri d() {
        if (this.e == null) {
            return null;
        }
        return this.e.movieUri;
    }
    
    public void onPrepared(final MediaPlayer mediaPlayer) {
        this.a(PlayerState.INITIALIZED);
        this.c = true;
        if (this.f) {
            this.f = false;
            this.start();
        }
    }
    
    public void onSeekComplete(final MediaPlayer mediaPlayer) {
        if (this.m != null) {
            this.m.onSeekToComplete();
        }
        if (this.h != null) {
            this.h.onSeekComplete();
        }
    }
    
    public void onVideoSizeChanged(final MediaPlayer mediaPlayer, final int n, final int n2) {
        if (this.h != null) {
            this.h.onVideSizeChanged(mediaPlayer, n, n2);
        }
    }
    
    public void onCompletion(final MediaPlayer mediaPlayer) {
        if (this.h != null) {
            this.h.onCompletion();
        }
    }
    
    public void onBufferingUpdate(final MediaPlayer mediaPlayer, final int n) {
    }
    
    public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
        return false;
    }
    
    private void a(final PlayerState a) {
        this.a = a;
        if (this.h != null) {
            this.h.onStateChanged(this.a);
        }
    }
    
    public boolean isUninitialized() {
        return this.a == PlayerState.UNINITIALIZED;
    }
    
    public boolean isInitializing() {
        return this.a == PlayerState.INITIALIZING;
    }
    
    public boolean isInitialized() {
        return this.a == PlayerState.INITIALIZED;
    }
    
    public boolean isPlaying() {
        return this.a == PlayerState.PLAYING;
    }
    
    public boolean isPaused() {
        return this.a == PlayerState.PAUSED;
    }
    
    public boolean isStoped() {
        return this.a == PlayerState.STOPED;
    }
    
    public void start() {
        if (this.isPlaying()) {
            return;
        }
        if (this.c() != null && this.i) {
            this.f = true;
            this.c().getHolder().removeCallback(this.o);
            this.c().getHolder().addCallback(this.o);
            return;
        }
        if (this.isInitializing() || !this.c) {
            this.f = true;
            return;
        }
        if (this.isStoped() || this.isUninitialized()) {
            this.f = true;
            this.a(this.g);
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
        this.j.postDelayed(this.p, (long)this.k);
    }
    
    public void pause() {
        if (!this.isPlaying()) {
            return;
        }
        this.f = false;
        this.b.pause();
        this.a(PlayerState.PAUSED);
    }
    
    public void resume() {
        if (!this.isPaused()) {
            return;
        }
        this.start();
    }
    
    public void restart() {
        this.b(this.g);
        this.start();
    }
    
    public void seekTo(final long n) {
        if (this.b == null) {
            return;
        }
        this.b.seekTo((int)n);
    }
    
    public void seekToPreview(final int a, final OnSeekToPreviewListener onSeekToPreviewListener) {
        this.l = Math.min(a, this.getDuration());
        this.n.removeCallbacks((Runnable)null);
        this.n.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                if (TuSDKMoviePlayer.this.b == null) {
                    return;
                }
                TuSDKMoviePlayer.this.j.removeCallbacks(TuSDKMoviePlayer.this.p);
                TuSDKMoviePlayer.this.start();
                TuSDKMoviePlayer.this.a(new OnSeekToPreviewListener() {
                    @Override
                    public void onSeekToComplete() {
                        TuSDKMoviePlayer.this.pause();
                        if (onSeekToPreviewListener != null) {
                            onSeekToPreviewListener.onSeekToComplete();
                        }
                        TuSDKMoviePlayer.this.m = null;
                    }
                });
                TuSDKMoviePlayer.this.seekTo(TuSDKMoviePlayer.this.l);
            }
        }, 10L);
    }
    
    public void stop() {
        if (this.isStoped() || this.isUninitialized() || this.b == null) {
            return;
        }
        this.j.removeCallbacks(this.p);
        this.f = false;
        this.c = false;
        this.b.stop();
        this.e();
        this.a(PlayerState.STOPED);
    }
    
    public void destory() {
        this.stop();
        this.e();
    }
    
    private void e() {
        if (this.b == null) {
            return;
        }
        this.b.release();
        this.b = null;
        this.a(PlayerState.UNINITIALIZED);
    }
    
    public static class TuSDKMoviePlayerOption
    {
        public Context context;
        public Uri movieUri;
        public SurfaceView surfaceView;
    }
    
    public interface TuSDKMoviePlayerDelegate
    {
        void onStateChanged(final PlayerState p0);
        
        void onVideSizeChanged(final MediaPlayer p0, final int p1, final int p2);
        
        void onProgress(final int p0);
        
        void onSeekComplete();
        
        void onCompletion();
    }
    
    public enum PlayerState
    {
        UNINITIALIZED, 
        INITIALIZING, 
        INITIALIZED, 
        PLAYING, 
        PAUSED, 
        STOPED;
    }
    
    public interface OnSeekToPreviewListener
    {
        void onSeekToComplete();
    }
}
