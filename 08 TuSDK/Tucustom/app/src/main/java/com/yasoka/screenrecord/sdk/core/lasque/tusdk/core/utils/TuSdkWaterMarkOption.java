// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;

public class TuSdkWaterMarkOption
{
    private WaterMarkPosition a;
    private String b;
    private Bitmap c;
    private float d;
    private TextPosition e;
    private int f;
    private int g;
    private String h;
    private String i;
    
    public TuSdkWaterMarkOption() {
        this.a = WaterMarkPosition.BottomRight;
        this.d = 6.0f;
        this.e = TextPosition.Right;
        this.f = 2;
        this.g = 24;
        this.h = "#FFFFFF";
        this.i = "#000000";
    }
    
    public WaterMarkPosition getMarkPosition() {
        return this.a;
    }
    
    public void setMarkPosition(final WaterMarkPosition a) {
        this.a = a;
    }
    
    public String getMarkText() {
        return this.b;
    }
    
    public void setMarkText(final String b) {
        this.b = b;
    }
    
    public Bitmap getMarkImage() {
        return this.c;
    }
    
    public void setMarkImage(final Bitmap c) {
        this.c = c;
    }
    
    public float getMarkMargin() {
        return this.d;
    }
    
    public void setMarkMargin(final float d) {
        this.d = d;
    }
    
    public TextPosition getMarkTextPosition() {
        return this.e;
    }
    
    public void setMarkTextPosition(final TextPosition e) {
        this.e = e;
    }
    
    public int getMarkTextPadding() {
        return this.f;
    }
    
    public void setMarkTextPadding(final int f) {
        this.f = f;
    }
    
    public int getMarkTextSize() {
        return this.g;
    }
    
    public void setMarkTextSize(final int g) {
        this.g = g;
    }
    
    public String getMarkTextColor() {
        return this.h;
    }
    
    public void setMarkTextColor(final String h) {
        this.h = h;
    }
    
    public String getMarkTextShadowColor() {
        return this.i;
    }
    
    public void setMarkTextShadowColor(final String i) {
        this.i = i;
    }
    
    public boolean isValid() {
        return (StringHelper.isNotEmpty(this.b) && StringHelper.isNotBlank(this.b)) || this.c != null;
    }
    
    public void destroy() {
        if (this.c != null) {
            BitmapHelper.recycled(this.c);
        }
    }
    
    public enum TextPosition
    {
        Left, 
        Right;
    }
    
    public enum WaterMarkPosition
    {
        TopLeft, 
        TopRight, 
        BottomLeft, 
        BottomRight, 
        Center;
    }
}
