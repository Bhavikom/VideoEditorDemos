// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.album;

//import org.lasque.tusdk.core.utils.ReflectUtils;
//import org.lasque.tusdk.impl.activity.TuFragment;
import java.util.ArrayList;
//import org.lasque.tusdk.core.task.AlbumTaskManager;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.AlbumTaskManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuComponentFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;

import java.util.List;
//import org.lasque.tusdk.impl.activity.TuComponentFragment;

public abstract class TuAlbumMultipleListFragmentBase extends TuComponentFragment
{
    private String a;
    
    public String getSkipAlbumName() {
        return this.a;
    }
    
    public void setSkipAlbumName(final String a) {
        this.a = a;
    }
    
    public abstract List<AlbumSqlInfo> getGroups();
    
    public abstract void notifySelectedGroup(final AlbumSqlInfo p0);
    
    public abstract Class<?> getPreviewFragmentClazz();
    
    public abstract int getPreviewFragmentLayoutId();
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
        StatisticsManger.appendComponent(ComponentActType.albumMultipleListFragment);
    }
    
    @Override
    public void onDestroyView() {
        AlbumTaskManager.shared.resetQueues();
        super.onDestroyView();
    }
    
    public void autoSelectedAblumGroupAction(final ArrayList<AlbumSqlInfo> list) {
        if (list == null) {
            return;
        }
        AlbumSqlInfo albumSqlInfo = null;
        for (final AlbumSqlInfo albumSqlInfo2 : list) {
            if (albumSqlInfo2.total < 1) {
                continue;
            }
            if (this.a != null && albumSqlInfo2.title.equalsIgnoreCase(this.a)) {
                albumSqlInfo = albumSqlInfo2;
                break;
            }
        }
        if (albumSqlInfo == null && list.size() > 0) {
            albumSqlInfo = list.get(0);
        }
        this.notifySelectedGroup(albumSqlInfo);
    }
    
    protected <T extends TuFragment> T getPreviewFragmentInstance() {
        final TuFragment tuFragment = ReflectUtils.classInstance(this.getPreviewFragmentClazz());
        if (tuFragment == null) {
            return null;
        }
        tuFragment.setRootViewLayoutId(this.getPreviewFragmentLayoutId());
        return (T)tuFragment;
    }
}
