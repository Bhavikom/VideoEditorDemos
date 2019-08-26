// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
//import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.LiveStickerPlayController;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;

import java.util.ArrayList;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.List;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
//import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
//import org.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;

public class TuSDKMediaEffectsManagerImpl implements TuSDKMediaEffectsManager
{
    private TuSDKMediaEffectsDataManager a;
    private TuSDKComboFilterWrapChain b;
    private LiveStickerPlayController c;
    private boolean d;
    private static final TuSdkMediaEffectData.TuSdkMediaEffectDataType[] e;
    private TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate f;
    
    public TuSDKMediaEffectsManagerImpl() {
        this.d = true;
        this.b = new TuSDKComboFilterWrapChain();
        this.a = new TuSDKMediaEffectsDataManager();
    }
    
    @Override
    public TuSDKComboFilterWrapChain getFilterWrapChain() {
        return this.b;
    }
    
    @Override
    public void setMediaEffectDelegate(final TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate f) {
        this.f = f;
    }
    
    @Override
    public boolean addMediaEffectData(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        if (tuSdkMediaEffectData == null) {
            return false;
        }
        this.a(tuSdkMediaEffectData);
        return this.a.addMediaEffect(tuSdkMediaEffectData);
    }
    
    @Override
    public void addTerminalNode(final SelesContext.SelesInput selesInput) {
        if (this.b == null || selesInput == null) {
            return;
        }
        this.b.addTerminalNode(selesInput);
    }
    
    private void a(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        if (tuSdkMediaEffectData instanceof TuSdkMediaStickerEffectData || tuSdkMediaEffectData instanceof TuSdkMediaStickerAudioEffectData) {
            this.d = true;
        }
        if (tuSdkMediaEffectData.getMediaEffectType() == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker || tuSdkMediaEffectData.getMediaEffectType() == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio) {
            this.a.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
            this.removeAllLiveSticker();
        }
    }
    
