// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors;

//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.List;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.secret.TuSdkImageNative;
import java.nio.ByteBuffer;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.TuSdkImageNative;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;

public class TuSDKColorHDRFilter extends SelesThreeInputFilter implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
    public static final int CLIP_X_NUM = 8;
    public static final int CLIP_Y_NUM = 8;
    private float a;
    private float b;
    private int c;
    private int d;
    private int e;
    private float f;
    
    public static ByteBuffer getClipHistBuffer(final Bitmap bitmap) {
        final ByteBuffer allocate = ByteBuffer.allocate(16384);
        TuSdkImageNative.getClipHistList(bitmap, 8, 8, 2.0f, allocate.array());
        return allocate;
    }
    
    public TuSDKColorHDRFilter() {
        super("-schdr");
        this.a = 0.125f;
        this.b = 0.125f;
        this.f = 0.5f;
        this.disableSecondFrameCheck();
        this.disableThirdFrameCheck();
    }
    
    public TuSDKColorHDRFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null && filterOption.args.containsKey("mixied")) {
            this.f = Float.parseFloat(filterOption.args.get("mixied"));
        }
    }
    
    @Override
    public void appendTextures(final List<SelesPicture> list) {
        if (list == null) {
            return;
        }
        int n = 1;
        for (final SelesPicture selesPicture : list) {
            selesPicture.processImage();
            selesPicture.addTarget(this, n);
            ++n;
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.c = this.mFilterProgram.uniformIndex("clipX");
        this.d = this.mFilterProgram.uniformIndex("clipY");
        this.e = this.mFilterProgram.uniformIndex("HDRStrength");
        this.a(this.a);
        this.b(this.b);
        this.setStrength(this.f);
    }
    
    private void a(final float a) {
        this.setFloat(this.a = a, this.c, this.mFilterProgram);
    }
    
    private void b(final float b) {
        this.setFloat(this.b = b, this.d, this.mFilterProgram);
    }
    
    public float getStrength() {
        return this.f;
    }
    
    public void setStrength(final float f) {
        this.setFloat(this.f = f, this.e, this.mFilterProgram);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("mixied", this.getStrength(), 0.0f, 1.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("mixied")) {
            this.setStrength(filterArg.getValue());
        }
    }
}
