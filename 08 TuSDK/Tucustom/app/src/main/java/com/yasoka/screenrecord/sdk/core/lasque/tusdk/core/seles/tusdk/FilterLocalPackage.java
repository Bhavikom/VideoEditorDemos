// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

import java.util.Iterator;
//import org.lasque.tusdk.core.type.DownloadTaskStatus;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.json.JSONObject;
import android.widget.ImageView;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
//import org.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.FilterAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;

import java.util.ArrayList;
//import org.lasque.tusdk.core.TuSdkConfigs;
import java.util.List;
//import org.lasque.tusdk.core.secret.FilterAdapter;
//import org.lasque.tusdk.core.network.TuSdkDownloadManger;

public class FilterLocalPackage implements TuSdkDownloadManger.TuSdkDownloadMangerDelegate
{
    public static final String NormalFilterCode = "Normal";
    private static FilterLocalPackage a;
    private FilterAdapter b;
    private List<FilterLocalPackageDelegate> c;
    
    public static FilterLocalPackage shared() {
        return FilterLocalPackage.a;
    }
    
    public static FilterLocalPackage init(final TuSdkConfigs tuSdkConfigs) {
        if (FilterLocalPackage.a == null && tuSdkConfigs != null) {
            FilterLocalPackage.a = new FilterLocalPackage(tuSdkConfigs);
        }
        return FilterLocalPackage.a;
    }
    
    public void appenDelegate(final FilterLocalPackageDelegate filterLocalPackageDelegate) {
        if (filterLocalPackageDelegate == null || this.c.contains(filterLocalPackageDelegate)) {
            return;
        }
        this.c.add(filterLocalPackageDelegate);
    }
    
    public void removeDelegate(final FilterLocalPackageDelegate filterLocalPackageDelegate) {
        if (filterLocalPackageDelegate == null) {
            return;
        }
        this.c.remove(filterLocalPackageDelegate);
    }
    
    private FilterLocalPackage(final TuSdkConfigs tuSdkConfigs) {
        this.c = new ArrayList<FilterLocalPackageDelegate>();
        (this.b = new FilterAdapter(tuSdkConfigs)).setDownloadDelegate(this);
    }
    
    public List<String> getCodes() {
        return this.b.getCodes();
    }
    
    public void setInitDelegate(final FilterAdapter.FiltersConfigDelegate initDelegate) {
        this.b.setInitDelegate(initDelegate);
    }
    
    public boolean isInited() {
        return this.b.isInited();
    }
    
    public List<String> verifyCodes(final List<String> list) {
        return this.b.verifyCodes(list);
    }
    
    public List<FilterOption> getFilters(final List<String> list) {
        return this.b.getFilters(list);
    }
    
    public List<FilterOption> getGroupFilters(final FilterGroup filterGroup) {
        return this.b.getGroupFilters(filterGroup);
    }
    
    public FilterGroup getFilterGroup(final long n) {
        return this.b.getFilterGroup(n);
    }
    
    public String getGroupNameKey(final long n) {
        return this.b.getGroupNameKey(n);
    }
    
    public int getGroupType(final long n) {
        return this.b.getGroupType(n);
    }
    
    public int getGroupFiltersType(final long n) {
        return this.b.getGroupFiltersType(n);
    }
    
    public List<FilterGroup> getGroups() {
        return this.b.getGroups();
    }
    
    public List<FilterGroup> getGroupsByAtionScen(final int n) {
        return this.b.getGroupsByAtionScen(n);
    }
    
    public String getGroupDefaultFilterCode(final FilterGroup filterGroup) {
        return this.b.getGroupDefaultFilterCode(filterGroup);
    }
    
    public FilterOption option(final String s) {
        return this.b.option(s);
    }
    
    public FilterWrap getFilterWrap(final String s) {
        final FilterOption option = this.option(s);
        final FilterWrap creat = FilterWrap.creat(option);
        StatisticsManger.appendFilter(option);
        return creat;
    }
    
    public List<SelesPicture> loadTextures(final String s) {
        return this.b.loadTextures(s);
    }
    
    public List<SelesPicture> loadInternalTextures(final List<String> list) {
        return this.b.loadInternalTextures(list);
    }
    
    public SelesOutInput createFilter(final FilterOption filterOption) {
        return this.b.createFilter(filterOption);
    }
    
    public void loadGroupThumb(final ImageView imageView, final FilterGroup filterGroup) {
        this.b.loadGroupThumb(imageView, filterGroup);
    }
    
    public void loadGroupDefaultFilterThumb(final ImageView imageView, final FilterGroup filterGroup) {
        this.b.loadGroupDefaultFilterThumb(imageView, filterGroup);
    }
    
    public void loadFilterThumb(final ImageView imageView, final FilterOption filterOption) {
        this.b.loadFilterThumb(imageView, filterOption);
    }
    
    public void cancelLoadImage(final ImageView imageView) {
        this.b.cancelLoadImage(imageView);
    }
    
    public void download(final long n, final String s, final String s2) {
        this.b.download(n, s, s2);
    }
    
    public void cancelDownload(final long n) {
        this.b.cancelDownload(n);
    }
    
    public void removeDownloadWithIdt(final long n) {
        this.b.removeDownloadWithIdt(n);
    }
    
    public JSONObject getAllDatas() {
        return this.b.getAllDatas();
    }
    
    @Override
    public void onDownloadMangerStatusChanged(final TuSdkDownloadManger tuSdkDownloadManger, final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
        final Iterator<FilterLocalPackageDelegate> iterator = new ArrayList<FilterLocalPackageDelegate>(this.c).iterator();
        while (iterator.hasNext()) {
            iterator.next().onFilterPackageStatusChanged(this, tuSdkDownloadItem, downloadTaskStatus);
        }
    }
    
    public interface FilterLocalPackageDelegate
    {
        void onFilterPackageStatusChanged(final FilterLocalPackage p0, final TuSdkDownloadItem p1, final DownloadTaskStatus p2);
    }
}
