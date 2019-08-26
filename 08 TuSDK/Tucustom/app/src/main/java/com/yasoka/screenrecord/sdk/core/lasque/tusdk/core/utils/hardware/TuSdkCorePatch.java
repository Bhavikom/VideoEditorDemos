// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

import java.util.HashMap;
import java.util.Map;

public class TuSdkCorePatch
{
    private static final Map<String, String> a;
    private static final Map<String, String> b;
    
    public static boolean applyThumbRenderPatch() {
        boolean matchDeviceModelAndManuFacturer = false;
        for (final Map.Entry<String, String> entry : TuSdkCorePatch.a.entrySet()) {
            matchDeviceModelAndManuFacturer = HardwareHelper.isMatchDeviceModelAndManuFacturer(entry.getKey(), entry.getValue());
            if (matchDeviceModelAndManuFacturer) {
                break;
            }
        }
        return matchDeviceModelAndManuFacturer;
    }
    
    public static boolean applyDeletedProgramPatch() {
        boolean matchDeviceModelAndManuFacturer = false;
        for (final Map.Entry<String, String> entry : TuSdkCorePatch.b.entrySet()) {
            matchDeviceModelAndManuFacturer = HardwareHelper.isMatchDeviceModelAndManuFacturer(entry.getKey(), entry.getValue());
            if (matchDeviceModelAndManuFacturer) {
                break;
            }
        }
        return matchDeviceModelAndManuFacturer;
    }
    
    static {
        a = new HashMap<String, String>();
        b = new HashMap<String, String>();
        TuSdkCorePatch.a.put("V1732A", "VIVO");
        TuSdkCorePatch.a.put("vivo Y71A", "VIVO");
        TuSdkCorePatch.a.put("SM-J3300", "Samsung");
        TuSdkCorePatch.b.put("Redmi 6", "XIAOMI");
        TuSdkCorePatch.b.put("Redmi 6A", "XIAOMI");
        TuSdkCorePatch.b.put("V1732A", "VIVO");
    }
}
