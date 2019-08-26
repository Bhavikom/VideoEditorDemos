package org.lasque.tusdk.core.media.suit;

import android.media.MediaFormat;
import org.lasque.tusdk.core.media.camera.TuSdkCamera;
import org.lasque.tusdk.core.media.camera.TuSdkCameraImpl;
import org.lasque.tusdk.core.media.record.TuSdkCameraRecorder;
import org.lasque.tusdk.core.media.record.TuSdkMediaRecordHub;
import org.lasque.tusdk.core.media.record.TuSdkMediaRecordHub.TuSdkMediaRecordHubListener;

public class TuSdkCameraSuit
{
  public static TuSdkCamera createCamera()
  {
    return new TuSdkCameraImpl();
  }
  
  public static TuSdkMediaRecordHub cameraRecorder(MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2, TuSdkCamera paramTuSdkCamera, TuSdkMediaRecordHub.TuSdkMediaRecordHubListener paramTuSdkMediaRecordHubListener)
  {
    TuSdkCameraRecorder localTuSdkCameraRecorder = new TuSdkCameraRecorder();
    localTuSdkCameraRecorder.setOutputVideoFormat(paramMediaFormat1);
    localTuSdkCameraRecorder.setOutputAudioFormat(paramMediaFormat2);
    localTuSdkCameraRecorder.appendRecordSurface(paramTuSdkCamera);
    localTuSdkCameraRecorder.setRecordListener(paramTuSdkMediaRecordHubListener);
    return localTuSdkCameraRecorder;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\suit\TuSdkCameraSuit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */