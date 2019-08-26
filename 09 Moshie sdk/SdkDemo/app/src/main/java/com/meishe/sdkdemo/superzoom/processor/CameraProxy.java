package com.meishe.sdkdemo.superzoom.processor;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraProxy {

    private static final String TAG = "CameraProxy";

    private Context mContext;
    private int mCameraId;
    private Camera mCamera;

    private boolean isCameraOpen = false;
    private boolean mCameraOpenFailed = false;

    private CameraInfo mCameraInfo = new CameraInfo();

    public CameraProxy(Context context) {
        mContext = context;
    }

    public Camera getCamera() {
        return mCamera;
    }

    public boolean openCamera(int cameraId) {
        try {
            releaseCamera();
            mCamera = Camera.open(cameraId);
            mCamera.getParameters();
            mCameraId = cameraId;
            mCamera.getCameraInfo(cameraId, mCameraInfo);
            mCamera.setDisplayOrientation(90);

            setDefaultParameters();

            isCameraOpen = true;
            mCameraOpenFailed = false;
        } catch (Exception e) {
            mCameraOpenFailed = true;
            mCamera = null;
            Log.i(TAG, "openCamera fail msg=" + e.getMessage());
            return false;
        }
        return true;
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            try {
                mCamera.setPreviewTexture(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void startPreview(SurfaceTexture surfaceTexture, PreviewCallback previewcallback) {
        try {
            if (mCamera == null) {
                return;
            }
            if (surfaceTexture != null && mCamera != null) {
                mCamera.setPreviewTexture(surfaceTexture);
            }

            if (previewcallback != null && mCamera != null) {
                mCamera.setPreviewCallback(previewcallback);
            }
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPreview() {
        if (mCamera != null)
            mCamera.stopPreview();
    }

    public Size getPreviewSize() {
        if (mCamera != null) {
            return mCamera.getParameters().getPreviewSize();
        } else {
            return null;
        }
    }

    public Size getPictureSize() {
        if (mCamera != null) {
            return mCamera.getParameters().getPictureSize();
        }
        return null;
    }

    public void setOneShotPreviewCallback(PreviewCallback callback) {
        mCamera.setOneShotPreviewCallback(callback);
    }


    public void addPreviewCallbackBuffer(byte[] callbackBuffer) {
        mCamera.addCallbackBuffer(callbackBuffer);
    }


    public int getOrientation() {
        if (mCameraInfo == null) {
            return 0;
        }
        return mCameraInfo.orientation;
    }

    public boolean isFlipHorizontal() {
        if (mCameraInfo == null) {
            return false;
        }
        return mCameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT ? true : false;
    }

    public int getCameraId() {
        return mCameraId;
    }

    public boolean isFrontCamera() {
        return mCameraId == CameraInfo.CAMERA_FACING_FRONT;
    }

    public void setRotation(int rotation) {
        if (mCamera != null) {
            Parameters params = mCamera.getParameters();
            params.setRotation(rotation);
            mCamera.setParameters(params);
        }
    }

    public void takePicture(Camera.ShutterCallback shutterCallback, Camera.PictureCallback rawCallback,
                            Camera.PictureCallback jpegCallback) {
        if (mCamera != null) {
            mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
        }
    }

    public int getDisplayOrientation(int dir) {
        /**
         * 请注意前置摄像头与后置摄像头旋转定义不同
         * 请注意不同手机摄像头旋转定义不同
         */
        int newdir = dir;
        if (isFrontCamera() &&
                ((mCameraInfo.orientation == 270 && (dir & 1) == 1) ||
                        (mCameraInfo.orientation == 90 && (dir & 1) == 0))) {
            newdir = (dir ^ 2);
        }
        return newdir;
    }

    public boolean needMirror() {
        if (isFrontCamera()) {
            return true;
        } else {
            return false;
        }
    }

    public void setDefaultParameters() {
        Parameters parameters = mCamera.getParameters();
        Log.d(TAG, "parameters: " + parameters.flatten());
        if (parameters.getSupportedFocusModes().contains(
                Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes != null && flashModes.contains(Parameters.FLASH_MODE_OFF)) {
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
        }
        Point previewSize = getSuitablePreviewSize();
//      parameters.setPreviewSize(previewSize.x, previewSize.y);
        parameters.setPreviewSize(640, 480);
        Point pictureSize = getSuitablePictureSize();
        parameters.setPictureSize(pictureSize.x, pictureSize.y);
        mCamera.setParameters(parameters);
    }

    public Parameters getParameters() {
        return mCamera.getParameters();
    }

    public void setPreviewSize(int width, int height) {
        if (mCamera == null)
            return;
        Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(width, height);
        if (mCamera == null)
            return;
        mCamera.setParameters(parameters);
    }


    public void toggleFlash(boolean state) {
        Parameters parameters = mCamera.getParameters();
        if (state) {
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            // 打开
        } else {
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            // 关闭
        }
        if (mCamera == null)
            return;
        mCamera.setParameters(parameters);
    }

    private Point getSuitablePreviewSize() {
        Point defaultsize = new Point(1920, 1080);
        if (mCamera != null) {
            List<Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
            for (Size s : sizes) {
                if ((s.width == defaultsize.x) && (s.height == defaultsize.y)) {
                    return defaultsize;
                }
            }
            return new Point(640, 480);
        }
        return null;
    }

    public ArrayList<String> getSupportedPreviewSize(String[] previewSizes) {
        ArrayList<String> result = new ArrayList<String>();
        if (mCamera != null) {
            List<Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
            for (String candidate : previewSizes) {
                int index = candidate.indexOf('x');
                if (index == -1) continue;
                int width = Integer.parseInt(candidate.substring(0, index));
                int height = Integer.parseInt(candidate.substring(index + 1));
                for (Size s : sizes) {
                    if ((s.width == width) && (s.height == height)) {
                        result.add(candidate);
                    }
                }
            }
        }
        return result;
    }

    private Point getSuitablePictureSize() {
        Point defaultsize = new Point(4608, 3456);
        //	Point defaultsize = new Point(3264, 2448);
        if (mCamera != null) {
            Point maxSize = new Point(0, 0);
            List<Size> sizes = mCamera.getParameters().getSupportedPictureSizes();
            for (Size s : sizes) {
                if ((s.width == defaultsize.x) && (s.height == defaultsize.y)) {
                    return defaultsize;
                }
                if (maxSize.x < s.width) {
                    maxSize.x = s.width;
                    maxSize.y = s.height;
                }
            }
            return maxSize;
        }
        return null;
    }


    public int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }

    public boolean cameraOpenFailed() {
        return mCameraOpenFailed;
    }

    public boolean isCameraOpen() {
        return isCameraOpen;
    }

}
