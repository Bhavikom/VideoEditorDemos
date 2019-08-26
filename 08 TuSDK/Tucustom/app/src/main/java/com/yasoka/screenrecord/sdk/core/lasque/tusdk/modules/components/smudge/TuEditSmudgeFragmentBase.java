// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.smudge;

//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushLocalPackage;
//import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuImageResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.smudge.SmudgeView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.smudge.TuBrushSizeAnimView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushLocalPackage;
//import org.lasque.tusdk.impl.components.widget.smudge.TuBrushSizeAnimView;
//import org.lasque.tusdk.impl.components.widget.smudge.SmudgeView;
//import org.lasque.tusdk.impl.activity.TuImageResultFragment;

public abstract class TuEditSmudgeFragmentBase extends TuImageResultFragment implements SmudgeView.SmudgeViewDelegate
{
    public abstract SmudgeView getSmudgeView();
    
    public abstract TuBrushSizeAnimView getSizeAnimView();
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        StatisticsManger.appendComponent(ComponentActType.editSmudgeFragment);
        if (this.getSizeAnimView() != null) {
            this.showView(this.getSizeAnimView(), false);
        }
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.getSmudgeView() != null) {
            this.getSmudgeView().destroy();
        }
    }
    
    protected void handleBackButton() {
        this.navigatorBarBackAction(null);
    }
    
    public boolean selectBrushCode(final String s) {
        BrushData brush;
        if (s.equals("Eraser")) {
            brush = BrushLocalPackage.shared().getEeaserBrush();
        }
        else {
            brush = BrushLocalPackage.shared().getBrushWithCode(s);
        }
        if (brush == null) {
            return false;
        }
        if (this.getSmudgeView() != null) {
            this.getSmudgeView().setBrush(brush);
        }
        return true;
    }
    
    protected void handleUndoButton() {
        if (this.getSmudgeView() != null) {
            this.getSmudgeView().undo();
        }
    }
    
    protected void handleRedoButton() {
        if (this.getSmudgeView() != null) {
            this.getSmudgeView().redo();
        }
    }
    
    protected void handleOrigianlButtonDown() {
        if (this.getSmudgeView() != null) {
            this.getSmudgeView().showOriginalImage(true);
        }
    }
    
    protected void handleOrigianlButtonUp() {
        if (this.getSmudgeView() != null) {
            this.getSmudgeView().showOriginalImage(false);
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
    
    @Override
    public void onRefreshStepStatesWithHistories(final int n, final int n2) {
    }
    
    protected void handleCompleteButton() {
        if (this.getSmudgeView() == null) {
            this.handleBackButton();
            return;
        }
        final TuSdkResult tuSdkResult = new TuSdkResult();
        this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                TuEditSmudgeFragmentBase.this.asyncEditWithResult(tuSdkResult);
            }
        }).start();
    }
    
    protected void asyncEditWithResult(final TuSdkResult tuSdkResult) {
        this.loadOrginImage(tuSdkResult);
        tuSdkResult.image = this.getSmudgeView().getCanvasImage(tuSdkResult.image, !this.isShowResultPreview());
        this.asyncProcessingIfNeedSave(tuSdkResult);
    }
}
