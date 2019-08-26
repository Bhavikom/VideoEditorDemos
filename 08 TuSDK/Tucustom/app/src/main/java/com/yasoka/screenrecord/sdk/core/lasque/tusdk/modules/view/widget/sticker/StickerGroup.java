// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

import org.json.JSONArray;
//import org.lasque.tusdk.core.utils.json.JsonHelper;
//import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import android.text.TextUtils;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkHttpEngine;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;

import java.util.Locale;
import java.util.Iterator;
import org.json.JSONObject;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.json.DataBase;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class StickerGroup extends JsonBaseBean implements Serializable
{
    @DataBase("id")
    public long groupId;
    @DataBase("category_id")
    public long categoryId;
    @DataBase("file")
    public String file;
    @DataBase("valid_type")
    public int validType;
    @DataBase("valid_key")
    public String validKey;
    @DataBase("name")
    public String name;
    @DataBase("thumb")
    public String previewName;
    @DataBase("name_en")
    public String name_en;
    @DataBase("stickers")
    public ArrayList<StickerData> stickers;
    public boolean isDownload;
    
    public StickerGroup() {
    }
    
    public StickerGroup(final JSONObject jsonObject) {
        this.deserialize(jsonObject);
    }
    
    public StickerGroup copy() {
        final StickerGroup stickerGroup = new StickerGroup();
        stickerGroup.groupId = this.groupId;
        stickerGroup.categoryId = this.categoryId;
        stickerGroup.name = this.name;
        stickerGroup.name_en = this.name_en;
        stickerGroup.isDownload = this.isDownload;
        if (this.stickers == null) {
            return stickerGroup;
        }
        stickerGroup.stickers = new ArrayList<StickerData>(this.stickers.size());
        final Iterator<StickerData> iterator = this.stickers.iterator();
        while (iterator.hasNext()) {
            stickerGroup.stickers.add(iterator.next().copy());
        }
        return stickerGroup;
    }
    
    public StickerData getSticker(final long n) {
        if (this.stickers == null) {
            return null;
        }
        for (final StickerData stickerData : this.stickers) {
            if (stickerData.stickerId == n) {
                return stickerData;
            }
        }
        return null;
    }
    
    public boolean requireFaceFeature() {
        if (this.stickers == null) {
            return false;
        }
        final Iterator<StickerData> iterator = this.stickers.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().requireFaceFeature()) {
                return true;
            }
        }
        return false;
    }
    
    public String getNameOfLanguage() {
        final String language = Locale.getDefault().getLanguage();
        if (!TextUtils.isEmpty((CharSequence)this.name_en) && language != null && !language.endsWith("zh")) {
            return this.name_en;
        }
        return this.name;
    }
    
    public String getPreviewNamePath() {
        if (TextUtils.isEmpty((CharSequence)this.previewName)) {
            return null;
        }
        return TuSdkHttpEngine.WEB_PIC_DOMAIN + "/" + this.previewName;
    }
    
    public void deserialize(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.groupId = jsonObject.optLong("id", 0L);
        this.categoryId = jsonObject.optLong("category_id", 0L);
        this.file = jsonObject.optString("file");
        this.validType = jsonObject.optInt("valid_type", 0);
        this.validKey = jsonObject.optString("valid_key");
        this.name = jsonObject.optString("name");
        this.name_en = jsonObject.optString("name_en");
        this.previewName = jsonObject.optString("thumb");
        final JSONArray jsonArray = JsonHelper.getJSONArray(jsonObject, "stickers");
        if (jsonArray != null && jsonArray.length() > 0) {
            this.stickers = new ArrayList<StickerData>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); ++i) {
                this.stickers.add(new StickerData(jsonArray.optJSONObject(i)));
            }
        }
    }
}
