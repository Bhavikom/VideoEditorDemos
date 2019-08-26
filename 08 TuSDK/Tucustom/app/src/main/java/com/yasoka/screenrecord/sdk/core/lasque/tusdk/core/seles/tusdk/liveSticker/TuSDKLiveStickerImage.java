// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker;

//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import java.io.File;
//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
import java.util.Iterator;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.sticker.StickerPositionInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.LiveStickerLoader;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.StickerPositionInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;

import java.util.ArrayList;
import java.util.List;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
//import org.lasque.tusdk.core.sticker.LiveStickerLoader;

public class TuSDKLiveStickerImage
{
    private LiveStickerLoader a;
    private boolean b;
    private int c;
    private int d;
    private int e;
    private boolean f;
    private long g;
    private StickerData h;
    private boolean i;
    private boolean j;
    private final List<TuSDKStickerAnimationItem> k;
    
    public TuSDKLiveStickerImage(final LiveStickerLoader a) {
        this.f = true;
        this.k = new ArrayList<TuSDKStickerAnimationItem>();
        this.a = a;
    }
    
    public boolean isActived() {
        return this.i;
    }
    
    public boolean isEnabled() {
        return this.j;
    }
    
    public void updateSticker(final StickerData h) {
        if (h.getType() != StickerData.StickerType.TypeDynamic) {
            return;
        }
        final StickerPositionInfo positionInfo = h.positionInfo;
        this.h = h;
        this.e = ((positionInfo != null) ? positionInfo.frameInterval : 0);
        if (this.e <= 0) {
            this.e = 100;
        }
        this.reset();
        this.i = true;
        this.c();
    }
    
    public StickerData getSticker() {
        return this.h;
    }
    
    public void removeSticker() {
        if (this.h != null) {
            final Bitmap image = this.h.getImage();
            this.h.setImage(null);
            if (image != null && !image.isRecycled()) {
                image.recycle();
            }
        }
        this.h = null;
        this.reset();
    }
    
    public int getCurrentTextureID() {
        if (!this.i) {
            return 0;
        }
        if (this.f) {
            this.d = this.a(System.currentTimeMillis());
        }
        if (this.d >= this.k.size()) {
            return 0;
        }
        return this.k.get(this.d).textureID;
    }
    
    public TuSdkSize getTextureSize() {
        if (!this.i || this.k == null || this.k.size() == 0) {
            return null;
        }
        return this.k.get(0).imageSize;
    }
    
    public void setCurrentFrameIndex(final int d) {
        if (d < 0) {
            return;
        }
        this.d = d;
    }
    
    public int getCurrentFrameIndex() {
        return this.d;
    }
    
    public void seekStickerToFrameTime(final long n) {
        if (n < 0L) {
            return;
        }
        this.d = this.a(n);
    }
    
    public void setBenchmarkTime(final long g) {
        this.g = g;
    }
    
    public void setEnableAutoplayMode(final boolean f) {
        if (f == this.f) {
            return;
        }
        this.f = f;
        this.b();
    }

    public void reset() {
        this.b();
        this.c = 0;
        if (this.k.size() > 0) {
            this.j = false;
            ArrayList var1 = new ArrayList(this.k);
            this.k.clear();
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
                TuSDKLiveStickerImage.TuSDKStickerAnimationItem var3 = (TuSDKLiveStickerImage.TuSDKStickerAnimationItem)var2.next();
                var3.destory();
            }
        }

        if (!this.b) {
            this.i = false;
        }

        this.b = false;
    }
    
    @Override
    protected void finalize() {
        this.reset();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    private void a() {
        if (!this.f || this.d > 0 || this.g > 0L) {
            return;
        }
        this.d = 0;
        this.setBenchmarkTime(System.currentTimeMillis());
    }
    
    private void b() {
        this.setBenchmarkTime(0L);
        this.d = 0;
    }
    
    private int a(final long n) {
        if (n < 0L || this.g <= 0L || n < this.g || this.e == 0 || this.k.size() == 0) {
            return 0;
        }
        int n2 = (int)Math.floor((n - this.g) / (float)this.e);
        if (n2 > this.k.size() - 1) {
            if (this.h.positionInfo.loopStartIndex > 0 && this.h.positionInfo.loopStartIndex < this.k.size()) {
                final int loopStartIndex = this.h.positionInfo.loopStartIndex;
                n2 = (n2 - loopStartIndex) % (this.k.size() - loopStartIndex) + loopStartIndex;
            }
            else {
                n2 %= this.k.size();
            }
        }
        return n2;
    }
    
    private void c() {
        this.b = true;
        final StickerPositionInfo positionInfo = this.h.positionInfo;
        if (positionInfo != null && positionInfo.hasAnimationSupported()) {
            this.nextTextureLoadTask();
        }
        else {
            this.a(this.h.stickerImageName);
        }
    }
    
    protected void nextTextureLoadTask() {
        this.a(this.h.positionInfo.resourceList.get(this.c));
    }
    
    private void a(final String var1) {
        if (this.a != null) {
            this.a.loadImage(new Runnable() {
                public void run() {
                    final Bitmap var1x;
                    if (TuSDKLiveStickerImage.this.h.getImage() != null) {
                        var1x = TuSDKLiveStickerImage.this.h.getImage();
                    } else if (var1.toLowerCase().endsWith(".png")) {
                        StickerGroup var2 = StickerLocalPackage.shared().getStickerGroup(TuSDKLiveStickerImage.this.h.groupId);
                        String var3 = TuSdk.getAppTempPath() + File.separator + var2.file.substring(0, var2.file.lastIndexOf("."));
                        var3 = var3 + File.separator + TuSDKLiveStickerImage.this.h.stickerId + File.separator + var1;
                        var1x = BitmapHelper.getBitmap(new File(var3));
                    } else {
                        var1x = StickerLocalPackage.shared().loadSmartStickerItem(TuSDKLiveStickerImage.this.h, var1);
                    }

                    TuSDKLiveStickerImage.this.runOnGLContext(new Runnable() {
                        public void run() {
                            TuSDKLiveStickerImage.this.a(var1x);
                        }
                    });
                }
            });
        }
    }
    
    private void a(final SelesFramebuffer selesFramebuffer) {
        if (!this.b) {
            selesFramebuffer.destroy();
            this.i = false;
            return;
        }
        this.k.add(new TuSDKStickerAnimationItem(selesFramebuffer));
        final StickerPositionInfo positionInfo = this.h.positionInfo;
        if (positionInfo != null && positionInfo.hasAnimationSupported()) {
            ++this.c;
            final int size = positionInfo.resourceList.size();
            if (this.c >= size) {
                this.d();
            }
            else {
                final int min = Math.min(5, size);
                if (!this.j && this.c > min) {
                    this.j = true;
                    this.a();
                }
                this.nextTextureLoadTask();
            }
        }
        else {
            this.d();
        }
    }
    
    private void d() {
        this.j = true;
        this.b = false;
        this.c = 0;
        final StickerPositionInfo positionInfo = this.h.positionInfo;
        if (positionInfo != null && positionInfo.hasAnimationSupported()) {
            this.a();
        }
    }
    
    private void a(final Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        final boolean b = false;
        TuSdkSize tuSdkSize = TuSdkSize.create(bitmap);
        if (tuSdkSize.minSide() <= 0) {
            TLog.e("Passed image must not be empty - it should be at least 1px tall and wide", new Object[0]);
            return;
        }
        if (b) {
            tuSdkSize = SelesContext.sizeThatFitsWithinATexture(tuSdkSize.copy());
        }
        final SelesFramebuffer fetchTexture = SelesContext.sharedFramebufferCache().fetchTexture(tuSdkSize, false);
        fetchTexture.bindTexture(bitmap, b, true);
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                TuSDKLiveStickerImage.this.a(fetchTexture);
            }
        });
    }
    
    protected void runOnGLContext(final Runnable runnable) {
        if (runnable == null || this.a == null) {
            return;
        }
        this.a.uploadTexture(runnable);
    }
    
    public final class TuSDKStickerAnimationItem
    {
        public TuSdkSize imageSize;
        public int textureID;
        private SelesFramebuffer b;
        
        public TuSDKStickerAnimationItem(final SelesFramebuffer b) {
            this.textureID = b.getTexture();
            this.imageSize = b.getSize();
            this.b = b;
        }
        
        public void destory() {
            if (this.b != null) {
                this.b.destroy();
                this.b = null;
            }
        }
        
        @Override
        protected void finalize() {
            this.destory();
            try {
                super.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
