// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.LinkedList;
import java.util.Map;
import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.SdkValid;
import java.util.ArrayList;

public class TuSDKMediaEffectsDataManager
{
    private TuSdkMediaEffectLinkedMap a;
    private TuSDKMediaEffectsManagerDelegate b;
    
    public TuSDKMediaEffectsDataManager() {
        this.a = new TuSdkMediaEffectLinkedMap();
    }
    
    public boolean addMediaEffect(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        final ArrayList<TuSdkMediaEffectData.TuSdkMediaEffectDataType> list = new ArrayList<TuSdkMediaEffectData.TuSdkMediaEffectDataType>();
        switch (tuSdkMediaEffectData.getMediaEffectType().ordinal()) {
            case 1: {
                if (!SdkValid.shared.videoEditorComicEffectsSupport()) {
                    TLog.e("You are not allowed to use conmic effect, please see http://tusdk.com", new Object[0]);
                    return false;
                }
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter);
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic);
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic);
                break;
            }
            case 2: {
                if ((this.a).get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter).contains(tuSdkMediaEffectData)) {
                    break;
                }
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic);
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter);
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter);
                break;
            }
            case 3: {
                if (!this.a()) {
                    return false;
                }
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
                break;
            }
            case 4: {
                if (!this.a(tuSdkMediaEffectData)) {
                    return false;
                }
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
                break;
            }
            case 5: {
                if (!SdkValid.shared.videoEditorParticleEffectsFilterEnabled()) {
                    TLog.e("You are not allowed to use editor particle effect, please see http://tusdk.com", new Object[0]);
                    return false;
                }
                if ((this.a).get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle).contains(tuSdkMediaEffectData)) {
                    break;
                }
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle);
                break;
            }
            case 6: {
                if (!SdkValid.shared.videoEditorEffectsfilterEnabled()) {
                    TLog.e("You are not allowed to use editor scene effect, please see http://tusdk.com", new Object[0]);
                    return false;
                }
                if ((this.a).get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene).contains(tuSdkMediaEffectData)) {
                    break;
                }
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene);
                break;
            }
            case 7: {
                if (!SdkValid.shared.videoEditorMusicEnabled()) {
                    TLog.e("You are not allowed to use editor music, please see http://tusdk.com", new Object[0]);
                    return false;
                }
                if (!SdkValid.shared.videoEditorStickerEnabled()) {
                    TLog.e("You are not allowed to use editor sticker, please see http://tusdk.com", new Object[0]);
                    return false;
                }
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio);
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
                if (!this.a(tuSdkMediaEffectData)) {
                    return false;
                }
                if (!this.a()) {
                    return false;
                }
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker, ((TuSdkMediaStickerAudioEffectData)tuSdkMediaEffectData).getMediaStickerEffectData());
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio);
                break;
            }
            case 8: {
                if (!SdkValid.shared.videoEditorTextEffectsEnabled()) {
                    TLog.e("You are not allowed to use editor text, please see http://tusdk.com", new Object[0]);
                    return false;
                }
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeText, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeText);
                break;
            }
            case 9: {
                if (!SdkValid.shared.videoEditorTextEffectsEnabled()) {
                    TLog.e("You are not allowed to use editor text, please see http://tusdk.com", new Object[0]);
                    return false;
                }
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediEffectDataTypeStickerImage, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediEffectDataTypeStickerImage);
                break;
            }
            case 10: {
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace);
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace);
                break;
            }
            case 11: {
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSkinFace);
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSkinFace, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSkinFace);
                break;
            }
            case 12: {
                if (!SdkValid.shared.videoEditorMonsterFaceSupport()) {
                    TLog.e("You are not allowed to use monster face effect , please see https://tutucloud.com", new Object[0]);
                    return false;
                }
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace);
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio);
                this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
                break;
            }
            case 13: {
                if (!SdkValid.shared.videoEditorTransitionEffectsSupport()) {
                    TLog.e("You are not allowed to use editor transition effect, please see http://tusdk.com", new Object[0]);
                    return false;
                }
                if ((this.a).get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeTransition).contains(tuSdkMediaEffectData)) {
                    break;
                }
                this.a.putMediaEffectData(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeTransition, tuSdkMediaEffectData);
                list.add(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeTransition);
                break;
            }
            default: {
                TLog.e("unkwon MediaEffectDataType %s", new Object[] { tuSdkMediaEffectData.getMediaEffectType() });
                return false;
            }
        }
        if (list.size() > 0 && this.b != null) {
            this.b.mediaEffectsManager(this, list);
        }
        return true;
    }
    
    private boolean a() {
        if (!SdkValid.shared.videoEditorMusicEnabled()) {
            TLog.e("You are not allowed to use editor music, please see http://tusdk.com", new Object[0]);
            return false;
        }
        this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
        this.a.clearByType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
        return true;
    }
    
    private boolean a(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        if (!SdkValid.shared.videoEditorStickerEnabled()) {
            TLog.e("You are not allowed to use editor sticker, please see http://tusdk.com", new Object[0]);
            return false;
        }
        if ((this.a).get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker).contains(tuSdkMediaEffectData)) {
            return false;
        }
        this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
        this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
        return true;
    }
    
    public boolean removeMediaEffect(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        final boolean deleteMediaEffectData = this.a.deleteMediaEffectData(tuSdkMediaEffectData.getMediaEffectType(), tuSdkMediaEffectData);
        tuSdkMediaEffectData.setIsApplied(false);
        if (this.b != null) {
            this.b.mediaEffectsManager(this, (ArrayList)Arrays.asList(tuSdkMediaEffectData.getMediaEffectType()));
        }
        return deleteMediaEffectData;
    }
    
    public void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType key) {
        final Iterator<TuSdkMediaEffectData> iterator = (this.a).get(key).iterator();
        while (iterator.hasNext()) {
            iterator.next().setIsApplied(false);
        }
        this.a.clearByType(key);
        if (this.b != null) {
            this.b.mediaEffectsManager(this, (ArrayList)Arrays.asList(key));
        }
    }
    
    public void removeAllMediaEffect() {
        this.resetAllMediaEffects();
        final ArrayList<TuSdkMediaEffectData.TuSdkMediaEffectDataType> list = new ArrayList<TuSdkMediaEffectData.TuSdkMediaEffectDataType>();
        for (final Map.Entry<TuSdkMediaEffectData.TuSdkMediaEffectDataType, List<TuSdkMediaEffectData>> entry : this.a.entrySet()) {
            final TuSdkMediaEffectData.TuSdkMediaEffectDataType e = entry.getKey();
            final List<TuSdkMediaEffectData> list2 = entry.getValue();
            list.add(e);
            final Iterator<TuSdkMediaEffectData> iterator2 = list2.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setIsApplied(false);
            }
            list2.clear();
            this.a.clearByType(e);
        }
        if (this.b != null) {
            this.b.mediaEffectsManager(this, list);
        }
    }
    
    public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType key) {
        return (List<T>)(this.a).get(key);
    }
    
    public LinkedList<TuSdkMediaEffectData> getApplyMediaEffectDataList(final TuSdkMediaEffectData.TuSdkMediaEffectDataType[] array) {
        return this.a.getApplyMediaEffectDataList(array);
    }
    
    public void resetAllMediaEffects() {
        this.a.resetMediaEffects();
    }
    
    public TuSdkMediaEffectLinkedMap.TuSdkMediaEffectApply seekTime(final long n) {
        return this.a.seekTimeUs(n);
    }
    
    public List<TuSdkMediaEffectData> getAllMediaEffectData() {
        return this.a.getAllMediaEffectData();
    }
    
    public boolean hasMediaAudioEffects() {
        return (this.a).get(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio).size() > 0;
    }
    
    public void setManagerDelegate(final TuSDKMediaEffectsManagerDelegate b) {
        this.b = b;
    }
    
    public interface TuSDKMediaEffectsManagerDelegate
    {
        void mediaEffectsManager(final TuSDKMediaEffectsDataManager p0, final ArrayList<TuSdkMediaEffectData.TuSdkMediaEffectDataType> p1);
    }
}
