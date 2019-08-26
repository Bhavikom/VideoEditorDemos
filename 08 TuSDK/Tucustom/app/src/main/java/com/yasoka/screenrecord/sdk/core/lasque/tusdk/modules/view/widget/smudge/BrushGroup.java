// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;

import org.json.JSONArray;
//import org.lasque.tusdk.core.utils.json.JsonHelper;
import java.util.Iterator;
import org.json.JSONObject;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.json.DataBase;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class BrushGroup extends JsonBaseBean implements Serializable
{
    @DataBase("id")
    public long groupId;
    @DataBase("file")
    public String file;
    @DataBase("valid_type")
    public int validType;
    @DataBase("valid_key")
    public String validKey;
    @DataBase("name")
    public String name;
    @DataBase("brushes")
    public ArrayList<BrushData> brushes;
    public boolean isDownload;
    
    public BrushGroup() {
    }
    
    public BrushGroup(final JSONObject jsonObject) {
        this.deserialize(jsonObject);
    }
    
    public BrushGroup copy() {
        final BrushGroup brushGroup = new BrushGroup();
        brushGroup.groupId = this.groupId;
        brushGroup.name = this.name;
        brushGroup.isDownload = this.isDownload;
        if (this.brushes == null) {
            return brushGroup;
        }
        brushGroup.brushes = new ArrayList<BrushData>(this.brushes.size());
        final Iterator<BrushData> iterator = this.brushes.iterator();
        while (iterator.hasNext()) {
            brushGroup.brushes.add(iterator.next().copy());
        }
        return brushGroup;
    }
    
    public BrushData getBrush(final long n) {
        if (this.brushes == null) {
            return null;
        }
        for (final BrushData brushData : this.brushes) {
            if (brushData.brushId == n) {
                return brushData;
            }
        }
        return null;
    }
    
    public void deserialize(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.groupId = jsonObject.optLong("id", 0L);
        this.file = jsonObject.optString("file");
        this.validType = jsonObject.optInt("valid_type", 0);
        this.validKey = jsonObject.optString("valid_key");
        this.name = jsonObject.optString("name");
        final JSONArray jsonArray = JsonHelper.getJSONArray(jsonObject, "brushes");
        if (jsonArray != null && jsonArray.length() > 0) {
            this.brushes = new ArrayList<BrushData>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); ++i) {
                this.brushes.add(new BrushData(jsonArray.optJSONObject(i)));
            }
        }
    }
}
