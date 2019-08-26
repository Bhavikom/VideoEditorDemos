// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.filter;

//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.network.TuSdkDownloadTask;
import android.view.ViewGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadTask;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuOnlineFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface;
//import org.lasque.tusdk.core.type.DownloadTaskStatus;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface;
//import org.lasque.tusdk.impl.activity.TuOnlineFragment;

public abstract class TuFilterOnlineFragmentBase extends TuOnlineFragment
{
    private GroupFilterItemViewInterface.GroupFilterAction a;
    private FilterLocalPackage.FilterLocalPackageDelegate b;
    
    public TuFilterOnlineFragmentBase() {
        this.a = GroupFilterItemViewInterface.GroupFilterAction.ActionNormal;
        this.b = new FilterLocalPackage.FilterLocalPackageDelegate() {
            @Override
            public void onFilterPackageStatusChanged(final FilterLocalPackage filterLocalPackage, final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
                notifyOnlineData(tuSdkDownloadItem);
            }
        };
    }
    
    protected abstract void onHandleSelected(final long p0);
    
    protected abstract void onHandleDetail(final long p0);
    
    public GroupFilterItemViewInterface.GroupFilterAction getAction() {
        if (this.a == null) {
            this.a = GroupFilterItemViewInterface.GroupFilterAction.ActionNormal;
        }
        return this.a;
    }
    
    public void setAction(final GroupFilterItemViewInterface.GroupFilterAction a) {
        this.a = a;
    }
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        this.getWebview();
        this.setOnlineType(TuSdkDownloadTask.DownloadTaskType.TypeFilter.getAct());
        this.setArgs("action=" + this.a.getValue());
        StatisticsManger.appendComponent(ComponentActType.editFilterOnlineFragment);
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
        super.viewDidLoad(viewGroup);
        FilterLocalPackage.shared().appenDelegate(this.b);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FilterLocalPackage.shared().removeDelegate(this.b);
    }
    
    @Override
    protected String getPageFinishedData() {
        return FilterLocalPackage.shared().getAllDatas().toString();
    }
    
    @Override
    protected void onResourceDownload(final long n, final String s, final String s2) {
        FilterLocalPackage.shared().download(n, s, s2);
    }
    
    @Override
    protected void onResourceCancelDownload(final long n) {
        FilterLocalPackage.shared().cancelDownload(n);
    }
    
    @Override
    protected void handleSelected(final String[] array) {
        if (array.length < 3) {
            return;
        }
        this.onHandleSelected(Long.parseLong(array[2]));
    }
    
    @Override
    protected void handleDetail(final String[] array) {
        if (array.length < 3) {
            return;
        }
        this.onHandleDetail(Long.parseLong(array[2]));
    }
}
