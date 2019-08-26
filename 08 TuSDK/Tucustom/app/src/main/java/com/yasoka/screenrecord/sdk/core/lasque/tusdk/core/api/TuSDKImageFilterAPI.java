// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api;

//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.util.List;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

public abstract class TuSDKImageFilterAPI
{
    protected abstract FilterWrap getFilterWrap();
    
    public List<String> getArgKeys() {
        if (this.getFilterWrap() == null) {
            return null;
        }
        return this.getFilterWrap().getFilterParameter().getArgKeys();
    }
    
    protected void submitFilterParameter() {
        if (this.getFilterWrap() == null) {
            return;
        }
        this.getFilterWrap().submitFilterParameter();
    }
    
    public boolean setFilterArgPrecentValue(final String s, final float precentValue) {
        if (this.getFilterWrap() == null) {
            return false;
        }
        final SelesParameters.FilterArg filterArg = this.getFilterWrap().getFilterParameter().getFilterArg(s);
        if (filterArg == null) {
            TLog.e("setFilterArgPrecentValue Key : %s  does not exist", s);
            return false;
        }
        filterArg.setPrecentValue(precentValue);
        return true;
    }
    
    public float getFilterArgPrecentValue(final String s) {
        if (this.getFilterWrap() == null) {
            return 0.0f;
        }
        final SelesParameters.FilterArg filterArg = this.getFilterWrap().getFilterParameter().getFilterArg(s);
        if (filterArg == null) {
            TLog.e("setFilterArg Invalid key : %s", s);
            return 0.0f;
        }
        return filterArg.getPrecentValue();
    }
    
    public void reset() {
        if (this.getFilterWrap() == null) {
            return;
        }
        this.getFilterWrap().getFilterParameter().reset();
    }
    
    public Bitmap process(final Bitmap bitmap) {
        return this.process(bitmap, ImageOrientation.Up);
    }
    
    public Bitmap process(final Bitmap bitmap, final ImageOrientation imageOrientation) {
        return this.process(bitmap, imageOrientation, 0.0f);
    }
    
    public final Bitmap process(final Bitmap bitmap, final ImageOrientation imageOrientation, final float n) {
        if (!this.a()) {
            return bitmap;
        }
        if (this.getFilterWrap() == null || bitmap == null) {
            return bitmap;
        }
        final Bitmap imageScale = BitmapHelper.imageScale(bitmap, TuSdkSize.create(bitmap).limitScale());
        this.submitFilterParameter();
        return this.getFilterWrap().process(imageScale, imageOrientation, n);
    }
    
    private boolean a() {
        if (!SdkValid.shared.sdkValid()) {
            TLog.e("Configuration not found! Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
            return false;
        }
        if (SdkValid.shared.isExpired()) {
            TLog.e("Your account has expired Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
            return false;
        }
        return true;
    }
}
