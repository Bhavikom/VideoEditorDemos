package org.lasque.tusdk.core.media.codec.video;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
import org.lasque.tusdk.core.utils.hardware.HardwareHelper;

public class TuSdkVideoSurfaceDecodecOperationPatch
{
  private static final Map<String, String> a = new HashMap();
  
  public TuSdkMediaCodec patchMediaCodec(String paramString)
  {
    boolean bool = false;
    Object localObject1 = a.entrySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      bool = HardwareHelper.isMatchDeviceModelAndManuFacturer((String)((Map.Entry)localObject2).getKey(), (String)((Map.Entry)localObject2).getValue());
      if (bool) {
        break;
      }
    }
    if (!bool) {
      return TuSdkMediaCodecImpl.createDecoderByType(paramString);
    }
    Object localObject2 = paramString;
    int i = -1;
    switch (((String)localObject2).hashCode())
    {
    case 1331836730: 
      if (((String)localObject2).equals("video/avc")) {
        i = 0;
      }
      break;
    }
    switch (i)
    {
    case 0: 
      localObject1 = TuSdkMediaCodecImpl.createByCodecName("OMX.google.h264.decoder");
      break;
    default: 
      localObject1 = TuSdkMediaCodecImpl.createDecoderByType(paramString);
    }
    return (TuSdkMediaCodec)localObject1;
  }
  
  static
  {
    a.put("BKL-AL00", "HUAWEI");
    a.put("MHA-AL00", "HUAWEI");
    a.put("FRD-AL10", "HUAWEI");
    a.put("NXT-AL10", "HUAWEI");
    a.put("PRO 6 Plus", "Meizu");
    a.put("MX6", "Meizu");
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\video\TuSdkVideoSurfaceDecodecOperationPatch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */