// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.album;

//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuComponentFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
//import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
//import org.lasque.tusdk.impl.activity.TuComponentFragment;

public abstract class TuPhotoListFragmentBase extends TuComponentFragment
{
    private AlbumSqlInfo a;
    
    public AlbumSqlInfo getAlbumInfo() {
        return this.a;
    }
    
    public void setAlbumInfo(final AlbumSqlInfo a) {
        this.a = a;
    }
    
    public abstract void notifySelectedPhoto(final ImageSqlInfo p0);
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
        StatisticsManger.appendComponent(ComponentActType.photoListFragment);
    }
}
