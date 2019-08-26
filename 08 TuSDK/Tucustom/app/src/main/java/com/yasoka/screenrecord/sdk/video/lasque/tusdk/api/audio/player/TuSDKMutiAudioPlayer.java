// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.player;

//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
//import org.lasque.tusdk.core.decoder.TuSDKAudioInfo;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAverageAudioMixer;
//import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManagaer;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixer;
import android.media.AudioTrack;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixer;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAverageAudioMixer;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManagaer;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKAudioInfo;

import java.util.List;

public class TuSDKMutiAudioPlayer
{
    private List<TuSDKAudioEntry> a;
    private AudioTrack b;
    private TuSDKAudioMixer c;
    private TuSDKMutiAudioPlayerDelegate d;
    private boolean e;
    private State f;
    private TuSDKAudioDecoderTaskManagaer g;
    private TuSDKAudioMixer.OnAudioMixerDelegate h;
    
    public TuSDKMutiAudioPlayer() {
        this.c = new TuSDKAverageAudioMixer();
        this.e = false;
        this.f = State.Idle;
        this.g = new TuSDKAudioDecoderTaskManagaer();
        this.h = new TuSDKAudioMixer.OnAudioMixerDelegate() {
            @Override
            public void onMixed(final byte[] array) {
                TuSDKMutiAudioPlayer.this.write(array);
            }
            
            @Override
            public void onMixingError(final int n) {
            }
            
            @Override
            public void onReayTrunkTrackInfo(final TuSDKAudioInfo tuSDKAudioInfo) {
                TuSDKMutiAudioPlayer.this.b = TuSDKMutiAudioPlayer.this.a(tuSDKAudioInfo);
            }
            
            @Override
            public void onStateChanged(final TuSDKAudioMixer.State state) {
                if (state == TuSDKAudioMixer.State.Mixing) {
                    TuSDKMutiAudioPlayer.this.b();
                }
                else if (state == TuSDKAudioMixer.State.Complete) {
                    TuSDKMutiAudioPlayer.this.c();
                }
            }
        };
    }
    
    private AudioTrack a(final TuSDKAudioInfo tuSDKAudioInfo) {
        return new AudioTrack(3, tuSDKAudioInfo.sampleRate, tuSDKAudioInfo.channelConfig, tuSDKAudioInfo.audioFormat, AudioTrack.getMinBufferSize(tuSDKAudioInfo.sampleRate, tuSDKAudioInfo.channelConfig, tuSDKAudioInfo.audioFormat), 1);
    }
    
    public void asyncPrepare(final List<TuSDKAudioEntry> c) {
        if (this.f == State.Playing || this.f == State.PreParing) {
            return;
        }
        this.stop();
        this.a = new ArrayList<TuSDKAudioEntry>(c);
        if (this.a == null || this.a.size() == 0) {
            TLog.e("%s : Please set a valid file path", new Object[] { this });
            return;
        }
        this.g.setAudioEntry(this.a);
        this.g.setDelegate(new TuSDKAudioDecoderTaskManagaer.TuSDKAudioDecoderTaskMangaerDelegate() {
            @Override
            public void onStateChanged(final TuSDKAudioDecoderTaskManagaer.State state) {
                if (state == TuSDKAudioDecoderTaskManagaer.State.Complete) {
                    if (TuSDKMutiAudioPlayer.this.f == State.PreParing) {
                        TuSDKMutiAudioPlayer.this.a(State.PrePared);
                    }
                }
                else if (state == TuSDKAudioDecoderTaskManagaer.State.Decoding) {
                    TuSDKMutiAudioPlayer.this.a(State.PreParing);
                }
                else if (state == TuSDKAudioDecoderTaskManagaer.State.Cancelled) {
                    TuSDKMutiAudioPlayer.this.a(State.Idle);
                }
            }
        });
        this.g.start();
    }
    
    public TuSDKMutiAudioPlayer setLooping(final boolean e) {
        this.e = e;
        return this;
    }
    
    public State getState() {
        return this.f;
    }
    
    private void a() {
        if (this.a == null || this.a.size() == 0) {
            TLog.e("%s : Please set a valid file path", new Object[] { this });
            return;
        }
        this.c.setOnAudioMixDelegate(this.h);
        this.c.mixAudios(this.a);
    }
    
    public void start() {
        if (this.f == State.Playing || this.f == State.PreParing) {
            return;
        }
        this.a();
    }
    
    private void b() {
        if (this.d().getState() != 0) {
            this.d().play();
            this.a(State.Playing);
        }
    }
    
    public void stop() {
        final boolean e = this.e;
        this.e = false;
        this.c();
        this.e = e;
    }
    
    private void c() {
        if (this.c != null) {
            this.c.cancel();
        }
        if (this.b != null) {
            if (this.b.getState() != 0) {
                this.b.stop();
                this.b.release();
            }
            this.b = null;
        }
        if (this.g != null) {
            this.g.cancel();
        }
        if (this.e) {
            this.a();
            return;
        }
        if (this.f == State.Playing) {
            this.a(State.Complete);
        }
        this.f = State.Idle;
    }
    
    public void write(final byte[] array, final int n, final int n2) {
        if (array == null || this.b == null || this.f != State.Playing) {
            return;
        }
        this.b.write(array, n, n2);
    }
    
    public void write(final byte[] array) {
        this.write(array, 0, array.length);
    }
    
    private void a(final State f) {
        if (this.f == f) {
            return;
        }
        this.f = f;
        if (this.d != null) {
            this.d.onStateChanged(f);
        }
    }
    
    private AudioTrack d() {
        if (this.b == null || this.f == State.Complete || this.b.getState() == 0) {
            this.b = this.a(TuSDKAudioInfo.defaultAudioInfo());
        }
        return this.b;
    }
    
    public TuSDKMutiAudioPlayer setAudioMixer(final TuSDKAudioMixer c) {
        this.c = c;
        return this;
    }
    
    public TuSDKMutiAudioPlayerDelegate getDelegate() {
        return this.d;
    }
    
    public TuSDKMutiAudioPlayer setDelegate(final TuSDKMutiAudioPlayerDelegate d) {
        this.d = d;
        return this;
    }
    
    public enum State
    {
        Idle, 
        PreParing, 
        PrePared, 
        Playing, 
        Complete;
    }
    
    public interface TuSDKMutiAudioPlayerDelegate
    {
        void onStateChanged(final State p0);
    }
}
