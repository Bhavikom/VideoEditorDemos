package org.lasque.tusdk.modules.components.paintdraw;

import android.view.ViewGroup;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.impl.components.widget.paintdraw.PaintDrawView;
import org.lasque.tusdk.impl.components.widget.paintdraw.PaintDrawView.PaintDrawViewDelagate;
import org.lasque.tusdk.impl.components.widget.smudge.TuBrushSizeAnimView;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.view.widget.paintdraw.PaintData;

public abstract class TuEditPaintFragmentBase
  extends TuImageResultFragment
  implements PaintDrawView.PaintDrawViewDelagate
{
  public abstract PaintDrawView getPaintDrawView();
  
  public abstract TuBrushSizeAnimView getSizeAnimView();
  
  protected abstract List<PaintData> getColorList();
  
  protected void notifyProcessing(TuSdkResult paramTuSdkResult) {}
  
  protected boolean asyncNotifyProcessing(TuSdkResult paramTuSdkResult)
  {
    return false;
  }
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.editPaintSmudgeFragment);
    if (getSizeAnimView() != null) {
      showView(getSizeAnimView(), false);
    }
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup) {}
  
  public void onRefreshStepStatesWithHistories(int paramInt1, int paramInt2) {}
  
  public void onDestroyView()
  {
    super.onDestroyView();
    if (getPaintDrawView() != null) {
      getPaintDrawView().destroy();
    }
  }
  
  protected void handleBackButton()
  {
    navigatorBarBackAction(null);
  }
  
  public boolean selectPaint(PaintData paramPaintData)
  {
    if (paramPaintData == null) {
      return false;
    }
    List localList = getColorList();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      PaintData localPaintData = (PaintData)localIterator.next();
      if (localPaintData.getData().equals(paramPaintData.getData()))
      {
        getPaintDrawView().setPaintColor(((Integer)paramPaintData.getData()).intValue());
        return true;
      }
    }
    return false;
  }
  
  protected void handleUndoButton()
  {
    if (getPaintDrawView() != null) {
      getPaintDrawView().undo();
    }
  }
  
  protected void handleRedoButton()
  {
    if (getPaintDrawView() != null) {
      getPaintDrawView().redo();
    }
  }
  
  protected void handleOrigianlButtonDown()
  {
    if (getPaintDrawView() != null) {
      getPaintDrawView().showOriginalImage(true);
    }
  }
  
  protected void handleOrigianlButtonUp()
  {
    if (getPaintDrawView() != null) {
      getPaintDrawView().showOriginalImage(false);
    }
  }
  
  protected void startSizeAnimation(int paramInt1, int paramInt2)
  {
    TuBrushSizeAnimView localTuBrushSizeAnimView = getSizeAnimView();
    if (localTuBrushSizeAnimView == null) {
      return;
    }
    localTuBrushSizeAnimView.changeRadius(paramInt1, paramInt2);
    showView(getSizeAnimView(), true);
  }
  
  protected void handleCompleteButton()
  {
    if (getPaintDrawView() == null)
    {
      handleBackButton();
      return;
    }
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditPaintFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    loadOrginImage(paramTuSdkResult);
    paramTuSdkResult.image = getPaintDrawView().getCanvasImage(paramTuSdkResult.image, !isShowResultPreview());
    TLog.d("TuEditEntryFragment editCompleted:%s", new Object[] { paramTuSdkResult });
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\paintdraw\TuEditPaintFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */