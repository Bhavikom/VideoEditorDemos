package org.lasque.tusdk.api.audio.postproc.resample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.api.postpro.TuSDKPostProcess;
import org.lasque.tusdk.api.postpro.TuSDKPostProcess.PostProcessArg;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;

public class TuSDKAudioResampler
  extends TuSDKPostProcess
{
  public final boolean process(TuSDKMediaDataSource paramTuSDKMediaDataSource, File paramFile, int paramInt1, int paramInt2)
  {
    ArrayList localArrayList = new ArrayList(5);
    if (paramInt1 > 0) {
      localArrayList.add(new TuSDKPostProcess.PostProcessArg("-ar", String.valueOf(paramInt1)));
    }
    if (paramInt2 > 0) {
      localArrayList.add(new TuSDKPostProcess.PostProcessArg("-ac", String.valueOf(paramInt2)));
    }
    if (localArrayList.size() == 0)
    {
      TLog.e("%s : Invalid parameter", new Object[] { this });
      return false;
    }
    localArrayList.add(new TuSDKPostProcess.PostProcessArg("-f", "wav"));
    return super.process(paramTuSDKMediaDataSource, paramFile, localArrayList);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\postproc\resample\TuSDKAudioResampler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */