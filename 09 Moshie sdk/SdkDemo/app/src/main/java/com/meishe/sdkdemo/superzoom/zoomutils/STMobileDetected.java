package com.meishe.sdkdemo.superzoom.zoomutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;

import com.meicam.effect.sdk.NvsFaceFeaturePoint;
import com.meicam.sdk.NvsPosition2D;
import com.sensetime.stmobile.STCommonNative;
import com.sensetime.stmobile.STMobileAuthentificationNative;
import com.sensetime.stmobile.STMobileHumanActionNative;
//import com.sensetime.stmobile.STMobileStickerNative;
import com.sensetime.stmobile.model.STHumanAction;
import com.sensetime.stmobile.model.STPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by meicam-dx on 2017/10/26.
 */

public class STMobileDetected {

    public final static int ST_CLOCKWISE_ROTATE_0 = 0;  //< 图像不需要转向
    public final static int ST_CLOCKWISE_ROTATE_90 = 1;  //< 图像需要顺时针旋转90度
    public final static int ST_CLOCKWISE_ROTATE_180 = 2; //< 图像需要顺时针旋转180度
    public final static int ST_CLOCKWISE_ROTATE_270 = 3; //< 图像需要顺时针旋转270度

    private final static String PREF_ACTIVATE_CODE_FILE = "activate_code_file";
    private final static String PREF_ACTIVATE_CODE = "activate_code";

    private final String TAG = "STMobileDetected";
    private final static String LIENCSE_FILE_NAME = "SenseME.lic";
    private static final String ST_MODEL_NAME_ACTION = "SenseME.model";
    private static final String ST_MODEL_NAME_FACE_ATTRIBUTE = "face_attribute_1.0.1.model";
    private static final String ST_MODEL_NAME_EYEBALL_CENTER = "M_Eyeball_Center.model";
    private static final String ST_MODEL_NAME_EYEBALL_CONTOUR = "M_Eyeball_Contour.model";
    private static final String ST_MODEL_NAME_FACE_EXTRA = "M_SenseME_Face_Extra_5.0.0.model";

    private Context mContext;
    private STMobileHumanActionNative m_STHumanActionNative = new STMobileHumanActionNative();
//    private STCommonNative m_STCommon = new STCommon();
//    private STMobileStickerNative mStStickerNative = new STMobileStickerNative();
    private boolean mNeedSticker = false;
    private String mStickernameChange;
    private String mCurrentStickername;
    private Object mStickerChangeObject = new Object();
    private boolean mInitSticker = false;

    private int m_humanActionCreateConfig = STMobileHumanActionNative.ST_MOBILE_HUMAN_ACTION_DEFAULT_CONFIG_VIDEO;
    private boolean m_isCreateHumanActionHandleSucceeded = false;
    private long m_detectConfig = STMobileHumanActionNative.ST_MOBILE_FACE_DETECT;

    public boolean checkActiveCodeFromBuffer(Context context) {

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(PREF_ACTIVATE_CODE_FILE, Context.MODE_PRIVATE);
        String activateCode = sp.getString(PREF_ACTIVATE_CODE, null);
        Integer error = new Integer(-1);

        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = null;
        BufferedReader br = null;
        // 读取license文件内容
        try {
            isr = new InputStreamReader(context.getAssets().open(LIENCSE_FILE_NAME));
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        String licenseBuffer = sb.toString();

        if (activateCode == null || (STMobileAuthentificationNative.checkActiveCodeFromBuffer(context, licenseBuffer, licenseBuffer.length(), activateCode, activateCode.length()) != 0)) {
            activateCode = STMobileAuthentificationNative.generateActiveCodeFromBuffer(context, licenseBuffer, licenseBuffer.length());
            if (activateCode != null && activateCode.length() > 0) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(PREF_ACTIVATE_CODE, activateCode);
                editor.commit();

                int result = STMobileAuthentificationNative.checkActiveCodeFromBuffer(context, licenseBuffer, licenseBuffer.length(), activateCode, activateCode.length());
                return result != 0 ? false : true;
            }
            return false;
        }

        return true;
    }

    /**
     * 初始化商汤人脸检测
     */
    public boolean initSTMobileDetected(Context context) {

        if (m_STHumanActionNative == null)
            m_STHumanActionNative = new STMobileHumanActionNative();

        m_detectConfig = STMobileHumanActionNative.ST_MOBILE_FACE_DETECT;
        if (!checkActiveCodeFromBuffer(context))
            return false;
        //从asset资源文件夹读取model到内存，再使用底层st_mobile_human_action_create_from_buffer接口创建handle
        int result = m_STHumanActionNative.createInstanceFromAssetFile(ST_MODEL_NAME_ACTION, m_humanActionCreateConfig, context.getAssets());
        if (result == 0) {
            m_isCreateHumanActionHandleSucceeded = true;
        } else {
            Log.e(TAG, "the result for createInstance for human_action is " + result);
            m_isCreateHumanActionHandleSucceeded = false;
            return false;
        }

        mContext = context;
        return true;
    }

    public boolean isEnableSticker() {
        boolean bisEnable = false;
        synchronized (mStickerChangeObject) {
            bisEnable = mNeedSticker;
        }
        return bisEnable;
    }

    public void enableSticker(boolean needSticker) {
        synchronized (mStickerChangeObject) {
            mNeedSticker = needSticker;

        }
    }

    public void changeSticker(String stickName) {

        synchronized (mStickerChangeObject) {
            mStickernameChange = stickName;
        }

        enableSticker(stickName != null ? true : false);
    }

    public void closeDetected() {
        if (m_STHumanActionNative != null) {
            m_STHumanActionNative.destroyInstance();
            m_STHumanActionNative = null;
        }
    }

    private int getHumanActionOrientation(int cameraOrientation, boolean flipHorizontal) {
        boolean frontCamera = flipHorizontal;

        //获取重力传感器返回的方向
        int orientation = Accelerometer.getDirection();

        //在使用后置摄像头，且传感器方向为0或2时，后置摄像头与前置orentation相反
        if (!frontCamera && orientation == STMobileDetected.ST_CLOCKWISE_ROTATE_0) {
            orientation = STMobileDetected.ST_CLOCKWISE_ROTATE_180;
        } else if (!frontCamera && orientation == STMobileDetected.ST_CLOCKWISE_ROTATE_180) {
            orientation = STMobileDetected.ST_CLOCKWISE_ROTATE_0;
        }

        // 请注意前置摄像头与后置摄像头旋转定义不同 && 不同手机摄像头旋转定义不同
        if (((cameraOrientation == 270 && (orientation & STMobileDetected.ST_CLOCKWISE_ROTATE_90) == STMobileDetected.ST_CLOCKWISE_ROTATE_90) ||
                (cameraOrientation == 90 && (orientation & STMobileDetected.ST_CLOCKWISE_ROTATE_90) == STMobileDetected.ST_CLOCKWISE_ROTATE_0)))
            orientation = (orientation ^ STMobileDetected.ST_CLOCKWISE_ROTATE_180);
        return orientation;
    }

    private int getCurrentOrientation() {
        int dir = Accelerometer.getDirection();
        int orientation = dir - 1;
        if (orientation < 0) {
            orientation = dir ^ 3;
        }

        return orientation;
    }

    public NvsFaceFeaturePoint convertFromSTHumanAction(STHumanAction action) {
        if (action == null)
            return null;
        if (action.faceCount > 0) {
            NvsFaceFeaturePoint faceFeature = NvsFaceFeaturePoint.createFaceFeaturePoint(action.faceCount);
            for (int i = 0; i < faceFeature.faceCount; i++) {
                faceFeature.faces[i].setID(action.faces[i].face106.getID());
                float[] visibilityArray = faceFeature.faces[i].getVisibilityArray();
                NvsPosition2D[] fepoint = faceFeature.faces[i].getPoints_array();
                STPoint[] detectedPoint = action.faces[i].face106.getPointsArray();
                float[] detectedvisibilityArray = action.faces[i].face106.getVisibilityArray();
                for (int n = 0; n < fepoint.length; n++) {
                    visibilityArray[n] = detectedvisibilityArray[n];
                    NvsPosition2D point = new NvsPosition2D(detectedPoint[n].getX(), detectedPoint[n].getY());
                    fepoint[n] = point;
                }
            }

            return faceFeature;
        }

        return null;
    }

    /**
     * 商汤人脸检测
     */
    public STHumanAction stMobileDetected(byte[] ImageData, int width, int height, int cameraOrientation, boolean flipHorizontal) {
        if (!m_isCreateHumanActionHandleSucceeded)
            return null;
        if (m_detectConfig < STMobileHumanActionNative.ST_MOBILE_FACE_DETECT)
            return null;

        if (m_STHumanActionNative == null)
            return null;

        boolean isEnable = false;
        String strStickerName;
        synchronized (mStickerChangeObject) {
            strStickerName = mStickernameChange;
            isEnable = mNeedSticker;
        }

        if (isEnable) {
//            if (!mInitSticker) {
//                //init sticker
//                int result = mStStickerNative.createInstance(mContext);
//                if (result != 0) {
//                    Log.e(TAG, "init sticker inistance failed, error code: " + result);
//                }
//                mInitSticker = true;
//            }
//
//            if (strStickerName != null && strStickerName != mCurrentStickername) {
//                mStStickerNative.changeSticker(strStickerName);
//                mCurrentStickername = strStickerName;
//                //reset humanAction config
//                m_detectConfig = STMobileHumanActionNative.ST_MOBILE_FACE_DETECT | mStStickerNative.getTriggerAction();
//            }
        } else {
            m_detectConfig = STMobileHumanActionNative.ST_MOBILE_FACE_DETECT;
        }

        long currentTime = System.currentTimeMillis();

        int faceRotation = getHumanActionOrientation(cameraOrientation, flipHorizontal);
        STHumanAction humanAction = m_STHumanActionNative.humanActionDetect(ImageData, STCommonNative.ST_PIX_FMT_NV21, m_detectConfig, faceRotation, width, height);
        if (humanAction.faceCount > 0) {
            int faceId = Camera.CameraInfo.CAMERA_FACING_BACK;
            if (flipHorizontal)
                faceId = Camera.CameraInfo.CAMERA_FACING_FRONT;

            int TexWidth = width;
            int TexHeight = height;
            if (cameraOrientation == 90 || cameraOrientation == 270) {
                TexWidth = height;
                TexHeight = width;
            }

//            humanAction = STHumanAction.humanActionRotateAndMirror(humanAction, TexWidth, TexHeight, faceId, cameraOrientation);
            humanAction = STHumanAction.humanActionRotate(TexWidth, TexHeight, cameraOrientation, false, humanAction);
            humanAction = STHumanAction.humanActionMirror(TexWidth, humanAction);
        }

        return humanAction;
    }

    public Rect getFaceRect(STHumanAction action) {
        if (action == null)
            return null;
        if (action.faceCount > 0) {
            return action.faces[0].face106.getRect().convertToRect();
        }
        return null;
    }
}
