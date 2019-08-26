package org.lasque.tusdk.video.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.utils.TLog;

public class TuSDKMediaEffectsDataManager
{
  private TuSdkMediaEffectLinkedMap a = new TuSdkMediaEffectLinkedMap();
  private TuSDKMediaEffectsManagerDelegate b;
  
  public boolean addMediaEffect(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    ArrayList localArrayList = new ArrayList();
    switch (1.a[paramTuSdkMediaEffectData.getMediaEffectType().ordinal()])
    {
    case 1: 
      if (!SdkValid.shared.videoEditorComicEffectsSupport())
      {
        TLog.e("You are not allowed to use conmic effect, please see http://tusdk.com", new Object[0]);
        return false;
      }
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter);
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic);
      this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic, paramTuSdkMediaEffectData);
      localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic);
      break;
    case 2: 
      if (!((List)this.a.get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter)).contains(paramTuSdkMediaEffectData))
      {
        removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic);
        removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter);
        this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter, paramTuSdkMediaEffectData);
        localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter);
      }
      break;
    case 3: 
      if (!a()) {
        return false;
      }
      this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio, paramTuSdkMediaEffectData);
      localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
      break;
    case 4: 
      if (!a(paramTuSdkMediaEffectData)) {
        return false;
      }
      this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker, paramTuSdkMediaEffectData);
      localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
      break;
    case 5: 
      if (!SdkValid.shared.videoEditorParticleEffectsFilterEnabled())
      {
        TLog.e("You are not allowed to use editor particle effect, please see http://tusdk.com", new Object[0]);
        return false;
      }
      if (!((List)this.a.get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle)).contains(paramTuSdkMediaEffectData))
      {
        this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle, paramTuSdkMediaEffectData);
        localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle);
      }
      break;
    case 6: 
      if (!SdkValid.shared.videoEditorEffectsfilterEnabled())
      {
        TLog.e("You are not allowed to use editor scene effect, please see http://tusdk.com", new Object[0]);
        return false;
      }
      if (!((List)this.a.get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene)).contains(paramTuSdkMediaEffectData))
      {
        this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene, paramTuSdkMediaEffectData);
        localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene);
      }
      break;
    case 7: 
      if (!SdkValid.shared.videoEditorMusicEnabled())
      {
        TLog.e("You are not allowed to use editor music, please see http://tusdk.com", new Object[0]);
        return false;
      }
      if (!SdkValid.shared.videoEditorStickerEnabled())
      {
        TLog.e("You are not allowed to use editor sticker, please see http://tusdk.com", new Object[0]);
        return false;
      }
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio);
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
      if (!a(paramTuSdkMediaEffectData)) {
        return false;
      }
      if (!a()) {
        return false;
      }
      this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker, ((TuSdkMediaStickerAudioEffectData)paramTuSdkMediaEffectData).getMediaStickerEffectData());
      localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio);
      break;
    case 8: 
      if (!SdkValid.shared.videoEditorTextEffectsEnabled())
      {
        TLog.e("You are not allowed to use editor text, please see http://tusdk.com", new Object[0]);
        return false;
      }
      this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeText, paramTuSdkMediaEffectData);
      localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeText);
      break;
    case 9: 
      if (!SdkValid.shared.videoEditorTextEffectsEnabled())
      {
        TLog.e("You are not allowed to use editor text, please see http://tusdk.com", new Object[0]);
        return false;
      }
      this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediEffectDataTypeStickerImage, paramTuSdkMediaEffectData);
      localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediEffectDataTypeStickerImage);
      break;
    case 10: 
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace);
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
      this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace, paramTuSdkMediaEffectData);
      localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace);
      break;
    case 11: 
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSkinFace);
      this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSkinFace, paramTuSdkMediaEffectData);
      localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSkinFace);
      break;
    case 12: 
      if (!SdkValid.shared.videoEditorMonsterFaceSupport())
      {
        TLog.e("You are not allowed to use monster face effect , please see https://tutucloud.com", new Object[0]);
        return false;
      }
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace);
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio);
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
      this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace, paramTuSdkMediaEffectData);
      localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
      break;
    case 13: 
      if (!SdkValid.shared.videoEditorTransitionEffectsSupport())
      {
        TLog.e("You are not allowed to use editor transition effect, please see http://tusdk.com", new Object[0]);
        return false;
      }
      if (!((List)this.a.get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeTransition)).contains(paramTuSdkMediaEffectData))
      {
        this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeTransition, paramTuSdkMediaEffectData);
        localArrayList.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeTransition);
      }
      break;
    default: 
      TLog.e("unkwon MediaEffectDataType %s", new Object[] { paramTuSdkMediaEffectData.getMediaEffectType() });
      return false;
    }
    if ((localArrayList.size() > 0) && (this.b != null)) {
      this.b.mediaEffectsManager(this, localArrayList);
    }
    return true;
  }
  
  private boolean a()
  {
    if (!SdkValid.shared.videoEditorMusicEnabled())
    {
      TLog.e("You are not allowed to use editor music, please see http://tusdk.com", new Object[0]);
      return false;
    }
    removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
    this.a.clearByType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
    return true;
  }
  
  private boolean a(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (!SdkValid.shared.videoEditorStickerEnabled())
    {
      TLog.e("You are not allowed to use editor sticker, please see http://tusdk.com", new Object[0]);
      return false;
    }
    if (((List)this.a.get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker)).contains(paramTuSdkMediaEffectData)) {
      return false;
    }
    removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
    removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
    return true;
  }
  
  public boolean removeMediaEffect(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    boolean bool = this.a.deleteMediaEffectData(paramTuSdkMediaEffectData.getMediaEffectType(), paramTuSdkMediaEffectData);
    paramTuSdkMediaEffectData.setIsApplied(false);
    if (this.b != null) {
      this.b.mediaEffectsManager(this, (ArrayList)Arrays.asList(new TuSdkMediaEffectData.TuSdkMediaEffectDataType[] { paramTuSdkMediaEffectData.getMediaEffectType() }));
    }
    return bool;
  }
  
  public void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    List localList = (List)this.a.get(paramTuSdkMediaEffectDataType);
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      localTuSdkMediaEffectData.setIsApplied(false);
    }
    this.a.clearByType(paramTuSdkMediaEffectDataType);
    if (this.b != null) {
      this.b.mediaEffectsManager(this, (ArrayList)Arrays.asList(new TuSdkMediaEffectData.TuSdkMediaEffectDataType[] { paramTuSdkMediaEffectDataType }));
    }
  }
  
  public void removeAllMediaEffect()
  {
    resetAllMediaEffects();
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator1 = this.a.entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      TuSdkMediaEffectData.TuSdkMediaEffectDataType localTuSdkMediaEffectDataType = (TuSdkMediaEffectData.TuSdkMediaEffectDataType)localEntry.getKey();
      List localList = (List)localEntry.getValue();
      localArrayList.add(localTuSdkMediaEffectDataType);
      Iterator localIterator2 = localList.iterator();
      while (localIterator2.hasNext())
      {
        TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator2.next();
        localTuSdkMediaEffectData.setIsApplied(false);
      }
      localList.clear();
      this.a.clearByType(localTuSdkMediaEffectDataType);
    }
    if (this.b != null) {
      this.b.mediaEffectsManager(this, localArrayList);
    }
  }
  
  public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    return (List)this.a.get(paramTuSdkMediaEffectDataType);
  }
  
  public LinkedList<TuSdkMediaEffectData> getApplyMediaEffectDataList(TuSdkMediaEffectData.TuSdkMediaEffectDataType[] paramArrayOfTuSdkMediaEffectDataType)
  {
    return this.a.getApplyMediaEffectDataList(paramArrayOfTuSdkMediaEffectDataType);
  }
  
  public void resetAllMediaEffects()
  {
    this.a.resetMediaEffects();
  }
  
  public TuSdkMediaEffectLinkedMap.TuSdkMediaEffectApply seekTime(long paramLong)
  {
    return this.a.seekTimeUs(paramLong);
  }
  
  public List<TuSdkMediaEffectData> getAllMediaEffectData()
  {
    return this.a.getAllMediaEffectData();
  }
  
  public boolean hasMediaAudioEffects()
  {
    return ((List)this.a.get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio)).size() > 0;
  }
  
  public void setManagerDelegate(TuSDKMediaEffectsManagerDelegate paramTuSDKMediaEffectsManagerDelegate)
  {
    this.b = paramTuSDKMediaEffectsManagerDelegate;
  }
  
  public static abstract interface TuSDKMediaEffectsManagerDelegate
  {
    public abstract void mediaEffectsManager(TuSDKMediaEffectsDataManager paramTuSDKMediaEffectsDataManager, ArrayList<TuSdkMediaEffectData.TuSdkMediaEffectDataType> paramArrayList);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSDKMediaEffectsDataManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */