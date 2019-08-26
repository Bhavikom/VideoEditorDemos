// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.suit;

//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
//import org.lasque.tusdk.core.media.record.TuSdkCameraRecorder;
//import org.lasque.tusdk.core.media.record.TuSdkMediaRecordHub;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCamera;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkCameraRecorder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkMediaRecordHub;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraImpl;
//import org.lasque.tusdk.core.media.camera.TuSdkCamera;

public class TuSdkCameraSuit
{
    public static TuSdkCamera createCamera() {
        return new TuSdkCameraImpl();
    }
    
    public static TuSdkMediaRecordHub cameraRecorder(final MediaFormat outputVideoFormat, final MediaFormat outputAudioFormat, final TuSdkCamera tuSdkCamera, final TuSdkMediaRecordHub.TuSdkMediaRecordHubListener recordListener) {
        final TuSdkCameraRecorder tuSdkCameraRecorder = new TuSdkCameraRecorder();
        tuSdkCameraRecorder.setOutputVideoFormat(outputVideoFormat);
        tuSdkCameraRecorder.setOutputAudioFormat(outputAudioFormat);
        tuSdkCameraRecorder.appendRecordSurface(tuSdkCamera);
        tuSdkCameraRecorder.setRecordListener(recordListener);
        return tuSdkCameraRecorder;
    }
}
