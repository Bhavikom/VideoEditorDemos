// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget;

import java.util.List;

public interface ParameterConfigViewInterface
{
    void setDelegate(final ParameterConfigViewDelegate p0);
    
    void seekTo(final float p0);
    
    void setParams(final List<String> p0, final int p1);
    
    public interface ParameterConfigViewDelegate
    {
        void onParameterConfigDataChanged(final ParameterConfigViewInterface p0, final int p1, final float p2);
        
        float readParameterValue(final ParameterConfigViewInterface p0, final int p1);
        
        void onParameterConfigRest(final ParameterConfigViewInterface p0, final int p1);
    }
}
