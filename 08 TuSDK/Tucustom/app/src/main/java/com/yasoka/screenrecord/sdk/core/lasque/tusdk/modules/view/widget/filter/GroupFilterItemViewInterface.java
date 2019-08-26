// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.filter;

//import org.lasque.tusdk.core.task.FilterTaskInterface;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.FilterTaskInterface;

public interface
GroupFilterItemViewInterface
{
    void setAction(final GroupFilterAction p0);
    
    boolean isSelected();
    
    boolean isCameraAction();
    
    boolean isActivating();
    
    void stopActivating();
    
    void waitInActivate(final long p0);
    
    void setFilterTask(final FilterTaskInterface p0);
    
    public enum GroupFilterAction
    {
        ActionNormal(0), 
        ActionEdit(1), 
        ActionCamera(2);
        
        private int a;
        
        private GroupFilterAction(final int a) {
            this.a = a;
        }
        
        public int getValue() {
            return this.a;
        }
    }
}
