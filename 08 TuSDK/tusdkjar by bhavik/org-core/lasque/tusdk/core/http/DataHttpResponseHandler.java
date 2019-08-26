package org.lasque.tusdk.core.http;

import android.os.Message;
import org.lasque.tusdk.core.utils.TLog;

public abstract class DataHttpResponseHandler
  extends ClearHttpResponseHandler
{
  protected static final int PROGRESS_DATA_MESSAGE = 7;
  
  public static byte[] copyOfRange(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt1 > paramInt2) {
      throw new IllegalArgumentException();
    }
    int i = paramArrayOfByte.length;
    if ((paramInt1 < 0) || (paramInt1 > i)) {
      throw new ArrayIndexOutOfBoundsException();
    }
    int j = paramInt2 - paramInt1;
    int k = Math.min(j, i - paramInt1);
    byte[] arrayOfByte = new byte[j];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, k);
    return arrayOfByte;
  }
  
  public void onProgressData(byte[] paramArrayOfByte)
  {
    TLog.d("onProgressData(byte[]) was not overriden, but callback was received", new Object[0]);
  }
  
  public final void sendProgressDataMessage(byte[] paramArrayOfByte)
  {
    sendMessage(obtainMessage(7, new Object[] { paramArrayOfByte }));
  }
  
  protected void handleMessage(Message paramMessage)
  {
    super.handleMessage(paramMessage);
    switch (paramMessage.what)
    {
    case 7: 
      Object[] arrayOfObject = (Object[])paramMessage.obj;
      if ((arrayOfObject != null) && (arrayOfObject.length >= 1)) {
        try
        {
          onProgressData((byte[])arrayOfObject[0]);
        }
        catch (Throwable localThrowable)
        {
          TLog.e(localThrowable, "custom onProgressData contains an error", new Object[0]);
        }
      } else {
        TLog.e("PROGRESS_DATA_MESSAGE didn't got enough params", new Object[0]);
      }
      break;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\DataHttpResponseHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */