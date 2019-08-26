// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.paintdraw;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.TuSdkContext;
import java.util.Iterator;
//import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.modules.view.widget.paintdraw.PaintData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuImageResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.paintdraw.PaintDrawView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.smudge.TuBrushSizeAnimView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.paintdraw.PaintData;

import java.util.List;
//import org.lasque.tusdk.impl.components.widget.smudge.TuBrushSizeAnimView;
//import org.lasque.tusdk.impl.components.widget.paintdraw.PaintDrawView;
//import org.lasque.tusdk.impl.activity.TuImageResultFragment;

public abstract class TuEditPaintFragmentBase extends TuImageResultFragment implements PaintDrawView.PaintDrawViewDelagate
{
    public abstract PaintDrawView getPaintDrawView();
    
    public abstract TuBrushSizeAnimView getSizeAnimView();
    
    protected abstract List<PaintData> getColorList();
    
    @Override
    protected void notifyProcessing(final TuSdkResult tuSdkResult) {
    }
    
    @Override
    protected boolean asyncNotifyProcessing(final TuSdkResult tuSdkResult) {
        return false;
    }
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        StatisticsManger.appendComponent(ComponentActType.editPaintSmudgeFragment);
        if (this.getSizeAnimView() != null) {
            this.showView(this.getSizeAnimView(), false);
        }
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
    }
    
    @Override
    public void onRefreshStepStatesWithHistories(final int n, final int n2) {
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.getPaintDrawView() != null) {
            this.getPaintDrawView().destroy();
        }
    }
    
    protected void handleBackButton() {
        this.navigatorBarBackAction(null);
    }
    
    public boolean selectPaint(final PaintData paintData) {
        if (paintData == null) {
            return false;
        }
        final Iterator<PaintData> iterator = this.getColorList().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getData().equals(paintData.getData())) {
                this.getPaintDrawView().setPaintColor((int)paintData.getData());
                return true;
            }
        }
        return false;
    }
    
    protected void handleUndoButton() {
        if (this.getPaintDrawView() != null) {
            this.getPaintDrawView().undo();
        }
    }
    
    protected void handleRedoButton() {
        if (this.getPaintDrawView() != null) {
            this.getPaintDrawView().redo();
        }
    }
    
    protected void handleOrigianlButtonDown() {
        if (this.getPaintDrawView() != null) {
            this.getPaintDrawView().showOriginalImage(true);
        }
    }
    
    protected void handleOrigianlButtonUp() {
        if (this.getPaintDrawView() != null) {
            this.getPaintDrawView().showOriginalImage(false);
        }
    }
    
    protected void startSizeAnimation(final int n, final int n2) {
        final TuBrushSizeAnimView sizeAnimView = this.getSizeAnimView();
        if (sizeAnimView == null) {
            return;
        }
        sizeAnimView.changeRadius(n, n2);
        this.showView(this.getSizeAnimView(), true);
    }
    
    protected void handleCompleteButton() {
        if (this.getPaintDrawView() == null) {
            this.handleBackButton();
            return;
        }
        final TuSdkResult tuSdkResult = new TuSdkResult();
        this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                TuEditPaintFragmentBase.this.asyncEditWithResult(tuSdkResult);
            }
        }).start();
    }
    
    protected void asyncEditWithResult(final TuSdkResult tuSdkResult) {
        this.loadOrginImage(tuSdkResult);
        tuSdkResult.image = this.getPaintDrawView().getCanvasImage(tuSdkResult.image, !this.isShowResultPreview());
        TLog.d("TuEditEntryFragment editCompleted:%s", tuSdkResult);
        this.asyncProcessingIfNeedSave(tuSdkResult);
    }
}
