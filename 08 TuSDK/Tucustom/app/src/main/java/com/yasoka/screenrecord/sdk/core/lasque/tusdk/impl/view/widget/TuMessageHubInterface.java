// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget;

import android.content.Context;

public interface TuMessageHubInterface
{
    void setStatus(final Context p0, final String p1);
    
    void setStatus(final Context p0, final int p1);
    
    void showToast(final Context p0, final String p1);
    
    void showToast(final Context p0, final int p1);
    
    void showSuccess(final Context p0, final String p1);
    
    void showSuccess(final Context p0, final int p1);
    
    void showError(final Context p0, final String p1);
    
    void showError(final Context p0, final int p1);
    
    void dismiss();
    
    void dismissRightNow();
    
    void applyToViewWithNavigationBarHidden(final boolean p0);
}
