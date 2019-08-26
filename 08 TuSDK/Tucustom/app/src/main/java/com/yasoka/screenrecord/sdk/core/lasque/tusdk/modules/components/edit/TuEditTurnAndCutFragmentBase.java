// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.edit;

//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.graphics.Rect;
import android.widget.ImageView;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.modules.components.ComponentErrorType;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
import android.content.Context;
//import org.lasque.tusdk.core.view.TuSdkTouchImageView;
//import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import android.widget.RelativeLayout;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkTouchImageView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkTouchImageViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuMaskRegionView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuImageResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentErrorType;
//import org.lasque.tusdk.core.view.TuSdkTouchImageViewInterface;
//import org.lasque.tusdk.impl.activity.TuImageResultFragment;

public abstract class TuEditTurnAndCutFragmentBase extends TuImageResultFragment {
    private String a;
    private TuSdkTouchImageViewInterface b;
    protected View.OnLayoutChangeListener mRegionLayoutChangeListener = new View.OnLayoutChangeListener() {
        public void onLayoutChange(View var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
            if (var2 != var6 || var3 != var7 || var4 != var8 || var5 != var9) {
                TuEditTurnAndCutFragmentBase.this.onRegionLayoutChanged(TuEditTurnAndCutFragmentBase.this.getCutRegionView());
            }

        }
    };

    public TuEditTurnAndCutFragmentBase() {
    }

    public abstract TuSdkSize getCutSize();

    public abstract RelativeLayout getImageWrapView();

    public abstract TuMaskRegionView getCutRegionView();

    public String getSelectedFilterCode() {
        return this.a;
    }

    public <T extends View & TuSdkTouchImageViewInterface> T getImageView() {
        if (this.b == null) {
            RelativeLayout var1 = this.getImageWrapView();
            if (var1 != null) {
                this.b = new TuSdkTouchImageView(this.getActivity());
                var1.addView((View)this.b, new RelativeLayout.LayoutParams(-1, -1));
                this.b.setInvalidateTargetView(this.getCutRegionView());
            }
        }

        return (T) this.b;
    }

    protected void onRegionLayoutChanged(TuMaskRegionView var1) {
        if (var1 != null && this.getImageView() != null) {
            ((TuSdkTouchImageViewInterface)this.getImageView()).changeRegionRatio(var1.getRegionRect(), this.getCutSize() == null ? 0.0F : var1.getRegionRatio());
        }
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
            if (this.getCutSize() != null && this.getCutRegionView() != null) {
                this.getCutRegionView().setRegionSize(this.getCutSize());
                ((TuSdkTouchImageViewInterface)this.getImageView()).setScaleType(ImageView.ScaleType.CENTER_CROP);
                Rect var2 = this.getCutRegionView().getRegionRect();
                TuSdkViewHelper.setViewRect(this.getImageView(), var2);
                ((TuSdkTouchImageViewInterface)this.getImageView()).setZoom(1.0F);
            } else {
                ((TuSdkTouchImageViewInterface)this.getImageView()).setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        }
    }

    protected boolean handleSwitchFilter(final String var1) {
        if (this.getImageView() != null && !((TuSdkTouchImageViewInterface)this.getImageView()).isInAnimation()) {
            final Bitmap var2 = this.getImage();
            if (var2 != null && var1 != null && !var1.equalsIgnoreCase(this.a)) {
                this.a = var1;
                this.hubStatus(TuSdkContext.getString("lsq_edit_filter_processing"));
                ThreadHelper.runThread(new Runnable() {
                    public void run() {
                        TuEditTurnAndCutFragmentBase.this.a(var1, var2);
                    }
                });
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void a(String var1, Bitmap var2) {
        final Bitmap var3 = this.a(var2, var1, ((TuSdkTouchImageViewInterface)this.getImageView()).getImageOrientation());
        ThreadHelper.post(new Runnable() {
            public void run() {
                TuEditTurnAndCutFragmentBase.this.processedFilter(var3);
            }
        });
    }

    private Bitmap a(Bitmap var1, String var2, ImageOrientation var3) {
        FilterWrap var4 = FilterLocalPackage.shared().getFilterWrap(var2);
        if (var4 == null) {
            return var1;
        } else {
            var4.setFilterParameter((SelesParameters)null);
            return var4.process(var1, var3, 0.0F);
        }
    }

    protected void processedFilter(Bitmap var1) {
        if (this.getImageView() != null) {
            ((TuSdkTouchImageViewInterface)this.getImageView()).setImageBitmapWithAnim(var1);
            this.hubDismiss();
        }
    }

    protected void handleCompleteButton() {
        if (this.getImageView() != null && !((TuSdkTouchImageViewInterface)this.getImageView()).isInAnimation()) {
            final TuSdkResult var1 = new TuSdkResult();
            var1.cutRect = ((TuSdkTouchImageViewInterface)this.getImageView()).getZoomedRect();
            var1.imageOrientation = ((TuSdkTouchImageViewInterface)this.getImageView()).getImageOrientation();
            var1.outputSize = this.getCutSize();
            var1.filterCode = this.getSelectedFilterCode();
            this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
            (new Thread(new Runnable() {
                public void run() {
                    TuEditTurnAndCutFragmentBase.this.asyncEditWithResult(var1);
                }
            })).start();
        }
    }

    protected void asyncEditWithResult(TuSdkResult var1) {
        this.loadOrginImage(var1);
        if (var1.outputSize != null) {
            var1.image = BitmapHelper.imageCorp(var1.image, var1.cutRect, var1.outputSize, var1.imageOrientation);
        } else {
            var1.image = BitmapHelper.imageRotaing(var1.image, var1.imageOrientation);
        }

        if (var1.filterCode != null) {
            var1.image = this.a(var1.image, var1.filterCode, ImageOrientation.Up);
        }

        this.asyncProcessingIfNeedSave(var1);
    }
}
