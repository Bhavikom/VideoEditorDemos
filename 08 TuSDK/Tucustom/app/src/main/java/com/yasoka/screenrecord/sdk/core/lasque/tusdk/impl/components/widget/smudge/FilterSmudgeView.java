// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.smudge;

//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.utils.TLog;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.secret.SdkValid;
import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class FilterSmudgeView extends SmudgeView
{
    public FilterSmudgeView(final Context context) {
        super(context);
    }
    
    public FilterSmudgeView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public FilterSmudgeView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    protected SimpleProcessor getProcessorInstance() {
        if (!SdkValid.shared.wipeFilterEnabled()) {
            return null;
        }
        if (this.mSmudgeProcessor == null) {
            this.mSmudgeProcessor = new FilterSmudgeProcessor();
        }
        return this.mSmudgeProcessor;
    }
    
    @Override
    public void setImageBitmap(final Bitmap imageBitmap) {
        if (!SdkValid.shared.wipeFilterEnabled()) {
            TLog.e("You are not allowed to use the wipe-filter feature, please see http://tusdk.com", new Object[0]);
            return;
        }
        super.setImageBitmap(imageBitmap);
    }
    
    public FilterWrap getFilterWrap() {
        if (this.getProcessorInstance() != null) {
            return ((FilterSmudgeProcessor)this.getProcessorInstance()).getFilterWrap();
        }
        return null;
    }
    
    public final void setFilterWrap(final FilterWrap filterWrap) {
        if (this.getProcessorInstance() != null) {
            ((FilterSmudgeProcessor)this.getProcessorInstance()).setFilterWrap(filterWrap);
        }
    }
    
    @Override
    protected void updateBrushSettings() {
        if (this.getProcessorInstance() == null) {
            return;
        }
        this.getProcessorInstance().setBrushSize(this.getBrushSize());
    }
}
