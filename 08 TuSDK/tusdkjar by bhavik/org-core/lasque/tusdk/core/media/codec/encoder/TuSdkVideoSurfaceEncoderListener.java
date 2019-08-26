package org.lasque.tusdk.core.media.codec.encoder;

import org.lasque.tusdk.core.seles.egl.SelesRenderer;

public abstract interface TuSdkVideoSurfaceEncoderListener
  extends TuSdkEncoderListener, SelesRenderer
{
  public abstract void onEncoderDrawFrame(long paramLong, boolean paramBoolean);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\encoder\TuSdkVideoSurfaceEncoderListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */