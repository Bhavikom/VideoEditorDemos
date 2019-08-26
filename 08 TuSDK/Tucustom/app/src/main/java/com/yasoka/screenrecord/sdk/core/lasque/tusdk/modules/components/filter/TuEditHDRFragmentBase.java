// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.filter;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.SdkValid;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorHDRFilter;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorHDRFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuFilterResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;

import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.List;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.impl.activity.TuFilterResultFragment;

public abstract class TuEditHDRFragmentBase extends TuFilterResultFragment
{
    private ByteBuffer a;
    private FilterOption.RunTimeTextureDelegate b;
    
    public TuEditHDRFragmentBase() {
        this.b = new FilterOption.RunTimeTextureDelegate() {
            @Override
            public List<SelesPicture> getRunTimeTextures() {
                final ArrayList<SelesPicture> list = new ArrayList<SelesPicture>();
                list.add(new SelesPicture(TuEditHDRFragmentBase.this.a, 256, 64));
                return list;
            }
        };
    }
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        super.loadView(viewGroup);
        StatisticsManger.appendComponent(ComponentActType.editHDRFragment);
        this.setFilterWrap(FilterLocalPackage.shared().getFilterWrap(null));
    }
    
    private FilterWrap a() {
        final FilterOption filterOption = new FilterOption(this.b) {
            @Override
            public SelesOutInput getFilter() {
                return new TuSDKColorHDRFilter();
            }
        };
        filterOption.id = Long.MAX_VALUE;
        filterOption.canDefinition = true;
        filterOption.isInternal = true;
        final ArrayList<String> internalTextures = new ArrayList<String>();
        internalTextures.add("d78aa55b64bb63f97bc5feb3c6ba5600");
        filterOption.internalTextures = internalTextures;
        return FilterWrap.creat(filterOption);
    }
    
    @Override
    protected boolean preProcessWithImage(final Bitmap bitmap) {
        if (!SdkValid.shared.hdrFilterEnabled()) {
            TLog.e("You are not allowed to use the HDR feature, please see http://tusdk.com", new Object[0]);
            return false;
        }
        this.a = TuSDKColorHDRFilter.getClipHistBuffer(bitmap);
        return true;
    }
    
    @Override
    protected void postProcessWithImage(final Bitmap bitmap) {
        this.setImageViewFilter(this.a());
        this.refreshConfigView();
        super.postProcessWithImage(bitmap);
    }
}
