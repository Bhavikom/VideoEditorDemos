// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge;

import java.util.Iterator;
//import org.lasque.tusdk.core.type.DownloadTaskStatus;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.json.JSONObject;
import android.widget.ImageView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.BrushAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;

import java.util.ArrayList;
//import org.lasque.tusdk.core.TuSdkConfigs;
import java.util.List;
//import org.lasque.tusdk.core.secret.BrushAdapter;
//import org.lasque.tusdk.core.network.TuSdkDownloadManger;

public class BrushLocalPackage implements TuSdkDownloadManger.TuSdkDownloadMangerDelegate
{
    public static final String EraserBrushCode = "Eraser";
    private static BrushLocalPackage a;
    private BrushAdapter b;
    private List<BrushLocalPackageDelegate> c;
    
    public static BrushLocalPackage shared() {
        return BrushLocalPackage.a;
    }
    
    public static BrushLocalPackage init(final TuSdkConfigs tuSdkConfigs) {
        if (BrushLocalPackage.a == null && tuSdkConfigs != null) {
            BrushLocalPackage.a = new BrushLocalPackage(tuSdkConfigs);
        }
        return BrushLocalPackage.a;
    }
    
    public void appenDelegate(final BrushLocalPackageDelegate brushLocalPackageDelegate) {
        if (brushLocalPackageDelegate == null || this.c.contains(brushLocalPackageDelegate)) {
            return;
        }
        this.c.add(brushLocalPackageDelegate);
    }
    
    public void removeDelegate(final BrushLocalPackageDelegate brushLocalPackageDelegate) {
        if (brushLocalPackageDelegate == null) {
            return;
        }
        this.c.remove(brushLocalPackageDelegate);
    }
    
    private BrushLocalPackage(final TuSdkConfigs tuSdkConfigs) {
        this.c = new ArrayList<BrushLocalPackageDelegate>();
        (this.b = new BrushAdapter(tuSdkConfigs)).setDownloadDelegate(this);
    }
    
    public List<String> getCodes() {
        return this.b.getCodes();
    }
    
    public boolean isInited() {
        return this.b.isInited();
    }
    
    public List<String> verifyCodes(final List<String> list) {
        return this.b.verifyCodes(list);
    }
    
    public BrushData getEeaserBrush() {
        return this.b.getEraserBrush();
    }
    
    public BrushData getBrushWithCode(final String s) {
        return this.b.getBrushWithCode(s);
    }
    
    public List<BrushData> getBrushWithCodes(final List<String> list) {
        return this.b.getBrushWithCodes(list);
    }
    
    public boolean loadBrushData(final BrushData brushData) {
        return this.b.loadBrushData(brushData);
    }
    
    public void loadThumbWithBrush(final ImageView imageView, final BrushData brushData) {
        this.b.loadThumbWithBrush(brushData, imageView);
    }
    
    public void cancelLoadImage(final ImageView imageView) {
        this.b.cancelLoadImage(imageView);
    }
    
    public void download(final long n, final String s, final String s2) {
        this.b.download(n, s, s2);
    }
    
    public void cancelDownload(final long n) {
        this.b.cancelDownload(n);
    }
    
    public void removeDownloadWithIdt(final long n) {
        this.b.removeDownloadWithIdt(n);
    }
    
    public JSONObject getAllDatas() {
        return this.b.getAllDatas();
    }

    @Override
    public void onDownloadMangerStatusChanged(final TuSdkDownloadManger tuSdkDownloadManger, final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
        final Iterator<BrushLocalPackageDelegate> iterator = new ArrayList<BrushLocalPackageDelegate>(this.c).iterator();
        while (iterator.hasNext()) {
            iterator.next().onBrushPackageStatusChanged(this, tuSdkDownloadItem, downloadTaskStatus);
        }
    }

    public interface BrushLocalPackageDelegate
    {
        void onBrushPackageStatusChanged(final BrushLocalPackage p0, final TuSdkDownloadItem p1, final DownloadTaskStatus p2);
    }
}
