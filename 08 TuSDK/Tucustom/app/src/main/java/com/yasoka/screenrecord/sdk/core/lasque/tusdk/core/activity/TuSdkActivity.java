// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;

//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;

public class TuSdkActivity extends AppCompatActivity
{
    public <T extends View> T getViewById(final int n) {
        return TuSdkViewHelper.loadView(this.findViewById(n));
    }
    
    public <T extends View> T getViewById(final String s) {
        final int idResId = TuSdkContext.getIDResId(s);
        if (idResId == 0) {
            return null;
        }
        return (T)this.getViewById(idResId);
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.initView();
    }
    
    protected void initView() {
    }
}
