// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.image;

//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkImageComposeItem;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
//import org.lasque.tusdk.core.utils.StringHelper;
import java.util.Collections;
import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.tusdk.TuSDKMediaTransitionWrap;
//import org.lasque.tusdk.video.editor.TuSdkMediaTransitionEffectData;
import java.util.Iterator;
//import org.lasque.tusdk.api.engine.TuSdkFilterEngineImpl;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.suit.TuSdkMediaSuit;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
//import org.lasque.tusdk.api.engine.TuSdkFilterEngine;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
//import org.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkComposeItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkComposeItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkImageComposeItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkMediaVideoComposer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.suit.TuSdkMediaSuit;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.TuSDKMediaTransitionWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine.TuSdkFilterEngine;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine.TuSdkFilterEngineImpl;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaTransitionEffectData;

import java.util.LinkedList;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import java.util.List;
//import org.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkMediaVideoComposer;

public class TuSDKMediaMovieCompositionComposer
{
    private static String a;
    private float b;
    private TuSdkMediaVideoComposer c;
    private List<ImageSqlInfo> d;
    private LinkedList<TuSdkComposeItem> e;
    private TuSdkMediaProgress f;
    private MediaFormat g;
    private MediaFormat h;
    private TuSdkFilterEngine i;
    private boolean j;
    private String k;
    private boolean l;
    private List<TuSdkMediaEffectData> m;
    
    public TuSDKMediaMovieCompositionComposer() {
        this(TuSDKMediaMovieCompositionComposer.a);
    }
    
    public TuSDKMediaMovieCompositionComposer(final String a) {
        this.b = 2.0f;
        this.j = false;
        this.l = true;
        TuSDKMediaMovieCompositionComposer.a = a;
        this.e = new LinkedList<TuSdkComposeItem>();
        this.g = TuSdkMediaFormat.buildSafeVideoEncodecFormat(TuSdkSize.create(1080, 1920), TuSdkVideoQuality.LIVE_HIGH1, 2130708361);
        this.h = TuSdkMediaFormat.buildSafeAudioEncodecFormat(44100, 1, 96000, 2);
        this.m = new LinkedList<TuSdkMediaEffectData>();
    }
    
    public void setImageSource(final List<ImageSqlInfo> d) {
        this.d = d;
    }
    
    public void setVideoFormat(final MediaFormat g) {
        if (g == null) {
            return;
        }
        this.g = g;
    }
    
    public void setMediaProgress(final TuSdkMediaProgress f) {
        this.f = f;
    }
    
    public void startExport() {
        if (this.c()) {
            if (this.c != null) {
                this.c.stop();
                this.c = null;
            }
            this.c = TuSdkMediaSuit.imageToVideo((LinkedList)this.a(this.d), TuSDKMediaMovieCompositionComposer.a, this.g, this.h, this.f, this.l, this.a());
        }
        else {
            TLog.e("start composer fail", new Object[0]);
        }
    }
    
    private TuSdkSurfaceRender a() {
        return (TuSdkSurfaceRender)new TuSdkSurfaceRender() {
            public void onSurfaceCreated() {
                if (TuSDKMediaMovieCompositionComposer.this.i == null) {
                    TuSDKMediaMovieCompositionComposer.this.i = new TuSdkFilterEngineImpl(false, true);
                    TuSDKMediaMovieCompositionComposer.this.i.onSurfaceCreated();
                }
                else {
                    TuSDKMediaMovieCompositionComposer.this.i.onSurfaceCreated();
                }
                TuSDKMediaMovieCompositionComposer.this.i.removeAllMediaEffects();
                TuSDKMediaMovieCompositionComposer.this.b();
            }
            
            public void onSurfaceChanged(final int n, final int n2) {
                TuSDKMediaMovieCompositionComposer.this.i.onSurfaceChanged(n, n2);
            }
            
            public void onSurfaceDestory() {
            }
            
            public int onDrawFrame(final int n, final int n2, final int n3, final long n4) {
                return TuSDKMediaMovieCompositionComposer.this.i.processFrame(n, n2, n3, n4);
            }
            
            public void onDrawFrameCompleted() {
            }
        };
    }
    
    public void cancelExport() {
        if (this.c != null) {
            this.c.stop();
        }
    }
    
    private void b() {
        final Iterator<TuSdkMediaEffectData> iterator = this.m.iterator();
        while (iterator.hasNext()) {
            this.i.addMediaEffectData(iterator.next());
        }
    }
    
