// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

public class TuSdkError extends Error
{
    private int a;
    
    public int getErrorCode() {
        return this.a;
    }
    
    public TuSdkError() {
    }
    
    public TuSdkError(final String message) {
        super(message);
    }
    
    public TuSdkError(final String message, final int a) {
        super(message);
        this.a = a;
    }
    
    public TuSdkError(final Throwable cause) {
        super(cause);
    }
    
    public TuSdkError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
