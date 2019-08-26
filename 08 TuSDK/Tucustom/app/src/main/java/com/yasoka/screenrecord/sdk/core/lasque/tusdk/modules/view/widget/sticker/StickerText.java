// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;

import org.json.JSONObject;
//import org.lasque.tusdk.core.utils.json.DataBase;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class StickerText extends JsonBaseBean implements Serializable
{
    @DataBase("id")
    public long textId;
    @DataBase("sticker_id")
    public long stickerId;
    @DataBase("group_id")
    public long groupId;
    @DataBase("category_id")
    public long categoryId;
    @DataBase("type")
    public int textType;
    @DataBase("content")
    public String content;
    @DataBase("color")
    public String color;
    @DataBase("shadowColor")
    public String shadowColor;
    @DataBase("backgroundColor")
    public String backgroundColor;
    @DataBase("underline")
    public boolean underline;
    @DataBase("size")
    public float textSize;
    @DataBase("alignment")
    public int alignment;
    @DataBase("paddings")
    public int paddings;
    @DataBase("rect_left")
    public float rectLeft;
    @DataBase("rect_top")
    public float rectTop;
    @DataBase("rect_width")
    public float rectWidth;
    @DataBase("rect_height")
    public float rectHeight;
    
    public StickerText() {
    }
    
    public StickerText(final JSONObject jsonObject) {
        this.deserialize(jsonObject);
    }
    
    public StickerText copy() {
        final StickerText stickerText = new StickerText();
        stickerText.textId = this.textId;
        stickerText.stickerId = this.stickerId;
        stickerText.groupId = this.groupId;
        stickerText.categoryId = this.categoryId;
        stickerText.textType = this.textType;
        stickerText.content = this.content;
        stickerText.color = this.color;
        stickerText.shadowColor = this.shadowColor;
        stickerText.textSize = this.textSize;
        stickerText.alignment = this.alignment;
        stickerText.rectLeft = this.rectLeft;
        stickerText.rectTop = this.rectTop;
        stickerText.rectWidth = this.rectWidth;
        stickerText.rectHeight = this.rectHeight;
        stickerText.paddings = this.paddings;
        stickerText.underline = this.underline;
        stickerText.backgroundColor = this.backgroundColor;
        return stickerText;
    }
    
    public StickerTextType getType() {
        switch (this.textType) {
            case 2: {
                return StickerTextType.TypeTime;
            }
            case 3: {
                return StickerTextType.TypeDate;
            }
            case 4: {
                return StickerTextType.TypeDateTime;
            }
            case 5: {
                return StickerTextType.TypeLocal;
            }
            case 6: {
                return StickerTextType.TypeWeather;
            }
            default: {
                return StickerTextType.TypeDefault;
            }
        }
    }
    
    public RectF getRect() {
        return new RectF(this.rectLeft, this.rectTop, this.rectLeft + this.rectWidth, this.rectTop + this.rectHeight);
    }
    
    public int getColor() {
        if (this.color == null) {
            return -1;
        }
        return Color.parseColor(this.color);
    }
    
    public int getShadowColor() {
        if (this.shadowColor == null) {
            return -16777216;
        }
        return Color.parseColor(this.shadowColor);
    }
    
    public Paint.Align getAlignment() {
        switch (this.alignment) {
            case 1: {
                return Paint.Align.CENTER;
            }
            case 2: {
                return Paint.Align.RIGHT;
            }
            default: {
                return Paint.Align.LEFT;
            }
        }
    }
    
    public void deserialize(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.textId = jsonObject.optLong("id", 0L);
        this.stickerId = jsonObject.optLong("sticker_id", 0L);
        this.groupId = jsonObject.optLong("group_id", 0L);
        this.categoryId = jsonObject.optLong("category_id", 0L);
        this.textType = jsonObject.optInt("type", 0);
        this.content = jsonObject.optString("content");
        this.color = jsonObject.optString("color");
        this.shadowColor = jsonObject.optString("shadowColor");
        this.textSize = (float)jsonObject.optDouble("size", 0.0);
        this.alignment = jsonObject.optInt("alignment", 0);
        this.rectLeft = (float)jsonObject.optDouble("rect_left", 0.0);
        this.rectTop = (float)jsonObject.optDouble("rect_top", 0.0);
        this.rectWidth = (float)jsonObject.optDouble("rect_width", 0.0);
        this.rectHeight = (float)jsonObject.optDouble("rect_height", 0.0);
    }
    
    public enum StickerTextType
    {
        TypeDefault, 
        TypeTime, 
        TypeDate, 
        TypeDateTime, 
        TypeLocal, 
        TypeWeather;
    }
}
