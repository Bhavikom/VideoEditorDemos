// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

import android.view.View;
import java.util.List;

public class TuSdkIndexPath
{
    public int row;
    public int section;
    public int viewType;
    public boolean isHeader;
    
    public TuSdkIndexPath() {
    }
    
    public TuSdkIndexPath(final int n, final int n2) {
        this(n, n2, 0);
    }
    
    public TuSdkIndexPath(final int section, final int row, final int viewType) {
        this.section = section;
        this.viewType = viewType;
        this.row = row;
        if (this.row == -1) {
            this.isHeader = true;
        }
    }
    
    public interface TuSdkDataSource
    {
        List<TuSdkIndexPath> getIndexPaths();
        
        TuSdkIndexPath getIndexPath(final int p0);
        
        int viewTypes();
        
        int sectionCount();
        
        int rowCount(final int p0);
        
        int count();
        
        void onViewBinded(final TuSdkIndexPath p0, final View p1);
        
        Object getItem(final TuSdkIndexPath p0);
    }
}
