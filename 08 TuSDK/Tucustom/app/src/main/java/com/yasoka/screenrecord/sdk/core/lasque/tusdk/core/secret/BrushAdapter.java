// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret;

//import org.lasque.tusdk.core.task.ImageViewTaskWare;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import android.text.TextUtils;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.TuSdkContext;
import android.widget.ImageView;
import java.util.Map;
//import org.lasque.tusdk.core.utils.json.JsonWrapper;
//import org.lasque.tusdk.core.TuSdkBundle;
//import org.lasque.tusdk.core.utils.TLog;
import android.os.Handler;
import android.os.Looper;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkBundle;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadTask;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.ImageViewTaskWare;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonWrapper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushGroup;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
//import org.lasque.tusdk.core.network.TuSdkDownloadTask;
//import org.lasque.tusdk.core.TuSdkConfigs;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushGroup;
import java.util.ArrayList;
//import org.lasque.tusdk.core.network.TuSdkDownloadAdapter;

public class BrushAdapter extends TuSdkDownloadAdapter<BrushAdapter.BrushThumbTaskImageWare>
{
    public static final String EraserBrushCode = "Eraser";
    private ArrayList<BrushGroup> a;
    private ArrayList<BrushData> b;
    private TuSdkConfigs c;
    private boolean d;
    private int e;
    
    public BrushAdapter(final TuSdkConfigs c) {
        this.c = c;
        this.setDownloadTaskType(TuSdkDownloadTask.DownloadTaskType.TypeBrush);
        if (SdkValid.shared.smudgeEnabled()) {
            this.a();
        }
    }
    
    public boolean isInited() {
        return this.d;
    }
    
    public List<String> getCodes() {
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator<BrushData> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().code);
        }
        return list;
    }
    
    public BrushData getEraserBrush() {
        final BrushData brushData = new BrushData();
        brushData.setType(BrushData.BrushType.TypeEraser);
        brushData.code = "Eraser";
        return brushData;
    }
    
    public BrushData getBrushWithCode(final String anotherString) {
        for (final BrushData brushData : this.b) {
            if (brushData.code.equalsIgnoreCase(anotherString)) {
                return brushData;
            }
        }
        return null;
    }
    
    public List<String> verifyCodes(final List<String> list) {
        if (!this.d || list == null || this.b == null || !SdkValid.shared.sdkValid()) {
            return null;
        }
        final ArrayList<String> list2 = new ArrayList<String>(list.size());
        for (final String e : list) {
            if (this.getBrushWithCode(e) != null) {
                list2.add(e);
            }
        }
        return list2;
    }
    
    public List<BrushData> getBrushWithCodes(final List<String> list) {
        if (!this.d || list == null || this.b == null || !SdkValid.shared.sdkValid()) {
            return null;
        }
        final ArrayList<BrushData> list2 = new ArrayList<BrushData>();
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            final BrushData brushWithCode = this.getBrushWithCode(iterator.next());
            if (brushWithCode != null) {
                list2.add(brushWithCode);
            }
        }
        return list2;
    }
    
    private void a() {
        this.d = false;
        this.e = 0;
        this.a = new ArrayList<BrushGroup>();
        this.b = new ArrayList<BrushData>();
        this.tryLoadTaskDataWithCache();
        new Thread(new Runnable() {
            @Override
            public void run() {
                BrushAdapter.this.asyncLoadLocalBrushes();
            }
        }).start();
    }
    
    protected void asyncLoadLocalBrushes() {
        this.asyncLoadDownloadDatas();
        if (this.c != null && this.c.brushGroups != null) {
            final Iterator<BrushGroup> iterator = new ArrayList<BrushGroup>(this.c.brushGroups).iterator();
            while (iterator.hasNext()) {
                this.a(iterator.next());
            }
        }
        new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
            @Override
            public void run() {
                BrushAdapter.this.d = true;
                TLog.d("BrushAdapter inited: %s", BrushAdapter.this.e);
            }
        });
    }
    
    private void a(final BrushGroup brushGroup) {
        if (brushGroup.file == null) {
            return;
        }
        final String loadBrushGroup = SdkValid.shared.loadBrushGroup(TuSdkBundle.sdkBundleBrush(brushGroup.file), null);
        if (loadBrushGroup == null) {
            return;
        }
        final BrushGroup e = JsonWrapper.deserialize(loadBrushGroup, BrushGroup.class);
        if (e == null || e.brushes == null || e.brushes.isEmpty()) {
            return;
        }
        this.a.add(e);
        this.a(brushGroup, e);
    }
    
    private void a(final BrushGroup brushGroup, final BrushGroup brushGroup2) {
        brushGroup2.validKey = brushGroup.validKey;
        if (brushGroup.name != null) {
            brushGroup2.name = brushGroup.name;
        }
        if (brushGroup.brushes == null || brushGroup.brushes.isEmpty()) {
            final Iterator<BrushData> iterator = brushGroup2.brushes.iterator();
            while (iterator.hasNext()) {
                this.b.add(iterator.next());
                ++this.e;
            }
            return;
        }
        final ArrayList<BrushData> brushes = new ArrayList<BrushData>(brushGroup.brushes.size());
        for (final BrushData brushData : brushGroup.brushes) {
            final BrushData brush = brushGroup2.getBrush(brushData.brushId);
            if (brush == null) {
                continue;
            }
            brushes.add(brush);
            this.b.add(brush);
            this.a(brushData, brush);
            ++this.e;
        }
        brushGroup2.brushes = brushes;
    }
    
    private void a(final BrushData brushData, final BrushData brushData2) {
        if (brushData2.args == null) {
            brushData2.args = brushData.args;
        }
        else if (brushData.args != null) {
            for (final Map.Entry<String, String> entry : brushData.args.entrySet()) {
                brushData2.args.put(entry.getKey(), entry.getValue());
            }
        }
        if (brushData.name != null) {
            brushData2.name = brushData.name;
        }
        if (brushData.thumb != null) {
            brushData2.thumb = brushData.thumb;
        }
    }
    
    public void loadThumbWithBrush(final BrushData brushData, final ImageView imageView) {
        if (brushData == null || imageView == null) {
            return;
        }
        final BrushData brushWithCode = this.getBrushWithCode(brushData.code);
        if (brushWithCode == null || this.a(imageView, brushWithCode.thumb)) {
            return;
        }
        this.loadImage(new BrushThumbTaskImageWare(imageView, brushWithCode));
    }
    
    private boolean a(final ImageView imageView, final String s) {
        if (s == null) {
            return false;
        }
        final int drawableResId = TuSdkContext.getDrawableResId(s);
        if (drawableResId != 0) {
            imageView.setImageResource(drawableResId);
            return true;
        }
        return false;
    }
    
    public boolean loadBrushData(final BrushData brushData) {
        if (brushData == null) {
            return false;
        }
        brushData.setImage(null);
        if (brushData.code.equals("Eraser")) {
            brushData.setImage(BitmapHelper.createOvalImage(200, 200, -16777216));
            return true;
        }
        final BrushGroup groupByID = this.findGroupByID(brushData.groupId);
        if (groupByID == null) {
            return false;
        }
        final BrushData brush = groupByID.getBrush(brushData.brushId);
        if (brush == null) {
            return false;
        }
        final Bitmap brush2 = SdkValid.shared.readBrush(brush.groupId, brush.brushImageKey);
        if (brush2 == null) {
            return false;
        }
        brushData.setImage(brush2);
        StatisticsManger.appendBrush(brushData);
        return true;
    }
    
    public BrushGroup findGroupByID(final Long n) {
        if (this.a != null && this.a.size() > 0) {
            for (final BrushGroup brushGroup : this.a) {
                if (brushGroup.groupId == n) {
                    return brushGroup;
                }
            }
        }
        return null;
    }
    
    @Override
    protected String getCacheKey(final BrushThumbTaskImageWare brushThumbTaskImageWare) {
        return TextUtils.isEmpty((CharSequence)brushThumbTaskImageWare.data.thumbKey) ? brushThumbTaskImageWare.data.code : brushThumbTaskImageWare.data.thumbKey;
    }
    
    @Override
    protected Bitmap asyncTaskLoadImage(final BrushThumbTaskImageWare brushThumbTaskImageWare) {
        Bitmap brushThumb = null;
        if (brushThumbTaskImageWare.data != null) {
            brushThumb = SdkValid.shared.readBrushThumb(brushThumbTaskImageWare.data.groupId, brushThumbTaskImageWare.data.brushId);
        }
        return brushThumb;
    }
    
    @Override
    public boolean containsGroupId(final long l) {
        return this.findGroupByID(l) != null;
    }
    
    @Override
    protected void removeDownloadData(final long n) {
        final BrushGroup groupByID = this.findGroupByID(n);
        this.a.remove(groupByID);
        if (groupByID.brushes != null) {
            final Iterator<BrushData> iterator = groupByID.brushes.iterator();
            while (iterator.hasNext()) {
                this.b.remove(iterator.next());
                --this.e;
            }
        }
        SdkValid.shared.removeBrushGroup(n);
        TLog.d("remove download brushes [%s]: %s | %s", n, this.e, this.b.size());
    }
    
    @Override
    protected boolean appendDownload(final TuSdkDownloadItem tuSdkDownloadItem) {
        if (!super.appendDownload(tuSdkDownloadItem)) {
            return false;
        }
        final String loadBrushGroup = SdkValid.shared.loadBrushGroup(tuSdkDownloadItem.localDownloadPath().getAbsolutePath(), tuSdkDownloadItem.key);
        if (loadBrushGroup == null) {
            return false;
        }
        final BrushGroup e = JsonWrapper.deserialize(loadBrushGroup, BrushGroup.class);
        if (e == null) {
            return false;
        }
        if (e.brushes == null || e.brushes.isEmpty()) {
            return false;
        }
        e.isDownload = true;
        this.a.add(e);
        final Iterator<BrushData> iterator = e.brushes.iterator();
        while (iterator.hasNext()) {
            this.b.add(iterator.next());
            ++this.e;
        }
        return true;
    }
    
    @Override
    protected Collection<?> getAllGroupID() {
        final ArrayList<Long> list = new ArrayList<Long>();
        if (this.a != null && this.a.size() > 0) {
            final Iterator<BrushGroup> iterator = this.a.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next().groupId);
            }
        }
        return list;
    }
    
    public static class BrushThumbTaskImageWare extends ImageViewTaskWare
    {
        public BrushData data;
        
        public BrushThumbTaskImageWare(final ImageView imageView, final BrushData data) {
            this.setImageView(imageView);
            this.data = data;
        }
    }
}