    @Override
    public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        return this.a.mediaEffectsWithType(tuSdkMediaEffectDataType);
    }
    
    @Override
    public List<TuSdkMediaEffectData> getAllMediaEffectData() {
        return this.a.getAllMediaEffectData();
    }
    
    @Override
    public boolean removeMediaEffectData(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        if (tuSdkMediaEffectData == null) {
            TLog.e("remove TuSdkMediaEffectData must be not null !!", new Object[0]);
            return false;
        }
        this.b.removeFilterWrap(tuSdkMediaEffectData.getFilterWrap());
        return this.a.removeMediaEffect(tuSdkMediaEffectData);
    }
    
    @Override
    public void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        final Iterator<TuSdkMediaEffectData> iterator = this.a.mediaEffectsWithType(tuSdkMediaEffectDataType).iterator();
        while (iterator.hasNext()) {
            this.b.removeFilterWrap(iterator.next().getFilterWrap());
        }
        this.a.removeMediaEffectsWithType(tuSdkMediaEffectDataType);
        if (tuSdkMediaEffectDataType == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker) {
            this.removeAllLiveSticker();
        }
    }
    
    @Override
    public void removeAllMediaEffects() {
        this.removeAllLiveSticker();
        if (this.b != null) {
            this.b.removeAllFilterWrapNode();
        }
        this.a.removeAllMediaEffect();
        this.d = true;
    }
    
    @Override
    public void updateEffectTimeLine(final long n, final OnFilterChangeListener onFilterChangeListener) {
        final TuSdkMediaEffectLinkedMap.TuSdkMediaEffectApply seekTime = this.a.seekTime(n);
        final List<TuSdkMediaEffectData> a = seekTime.a;
        for (final TuSdkMediaEffectData tuSdkMediaEffectData : seekTime.b) {
            switch (tuSdkMediaEffectData.getMediaEffectType().ordinal()) {
                case 1: {
                    ((TuSdkMediaTileEffectDataBase)tuSdkMediaEffectData).getStickerData().setEnabled(false);
                    break;
                }
                case 2: {
                    ((TuSdkMediaTileEffectDataBase)tuSdkMediaEffectData).getStickerData().setEnabled(false);
                    break;
                }
                case 3: {
                    this.a((TuSdkMediaStickerEffectData)tuSdkMediaEffectData, false);
                    break;
                }
                case 4: {
                    ((TuSdkMediaParticleEffectData)tuSdkMediaEffectData).resetParticleFilter();
                    break;
                }
                case 5: {
                    this.a(((TuSdkMediaStickerAudioEffectData)tuSdkMediaEffectData).getMediaStickerEffectData(), false);
                    break;
                }
                case 6: {
                    tuSdkMediaEffectData.setIsApplied(true);
                    break;
                }
            }
            if (tuSdkMediaEffectData.isApplied()) {
                this.b.removeFilterWrap(tuSdkMediaEffectData.getFilterWrap());
                tuSdkMediaEffectData.setIsApplied(false);
            }
        }
        for (final TuSdkMediaEffectData tuSdkMediaEffectData2 : a) {
            switch (tuSdkMediaEffectData2.getMediaEffectType().ordinal()) {
                case 4: {
                    this.a((TuSdkMediaParticleEffectData)tuSdkMediaEffectData2, n);
                    continue;
                }
                case 1: {
                    this.applyTextStickerData((TuSdkMediaTileEffectDataBase)tuSdkMediaEffectData2, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeText);
                    continue;
                }
                case 2: {
                    this.applyTextStickerData((TuSdkMediaTileEffectDataBase)tuSdkMediaEffectData2, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediEffectDataTypeStickerImage);
                    continue;
                }
                case 3: {
                    this.a((TuSdkMediaStickerEffectData)tuSdkMediaEffectData2);
                    continue;
                }
                case 7: {
                    this.a((TuSdkMediaAudioEffectData)tuSdkMediaEffectData2);
                    continue;
                }
                case 8: {
                    this.a((TuSdkMediaFilterEffectData)tuSdkMediaEffectData2, onFilterChangeListener);
                    continue;
                }
                case 9: {
                    this.a((TuSdkMediaSceneEffectData)tuSdkMediaEffectData2);
                    continue;
                }
                case 10: {
                    this.a((TuSdkMediaComicEffectData)tuSdkMediaEffectData2);
                    continue;
                }
                case 11: {
                    this.a((TuSdkMediaPlasticFaceEffect)tuSdkMediaEffectData2);
                    continue;
                }
                case 12: {
                    this.a((TuSdkMediaSkinFaceEffect)tuSdkMediaEffectData2);
                    continue;
                }
                case 13: {
                    this.a((TuSDKMediaMonsterFaceEffect)tuSdkMediaEffectData2);
                    continue;
                }
                case 6: {
                    this.a((TuSdkMediaTransitionEffectData)tuSdkMediaEffectData2);
                    break;
                }
            }
            TLog.w("apply not find effect %s", new Object[] { tuSdkMediaEffectData2.getMediaEffectType() });
        }
    }
    
    private void a() {
        this.b.removeAllFilterWrapNode();
        for (final TuSdkMediaEffectData tuSdkMediaEffectData : this.a.getApplyMediaEffectDataList(TuSDKMediaEffectsManagerImpl.e)) {
            if (tuSdkMediaEffectData.isApplied()) {
                this.b.addFilterWrap(tuSdkMediaEffectData.getFilterWrap());
            }
        }
    }
    
    private void a(final OnFilterChangeListener onFilterChangeListener, final TuSdkMediaEffectData tuSdkMediaEffectData) {
        if (onFilterChangeListener == null) {
            return;
        }
        onFilterChangeListener.onFilterChanged(tuSdkMediaEffectData.getFilterWrap());
    }
    
    private void b(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        if (this.f == null) {
            return;
        }
        this.f.didApplyingMediaEffect(tuSdkMediaEffectData);
    }
    
    private void a(final TuSdkMediaParticleEffectData tuSdkMediaParticleEffectData, final long n) {
        if (tuSdkMediaParticleEffectData == null) {
            return;
        }
        if (!tuSdkMediaParticleEffectData.isApplied()) {
            tuSdkMediaParticleEffectData.setIsApplied(true);
            tuSdkMediaParticleEffectData.resetParticleFilter();
            tuSdkMediaParticleEffectData.getFilterWrap().setParticleSize(tuSdkMediaParticleEffectData.getSize());
            tuSdkMediaParticleEffectData.getFilterWrap().setParticleColor(tuSdkMediaParticleEffectData.getColor());
            this.a();
            this.b(tuSdkMediaParticleEffectData);
        }
        tuSdkMediaParticleEffectData.getFilterWrap().updateParticleEmitPosition(tuSdkMediaParticleEffectData.getPointF(n));
    }
    
    private void a(final TuSdkMediaFilterEffectData tuSdkMediaFilterEffectData, final OnFilterChangeListener onFilterChangeListener) {
        if (tuSdkMediaFilterEffectData == null || tuSdkMediaFilterEffectData.isApplied()) {
            return;
        }
        tuSdkMediaFilterEffectData.setIsApplied(true);
        this.a();
        this.a(onFilterChangeListener, tuSdkMediaFilterEffectData);
        this.b(tuSdkMediaFilterEffectData);
    }
    
    private void a(final TuSdkMediaSceneEffectData tuSdkMediaSceneEffectData) {
        if (tuSdkMediaSceneEffectData == null || tuSdkMediaSceneEffectData.isApplied()) {
            return;
        }
        tuSdkMediaSceneEffectData.setIsApplied(true);
        this.a();
        this.b(tuSdkMediaSceneEffectData);
    }
    
    private void a(final TuSdkMediaComicEffectData tuSdkMediaComicEffectData) {
        if (tuSdkMediaComicEffectData == null || tuSdkMediaComicEffectData.isApplied()) {
            return;
        }
        tuSdkMediaComicEffectData.setIsApplied(true);
        this.a();
        this.b(tuSdkMediaComicEffectData);
    }
    
    private void a(final TuSdkMediaPlasticFaceEffect tuSdkMediaPlasticFaceEffect) {
        if (tuSdkMediaPlasticFaceEffect == null || tuSdkMediaPlasticFaceEffect.isApplied()) {
            return;
        }
        tuSdkMediaPlasticFaceEffect.setIsApplied(true);
        this.a();
        this.b(tuSdkMediaPlasticFaceEffect);
    }
    
    private void a(final TuSdkMediaSkinFaceEffect tuSdkMediaSkinFaceEffect) {
        if (tuSdkMediaSkinFaceEffect == null || tuSdkMediaSkinFaceEffect.isApplied()) {
            return;
        }
        tuSdkMediaSkinFaceEffect.setIsApplied(true);
        this.a();
        this.b(tuSdkMediaSkinFaceEffect);
    }
    
    private void a(final TuSDKMediaMonsterFaceEffect tuSDKMediaMonsterFaceEffect) {
        if (tuSDKMediaMonsterFaceEffect == null || tuSDKMediaMonsterFaceEffect.isApplied()) {
            return;
        }
        tuSDKMediaMonsterFaceEffect.setIsApplied(true);
        this.a();
        this.b(tuSDKMediaMonsterFaceEffect);
    }
    
    private void a(final TuSdkMediaTransitionEffectData tuSdkMediaTransitionEffectData) {
        if (tuSdkMediaTransitionEffectData == null || tuSdkMediaTransitionEffectData.isApplied()) {
            return;
        }
        tuSdkMediaTransitionEffectData.setIsApplied(true);
        this.a();
        this.b(tuSdkMediaTransitionEffectData);
    }
    
    protected void applyTextStickerData(final TuSdkMediaTileEffectDataBase tuSdkMediaTileEffectDataBase, final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        if (tuSdkMediaTileEffectDataBase.getFilterWrap() == null || tuSdkMediaTileEffectDataBase.isApplied()) {
            return;
        }
        tuSdkMediaTileEffectDataBase.getStickerData().setEnabled(true);
        tuSdkMediaTileEffectDataBase.getStickerData().reset();
        final ArrayList<TuSdkImage2DSticker> list = new ArrayList<TuSdkImage2DSticker>();
        final Iterator<TuSdkMediaEffectData> iterator = this.a.mediaEffectsWithType(tuSdkMediaEffectDataType).iterator();
        while (iterator.hasNext()) {
            list.add(((TuSdkMediaTileEffectDataBase)iterator.next()).getStickerData());
        }
        tuSdkMediaTileEffectDataBase.getFilterWrap().updateTileStickers((List)list);
        tuSdkMediaTileEffectDataBase.setIsApplied(true);
        this.a();
        this.b(tuSdkMediaTileEffectDataBase);
    }
    
    private void a(final TuSdkMediaStickerEffectData tuSdkMediaStickerEffectData) {
        if (tuSdkMediaStickerEffectData == null) {
            return;
        }
        if (this.d) {
            this.showGroupSticker(tuSdkMediaStickerEffectData);
            this.d = false;
        }
        if (!tuSdkMediaStickerEffectData.isApplied()) {
            this.a(tuSdkMediaStickerEffectData, true);
            tuSdkMediaStickerEffectData.setIsApplied(true);
            this.a();
            this.b((TuSdkMediaEffectData)tuSdkMediaStickerEffectData);
        }
    }
    
    private void a(final TuSdkMediaAudioEffectData tuSdkMediaAudioEffectData) {
        if (tuSdkMediaAudioEffectData == null || !tuSdkMediaAudioEffectData.isVaild()) {
            return;
        }
        this.b(tuSdkMediaAudioEffectData);
    }
    
    @Deprecated
    @Override
    public void removeAllLiveSticker() {
        if (this.c != null) {
            this.c.removeAllStickers();
            this.d = true;
        }
    }
    
    @Override
    public void showGroupSticker(final TuSdkMediaStickerEffectData tuSdkMediaStickerEffectData) {
        final StickerGroup stickerGroup = tuSdkMediaStickerEffectData.getStickerGroup();
        if (stickerGroup == null || stickerGroup.stickers == null || stickerGroup.categoryId != StickerCategory.StickerCategoryType.StickerCategorySmart.getValue()) {
            TLog.e("Only live sticker could be used here", new Object[0]);
            return;
        }
        if (stickerGroup.stickers.size() > 5) {
            TLog.e("Too many live stickers in the group, please try to remove some stickers first.", new Object[0]);
            return;
        }
        if (this.c == null) {
            this.c = new LiveStickerPlayController(SelesContext.currentEGLContext());
        }
        this.c.showGroupSticker(stickerGroup);
        this.b(tuSdkMediaStickerEffectData);
    }
    
    private void b(final TuSdkMediaStickerEffectData tuSdkMediaStickerEffectData) {
        if (this.c != null && this.b != null && tuSdkMediaStickerEffectData.getFilterWrap() instanceof SelesParameters.FilterStickerInterface) {
            ((SelesParameters.FilterStickerInterface)tuSdkMediaStickerEffectData.getFilterWrap()).updateStickers(this.c.getStickers());
            boolean b = false;
            for (final TuSdkMediaEffectData tuSdkMediaEffectData : this.a.mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker)) {
                if (tuSdkMediaEffectData instanceof TuSdkMediaStickerEffectData && tuSdkMediaEffectData.isApplied()) {
                    b = true;
                    break;
                }
            }
            this.a(tuSdkMediaStickerEffectData, b);
        }
    }
    
    private void a(final TuSdkMediaStickerEffectData tuSdkMediaStickerEffectData, final boolean stickerVisibility) {
        if (this.c != null && this.b != null && tuSdkMediaStickerEffectData.getFilterWrap() instanceof SelesParameters.FilterStickerInterface) {
            ((SelesParameters.FilterStickerInterface)tuSdkMediaStickerEffectData.getFilterWrap()).setStickerVisibility(stickerVisibility);
        }
    }
    
    @Override
    public LiveStickerPlayController getLiveStickerPlayController() {
        return this.c;
    }
    
    @Override
    public void release() {
        this.removeAllLiveSticker();
        this.removeAllMediaEffects();
        if (this.c != null) {
            this.c.destroy();
            this.c = null;
        }
        if (this.b != null) {
            this.b.destroy();
            this.b = null;
        }
    }
    
    static {
        e = new TuSdkMediaEffectData.TuSdkMediaEffectDataType[] { TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSkinFace, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeTransition };
    }
}
