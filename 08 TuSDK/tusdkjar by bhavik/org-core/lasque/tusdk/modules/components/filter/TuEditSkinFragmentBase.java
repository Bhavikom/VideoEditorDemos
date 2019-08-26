package org.lasque.tusdk.modules.components.filter;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument;
import org.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument.ImageItems;
import org.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument.ImageMarks;
import org.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument.ImageMarksPoints;
import org.lasque.tusdk.core.network.analysis.ImageMarkFaceAnalysis;
import org.lasque.tusdk.core.network.analysis.ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener;
import org.lasque.tusdk.core.network.analysis.ImageMarkFaceResult;
import org.lasque.tusdk.core.network.analysis.ImageOnlineAnalysis.ImageAnalysisType;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinWhiteningFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.impl.activity.TuFilterResultFragment;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuEditSkinFragmentBase
  extends TuFilterResultFragment
{
  private ImageMarkFaceAnalysis a;
  private TuSDKSkinWhiteningFilter b;
  private PointF[] c;
  protected float mRetouchSize = 1.0F;
  private int d = -1;
  private float e;
  private ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener f = new ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener()
  {
    public void onImageFaceAnalysisCompleted(ImageMarkFaceResult paramAnonymousImageMarkFaceResult, ImageOnlineAnalysis.ImageAnalysisType paramAnonymousImageAnalysisType)
    {
      if (paramAnonymousImageAnalysisType == ImageOnlineAnalysis.ImageAnalysisType.Succeed)
      {
        if ((paramAnonymousImageMarkFaceResult == null) || (paramAnonymousImageMarkFaceResult.count <= 0))
        {
          TuEditSkinFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_error_no_face"));
        }
        else
        {
          if (paramAnonymousImageMarkFaceResult.count == 1)
          {
            TuEditSkinFragmentBase.a(TuEditSkinFragmentBase.this, TuEditSkinFragmentBase.a(TuEditSkinFragmentBase.this, paramAnonymousImageMarkFaceResult));
            FaceAligment localFaceAligment = new FaceAligment();
            localFaceAligment.setOrginMarks(TuEditSkinFragmentBase.c(TuEditSkinFragmentBase.this));
            TuEditSkinFragmentBase.d(TuEditSkinFragmentBase.this).updateFaceFeatures(new FaceAligment[] { localFaceAligment }, 0.0F);
            TuEditSkinFragmentBase.e(TuEditSkinFragmentBase.this);
            TuEditSkinFragmentBase.this.hubDismiss();
            TuEditSkinFragmentBase.this.onFaceDetectionResult(true);
            return;
          }
          TuEditSkinFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_error_multi_face"));
        }
      }
      else if (paramAnonymousImageAnalysisType == ImageOnlineAnalysis.ImageAnalysisType.NoAccessRight)
      {
        TLog.e("You are not allowed to use the face mark api, please see http://tusdk.com", new Object[0]);
        TuEditSkinFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_error_no_face_access"));
      }
      else
      {
        TLog.e("error on face mark :%s", new Object[] { paramAnonymousImageAnalysisType });
        TuEditSkinFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_skin_error"));
      }
      TuEditSkinFragmentBase.this.onFaceDetectionResult(false);
    }
  };
  
  public void setRetouchSize(float paramFloat)
  {
    this.mRetouchSize = paramFloat;
  }
  
  protected abstract void setConfigViewShowState(boolean paramBoolean);
  
  protected abstract View buildActionButton(String paramString, int paramInt);
  
  protected abstract void onFaceDetectionResult(boolean paramBoolean);
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.editSkinFragment);
    setFilterWrap(a());
    super.loadView(paramViewGroup);
    buildActionButtons();
  }
  
  protected void buildActionButtons()
  {
    SelesParameters localSelesParameters = getFilterParameter();
    if ((localSelesParameters == null) || (localSelesParameters.size() == 0)) {
      return;
    }
    int i = 0;
    Iterator localIterator = localSelesParameters.getArgKeys().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      buildActionButton(str, i);
      i++;
    }
  }
  
  protected void handleAction(Integer paramInteger)
  {
    this.d = paramInteger.intValue();
    this.e = readParameterValue((ParameterConfigViewInterface)getConfigView(), this.d);
    if (getConfigView() == null) {
      return;
    }
    SelesParameters localSelesParameters = getFilterParameter();
    if (localSelesParameters.size() <= this.d) {
      return;
    }
    String str = (String)localSelesParameters.getArgKeys().get(this.d);
    if (str == null) {
      return;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(str);
    ((ParameterConfigViewInterface)getConfigView()).setParams(localArrayList, 0);
    setConfigViewShowState(true);
  }
  
  public int getCurrentAction()
  {
    return this.d;
  }
  
  protected void handleConfigCompeleteButton()
  {
    setConfigViewShowState(false);
  }
  
  protected void handleConfigCancel()
  {
    SelesParameters.FilterArg localFilterArg = getFilterArg(this.d);
    localFilterArg.setPrecentValue(this.e);
    requestRender();
    setConfigViewShowState(false);
  }
  
  public void onParameterConfigDataChanged(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt, float paramFloat)
  {
    super.onParameterConfigDataChanged(paramParameterConfigViewInterface, this.d, paramFloat);
  }
  
  public void onParameterConfigRest(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt)
  {
    super.onParameterConfigRest(paramParameterConfigViewInterface, this.d);
  }
  
  public float readParameterValue(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt)
  {
    return super.readParameterValue(paramParameterConfigViewInterface, this.d);
  }
  
  private FilterWrap a()
  {
    FilterOption local1 = new FilterOption()
    {
      public SelesOutInput getFilter()
      {
        TuSDKSkinWhiteningFilter localTuSDKSkinWhiteningFilter = new TuSDKSkinWhiteningFilter();
        localTuSDKSkinWhiteningFilter.setRetouchSize(TuEditSkinFragmentBase.this.mRetouchSize);
        TuEditSkinFragmentBase.a(TuEditSkinFragmentBase.this, localTuSDKSkinWhiteningFilter);
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
  
  protected void asyncLoadImageCompleted(Bitmap paramBitmap)
  {
    super.asyncLoadImageCompleted(paramBitmap);
    if (paramBitmap != null) {
      startImageMarkFaceAnalysis(paramBitmap);
    }
  }
  
  protected void startImageMarkFaceAnalysis(final Bitmap paramBitmap)
  {
    if (this.a == null) {
      this.a = new ImageMarkFaceAnalysis();
    } else {
      this.a.reset();
    }
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        TuEditSkinFragmentBase.b(TuEditSkinFragmentBase.this).analysisWithThumb(paramBitmap, TuEditSkinFragmentBase.a(TuEditSkinFragmentBase.this));
      }
    });
  }
  
  private PointF[] a(ImageMarkFaceResult paramImageMarkFaceResult)
  {
    PointF[] arrayOfPointF = new PointF[5];
    ImageMark5FaceArgument.ImageMarksPoints localImageMarksPoints = null;
    int i = 0;
    ImageMark5FaceArgument.ImageItems localImageItems = (ImageMark5FaceArgument.ImageItems)paramImageMarkFaceResult.items.get(0);
    localImageMarksPoints = localImageItems.marks.eye_left;
    arrayOfPointF[(i++)] = new PointF(localImageMarksPoints.x, localImageMarksPoints.y);
    localImageMarksPoints = localImageItems.marks.eye_right;
    arrayOfPointF[(i++)] = new PointF(localImageMarksPoints.x, localImageMarksPoints.y);
    localImageMarksPoints = localImageItems.marks.nose;
    arrayOfPointF[(i++)] = new PointF(localImageMarksPoints.x, localImageMarksPoints.y);
    localImageMarksPoints = localImageItems.marks.mouth_left;
    arrayOfPointF[(i++)] = new PointF(localImageMarksPoints.x, localImageMarksPoints.y);
    localImageMarksPoints = localImageItems.marks.mouth_right;
    arrayOfPointF[(i++)] = new PointF(localImageMarksPoints.x, localImageMarksPoints.y);
    return arrayOfPointF;
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    loadOrginImage(paramTuSdkResult);
    if ((paramTuSdkResult.filterWrap != null) && (paramTuSdkResult.image != null))
    {
      float f1 = TuSdkSize.create(paramTuSdkResult.image).limitScale();
      paramTuSdkResult.image = BitmapHelper.imageScale(paramTuSdkResult.image, f1);
      FilterWrap localFilterWrap = paramTuSdkResult.filterWrap.clone();
      TuSDKSkinWhiteningFilter localTuSDKSkinWhiteningFilter = (TuSDKSkinWhiteningFilter)localFilterWrap.getFilter();
      if (localTuSDKSkinWhiteningFilter != null)
      {
        FaceAligment localFaceAligment = new FaceAligment();
        localFaceAligment.setOrginMarks(this.c);
        localTuSDKSkinWhiteningFilter.updateFaceFeatures(new FaceAligment[] { localFaceAligment }, 0.0F);
      }
      paramTuSdkResult.image = localFilterWrap.process(paramTuSdkResult.image);
    }
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\filter\TuEditSkinFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */