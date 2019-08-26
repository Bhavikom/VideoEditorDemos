// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.sticker;

//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.type.DownloadTaskStatus;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuComponentFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;

import java.util.List;
//import org.lasque.tusdk.impl.activity.TuComponentFragment;

public abstract class TuStickerChooseFragmentBase extends TuComponentFragment
{
    private List<StickerCategory> a;
    private StickerLocalPackage.StickerPackageDelegate b;
    
    public TuStickerChooseFragmentBase() {
        this.b = new StickerLocalPackage.StickerPackageDelegate() {
            @Override
            public void onStickerPackageStatusChanged(final StickerLocalPackage stickerLocalPackage, final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
                if (tuSdkDownloadItem == null || downloadTaskStatus == null) {
                    return;
                }
                switch (downloadTaskStatus.ordinal()) {
                    case 1:
                    case 2: {
                        TuStickerChooseFragmentBase.this.reloadStickers();
                        break;
                    }
                }
            }
        };
    }
    
    public List<StickerCategory> getCategories() {
        if (this.a == null) {
            this.a = StickerLocalPackage.shared().getCategories();
        }
        return this.a;
    }
    
    public void setCategories(final List<StickerCategory> a) {
        this.a = a;
    }
    
    protected StickerCategory getCategory(final int n) {
        if (this.getCategories() == null || n < 0 || n >= this.a.size()) {
            return null;
        }
        return this.a.get(n);
    }
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
        StickerLocalPackage.shared().appenDelegate(this.b);
        StatisticsManger.appendComponent(ComponentActType.editStickerLocalFragment);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        StickerLocalPackage.shared().removeDelegate(this.b);
    }
    
    protected void removeStickerGroup(final StickerGroup stickerGroup) {
        if (stickerGroup == null) {
            return;
        }
        StickerLocalPackage.shared().removeDownloadWithIdt(stickerGroup.groupId);
    }
    
    protected void reloadStickers() {
        this.setCategories(StickerLocalPackage.shared().getCategories(this.getCategories()));
    }
}
