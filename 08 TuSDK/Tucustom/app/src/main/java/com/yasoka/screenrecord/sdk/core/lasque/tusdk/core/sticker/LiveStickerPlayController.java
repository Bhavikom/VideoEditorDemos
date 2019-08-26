// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLContext;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
import java.util.List;

public class LiveStickerPlayController
{
    public static final int LIVE_STICKER_MAX_NUM = 5;
    private LiveStickerLoader a;
    private List<TuSDKLiveStickerImage> b;
    private List<TuSDKLiveStickerImage> c;
    private List<StickerData> d;
    private boolean e;
    
    public LiveStickerPlayController(final EGLContext eglContext) {
        if (eglContext == null) {
            return;
        }
        this.d = new ArrayList<StickerData>();
        this.c = new ArrayList<TuSDKLiveStickerImage>();
        this.b = new ArrayList<TuSDKLiveStickerImage>();
        this.a = new LiveStickerLoader(eglContext);
    }
    
    public LiveStickerLoader getLiveStickerLoader() {
        return this.a;
    }
    
    public void destroy() {
        if (this.a != null) {
            this.a.destroy();
            this.a = null;
        }
        this.removeAllStickers();
        if (this.c != null) {
            this.c.clear();
            this.c = null;
        }
        this.b = null;
        this.d = null;
    }
    
    private boolean a(final StickerData stickerData) {
        if (this.a == null) {
            return false;
        }
        if (this.e) {
            this.removeAllStickers();
            this.e = false;
        }
        if (this.b(stickerData)) {
            return false;
        }
        this.d.add(stickerData);
        TuSDKLiveStickerImage a = this.a();
        if (a == null) {
            a = new TuSDKLiveStickerImage(this.a);
        }
        a.updateSticker(stickerData);
        this.b.add(a);
        return true;
    }
    
    private boolean b(final StickerData stickerData) {
        return this.d != null && this.d.size() > 0 && this.d.contains(stickerData);
    }
    
    public void removeSticker(final StickerData stickerData) {
        if (stickerData == null || !this.b(stickerData)) {
            return;
        }
        this.d.remove(stickerData);
        final TuSDKLiveStickerImage c = this.c(stickerData);
        if (c == null) {
            return;
        }
        c.removeSticker();
        this.b.remove(c);
        this.c.add(c);
    }
    
    private TuSDKLiveStickerImage c(final StickerData stickerData) {
        if (this.b == null || this.b.size() <= 0) {
            return null;
        }
        for (int i = 0; i < this.b.size(); ++i) {
            final TuSDKLiveStickerImage obj = this.b.get(i);
            if (obj.equals(obj)) {
                return obj;
            }
        }
        return null;
    }
    
    private TuSDKLiveStickerImage a() {
        if (this.c == null || this.c.size() == 0) {
            return null;
        }
        for (int i = 0; i < this.c.size(); ++i) {
            final TuSDKLiveStickerImage tuSDKLiveStickerImage = this.c.get(i);
            if (!tuSDKLiveStickerImage.isActived() && !tuSDKLiveStickerImage.isEnabled()) {
                return this.c.remove(i);
            }
        }
        return null;
    }
    
    public boolean showGroupSticker(final StickerGroup stickerGroup) {
        if (this.isGroupStickerUsed(stickerGroup)) {
            TLog.e("The sticker group has already been used", new Object[0]);
            return false;
        }
        if (stickerGroup.stickers == null || stickerGroup.stickers.size() <= 0) {
            TLog.e("invalid sticker group", new Object[0]);
            return false;
        }
        this.removeAllStickers();
        this.e = false;
        for (int i = 0; i < stickerGroup.stickers.size(); ++i) {
            this.a(stickerGroup.stickers.get(i));
        }
        return this.e = true;
    }
    
    public boolean isGroupStickerUsed(final StickerGroup stickerGroup) {
        return this.e && this.d != null && this.d.size() > 0 && this.d.get(0).groupId == stickerGroup.groupId;
    }
    
    public long getCurrentGroupId() {
        if (this.e && this.d != null && this.d.size() > 0) {
            return this.d.get(0).groupId;
        }
        return -1L;
    }
    
    public void removeAllStickers() {
        if (this.b == null || this.b.size() == 0) {
            return;
        }
        for (int i = 0; i < this.b.size(); ++i) {
            final TuSDKLiveStickerImage tuSDKLiveStickerImage = this.b.get(i);
            tuSDKLiveStickerImage.reset();
            this.c.add(tuSDKLiveStickerImage);
        }
        this.b.clear();
        this.d.clear();
        this.e = false;
    }
    
    public void pauseAllStickers() {
        this.a(true);
    }
    
    public void resumeAllStickers() {
        this.a(false);
    }
    
    private void a(final boolean b) {
        if (this.b == null || this.b.size() == 0) {
            return;
        }
        for (int i = 0; i < this.b.size(); ++i) {
            this.b.get(i).setEnableAutoplayMode(!b);
        }
    }
    
    public List<TuSDKLiveStickerImage> getStickers() {
        return this.b;
    }
}
