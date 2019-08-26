// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.sticker;

//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.network.TuSdkDownloadTask;
import android.view.ViewGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadTask;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuOnlineFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
//import org.lasque.tusdk.core.type.DownloadTaskStatus;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
//import org.lasque.tusdk.impl.activity.TuOnlineFragment;

public abstract class TuStickerOnlineFragmentBase extends TuOnlineFragment
{
    private StickerLocalPackage.StickerPackageDelegate a;
    
    public TuStickerOnlineFragmentBase() {
        this.a = new StickerLocalPackage.StickerPackageDelegate() {
            @Override
            public void onStickerPackageStatusChanged(final StickerLocalPackage stickerLocalPackage, final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
                TuStickerOnlineFragmentBase.this.notifyOnlineData(tuSdkDownloadItem);
            }
        };
    }
    
    protected abstract void onHandleSelected(final StickerData p0);
    
    protected abstract void onHandleDetail(final long p0);
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        this.getWebview();
        this.setOnlineType(TuSdkDownloadTask.DownloadTaskType.TypeSticker.getAct());
        StatisticsManger.appendComponent(ComponentActType.editStickerOnlineFragment);
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
        super.viewDidLoad(viewGroup);
        StickerLocalPackage.shared().appenDelegate(this.a);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        StickerLocalPackage.shared().removeDelegate(this.a);
    }
    
    @Override
    protected String getPageFinishedData() {
        return StickerLocalPackage.shared().getAllDatas().toString();
    }
    
    @Override
    protected void onResourceDownload(final long n, final String s, final String s2) {
        StickerLocalPackage.shared().download(n, s, s2);
    }
    
    @Override
    protected void onResourceCancelDownload(final long n) {
        StickerLocalPackage.shared().cancelDownload(n);
    }
    
    @Override
    protected void handleSelected(final String[] array) {
        if (array.length < 4) {
            return;
        }
        final StickerData sticker = StickerLocalPackage.shared().getSticker(Long.parseLong(array[3]));
        if (sticker == null) {
            return;
        }
        this.onHandleSelected(sticker);
    }
    
    @Override
    protected void handleDetail(final String[] array) {
        if (array.length < 3) {
            return;
        }
        this.onHandleDetail(Long.parseLong(array[2]));
    }
}
