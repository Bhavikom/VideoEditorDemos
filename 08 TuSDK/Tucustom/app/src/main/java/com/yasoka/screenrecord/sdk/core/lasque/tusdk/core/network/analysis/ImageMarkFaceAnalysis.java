// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis;

//import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;

public class ImageMarkFaceAnalysis
{
    private ImageOnlineAnalysis a;
    
    public void reset() {
        if (this.a != null) {
            this.a.cancel();
            this.a = null;
        }
    }
    
    public void analysisWithThumb(final Bitmap image, final ImageFaceMarkAnalysisListener imageFaceMarkAnalysisListener) {
        if (imageFaceMarkAnalysisListener == null) {
            return;
        }
        if (image == null) {
            imageFaceMarkAnalysisListener.onImageFaceAnalysisCompleted(null, ImageOnlineAnalysis.ImageAnalysisType.NotInputImage);
            return;
        }
        (this.a = new ImageOnlineAnalysis()).setImage(image);
        this.a.analysisFaces(new ImageOnlineAnalysis.ImageAnalysisListener() {
            @Override
            public <T extends JsonBaseBean> void onImageAnalysisCompleted(final T t, final ImageOnlineAnalysis.ImageAnalysisType imageAnalysisType) {
                if (imageAnalysisType != ImageOnlineAnalysis.ImageAnalysisType.Succeed) {
                    imageFaceMarkAnalysisListener.onImageFaceAnalysisCompleted(null, imageAnalysisType);
                    return;
                }
                imageFaceMarkAnalysisListener.onImageFaceAnalysisCompleted((ImageMarkFaceResult)t, imageAnalysisType);
            }
        });
    }
    
    public interface ImageFaceMarkAnalysisListener
    {
        void onImageFaceAnalysisCompleted(final ImageMarkFaceResult p0, final ImageOnlineAnalysis.ImageAnalysisType p1);
    }
}
