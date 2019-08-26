// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret;

//import org.lasque.tusdk.core.task.ImageViewTask;
//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.sticker.StickerPositionInfo;
//import org.lasque.tusdk.core.TuSdk;
import java.io.File;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import android.text.TextUtils;
//import org.lasque.tusdk.core.utils.StringHelper;
import android.graphics.Bitmap;
import android.widget.ImageView;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerText;
//import org.lasque.tusdk.core.utils.json.JsonWrapper;
//import org.lasque.tusdk.core.TuSdkBundle;
//import org.lasque.tusdk.core.utils.TLog;
import android.os.Handler;
import android.os.Looper;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkBundle;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadTask;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.StickerPositionInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.ImageViewTask;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.ImageViewTaskWare;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonWrapper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerText;

import java.util.Collection;
//import org.lasque.tusdk.core.network.TuSdkDownloadTask;
import java.util.Iterator;
import java.util.ArrayList;
//import org.lasque.tusdk.core.TuSdkConfigs;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import java.util.Hashtable;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import java.util.List;
//import org.lasque.tusdk.core.task.ImageViewTaskWare;
//import org.lasque.tusdk.core.network.TuSdkDownloadAdapter;

public class StickerAdapter extends TuSdkDownloadAdapter<ImageViewTaskWare>
{
    private List<StickerCategory> a;
    private Hashtable<Long, StickerGroup> b;
    private List<StickerGroup> c;
    private Hashtable<Long, StickerData> d;
    private TuSdkConfigs e;
    private boolean f;
    private int g;
    
    public boolean isInited() {
        return this.f;
    }
    
