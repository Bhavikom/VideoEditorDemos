// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components;

//import org.lasque.tusdk.core.secret.StatisticsManger;
import android.support.v7.app.AppCompatActivity;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;

public abstract class TuAlbumComponentBase extends TuSdkComponent
{
    public TuAlbumComponentBase(final AppCompatActivity activity) {
        super(activity);
        if (this.getClass().getSimpleName().contains("vatar")) {
            StatisticsManger.appendComponent(ComponentActType.avatarComponent);
        }
        else {
            StatisticsManger.appendComponent(ComponentActType.albumComponent);
        }
    }
}
