// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

//import org.lasque.tusdk.core.view.TuSdkViewInterface;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewInterface;

public interface TuSdkCellViewInterface<T> extends TuSdkViewInterface
{
    void setModel(final T p0);
    
    T getModel();
}
