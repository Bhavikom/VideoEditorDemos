// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.FilterAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.util.List;
//import org.lasque.tusdk.core.TuSdkConfigs;
//import org.lasque.tusdk.core.secret.FilterAdapter;

public class FilterManager implements FilterAdapter.FiltersConfigDelegate
{
    private static FilterManager a;
    private FilterLocalPackage b;
    private FilterManagerDelegate c;
    
    public static FilterManager shared() {
        return FilterManager.a;
    }
    
    public static FilterManager init(final TuSdkConfigs tuSdkConfigs) {
        if (FilterManager.a == null && tuSdkConfigs != null) {
            FilterManager.a = new FilterManager(tuSdkConfigs);
        }
        return FilterManager.a;
    }
    
    public List<String> getFilterNames() {
        return this.b.getCodes();
    }
    
    public boolean isInited() {
        return this.b.isInited();
    }
    
    private FilterManager(final TuSdkConfigs tuSdkConfigs) {
        (this.b = FilterLocalPackage.init(tuSdkConfigs)).setInitDelegate(this);
    }
    
    public void checkFilterManager(final FilterManagerDelegate c) {
        this.c = c;
        this.a();
    }
    
    private void a() {
        if (this.b.isInited() && this.c != null) {
            this.c.onFilterManagerInited(this);
            this.c = null;
        }
    }
    
    public boolean isNormalFilter(final String s) {
        return s == null || StringHelper.isEmpty(s) || s.equals("Normal");
    }
    
    public boolean isFilterEffect(final String s) {
        return this.isNormalFilter(s) || this.b.getGroupFiltersType(this.b.option(s).groupId) == 0;
    }
    
    public boolean isSceneEffectFilter(final String s) {
        return s != null && this.b.getGroupFiltersType(this.b.option(s).groupId) == 1;
    }
    
    public boolean isParticleEffectFilter(final String s) {
        return s != null && this.b.getGroupFiltersType(this.b.option(s).groupId) == 2;
    }
    
    public boolean isConmicEffectFilter(final String s) {
        return s != null && this.b.getGroupFiltersType(this.b.option(s).groupId) == 3;
    }
    
    public int getGroupTypeByCode(final String s) {
        return this.b.getGroupType(this.b.option(s).groupId);
    }
    
    @Override
    public void onFiltersConfigInited() {
        this.a();
    }
    
    public Bitmap process(final Bitmap bitmap, final String s) {
        return this.process(bitmap, s, ImageOrientation.Up);
    }
    
    public Bitmap process(final Bitmap bitmap, final String s, final float n) {
        return this.process(bitmap, s, ImageOrientation.Up, n);
    }
    
    public Bitmap process(final Bitmap bitmap, final String s, final SelesParameters selesParameters, final float n) {
        return this.process(bitmap, s, selesParameters, ImageOrientation.Up, n);
    }
    
    public Bitmap process(final Bitmap bitmap, final String s, final ImageOrientation imageOrientation) {
        return this.process(bitmap, s, imageOrientation, 0.0f);
    }
    
    public Bitmap process(final Bitmap bitmap, final String s, final ImageOrientation imageOrientation, final float n) {
        return this.process(bitmap, s, null, imageOrientation, n);
    }
    
    public Bitmap process(final Bitmap bitmap, final String s, final SelesParameters filterParameter, final ImageOrientation imageOrientation, final float n) {
        if (!SdkValid.shared.filterAPIEnabled()) {
            TLog.e("You are not allowed to use the filterAPI feature, please see http://tusdk.com", new Object[0]);
            return null;
        }
        if (this.isNormalFilter(s)) {
            return bitmap;
        }
        final FilterWrap filterWrapWithCode = SdkValid.shared.getFilterWrapWithCode(s);
        if (filterWrapWithCode == null) {
            TLog.e("You are not allowed to use the filter [%s] in Filter API, please see http://tusdk.com", s);
            return null;
        }
        filterWrapWithCode.setFilterParameter(filterParameter);
        return filterWrapWithCode.process(bitmap, imageOrientation, n);
    }
    
    public interface FilterManagerDelegate
    {
        void onFilterManagerInited(final FilterManager p0);
    }
}
