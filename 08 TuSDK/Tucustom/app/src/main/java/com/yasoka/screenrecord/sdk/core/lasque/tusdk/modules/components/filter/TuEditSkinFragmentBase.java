// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.filter;

//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import java.util.ArrayList;
//import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import java.util.Iterator;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
import android.view.View;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.network.analysis.ImageOnlineAnalysis;
//import org.lasque.tusdk.core.network.analysis.ImageMarkFaceResult;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageMarkFaceAnalysis;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageMarkFaceResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageOnlineAnalysis;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinWhiteningFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuFilterResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinWhiteningFilter;
//import org.lasque.tusdk.core.network.analysis.ImageMarkFaceAnalysis;
//import org.lasque.tusdk.impl.activity.TuFilterResultFragment;

public abstract class TuEditSkinFragmentBase extends TuFilterResultFragment
{
    private ImageMarkFaceAnalysis a;
    private TuSDKSkinWhiteningFilter b;
    private PointF[] c;
    protected float mRetouchSize;
    private int d;
    private float e;
    private ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener f;
    
    public TuEditSkinFragmentBase() {
        this.mRetouchSize = 1.0f;
        this.d = -1;
        this.f = new ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener() {
            @Override
            public void onImageFaceAnalysisCompleted(final ImageMarkFaceResult imageMarkFaceResult, final ImageOnlineAnalysis.ImageAnalysisType imageAnalysisType) {
                if (imageAnalysisType == ImageOnlineAnalysis.ImageAnalysisType.Succeed) {
                    if (imageMarkFaceResult == null || imageMarkFaceResult.count <= 0) {
                        TuEditSkinFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_error_no_face"));
                    }
                    else {
                        if (imageMarkFaceResult.count == 1) {
                            TuEditSkinFragmentBase.this.c = TuEditSkinFragmentBase.this.a(imageMarkFaceResult);
                            final FaceAligment faceAligment = new FaceAligment();
                            faceAligment.setOrginMarks(TuEditSkinFragmentBase.this.c);
                            TuEditSkinFragmentBase.this.b.updateFaceFeatures(new FaceAligment[] { faceAligment }, 0.0f);
                            requestRender();
                            TuEditSkinFragmentBase.this.hubDismiss();
                            TuEditSkinFragmentBase.this.onFaceDetectionResult(true);
                            return;
                        }
                        TuEditSkinFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_error_multi_face"));
                    }
                }
                else if (imageAnalysisType == ImageOnlineAnalysis.ImageAnalysisType.NoAccessRight) {
                    TLog.e("You are not allowed to use the face mark api, please see http://tusdk.com", new Object[0]);
                    TuEditSkinFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_error_no_face_access"));
                }
                else {
                    TLog.e("error on face mark :%s", imageAnalysisType);
                    TuEditSkinFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_skin_error"));
                }
                TuEditSkinFragmentBase.this.onFaceDetectionResult(false);
            }
        };
    }
    
    public void setRetouchSize(final float mRetouchSize) {
        this.mRetouchSize = mRetouchSize;
    }
    
    protected abstract void setConfigViewShowState(final boolean p0);
    
    protected abstract View buildActionButton(final String p0, final int p1);
    
