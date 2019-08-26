package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkAudioEffectsImpl
  implements TuSdkAudioEffects
{
  private boolean a;
  private boolean b;
  private boolean c;
  private int d = -1;
  private final List<AudioEffect> e = new ArrayList();
  
  public TuSdkAudioEffectsImpl(int paramInt)
  {
    setAudioSession(paramInt);
  }
  
  public void setAudioSession(int paramInt)
  {
    this.d = paramInt;
  }
  
  public void release()
  {
    Iterator localIterator = this.e.iterator();
    while (localIterator.hasNext())
    {
      AudioEffect localAudioEffect = (AudioEffect)localIterator.next();
      try
      {
        localAudioEffect.release();
      }
      catch (Exception localException)
      {
        TLog.e(localException, "%s release error: %s", new Object[] { "TuSdkAudioEffectsImpl", localAudioEffect });
      }
    }
    this.e.clear();
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public boolean enableAcousticEchoCanceler()
  {
    if (this.a) {
      return true;
    }
    if (!a(AcousticEchoCanceler.isAvailable(), "enableAcousticEchoCanceler")) {
      return false;
    }
    AcousticEchoCanceler localAcousticEchoCanceler = AcousticEchoCanceler.create(this.d);
    this.a = a(localAcousticEchoCanceler, "AcousticEchoCanceler");
    if (this.a) {
      this.e.add(localAcousticEchoCanceler);
    }
    return this.a;
  }
  
  public boolean enableAutomaticGainControl()
  {
    if (this.b) {
      return true;
    }
    if (!a(AutomaticGainControl.isAvailable(), "enableAutomaticGainControl")) {
      return false;
    }
    AutomaticGainControl localAutomaticGainControl = AutomaticGainControl.create(this.d);
    this.b = a(localAutomaticGainControl, "AutomaticGainControl");
    if (this.b) {
      this.e.add(localAutomaticGainControl);
    }
    return this.b;
  }
  
  public boolean enableNoiseSuppressor()
  {
    if (this.c) {
      return true;
    }
    if (!a(NoiseSuppressor.isAvailable(), "enableNoiseSuppressor")) {
      return false;
    }
    NoiseSuppressor localNoiseSuppressor = NoiseSuppressor.create(this.d);
    this.c = a(localNoiseSuppressor, "NoiseSuppressor");
    if (this.c) {
      this.e.add(localNoiseSuppressor);
    }
    return this.c;
  }
  
  private boolean a(boolean paramBoolean, String paramString)
  {
    if (this.d < 1)
    {
      TLog.w("%s %s need a AudioSession.", new Object[] { "TuSdkAudioEffectsImpl", paramString });
      return false;
    }
    if (!paramBoolean)
    {
      TLog.w("%s can not support %s.", new Object[] { "TuSdkAudioEffectsImpl", paramString });
      return false;
    }
    return true;
  }
  
  private boolean a(AudioEffect paramAudioEffect, String paramString)
  {
    if (paramAudioEffect == null)
    {
      TLog.w("%s create %s failed.", new Object[] { "TuSdkAudioEffectsImpl", paramString });
      return false;
    }
    boolean bool = false;
    try
    {
      paramAudioEffect.setEnabled(true);
      bool = paramAudioEffect.getEnabled();
    }
    catch (Exception localException)
    {
      TLog.e(localException, "%s enable %s failed.", new Object[] { "TuSdkAudioEffectsImpl", paramString });
      return false;
    }
    return bool;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioEffectsImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */