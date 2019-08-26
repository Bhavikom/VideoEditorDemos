// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components;

//import org.lasque.tusdk.core.utils.TuSdkError;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkError;

public enum ComponentErrorType
{
    TypeUnknow(0, "Unknow error"), 
    TypeInputImageEmpty(1001, "Can not found any input image."), 
    TypeUnsupportCamera(2001, "The device unsupport camera."), 
    TypeStorageSpace(3001, "Insufficient storage space."), 
    TypeNotFoundSDCard(3002, "Can not found any SDCard.");
    
    int a;
    String b;
    
    private ComponentErrorType(final int a, final String b) {
        this.a = a;
        this.b = b;
    }
    
    public int getErrorCode() {
        return this.a;
    }
    
    public String getMsg() {
        return this.b;
    }
    
    public TuSdkError getError(final Object o) {
        return new TuSdkError(String.format("Component Error %s(%s): %s", o, this.a, this.b), this.a);
    }
}
