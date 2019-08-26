package org.lasque.tusdk.modules.components.edit;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import org.lasque.tusdk.core.utils.image.AlbumHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.impl.activity.TuResultFragment;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.components.ComponentErrorType;

public abstract class TuEditMultiplePlaygroundFragmentBase
  extends TuResultFragment
{
  private int a;
  private boolean b;
  private boolean c;
  private List<TuEditActionType> d;
  private ImageAutoColorAnalysis e;
  private int f = 20;
  private List<TuDraftImageWrap> g;
  private TuDraftImageWrap h;
  private int i = -1;
  private ImageAutoColorAnalysis.ImageAutoColorAnalysisListener j = new ImageAutoColorAnalysis.ImageAutoColorAnalysisListener()
  {
    public void onImageAutoColorAnalysisCompleted(Bitmap paramAnonymousBitmap, ImageOnlineAnalysis.ImageAnalysisType paramAnonymousImageAnalysisType)
    {
      if (paramAnonymousImageAnalysisType != ImageOnlineAnalysis.ImageAnalysisType.Succeed)
      {
        TLog.e("error on auto adjust:%s", new Object[] { paramAnonymousImageAnalysisType });
        TuEditMultiplePlaygroundFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_adjust_color_error"));
      }
    }
  };
  private ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener k = new ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener()
  {
    public void onImageAutoColorAnalysisCopyCompleted(File paramAnonymousFile)
    {
      if ((paramAnonymousFile != null) && (paramAnonymousFile.exists()))
      {
        TuEditMultiplePlaygroundFragmentBase.this.appendHistory(paramAnonymousFile);
        TuEditMultiplePlaygroundFragmentBase.this.hubDismiss();
      }
      else
      {
        TLog.e("error on saving temp file", new Object[0]);
        TuEditMultiplePlaygroundFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_adjust_color_error"));
      }
    }
  };
  
  public abstract int[] getRatioTypes();
  
  protected abstract void onRefreshStepStates(int paramInt1, int paramInt2);
  
  protected abstract boolean prepareSave(int paramInt1, int paramInt2);
  
  protected abstract boolean prepareSaveDraftImage(TuDraftImageWrap paramTuDraftImageWrap);
  
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
    for (int m = 0; m < this.d.size(); m++)
    {
      TuEditActionType localTuEditActionType = (TuEditActionType)this.d.get(m);
      if (((localTuEditActionType != TuEditActionType.TypeSmudge) || (SdkValid.shared.smudgeEnabled())) && ((localTuEditActionType != TuEditActionType.TypeWipeFilter) || (SdkValid.shared.wipeFilterEnabled()))) {
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
    if (getProcessingDraftImageWrap() == null) {
      onRefreshStepStates(0, 0);
    } else {
      onRefreshStepStates(getProcessingDraftImageWrap().getHistoriesSize(), getProcessingDraftImageWrap().getBrushiesSize());
    }
  }
  
  protected void clearAllSteps()
  {
    List localList = getDraftImageList();
    for (int m = 0; m < localList.size(); m++)
    {
      TuDraftImageWrap localTuDraftImageWrap = (TuDraftImageWrap)localList.get(m);
      localTuDraftImageWrap.clearAllSteps();
    }
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
  
  public List<TuDraftImageWrap> getDraftImageList()
  {
    if (this.g == null) {
      this.g = new ArrayList();
    }
    return this.g;
  }
  
  public void setDraftImageList(List<TuDraftImageWrap> paramList)
  {
    this.g = paramList;
  }
  
  public TuDraftImageWrap getProcessingDraftImageWrap()
  {
    return this.h;
  }
  
  protected void setProcessingDraftImageWrap(TuDraftImageWrap paramTuDraftImageWrap)
  {
    this.h = paramTuDraftImageWrap;
  }
  
  public int getProcessingDraftIndex()
  {
    return this.i;
  }
  
  public void setProcessingDraftImageIndex(int paramInt)
  {
    this.i = paramInt;
  }
  
  protected void handleStepPrevButton(ImageView paramImageView, DraftImageLoadListener paramDraftImageLoadListener)
  {
    if (getProcessingDraftImageWrap() == null) {
      return;
    }
    if (getProcessingDraftImageWrap().getHistoriesSize() < 2) {
      return;
    }
    getProcessingDraftImageWrap().getBrushies().add(getProcessingDraftImageWrap().getHistories().remove(getProcessingDraftImageWrap().getHistories().size() - 1));
    refreshStepStates();
    asyncLoadImage(paramImageView, getProcessingDraftImageWrap(), false, paramDraftImageLoadListener);
  }
  
  protected void handleStepNextButton(ImageView paramImageView, DraftImageLoadListener paramDraftImageLoadListener)
  {
    if (getProcessingDraftImageWrap() == null) {
      return;
    }
    if (getProcessingDraftImageWrap().getBrushiesSize() == 0) {
      return;
    }
    List localList1 = getProcessingDraftImageWrap().getBrushies();
    List localList2 = getProcessingDraftImageWrap().getHistories();
    localList2.add(localList1.remove(localList1.size() - 1));
    refreshStepStates();
    asyncLoadImage(paramImageView, getProcessingDraftImageWrap(), false, paramDraftImageLoadListener);
  }
  
  protected Bitmap loadDraftImage(TuDraftImageWrap paramTuDraftImageWrap)
  {
    if (paramTuDraftImageWrap == null) {
      return null;
    }
    int m = a();
    Bitmap localBitmap = paramTuDraftImageWrap.getImage(m);
    if (localBitmap == null) {
      return null;
    }
    if (paramTuDraftImageWrap.getLastSteps() == null)
    {
      int[] arrayOfInt = getRatioTypes();
      if ((arrayOfInt == null) || (arrayOfInt.length == 0)) {
        arrayOfInt = RatioType.ratioTypes;
      }
      float f1 = RatioType.firstRatio(arrayOfInt[0]);
      if (f1 > 0.0F) {
        localBitmap = BitmapHelper.imageCorp(localBitmap, f1);
      }
      File localFile = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", new Object[] { StringHelper.timeStampString() }));
      BitmapHelper.saveBitmap(localFile, localBitmap, getOutputCompress());
      if (localFile.exists())
      {
        localBitmap = BitmapHelper.imageResize(localBitmap, getImageDisplaySize(), true);
        paramTuDraftImageWrap.getHistories().add(localFile);
      }
    }
    return localBitmap;
  }
  
  public TuSdkSize getImageDisplaySize()
  {
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(getActivity());
    if (localTuSdkSize != null)
    {
      localTuSdkSize.width = ((int)Math.floor(localTuSdkSize.width * 0.75D));
      localTuSdkSize.height = ((int)Math.floor(localTuSdkSize.height * 0.75D));
    }
    return localTuSdkSize;
  }
  
  protected void asyncLoadImage(final ImageView paramImageView, final File paramFile, boolean paramBoolean, final DraftImageLoadListener paramDraftImageLoadListener)
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
        final Bitmap localBitmap = BitmapHelper.getBitmap(paramFile, TuEditMultiplePlaygroundFragmentBase.this.getImageDisplaySize(), true);
        TuEditMultiplePlaygroundFragmentBase.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            if (TuEditMultiplePlaygroundFragmentBase.1.this.b != null) {
              TuEditMultiplePlaygroundFragmentBase.1.this.b.setImageBitmap(localBitmap);
            }
            if (TuEditMultiplePlaygroundFragmentBase.1.this.c != null) {
              TuEditMultiplePlaygroundFragmentBase.1.this.c.onLoadingComplete(TuEditMultiplePlaygroundFragmentBase.1.this.b, localBitmap);
            }
            TuEditMultiplePlaygroundFragmentBase.this.hubDismiss();
          }
        });
      }
    });
  }
  
  protected void asyncLoadImage(final ImageView paramImageView, final TuDraftImageWrap paramTuDraftImageWrap, boolean paramBoolean, final DraftImageLoadListener paramDraftImageLoadListener)
  {
    if (paramTuDraftImageWrap == null) {
      return;
    }
    if (paramTuDraftImageWrap.getLastSteps() != null)
    {
      asyncLoadImage(paramImageView, paramTuDraftImageWrap.getLastSteps(), paramBoolean, paramDraftImageLoadListener);
      return;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        final Bitmap localBitmap = TuEditMultiplePlaygroundFragmentBase.this.loadDraftImage(paramTuDraftImageWrap);
        TuEditMultiplePlaygroundFragmentBase.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            if (TuEditMultiplePlaygroundFragmentBase.2.this.b != null) {
              TuEditMultiplePlaygroundFragmentBase.2.this.b.setImageBitmap(localBitmap);
            }
            if (TuEditMultiplePlaygroundFragmentBase.2.this.c != null) {
              TuEditMultiplePlaygroundFragmentBase.2.this.c.onLoadingComplete(TuEditMultiplePlaygroundFragmentBase.2.this.b, localBitmap);
            }
          }
        });
      }
    });
  }
  
  public synchronized File getLastSteps()
  {
    if ((getProcessingDraftImageWrap() == null) || (getProcessingDraftImageWrap().getHistoriesSize() == 0)) {
      return null;
    }
    return getProcessingDraftImageWrap().getLastSteps();
  }
  
  public synchronized void appendHistory(File paramFile)
  {
    if (getProcessingDraftImageWrap() == null) {
      return;
    }
    if ((paramFile == null) || (!paramFile.exists()) || (!paramFile.isFile())) {
      return;
    }
    if (isDisableStepsSave())
    {
      getProcessingDraftImageWrap().clearSteps(getProcessingDraftImageWrap().getHistories());
      getProcessingDraftImageWrap().getHistories().add(paramFile);
    }
    else
    {
      int m = getProcessingDraftImageWrap().getHistoriesSize() - getLimitHistoryCount();
      if (m > 1)
      {
        ArrayList localArrayList = new ArrayList();
        for (int n = 1; n <= m; n++) {
          localArrayList.add(getProcessingDraftImageWrap().getHistories().get(n));
        }
        getProcessingDraftImageWrap().getHistories().removeAll(localArrayList);
        getProcessingDraftImageWrap().clearSteps(localArrayList);
        clearSteps(localArrayList);
      }
      getProcessingDraftImageWrap().getHistories().add(paramFile);
      getProcessingDraftImageWrap().clearSteps(getProcessingDraftImageWrap().getBrushies());
    }
    refreshStepStates();
  }
  
  private int a()
  {
    int m = 0;
    if (getLimitSideSize() > 0)
    {
      m = getLimitSideSize();
    }
    else
    {
      localObject = ContextUtils.getScreenSize(getActivity());
      if (localObject != null) {
        m = ContextUtils.getScreenSize(getActivity()).maxSide();
      }
    }
    Object localObject = Integer.valueOf(SdkValid.shared.maxImageSide());
    if (((Integer)localObject).intValue() == 0) {
      return m;
    }
    m = Math.min(m, ((Integer)localObject).intValue());
    return m;
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
        Bitmap localBitmap = TuEditMultiplePlaygroundFragmentBase.this.loadDraftImage(TuEditMultiplePlaygroundFragmentBase.this.getProcessingDraftImageWrap());
        TuEditMultiplePlaygroundFragmentBase.a(TuEditMultiplePlaygroundFragmentBase.this, localBitmap);
      }
    });
  }
  
  private void a(Bitmap paramBitmap)
  {
    if (paramBitmap == null) {
      return;
    }
    File localFile = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", new Object[] { StringHelper.timeStampString() }));
    this.e.analysisWithThumb(paramBitmap, getLastSteps(), localFile, this.j, this.k);
  }
  
  protected void handleCompleteButton()
  {
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    ArrayList localArrayList = new ArrayList(getDraftImageList().size());
    localTuSdkResult.images = localArrayList;
    if (!prepareSave(getDraftImageList().size(), changedCount()))
    {
      List localList = getDraftImageList();
      for (int m = 0; m < localList.size(); m++)
      {
        TuDraftImageWrap localTuDraftImageWrap = (TuDraftImageWrap)localList.get(m);
        if (localTuDraftImageWrap.getImageSqlInfo() != null) {
          localTuSdkResult.images.add(localTuDraftImageWrap.getImageSqlInfo());
        }
      }
      notifyProcessing(localTuSdkResult);
      return;
    }
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditMultiplePlaygroundFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected int changedCount()
  {
    int m = 0;
    List localList = getDraftImageList();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      TuDraftImageWrap localTuDraftImageWrap = (TuDraftImageWrap)localIterator.next();
      if (localTuDraftImageWrap.isChanged()) {
        m++;
      }
    }
    return m;
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    paramTuSdkResult.image = BitmapHelper.getBitmap(paramTuSdkResult.imageFile, true);
    if (getWaterMarkOption() != null) {
      paramTuSdkResult.image = addWaterMarkToImage(paramTuSdkResult.image);
    }
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
  
  public boolean isSaveToAlbum()
  {
    return (super.isSaveToAlbum()) || (!isSaveToTemp());
  }
  
  protected void showProgress(int paramInt1, int paramInt2)
  {
    hubStatus(TuSdkContext.getString("lsq_edit_processing_index", new Object[] { Integer.valueOf(paramInt2), Integer.valueOf(paramInt1) }));
  }
  
  protected void saveToAlbum(TuSdkResult paramTuSdkResult)
  {
    hubStatus(TuSdkContext.getString("lsq_save_saveToAlbum"));
    int m = getDraftImageList().size();
    for (int n = 0; n < getDraftImageList().size(); n++)
    {
      showProgress(m, n);
      TuDraftImageWrap localTuDraftImageWrap = (TuDraftImageWrap)getDraftImageList().get(n);
      localTuDraftImageWrap.setImage(null);
      Object localObject;
      if (!prepareSaveDraftImage(localTuDraftImageWrap))
      {
        localObject = localTuDraftImageWrap.getOutputImageSqlInfo();
        if (localObject != null) {
          paramTuSdkResult.images.add(localTuDraftImageWrap.getImageSqlInfo());
        }
      }
      else
      {
        localObject = null;
        if ((localObject = canSaveFile()) != null)
        {
          notifyError(paramTuSdkResult, (ComponentErrorType)localObject);
        }
        else
        {
          Bitmap localBitmap = localTuDraftImageWrap.getImage();
          if (localBitmap != null)
          {
            if (getWaterMarkOption() != null) {
              localBitmap = addWaterMarkToImage(localBitmap);
            }
            File localFile = null;
            if (StringHelper.isNotBlank(getSaveToAlbumName())) {
              localFile = AlbumHelper.getAlbumFile(getSaveToAlbumName());
            }
            ImageSqlInfo localImageSqlInfo = ImageSqlHelper.saveJpgToAblum(getActivity(), localBitmap, getOutputCompress(), localFile);
            localFile = new File(localImageSqlInfo.path);
            if ((localBitmap != null) && (!localBitmap.isRecycled()))
            {
              localBitmap.recycle();
              localBitmap = null;
            }
            if ((localImageSqlInfo != null) && (localFile.exists()))
            {
              ImageSqlHelper.notifyRefreshAblum(getActivity(), localImageSqlInfo);
              paramTuSdkResult.images.add(localImageSqlInfo);
            }
          }
        }
      }
    }
    if (paramTuSdkResult.images.size() > 0) {
      hubSuccess(TuSdkContext.getString("lsq_save_saveToTemp_completed"));
    }
  }
  
  protected void saveToTemp(TuSdkResult paramTuSdkResult)
  {
    hubStatus(TuSdkContext.getString("lsq_save_saveToTemp"));
    int m = getDraftImageList().size();
    for (int n = 0; n < getDraftImageList().size(); n++)
    {
      showProgress(m, n);
      TuDraftImageWrap localTuDraftImageWrap = (TuDraftImageWrap)getDraftImageList().get(n);
      localTuDraftImageWrap.setImage(null);
      Object localObject;
      if (!prepareSaveDraftImage(localTuDraftImageWrap))
      {
        localObject = localTuDraftImageWrap.getOutputImageSqlInfo();
        if (localObject != null) {
          paramTuSdkResult.images.add(localTuDraftImageWrap.getImageSqlInfo());
        }
      }
      else
      {
        localObject = null;
        if ((localObject = canSaveFile()) != null)
        {
          notifyError(paramTuSdkResult, (ComponentErrorType)localObject);
        }
        else
        {
          Bitmap localBitmap = localTuDraftImageWrap.getImage();
          if (localBitmap != null)
          {
            if (getWaterMarkOption() != null) {
              localBitmap = addWaterMarkToImage(localBitmap);
            }
            File localFile = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", new Object[] { StringHelper.timeStampString() }));
            BitmapHelper.saveBitmap(localFile, localBitmap, getOutputCompress());
            if ((localBitmap != null) && (!localBitmap.isRecycled()))
            {
              localBitmap.recycle();
              localBitmap = null;
            }
            if ((localFile != null) && (localFile.exists()))
            {
              ImageSqlInfo localImageSqlInfo = new ImageSqlInfo();
              localImageSqlInfo.path = localFile.getAbsolutePath();
              paramTuSdkResult.images.add(localImageSqlInfo);
            }
          }
        }
      }
    }
    if (paramTuSdkResult.images.size() > 0) {
      hubSuccess(TuSdkContext.getString("lsq_save_saveToTemp_completed"));
    }
  }
  
  protected void asyncProcessingIfNeedSave(TuSdkResult paramTuSdkResult)
  {
    if (ThreadHelper.isMainThread())
    {
      notifyProcessing(paramTuSdkResult);
      return;
    }
    if (!asyncNotifyProcessing(paramTuSdkResult)) {
      if (isSaveToTemp()) {
        saveToTemp(paramTuSdkResult);
      } else if (isSaveToAlbum()) {
        saveToAlbum(paramTuSdkResult);
      }
    }
    clearAllSteps();
    backUIThreadNotifyProcessing(paramTuSdkResult);
    StatisticsManger.appendComponent(ComponentActType.editPhotoAction);
  }
  
  protected static abstract interface DraftImageLoadListener
  {
    public abstract void onLoadingComplete(View paramView, Bitmap paramBitmap);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\edit\TuEditMultiplePlaygroundFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */