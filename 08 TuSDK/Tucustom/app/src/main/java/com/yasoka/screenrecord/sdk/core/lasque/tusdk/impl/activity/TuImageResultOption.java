// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity;

public abstract class TuImageResultOption extends TuResultOption
{
    private boolean a;
    private boolean b;
    
    public boolean isShowResultPreview() {
        return this.a;
    }
    
    public void setShowResultPreview(final boolean a) {
        this.a = a;
    }
    
    public boolean isAutoRemoveTemp() {
        return this.b;
    }
    
    public void setAutoRemoveTemp(final boolean b) {
        this.b = b;
    }
    
    private void a(final TuImageResultFragment tuImageResultFragment) {
        if (tuImageResultFragment == null) {
            return;
        }
        tuImageResultFragment.setShowResultPreview(this.isShowResultPreview());
        tuImageResultFragment.setAutoRemoveTemp(this.isAutoRemoveTemp());
    }
    
    @Override
    protected <T extends TuFragment> T fragmentInstance() {
        final TuImageResultFragment fragmentInstance = (TuImageResultFragment)super.fragmentInstance();
        if (fragmentInstance instanceof TuImageResultFragment) {
            this.a(fragmentInstance);
        }
        return (T)fragmentInstance;
    }
}
