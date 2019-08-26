// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.util.List;

public class BlackholeHttpResponseHandler extends ClearHttpResponseHandler
{
    @Override
    public void onSuccess(final int n, final List<HttpHeader> list, final byte[] array) {
    }
    
    @Override
    public void onFailure(final int n, final List<HttpHeader> list, final byte[] array, final Throwable t) {
    }
    
    @Override
    public void onProgress(final long n, final long n2) {
    }
    
    @Override
    public void onCancel() {
    }
    
    @Override
    public void onFinish() {
    }
    
    @Override
    public void onPostProcessResponse(final ResponseHandlerInterface responseHandlerInterface, final HttpResponse httpResponse) {
    }
    
    @Override
    public void onPreProcessResponse(final ResponseHandlerInterface responseHandlerInterface, final HttpResponse httpResponse) {
    }
    
    @Override
    public void onRetry(final int n) {
    }
    
    @Override
    public void onStart() {
    }
    
    @Override
    public void onUserException(final Throwable t) {
    }
}
