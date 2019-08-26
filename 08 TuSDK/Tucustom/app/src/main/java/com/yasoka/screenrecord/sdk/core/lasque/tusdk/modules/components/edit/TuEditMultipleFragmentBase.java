// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.edit;

//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.core.utils.image.RatioType;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.TuSdkResult;
import android.content.Context;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.network.analysis.ImageOnlineAnalysis;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageOnlineAnalysis;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.RatioType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuImageResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;

import java.util.ArrayList;
import java.io.File;
//import org.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis;
import java.util.List;
//import org.lasque.tusdk.impl.activity.TuImageResultFragment;

public abstract class TuEditMultipleFragmentBase extends TuImageResultFragment
{
    private int a;
    private boolean b;
    private boolean c;
    private List<TuEditActionType> d;
    private ImageAutoColorAnalysis e;
    private int f;
    private final List<File> g;
    private final List<File> h;
    private ImageAutoColorAnalysis.ImageAutoColorAnalysisListener i;
    private ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener j;
    
    public TuEditMultipleFragmentBase() {
        this.f = 20;
        this.g = new ArrayList<File>();
        this.h = new ArrayList<File>();
        this.i = new ImageAutoColorAnalysis.ImageAutoColorAnalysisListener() {
            @Override
            public void onImageAutoColorAnalysisCompleted(final Bitmap displayImage, final ImageOnlineAnalysis.ImageAnalysisType imageAnalysisType) {
                if (imageAnalysisType == ImageOnlineAnalysis.ImageAnalysisType.Succeed) {
                    TuEditMultipleFragmentBase.this.setDisplayImage(displayImage);
                }
                else {
                    TLog.e("error on auto adjust:%s", imageAnalysisType);
                    TuEditMultipleFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_adjust_color_error"));
                }
            }
        };
        this.j = new ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener() {
            @Override
            public void onImageAutoColorAnalysisCopyCompleted(final File file) {
                if (file != null && file.exists()) {
                    TuEditMultipleFragmentBase.this.appendHistory(file);
                    TuEditMultipleFragmentBase.this.hubDismiss();
                }
                else {
                    TLog.e("error on saving temp file", new Object[0]);
                    TuEditMultipleFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_adjust_color_error"));
                }
            }
        };
    }
    
    public abstract void setDisplayImage(final Bitmap p0);
    
    public abstract int[] getRatioTypes();
    
    protected abstract void onRefreshStepStates(final int p0, final int p1);
    
    public final int getLimitSideSize() {
        return this.a;
    }
    
    public final void setLimitSideSize(final int a) {
        this.a = a;
    }
    
    public final boolean isLimitForScreen() {
        return this.b;
    }
    
    public final void setLimitForScreen(final boolean b) {
        this.b = b;
    }
    
    protected int getLimitHistoryCount() {
        return this.f;
    }
    
    protected void setLimitHistoryCount(final int f) {
        this.f = f;
    }
    
    public boolean isDisableStepsSave() {
        return this.c;
    }
    
    public void setDisableStepsSave(final boolean c) {
        this.c = c;
    }
    
    public List<TuEditActionType> getModules() {
        if (this.d == null || this.d.size() == 0) {
            this.d = TuEditActionType.multipleActionTypes();
        }
        final ArrayList<TuEditActionType> d = new ArrayList<TuEditActionType>();
        for (int i = 0; i < this.d.size(); ++i) {
            final TuEditActionType e = this.d.get(i);
            if (e != TuEditActionType.TypeSmudge || SdkValid.shared.smudgeEnabled()) {
                if (e != TuEditActionType.TypeWipeFilter || SdkValid.shared.wipeFilterEnabled()) {
                    if (e != TuEditActionType.TypeHDR || SdkValid.shared.hdrFilterEnabled()) {
                        if (e != TuEditActionType.TypePaint || SdkValid.shared.paintEnabled()) {
                            d.add(e);
                        }
                    }
                }
            }
        }
        return this.d = d;
    }
    
