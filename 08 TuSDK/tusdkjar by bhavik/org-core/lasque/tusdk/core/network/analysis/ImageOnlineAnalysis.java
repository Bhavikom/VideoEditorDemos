package org.lasque.tusdk.core.network.analysis;

import android.graphics.Bitmap;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.lasque.tusdk.core.http.RequestHandle;
import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import org.lasque.tusdk.core.network.TuSdkHttpHandler;
import org.lasque.tusdk.core.network.TuSdkHttpParams;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.TuSdkError;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.core.utils.json.JsonWrapper;

public class ImageOnlineAnalysis
{
  public static final int ERROR_CODE_NO_FACE_FOUND = -601;
  public static final int ERROR_CODE_NO_ACCESS_RIGHT = -303;
  private Bitmap a;
  private RequestHandle b;
  
  public void setImage(Bitmap paramBitmap)
  {
    this.a = paramBitmap;
  }
  
  public void cancel()
  {
    if (this.b == null) {
      return;
    }
    if (!this.b.isCancelled()) {
      this.b.cancel(true);
    }
    this.b = null;
  }
  
  public void analysisColor(ImageAnalysisListener paramImageAnalysisListener)
  {
    a("/image/infos", null, ImageAnalysisResult.class, paramImageAnalysisListener);
  }
  
  public void analysisFaces(ImageAnalysisListener paramImageAnalysisListener)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("marks", Integer.valueOf(5));
    localHashMap.put("normalize", Integer.valueOf(1));
    localHashMap.put("mutiple", Integer.valueOf(1));
    a("/face/landmark", localHashMap, ImageMarkFaceResult.class, paramImageAnalysisListener);
  }
  
  private <T extends JsonBaseBean> void a(final String paramString, final Map<String, Object> paramMap, final Class<T> paramClass, final ImageAnalysisListener paramImageAnalysisListener)
  {
    if ((StringHelper.isBlank(paramString)) || (paramImageAnalysisListener == null)) {
      return;
    }
    if (this.a == null)
    {
      paramImageAnalysisListener.onImageAnalysisCompleted(null, ImageAnalysisType.NotInputImage);
      return;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        ImageOnlineAnalysis.a(ImageOnlineAnalysis.this, paramString, paramMap, paramClass, paramImageAnalysisListener);
      }
    });
  }
  
  private <T extends JsonBaseBean> void b(final String paramString, final Map<String, Object> paramMap, final Class<T> paramClass, final ImageAnalysisListener paramImageAnalysisListener)
  {
    Bitmap localBitmap = BitmapHelper.imageLimit(this.a, 512);
    this.a = null;
    final InputStream localInputStream = BitmapHelper.bitmap2InputStream(localBitmap, 70);
    localBitmap = null;
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        ImageOnlineAnalysis.this.mainThreadRequest(localInputStream, paramString, paramMap, paramClass, paramImageAnalysisListener);
      }
    });
  }
  
  protected <T extends JsonBaseBean> void mainThreadRequest(InputStream paramInputStream, String paramString, Map<String, Object> paramMap, final Class<T> paramClass, final ImageAnalysisListener paramImageAnalysisListener)
  {
    if (paramInputStream == null)
    {
      paramImageAnalysisListener.onImageAnalysisCompleted(null, ImageAnalysisType.NotInputImage);
      return;
    }
    cancel();
    TuSdkHttpHandler local3 = new TuSdkHttpHandler()
    {
      protected void onRequestedSucceed(TuSdkHttpHandler paramAnonymousTuSdkHttpHandler)
      {
        JsonBaseBean localJsonBaseBean = paramAnonymousTuSdkHttpHandler.getJson().getJsonWithType(paramClass);
        paramImageAnalysisListener.onImageAnalysisCompleted(localJsonBaseBean, ImageOnlineAnalysis.ImageAnalysisType.Succeed);
      }
      
      protected void onRequestedFailed(TuSdkHttpHandler paramAnonymousTuSdkHttpHandler)
      {
        if ((paramAnonymousTuSdkHttpHandler.getError() != null) && (paramAnonymousTuSdkHttpHandler.getError().getErrorCode() == 64935)) {
          paramImageAnalysisListener.onImageAnalysisCompleted(null, ImageOnlineAnalysis.ImageAnalysisType.Succeed);
        } else if ((paramAnonymousTuSdkHttpHandler.getError() != null) && (paramAnonymousTuSdkHttpHandler.getError().getErrorCode() == 65233)) {
          paramImageAnalysisListener.onImageAnalysisCompleted(null, ImageOnlineAnalysis.ImageAnalysisType.NoAccessRight);
        } else {
          paramImageAnalysisListener.onImageAnalysisCompleted(null, ImageOnlineAnalysis.ImageAnalysisType.ServiceFailed);
        }
      }
      
      protected void onRequestedFinish(TuSdkHttpHandler paramAnonymousTuSdkHttpHandler)
      {
        ImageOnlineAnalysis.a(ImageOnlineAnalysis.this, null);
      }
    };
    TuSdkHttpParams localTuSdkHttpParams = new TuSdkHttpParams();
    localTuSdkHttpParams.put("pic", paramInputStream);
    if (paramMap != null)
    {
      Iterator localIterator = paramMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localTuSdkHttpParams.put(str, paramMap.get(str));
      }
    }
    TuSdkHttpEngine.service().postService(paramString, localTuSdkHttpParams, local3);
  }
  
  public static abstract interface ImageAnalysisListener
  {
    public abstract <T extends JsonBaseBean> void onImageAnalysisCompleted(T paramT, ImageOnlineAnalysis.ImageAnalysisType paramImageAnalysisType);
  }
  
  public static enum ImageAnalysisType
  {
    private ImageAnalysisType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\analysis\ImageOnlineAnalysis.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */