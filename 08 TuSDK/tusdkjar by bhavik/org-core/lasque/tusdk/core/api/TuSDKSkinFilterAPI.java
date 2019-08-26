package org.lasque.tusdk.core.api;

import android.graphics.Bitmap;
import android.graphics.PointF;
import java.io.File;
import java.util.ArrayList;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis;
import org.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener;
import org.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis.ImageAutoColorAnalysisListener;
import org.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument;
import org.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument.ImageItems;
import org.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument.ImageMarks;
import org.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument.ImageMarksPoints;
import org.lasque.tusdk.core.network.analysis.ImageMarkFaceAnalysis;
import org.lasque.tusdk.core.network.analysis.ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener;
import org.lasque.tusdk.core.network.analysis.ImageMarkFaceResult;
import org.lasque.tusdk.core.network.analysis.ImageOnlineAnalysis.ImageAnalysisType;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinWhiteningFilter;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKSkinFilterAPI
{
  private float a = 0.3F;
  private float b = 0.3F;
  private float c = 5000.0F;
  private float d = 1.045F;
  private float e = 0.048F;
  private FilterWrap f;
  private ImageMarkFaceAnalysis g;
  private TuSDKSkinWhiteningFilter h;
  private PointF[] i;
  private Object j = new Object();
  private SkinFilterManagerDelegate k;
  private SkinFilterManagerDelegate l;
  private AutoAdjustResultDelegate m;
  private Bitmap n;
  private boolean o = true;
  private ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener p = new ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener()
  {
    public void onImageFaceAnalysisCompleted(ImageMarkFaceResult paramAnonymousImageMarkFaceResult, ImageOnlineAnalysis.ImageAnalysisType paramAnonymousImageAnalysisType)
    {
      synchronized (TuSDKSkinFilterAPI.f(TuSDKSkinFilterAPI.this))
      {
        switch (TuSDKSkinFilterAPI.8.a[paramAnonymousImageAnalysisType.ordinal()])
        {
        case 1: 
          if ((paramAnonymousImageMarkFaceResult == null) || (paramAnonymousImageMarkFaceResult.count <= 0))
          {
            TLog.e("Error: no face detected", new Object[0]);
            if (TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this) != null) {
              TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this).onFaceMarkResult(TuSDKSkinFilterAPI.TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeNoFaceDetected);
            }
          }
          else
          {
            if (paramAnonymousImageMarkFaceResult.count == 1)
            {
              TuSDKSkinFilterAPI.a(TuSDKSkinFilterAPI.this, TuSDKSkinFilterAPI.a(TuSDKSkinFilterAPI.this, paramAnonymousImageMarkFaceResult));
              FaceAligment localFaceAligment = new FaceAligment();
              localFaceAligment.setOrginMarks(TuSDKSkinFilterAPI.h(TuSDKSkinFilterAPI.this));
              TuSDKSkinFilterAPI.l(TuSDKSkinFilterAPI.this).updateFaceFeatures(new FaceAligment[] { localFaceAligment }, 0.0F);
              TuSDKSkinFilterAPI.f(TuSDKSkinFilterAPI.this).notify();
              if (TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this) != null) {
                TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this).onFaceMarkResult(TuSDKSkinFilterAPI.TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeSucceed);
              }
              return;
            }
            TLog.e("Error: multiple faces detected", new Object[0]);
            if (TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this) != null) {
              TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this).onFaceMarkResult(TuSDKSkinFilterAPI.TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeFailedMultipleFacesDetected);
            }
          }
          break;
        case 2: 
          TLog.e("You are not allowed to use the face mark api, please see http://tusdk.com", new Object[0]);
          if (TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this) != null) {
            TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this).onFaceMarkResult(TuSDKSkinFilterAPI.TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeFailed);
          }
          break;
        default: 
          if (TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this) != null) {
            TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this).onFaceMarkResult(TuSDKSkinFilterAPI.TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeFailed);
          }
          TLog.e("error on face mark :%s", new Object[] { paramAnonymousImageAnalysisType });
        }
        TuSDKSkinFilterAPI.f(TuSDKSkinFilterAPI.this).notify();
      }
    }
  };
  private ImageAutoColorAnalysis q;
  private ImageAutoColorAnalysis.ImageAutoColorAnalysisListener r = new ImageAutoColorAnalysis.ImageAutoColorAnalysisListener()
  {
    public void onImageAutoColorAnalysisCompleted(Bitmap paramAnonymousBitmap, ImageOnlineAnalysis.ImageAnalysisType paramAnonymousImageAnalysisType)
    {
      if (paramAnonymousImageAnalysisType == ImageOnlineAnalysis.ImageAnalysisType.Succeed)
      {
        if (TuSDKSkinFilterAPI.m(TuSDKSkinFilterAPI.this) != null) {
          TuSDKSkinFilterAPI.m(TuSDKSkinFilterAPI.this).onGetAutoAdjustResult(paramAnonymousBitmap);
        }
      }
      else
      {
        if (TuSDKSkinFilterAPI.m(TuSDKSkinFilterAPI.this) != null) {
          TuSDKSkinFilterAPI.m(TuSDKSkinFilterAPI.this).onGetAutoAdjustResult(TuSDKSkinFilterAPI.n(TuSDKSkinFilterAPI.this));
        }
        TLog.e("error on auto adjust:%s", new Object[] { paramAnonymousImageAnalysisType });
      }
    }
  };
  private ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener s = new ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener()
  {
    public void onImageAutoColorAnalysisCopyCompleted(File paramAnonymousFile) {}
  };
  
  public TuSDKSkinFilterAPI(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    setParameters(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
  }
  
  public TuSDKSkinFilterAPI() {}
  
  private FilterWrap a()
  {
    if (this.f == null) {}
    FilterOption local1 = new FilterOption()
    {
      public SelesOutInput getFilter()
      {
        TuSDKSkinWhiteningFilter localTuSDKSkinWhiteningFilter = new TuSDKSkinWhiteningFilter();
        localTuSDKSkinWhiteningFilter.setSmoothing(TuSDKSkinFilterAPI.a(TuSDKSkinFilterAPI.this));
        localTuSDKSkinWhiteningFilter.setWhitening(TuSDKSkinFilterAPI.b(TuSDKSkinFilterAPI.this));
        localTuSDKSkinWhiteningFilter.setSkinColor(TuSDKSkinFilterAPI.c(TuSDKSkinFilterAPI.this));
        localTuSDKSkinWhiteningFilter.setEyeEnlargeSize(TuSDKSkinFilterAPI.d(TuSDKSkinFilterAPI.this));
        localTuSDKSkinWhiteningFilter.setChinSize(TuSDKSkinFilterAPI.e(TuSDKSkinFilterAPI.this));
        TuSDKSkinFilterAPI.a(TuSDKSkinFilterAPI.this, localTuSDKSkinWhiteningFilter);
        return localTuSDKSkinWhiteningFilter;
      }
    };
    local1.id = Long.MAX_VALUE;
    local1.canDefinition = true;
    local1.isInternal = true;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("f8a6ed3ec939d6941c94a272aff1791b");
    local1.internalTextures = localArrayList;
    FilterWrap localFilterWrap = FilterWrap.creat(local1);
    return localFilterWrap;
  }
  
  public TuSDKSkinFilterAPI setSmoothing(float paramFloat)
  {
    if ((paramFloat < 0.0F) || (paramFloat > 1.0F)) {
      return this;
    }
    this.a = paramFloat;
    return this;
  }
  
  public TuSDKSkinFilterAPI setWhitening(float paramFloat)
  {
    if ((paramFloat < 0.0F) || (paramFloat > 1.0F)) {
      return this;
    }
    this.b = paramFloat;
    return this;
  }
  
  public TuSDKSkinFilterAPI setSkinColor(float paramFloat)
  {
    if ((paramFloat < 4000.0F) || (paramFloat > 6000.0F)) {
      return this;
    }
    this.c = paramFloat;
    return this;
  }
  
  public TuSDKSkinFilterAPI setEyeSize(float paramFloat)
  {
    if ((paramFloat < 1.0F) || (paramFloat > 1.2F)) {
      return this;
    }
    this.d = paramFloat;
    return this;
  }
  
  public TuSDKSkinFilterAPI setChinSize(float paramFloat)
  {
    if ((paramFloat < 0.0F) || (paramFloat > 0.1F)) {
      return this;
    }
    this.e = paramFloat;
    return this;
  }
  
  public TuSDKSkinFilterAPI setParameters(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    return setSmoothing(paramFloat1).setWhitening(paramFloat2).setSkinColor(paramFloat3).setEyeSize(paramFloat4).setChinSize(paramFloat5);
  }
  
  public void process(Bitmap paramBitmap, SkinFilterManagerDelegate paramSkinFilterManagerDelegate)
  {
    if (!b())
    {
      if (paramSkinFilterManagerDelegate != null) {
        paramSkinFilterManagerDelegate.onGetSkinFilterResult(paramBitmap);
      }
      return;
    }
    this.k = paramSkinFilterManagerDelegate;
    this.o = ((this.n == null) || (this.n.isRecycled()) || (this.n != paramBitmap) || (this.i == null) || (this.i.length == 0));
    this.n = paramBitmap;
    a(this.n, ImageOrientation.Up);
    boolean bool = this.o;
    if (bool) {
      b(paramBitmap);
    }
  }
  
  private void a(Bitmap paramBitmap, ImageOrientation paramImageOrientation)
  {
    a(paramBitmap, paramImageOrientation, 0.0F);
  }
  
  private void a(Bitmap paramBitmap, ImageOrientation paramImageOrientation, float paramFloat)
  {
    a(paramBitmap, null, paramImageOrientation, paramFloat);
  }
  
  private void a(final Bitmap paramBitmap, SelesParameters paramSelesParameters, final ImageOrientation paramImageOrientation, final float paramFloat)
  {
    if (this.o)
    {
      this.i = null;
      a(paramBitmap);
    }
    final FilterWrap localFilterWrap = a();
    localFilterWrap.setFilterParameter(paramSelesParameters);
    final TuSDKSkinWhiteningFilter localTuSDKSkinWhiteningFilter = (TuSDKSkinWhiteningFilter)localFilterWrap.getFilter();
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        synchronized (TuSDKSkinFilterAPI.f(TuSDKSkinFilterAPI.this))
        {
          try
          {
            if (TuSDKSkinFilterAPI.g(TuSDKSkinFilterAPI.this)) {
              TuSDKSkinFilterAPI.f(TuSDKSkinFilterAPI.this).wait();
            }
          }
          catch (InterruptedException localInterruptedException)
          {
            localInterruptedException.printStackTrace();
          }
        }
        if (localTuSDKSkinWhiteningFilter != null)
        {
          ??? = new FaceAligment();
          ((FaceAligment)???).setOrginMarks(TuSDKSkinFilterAPI.h(TuSDKSkinFilterAPI.this));
          localTuSDKSkinWhiteningFilter.updateFaceFeatures(new FaceAligment[] { ??? }, 0.0F);
        }
        ??? = localFilterWrap.process(paramBitmap, paramImageOrientation, paramFloat);
        if (TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this) != null) {
          TuSDKSkinFilterAPI.i(TuSDKSkinFilterAPI.this).onGetSkinFilterResult((Bitmap)???);
        }
      }
    });
  }
  
  public void handleLocalSkinFilterProcess(Bitmap paramBitmap, SkinFilterManagerDelegate paramSkinFilterManagerDelegate)
  {
    if (!b())
    {
      if (paramSkinFilterManagerDelegate != null) {
        paramSkinFilterManagerDelegate.onGetSkinFilterResult(paramBitmap);
      }
      return;
    }
    this.l = paramSkinFilterManagerDelegate;
    b(paramBitmap, null, ImageOrientation.Up, 0.0F);
  }
  
  private void b(Bitmap paramBitmap, SelesParameters paramSelesParameters, ImageOrientation paramImageOrientation, float paramFloat)
  {
    FilterWrap localFilterWrap = a();
    Bitmap localBitmap = localFilterWrap.process(paramBitmap, paramImageOrientation, paramFloat);
    if (this.l != null) {
      this.l.onGetSkinFilterResult(localBitmap);
    }
  }
  
  private void a(final Bitmap paramBitmap)
  {
    if (this.g == null) {
      this.g = new ImageMarkFaceAnalysis();
    } else {
      this.g.reset();
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        TuSDKSkinFilterAPI.k(TuSDKSkinFilterAPI.this).analysisWithThumb(paramBitmap, TuSDKSkinFilterAPI.j(TuSDKSkinFilterAPI.this));
      }
    });
  }
  
  private PointF[] a(ImageMarkFaceResult paramImageMarkFaceResult)
  {
    PointF[] arrayOfPointF = new PointF[5];
    ImageMark5FaceArgument.ImageMarksPoints localImageMarksPoints = null;
    int i1 = 0;
    ImageMark5FaceArgument.ImageItems localImageItems = (ImageMark5FaceArgument.ImageItems)paramImageMarkFaceResult.items.get(0);
    localImageMarksPoints = localImageItems.marks.eye_left;
    arrayOfPointF[(i1++)] = new PointF(localImageMarksPoints.x, localImageMarksPoints.y);
    localImageMarksPoints = localImageItems.marks.eye_right;
    arrayOfPointF[(i1++)] = new PointF(localImageMarksPoints.x, localImageMarksPoints.y);
    localImageMarksPoints = localImageItems.marks.nose;
    arrayOfPointF[(i1++)] = new PointF(localImageMarksPoints.x, localImageMarksPoints.y);
    localImageMarksPoints = localImageItems.marks.mouth_left;
    arrayOfPointF[(i1++)] = new PointF(localImageMarksPoints.x, localImageMarksPoints.y);
    localImageMarksPoints = localImageItems.marks.mouth_right;
    arrayOfPointF[(i1++)] = new PointF(localImageMarksPoints.x, localImageMarksPoints.y);
    return arrayOfPointF;
  }
  
  private void b(final Bitmap paramBitmap)
  {
    if (this.q == null) {
      this.q = new ImageAutoColorAnalysis();
    } else {
      this.q.reset();
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        TuSDKSkinFilterAPI.a(TuSDKSkinFilterAPI.this, paramBitmap);
      }
    });
  }
  
  private void c(Bitmap paramBitmap)
  {
    if (paramBitmap == null) {
      return;
    }
    File localFile = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", new Object[] { StringHelper.timeStampString() }));
    BitmapHelper.saveBitmap(localFile, paramBitmap, 95);
    this.q.analysisWithThumb(paramBitmap, localFile, null, this.r, this.s);
  }
  
  private boolean b()
  {
    if (!SdkValid.shared.sdkValid())
    {
      TLog.e("Configuration not found! Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
      return false;
    }
    if (SdkValid.shared.isExpired())
    {
      TLog.e("Your account has expired Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
      return false;
    }
    return true;
  }
  
  public static abstract interface AutoAdjustResultDelegate
  {
    public abstract void onGetAutoAdjustResult(Bitmap paramBitmap);
  }
  
  public static abstract interface SkinFilterManagerDelegate
  {
    public abstract void onGetSkinFilterResult(Bitmap paramBitmap);
    
    public abstract void onFaceMarkResult(TuSDKSkinFilterAPI.TuSDKSkinFilterFaceMarkResultType paramTuSDKSkinFilterFaceMarkResultType);
  }
  
  public static enum TuSDKSkinFilterFaceMarkResultType
  {
    private TuSDKSkinFilterFaceMarkResultType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\TuSDKSkinFilterAPI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */