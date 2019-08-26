package org.lasque.tusdk.modules.components.smudge;

import android.view.ViewGroup;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.impl.components.widget.smudge.SmudgeView;
import org.lasque.tusdk.impl.components.widget.smudge.SmudgeView.SmudgeViewDelegate;
import org.lasque.tusdk.impl.components.widget.smudge.TuBrushSizeAnimView;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import org.lasque.tusdk.modules.view.widget.smudge.BrushLocalPackage;

public abstract class TuEditSmudgeFragmentBase
  extends TuImageResultFragment
  implements SmudgeView.SmudgeViewDelegate
{
  public abstract SmudgeView getSmudgeView();
  
  public abstract TuBrushSizeAnimView getSizeAnimView();
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.editSmudgeFragment);
    if (getSizeAnimView() != null) {
      showView(getSizeAnimView(), false);
    }
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup) {}
  
  public void onDestroyView()
  {
    super.onDestroyView();
    if (getSmudgeView() != null) {
      getSmudgeView().destroy();
    }
  }
  
  protected void handleBackButton()
  {
    navigatorBarBackAction(null);
  }
  
  public boolean selectBrushCode(String paramString)
  {
    BrushData localBrushData = null;
    if (paramString.equals("Eraser")) {
      localBrushData = BrushLocalPackage.shared().getEeaserBrush();
    } else {
      localBrushData = BrushLocalPackage.shared().getBrushWithCode(paramString);
    }
    if (localBrushData == null) {
      return false;
    }
    if (getSmudgeView() != null) {
      getSmudgeView().setBrush(localBrushData);
    }
    return true;
  }
  
  protected void handleUndoButton()
  {
    if (getSmudgeView() != null) {
      getSmudgeView().undo();
    }
  }
  
  protected void handleRedoButton()
  {
    if (getSmudgeView() != null) {
      getSmudgeView().redo();
    }
  }
  
  protected void handleOrigianlButtonDown()
  {
    if (getSmudgeView() != null) {
      getSmudgeView().showOriginalImage(true);
    }
  }
  
  protected void handleOrigianlButtonUp()
  {
    if (getSmudgeView() != null) {
      getSmudgeView().showOriginalImage(false);
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
  
  public void onRefreshStepStatesWithHistories(int paramInt1, int paramInt2) {}
  
  protected void handleCompleteButton()
  {
    if (getSmudgeView() == null)
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
        TuEditSmudgeFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    loadOrginImage(paramTuSdkResult);
    paramTuSdkResult.image = getSmudgeView().getCanvasImage(paramTuSdkResult.image, !isShowResultPreview());
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\smudge\TuEditSmudgeFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */