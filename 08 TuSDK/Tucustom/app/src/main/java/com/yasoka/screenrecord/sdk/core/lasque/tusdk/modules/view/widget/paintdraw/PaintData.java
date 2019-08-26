// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.paintdraw;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;

import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class PaintData extends JsonBaseBean implements Serializable
{
    private Object a;
    private PaintType b;
    
    public PaintData(final int i, final PaintType b) {
        this.b = b;
        this.a = i;
    }
    
    public Object getData() {
        return this.a;
    }
    
    public void setData(final Object a) {
        this.a = a;
    }
    
    public PaintType getPaintType() {
        return this.b;
    }
    
    public void setPaintType(final PaintType b) {
        this.b = b;
    }
    
    public enum PaintType
    {
        Color, 
        Image;
    }
}
