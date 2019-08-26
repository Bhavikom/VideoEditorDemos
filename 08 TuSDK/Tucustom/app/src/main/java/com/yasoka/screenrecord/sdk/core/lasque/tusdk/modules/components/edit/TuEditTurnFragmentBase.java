// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.edit;

//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.TuSdkContext;
import android.view.View;
import android.widget.ImageView;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.modules.components.ComponentErrorType;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
import android.content.Context;
//import org.lasque.tusdk.core.view.TuSdkTouchImageView;
import android.widget.RelativeLayout;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkTouchImageView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkTouchImageViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuImageResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentErrorType;
//import org.lasque.tusdk.core.view.TuSdkTouchImageViewInterface;
//import org.lasque.tusdk.impl.activity.TuImageResultFragment;

public abstract class TuEditTurnFragmentBase extends TuImageResultFragment {
    private TuSdkTouchImageViewInterface a;

    public TuEditTurnFragmentBase() {
    }

    public abstract RelativeLayout getImageWrapView();

    public <T extends View & TuSdkTouchImageViewInterface> T getImageView() {
        if (this.a == null) {
            RelativeLayout var1 = this.getImageWrapView();
            if (var1 != null) {
                this.a = new TuSdkTouchImageView(this.getActivity());
                var1.addView((View)this.a, new RelativeLayout.LayoutParams(-1, -1));
                this.a.setInvalidateTargetView(this.getImageWrapView());
            }
        }

        return (T) this.a;
    }

    protected void loadView(ViewGroup var1) {
        StatisticsManger.appendComponent(ComponentActType.editEntryFragment);
    }

    protected void viewDidLoad(ViewGroup var1) {
        if (this.getImage() == null) {
            this.notifyError((TuSdkResult)null, ComponentErrorType.TypeInputImageEmpty);
            TLog.e("Can not find input image.", new Object[0]);
        } else if (this.getImageView() != null) {
            ((TuSdkTouchImageViewInterface)this.getImageView()).setImageBitmap(this.getImage());
            ((TuSdkTouchImageViewInterface)this.getImageView()).setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    protected void handleCompleteButton() {
        if (this.getImageView() != null && !((TuSdkTouchImageViewInterface)this.getImageView()).isInAnimation()) {
            final TuSdkResult var1 = new TuSdkResult();
            var1.imageOrientation = ((TuSdkTouchImageViewInterface)this.getImageView()).getImageOrientation();
            this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
            (new Thread(new Runnable() {
                public void run() {
                    TuEditTurnFragmentBase.this.asyncEditWithResult(var1);
                }
            })).start();
        }
    }

    protected void asyncEditWithResult(TuSdkResult var1) {
        this.loadOrginImage(var1);
        var1.image = BitmapHelper.imageRotaing(var1.image, var1.imageOrientation);
        this.asyncProcessingIfNeedSave(var1);
    }

    protected void changeImageAnimation(TuSdkTouchImageViewInterface.LsqImageChangeType var1) {
        if (this.getImageView() != null && !((TuSdkTouchImageViewInterface)this.getImageView()).isInAnimation()) {
            ((TuSdkTouchImageViewInterface)this.getImageView()).changeImageAnimation(var1);
        }
    }
}
