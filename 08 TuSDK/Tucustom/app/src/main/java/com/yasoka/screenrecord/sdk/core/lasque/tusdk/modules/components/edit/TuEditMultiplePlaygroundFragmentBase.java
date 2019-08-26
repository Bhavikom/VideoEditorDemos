// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.edit;

//import org.lasque.tusdk.modules.components.ComponentErrorType;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.view.View;
import android.content.Context;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.utils.image.RatioType;
import android.widget.ImageView;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.secret.SdkValid;
import java.util.ArrayList;
import java.io.File;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.network.analysis.ImageOnlineAnalysis;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis;
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
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.RatioType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentErrorType;

import java.util.List;
//import org.lasque.tusdk.impl.activity.TuResultFragment;

public abstract class TuEditMultiplePlaygroundFragmentBase extends TuResultFragment
{
    private int a;
    private boolean b;
    private boolean c;
    private List<TuEditActionType> d;
    private ImageAutoColorAnalysis e;
    private int f;
    private List<TuDraftImageWrap> g;
    private TuDraftImageWrap h;
    private int i;
    private ImageAutoColorAnalysis.ImageAutoColorAnalysisListener j;
    private ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener k;
    
    public TuEditMultiplePlaygroundFragmentBase() {
        this.f = 20;
        this.i = -1;
        this.j = new ImageAutoColorAnalysis.ImageAutoColorAnalysisListener() {
            @Override
            public void onImageAutoColorAnalysisCompleted(final Bitmap bitmap, final ImageOnlineAnalysis.ImageAnalysisType imageAnalysisType) {
                if (imageAnalysisType != ImageOnlineAnalysis.ImageAnalysisType.Succeed) {
                    TLog.e("error on auto adjust:%s", imageAnalysisType);
                    TuEditMultiplePlaygroundFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_adjust_color_error"));
                }
            }
        };
        this.k = new ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener() {
            @Override
            public void onImageAutoColorAnalysisCopyCompleted(final File file) {
                if (file != null && file.exists()) {
                    TuEditMultiplePlaygroundFragmentBase.this.appendHistory(file);
                    TuEditMultiplePlaygroundFragmentBase.this.hubDismiss();
                }
                else {
                    TLog.e("error on saving temp file", new Object[0]);
                    TuEditMultiplePlaygroundFragmentBase.this.hubError(TuSdkContext.getString("lsq_edit_process_adjust_color_error"));
                }
            }
        };
    }
    
    public abstract int[] getRatioTypes();
    
    protected abstract void onRefreshStepStates(final int p0, final int p1);
    
    protected abstract boolean prepareSave(final int p0, final int p1);
    
    protected abstract boolean prepareSaveDraftImage(final TuDraftImageWrap p0);
    
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
                    d.add(e);
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
        if (this.getProcessingDraftImageWrap() == null) {
            this.onRefreshStepStates(0, 0);
        }
        else {
            this.onRefreshStepStates(this.getProcessingDraftImageWrap().getHistoriesSize(), this.getProcessingDraftImageWrap().getBrushiesSize());
        }
    }
    
    protected void clearAllSteps() {
        final List<TuDraftImageWrap> draftImageList = this.getDraftImageList();
        for (int i = 0; i < draftImageList.size(); ++i) {
            draftImageList.get(i).clearAllSteps();
        }
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
    
    public List<TuDraftImageWrap> getDraftImageList() {
        if (this.g == null) {
            this.g = new ArrayList<TuDraftImageWrap>();
        }
        return this.g;
    }
    
    public void setDraftImageList(final List<TuDraftImageWrap> g) {
        this.g = g;
    }
    
    public TuDraftImageWrap getProcessingDraftImageWrap() {
        return this.h;
    }
    
    protected void setProcessingDraftImageWrap(final TuDraftImageWrap h) {
        this.h = h;
    }
    
    public int getProcessingDraftIndex() {
        return this.i;
    }
    
    public void setProcessingDraftImageIndex(final int i) {
        this.i = i;
    }
    
    protected void handleStepPrevButton(final ImageView imageView, final DraftImageLoadListener draftImageLoadListener) {
        if (this.getProcessingDraftImageWrap() == null) {
            return;
        }
        if (this.getProcessingDraftImageWrap().getHistoriesSize() < 2) {
            return;
        }
        this.getProcessingDraftImageWrap().getBrushies().add(this.getProcessingDraftImageWrap().getHistories().remove(this.getProcessingDraftImageWrap().getHistories().size() - 1));
        this.refreshStepStates();
        this.asyncLoadImage(imageView, this.getProcessingDraftImageWrap(), false, draftImageLoadListener);
    }
    
    protected void handleStepNextButton(final ImageView imageView, final DraftImageLoadListener draftImageLoadListener) {
        if (this.getProcessingDraftImageWrap() == null) {
            return;
        }
        if (this.getProcessingDraftImageWrap().getBrushiesSize() == 0) {
            return;
        }
        final List<File> brushies = this.getProcessingDraftImageWrap().getBrushies();
        this.getProcessingDraftImageWrap().getHistories().add(brushies.remove(brushies.size() - 1));
        this.refreshStepStates();
        this.asyncLoadImage(imageView, this.getProcessingDraftImageWrap(), false, draftImageLoadListener);
    }
    
    protected Bitmap loadDraftImage(final TuDraftImageWrap tuDraftImageWrap) {
        if (tuDraftImageWrap == null) {
            return null;
        }
        Bitmap bitmap = tuDraftImageWrap.getImage(this.a());
        if (bitmap == null) {
            return null;
        }
        if (tuDraftImageWrap.getLastSteps() == null) {
            int[] array = this.getRatioTypes();
            if (array == null || array.length == 0) {
                array = RatioType.ratioTypes;
            }
            final float firstRatio = RatioType.firstRatio(array[0]);
            if (firstRatio > 0.0f) {
                bitmap = BitmapHelper.imageCorp(bitmap, firstRatio);
            }
            final File file = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", StringHelper.timeStampString()));
            BitmapHelper.saveBitmap(file, bitmap, this.getOutputCompress());
            if (file.exists()) {
                bitmap = BitmapHelper.imageResize(bitmap, this.getImageDisplaySize(), true);
                tuDraftImageWrap.getHistories().add(file);
            }
        }
        return bitmap;
    }
    
    public TuSdkSize getImageDisplaySize() {
        final TuSdkSize screenSize = ContextUtils.getScreenSize((Context)this.getActivity());
        if (screenSize != null) {
            screenSize.width = (int)Math.floor(screenSize.width * 0.75);
            screenSize.height = (int)Math.floor(screenSize.height * 0.75);
        }
        return screenSize;
    }
    
    protected void asyncLoadImage(final ImageView imageView, final File file, final boolean b, final DraftImageLoadListener draftImageLoadListener) {
        if (file == null || !file.exists() || !file.isFile()) {
            return;
        }
        if (b) {
            this.hubStatus(TuSdkContext.getString("lsq_edit_loading"));
        }
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                TuEditMultiplePlaygroundFragmentBase.this.runOnUiThread(new Runnable() {
                    final /* synthetic */ Bitmap a = BitmapHelper.getBitmap(file, TuEditMultiplePlaygroundFragmentBase.this.getImageDisplaySize(), true);
                    
                    @Override
                    public void run() {
                        if (imageView != null) {
                            imageView.setImageBitmap(this.a);
                        }
                        if (draftImageLoadListener != null) {
                            draftImageLoadListener.onLoadingComplete((View)imageView, this.a);
                        }
                        TuEditMultiplePlaygroundFragmentBase.this.hubDismiss();
                    }
                });
            }
        });
    }
    
    protected void asyncLoadImage(final ImageView imageView, final TuDraftImageWrap tuDraftImageWrap, final boolean b, final DraftImageLoadListener draftImageLoadListener) {
        if (tuDraftImageWrap == null) {
            return;
        }
        if (tuDraftImageWrap.getLastSteps() != null) {
            this.asyncLoadImage(imageView, tuDraftImageWrap.getLastSteps(), b, draftImageLoadListener);
            return;
        }
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                TuEditMultiplePlaygroundFragmentBase.this.runOnUiThread(new Runnable() {
                    final /* synthetic */ Bitmap a = TuEditMultiplePlaygroundFragmentBase.this.loadDraftImage(tuDraftImageWrap);
                    
                    @Override
                    public void run() {
                        if (imageView != null) {
                            imageView.setImageBitmap(this.a);
                        }
                        if (draftImageLoadListener != null) {
                            draftImageLoadListener.onLoadingComplete((View)imageView, this.a);
                        }
                    }
                });
            }
        });
    }
    
    public synchronized File getLastSteps() {
        if (this.getProcessingDraftImageWrap() == null || this.getProcessingDraftImageWrap().getHistoriesSize() == 0) {
            return null;
        }
        return this.getProcessingDraftImageWrap().getLastSteps();
    }
    
    public synchronized void appendHistory(final File file) {
        if (this.getProcessingDraftImageWrap() == null) {
            return;
        }
        if (file == null || !file.exists() || !file.isFile()) {
            return;
        }
        if (this.isDisableStepsSave()) {
            this.getProcessingDraftImageWrap().clearSteps(this.getProcessingDraftImageWrap().getHistories());
            this.getProcessingDraftImageWrap().getHistories().add(file);
        }
        else {
            final int n = this.getProcessingDraftImageWrap().getHistoriesSize() - this.getLimitHistoryCount();
            if (n > 1) {
                final ArrayList<File> list = new ArrayList<File>();
                for (int i = 1; i <= n; ++i) {
                    list.add(this.getProcessingDraftImageWrap().getHistories().get(i));
                }
                this.getProcessingDraftImageWrap().getHistories().removeAll(list);
                this.getProcessingDraftImageWrap().clearSteps(list);
                this.clearSteps(list);
            }
            this.getProcessingDraftImageWrap().getHistories().add(file);
            this.getProcessingDraftImageWrap().clearSteps(this.getProcessingDraftImageWrap().getBrushies());
        }
        this.refreshStepStates();
    }
    
    private int a() {
        int a = 0;
        if (this.getLimitSideSize() > 0) {
            a = this.getLimitSideSize();
        }
        else if (ContextUtils.getScreenSize((Context)this.getActivity()) != null) {
            a = ContextUtils.getScreenSize((Context)this.getActivity()).maxSide();
        }
        final Integer value = SdkValid.shared.maxImageSide();
        if (value == 0) {
            return a;
        }
        return Math.min(a, value);
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
                TuEditMultiplePlaygroundFragmentBase.this.a(TuEditMultiplePlaygroundFragmentBase.this.loadDraftImage(TuEditMultiplePlaygroundFragmentBase.this.getProcessingDraftImageWrap()));
            }
        });
    }
    
    private void a(final Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        this.e.analysisWithThumb(bitmap, this.getLastSteps(), new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", StringHelper.timeStampString())), this.j, this.k);
    }
    
    protected void handleCompleteButton() {
        final TuSdkResult tuSdkResult = new TuSdkResult();
        tuSdkResult.images = new ArrayList<ImageSqlInfo>(this.getDraftImageList().size());
        if (!this.prepareSave(this.getDraftImageList().size(), this.changedCount())) {
            final List<TuDraftImageWrap> draftImageList = this.getDraftImageList();
            for (int i = 0; i < draftImageList.size(); ++i) {
                final TuDraftImageWrap tuDraftImageWrap = draftImageList.get(i);
                if (tuDraftImageWrap.getImageSqlInfo() != null) {
                    tuSdkResult.images.add(tuDraftImageWrap.getImageSqlInfo());
                }
            }
            this.notifyProcessing(tuSdkResult);
            return;
        }
        this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                TuEditMultiplePlaygroundFragmentBase.this.asyncEditWithResult(tuSdkResult);
            }
        }).start();
    }
    
    protected int changedCount() {
        int n = 0;
        final Iterator<TuDraftImageWrap> iterator = this.getDraftImageList().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isChanged()) {
                ++n;
            }
        }
        return n;
    }
    
    protected void asyncEditWithResult(final TuSdkResult tuSdkResult) {
        tuSdkResult.image = BitmapHelper.getBitmap(tuSdkResult.imageFile, true);
        if (this.getWaterMarkOption() != null) {
            tuSdkResult.image = this.addWaterMarkToImage(tuSdkResult.image);
        }
        this.asyncProcessingIfNeedSave(tuSdkResult);
    }
    
    @Override
    public boolean isSaveToAlbum() {
        return super.isSaveToAlbum() || !this.isSaveToTemp();
    }
    
    protected void showProgress(final int i, final int j) {
        this.hubStatus(TuSdkContext.getString("lsq_edit_processing_index", j, i));
    }
    
    @Override
    protected void saveToAlbum(final TuSdkResult tuSdkResult) {
        this.hubStatus(TuSdkContext.getString("lsq_save_saveToAlbum"));
        final int size = this.getDraftImageList().size();
        for (int i = 0; i < this.getDraftImageList().size(); ++i) {
            this.showProgress(size, i);
            final TuDraftImageWrap tuDraftImageWrap = this.getDraftImageList().get(i);
            tuDraftImageWrap.setImage(null);
            if (!this.prepareSaveDraftImage(tuDraftImageWrap)) {
                if (tuDraftImageWrap.getOutputImageSqlInfo() != null) {
                    tuSdkResult.images.add(tuDraftImageWrap.getImageSqlInfo());
                }
            }
            else {
                final ComponentErrorType canSaveFile;
                if ((canSaveFile = this.canSaveFile()) != null) {
                    this.notifyError(tuSdkResult, canSaveFile);
                }
                else {
                    Bitmap bitmap = tuDraftImageWrap.getImage();
                    if (bitmap != null) {
                        if (this.getWaterMarkOption() != null) {
                            bitmap = this.addWaterMarkToImage(bitmap);
                        }
                        File albumFile = null;
                        if (StringHelper.isNotBlank(this.getSaveToAlbumName())) {
                            albumFile = AlbumHelper.getAlbumFile(this.getSaveToAlbumName());
                        }
                        final ImageSqlInfo saveJpgToAblum = ImageSqlHelper.saveJpgToAblum((Context)this.getActivity(), bitmap, this.getOutputCompress(), albumFile);
                        final File file = new File(saveJpgToAblum.path);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                        if (saveJpgToAblum != null && file.exists()) {
                            ImageSqlHelper.notifyRefreshAblum((Context)this.getActivity(), saveJpgToAblum);
                            tuSdkResult.images.add(saveJpgToAblum);
                        }
                    }
                }
            }
        }
        if (tuSdkResult.images.size() > 0) {
            this.hubSuccess(TuSdkContext.getString("lsq_save_saveToTemp_completed"));
        }
    }
    
    @Override
    protected void saveToTemp(final TuSdkResult tuSdkResult) {
        this.hubStatus(TuSdkContext.getString("lsq_save_saveToTemp"));
        final int size = this.getDraftImageList().size();
        for (int i = 0; i < this.getDraftImageList().size(); ++i) {
            this.showProgress(size, i);
            final TuDraftImageWrap tuDraftImageWrap = this.getDraftImageList().get(i);
            tuDraftImageWrap.setImage(null);
            if (!this.prepareSaveDraftImage(tuDraftImageWrap)) {
                if (tuDraftImageWrap.getOutputImageSqlInfo() != null) {
                    tuSdkResult.images.add(tuDraftImageWrap.getImageSqlInfo());
                }
            }
            else {
                final ComponentErrorType canSaveFile;
                if ((canSaveFile = this.canSaveFile()) != null) {
                    this.notifyError(tuSdkResult, canSaveFile);
                }
                else {
                    Bitmap bitmap = tuDraftImageWrap.getImage();
                    if (bitmap != null) {
                        if (this.getWaterMarkOption() != null) {
                            bitmap = this.addWaterMarkToImage(bitmap);
                        }
                        final File file = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", StringHelper.timeStampString()));
                        BitmapHelper.saveBitmap(file, bitmap, this.getOutputCompress());
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                        if (file != null && file.exists()) {
                            final ImageSqlInfo imageSqlInfo = new ImageSqlInfo();
                            imageSqlInfo.path = file.getAbsolutePath();
                            tuSdkResult.images.add(imageSqlInfo);
                        }
                    }
                }
            }
        }
        if (tuSdkResult.images.size() > 0) {
            this.hubSuccess(TuSdkContext.getString("lsq_save_saveToTemp_completed"));
        }
    }
    
    @Override
    protected void asyncProcessingIfNeedSave(final TuSdkResult tuSdkResult) {
        if (ThreadHelper.isMainThread()) {
            this.notifyProcessing(tuSdkResult);
            return;
        }
        if (!this.asyncNotifyProcessing(tuSdkResult)) {
            if (this.isSaveToTemp()) {
                this.saveToTemp(tuSdkResult);
            }
            else if (this.isSaveToAlbum()) {
                this.saveToAlbum(tuSdkResult);
            }
        }
        this.clearAllSteps();
        this.backUIThreadNotifyProcessing(tuSdkResult);
        StatisticsManger.appendComponent(ComponentActType.editPhotoAction);
    }
    
    protected interface DraftImageLoadListener
    {
        void onLoadingComplete(final View p0, final Bitmap p1);
    }
}
