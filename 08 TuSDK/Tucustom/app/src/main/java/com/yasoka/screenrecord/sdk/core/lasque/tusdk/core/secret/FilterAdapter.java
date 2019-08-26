// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret;

//import org.lasque.tusdk.core.task.ImageViewTaskWare;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import android.text.TextUtils;
import android.widget.ImageView;
import java.util.Arrays;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKProgramFilter;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.seles.tusdk.particle.TuSDKParticleFilter;
//import org.lasque.tusdk.core.utils.json.JsonHelper;
//import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorNoirFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveScanningLineFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveFaultFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveRadialBlurFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveOldTVFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveSloshFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveMirrorImageFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveHeartbeatFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveXRayFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveLightningFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveSignalFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveFancy01Filter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveMegrimFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveSoulOutFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveEdgeMagicFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveShakeFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinNaturalFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinMoistFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKLiveSkinColor2Filter;
//import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKLiveSkinColorFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorSelectiveFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinColorFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinColor2Filter;
//import org.lasque.tusdk.core.seles.tusdk.filters.arts.TuSDKArtBrushFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.flowabs.TuSDKTfmInkFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.flowabs.TuSDKTfmFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lights.TuSDKSobelEdgeFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.lights.TuSDKLightGlareFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorHDRFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorMixCoverFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorLomoFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorMixedFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.TuSDKNormalFilter;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.Map;
//import org.lasque.tusdk.core.utils.json.JsonWrapper;
import android.os.Handler;
import android.os.Looper;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkBundle;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadTask;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.TuSDKNormalFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.arts.TuSDKArtBrushFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKProgramFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorHDRFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorLomoFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorMixCoverFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorMixedFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorNoirFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorSelectiveFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.flowabs.TuSDKTfmFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.flowabs.TuSDKTfmInkFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lights.TuSDKLightGlareFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lights.TuSDKSobelEdgeFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveEdgeMagicFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveFancy01Filter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveFaultFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveHeartbeatFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveLightningFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveMegrimFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveMirrorImageFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveOldTVFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveRadialBlurFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveScanningLineFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveShakeFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveSignalFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveSloshFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveSoulOutFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveXRayFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKLiveSkinColor2Filter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKLiveSkinColorFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinColor2Filter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinColorFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinMoistFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinNaturalFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.particle.TuSDKParticleFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonWrapper;

import java.util.Collection;
//import org.lasque.tusdk.core.TuSdkBundle;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.List;
//import org.lasque.tusdk.core.network.TuSdkDownloadTask;
//import org.lasque.tusdk.core.TuSdkConfigs;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import java.util.Hashtable;
//import org.lasque.tusdk.core.seles.tusdk.FilterGroup;
import java.util.ArrayList;
//import org.lasque.tusdk.core.network.TuSdkDownloadAdapter;

public class FilterAdapter extends TuSdkDownloadAdapter<FilterThumbTaskImageWare>
{
    public static final String NormalFilterCode = "Normal";
    private ArrayList<FilterGroup> a;
    private Hashtable<String, FilterOption> b;
    private ArrayList<String> c;
    private FiltersConfigDelegate d;
    private boolean e;
    private TuSdkConfigs f;
    
    public FilterAdapter(final TuSdkConfigs f) {
        this.f = f;
        this.setDownloadTaskType(TuSdkDownloadTask.DownloadTaskType.TypeFilter);
        this.b();
    }
    
    public List<String> getCodes() {
        return this.verifyCodes(this.c);
    }
    
    public void setInitDelegate(final FiltersConfigDelegate d) {
        this.d = d;
        this.a();
    }
    
    private void a() {
        if (this.e && this.d != null) {
            this.d.onFiltersConfigInited();
            this.d = null;
            TLog.d("FiltersConfig inited: %s", this.c.size());
        }
    }
    
    public boolean isInited() {
        return this.e;
    }
    
    public List<String> verifyCodes(final List<String> list) {
        if (!this.e || list == null || this.c == null || !SdkValid.shared.sdkValid()) {
            return null;
        }
        final ArrayList<String> list2 = new ArrayList<String>(list.size());
        for (final String s : list) {
            if (this.c.contains(s)) {
                list2.add(s);
            }
        }
        return list2;
    }
    
    public List<FilterOption> getFilters(final List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        final ArrayList<FilterOption> list2 = new ArrayList<FilterOption>(list.size());
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            final FilterOption filterOption = this.b.get(iterator.next());
            if (filterOption != null) {
                list2.add(filterOption.copy());
            }
        }
        return list2;
    }
    
    public List<FilterOption> getGroupFilters(final FilterGroup filterGroup) {
        if (filterGroup == null) {
            return null;
        }
        final FilterGroup filterGroup2 = this.getFilterGroup(filterGroup.groupId);
        if (filterGroup2 == null || filterGroup2.filters == null || filterGroup2.filters.isEmpty()) {
            return null;
        }
        final ArrayList list = new ArrayList<FilterOption>(filterGroup2.filters.size());
        final Iterator<FilterOption> iterator = filterGroup2.filters.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().copy());
        }
        return (List<FilterOption>)list;
    }
    
    public String getGroupNameKey(final long n) {
        final FilterGroup filterGroup = this.getFilterGroup(n);
        if (filterGroup == null) {
            return null;
        }
        return filterGroup.getNameKey();
    }
    
    public int getGroupType(final long n) {
        final FilterGroup filterGroup = this.getFilterGroup(n);
        if (filterGroup == null) {
            return 0;
        }
        return filterGroup.getCategoryId();
    }
    
    public int getGroupFiltersType(final long n) {
        final FilterGroup filterGroup = this.getFilterGroup(n);
        if (filterGroup == null) {
            return 0;
        }
        return filterGroup.getGroupFiltersType();
    }
    
    public List<FilterGroup> getGroups() {
        final ArrayList<FilterGroup> list = new ArrayList<FilterGroup>(this.a.size());
        for (final FilterGroup filterGroup : this.a) {
            if (filterGroup.categoryId <= 0) {
                list.add(filterGroup.copy());
            }
        }
        return list;
    }
    
    public List<FilterGroup> getGroupsByAtionScen(final int n) {
        final ArrayList<FilterGroup> list = new ArrayList<FilterGroup>(this.a.size());
        for (final FilterGroup filterGroup : this.getGroups()) {
            if (filterGroup.canUseForAtionScenType(n)) {
                list.add(filterGroup.copy());
            }
        }
        return list;
    }
    
    public String getGroupDefaultFilterCode(final FilterGroup filterGroup) {
        if (filterGroup == null) {
            return null;
        }
        final FilterGroup filterGroup2 = this.getFilterGroup(filterGroup.groupId);
        if (filterGroup2 == null) {
            return null;
        }
        final FilterOption defaultFilter = filterGroup2.getDefaultFilter();
        if (defaultFilter == null) {
            return null;
        }
        return defaultFilter.code;
    }
    
    private void b() {
        this.a = new ArrayList<FilterGroup>();
        this.b = new Hashtable<String, FilterOption>();
        this.c = new ArrayList<String>();
        this.a("Normal", 0, new String[0]);
        this.tryLoadTaskDataWithCache();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FilterAdapter.this.c();
            }
        }).start();
    }
    
    private void c() {
        SdkValid.shared.loadFilterConfig(TuSdkBundle.sdkBundleTexture("lsq_internal_filters.filter"));
        if (this.f != null && this.f.filterGroups != null) {
            final Iterator<FilterGroup> iterator = new ArrayList<FilterGroup>(this.f.filterGroups).iterator();
            while (iterator.hasNext()) {
                this.a(iterator.next());
            }
        }
        this.asyncLoadDownloadDatas();
        new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
            @Override
            public void run() {
                FilterAdapter.this.e = true;
                FilterAdapter.this.a();
            }
        });
    }
    
    private void a(final FilterGroup filterGroup) {
        if (filterGroup == null || filterGroup.file == null) {
            return;
        }
        final String loadFilterGroup = SdkValid.shared.loadFilterGroup(TuSdkBundle.sdkBundleTexture(filterGroup.file), null);
        if (loadFilterGroup == null) {
            return;
        }
        final FilterGroup e = JsonWrapper.deserialize(loadFilterGroup, FilterGroup.class);
        if (e == null || this.containsGroupId(e.groupId)) {
            return;
        }
        if (e.filters == null || e.filters.isEmpty()) {
            return;
        }
        if (filterGroup.filters == null || filterGroup.filters.isEmpty()) {
            this.b(e);
        }
        else {
            this.a(filterGroup, e);
        }
        if (filterGroup.name != null) {
            e.name = filterGroup.name;
        }
        if (filterGroup.thumb != null) {
            e.thumb = filterGroup.thumb;
        }
        if (filterGroup.color != null) {
            e.color = filterGroup.color;
        }
        e.groupFiltersType = filterGroup.groupFiltersType;
        if (filterGroup.defaultFilterId > 0L && e.getFilterOption(filterGroup.defaultFilterId) != null) {
            e.defaultFilterId = filterGroup.defaultFilterId;
        }
        if (e.filters != null && !e.filters.isEmpty()) {
            this.a.add(e);
        }
    }
    
    private boolean b(final FilterGroup filterGroup) {
        final ArrayList<FilterOption> filters = new ArrayList<FilterOption>(filterGroup.filters.size());
        for (final FilterOption e : filterGroup.filters) {
            e.disableRuntime = filterGroup.disableRuntime;
            if (this.a(e)) {
                filters.add(e);
            }
        }
        filterGroup.filters = filters;
        return !filters.isEmpty();
    }
    
    private boolean a(final FilterOption value) {
        if (value.version > 11) {
            return false;
        }
        if (!this.c.contains(value.code)) {
            this.c.add(value.code);
            this.b.put(value.code, value);
        }
        return true;
    }
    
    private void a(final FilterGroup filterGroup, final FilterGroup filterGroup2) {
        final ArrayList<FilterOption> filters = new ArrayList<FilterOption>(filterGroup.filters.size());
        for (final FilterOption filterOption : filterGroup.filters) {
            final FilterOption filterOption2 = filterGroup2.getFilterOption(filterOption.id);
            if (filterOption2 != null) {
                if (filterOption2.version > 11) {
                    continue;
                }
                if (filterOption.name != null) {
                    filterOption2.name = filterOption.name;
                }
                if (filterOption.thumb != null) {
                    filterOption2.thumb = filterOption.thumb;
                }
                if (filterOption.color != null) {
                    filterOption2.color = filterOption.color;
                }
                filterGroup2.groupFiltersType = filterGroup.groupFiltersType;
                filterOption2.disableRuntime = filterGroup2.disableRuntime;
                if (filterOption.args != null) {
                    this.a(filterOption, filterOption2);
                }
                if (!this.a(filterOption2)) {
                    continue;
                }
                filters.add(filterOption2);
            }
        }
        filterGroup2.filters = filters;
    }
    
    private void a(final FilterOption filterOption, final FilterOption filterOption2) {
        if (filterOption2.args == null) {
            filterOption2.args = filterOption.args;
            return;
        }
        for (final Map.Entry<String, String> entry : filterOption.args.entrySet()) {
            filterOption2.args.put(entry.getKey(), entry.getValue());
        }
    }
    
    private FilterOption d() {
        return this.b.get("Normal");
    }
    
    public FilterOption option(final String s) {
        final FilterOption a = this.a(s);
        if (a != null) {
            return a.copy();
        }
        return this.d().copy();
    }
    
    private FilterOption a(final String key) {
        if (key == null) {
            return null;
        }
        return this.b.get(key);
    }
    
    public List<SelesPicture> loadTextures(final String s) {
        final FilterOption a = this.a(s);
        if (a == null || a.textures == null) {
            return null;
        }
        List<SelesPicture> list;
        if (a.encryptType == 0) {
            list = this.b(a);
        }
        else {
            list = SdkValid.shared.readTextures(a.groupId, a.textures);
        }
        return list;
    }
    
    private List<SelesPicture> b(final FilterOption filterOption) {
        if (filterOption == null || filterOption.textures == null) {
            return null;
        }
        if (this.getFilterGroup(filterOption.groupId) == null) {
            return null;
        }
        final ArrayList<SelesPicture> list = new ArrayList<SelesPicture>(filterOption.textures.size());
        final Iterator<String> iterator = filterOption.textures.iterator();
        while (iterator.hasNext()) {
            final Bitmap assetsBitmap = TuSdkContext.getAssetsBitmap(TuSdkBundle.sdkBundleTexture(iterator.next()));
            if (assetsBitmap != null) {
                list.add(new SelesPicture(assetsBitmap, false, true));
            }
        }
        return list;
    }
    
    public List<SelesPicture> loadInternalTextures(final List<String> list) {
        return SdkValid.shared.readInternalTextures(list);
    }
    
    public FilterGroup getFilterGroup(final long n) {
        for (final FilterGroup filterGroup : this.a) {
            if (filterGroup.groupId == n) {
                return filterGroup;
            }
        }
        return null;
    }
    
    public SelesOutInput createFilter(FilterOption a) {
        if (a == null) {
            return null;
        }
        a = this.a(a.code);
        if (a == null) {
            return null;
        }
        switch (a.filterType) {
            case 0: {
                return new TuSDKNormalFilter();
            }
            case 16: {
                return new TuSDKColorMixedFilter(a);
            }
            case 32: {
                return new TuSDKColorLomoFilter(a);
            }
            case 48: {
                return new TuSDKColorMixCoverFilter(a);
            }
            case 49: {
                return new TuSDKColorHDRFilter();
            }
            case 64: {
                return new TuSDKLightGlareFilter(a);
            }
            case 65: {
                return new TuSDKSobelEdgeFilter(a);
            }
            case 66: {
                return new TuSDKTfmFilter();
            }
            case 67: {
                return new TuSDKTfmInkFilter();
            }
            case 80: {
                return new TuSDKArtBrushFilter(a);
            }
            case 96: {
                return new TuSDKSkinColor2Filter(a);
            }
            case 97: {
                return new TuSDKSkinColorFilter(a);
            }
            case 98: {
                return new TuSDKColorSelectiveFilter(a);
            }
            case 99: {
                return new TuSDKLiveSkinColorFilter(a);
            }
            case 100: {
                return new TuSDKLiveSkinColor2Filter(a);
            }
            case 101: {
                return new TuSDKSkinMoistFilter();
            }
            case 102: {
                return new TuSDKSkinNaturalFilter();
            }
            case 112: {
                return this.c(a);
            }
            case 113: {
                return new TuSDKLiveShakeFilter(a);
            }
            case 114: {
                return new TuSDKLiveEdgeMagicFilter(a);
            }
            case 115: {
                return new TuSDKLiveSoulOutFilter(a);
            }
            case 116: {
                return new TuSDKLiveMegrimFilter(a);
            }
            case 117: {
                return new TuSDKLiveFancy01Filter(a);
            }
            case 118: {
                return new TuSDKLiveSignalFilter(a);
            }
            case 119: {
                return new TuSDKLiveLightningFilter(a);
            }
            case 120: {
                return new TuSDKLiveXRayFilter(a);
            }
            case 121: {
                return new TuSDKLiveHeartbeatFilter(a);
            }
            case 128: {
                return new TuSDKLiveMirrorImageFilter();
            }
            case 129: {
                return new TuSDKLiveSloshFilter(a);
            }
            case 130: {
                return new TuSDKLiveOldTVFilter(a);
            }
            case 131: {
                return new TuSDKLiveRadialBlurFilter(a);
            }
            case 132: {
                return new TuSDKLiveFaultFilter();
            }
            case 133: {
                return new TuSDKLiveScanningLineFilter();
            }
            case 240: {
                return new TuSDKColorNoirFilter();
            }
            case 241: {
                if (a.argList != null && !a.argList.isEmpty()) {
                    return new TuSDKParticleFilter(JsonHelper.json(a.argList));
                }
                return null;
            }
            default: {
                return null;
            }
        }
    }
    
    private SelesOutInput c(final FilterOption filterOption) {
        if (StringHelper.isNotBlank(filterOption.vertex) && StringHelper.isNotBlank(filterOption.fragment)) {
            return new TuSDKProgramFilter(filterOption.vertex, filterOption.fragment);
        }
        if (StringHelper.isNotBlank(filterOption.fragment)) {
            return new TuSDKProgramFilter(filterOption.fragment);
        }
        return null;
    }
    
    private void a(final String s, final int n, final String... array) {
        this.a(s, n, false, array);
    }
    
    private void a(final String s, final int n, final boolean b, final String... array) {
        this.a(s, n, b, false, array);
    }
    
    private void a(final String key, final int filterType, final boolean texturesKeepInput, final boolean canDefinition, final String... a) {
        final FilterOption value = new FilterOption();
        value.id = 0L;
        value.code = key;
        value.name = String.format("lsq_filter_%s", key);
        value.filterType = filterType;
        if (a != null) {
            value.textures = new ArrayList<String>(Arrays.asList(a));
        }
        value.texturesKeepInput = texturesKeepInput;
        value.canDefinition = canDefinition;
        value.encryptType = 1;
        value.isInternal = true;
        this.c.add(key);
        this.b.put(key, value);
    }
    
    public void loadGroupThumb(final ImageView imageView, final FilterGroup filterGroup) {
        if (imageView == null || filterGroup == null) {
            return;
        }
        final FilterGroup filterGroup2 = this.getFilterGroup(filterGroup.groupId);
        if (filterGroup2 == null || this.a(imageView, filterGroup2.thumb)) {
            return;
        }
        this.loadImage(new FilterThumbTaskImageWare(imageView, FilterThumbTaskImageWare.FilterThumbTaskType.TypeGroupThumb, null, filterGroup2));
    }
    
    public void loadGroupDefaultFilterThumb(final ImageView imageView, final FilterGroup filterGroup) {
        if (imageView == null || filterGroup == null) {
            return;
        }
        final FilterGroup filterGroup2 = this.getFilterGroup(filterGroup.groupId);
        if (filterGroup2 == null) {
            return;
        }
        this.loadFilterThumb(imageView, filterGroup2.getDefaultFilter());
    }
    
    public void loadFilterThumb(final ImageView imageView, final FilterOption filterOption) {
        if (imageView == null || filterOption == null) {
            return;
        }
        final FilterOption a = this.a(filterOption.code);
        if (a == null || this.a(imageView, a.thumb)) {
            return;
        }
        this.loadImage(new FilterThumbTaskImageWare(imageView, FilterThumbTaskImageWare.FilterThumbTaskType.TypeFilterThumb, a, null));
    }
    
    private boolean a(final ImageView imageView, final String s) {
        if (s == null) {
            return false;
        }
        final int drawableResId = TuSdkContext.getDrawableResId(s);
        if (drawableResId != 0) {
            imageView.setImageResource(drawableResId);
            return true;
        }
        return false;
    }
    
    @Override
    protected String getCacheKey(final FilterThumbTaskImageWare filterThumbTaskImageWare) {
        String s = null;
        switch (filterThumbTaskImageWare.taskType.ordinal()) {
            case 1: {
                if (filterThumbTaskImageWare.group != null) {
                    s = (TextUtils.isEmpty((CharSequence)filterThumbTaskImageWare.group.thumbKey) ? filterThumbTaskImageWare.group.code : filterThumbTaskImageWare.group.thumbKey);
                    break;
                }
                break;
            }
            case 2: {
                if (filterThumbTaskImageWare.option != null) {
                    s = (TextUtils.isEmpty((CharSequence)filterThumbTaskImageWare.option.thumbKey) ? filterThumbTaskImageWare.option.code : filterThumbTaskImageWare.option.thumbKey);
                    break;
                }
                break;
            }
        }
        return s;
    }
    
    @Override
    protected Bitmap asyncTaskLoadImage(final FilterThumbTaskImageWare filterThumbTaskImageWare) {
        Bitmap bitmap = null;
        if (filterThumbTaskImageWare.taskType == FilterThumbTaskImageWare.FilterThumbTaskType.TypeGroupThumb && filterThumbTaskImageWare.group != null) {
            bitmap = this.b(filterThumbTaskImageWare.group.thumb);
            if (bitmap != null) {
                return bitmap;
            }
            final FilterGroup filterGroup = this.getFilterGroup(filterThumbTaskImageWare.group.groupId);
            if (filterGroup != null) {
                bitmap = SdkValid.shared.readFilterThumb(filterGroup.groupId, 0L);
            }
        }
        else if (filterThumbTaskImageWare.taskType == FilterThumbTaskImageWare.FilterThumbTaskType.TypeFilterThumb && filterThumbTaskImageWare.option != null) {
            bitmap = this.b(filterThumbTaskImageWare.option.thumb);
            if (bitmap != null) {
                return bitmap;
            }
            final FilterGroup filterGroup2 = this.getFilterGroup(filterThumbTaskImageWare.option.groupId);
            if (filterGroup2 != null) {
                bitmap = SdkValid.shared.readFilterThumb(filterGroup2.groupId, filterThumbTaskImageWare.option.id);
            }
        }
        return bitmap;
    }
    
    private Bitmap b(final String s) {
        if (StringHelper.isEmpty(s)) {
            return null;
        }
        return TuSdkContext.getAssetsBitmap(TuSdkBundle.sdkBundleTexture(s));
    }
    
    @Override
    public boolean containsGroupId(final long n) {
        return this.getFilterGroup(n) != null;
    }
    
    @Override
    protected void removeDownloadData(final long l) {
        final FilterGroup filterGroup = this.getFilterGroup(l);
        if (filterGroup == null) {
            return;
        }
        if (filterGroup.filters != null) {
            for (final FilterOption filterOption : filterGroup.filters) {
                this.b.remove(filterOption.code);
                this.c.remove(filterOption.code);
            }
        }
        this.a.remove(filterGroup);
        SdkValid.shared.removeFilterGroup(filterGroup.groupId);
        TLog.d("remove download filter [%s]: %s | %s", l, this.c.size(), this.a.size());
    }
    
    @Override
    protected boolean appendDownload(final TuSdkDownloadItem tuSdkDownloadItem) {
        if (!super.appendDownload(tuSdkDownloadItem)) {
            return false;
        }
        final String loadFilterGroup = SdkValid.shared.loadFilterGroup(tuSdkDownloadItem.localDownloadPath().getAbsolutePath(), tuSdkDownloadItem.key);
        if (loadFilterGroup == null) {
            return false;
        }
        final FilterGroup e = JsonWrapper.deserialize(loadFilterGroup, FilterGroup.class);
        if (e == null) {
            return false;
        }
        if (e.filters == null || e.filters.isEmpty()) {
            return false;
        }
        e.isDownload = true;
        if (this.b(e)) {
            this.a.add(e);
        }
        return true;
    }
    
    @Override
    protected Collection<?> getAllGroupID() {
        final ArrayList<Long> list = new ArrayList<Long>(this.a.size());
        final Iterator<FilterGroup> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().groupId);
        }
        return list;
    }
    
    public interface FiltersConfigDelegate
    {
        void onFiltersConfigInited();
    }
}
