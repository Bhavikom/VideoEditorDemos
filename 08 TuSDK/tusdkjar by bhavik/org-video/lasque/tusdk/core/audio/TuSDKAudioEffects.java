package org.lasque.tusdk.core.audio;

import android.annotation.TargetApi;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.AudioEffect.Descriptor;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import java.util.List;
import java.util.UUID;
import org.lasque.tusdk.core.utils.TLog;

class TuSDKAudioEffects
{
  private static final UUID a = UUID.fromString("bb392ec0-8d4d-11e0-a896-0002a5d5c51b");
  private static final UUID b = UUID.fromString("aa8130e0-66fc-11e0-bad0-0002a5d5c51b");
  private static final UUID c = UUID.fromString("c06c8400-8e06-11e0-9cb6-0002a5d5c51b");
  private static AudioEffect.Descriptor[] d = null;
  private AcousticEchoCanceler e = null;
  private AutomaticGainControl f = null;
  private NoiseSuppressor g = null;
  private boolean h = false;
  private boolean i = false;
  private boolean j = false;
  
  public static boolean isAcousticEchoCancelerSupported()
  {
    return (TuSDKAudioEffectUtils.runningOnJellyBeanOrHigher()) && (e());
  }
  
  public static boolean isAutomaticGainControlSupported()
  {
    return (TuSDKAudioEffectUtils.runningOnJellyBeanOrHigher()) && (f());
  }
  
  public static boolean isNoiseSuppressorSupported()
  {
    return (TuSDKAudioEffectUtils.runningOnJellyBeanOrHigher()) && (g());
  }
  
  public static boolean isAcousticEchoCancelerBlacklisted()
  {
    List localList = TuSDKAudioEffectUtils.getBlackListedModelsForAecUsage();
    boolean bool = localList.contains(Build.MODEL);
    if (bool) {
      TLog.w(Build.MODEL + " is blacklisted for HW AEC usage!", new Object[0]);
    }
    return bool;
  }
  
  public static boolean isAutomaticGainControlBlacklisted()
  {
    List localList = TuSDKAudioEffectUtils.getBlackListedModelsForAgcUsage();
    boolean bool = localList.contains(Build.MODEL);
    if (bool) {
      TLog.w(Build.MODEL + " is blacklisted for HW AGC usage!", new Object[0]);
    }
    return bool;
  }
  
  public static boolean isNoiseSuppressorBlacklisted()
  {
    List localList = TuSDKAudioEffectUtils.getBlackListedModelsForNsUsage();
    boolean bool = localList.contains(Build.MODEL);
    if (bool) {
      TLog.w("TuSDKAudioEffects", new Object[] { Build.MODEL + " is blacklisted for HW NS usage!" });
    }
    return bool;
  }
  
  @TargetApi(18)
  private static boolean b()
  {
    for (AudioEffect.Descriptor localDescriptor : ) {
      if ((localDescriptor.type.equals(AudioEffect.EFFECT_TYPE_AEC)) && (localDescriptor.uuid.equals(a)))
      {
        TLog.w("the platform AEC should be excluded based on its UUID", new Object[0]);
        return true;
      }
    }
    return false;
  }
  
  @TargetApi(18)
  private static boolean c()
  {
    for (AudioEffect.Descriptor localDescriptor : ) {
      if ((localDescriptor.type.equals(AudioEffect.EFFECT_TYPE_AGC)) && (localDescriptor.uuid.equals(b))) {
        return true;
      }
    }
    return false;
  }
  
  @TargetApi(18)
  private static boolean d()
  {
    for (AudioEffect.Descriptor localDescriptor : ) {
      if ((localDescriptor.type.equals(AudioEffect.EFFECT_TYPE_NS)) && (localDescriptor.uuid.equals(c))) {
        return true;
      }
    }
    return false;
  }
  
  @TargetApi(18)
  private static boolean e()
  {
    return a(AudioEffect.EFFECT_TYPE_AEC);
  }
  
  @TargetApi(18)
  private static boolean f()
  {
    return a(AudioEffect.EFFECT_TYPE_AGC);
  }
  
  @TargetApi(18)
  private static boolean g()
  {
    return a(AudioEffect.EFFECT_TYPE_NS);
  }
  
  public static boolean canUseAcousticEchoCanceler()
  {
    boolean bool = (isAcousticEchoCancelerSupported()) && (!TuSDKAudioEffectUtils.useBasedAcousticEchoCanceler()) && (!isAcousticEchoCancelerBlacklisted()) && (!b());
    TLog.d("canUseAcousticEchoCanceler: " + bool, new Object[0]);
    return bool;
  }
  
  public static boolean canUseAutomaticGainControl()
  {
    boolean bool = (isAutomaticGainControlSupported()) && (!TuSDKAudioEffectUtils.useBasedAutomaticGainControl()) && (!isAutomaticGainControlBlacklisted()) && (!c());
    TLog.d("canUseAutomaticGainControl: " + bool, new Object[0]);
    return bool;
  }
  
  public static boolean canUseNoiseSuppressor()
  {
    boolean bool = (isNoiseSuppressorSupported()) && (!TuSDKAudioEffectUtils.useBasedNoiseSuppressor()) && (!isNoiseSuppressorBlacklisted()) && (!d());
    TLog.d("canUseNoiseSuppressor: " + bool, new Object[0]);
    return bool;
  }
  
  static TuSDKAudioEffects a()
  {
    if (!TuSDKAudioEffectUtils.runningOnJellyBeanOrHigher())
    {
      TLog.w("API level 16 or higher is required!", new Object[0]);
      return null;
    }
    return new TuSDKAudioEffects();
  }
  
  public boolean setAEC(boolean paramBoolean)
  {
    if (!canUseAcousticEchoCanceler())
    {
      TLog.w("Platform AEC is not supported", new Object[0]);
      this.h = false;
      return false;
    }
    if ((this.e != null) && (paramBoolean != this.h))
    {
      TLog.e("Platform AEC state can't be modified while recording", new Object[0]);
      return false;
    }
    this.h = paramBoolean;
    return true;
  }
  
  public boolean setAGC(boolean paramBoolean)
  {
    if (!canUseAutomaticGainControl())
    {
      TLog.w("Platform AGC is not supported", new Object[0]);
      this.i = false;
      return false;
    }
    if ((this.f != null) && (paramBoolean != this.i))
    {
      TLog.e("Platform AGC state can't be modified while recording", new Object[0]);
      return false;
    }
    this.i = paramBoolean;
    return true;
  }
  
  public boolean setNS(boolean paramBoolean)
  {
    if (!canUseNoiseSuppressor())
    {
      TLog.w("Platform NS is not supported", new Object[0]);
      this.j = false;
      return false;
    }
    if ((this.g != null) && (paramBoolean != this.j))
    {
      TLog.e("Platform NS state can't be modified while recording", new Object[0]);
      return false;
    }
    this.j = paramBoolean;
    return true;
  }
  
  public void enable(int paramInt)
  {
    a(this.e == null);
    a(this.f == null);
    a(this.g == null);
    boolean bool1;
    boolean bool2;
    if (isAcousticEchoCancelerSupported())
    {
      this.e = AcousticEchoCanceler.create(paramInt);
      if (this.e != null)
      {
        bool1 = this.e.getEnabled();
        bool2 = (this.h) && (canUseAcousticEchoCanceler());
        if (this.e.setEnabled(bool2) != 0) {
          TLog.e("Failed to set the AcousticEchoCanceler state", new Object[0]);
        }
        TLog.d("AcousticEchoCanceler: is " + (this.e.getEnabled() ? "enabled" : "disabled"), new Object[0]);
      }
      else
      {
        TLog.e("Failed to create the AcousticEchoCanceler instance", new Object[0]);
      }
    }
    if (isAutomaticGainControlSupported())
    {
      this.f = AutomaticGainControl.create(paramInt);
      if (this.f != null)
      {
        bool1 = this.f.getEnabled();
        bool2 = (this.i) && (canUseAutomaticGainControl());
        if (this.f.setEnabled(bool2) != 0) {
          TLog.e("Failed to set the AutomaticGainControl state", new Object[0]);
        }
        TLog.d("AutomaticGainControl: is " + (this.f.getEnabled() ? "enabled" : "disabled"), new Object[0]);
      }
      else
      {
        TLog.e("Failed to create the AutomaticGainControl instance", new Object[0]);
      }
    }
    if (isNoiseSuppressorSupported())
    {
      this.g = NoiseSuppressor.create(paramInt);
      if (this.g != null)
      {
        bool1 = this.g.getEnabled();
        bool2 = (this.j) && (canUseNoiseSuppressor());
        if (this.g.setEnabled(bool2) != 0) {
          TLog.e("Failed to set the NoiseSuppressor state", new Object[0]);
        }
        TLog.d("NoiseSuppressor: is " + (this.g.getEnabled() ? "enabled" : "disabled"), new Object[0]);
      }
      else
      {
        TLog.e("Failed to create the NoiseSuppressor instance", new Object[0]);
      }
    }
  }
  
  public void release()
  {
    if (this.e != null)
    {
      this.e.release();
      this.e = null;
    }
    if (this.f != null)
    {
      this.f.release();
      this.f = null;
    }
    if (this.g != null)
    {
      this.g.release();
      this.g = null;
    }
  }
  
  private static void a(boolean paramBoolean)
  {
    if (!paramBoolean) {
      throw new AssertionError("Expected condition to be true");
    }
  }
  
  private static AudioEffect.Descriptor[] h()
  {
    if (d != null) {
      return d;
    }
    d = AudioEffect.queryEffects();
    return d;
  }
  
  private static boolean a(UUID paramUUID)
  {
    AudioEffect.Descriptor[] arrayOfDescriptor1 = h();
    if (arrayOfDescriptor1 == null) {
      return false;
    }
    for (AudioEffect.Descriptor localDescriptor : arrayOfDescriptor1) {
      if (localDescriptor.type.equals(paramUUID)) {
        return true;
      }
    }
    return false;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\audio\TuSDKAudioEffects.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */