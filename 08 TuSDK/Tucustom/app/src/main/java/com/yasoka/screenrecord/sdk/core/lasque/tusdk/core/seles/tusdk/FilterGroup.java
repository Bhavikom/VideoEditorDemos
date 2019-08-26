// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;

import org.json.JSONArray;
//import org.lasque.tusdk.core.utils.json.JsonHelper;
import java.util.Locale;
//import org.lasque.tusdk.core.TuSdkContext;
import org.json.JSONObject;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.json.DataBase;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class FilterGroup extends JsonBaseBean implements Serializable
{
    public static final int GROUP_FILTERS_TYPE_GENERAL = 0;
    public static final int GROUP_FILTERS_TYPE_SCENE_EFFECT = 1;
    public static final int GROUP_FILTERS_TYPE_PARTICLE_EFFECT = 2;
    public static final int GROUP_FILTERS_TYPE_CONMIC_EFFECT = 3;
    public static final int ATION_SCEN_IMAGE_SDK = 1;
    public static final int ATION_SCEN_LIVE_SDK = 2;
    public static final int ATION_SCEN_SHORT_VIDEO_SDK = 4;
    @DataBase("id")
    public long groupId;
    @DataBase("category_id")
    public int categoryId;
    @DataBase("type")
    public int groupFiltersType;
    @DataBase("ation_scen")
    public int ation_scen;
    @DataBase("file")
    public String file;
    @DataBase("valid_type")
    public int validType;
    @DataBase("valid_key")
    public String validKey;
    @DataBase("code")
    public String code;
    @DataBase("name")
    public String name;
    @DataBase("thumb")
    public String thumb;
    @DataBase("thumb_key")
    public String thumbKey;
    @DataBase("default_filter_id")
    public long defaultFilterId;
    @DataBase("filters")
    public ArrayList<FilterOption> filters;
    @DataBase("color")
    public String color;
    @DataBase("un_real_time")
    public boolean disableRuntime;
    public boolean isDownload;
    
    public FilterGroup() {
    }
    
    public FilterGroup(final JSONObject jsonObject) {
        this.deserialize(jsonObject);
    }
    
    public static FilterGroup create(final long groupId, final int validType, final String code, final long defaultFilterId, final String color) {
        final FilterGroup filterGroup = new FilterGroup();
        filterGroup.groupId = groupId;
        filterGroup.validType = validType;
        filterGroup.code = code;
        filterGroup.defaultFilterId = defaultFilterId;
        filterGroup.color = color;
        return filterGroup;
    }
    
    public FilterGroup copy() {
        final FilterGroup filterGroup = new FilterGroup();
        filterGroup.groupId = this.groupId;
        filterGroup.categoryId = this.categoryId;
        filterGroup.groupFiltersType = this.groupFiltersType;
        filterGroup.ation_scen = this.ation_scen;
        filterGroup.code = this.code;
        filterGroup.name = this.name;
        filterGroup.thumb = this.thumb;
        filterGroup.defaultFilterId = this.defaultFilterId;
        filterGroup.color = this.color;
        filterGroup.disableRuntime = this.disableRuntime;
        filterGroup.isDownload = this.isDownload;
        return filterGroup;
    }
    
    public FilterOption getFilterOption(final long n) {
        if (this.filters == null) {
            return null;
        }
        for (final FilterOption filterOption : this.filters) {
            if (filterOption.id == n) {
                return filterOption;
            }
        }
        return null;
    }
    
    public FilterOption getDefaultFilter() {
        return this.getFilterOption(this.defaultFilterId);
    }
    
    public String getName() {
        if (TuSdkContext.getStringResId(this.name) <= 0) {
            return this.getNameKey();
        }
        return TuSdkContext.getString(this.name);
    }
    
    public String getNameKey() {
        final String language = Locale.getDefault().getLanguage();
        if (this.name != null && language != null && language.endsWith("zh")) {
            return this.name;
        }
        if (this.code != null) {
            return this.code;
        }
        return this.name;
    }
    
    public int getCategoryId() {
        if (this.categoryId > 0) {
            return this.categoryId;
        }
        return 0;
    }
    
    public int getGroupFiltersType() {
        return this.groupFiltersType;
    }
    
    public String getDefaultFilterThumbKey() {
        final FilterOption defaultFilter = this.getDefaultFilter();
        if (defaultFilter == null) {
            return null;
        }
        return defaultFilter.thumbKey;
    }
    
    public boolean canUseForAtionScenType(final int n) {
        return this.ation_scen == n || (this.ation_scen == 5 && (n == 1 || n == 4));
    }
    
    public void deserialize(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.groupId = jsonObject.optLong("id", 0L);
        this.file = jsonObject.optString("file");
        this.categoryId = jsonObject.optInt("category_id", 0);
        this.groupFiltersType = jsonObject.optInt("type", 0);
        this.ation_scen = jsonObject.optInt("ation_scen", 0);
        this.validType = jsonObject.optInt("valid_type", 0);
        this.validKey = jsonObject.optString("valid_key");
        this.code = jsonObject.optString("code");
        this.name = jsonObject.optString("name");
        this.thumb = jsonObject.optString("thumb");
        this.thumbKey = jsonObject.optString("thumb_key");
        this.color = jsonObject.optString("color");
        this.disableRuntime = (jsonObject.optInt("un_real_time", 0) != 0);
        final JSONArray jsonArray = JsonHelper.getJSONArray(jsonObject, "filters");
        if (jsonArray != null && jsonArray.length() > 0) {
            this.filters = new ArrayList<FilterOption>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); ++i) {
                this.filters.add(new FilterOption(jsonArray.optJSONObject(i)));
            }
        }
        this.defaultFilterId = jsonObject.optLong("default_filter_id", 0L);
        if (this.defaultFilterId < 1L && this.filters != null && this.filters.size() > 0) {
            this.defaultFilterId = this.filters.get(0).id;
        }
    }
}
