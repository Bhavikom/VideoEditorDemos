package org.lasque.tusdk.core.media.codec.video;

import android.annotation.TargetApi;
import android.media.MediaFormat;
import android.os.Build;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.utils.hardware.HardwareHelper;

@TargetApi(18)
public class TuSdkVideoSurfaceEncodecOperationPatch
{
  private static final Map<String, String> a = new HashMap();
  private static final List<String> b = Arrays.asList(new String[] { "XIAOMI", "OPPO" });
  
  public TuSdkMediaCodec patchMediaCodec()
  {
    boolean bool = false;
    Object localObject = a.entrySet().iterator();
    while (((Iterator)localObject).hasNext())
    {
      Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
      bool = HardwareHelper.isMatchDeviceModelAndManuFacturer((String)localEntry.getKey(), (String)localEntry.getValue());
      if (bool) {
        break;
      }
    }
    if (!bool) {
      return null;
    }
    localObject = TuSdkMediaCodecImpl.createByCodecName("OMX.google.h264.encoder");
    return (TuSdkMediaCodec)localObject;
  }
  
  public boolean patchRequestKeyFrame(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return false;
    }
    int i = TuSdkMediaFormat.getInteger(paramMediaFormat, "i-frame-interval", 0);
    boolean bool = TuSdkMediaFormat.isEnableKeyFrameASAP(paramMediaFormat);
    if (i < 0) {
      paramMediaFormat.setInteger("i-frame-interval", 0);
    }
    String str = Build.MANUFACTURER;
    if ((str != null) && (b.contains(str.toUpperCase())) && (i < 1)) {
      paramMediaFormat.setInteger("i-frame-interval", 1);
    }
    return bool;
  }
  
  public boolean isEnableCompatibilityMode()
  {
    boolean bool = HardwareHelper.isMatchDeviceModelAndManuFacturer("MI NOTE LTE", "Xiaomi");
    return bool;
  }
  
  static
  {
    a.put("FRD-AL00", "HUAWEI");
    a.put("MHA-AL00", "HUAWEI");
    a.put("BKL-AL00", "HUAWEI");
    a.put("FRD-AL10", "HUAWEI");
    a.put("NXT-AL10", "HUAWEI");
    a.put("BND-AL10", "HUAWEI");
    a.put("BND-TL10", "HUAWEI");
    a.put("MP1605", "Meitu");
    a.put("Nexus 6P", "HUAWEI");
    a.put("HUAWEI NXT-AL10", "HUAWEI");
    a.put("KNT-AL10", "HUAWEI");
    a.put("KNT-AL20", "HUAWEI");
    a.put("ANE-TL00", "HUAWEI");
    a.put("MX6", "MeiZu");
    a.put("LLD-AL10", "HUAWEI");
    a.put("WAS-AL00", "HUAWEI");
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\video\TuSdkVideoSurfaceEncodecOperationPatch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */