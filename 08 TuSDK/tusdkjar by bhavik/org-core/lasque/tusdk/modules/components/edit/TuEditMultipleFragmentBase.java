package org.lasque.tusdk.modules.components.edit;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis;
import org.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener;
import org.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis.ImageAutoColorAnalysisListener;
import org.lasque.tusdk.core.network.analysis.ImageOnlineAnalysis.ImageAnalysisType;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuEditMultipleFragmentBase
  extends TuImageResultFragment
{
  private int a;
  private boolean b;
  private boolean c;
  private List<TuEditActionType> d;
  private ImageAutoColorAnalysis e;
  private int f = 20;
  private final List<File> g = new ArrayList();
  private final List<File> h = new ArrayList();
  private ImageAutoColorAnalysis.ImageAutoColorAnalysisListener i = new ImageAutoColorAnalysis.ImageAutoColorAnalysisListener()
  {
    public void onImageAutoColorAnalysisCompleted(Bitmap paramAnonymousBitmap, ImageOnlineAnalysis.ImageAnalysisType paramAnonymousImageAnalysisType)
    {
      if (paramAnonymousImageAnalysisType == ImageOnlineAnalysis.ImageAnalysisType.Succeed)
      {
        TuEditMultipleFragmentBase.this.setDisplayImage(paramAnonymousBitmap);
      }
      else
      {
        TLog.e("error on auto adjust:%s", new Object[] { paramAnonymousImageAnalysisType });
        TuEditMultipleFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_adjust_color_error"));
      }
    }
  };
  private ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener j = new ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener()
  {
    public void onImageAutoColorAnalysisCopyCompleted(File paramAnonymousFile)
    {
      if ((paramAnonymousFile != null) && (paramAnonymousFile.exists()))
      {
        TuEditMultipleFragmentBase.this.appendHistory(paramAnonymousFile);
        TuEditMultipleFragmentBase.this.hubDismiss();
      }
      else
      {
        TLog.e("error on saving temp file", new Object[0]);
        TuEditMultipleFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_adjust_color_error"));
      }
    }
  };
  
  public abstract void setDisplayImage(Bitmap paramBitmap);
  
  public abstract int[] getRatioTypes();
  
  protected abstract void onRefreshStepStates(int paramInt1, int paramInt2);
  
  public final int getLimitSideSize()
  {
    return this.a;
  }
  
  public final void setLimitSideSize(int paramInt)
  {
    this.a = paramInt;
  }
  
  public final boolean isLimitForScreen()
  {
    return this.b;
  }
  
  public final void setLimitForScreen(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  protected int getLimitHistoryCount()
  {
    return this.f;
  }
  
  protected void setLimitHistoryCount(int paramInt)
  {
    this.f = paramInt;
  }
  
  public boolean isDisableStepsSave()
  {
    return this.c;
  }
  
  public void setDisableStepsSave(boolean paramBoolean)
  {
    this.c = paramBoolean;
  }
  
  public List<TuEditActionType> getModules()
  {
    if ((this.d == null) || (this.d.size() == 0)) {
      this.d = TuEditActionType.multipleActionTypes();
    }
    ArrayList localArrayList = new ArrayList();
    for (int k = 0; k < this.d.size(); k++)
    {
      TuEditActionType localTuEditActionType = (TuEditActionType)this.d.get(k);
      if (((localTuEditActionType != TuEditActionType.TypeSmudge) || (SdkValid.shared.smudgeEnabled())) && ((localTuEditActionType != TuEditActionType.TypeWipeFilter) || (SdkValid.shared.wipeFilterEnabled())) && ((localTuEditActionType != TuEditActionType.TypeHDR) || (SdkValid.shared.hdrFilterEnabled())) && ((localTuEditActionType != TuEditActionType.TypePaint) || (SdkValid.shared.paintEnabled()))) {
        localArrayList.add(localTuEditActionType);
      }
    }
    this.d = localArrayList;
    return this.d;
  }
  
  public void setModules(List<TuEditActionType> paramList)
  {
    this.d = paramList;
  }
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.editMultipleFragment);
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup) {}
  
  public void onDestroyView()
  {
    super.onDestroyView();
    clearAllSteps();
  }
  
  protected final void refreshStepStates()
  {
    onRefreshStepStates(this.g.size(), this.h.size());
  }
  
  protected void clearAllSteps()
  {
    clearSteps(this.g);
    clearSteps(this.h);
  }
  
  protected void clearSteps(List<File> paramList)
  {
    if (paramList == null) {
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      File localFile = (File)localIterator.next();
      TLog.d("clearSteps (%s): %s", new Object[] { Long.valueOf(localFile.length()), localFile });
      FileHelper.delete(localFile);
    }
    paramList.clear();
  }
  
  protected void setHistories(List<File> paramList)
  {
    this.g.clear();
    this.g.addAll(paramList);
  }
  
  protected List<File> getHistories()
  {
    return this.g;
  }
  
  protected void setBrushies(List<File> paramList)
  {
    this.h.clear();
    this.h.addAll(paramList);
  }
  
  protected List<File> getBrushies()
  {
    return this.h;
  }
  
  protected void handleStepPrevButton()
  {
    if (this.g.size() < 2) {
      return;
    }
    this.h.add(this.g.remove(this.g.size() - 1));
    File localFile = getLastSteps();
    refreshStepStates();
    asyncLoadStepImage(localFile);
  }
  
  protected void handleStepNextButton()
  {
    if (this.h.size() == 0) {
      return;
    }
    this.g.add(this.h.remove(this.h.size() - 1));
    File localFile = getLastSteps();
    refreshStepStates();
    asyncLoadStepImage(localFile);
  }
  
  protected void asyncLoadStepImage(File paramFile)
  {
    asyncLoadStepImage(paramFile, true);
  }
  
  protected void asyncLoadStepImage(final File paramFile, boolean paramBoolean)
  {
    if ((paramFile == null) || (!paramFile.exists()) || (!paramFile.isFile())) {
      return;
    }
    if (paramBoolean) {
      hubStatus(TuSdkContext.getString("lsq_edit_loading"));
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        final Bitmap localBitmap = BitmapHelper.getBitmap(paramFile, TuEditMultipleFragmentBase.this.getImageDisplaySize(), true);
        TuEditMultipleFragmentBase.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            TuEditMultipleFragmentBase.this.hubDismiss();
            TuEditMultipleFragmentBase.this.setDisplayImage(localBitmap);
          }
        });
      }
    });
  }
  
  public synchronized File getLastSteps()
  {
    if (this.g.size() == 0) {
      return null;
    }
    return (File)this.g.get(this.g.size() - 1);
  }
  
  public synchronized void appendHistory(File paramFile)
  {
    if ((paramFile == null) || (!paramFile.exists()) || (!paramFile.isFile())) {
      return;
    }
    if (isDisableStepsSave())
    {
      clearSteps(this.g);
      this.g.add(paramFile);
    }
    else
    {
      int k = this.g.size() - getLimitHistoryCount();
      if (k > 1)
      {
        ArrayList localArrayList = new ArrayList();
        for (int m = 1; m <= k; m++) {
          localArrayList.add(this.g.get(m));
        }
        this.g.removeAll(localArrayList);
        clearSteps(localArrayList);
      }
      this.g.add(paramFile);
      clearSteps(this.h);
      refreshStepStates();
    }
  }
  
  private int a()
  {
    int k = 0;
    if (getLimitSideSize() > 0) {
      k = getLimitSideSize();
    } else {
      k = ContextUtils.getScreenSize(getActivity()).maxSide();
    }
    Integer localInteger = Integer.valueOf(SdkValid.shared.maxImageSide());
    if (localInteger.intValue() == 0) {
      return k;
    }
    k = Math.min(k, localInteger.intValue());
    return k;
  }
  
  protected Bitmap asyncLoadImage()
  {
    int k = a();
    TuSdkResult localTuSdkResult = new TuSdkResult();
    localTuSdkResult.image = BitmapHelper.getBitmap(getTempFilePath(), TuSdkSize.create(k, k), true);
    if (localTuSdkResult.image == null) {
      localTuSdkResult.image = BitmapHelper.getBitmap(getImageSqlInfo(), true, k);
    }
    if (localTuSdkResult.image == null)
    {
      localTuSdkResult.image = getImage();
      localTuSdkResult.image = BitmapHelper.imageLimit(localTuSdkResult.image, k);
    }
    if (localTuSdkResult.image == null) {
      return null;
    }
    int[] arrayOfInt = getRatioTypes();
    if ((arrayOfInt == null) || (arrayOfInt.length == 0)) {
      arrayOfInt = RatioType.ratioTypes;
    }
    float f1 = RatioType.firstRatio(arrayOfInt[0]);
    if (f1 > 0.0F) {
      localTuSdkResult.image = BitmapHelper.imageCorp(localTuSdkResult.image, f1);
    }
    localTuSdkResult.imageFile = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", new Object[] { StringHelper.timeStampString() }));
    BitmapHelper.saveBitmap(localTuSdkResult.imageFile, localTuSdkResult.image, getOutputCompress());
    if (localTuSdkResult.imageFile.exists())
    {
      localTuSdkResult.image = BitmapHelper.imageResize(localTuSdkResult.image, getImageDisplaySize(), true);
      this.g.add(localTuSdkResult.imageFile);
    }
    return localTuSdkResult.image;
  }
  
  protected void asyncLoadImageCompleted(Bitmap paramBitmap)
  {
    super.asyncLoadImageCompleted(paramBitmap);
    setDisplayImage(paramBitmap);
  }
  
  protected void handleAutoAdjust()
  {
    if (this.e == null) {
      this.e = new ImageAutoColorAnalysis();
    } else {
      this.e.reset();
    }
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        Bitmap localBitmap = TuEditMultipleFragmentBase.this.getImage();
        TuEditMultipleFragmentBase.a(TuEditMultipleFragmentBase.this, localBitmap);
      }
    });
  }
  
  private void a(Bitmap paramBitmap)
  {
    if (paramBitmap == null) {
      return;
    }
    File localFile = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", new Object[] { StringHelper.timeStampString() }));
    this.e.analysisWithThumb(paramBitmap, getLastSteps(), localFile, this.i, this.j);
  }
  
  protected void handleCompleteButton()
  {
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    localTuSdkResult.imageFile = getLastSteps();
    if ((localTuSdkResult.imageFile == null) || (!localTuSdkResult.imageFile.exists()) || (!localTuSdkResult.imageFile.isFile())) {
      return;
    }
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditMultipleFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    paramTuSdkResult.image = BitmapHelper.getBitmap(paramTuSdkResult.imageFile, true);
    if (getWaterMarkOption() != null) {
      paramTuSdkResult.image = addWaterMarkToImage(paramTuSdkResult.image);
    }
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\edit\TuEditMultipleFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */