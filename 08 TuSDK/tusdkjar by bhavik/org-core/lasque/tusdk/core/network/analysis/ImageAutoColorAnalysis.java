package org.lasque.tusdk.core.network.analysis;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import java.io.File;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.modules.components.ComponentActType;

public class ImageAutoColorAnalysis
{
  private ImageColorArgument a;
  private ImageOnlineAnalysis b;
  private _ImageAutoColorAnalysisWrap c;
  
  public _ImageAutoColorAnalysisWrap getFilter()
  {
    if (this.c != null) {
      return this.c.clone();
    }
    this.c = _ImageAutoColorAnalysisWrap.a();
    return this.c;
  }
  
  public void reset()
  {
    if (this.b != null)
    {
      this.b.cancel();
      this.b = null;
    }
    this.c = null;
    this.a = null;
  }
  
  public void analysisWithImage(final Bitmap paramBitmap, final ImageAutoColorAnalysisListener paramImageAutoColorAnalysisListener)
  {
    if (paramImageAutoColorAnalysisListener == null) {
      return;
    }
    if (paramBitmap == null)
    {
      paramImageAutoColorAnalysisListener.onImageAutoColorAnalysisCompleted(null, ImageOnlineAnalysis.ImageAnalysisType.NotInputImage);
      return;
    }
    if (this.a != null)
    {
      a(paramBitmap, paramImageAutoColorAnalysisListener);
      return;
    }
    this.b = new ImageOnlineAnalysis();
    this.b.setImage(paramBitmap);
    this.b.analysisColor(new ImageOnlineAnalysis.ImageAnalysisListener()
    {
      public <T extends JsonBaseBean> void onImageAnalysisCompleted(T paramAnonymousT, ImageOnlineAnalysis.ImageAnalysisType paramAnonymousImageAnalysisType)
      {
        if (paramAnonymousImageAnalysisType != ImageOnlineAnalysis.ImageAnalysisType.Succeed)
        {
          paramImageAutoColorAnalysisListener.onImageAutoColorAnalysisCompleted(null, paramAnonymousImageAnalysisType);
          return;
        }
        ImageAnalysisResult localImageAnalysisResult = (ImageAnalysisResult)paramAnonymousT;
        if ((localImageAnalysisResult != null) && (localImageAnalysisResult.color != null))
        {
          ImageAutoColorAnalysis.a(ImageAutoColorAnalysis.this, localImageAnalysisResult.color);
          ImageAutoColorAnalysis._ImageAutoColorAnalysisWrap.a(ImageAutoColorAnalysis.this.getFilter(), ImageAutoColorAnalysis.a(ImageAutoColorAnalysis.this));
          StatisticsManger.appendComponent(ComponentActType.image_Analysis_color);
        }
        ImageAutoColorAnalysis.a(ImageAutoColorAnalysis.this, paramBitmap, paramImageAutoColorAnalysisListener);
      }
    });
  }
  
  public void copyAnalysis(final File paramFile1, final File paramFile2, final ImageAutoColorAnalysisCopyListener paramImageAutoColorAnalysisCopyListener)
  {
    if ((paramImageAutoColorAnalysisCopyListener == null) || (this.a == null)) {
      return;
    }
    if ((paramFile1 == null) || (!paramFile1.exists()) || (paramFile2 == null))
    {
      paramImageAutoColorAnalysisCopyListener.onImageAutoColorAnalysisCopyCompleted(null);
      return;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        Bitmap localBitmap = BitmapHelper.getBitmap(paramFile1, TuSdkSize.create(TuSdkGPU.getMaxTextureOptimizedSize()), true);
        localBitmap = ImageAutoColorAnalysis.this.getFilter().process(localBitmap);
        final boolean bool = BitmapHelper.saveBitmap(paramFile2, localBitmap, 95);
        localBitmap = null;
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            ImageAutoColorAnalysis.2.this.c.onImageAutoColorAnalysisCopyCompleted(bool ? ImageAutoColorAnalysis.2.this.b : null);
          }
        });
      }
    });
  }
  
  private void a(final Bitmap paramBitmap, final ImageAutoColorAnalysisListener paramImageAutoColorAnalysisListener)
  {
    if (paramBitmap == null)
    {
      paramImageAutoColorAnalysisListener.onImageAutoColorAnalysisCompleted(null, ImageOnlineAnalysis.ImageAnalysisType.NotInputImage);
      return;
    }
    if (this.a == null)
    {
      paramImageAutoColorAnalysisListener.onImageAutoColorAnalysisCompleted(null, ImageOnlineAnalysis.ImageAnalysisType.ServiceFailed);
      return;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        final Bitmap localBitmap = ImageAutoColorAnalysis.this.getFilter().process(paramBitmap);
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            ImageAutoColorAnalysis.3.this.b.onImageAutoColorAnalysisCompleted(localBitmap, ImageOnlineAnalysis.ImageAnalysisType.Succeed);
          }
        });
      }
    });
  }
  
  public void analysisWithThumb(Bitmap paramBitmap, final File paramFile1, final File paramFile2, final ImageAutoColorAnalysisListener paramImageAutoColorAnalysisListener, final ImageAutoColorAnalysisCopyListener paramImageAutoColorAnalysisCopyListener)
  {
    if (paramImageAutoColorAnalysisListener == null) {
      return;
    }
    analysisWithImage(paramBitmap, new ImageAutoColorAnalysisListener()
    {
      public void onImageAutoColorAnalysisCompleted(Bitmap paramAnonymousBitmap, ImageOnlineAnalysis.ImageAnalysisType paramAnonymousImageAnalysisType)
      {
        paramImageAutoColorAnalysisListener.onImageAutoColorAnalysisCompleted(paramAnonymousBitmap, paramAnonymousImageAnalysisType);
        if (paramAnonymousImageAnalysisType != ImageOnlineAnalysis.ImageAnalysisType.Succeed)
        {
          if (paramImageAutoColorAnalysisCopyListener != null) {
            paramImageAutoColorAnalysisCopyListener.onImageAutoColorAnalysisCopyCompleted(null);
          }
          return;
        }
        ImageAutoColorAnalysis.this.copyAnalysis(paramFile1, paramFile2, paramImageAutoColorAnalysisCopyListener);
      }
    });
  }
  
  private static class _ImageAutoColorAnalysisFiler
    extends SelesFilter
  {
    private int a;
    private int b;
    private int c;
    private ImageColorArgument d = new ImageColorArgument();
    
    private _ImageAutoColorAnalysisFiler()
    {
      super();
      this.d.maxR = 1.0F;
      this.d.maxG = 1.0F;
      this.d.maxB = 1.0F;
      this.d.maxY = 1.0F;
      this.d.minR = 0.0F;
      this.d.minG = 0.0F;
      this.d.minB = 0.0F;
      this.d.minY = 0.0F;
      this.d.midR = 0.5F;
      this.d.midG = 0.5F;
      this.d.midB = 0.5F;
    }
    
    protected void onInitOnGLThread()
    {
      super.onInitOnGLThread();
      this.a = this.mFilterProgram.uniformIndex("maxRGBA");
      this.b = this.mFilterProgram.uniformIndex("minRGBA");
      this.c = this.mFilterProgram.uniformIndex("midRGB");
      a(this.d);
    }
    
    private void a(final float paramFloat1, final float paramFloat2, final float paramFloat3, final float paramFloat4)
    {
      runOnDraw(new Runnable()
      {
        public void run()
        {
          float[] arrayOfFloat = new float[4];
          arrayOfFloat[0] = paramFloat1;
          arrayOfFloat[1] = paramFloat2;
          arrayOfFloat[2] = paramFloat3;
          arrayOfFloat[3] = paramFloat4;
          SelesContext.setActiveShaderProgram(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.b(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.this));
          GLES20.glUniform4fv(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.c(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.this), 1, FloatBuffer.wrap(arrayOfFloat));
        }
      });
    }
    
    private void b(final float paramFloat1, final float paramFloat2, final float paramFloat3, final float paramFloat4)
    {
      runOnDraw(new Runnable()
      {
        public void run()
        {
          float[] arrayOfFloat = new float[4];
          arrayOfFloat[0] = paramFloat1;
          arrayOfFloat[1] = paramFloat2;
          arrayOfFloat[2] = paramFloat3;
          arrayOfFloat[3] = paramFloat4;
          SelesContext.setActiveShaderProgram(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.d(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.this));
          GLES20.glUniform4fv(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.e(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.this), 1, FloatBuffer.wrap(arrayOfFloat));
        }
      });
    }
    
    private void a(final float paramFloat1, final float paramFloat2, final float paramFloat3)
    {
      runOnDraw(new Runnable()
      {
        public void run()
        {
          float[] arrayOfFloat = new float[3];
          arrayOfFloat[0] = paramFloat1;
          arrayOfFloat[1] = paramFloat2;
          arrayOfFloat[2] = paramFloat3;
          SelesContext.setActiveShaderProgram(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.f(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.this));
          GLES20.glUniform3fv(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.g(ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.this), 1, FloatBuffer.wrap(arrayOfFloat));
        }
      });
    }
    
    private ImageColorArgument a()
    {
      return this.d;
    }
    
    private void a(ImageColorArgument paramImageColorArgument)
    {
      if (paramImageColorArgument == null) {
        return;
      }
      this.d = paramImageColorArgument;
      a(paramImageColorArgument.maxR, paramImageColorArgument.maxG, paramImageColorArgument.maxB, paramImageColorArgument.maxY);
      b(paramImageColorArgument.minR, paramImageColorArgument.minG, paramImageColorArgument.minB, paramImageColorArgument.minY);
      a(paramImageColorArgument.midR, paramImageColorArgument.midG, paramImageColorArgument.midB);
    }
  }
  
  private static class _ImageAutoColorAnalysisWrap
    extends FilterWrap
  {
    private static _ImageAutoColorAnalysisWrap b()
    {
      FilterOption local1 = new FilterOption()
      {
        public SelesOutInput getFilter()
        {
          return new ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler(null);
        }
      };
      return new _ImageAutoColorAnalysisWrap(local1);
    }
    
    private _ImageAutoColorAnalysisWrap(FilterOption paramFilterOption)
    {
      super();
    }
    
    public _ImageAutoColorAnalysisWrap clone()
    {
      _ImageAutoColorAnalysisWrap local_ImageAutoColorAnalysisWrap = b();
      if (local_ImageAutoColorAnalysisWrap != null)
      {
        local_ImageAutoColorAnalysisWrap.setFilterParameter(getFilterParameter());
        ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.a((ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler)local_ImageAutoColorAnalysisWrap.getFilter(), ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.a((ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler)getFilter()));
      }
      return local_ImageAutoColorAnalysisWrap;
    }
    
    private void a(ImageColorArgument paramImageColorArgument)
    {
      ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler.a((ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler)getFilter(), paramImageColorArgument);
    }
  }
  
  public static abstract interface ImageAutoColorAnalysisCopyListener
  {
    public abstract void onImageAutoColorAnalysisCopyCompleted(File paramFile);
  }
  
  public static abstract interface ImageAutoColorAnalysisListener
  {
    public abstract void onImageAutoColorAnalysisCompleted(Bitmap paramBitmap, ImageOnlineAnalysis.ImageAnalysisType paramImageAnalysisType);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\analysis\ImageAutoColorAnalysis.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */