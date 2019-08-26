// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.filter;

//import org.lasque.tusdk.core.task.FilterTaskInterface;
//import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//import androidx.recyclerview.widget.RecyclerView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.FilterTaskInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.recyclerview.TuSdkTableView;

import java.util.List;
//import org.lasque.tusdk.core.view.recyclerview.TuSdkTableView;

public interface GroupFilterTableViewInterface
{
    void setCellLayoutId(final int p0);
    
    void setGroupFilterCellWidth(final int p0);
    
    void setGroupDelegate(final GroupFilterGroupViewBase.GroupFilterGroupViewDelegate p0);
    
    void setItemClickDelegate(final TuSdkTableView.TuSdkTableViewItemClickDelegate<GroupFilterItem, GroupFilterItemViewBase> p0);
    
    void setAction(final GroupFilterItemViewInterface.GroupFilterAction p0);
    
    void setDisplaySelectionIcon(final boolean p0);
    
    void reloadData();
    
    void setModeList(final List<GroupFilterItem> p0);
    
    List<GroupFilterItem> getModeList();
    
    void setSelectedPosition(final int p0);
    
    void setSelectedPosition(final int p0, final boolean p1);
    
    int getSelectedPosition();
    
    void changeSelectedPosition(final int p0);
    
    void scrollToPosition(final int p0);
    
    void scrollToPositionWithOffset(final int p0, final int p1);
    
    void smoothScrollByCenter(final View p0);
    
    RecyclerView.Adapter getAdapter();
    
    void setFilterTask(final FilterTaskInterface p0);
}
