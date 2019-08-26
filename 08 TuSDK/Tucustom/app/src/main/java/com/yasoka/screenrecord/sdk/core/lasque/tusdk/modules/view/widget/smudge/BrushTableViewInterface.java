// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge;

//import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//import androidx.recyclerview.widget.RecyclerView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.recyclerview.TuSdkTableView;

import java.util.List;
//import org.lasque.tusdk.core.view.recyclerview.TuSdkTableView;

public interface BrushTableViewInterface
{
    void setCellLayoutId(final int p0);
    
    void setCellWidth(final int p0);
    
    void setItemClickDelegate(final TuSdkTableView.TuSdkTableViewItemClickDelegate<BrushData, BrushBarItemCellBase> p0);
    
    void setAction(final BrushAction p0);
    
    void reloadData();
    
    void setModeList(final List<BrushData> p0);
    
    List<BrushData> getModeList();
    
    void setSelectedPosition(final int p0);
    
    void setSelectedPosition(final int p0, final boolean p1);
    
    int getSelectedPosition();
    
    void changeSelectedPosition(final int p0);
    
    void scrollToPosition(final int p0);
    
    void scrollToPositionWithOffset(final int p0, final int p1);
    
    void smoothScrollByCenter(final View p0);
    
    RecyclerView.Adapter getAdapter();
    
    public enum BrushAction
    {
        ActionNormal, 
        ActionEdit;
    }
}
