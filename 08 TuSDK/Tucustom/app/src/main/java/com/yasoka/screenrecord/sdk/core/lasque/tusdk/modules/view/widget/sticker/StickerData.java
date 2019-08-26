// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

import org.json.JSONArray;
//import org.lasque.tusdk.core.utils.json.JsonHelper;
import java.util.Iterator;
import org.json.JSONObject;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.sticker.StickerPositionInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.StickerPositionInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;

import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.json.DataBase;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class StickerData extends JsonBaseBean implements Serializable
{
    @DataBase("id")
    public long stickerId;
    @DataBase("group_id")
    public long groupId;
    @DataBase("category_id")
    public long categoryId;
    @DataBase("name")
    public String name;
    @DataBase("thumb_name")
    public String previewName;
    @DataBase("sticker_name")
    public String stickerImageName;
    @DataBase("width")
    public int width;
    @DataBase("height")
    public int height;
    @DataBase("type_id")
    public int stickerType;
    @DataBase("texts")
    public ArrayList<StickerText> texts;
    @DataBase("sticker_face_info")
    public StickerPositionInfo positionInfo;
    private Bitmap a;
    
    public TuSdkSize size() {
        return TuSdkSize.create(this.width, this.height);
    }
    
    public TuSdkSize sizePixies() {
        return this.size().dp2Pix();
    }
    
    public final Bitmap getImage() {
        return this.a;
    }
    
    public final void setImage(final Bitmap a) {
        this.a = a;
    }
    
    public StickerData() {
    }
    
    public StickerData(final JSONObject jsonObject) {
        this.deserialize(jsonObject);
    }
    
    public StickerType getType() {
        switch (this.stickerType) {
            case 3: {
                return StickerType.TypeDynamic;
            }
            case 2: {
                return StickerType.TypeText;
            }
            default: {
                return StickerType.TypeImage;
            }
        }
    }
    
    public boolean requireFaceFeature() {
        return this.getType() == StickerType.TypeDynamic && this.positionInfo != null && this.positionInfo.getPosType().getValue() < StickerPositionInfo.StickerPositionType.StickerPosFullScreen.getValue();
    }
    
    public StickerData copy() {
        final StickerData stickerData = new StickerData();
        stickerData.stickerId = this.stickerId;
        stickerData.groupId = this.groupId;
        stickerData.categoryId = this.categoryId;
        stickerData.width = this.width;
        stickerData.height = this.height;
        stickerData.stickerType = this.stickerType;
        stickerData.a = this.a;
        if (this.texts != null && stickerData.stickerType == 2) {
            stickerData.texts = new ArrayList<StickerText>(this.texts.size());
            final Iterator<StickerText> iterator = this.texts.iterator();
            while (iterator.hasNext()) {
                stickerData.texts.add(iterator.next().copy());
            }
        }
        return stickerData;
    }
    
    public StickerText getStickerText(final long n) {
        if (this.texts == null) {
            return null;
        }
        for (final StickerText stickerText : this.texts) {
            if (stickerText.textId == n) {
                return stickerText;
            }
        }
        return null;
    }
    
    public static StickerData create(final long stickerId, final long categoryId, final String previewName, final String stickerImageName, final int width, final int height, final String s) {
        final StickerData stickerData = new StickerData();
        stickerData.stickerId = stickerId;
        stickerData.categoryId = categoryId;
        stickerData.previewName = previewName;
        stickerData.stickerImageName = stickerImageName;
        stickerData.width = width;
        stickerData.height = height;
        return stickerData;
    }
    
    public void deserialize(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.stickerId = jsonObject.optLong("id", 0L);
        this.groupId = jsonObject.optLong("group_id", 0L);
        this.categoryId = jsonObject.optLong("category_id", 0L);
        this.name = jsonObject.optString("name");
        this.previewName = jsonObject.optString("thumb_name");
        this.stickerImageName = jsonObject.optString("sticker_name");
        this.width = jsonObject.optInt("width", 0);
        this.height = jsonObject.optInt("height", 0);
        this.stickerType = jsonObject.optInt("type_id", 0);
        final JSONArray jsonArray = JsonHelper.getJSONArray(jsonObject, "texts");
        if (jsonArray != null && jsonArray.length() > 0) {
            this.texts = new ArrayList<StickerText>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); ++i) {
                this.texts.add(new StickerText(jsonArray.optJSONObject(i)));
            }
        }
        final JSONObject jsonObject2 = JsonHelper.getJSONObject(jsonObject, "sticker_face_info");
        if (jsonObject2 != null) {
            this.positionInfo = new StickerPositionInfo(jsonObject2);
        }
    }
    
    public enum StickerType
    {
        TypeImage, 
        TypeText, 
        TypeDynamic;
    }
}
