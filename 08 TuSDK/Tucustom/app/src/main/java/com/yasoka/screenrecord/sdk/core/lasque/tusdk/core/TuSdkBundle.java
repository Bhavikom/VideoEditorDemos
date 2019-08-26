// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core;

import java.io.File;

public class TuSdkBundle
{
    public static final String BUNDLE = "TuSDK.bundle";
    public static final String FILTER_TEXTURES = "textures";
    public static final String LOCAL_STICKERS = "stickers";
    public static final String LOCAL_BRUSHES = "brushes";
    public static final String OTHER_RESOURES = "others";
    public static final String MODEL_RESOURES = "model";
    public static final String CAMERA_FOCUS_BEEP_AUDIO_RAW = "camera_focus_beep.mp3";
    public static final String INTERNAL_FILTERS_CONFIG = "lsq_internal_filters.filter";
    
    public static String sdkBundle() {
        return "TuSDK.bundle";
    }
    
    public static String sdkBundle(final String s) {
        return String.format("%s%s%s", sdkBundle(), File.separator, s);
    }
    
    public static String sdkBundle(final String s, final String s2) {
        return sdkBundle(String.format("%s%s%s", s, File.separator, s2));
    }
    
    public static String sdkBundleTexture(final String s) {
        return sdkBundle("textures", s);
    }
    
    public static String sdkBundleOther(final String s) {
        return sdkBundle("others", s);
    }
    
    public static String sdkBundleModel(final String s) {
        return sdkBundle("model", s);
    }
    
    public static String sdkBundleSticker(final String s) {
        return sdkBundle("stickers", s);
    }
    
    public static String sdkBundleBrush(final String s) {
        return sdkBundle("brushes", s);
    }
}
