// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity;

//import org.lasque.tusdk.modules.components.TuSdkComponentOption;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.TuSdkComponentOption;

public abstract class TuResultOption extends TuSdkComponentOption
{
    private boolean a;
    private boolean b;
    private String c;
    private int d;
    
    public TuResultOption() {
        this.d = 90;
    }
    
    public boolean isSaveToTemp() {
        return this.a;
    }
    
    public void setSaveToTemp(final boolean a) {
        this.a = a;
    }
    
    public boolean isSaveToAlbum() {
        return this.b;
    }
    
    public void setSaveToAlbum(final boolean b) {
        this.b = b;
    }
    
    public String getSaveToAlbumName() {
        return this.c;
    }
    
    public void setSaveToAlbumName(final String c) {
        this.c = c;
    }
    
    public int getOutputCompress() {
        if (this.d < 0) {
            this.d = 0;
        }
        else if (this.d > 100) {
            this.d = 100;
        }
        return this.d;
    }
    
    public void setOutputCompress(final int d) {
        this.d = d;
    }
    
    private void a(final TuResultFragment tuResultFragment) {
        if (tuResultFragment == null) {
            return;
        }
        tuResultFragment.setSaveToTemp(this.isSaveToTemp());
        tuResultFragment.setSaveToAlbum(this.isSaveToAlbum());
        tuResultFragment.setSaveToAlbumName(this.getSaveToAlbumName());
        tuResultFragment.setOutputCompress(this.getOutputCompress());
    }
    
    @Override
    protected <T extends TuFragment> T fragmentInstance() {
        final TuResultFragment fragmentInstance = (TuResultFragment)super.fragmentInstance();
        if (fragmentInstance instanceof TuResultFragment) {
            this.a(fragmentInstance);
        }
        return (T)fragmentInstance;
    }
}
