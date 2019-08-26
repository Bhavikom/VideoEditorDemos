package org.lasque.tusdk.video.editor;

import android.graphics.Bitmap;
import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSdkMediaTextEffectData
  extends TuSdkMediaTileEffectDataBase
{
  public TuSdkMediaTextEffectData(TuSdkImage2DSticker paramTuSdkImage2DSticker)
  {
    super(paramTuSdkImage2DSticker);
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeText);
  }
  
  public TuSdkMediaTextEffectData(Bitmap paramBitmap, float paramFloat1, float paramFloat2, float paramFloat3, TuSdkSize paramTuSdkSize1, TuSdkSize paramTuSdkSize2)
  {
    super(paramBitmap, paramFloat1, paramFloat2, paramFloat3, paramTuSdkSize1, paramTuSdkSize2);
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeText);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaTextEffectData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */