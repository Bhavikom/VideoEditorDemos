// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.util.HashMap;

public final class NativeLibraryHelper
{
    private static NativeLibraryHelper a;
    private HashMap<String, String> b;
    
    public static NativeLibraryHelper shared() {
        if (NativeLibraryHelper.a == null) {
            NativeLibraryHelper.a = new NativeLibraryHelper();
        }
        return NativeLibraryHelper.a;
    }
    
    private NativeLibraryHelper() {
        this.b = new HashMap<String, String>();
    }
    
    public void mapLibrary(final NativeLibType nativeLibType, final String value) {
        this.b.put(nativeLibType.libName(), value);
    }
    
    public void loadLibrary(final NativeLibType nativeLibType) {
        if (this.b.containsKey(nativeLibType.libName())) {
            System.load(this.b.get(nativeLibType.libName()));
        }
        else {
            System.loadLibrary(nativeLibType.libName());
        }
    }
    
    public enum NativeLibType
    {
        LIB_CORE("tusdk-library"), 
        LIB_IMAGE("tusdk-image"), 
        LIB_FACE("tusdk-face"), 
        LIB_FACE_SDM("tusdk-face-smd");
        
        private String a;
        
        private NativeLibType(final String a) {
            this.a = a;
        }
        
        public String libName() {
            return this.a;
        }
    }
}
