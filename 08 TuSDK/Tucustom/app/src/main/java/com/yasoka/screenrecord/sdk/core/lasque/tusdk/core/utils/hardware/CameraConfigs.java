// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

public class CameraConfigs
{
    public enum CameraWhiteBalance
    {
        Auto, 
        Incandescent, 
        Fluorescent, 
        WarmFluorescent, 
        Daylight, 
        CloudyDaylight, 
        Twilight, 
        Shade;
    }
    
    public enum CameraAntibanding
    {
        Off, 
        Auto, 
        RATE_50HZ, 
        RATE_60HZ;
    }
    
    public enum CameraAutoFocus
    {
        Off, 
        Auto, 
        Macro, 
        ContinuousVideo, 
        ContinuousPicture, 
        EDOF;
    }
    
    public enum CameraFacing
    {
        Front, 
        Back;
    }
    
    public enum CameraFlash
    {
        Off, 
        Auto, 
        On, 
        Torch, 
        Always, 
        RedEye;
    }
}
