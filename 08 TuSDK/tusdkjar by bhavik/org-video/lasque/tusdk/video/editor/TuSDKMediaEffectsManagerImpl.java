package org.lasque.tusdk.video.editor;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate;
import org.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesParameters.FilterStickerInterface;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.TuSDK2DImageFilterWrap;
import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory.StickerCategoryType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

public class TuSDKMediaEffectsManagerImpl
  implements TuSDKMediaEffectsManager
{
  private TuSDKMediaEffectsDataManager a = new TuSDKMediaEffectsDataManager();
  private TuSDKComboFilterWrapChain b = new TuSDKComboFilterWrapChain();
  private LiveStickerPlayController c;
  private boolean d = true;
  private static final TuSdkMediaEffectData.TuSdkMediaEffectDataType[] e = { TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSkinFace, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeTransition };
  private TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate f;
  
  public TuSDKComboFilterWrapChain getFilterWrapChain()
  {
    return this.b;
  }
  
  public void setMediaEffectDelegate(TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate paramTuSDKVideoProcessorMediaEffectDelegate)
  {
    this.f = paramTuSDKVideoProcessorMediaEffectDelegate;
  }
  
  public boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (paramTuSdkMediaEffectData == null) {
      return false;
    }
    a(paramTuSdkMediaEffectData);
    return this.a.addMediaEffect(paramTuSdkMediaEffectData);
  }
  
  public void addTerminalNode(SelesContext.SelesInput paramSelesInput)
  {
    if ((this.b == null) || (paramSelesInput == null)) {
      return;
    }
    this.b.addTerminalNode(paramSelesInput);
  }
  
  private void a(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (((paramTuSdkMediaEffectData instanceof TuSdkMediaStickerEffectData)) || ((paramTuSdkMediaEffectData instanceof TuSdkMediaStickerAudioEffectData))) {
      this.d = true;
    }
    if ((paramTuSdkMediaEffectData.getMediaEffectType() == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker) || (paramTuSdkMediaEffectData.getMediaEffectType() == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio))
    {
      this.a.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
      removeAllLiveSticker();
    }
  }
  
  public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    return this.a.mediaEffectsWithType(paramTuSdkMediaEffectDataType);
  }
  
  public List<TuSdkMediaEffectData> getAllMediaEffectData()
  {
    return this.a.getAllMediaEffectData();
  }
  
  public boolean removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (paramTuSdkMediaEffectData == null)
    {
      TLog.e("remove TuSdkMediaEffectData must be not null !!", new Object[0]);
      return false;
    }
    this.b.removeFilterWrap(paramTuSdkMediaEffectData.getFilterWrap());
    return this.a.removeMediaEffect(paramTuSdkMediaEffectData);
  }
  
  public void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    List localList = this.a.mediaEffectsWithType(paramTuSdkMediaEffectDataType);
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      this.b.removeFilterWrap(localTuSdkMediaEffectData.getFilterWrap());
    }
    this.a.removeMediaEffectsWithType(paramTuSdkMediaEffectDataType);
    if (paramTuSdkMediaEffectDataType == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker) {
      removeAllLiveSticker();
    }
  }
  
  public void removeAllMediaEffects()
  {
    removeAllLiveSticker();
    if (this.b != null) {
      this.b.removeAllFilterWrapNode();
    }
    this.a.removeAllMediaEffect();
    this.d = true;
  }
  
  public void updateEffectTimeLine(long paramLong, TuSDKMediaEffectsManager.OnFilterChangeListener paramOnFilterChangeListener)
  {
    TuSdkMediaEffectLinkedMap.TuSdkMediaEffectApply localTuSdkMediaEffectApply = this.a.seekTime(paramLong);
    List localList1 = localTuSdkMediaEffectApply.a;
    List localList2 = localTuSdkMediaEffectApply.b;
    Iterator localIterator = localList2.iterator();
    TuSdkMediaEffectData localTuSdkMediaEffectData;
    while (localIterator.hasNext())
    {
      localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      switch (1.a[localTuSdkMediaEffectData.getMediaEffectType().ordinal()])
      {
      case 1: 
        ((TuSdkMediaTileEffectDataBase)localTuSdkMediaEffectData).getStickerData().setEnabled(false);
        break;
      case 2: 
        ((TuSdkMediaTileEffectDataBase)localTuSdkMediaEffectData).getStickerData().setEnabled(false);
        break;
      case 3: 
        a((TuSdkMediaStickerEffectData)localTuSdkMediaEffectData, false);
        break;
      case 4: 
        ((TuSdkMediaParticleEffectData)localTuSdkMediaEffectData).resetParticleFilter();
        break;
      case 5: 
        a(((TuSdkMediaStickerAudioEffectData)localTuSdkMediaEffectData).getMediaStickerEffectData(), false);
        break;
      case 6: 
        localTuSdkMediaEffectData.setIsApplied(true);
      }
      if (localTuSdkMediaEffectData.isApplied())
      {
        this.b.removeFilterWrap(localTuSdkMediaEffectData.getFilterWrap());
        localTuSdkMediaEffectData.setIsApplied(false);
      }
    }
    localIterator = localList1.iterator();
    while (localIterator.hasNext())
    {
      localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      switch (1.a[localTuSdkMediaEffectData.getMediaEffectType().ordinal()])
      {
      case 4: 
        a((TuSdkMediaParticleEffectData)localTuSdkMediaEffectData, paramLong);
        break;
      case 1: 
        applyTextStickerData((TuSdkMediaTileEffectDataBase)localTuSdkMediaEffectData, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeText);
        break;
      case 2: 
        applyTextStickerData((TuSdkMediaTileEffectDataBase)localTuSdkMediaEffectData, TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediEffectDataTypeStickerImage);
        break;
      case 3: 
        a((TuSdkMediaStickerEffectData)localTuSdkMediaEffectData);
        break;
      case 7: 
        a((TuSdkMediaAudioEffectData)localTuSdkMediaEffectData);
        break;
      case 8: 
        a((TuSdkMediaFilterEffectData)localTuSdkMediaEffectData, paramOnFilterChangeListener);
        break;
      case 9: 
        a((TuSdkMediaSceneEffectData)localTuSdkMediaEffectData);
        break;
      case 10: 
        a((TuSdkMediaComicEffectData)localTuSdkMediaEffectData);
        break;
      case 11: 
        a((TuSdkMediaPlasticFaceEffect)localTuSdkMediaEffectData);
        break;
      case 12: 
        a((TuSdkMediaSkinFaceEffect)localTuSdkMediaEffectData);
        break;
      case 13: 
        a((TuSDKMediaMonsterFaceEffect)localTuSdkMediaEffectData);
        break;
      case 6: 
        a((TuSdkMediaTransitionEffectData)localTuSdkMediaEffectData);
      case 5: 
      default: 
        TLog.w("apply not find effect %s", new Object[] { localTuSdkMediaEffectData.getMediaEffectType() });
      }
    }
  }
  
  private void a()
  {
    this.b.removeAllFilterWrapNode();
    LinkedList localLinkedList = this.a.getApplyMediaEffectDataList(e);
    Iterator localIterator = localLinkedList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      if (localTuSdkMediaEffectData.isApplied()) {
        this.b.addFilterWrap(localTuSdkMediaEffectData.getFilterWrap());
      }
    }
  }
  
  private void a(TuSDKMediaEffectsManager.OnFilterChangeListener paramOnFilterChangeListener, TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (paramOnFilterChangeListener == null) {
      return;
    }
    paramOnFilterChangeListener.onFilterChanged(paramTuSdkMediaEffectData.getFilterWrap());
  }
  
  private void b(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (this.f == null) {
      return;
    }
    this.f.didApplyingMediaEffect(paramTuSdkMediaEffectData);
  }
  
  private void a(TuSdkMediaParticleEffectData paramTuSdkMediaParticleEffectData, long paramLong)
  {
    if (paramTuSdkMediaParticleEffectData == null) {
      return;
    }
    if (!paramTuSdkMediaParticleEffectData.isApplied())
    {
      paramTuSdkMediaParticleEffectData.setIsApplied(true);
      paramTuSdkMediaParticleEffectData.resetParticleFilter();
      paramTuSdkMediaParticleEffectData.getFilterWrap().setParticleSize(paramTuSdkMediaParticleEffectData.getSize());
      paramTuSdkMediaParticleEffectData.getFilterWrap().setParticleColor(paramTuSdkMediaParticleEffectData.getColor());
      a();
      b(paramTuSdkMediaParticleEffectData);
    }
    PointF localPointF = paramTuSdkMediaParticleEffectData.getPointF(paramLong);
    paramTuSdkMediaParticleEffectData.getFilterWrap().updateParticleEmitPosition(localPointF);
  }
  
  private void a(TuSdkMediaFilterEffectData paramTuSdkMediaFilterEffectData, TuSDKMediaEffectsManager.OnFilterChangeListener paramOnFilterChangeListener)
  {
    if ((paramTuSdkMediaFilterEffectData == null) || (paramTuSdkMediaFilterEffectData.isApplied())) {
      return;
    }
    paramTuSdkMediaFilterEffectData.setIsApplied(true);
    a();
    a(paramOnFilterChangeListener, paramTuSdkMediaFilterEffectData);
    b(paramTuSdkMediaFilterEffectData);
  }
  
  private void a(TuSdkMediaSceneEffectData paramTuSdkMediaSceneEffectData)
  {
    if ((paramTuSdkMediaSceneEffectData == null) || (paramTuSdkMediaSceneEffectData.isApplied())) {
      return;
    }
    paramTuSdkMediaSceneEffectData.setIsApplied(true);
    a();
    b(paramTuSdkMediaSceneEffectData);
  }
  
  private void a(TuSdkMediaComicEffectData paramTuSdkMediaComicEffectData)
  {
    if ((paramTuSdkMediaComicEffectData == null) || (paramTuSdkMediaComicEffectData.isApplied())) {
      return;
    }
    paramTuSdkMediaComicEffectData.setIsApplied(true);
    a();
    b(paramTuSdkMediaComicEffectData);
  }
  
  private void a(TuSdkMediaPlasticFaceEffect paramTuSdkMediaPlasticFaceEffect)
  {
    if ((paramTuSdkMediaPlasticFaceEffect == null) || (paramTuSdkMediaPlasticFaceEffect.isApplied())) {
      return;
    }
    paramTuSdkMediaPlasticFaceEffect.setIsApplied(true);
    a();
    b(paramTuSdkMediaPlasticFaceEffect);
  }
  
  private void a(TuSdkMediaSkinFaceEffect paramTuSdkMediaSkinFaceEffect)
  {
    if ((paramTuSdkMediaSkinFaceEffect == null) || (paramTuSdkMediaSkinFaceEffect.isApplied())) {
      return;
    }
    paramTuSdkMediaSkinFaceEffect.setIsApplied(true);
    a();
    b(paramTuSdkMediaSkinFaceEffect);
  }
  
  private void a(TuSDKMediaMonsterFaceEffect paramTuSDKMediaMonsterFaceEffect)
  {
    if ((paramTuSDKMediaMonsterFaceEffect == null) || (paramTuSDKMediaMonsterFaceEffect.isApplied())) {
      return;
    }
    paramTuSDKMediaMonsterFaceEffect.setIsApplied(true);
    a();
    b(paramTuSDKMediaMonsterFaceEffect);
  }
  
  private void a(TuSdkMediaTransitionEffectData paramTuSdkMediaTransitionEffectData)
  {
    if ((paramTuSdkMediaTransitionEffectData == null) || (paramTuSdkMediaTransitionEffectData.isApplied())) {
      return;
    }
    paramTuSdkMediaTransitionEffectData.setIsApplied(true);
    a();
    b(paramTuSdkMediaTransitionEffectData);
  }
  
  protected void applyTextStickerData(TuSdkMediaTileEffectDataBase paramTuSdkMediaTileEffectDataBase, TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    if ((paramTuSdkMediaTileEffectDataBase.getFilterWrap() == null) || (paramTuSdkMediaTileEffectDataBase.isApplied())) {
      return;
    }
    paramTuSdkMediaTileEffectDataBase.getStickerData().setEnabled(true);
    paramTuSdkMediaTileEffectDataBase.getStickerData().reset();
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.a.mediaEffectsWithType(paramTuSdkMediaEffectDataType).iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      TuSdkMediaTileEffectDataBase localTuSdkMediaTileEffectDataBase = (TuSdkMediaTileEffectDataBase)localTuSdkMediaEffectData;
      localArrayList.add(localTuSdkMediaTileEffectDataBase.getStickerData());
    }
    paramTuSdkMediaTileEffectDataBase.getFilterWrap().updateTileStickers(localArrayList);
    paramTuSdkMediaTileEffectDataBase.setIsApplied(true);
    a();
    b(paramTuSdkMediaTileEffectDataBase);
  }
  
  private void a(TuSdkMediaStickerEffectData paramTuSdkMediaStickerEffectData)
  {
    if (paramTuSdkMediaStickerEffectData == null) {
      return;
    }
    if (this.d)
    {
      showGroupSticker(paramTuSdkMediaStickerEffectData);
      this.d = false;
    }
    if (!paramTuSdkMediaStickerEffectData.isApplied())
    {
      a(paramTuSdkMediaStickerEffectData, true);
      paramTuSdkMediaStickerEffectData.setIsApplied(true);
      a();
      b(paramTuSdkMediaStickerEffectData);
    }
  }
  
  private void a(TuSdkMediaAudioEffectData paramTuSdkMediaAudioEffectData)
  {
    if ((paramTuSdkMediaAudioEffectData == null) || (!paramTuSdkMediaAudioEffectData.isVaild())) {
      return;
    }
    b(paramTuSdkMediaAudioEffectData);
  }
  
  @Deprecated
  public void removeAllLiveSticker()
  {
    if (this.c != null)
    {
      this.c.removeAllStickers();
      this.d = true;
    }
  }
  
  public void showGroupSticker(TuSdkMediaStickerEffectData paramTuSdkMediaStickerEffectData)
  {
    StickerGroup localStickerGroup = paramTuSdkMediaStickerEffectData.getStickerGroup();
    if ((localStickerGroup == null) || (localStickerGroup.stickers == null) || (localStickerGroup.categoryId != StickerCategory.StickerCategoryType.StickerCategorySmart.getValue()))
    {
      TLog.e("Only live sticker could be used here", new Object[0]);
      return;
    }
    if (localStickerGroup.stickers.size() > 5)
    {
      TLog.e("Too many live stickers in the group, please try to remove some stickers first.", new Object[0]);
      return;
    }
    if (this.c == null) {
      this.c = new LiveStickerPlayController(SelesContext.currentEGLContext());
    }
    this.c.showGroupSticker(localStickerGroup);
    b(paramTuSdkMediaStickerEffectData);
  }
  
  private void b(TuSdkMediaStickerEffectData paramTuSdkMediaStickerEffectData)
  {
    if ((this.c != null) && (this.b != null) && ((paramTuSdkMediaStickerEffectData.getFilterWrap() instanceof SelesParameters.FilterStickerInterface)))
    {
      paramTuSdkMediaStickerEffectData.getFilterWrap().updateStickers(this.c.getStickers());
      boolean bool = false;
      Iterator localIterator = this.a.mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker).iterator();
      while (localIterator.hasNext())
      {
        TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
        if (((localTuSdkMediaEffectData instanceof TuSdkMediaStickerEffectData)) && (localTuSdkMediaEffectData.isApplied()))
        {
          bool = true;
          break;
        }
      }
      a(paramTuSdkMediaStickerEffectData, bool);
    }
  }
  
  private void a(TuSdkMediaStickerEffectData paramTuSdkMediaStickerEffectData, boolean paramBoolean)
  {
    if ((this.c != null) && (this.b != null) && ((paramTuSdkMediaStickerEffectData.getFilterWrap() instanceof SelesParameters.FilterStickerInterface))) {
      paramTuSdkMediaStickerEffectData.getFilterWrap().setStickerVisibility(paramBoolean);
    }
  }
  
  public LiveStickerPlayController getLiveStickerPlayController()
  {
    return this.c;
  }
  
  public void release()
  {
    removeAllLiveSticker();
    removeAllMediaEffects();
    if (this.c != null)
    {
      this.c.destroy();
      this.c = null;
    }
    if (this.b != null)
    {
      this.b.destroy();
      this.b = null;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSDKMediaEffectsManagerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */