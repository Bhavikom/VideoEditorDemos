// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixerRender;
//import org.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixerRender;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderInfoWrap;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSdkMixerRender;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager;

import java.util.Iterator;
//import org.lasque.tusdk.core.secret.StatisticsManger;
import java.util.ArrayList;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;
import java.util.List;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSdkMixerRender;
//import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderInfoWrap;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public class TuSdkEditorAudioMixerImpl implements TuSdkEditorAudioMixer
{
    private TuSdkMediaDataSource a;
    private TuSDKAudioRenderInfoWrap b;
    private TuSDKAudioDecoderTaskManager c;
    private TuSdkMixerRender d;
    private List<TuSDKAudioRenderEntry> e;
    private List<TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener> f;
    private boolean g;
    private TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener h;
    
    public TuSdkEditorAudioMixerImpl() {
        this.e = new ArrayList<TuSDKAudioRenderEntry>();
        this.f = new ArrayList<TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener>();
        this.g = true;
        this.h = new TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener() {
            @Override
            public void onStateChanged(final TuSDKAudioDecoderTaskManager.State state) {
                final Iterator<TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener> iterator = TuSdkEditorAudioMixerImpl.this.f.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onStateChanged(state);
                }
                if (state == TuSDKAudioDecoderTaskManager.State.Complete) {
                    StatisticsManger.appendComponent(9449473L);
                }
            }
        };
        this.c = new TuSDKAudioDecoderTaskManager();
    }
    
    @Override
    public void setDataSource(final TuSdkMediaDataSource a) {
        this.a = a;
    }
    
    @Override
    public void addAudioRenderEntry(final TuSDKAudioRenderEntry tuSDKAudioRenderEntry) {
        if (tuSDKAudioRenderEntry == null) {
            TLog.e("%s audio entry is null !!! ", new Object[] { "TuSdkEditorAudioMixer" });
            return;
        }
        this.clearAllAudioData();
        this.e.add(tuSDKAudioRenderEntry);
    }
    
    @Override
    public void setAudioRenderEntryList(final List<TuSDKAudioRenderEntry> list) {
        if (list == null || list.size() == 0) {
            TLog.e("%s mix audio list is null !!!", new Object[] { "TuSdkEditorAudioMixer" });
            return;
        }
        this.clearAllAudioData();
        this.e.addAll(list);
    }
    
    @Override
    public void addTaskStateListener(final TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener tuSDKAudioDecoderTaskStateListener) {
        if (tuSDKAudioDecoderTaskStateListener == null) {
            return;
        }
        this.f.add(tuSDKAudioDecoderTaskStateListener);
    }
    
    @Override
    public void removeTaskStateListener(final TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener tuSDKAudioDecoderTaskStateListener) {
        if (tuSDKAudioDecoderTaskStateListener == null) {
            return;
        }
        this.f.remove(tuSDKAudioDecoderTaskStateListener);
    }
    
    @Override
    public void removeAllTaskStateListener() {
        this.f.clear();
    }
    
    @Override
    public void clearAllAudioData() {
        this.e.clear();
        if (this.d != null) {
            this.d.clearAllAudioData();
        }
    }
    
    @Override
    public void loadAudio() {
        this.c.setAudioEntry(this.e);
        this.c.setTrunkAudioInfo(this.a().getRealAudioInfo());
        if (this.h != null) {
            this.c.setDelegate(this.h);
        }
        this.c.start();
    }
    
    @Override
    public boolean isLoaded() {
        return this.c.getState() == TuSDKAudioDecoderTaskManager.State.Complete;
    }
    
    public void setIncludeMasterSound(final boolean g) {
        if (!g) {
            this.setMasterAudioTrack(0.0f);
        }
        this.g = g;
    }
    
    @Override
    public void setMasterAudioTrack(final float trunkAudioVolume) {
        if (this.d == null || trunkAudioVolume < 0.0f || !this.g) {
            return;
        }
        this.d.setTrunkAudioVolume(trunkAudioVolume);
    }
    
    @Override
    public void setSecondAudioTrack(final float secondAudioTrack) {
        if (this.d == null || secondAudioTrack < 0.0f) {
            return;
        }
        this.d.setSecondAudioTrack(secondAudioTrack);
    }
    
    public TuSdkMixerRender getMixerAudioRender() {
        if (this.d == null) {
            (this.d = new TuSdkMixerRender()).setRawAudioTrackList(this.b());
        }
        return this.d;
    }
    
    @Override
    public void destroy() {
        this.c.cancel();
        if (this.d != null) {
            this.d.clearAllAudioData();
        }
        this.h = null;
    }
    
    private TuSDKAudioRenderInfoWrap a() {
        if (this.a == null || !this.a.isValid()) {
            TLog.e("%s data source is invalid !!!", new Object[] { "TuSdkEditorAudioMixer" });
            return null;
        }
        if (this.b == null) {
            this.b = TuSDKAudioRenderInfoWrap.createWithMediaDataSource(this.a);
        }
        return this.b;
    }
    
    private List<TuSDKAudioMixerRender.RawAudioTrack> b() {
        ArrayList<TuSDKAudioMixerRender.RawAudioTrack> list = new ArrayList<TuSDKAudioMixerRender.RawAudioTrack>();
        if (list == null || list.size() != this.e.size()) {
            list = new ArrayList<TuSDKAudioMixerRender.RawAudioTrack>();
            for (int i = 0; i < this.e.size(); ++i) {
                list.add(new TuSDKAudioMixerRender.RawAudioTrack(this.e.get(i)));
            }
        }
        return list;
    }
    
    @Override
    public void notifyLoadCompleted() {
        if (this.d == null) {
            return;
        }
        this.d.setRawAudioTrackList(this.b());
    }
}
