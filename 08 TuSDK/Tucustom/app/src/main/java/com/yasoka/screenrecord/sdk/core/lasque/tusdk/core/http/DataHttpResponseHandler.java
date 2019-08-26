// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import android.os.Message;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.TLog;

public abstract class DataHttpResponseHandler extends ClearHttpResponseHandler
{
    protected static final int PROGRESS_DATA_MESSAGE = 7;
    
    public static byte[] copyOfRange(final byte[] array, final int n, final int n2) {
        if (n > n2) {
            throw new IllegalArgumentException();
        }
        final int length = array.length;
        if (n < 0 || n > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        final int a = n2 - n;
        final int min = Math.min(a, length - n);
        final byte[] array2 = new byte[a];
        System.arraycopy(array, n, array2, 0, min);
        return array2;
    }
    
    public void onProgressData(final byte[] array) {
        TLog.d("onProgressData(byte[]) was not overriden, but callback was received", new Object[0]);
    }
    
    public final void sendProgressDataMessage(final byte[] array) {
        this.sendMessage(this.obtainMessage(7, new Object[] { array }));
    }
    
    @Override
    protected void handleMessage(final Message message) {
        super.handleMessage(message);
        switch (message.what) {
            case 7: {
                final Object[] array = (Object[])message.obj;
                if (array != null && array.length >= 1) {
                    try {
                        this.onProgressData((byte[])array[0]);
                    }
                    catch (Throwable t) {
                        TLog.e(t, "custom onProgressData contains an error", new Object[0]);
                    }
                    break;
                }
                TLog.e("PROGRESS_DATA_MESSAGE didn't got enough params", new Object[0]);
                break;
            }
        }
    }
}
