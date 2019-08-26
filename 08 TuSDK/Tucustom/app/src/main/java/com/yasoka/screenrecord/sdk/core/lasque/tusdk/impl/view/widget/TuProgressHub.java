// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget;

//import org.lasque.tusdk.core.view.widget.TuSdkProgressHubView;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuSdkProgressHub;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuSdkProgressHubView;
//import org.lasque.tusdk.core.view.widget.TuSdkProgressHub;

public class TuProgressHub extends TuSdkProgressHub
{
    public static final TuProgressHub ins;
    
    public static void setStatus(final Context context, final String s) {
        TuProgressHub.ins.showHubView(context, TuSdkProgressHubView.TuSdkHubViewShowType.TypeDefault, s, 0, 0, 0L);
    }
    
    public static void setStatus(final Context context, final int n) {
        TuProgressHub.ins.showHubView(context, TuSdkProgressHubView.TuSdkHubViewShowType.TypeDefault, null, n, 0, 0L);
    }
    
    public static void showToast(final Context context, final String s) {
        TuProgressHub.ins.showHubView(context, TuSdkProgressHubView.TuSdkHubViewShowType.TypeImage, s, 0, 0, 1000L);
    }
    
    public static void showToast(final Context context, final int n) {
        TuProgressHub.ins.showHubView(context, TuSdkProgressHubView.TuSdkHubViewShowType.TypeImage, null, n, 0, 1000L);
    }
    
    public static void showSuccess(final Context context, final String s) {
        TuProgressHub.ins.showHubView(context, TuSdkProgressHubView.TuSdkHubViewShowType.TypeSucceed, s, 0, 0, 1000L);
    }
    
    public static void showSuccess(final Context context, final int n) {
        TuProgressHub.ins.showHubView(context, TuSdkProgressHubView.TuSdkHubViewShowType.TypeSucceed, null, n, 0, 1000L);
    }
    
    public static void showError(final Context context, final String s) {
        TuProgressHub.ins.showHubView(context, TuSdkProgressHubView.TuSdkHubViewShowType.TypeFailed, s, 0, 0, 1000L);
    }
    
    public static void showError(final Context context, final int n) {
        TuProgressHub.ins.showHubView(context, TuSdkProgressHubView.TuSdkHubViewShowType.TypeFailed, null, n, 0, 1000L);
    }
    
    public static void showImage(final Context context, final int n, final String s) {
        TuProgressHub.ins.showHubView(context, TuSdkProgressHubView.TuSdkHubViewShowType.TypeImage, s, 0, n, 0L);
    }
    
    public static void showImage(final Context context, final int n, final int n2) {
        TuProgressHub.ins.showHubView(context, TuSdkProgressHubView.TuSdkHubViewShowType.TypeImage, null, n2, n, 0L);
    }
    
    public static void dismiss() {
        TuProgressHub.ins.dismissHub(true);
    }
    
    public static void dismissRightNow() {
        TuProgressHub.ins.dismissHub(false);
    }
    
    public static boolean isVisible() {
        return TuProgressHub.ins.existHubView();
    }
    
    private TuProgressHub() {
    }
    
    @Override
    public int getHubLayoutId() {
        return TuProgressHubView.getLayoutId();
    }
    
    static {
        ins = new TuProgressHub();
    }
}