    public boolean addMediaEffect(final TuSdkMediaTransitionEffectData tuSdkMediaTransitionEffectData) {
        return (tuSdkMediaTransitionEffectData.getEffectCode().equals((Object) TuSDKMediaTransitionWrap.TuSDKMediaTransitionType.TuSDKMediaTransitionTypePullInBottom) || tuSdkMediaTransitionEffectData.getEffectCode().equals((Object)TuSDKMediaTransitionWrap.TuSDKMediaTransitionType.TuSDKMediaTransitionTypePullInLeft) || tuSdkMediaTransitionEffectData.getEffectCode().equals((Object)TuSDKMediaTransitionWrap.TuSDKMediaTransitionType.TuSDKMediaTransitionTypePullInRight) || tuSdkMediaTransitionEffectData.getEffectCode().equals((Object)TuSDKMediaTransitionWrap.TuSDKMediaTransitionType.TuSDKMediaTransitionTypePullInTop)) && this.m.add(tuSdkMediaTransitionEffectData);
    }
    
    public void removeMediaEffect(final TuSdkMediaTransitionEffectData tuSdkMediaTransitionEffectData) {
        this.m.remove(tuSdkMediaTransitionEffectData);
    }
    
    public void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType other) {
        for (final TuSdkMediaEffectData tuSdkMediaEffectData : this.m) {
            if (tuSdkMediaEffectData.getMediaEffectType().equals(other)) {
                this.m.remove(tuSdkMediaEffectData);
            }
        }
    }
    
    public List<TuSdkMediaEffectData> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType other) {
        final ArrayList<TuSdkMediaEffectData> list = new ArrayList<TuSdkMediaEffectData>();
        for (final TuSdkMediaEffectData tuSdkMediaEffectData : this.m) {
            if (tuSdkMediaEffectData.getMediaEffectType().equals(other)) {
                list.add(tuSdkMediaEffectData);
            }
        }
        return Collections.unmodifiableList(list);
    }
    
    public List<TuSdkMediaEffectData> getAllMediaEffects() {
        return Collections.unmodifiableList((List<? extends TuSdkMediaEffectData>)this.m);
    }
    
    private boolean c() {
        try {
            if (this.d == null || this.e == null || this.d.isEmpty()) {
                return false;
            }
            if (this.f == null) {
                return false;
            }
            if (this.j || StringHelper.isBlank(TuSDKMediaMovieCompositionComposer.a)) {
                if (StringHelper.isBlank(this.k)) {
                    TuSDKMediaMovieCompositionComposer.a = AlbumHelper.getAlbumVideoFile().getPath();
                }
                else {
                    TuSDKMediaMovieCompositionComposer.a = AlbumHelper.getAlbumVideoFile(this.k).getPath();
                }
                TuSDKMediaMovieCompositionComposer.a = this.getOutputTempFilePath();
            }
            else {
                TuSDKMediaMovieCompositionComposer.a = this.getOutputTempFilePath();
            }
        }
        catch (NullPointerException ex) {
            TuSDKMediaMovieCompositionComposer.a = this.getOutputTempFilePath();
        }
        return true;
    }
    
    private LinkedList<TuSdkComposeItem> a(final List<ImageSqlInfo> list) {
        if (this.e != null && !this.e.isEmpty()) {
            this.e.clear();
        }
        for (final ImageSqlInfo imageSqlInfo : list) {
            final TuSdkImageComposeItem e = new TuSdkImageComposeItem();
            e.setDuration(this.b);
            e.setImagePath(imageSqlInfo.path);
            this.e.add((TuSdkComposeItem)e);
        }
        return this.e;
    }
    
    public void saveToAlbum(final boolean j) {
        this.j = j;
    }
    
    public boolean isSaveToAlbum() {
        return this.j;
    }
    
    public void setOutpuFilePath(final String a) {
        TuSDKMediaMovieCompositionComposer.a = a;
    }
    
    protected String getOutputTempFilePath() {
        return TuSdk.getAppTempPath().getPath() + "/LSQ_" + System.currentTimeMillis() + ".mp4";
    }
    
    public void setIsAllKeyFrame(final boolean l) {
        this.l = l;
    }
    
    public void setDuration(final float b) {
        this.b = b;
    }
    
    public float getDuration() {
        return this.b;
    }
    
    public TuSdkSize getRecommendOutputSize() {
        if (this.d == null || this.d.isEmpty()) {
            return new TuSdkSize(0, 0);
        }
        TuSdkSize tuSdkSize = this.d.get(0).size;
        for (final ImageSqlInfo imageSqlInfo : this.d) {
            if (imageSqlInfo.size.minSide() < tuSdkSize.minSide()) {
                tuSdkSize = imageSqlInfo.size;
            }
        }
        if (tuSdkSize.minSide() > 1080) {
            return new TuSdkSize(tuSdkSize.width / 2, tuSdkSize.height / 2);
        }
        return tuSdkSize;
    }
}
