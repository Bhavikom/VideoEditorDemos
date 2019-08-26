// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge;

//import org.lasque.tusdk.core.utils.json.JsonHelper;
import org.json.JSONObject;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;

import java.util.HashMap;
//import org.lasque.tusdk.core.utils.json.DataBase;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public final class BrushData extends JsonBaseBean implements Serializable
{
    @DataBase("id")
    public long brushId;
    @DataBase("group_id")
    public long groupId;
    @DataBase("code")
    public String code;
    @DataBase("name")
    public String name;
    public String thumb;
    @DataBase("thumb_name")
    public String thumbKey;
    @DataBase("brush_name")
    public String brushImageKey;
    @DataBase("brush_type")
    public int brushType;
    @DataBase("rotate_type")
    public int rotateType;
    @DataBase("position_type")
    public int positionType;
    @DataBase("size_type")
    public int sizeType;
    @DataBase("args")
    public HashMap<String, String> args;
    public boolean isInternal;
    private Bitmap a;
    
    public final Bitmap getImage() {
        return this.a;
    }
    
    public final void setImage(final Bitmap a) {
        this.a = a;
    }
    
    public BrushData() {
    }
    
    public BrushData(final JSONObject jsonObject) {
        this.deserialize(jsonObject);
    }
    
    public BrushType getType() {
        switch (this.brushType) {
            case 4: {
                return BrushType.TypeOnline;
            }
            case 3: {
                return BrushType.TypeStamp;
            }
            case 2: {
                return BrushType.TypeMosaic;
            }
            default: {
                return BrushType.TypeEraser;
            }
        }
    }
    
    public void setType(final BrushType brushType) {
        switch (brushType.ordinal()) {
            case 1: {
                this.brushType = 4;
                break;
            }
            case 2: {
                this.brushType = 3;
                break;
            }
            case 3: {
                this.brushType = 2;
                break;
            }
            default: {
                this.brushType = 1;
                break;
            }
        }
    }
    
    public RotateType getRotateType() {
        switch (this.rotateType) {
            case 4: {
                return RotateType.RotateLimitRandom;
            }
            case 3: {
                return RotateType.RotateRandom;
            }
            case 2: {
                return RotateType.RotateAuto;
            }
            default: {
                return RotateType.RotateNone;
            }
        }
    }
    
    public PositionType getPositionType() {
        switch (this.positionType) {
            case 2: {
                return PositionType.PositionRandom;
            }
            default: {
                return PositionType.PositionAuto;
            }
        }
    }
    
    public SizeType getSizeType() {
        switch (this.sizeType) {
            case 2: {
                return SizeType.SizeRandom;
            }
            default: {
                return SizeType.SizeAuto;
            }
        }
    }
    
    public String getNameKey() {
        if (this.name == null) {
            return String.format("lsq_brush_%s", this.code);
        }
        return this.name;
    }
    
    public BrushData copy() {
        final BrushData brushData = new BrushData();
        brushData.brushId = this.brushId;
        brushData.groupId = this.groupId;
        brushData.code = this.code;
        brushData.brushType = this.brushType;
        brushData.name = this.name;
        brushData.thumb = this.thumb;
        brushData.thumbKey = this.thumbKey;
        brushData.brushImageKey = this.brushImageKey;
        brushData.rotateType = this.rotateType;
        brushData.positionType = this.positionType;
        brushData.sizeType = this.sizeType;
        return brushData;
    }
    
    public static BrushData create(final long brushId, final String thumbKey, final String brushImageKey) {
        final BrushData brushData = new BrushData();
        brushData.brushId = brushId;
        brushData.thumbKey = thumbKey;
        brushData.brushImageKey = brushImageKey;
        return brushData;
    }
    
    public void deserialize(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.brushId = jsonObject.optLong("id", 0L);
        this.code = jsonObject.optString("code");
        this.groupId = jsonObject.optLong("group_id", 0L);
        this.name = jsonObject.optString("name");
        this.thumb = jsonObject.optString("thumb");
        this.thumbKey = jsonObject.optString("thumb_name");
        this.brushImageKey = jsonObject.optString("brush_name");
        this.brushType = jsonObject.optInt("brush_type", 0);
        this.rotateType = jsonObject.optInt("rotate_type", 0);
        this.positionType = jsonObject.optInt("position_type", 0);
        this.sizeType = jsonObject.optInt("size_type", 0);
        this.args = JsonHelper.toHashMap(JsonHelper.getJSONObject(jsonObject, "args"));
    }
    
    public enum SizeType
    {
        SizeAuto, 
        SizeRandom;
    }
    
    public enum PositionType
    {
        PositionAuto, 
        PositionRandom;
    }
    
    public enum RotateType
    {
        RotateNone, 
        RotateAuto, 
        RotateRandom, 
        RotateLimitRandom;
    }
    
    public enum BrushType
    {
        TypeEraser, 
        TypeMosaic, 
        TypeStamp, 
        TypeOnline;
    }
}