    public void setModules(final List<TuEditActionType> d) {
        this.d = d;
    }
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        StatisticsManger.appendComponent(ComponentActType.editMultipleFragment);
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.clearAllSteps();
    }
    
    protected final void refreshStepStates() {
        this.onRefreshStepStates(this.g.size(), this.h.size());
    }
    
    protected void clearAllSteps() {
        this.clearSteps(this.g);
        this.clearSteps(this.h);
    }
    
    protected void clearSteps(final List<File> list) {
        if (list == null) {
            return;
        }
        for (final File file : list) {
            TLog.d("clearSteps (%s): %s", file.length(), file);
            FileHelper.delete(file);
        }
        list.clear();
    }
    
    protected void setHistories(final List<File> list) {
        this.g.clear();
        this.g.addAll(list);
    }
    
    protected List<File> getHistories() {
        return this.g;
    }
    
    protected void setBrushies(final List<File> list) {
        this.h.clear();
        this.h.addAll(list);
    }
    
    protected List<File> getBrushies() {
        return this.h;
    }
    
    protected void handleStepPrevButton() {
        if (this.g.size() < 2) {
            return;
        }
        this.h.add(this.g.remove(this.g.size() - 1));
        final File lastSteps = this.getLastSteps();
        this.refreshStepStates();
        this.asyncLoadStepImage(lastSteps);
    }
    
    protected void handleStepNextButton() {
        if (this.h.size() == 0) {
            return;
        }
        this.g.add(this.h.remove(this.h.size() - 1));
        final File lastSteps = this.getLastSteps();
        this.refreshStepStates();
        this.asyncLoadStepImage(lastSteps);
    }
    
    protected void asyncLoadStepImage(final File file) {
        this.asyncLoadStepImage(file, true);
    }
    
    protected void asyncLoadStepImage(final File file, final boolean b) {
        if (file == null || !file.exists() || !file.isFile()) {
            return;
        }
        if (b) {
            this.hubStatus(TuSdkContext.getString("lsq_edit_loading"));
        }
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                TuEditMultipleFragmentBase.this.runOnUiThread(new Runnable() {
                    final /* synthetic */ Bitmap a = BitmapHelper.getBitmap(file, TuEditMultipleFragmentBase.this.getImageDisplaySize(), true);
                    
                    @Override
                    public void run() {
                        TuEditMultipleFragmentBase.this.hubDismiss();
                        TuEditMultipleFragmentBase.this.setDisplayImage(this.a);
                    }
                });
            }
        });
    }
    
    public synchronized File getLastSteps() {
        if (this.g.size() == 0) {
            return null;
        }
        return this.g.get(this.g.size() - 1);
    }
    
    public synchronized void appendHistory(final File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return;
        }
        if (this.isDisableStepsSave()) {
            this.clearSteps(this.g);
            this.g.add(file);
        }
        else {
            final int n = this.g.size() - this.getLimitHistoryCount();
            if (n > 1) {
                final ArrayList<File> list = new ArrayList<File>();
                for (int i = 1; i <= n; ++i) {
                    list.add(this.g.get(i));
                }
                this.g.removeAll(list);
                this.clearSteps(list);
            }
            this.g.add(file);
            this.clearSteps(this.h);
            this.refreshStepStates();
        }
    }
    
    private int a() {
        int a;
        if (this.getLimitSideSize() > 0) {
            a = this.getLimitSideSize();
        }
        else {
            a = ContextUtils.getScreenSize((Context)this.getActivity()).maxSide();
        }
        final Integer value = SdkValid.shared.maxImageSide();
        if (value == 0) {
            return a;
        }
        return Math.min(a, value);
    }
    
    @Override
    protected Bitmap asyncLoadImage() {
        final int a = this.a();
        final TuSdkResult tuSdkResult = new TuSdkResult();
        tuSdkResult.image = BitmapHelper.getBitmap(this.getTempFilePath(), TuSdkSize.create(a, a), true);
        if (tuSdkResult.image == null) {
            tuSdkResult.image = BitmapHelper.getBitmap(this.getImageSqlInfo(), true, a);
        }
        if (tuSdkResult.image == null) {
            tuSdkResult.image = this.getImage();
            tuSdkResult.image = BitmapHelper.imageLimit(tuSdkResult.image, a);
        }
        if (tuSdkResult.image == null) {
            return null;
        }
        int[] array = this.getRatioTypes();
        if (array == null || array.length == 0) {
            array = RatioType.ratioTypes;
        }
        final float firstRatio = RatioType.firstRatio(array[0]);
        if (firstRatio > 0.0f) {
            tuSdkResult.image = BitmapHelper.imageCorp(tuSdkResult.image, firstRatio);
        }
        BitmapHelper.saveBitmap(tuSdkResult.imageFile = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", StringHelper.timeStampString())), tuSdkResult.image, this.getOutputCompress());
        if (tuSdkResult.imageFile.exists()) {
            tuSdkResult.image = BitmapHelper.imageResize(tuSdkResult.image, this.getImageDisplaySize(), true);
            this.g.add(tuSdkResult.imageFile);
        }
        return tuSdkResult.image;
    }
    
    @Override
    protected void asyncLoadImageCompleted(final Bitmap displayImage) {
        super.asyncLoadImageCompleted(displayImage);
        this.setDisplayImage(displayImage);
    }
    
    protected void handleAutoAdjust() {
        if (this.e == null) {
            this.e = new ImageAutoColorAnalysis();
        }
        else {
            this.e.reset();
        }
        this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                TuEditMultipleFragmentBase.this.a(TuEditMultipleFragmentBase.this.getImage());
            }
        });
    }
    
    private void a(final Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        this.e.analysisWithThumb(bitmap, this.getLastSteps(), new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", StringHelper.timeStampString())), this.i, this.j);
    }
    
    protected void handleCompleteButton() {
        final TuSdkResult tuSdkResult = new TuSdkResult();
        tuSdkResult.imageFile = this.getLastSteps();
        if (tuSdkResult.imageFile == null || !tuSdkResult.imageFile.exists() || !tuSdkResult.imageFile.isFile()) {
            return;
        }
        this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                TuEditMultipleFragmentBase.this.asyncEditWithResult(tuSdkResult);
            }
        }).start();
    }
    
    protected void asyncEditWithResult(final TuSdkResult tuSdkResult) {
        tuSdkResult.image = BitmapHelper.getBitmap(tuSdkResult.imageFile, true);
        if (this.getWaterMarkOption() != null) {
            tuSdkResult.image = this.addWaterMarkToImage(tuSdkResult.image);
        }
        this.asyncProcessingIfNeedSave(tuSdkResult);
    }
}
