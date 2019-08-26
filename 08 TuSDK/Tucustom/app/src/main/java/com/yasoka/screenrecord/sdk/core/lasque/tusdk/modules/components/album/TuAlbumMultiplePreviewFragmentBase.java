// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.album;

//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.impl.activity.TuFragment;

public class TuAlbumMultiplePreviewFragmentBase extends TuFragment
{
    @Override
    protected void loadView(final ViewGroup viewGroup) {
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
        StatisticsManger.appendComponent(ComponentActType.photoListPreviewFragment);
    }
}
