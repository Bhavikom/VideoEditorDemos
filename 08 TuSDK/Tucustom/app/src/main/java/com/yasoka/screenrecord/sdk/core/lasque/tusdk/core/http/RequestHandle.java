// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import android.os.Looper;
import java.lang.ref.WeakReference;

public class RequestHandle
{
    private final WeakReference<ClearHttpRequest> a;
    
    public RequestHandle(final ClearHttpRequest referent) {
        this.a = new WeakReference<ClearHttpRequest>(referent);
    }
    
    public boolean cancel(final boolean b) {
        final ClearHttpRequest clearHttpRequest = this.a.get();
        if (clearHttpRequest == null) {
            return false;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    clearHttpRequest.cancel(b);
                }
            }).start();
            return true;
        }
        return clearHttpRequest.cancel(b);
    }
    
    public boolean isFinished() {
        final ClearHttpRequest clearHttpRequest = this.a.get();
        return clearHttpRequest == null || clearHttpRequest.isDone();
    }
    
    public boolean isCancelled() {
        final ClearHttpRequest clearHttpRequest = this.a.get();
        return clearHttpRequest == null || clearHttpRequest.isCancelled();
    }
    
    public boolean shouldBeGarbageCollected() {
        final boolean b = this.isCancelled() || this.isFinished();
        if (b) {
            this.a.clear();
        }
        return b;
    }
    
    public Object getTag() {
        final ClearHttpRequest clearHttpRequest = this.a.get();
        return (clearHttpRequest == null) ? null : clearHttpRequest.getTag();
    }
    
    public RequestHandle setTag(final Object requestTag) {
        final ClearHttpRequest clearHttpRequest = this.a.get();
        if (clearHttpRequest != null) {
            clearHttpRequest.setRequestTag(requestTag);
        }
        return this;
    }
}