    public List<StickerCategory> getCategories() {
        if (this.a != null) {
            final ArrayList<StickerCategory> list = new ArrayList<StickerCategory>(this.a.size());
            final Iterator<StickerCategory> iterator = this.a.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next().copy());
            }
            return list;
        }
        return null;
    }
    
    public List<StickerCategory> getCategories(final List<StickerCategory> list) {
        if (list == null || list.isEmpty()) {
            return this.getCategories();
        }
        final ArrayList<StickerCategory> list2 = new ArrayList<StickerCategory>(list.size());
        final Iterator<StickerCategory> iterator = list.iterator();
        while (iterator.hasNext()) {
            final StickerCategory category = this.getCategory(iterator.next().id);
            if (category != null) {
                list2.add(category);
            }
        }
        return list2;
    }
    
    public List<StickerGroup> getGroupListByType(final StickerCategory.StickerCategoryType stickerCategoryType) {
        if (stickerCategoryType == StickerCategory.StickerCategoryType.StickerCategorySmart) {
            return this.c;
        }
        return null;
    }
    
    public StickerCategory getCategory(final long n) {
        for (final StickerCategory stickerCategory : this.a) {
            if (stickerCategory.id == n) {
                return stickerCategory.copy();
            }
        }
        return null;
    }
    
    public StickerData getSticker(final long l) {
        final StickerData stickerData = this.d.get(l);
        if (stickerData != null) {
            return stickerData.copy();
        }
        return null;
    }
    
    public StickerGroup getStickerGroup(final long l) {
        return this.b.get(l);
    }
    
    public StickerAdapter(final TuSdkConfigs e) {
        this.e = e;
        this.setDownloadTaskType(TuSdkDownloadTask.DownloadTaskType.TypeSticker);
        this.a();
    }
    
    private void a() {
        this.f = false;
        this.g = 0;
        this.a = new ArrayList<StickerCategory>();
        this.b = new Hashtable<Long, StickerGroup>();
        this.c = new ArrayList<StickerGroup>();
        this.d = new Hashtable<Long, StickerData>();
        if (this.e != null && this.e.stickerCategories != null) {
            final Iterator<StickerCategory> iterator = this.e.stickerCategories.iterator();
            while (iterator.hasNext()) {
                this.a.add(iterator.next().copy());
            }
        }
        this.tryLoadTaskDataWithCache();
        new Thread(new Runnable() {
            @Override
            public void run() {
                StickerAdapter.this.asyncLoadLocalStickers();
            }
        }).start();
    }
    
    protected void asyncLoadLocalStickers() {
        this.asyncLoadDownloadDatas();
        if (this.e != null && this.e.stickerGroups != null) {
            final Iterator<StickerGroup> iterator = new ArrayList<StickerGroup>(this.e.stickerGroups).iterator();
            while (iterator.hasNext()) {
                this.a(iterator.next());
            }
        }
        new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
            @Override
            public void run() {
                StickerAdapter.this.f = true;
                TLog.d("StickerAdapter inited: %s", StickerAdapter.this.g);
            }
        });
    }
    
    private void a(final StickerGroup stickerGroup) {
        if (stickerGroup.file == null) {
            return;
        }
        final String loadStickerGroup = SdkValid.shared.loadStickerGroup(TuSdkBundle.sdkBundleSticker(stickerGroup.file), null);
        if (loadStickerGroup == null) {
            return;
        }
        final StickerGroup value = JsonWrapper.deserialize(loadStickerGroup, StickerGroup.class);
        if (value == null || this.b.containsKey(value.groupId)) {
            return;
        }
        if (value.stickers == null || value.stickers.isEmpty()) {
            return;
        }
        for (final StickerCategory stickerCategory : this.a) {
            if (stickerCategory.id != value.categoryId) {
                continue;
            }
            stickerCategory.append(value);
            break;
        }
        this.a(value, stickerGroup.file);
        this.b.put(value.groupId, value);
        this.a(stickerGroup, value);
    }
    
    private void a(final StickerGroup stickerGroup, final StickerGroup stickerGroup2) {
        stickerGroup2.validKey = stickerGroup.validKey;
        stickerGroup2.file = stickerGroup.file;
        if (stickerGroup.name != null) {
            stickerGroup2.name = stickerGroup.name;
        }
        if (stickerGroup.stickers == null || stickerGroup.stickers.isEmpty()) {
            for (final StickerData value : stickerGroup2.stickers) {
                this.d.put(value.stickerId, value);
                ++this.g;
            }
            return;
        }
        final ArrayList<StickerData> stickers = new ArrayList<StickerData>(stickerGroup.stickers.size());
        for (final StickerData stickerData : stickerGroup.stickers) {
            final StickerData sticker = stickerGroup2.getSticker(stickerData.stickerId);
            if (sticker == null) {
                continue;
            }
            stickers.add(sticker);
            this.d.put(sticker.stickerId, sticker);
            this.a(stickerData, sticker);
            ++this.g;
        }
        stickerGroup2.stickers = stickers;
    }
    
    private void a(final StickerData stickerData, final StickerData stickerData2) {
        if (stickerData.width > 0) {
            stickerData2.width = stickerData.width;
        }
        if (stickerData.height > 0) {
            stickerData2.height = stickerData.height;
        }
        if (stickerData.texts == null || stickerData.texts.isEmpty() || stickerData2.texts == null || stickerData2.texts.isEmpty()) {
            return;
        }
        for (final StickerText stickerText : stickerData2.texts) {
            final StickerText stickerText2 = stickerData.getStickerText(stickerText.textId);
            if (stickerText2 == null) {
                continue;
            }
            if (stickerText2.content != null) {
                stickerText.content = stickerText2.content;
            }
            if (stickerText2.color == null) {
                continue;
            }
            stickerText.color = stickerText2.color;
        }
    }
    
    public void loadThumb(final StickerData stickerData, final ImageView imageView) {
        if (stickerData == null || imageView == null) {
            return;
        }
        final StickerData stickerData2 = this.d.get(stickerData.stickerId);
        if (stickerData2 == null) {
            return;
        }
        (this).loadImage(new StickerThumbTaskImageWare(imageView, stickerData2));
    }
    
    public void loadGroupThumb(final StickerGroup stickerGroup, final ImageView imageView) {
        if (stickerGroup == null || imageView == null) {
            return;
        }
        final StickerGroup stickerGroup2 = this.b.get(stickerGroup.groupId);
        if (stickerGroup2 == null) {
            return;
        }
        (this).loadImage(new StickerGroupThumbTaskImageWare(imageView, stickerGroup2));
    }
    
    public boolean loadStickerItem(final StickerData stickerData) {
        if (stickerData == null) {
            return false;
        }
        stickerData.setImage(null);
        final StickerGroup stickerGroup = this.b.get(stickerData.groupId);
        if (stickerGroup == null) {
            return false;
        }
        final StickerData sticker = stickerGroup.getSticker(stickerData.stickerId);
        if (sticker == null) {
            return false;
        }
        final Bitmap sticker2 = SdkValid.shared.readSticker(sticker.groupId, sticker.stickerImageName);
        if (sticker2 == null) {
            return false;
        }
        stickerData.setImage(sticker2);
        StatisticsManger.appendSticker(stickerData);
        return true;
    }
    
    public Bitmap loadSmartStickerItem(final StickerData stickerData, final String s) {
        if (stickerData == null || StringHelper.isBlank(s)) {
            return null;
        }
        final StickerGroup stickerGroup = this.b.get(stickerData.groupId);
        if (stickerGroup == null) {
            return null;
        }
        final StickerData sticker = stickerGroup.getSticker(stickerData.stickerId);
        if (sticker == null) {
            return null;
        }
        return SdkValid.shared.readSticker(sticker.groupId, s);
    }
    
    @Override
    protected String getCacheKey(final ImageViewTaskWare imageViewTaskWare) {
        if (imageViewTaskWare == null) {
            return null;
        }
        if (imageViewTaskWare instanceof StickerGroupThumbTaskImageWare) {
            return ((StickerGroupThumbTaskImageWare)imageViewTaskWare).data.previewName;
        }
        final StickerData data = ((StickerThumbTaskImageWare)imageViewTaskWare).data;
        return TextUtils.isEmpty((CharSequence)data.previewName) ? data.stickerImageName : data.previewName;
    }
    
    @Override
    protected Bitmap asyncTaskLoadImage(final ImageViewTaskWare imageViewTaskWare) {
        Bitmap bitmap = null;
        if (imageViewTaskWare instanceof StickerGroupThumbTaskImageWare) {
            final StickerGroupThumbTaskImageWare stickerGroupThumbTaskImageWare = (StickerGroupThumbTaskImageWare)imageViewTaskWare;
            if (stickerGroupThumbTaskImageWare != null) {
                bitmap = SdkValid.shared.readSticker(stickerGroupThumbTaskImageWare.data.groupId, stickerGroupThumbTaskImageWare.data.previewName);
            }
        }
        else {
            final StickerThumbTaskImageWare stickerThumbTaskImageWare = (StickerThumbTaskImageWare)imageViewTaskWare;
            if (stickerThumbTaskImageWare != null) {
                bitmap = SdkValid.shared.readStickerThumb(stickerThumbTaskImageWare.data.groupId, stickerThumbTaskImageWare.data.stickerId);
            }
        }
        return bitmap;
    }
    
    @Override
    public boolean containsGroupId(final long l) {
        return this.b.containsKey(l);
    }
    
    @Override
    protected void removeDownloadData(final long l) {
        final StickerGroup stickerGroup = this.b.get(l);
        for (final StickerCategory stickerCategory : new ArrayList<StickerCategory>(this.a)) {
            if (stickerCategory.id != stickerGroup.categoryId) {
                continue;
            }
            stickerCategory.removeGroup(l);
            break;
        }
        this.b.remove(l);
        if (stickerGroup.stickers != null) {
            final Iterator<StickerData> iterator2 = stickerGroup.stickers.iterator();
            while (iterator2.hasNext()) {
                this.d.remove(iterator2.next().stickerId);
                --this.g;
            }
        }
        SdkValid.shared.removeStickerGroup(l);
        TLog.d("remove download stickers [%s]: %s | %s", l, this.g, this.d.size());
    }
    
    @Override
    protected boolean appendDownload(final TuSdkDownloadItem tuSdkDownloadItem) {
        if (!super.appendDownload(tuSdkDownloadItem)) {
            return false;
        }
        final String loadStickerGroup = SdkValid.shared.loadStickerGroup(tuSdkDownloadItem.localDownloadPath().getAbsolutePath(), tuSdkDownloadItem.key);
        if (loadStickerGroup == null) {
            return false;
        }
        final StickerGroup stickerGroup = JsonWrapper.deserialize(loadStickerGroup, StickerGroup.class);
        if (stickerGroup == null) {
            return false;
        }
        if (stickerGroup.stickers == null || stickerGroup.stickers.isEmpty()) {
            return false;
        }
        stickerGroup.file = tuSdkDownloadItem.localDownloadPath().getAbsolutePath();
        stickerGroup.isDownload = true;
        this.b(stickerGroup);
        return true;
    }
    
    public boolean addStickerGroupFile(final File file, final long n, final String s) {
        if (file == null || !file.exists() || file.isDirectory()) {
            TLog.e("sticker file does not exist", new Object[0]);
            return false;
        }
        if (s == null || TextUtils.isEmpty((CharSequence)s)) {
            TLog.e("Please enter a valid master", new Object[0]);
            return false;
        }
        if (this.containsGroupId(n)) {
            return false;
        }
        final String loadStickerGroup = SdkValid.shared.loadStickerGroup(file.getAbsolutePath(), SdkValid.shared.stickerGroupValidKey(n, s));
        if (loadStickerGroup == null) {
            return false;
        }
        final StickerGroup stickerGroup = JsonWrapper.deserialize(loadStickerGroup, StickerGroup.class);
        if (stickerGroup == null) {
            return false;
        }
        if (stickerGroup.stickers == null || stickerGroup.stickers.isEmpty()) {
            return false;
        }
        stickerGroup.file = file.getAbsolutePath();
        stickerGroup.isDownload = true;
        this.b(stickerGroup);
        return true;
    }
    
    private boolean b(final StickerGroup value) {
        if (value == null || this.containsGroupId(value.groupId)) {
            return false;
        }
        for (final StickerCategory stickerCategory : this.a) {
            if (stickerCategory.id != value.categoryId) {
                continue;
            }
            stickerCategory.insertFirst(value);
            break;
        }
        this.a(value, value.file);
        this.b.put(value.groupId, value);
        for (final StickerData value2 : value.stickers) {
            this.d.put(value2.stickerId, value2);
            ++this.g;
        }
        return true;
    }
    
    @Override
    protected Collection<?> getAllGroupID() {
        return this.b.keySet();
    }
    
    private void a(final StickerGroup stickerGroup, final String s) {
        if (stickerGroup.categoryId != StickerCategory.StickerCategoryType.StickerCategorySmart.getValue()) {
            return;
        }
        this.c.add(stickerGroup);
        final int lastIndex = s.lastIndexOf(".");
        if (lastIndex < 0) {
            return;
        }
        final String string = TuSdk.getAppTempPath() + File.separator + s.substring(0, lastIndex);
        if (!new File(string).isDirectory()) {
            return;
        }
        for (final StickerData stickerData : stickerGroup.stickers) {
            final File file = new File(string + File.separator + stickerData.stickerId + ".json");
            if (!file.exists()) {
                continue;
            }
            stickerData.positionInfo = JsonWrapper.deserialize(new String(FileHelper.readFile(file)), StickerPositionInfo.class);
        }
    }
    
    public static class StickerGroupThumbTaskImageWare extends ImageViewTaskWare
    {
        public StickerGroup data;
        
        public StickerGroupThumbTaskImageWare(final ImageView imageView, final StickerGroup data) {
            this.setImageView(imageView);
            this.data = data;
            this.setImageCompress(0);
        }
    }
    
    public static class StickerThumbTaskImageWare extends ImageViewTaskWare
    {
        public StickerData data;
        
        public StickerThumbTaskImageWare(final ImageView imageView, final StickerData data) {
            this.setImageView(imageView);
            this.data = data;
        }
    }
}