    protected abstract void onFaceDetectionResult(final boolean p0);
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        StatisticsManger.appendComponent(ComponentActType.editSkinFragment);
        this.setFilterWrap(this.a());
        super.loadView(viewGroup);
        this.buildActionButtons();
    }
    
    protected void buildActionButtons() {
        final SelesParameters filterParameter = this.getFilterParameter();
        if (filterParameter == null || filterParameter.size() == 0) {
            return;
        }
        int n = 0;
        final Iterator<String> iterator = filterParameter.getArgKeys().iterator();
        while (iterator.hasNext()) {
            this.buildActionButton(iterator.next(), n);
            ++n;
        }
    }
    
    protected void handleAction(final Integer var1) {
        this.d = var1;
        this.e = this.readParameterValue((ParameterConfigViewInterface)this.getConfigView(), this.d);
        if (this.getConfigView() != null) {
            SelesParameters var2 = this.getFilterParameter();
            if (var2.size() > this.d) {
                String var3 = (String)var2.getArgKeys().get(this.d);
                if (var3 != null) {
                    ArrayList var4 = new ArrayList();
                    var4.add(var3);
                    ((ParameterConfigViewInterface)this.getConfigView()).setParams(var4, 0);
                    this.setConfigViewShowState(true);
                }
            }
        }
    }
    
    public int getCurrentAction() {
        return this.d;
    }
    
    protected void handleConfigCompeleteButton() {
        this.setConfigViewShowState(false);
    }
    
    protected void handleConfigCancel() {
        this.getFilterArg(this.d).setPrecentValue(this.e);
        this.requestRender();
        this.setConfigViewShowState(false);
    }
    
    @Override
    public void onParameterConfigDataChanged(final ParameterConfigViewInterface parameterConfigViewInterface, final int n, final float n2) {
        super.onParameterConfigDataChanged(parameterConfigViewInterface, this.d, n2);
    }
    
    @Override
    public void onParameterConfigRest(final ParameterConfigViewInterface parameterConfigViewInterface, final int n) {
        super.onParameterConfigRest(parameterConfigViewInterface, this.d);
    }
    
    @Override
    public float readParameterValue(final ParameterConfigViewInterface parameterConfigViewInterface, final int n) {
        return super.readParameterValue(parameterConfigViewInterface, this.d);
    }
    
    private FilterWrap a() {
        final FilterOption filterOption = new FilterOption() {
            @Override
            public SelesOutInput getFilter() {
                final TuSDKSkinWhiteningFilter tuSDKSkinWhiteningFilter = new TuSDKSkinWhiteningFilter();
                tuSDKSkinWhiteningFilter.setRetouchSize(TuEditSkinFragmentBase.this.mRetouchSize);
                TuEditSkinFragmentBase.this.b = tuSDKSkinWhiteningFilter;
                return tuSDKSkinWhiteningFilter;
            }
        };
        filterOption.id = Long.MAX_VALUE;
        filterOption.canDefinition = true;
        filterOption.isInternal = true;
        final ArrayList<String> internalTextures = new ArrayList<String>();
        internalTextures.add("f8a6ed3ec939d6941c94a272aff1791b");
        filterOption.internalTextures = internalTextures;
        return FilterWrap.creat(filterOption);
    }
    
    @Override
    protected void asyncLoadImageCompleted(final Bitmap bitmap) {
        super.asyncLoadImageCompleted(bitmap);
        if (bitmap != null) {
            this.startImageMarkFaceAnalysis(bitmap);
        }
    }
    
    protected void startImageMarkFaceAnalysis(final Bitmap bitmap) {
        if (this.a == null) {
            this.a = new ImageMarkFaceAnalysis();
        }
        else {
            this.a.reset();
        }
        this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                TuEditSkinFragmentBase.this.a.analysisWithThumb(bitmap, TuEditSkinFragmentBase.this.f);
            }
        });
    }
    
    private PointF[] a(final ImageMarkFaceResult imageMarkFaceResult) {
        final PointF[] array = new PointF[5];
        int n = 0;
        final ImageMark5FaceArgument.ImageItems imageItems = imageMarkFaceResult.items.get(0);
        final ImageMark5FaceArgument.ImageMarksPoints eye_left = imageItems.marks.eye_left;
        array[n++] = new PointF(eye_left.x, eye_left.y);
        final ImageMark5FaceArgument.ImageMarksPoints eye_right = imageItems.marks.eye_right;
        array[n++] = new PointF(eye_right.x, eye_right.y);
        final ImageMark5FaceArgument.ImageMarksPoints nose = imageItems.marks.nose;
        array[n++] = new PointF(nose.x, nose.y);
        final ImageMark5FaceArgument.ImageMarksPoints mouth_left = imageItems.marks.mouth_left;
        array[n++] = new PointF(mouth_left.x, mouth_left.y);
        final ImageMark5FaceArgument.ImageMarksPoints mouth_right = imageItems.marks.mouth_right;
        array[n++] = new PointF(mouth_right.x, mouth_right.y);
        return array;
    }
    
    @Override
    protected void asyncEditWithResult(final TuSdkResult tuSdkResult) {
        this.loadOrginImage(tuSdkResult);
        if (tuSdkResult.filterWrap != null && tuSdkResult.image != null) {
            tuSdkResult.image = BitmapHelper.imageScale(tuSdkResult.image, TuSdkSize.create(tuSdkResult.image).limitScale());
            final FilterWrap clone = tuSdkResult.filterWrap.clone();
            final TuSDKSkinWhiteningFilter tuSDKSkinWhiteningFilter = (TuSDKSkinWhiteningFilter)clone.getFilter();
            if (tuSDKSkinWhiteningFilter != null) {
                final FaceAligment faceAligment = new FaceAligment();
                faceAligment.setOrginMarks(this.c);
                tuSDKSkinWhiteningFilter.updateFaceFeatures(new FaceAligment[] { faceAligment }, 0.0f);
            }
            tuSdkResult.image = clone.process(tuSdkResult.image);
        }
        this.asyncProcessingIfNeedSave(tuSdkResult);
    }
}
