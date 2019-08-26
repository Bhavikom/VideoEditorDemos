// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;

import java.util.Iterator;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.List;
//import org.lasque.tusdk.core.utils.json.DataBase;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class StickerCategory extends JsonBaseBean implements Serializable
{
    @DataBase("id")
    public long id;
    @DataBase("name")
    public String name;
    public List<StickerGroup> datas;
    public StickerCategoryExtendType extendType;
    
    public StickerCategory() {
    }
    
    public StickerCategory(final JSONObject jsonObject) {
        this.deserialize(jsonObject);
    }
    
    public StickerCategory(final String name) {
        this.name = name;
    }
    
    public StickerCategory(final long id, final String s) {
        this(s);
        this.id = id;
    }
    
    public void append(final StickerGroup stickerGroup) {
        if (this.datas == null) {
            this.datas = new ArrayList<StickerGroup>();
        }
        this.datas.add(stickerGroup);
    }
    
    public void insertFirst(final StickerGroup stickerGroup) {
        if (this.datas == null) {
            this.datas = new ArrayList<StickerGroup>();
        }
        this.datas.add(0, stickerGroup);
    }
    
    public StickerCategory copy() {
        final StickerCategory stickerCategory = new StickerCategory();
        stickerCategory.id = this.id;
        stickerCategory.name = this.name;
        if (this.datas == null) {
            return stickerCategory;
        }
        stickerCategory.datas = new ArrayList<StickerGroup>(this.datas.size());
        final Iterator<StickerGroup> iterator = this.datas.iterator();
        while (iterator.hasNext()) {
            stickerCategory.datas.add(iterator.next().copy());
        }
        return stickerCategory;
    }
    
    public StickerGroup removeGroup(final long n) {
        if (this.datas == null) {
            return null;
        }
        for (final StickerGroup stickerGroup : new ArrayList<StickerGroup>(this.datas)) {
            if (stickerGroup.groupId == n) {
                this.datas.remove(stickerGroup);
                return stickerGroup;
            }
        }
        return null;
    }
    
    public void deserialize(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.id = jsonObject.optLong("id", 0L);
        this.name = jsonObject.optString("name");
    }
    
    public enum StickerCategoryType
    {
        StickerCategorySmart(0L), 
        StickerCategoryOther(1L);
        
        private long a;
        
        private StickerCategoryType(final long a) {
            this.a = a;
        }
        
        public long getValue() {
            return this.a;
        }
    }
    
    public enum StickerCategoryExtendType
    {
        ExtendTypeAll, 
        ExtendTypeHistory;
    }
}
