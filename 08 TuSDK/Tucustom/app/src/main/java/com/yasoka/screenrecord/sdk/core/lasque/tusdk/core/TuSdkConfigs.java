// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

import org.json.JSONArray;
//import org.lasque.tusdk.core.utils.json.JsonHelper;
import org.json.JSONObject;
import java.util.HashMap;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushGroup;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
//import org.lasque.tusdk.core.seles.tusdk.FilterGroup;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.json.DataBase;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class TuSdkConfigs extends JsonBaseBean implements Serializable
{
    @DataBase("app_type")
    public int appType;
    @DataBase("filterGroups")
    public ArrayList<FilterGroup> filterGroups;
    @DataBase("stickerCategories")
    public ArrayList<StickerCategory> stickerCategories;
    @DataBase("stickerGroups")
    public ArrayList<StickerGroup> stickerGroups;
    @DataBase("brushGroups")
    public ArrayList<BrushGroup> brushGroups;
    @DataBase("master")
    public String master;
    @DataBase("masters")
    public HashMap<String, String> masters;
    
    public TuSdkConfigs() {
    }
    
    public TuSdkConfigs(final JSONObject jsonObject) {
        this.deserialize(jsonObject);
    }
    
    public void deserialize(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.master = jsonObject.optString("master", (String)null);
        this.masters = JsonHelper.toHashMap(JsonHelper.getJSONObject(jsonObject, "masters"));
        this.appType = jsonObject.optInt("app_type");
        final JSONArray jsonArray = JsonHelper.getJSONArray(jsonObject, "filterGroups");
        if (jsonArray != null && jsonArray.length() > 0) {
            this.filterGroups = new ArrayList<FilterGroup>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); ++i) {
                this.filterGroups.add(new FilterGroup(jsonArray.optJSONObject(i)));
            }
        }
        final JSONArray jsonArray2 = JsonHelper.getJSONArray(jsonObject, "stickerCategories");
        if (jsonArray2 != null && jsonArray2.length() > 0) {
            this.stickerCategories = new ArrayList<StickerCategory>(jsonArray2.length());
            for (int j = 0; j < jsonArray2.length(); ++j) {
                this.stickerCategories.add(new StickerCategory(jsonArray2.optJSONObject(j)));
            }
        }
        final JSONArray jsonArray3 = JsonHelper.getJSONArray(jsonObject, "stickerGroups");
        if (jsonArray3 != null && jsonArray3.length() > 0) {
            this.stickerGroups = new ArrayList<StickerGroup>(jsonArray3.length());
            for (int k = 0; k < jsonArray3.length(); ++k) {
                this.stickerGroups.add(new StickerGroup(jsonArray3.optJSONObject(k)));
            }
        }
        final JSONArray jsonArray4 = JsonHelper.getJSONArray(jsonObject, "brushGroups");
        if (jsonArray4 != null && jsonArray4.length() > 0) {
            this.brushGroups = new ArrayList<BrushGroup>(jsonArray4.length());
            for (int l = 0; l < jsonArray4.length(); ++l) {
                this.brushGroups.add(new BrushGroup(jsonArray4.optJSONObject(l)));
            }
        }
    }
}
