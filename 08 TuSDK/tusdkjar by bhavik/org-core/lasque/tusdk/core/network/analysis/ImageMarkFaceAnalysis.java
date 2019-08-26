package org.lasque.tusdk.core.network.analysis;

import android.graphics.Bitmap;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class ImageMarkFaceAnalysis
{
  private ImageOnlineAnalysis a;
  
  public void reset()
  {
    if (this.a != null)
    {
      this.a.cancel();
      this.a = null;
    }
  }
  
  public void analysisWithThumb(Bitmap paramBitmap, final ImageFaceMarkAnalysisListener paramImageFaceMarkAnalysisListener)
  {
    if (paramImageFaceMarkAnalysisListener == null) {
      return;
    }
    if (paramBitmap == null)
    {
      paramImageFaceMarkAnalysisListener.onImageFaceAnalysisCompleted(null, ImageOnlineAnalysis.ImageAnalysisType.NotInputImage);
      return;
    }
    this.a = new ImageOnlineAnalysis();
    this.a.setImage(paramBitmap);
    this.a.analysisFaces(new ImageOnlineAnalysis.ImageAnalysisListener()
    {
      public <T extends JsonBaseBean> void onImageAnalysisCompleted(T paramAnonymousT, ImageOnlineAnalysis.ImageAnalysisType paramAnonymousImageAnalysisType)
      {
        if (paramAnonymousImageAnalysisType != ImageOnlineAnalysis.ImageAnalysisType.Succeed)
        {
          paramImageFaceMarkAnalysisListener.onImageFaceAnalysisCompleted(null, paramAnonymousImageAnalysisType);
          return;
        }
        ImageMarkFaceResult localImageMarkFaceResult = (ImageMarkFaceResult)paramAnonymousT;
        paramImageFaceMarkAnalysisListener.onImageFaceAnalysisCompleted(localImageMarkFaceResult, paramAnonymousImageAnalysisType);
      }
    });
  }
  
  public static abstract interface ImageFaceMarkAnalysisListener
  {
    public abstract void onImageFaceAnalysisCompleted(ImageMarkFaceResult paramImageMarkFaceResult, ImageOnlineAnalysis.ImageAnalysisType paramImageAnalysisType);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\analysis\ImageMarkFaceAnalysis.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */