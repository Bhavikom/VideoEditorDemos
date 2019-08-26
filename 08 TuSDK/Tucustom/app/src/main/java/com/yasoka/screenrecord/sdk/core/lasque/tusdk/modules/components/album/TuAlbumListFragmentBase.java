// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.album;

//import org.lasque.tusdk.core.task.AlbumTaskManager;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.AlbumTaskManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuComponentFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;

import java.util.List;
//import org.lasque.tusdk.impl.activity.TuComponentFragment;

public abstract class TuAlbumListFragmentBase extends TuComponentFragment
{
    private boolean a;
    private String b;
    
    public boolean isDisableAutoSkipToPhotoList() {
        return this.a;
    }
    
    public void setDisableAutoSkipToPhotoList(final boolean a) {
        this.a = a;
    }
    
    public String getSkipAlbumName() {
        return this.b;
    }
    
    public void setSkipAlbumName(final String b) {
        this.b = b;
        if (b != null) {
            this.setDisableAutoSkipToPhotoList(false);
        }
    }
    
    public abstract List<AlbumSqlInfo> getGroups();
    
    public abstract void notifySelectedGroup(final AlbumSqlInfo p0);
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
        StatisticsManger.appendComponent(ComponentActType.albumListFragment);
    }
    
    @Override
    public void onDestroyView() {
        AlbumTaskManager.shared.resetQueues();
        super.onDestroyView();
    }
    
    protected void autoSelectedAblumGroupAction(final List<AlbumSqlInfo> list) {
        if (this.isDisableAutoSkipToPhotoList() || list == null) {
            return;
        }
        AlbumSqlInfo albumSqlInfo = null;
        for (final AlbumSqlInfo albumSqlInfo2 : list) {
            if (albumSqlInfo2.total < 1) {
                continue;
            }
            if (this.getSkipAlbumName() != null && albumSqlInfo2.title.equalsIgnoreCase(this.getSkipAlbumName())) {
                albumSqlInfo = albumSqlInfo2;
                break;
            }
            if (!"Camera".equalsIgnoreCase(albumSqlInfo2.title) || (albumSqlInfo != null && albumSqlInfo.total >= albumSqlInfo2.total)) {
                continue;
            }
            albumSqlInfo = albumSqlInfo2;
        }
        if (albumSqlInfo != null) {
            this.notifySelectedGroup(albumSqlInfo);
        }
    }
}
