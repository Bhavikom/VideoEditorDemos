package com.meishe.sdkdemo.particle;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.meicam.sdk.NvsAssetPackageParticleDescParser;
import com.meicam.sdk.NvsCaptureVideoFx;
import com.meicam.sdk.NvsParticleSystemContext;
import com.meicam.sdk.NvsPosition2D;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsVideoFrameInfo;
import com.meishe.sdkdemo.utils.Logger;
import com.sensetime.stmobile.STCommonNative;
import com.sensetime.stmobile.STHumanActionParamsType;
import com.sensetime.stmobile.STMobileAuthentificationNative;
import com.sensetime.stmobile.STMobileHumanActionNative;
import com.sensetime.stmobile.STRotateType;
import com.sensetime.stmobile.model.STHumanAction;
import com.sensetime.stmobile.model.STMobile106;
import com.sensetime.stmobile.model.STMobileFaceInfo;
import com.sensetime.stmobile.model.STMobileHandInfo;
import com.sensetime.stmobile.model.STPoint;
/*import com.sensetime.stmobile.STCommonNative;
import com.sensetime.stmobile.STHumanActionParamsType;
import com.sensetime.stmobile.STMobileAuthentificationNative;
import com.sensetime.stmobile.STMobileHumanActionNative;
import com.sensetime.stmobile.STRotateType;
import com.sensetime.stmobile.model.STHumanAction;
import com.sensetime.stmobile.model.STMobile106;
import com.sensetime.stmobile.model.STMobileFaceInfo;
import com.sensetime.stmobile.model.STMobileHandInfo;
import com.sensetime.stmobile.model.STPoint;*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by meicam-dx on 2017/10/26.
 */

public class STMobileDetected {

    private final static String PREF_ACTIVATE_CODE_FILE = "activate_code_file";
    private final static String PREF_ACTIVATE_CODE = "activate_code";

    public final static int ST_CLOCKWISE_ROTATE_0 = 0;  //< 图像不需要转向
    public final static int ST_CLOCKWISE_ROTATE_90 = 1;  //< 图像需要顺时针旋转90度
    public final static int ST_CLOCKWISE_ROTATE_180 = 2; //< 图像需要顺时针旋转180度
    public final static int ST_CLOCKWISE_ROTATE_270 = 3; //< 图像需要顺时针旋转270度

    private final String TAG = "STMobileDetected";
    private final static String LIENCSE_FILE_NAME = "facemode/tt_face.license";
    private static final String ST_MODEL_NAME_ACTION= "facemode/tt_face.model";
    private static final String ST_MODEL_NAME_FACE_ATTRIBUTE = "face_attribute_1.0.1.model";
    private static final String ST_MODEL_NAME_EYEBALL_CENTER = "M_Eyeball_Center.model";
    private static final String ST_MODEL_NAME_EYEBALL_CONTOUR = "M_Eyeball_Contour.model";
    private static final String ST_MODEL_NAME_FACE_EXTRA = "M_SenseME_Face_Extra_5.0.0.model";
    private STMobileHumanActionNative m_STHumanActionNative = new STMobileHumanActionNative();
    private STCommonNative m_STCommon = new STCommonNative();
    private int m_humanActionCreateConfig = STMobileHumanActionNative.ST_MOBILE_HUMAN_ACTION_DEFAULT_CONFIG_VIDEO;
    private long m_allHumanAction = STMobileHumanActionNative.ST_MOBILE_EYE_BLINK | STMobileHumanActionNative.ST_MOBILE_MOUTH_AH |
            STMobileHumanActionNative.ST_MOBILE_HEAD_YAW | STMobileHumanActionNative.ST_MOBILE_HEAD_PITCH |
            STMobileHumanActionNative.ST_MOBILE_BROW_JUMP | STMobileHumanActionNative.ST_MOBILE_HAND_GOOD |
            STMobileHumanActionNative.ST_MOBILE_HAND_PALM | STMobileHumanActionNative.ST_MOBILE_HAND_LOVE |
            STMobileHumanActionNative.ST_MOBILE_HAND_HOLDUP | STMobileHumanActionNative.ST_MOBILE_HAND_CONGRATULATE |
            STMobileHumanActionNative.ST_MOBILE_HAND_FINGER_HEART | STMobileHumanActionNative.ST_MOBILE_HAND_FIST |
            STMobileHumanActionNative.ST_MOBILE_HAND_OK | STMobileHumanActionNative.ST_MOBILE_HAND_SCISSOR |
            STMobileHumanActionNative.ST_MOBILE_HAND_PISTOL | STMobileHumanActionNative.ST_MOBILE_HAND_FINGER_INDEX ;
    private boolean m_isCreateHumanActionHandleSucceeded = false;
    private long m_detectConfig = 0;
    private long m_noActionStart = 0;
    private long m_noActionProidTime = 2000; //ms

    private NvsParticleSystemContext m_particleContextSelected = null; // 选中的粒子滤镜
    private NvsCaptureVideoFx m_currentCaptureVideoFx;
    private List<String> m_fxEmitterList0;
    private List<String> m_fxEmitterList1;
    private List<String> m_currentEmitterList_0;
    private List<String> m_currentEmitterList_90;
    private List<String> m_currentEmitterList_180;
    private List<String> m_currentEmitterList_270;
    private int m_fxEmitter0Place = NvsAssetPackageParticleDescParser.EMITTER_PLACE_CENTER;
    private int m_fxEmitter1Place = NvsAssetPackageParticleDescParser.EMITTER_PLACE_CENTER;
    private Object m_fxChangeObject = new Object();
    private Object m_detectedObject = new Object();

    private Context mContext;

    public boolean checkActiveCodeFromBuffer(Context context)  {

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
            while((line=br.readLine()) != null) {
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

        if (activateCode == null|| (STMobileAuthentificationNative.checkActiveCodeFromBuffer(context, licenseBuffer, licenseBuffer.length(), activateCode, activateCode.length()) != 0)) {
            activateCode = STMobileAuthentificationNative.generateActiveCodeFromBuffer(context, licenseBuffer, licenseBuffer.length());
            if (activateCode != null && activateCode.length() >0) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(PREF_ACTIVATE_CODE, activateCode);
                editor.commit();

                int result = STMobileAuthentificationNative.checkActiveCodeFromBuffer(context, licenseBuffer, licenseBuffer.length(),activateCode, activateCode.length());
                return result != 0 ? false : true;
            }
            return false;
        }

        return  true;
    }

    /**
     * 初始化商汤人脸检测
     */
    public boolean initSTMobileDetected(Context context, NvsStreamingContext streamingContext) {
        mContext = context;
        if(m_STHumanActionNative == null)
            m_STHumanActionNative = new STMobileHumanActionNative();

        m_detectConfig = 0;
        if(!checkActiveCodeFromBuffer(context))
            return  false;
        //从asset资源文件夹读取model到内存，再使用底层st_mobile_human_action_create_from_buffer接口创建handle
        int result = m_STHumanActionNative.createInstanceFromAssetFile(ST_MODEL_NAME_ACTION, m_humanActionCreateConfig, context.getAssets());
        if(result == 0){
            m_isCreateHumanActionHandleSucceeded = true;
            m_STCommon.setMouthahThreshold(0.4f);
            m_STHumanActionNative.setParam(STHumanActionParamsType.ST_HUMAN_ACTION_PARAM_BACKGROUND_BLUR_STRENGTH, 0.35f);
        }else{
            Log.e(TAG, "the result for createInstance for human_action is "+ result);
            m_isCreateHumanActionHandleSucceeded = false;
            return  false;
        }

        //采集数据回调
        streamingContext.setCapturedVideoFrameGrabberCallback(new NvsStreamingContext.CapturedVideoFrameGrabberCallback() {
            @Override
            public void onCapturedVideoFrameGrabbedArrived(final ByteBuffer image, final NvsVideoFrameInfo frameinfo) {
                stMobileDetected(image,frameinfo.frameWidth, frameinfo.frameHeight);
            }
        });

        m_noActionStart = -1;
        return  true;
    }

    public void closeDetected()
    {
        synchronized (m_detectedObject) {
            if (m_STHumanActionNative != null) {
                m_STHumanActionNative.destroyInstance();
                m_STHumanActionNative = null;
            }
        }
    }

    //设置检测类型
    public void setDetectedMode(int type) {
        if(type == NvsAssetPackageParticleDescParser.PARTICLE_TYPE_MOUTH){
            m_noActionProidTime = 100;
            m_detectConfig =  STMobileHumanActionNative.ST_MOBILE_FACE_DETECT |
                    STMobileHumanActionNative.ST_MOBILE_MOUTH_AH ;  ///<  嘴巴大张
        }else if(type == NvsAssetPackageParticleDescParser.PARTICLE_TYPE_EYE){
            m_noActionProidTime = 5000;
            m_detectConfig =  STMobileHumanActionNative.ST_MOBILE_FACE_DETECT |
                    STMobileHumanActionNative.ST_MOBILE_EYE_BLINK;///<  眨眼
        }else if(type == NvsAssetPackageParticleDescParser.PARTICLE_TYPE_GESTURE){
            m_noActionProidTime = 2000;
            m_detectConfig = STMobileHumanActionNative.ST_MOBILE_HAND_FINGER_INDEX;///<  食指指尖
            //   m_detectConfig = m_detectConfig | STMobileHumanActionNative.ST_MOBILE_HAND_PALM; ///<  手掌 4096
            //   m_detectConfig = m_detectConfig | STMobileHumanActionNative.ST_MOBILE_HAND_LOVE;///<  爱心 16384
            //   m_detectConfig = m_detectConfig | STMobileHumanActionNative.ST_MOBILE_HAND_HOLDUP; ///<  托手 32768
            //   m_detectConfig = m_detectConfig | STMobileHumanActionNative.ST_MOBILE_HAND_CONGRATULATE;///<  恭贺（抱拳） 131072
            //   m_detectConfig = m_detectConfig | STMobileHumanActionNative.ST_MOBILE_HAND_FINGER_HEART;///<  单手比爱心 262144
            //   m_detectConfig = m_detectConfig | STMobileHumanActionNative.ST_MOBILE_HAND_TWO_INDEX_FINGER;///< 平行手指 524288
            //   m_detectConfig = m_detectConfig | STMobileHumanActionNative.ST_MOBILE_HAND_OK;///< OK手势
            //   m_detectConfig = m_detectConfig | STMobileHumanActionNative.ST_MOBILE_HAND_SCISSOR;///< 剪刀手
            //   m_detectConfig = m_detectConfig | STMobileHumanActionNative.ST_MOBILE_HAND_PISTOL;///< 手枪手势
            //   m_detectConfig = m_detectConfig | STMobileHumanActionNative.ST_MOBILE_HAND_GOOD;///< 大拇哥 2048
        }else{
            m_detectConfig = 0;
            m_noActionProidTime = 2000;
        }
    }

    public void setRotateEmitter(NvsCaptureVideoFx captureVideoFx, List<String> list0, List<String> list90, List<String> list180, List<String> list270) {
        m_currentCaptureVideoFx = captureVideoFx;
        m_currentEmitterList_0 = list0;
        m_currentEmitterList_90 = list90;
        m_currentEmitterList_180 = list180;
        m_currentEmitterList_270 = list270;
    }

    //设置正在使用的粒子效果特技
    public void setCurrentParticleEffect(NvsParticleSystemContext fx, NvsAssetPackageParticleDescParser effectDescParser) {
        synchronized (m_fxChangeObject) {
            m_particleContextSelected = fx;
            if(effectDescParser != null) {
                m_fxEmitterList0 = null;
                m_fxEmitterList1 = null;
                if(effectDescParser.GetParticleType() == NvsAssetPackageParticleDescParser.PARTICLE_TYPE_EYE) {
                    m_fxEmitterList0 = effectDescParser.GetLeftEyeEmitter();
                    m_fxEmitterList1 = effectDescParser.GetRightEyeEmitter();
                    m_fxEmitter0Place = effectDescParser.GetLeftEyePlace();
                    m_fxEmitter1Place = effectDescParser.GetRightEyePlace();
                }else{
                    m_fxEmitterList0 = effectDescParser.GetParticlePartitionEmitter(0);
                    m_fxEmitter0Place = effectDescParser.GetParticlePartitionPlace(0);
                }

                setDetectedMode(effectDescParser.GetParticleType());
            }else{
                setDetectedMode(0);
            }
        }
    }

    private boolean checkDetectedFlag(long action, long flag) {
        long res = action & flag;
        return res == 0 ? false : true;
    }

    /**
     * 商汤人脸检测
     */
    private void stMobileDetected(ByteBuffer ImageData, int width, int height)
    {
        if(m_particleContextSelected == null)
            return;
        if(!m_isCreateHumanActionHandleSucceeded)
            return;
        if(m_detectConfig < STMobileHumanActionNative.ST_MOBILE_FACE_DETECT)
            return;

        if(m_STHumanActionNative == null)
            return;

        int faceRotation = getHumanActionOrientation();

        STHumanAction humanAction = null;
        synchronized (m_detectedObject) {
            humanAction = m_STHumanActionNative.humanActionDetect(ImageData.array(), STCommonNative.ST_PIX_FMT_NV21,m_detectConfig, faceRotation, width, height);
        }

        if(humanAction != null) {
            if (humanAction.hands != null && humanAction.hands.length > 0) {
                if(checkDetectedFlag(humanAction.hands[0].handAction, m_allHumanAction))
                {
                    m_noActionStart = System.currentTimeMillis();
                    enableParticle(true);
                    stMobileDetectedHandInfo(humanAction.hands[0],width,height);
                }
            }

            if (humanAction.faces != null && humanAction.faces.length > 0) {
                if(checkDetectedFlag(humanAction.faces[0].faceAction, m_allHumanAction)) {
                    m_noActionStart = System.currentTimeMillis();
//                    enableParticle(true);
                    enableRotateParticle(faceRotation);
                    stMobileDetectedFaceInfo(humanAction.faces[0], width, height);
                }else {
                    //如果是眨眼动作,继续跟随眼部运动
                    boolean isEyeBlinkActionDetected = ((m_detectConfig & STMobileHumanActionNative.ST_MOBILE_EYE_BLINK) > 0 ? true : false);
                    if(m_noActionStart > 0 && isEyeBlinkActionDetected){
                        STMobile106 arrayFaces = humanAction.faces[0].getFace();

                        changeParticleParamFroEye(arrayFaces, width, height);
                    }
                }
            }
        }

        long currentTime = System.currentTimeMillis();
        if(m_noActionStart > 0){
            if((currentTime - m_noActionStart) > m_noActionProidTime){

                enableParticle(false);
                m_noActionStart = -1;
                Log.e(TAG, "NO Action Detected");
            }
        }

    }

    private void stMobileDetectedFaceInfo(STMobileFaceInfo info, int width, int height)
    {
        STPoint currentPoint = null;
        STMobile106 arrayFaces = info.getFace();
        float fRateGain = 1.0f;
        if(checkDetectedFlag(info.faceAction, STMobileHumanActionNative.ST_MOBILE_MOUTH_AH)){

            STPoint[] points = arrayFaces.getPointsArray();
            STPoint point98 = points[98];
            STPoint point102 = points[102];

            STPoint point96 = points[96];
            STPoint point100 = points[100];

            float distance = point102.getY() - point98.getY();
            float mouthWidth = point100.getX() - point96.getX();

            if(m_fxEmitter0Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_CENTER) {
                if (point102.getY() < point98.getY()) {
                    currentPoint = point102;
                } else {
                    float newY = point98.getY() + distance / 2;
                    currentPoint = new STPoint(point102.getX(), newY);
                }
            }else if(m_fxEmitter0Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_LEFT){
                currentPoint = point96;
            }else if(m_fxEmitter0Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_RIGHT){
                currentPoint = point100;
            }else if(m_fxEmitter0Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_TOP){
                currentPoint = point98;
            }else if(m_fxEmitter0Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_BOTTOM){
                currentPoint = point102;
            }

            if (mouthWidth > 0) {
                fRateGain = distance / (mouthWidth / 2);
                fRateGain = (float) Math.exp(fRateGain * fRateGain / 2) - 1;
                if (fRateGain > 5.0f)
                    fRateGain = 5.0f;
                if (fRateGain < 0.1f)
                    fRateGain = 0.1f;
            }

            Log.e(TAG, "face action mouthAH(嘴巴大张) rate:" + fRateGain);

        }
        if(checkDetectedFlag(info.faceAction, STMobileHumanActionNative.ST_MOBILE_EYE_BLINK)){
            changeParticleParamFroEye(arrayFaces, width, height);
            Log.e(TAG, "face action mouthAH(眨眼)");
            return;
        }
        if(checkDetectedFlag(info.faceAction, STMobileHumanActionNative.ST_MOBILE_HEAD_YAW)){
            Log.e(TAG, "face action YAW(摇头)");
        }
        if(checkDetectedFlag(info.faceAction, STMobileHumanActionNative.ST_MOBILE_HEAD_PITCH)){
            Log.e(TAG, "face action pitch(点头)");
        }
        if(checkDetectedFlag(info.faceAction, STMobileHumanActionNative.ST_MOBILE_BROW_JUMP)){
            Log.e(TAG, "face action pitch(眉毛挑动)");
        }

        if(currentPoint != null && m_particleContextSelected != null){
            float normalX = currentPoint.getX() - (width / 2);
            float normalY = (height / 2)- currentPoint.getY();
            changeParticleParam(normalX, normalY,fRateGain, width, height);
        }
    }

    private void stMobileDetectedHandInfo(STMobileHandInfo info, int width, int height)
    {
        NvsPosition2D particlePos = null;
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_HOLDUP)){
            Log.e(TAG, "hand action holdup(托手)");
        }
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_CONGRATULATE)){
            Log.e(TAG, "hand action congratulate(抱拳)");
        }
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_FINGER_HEART)){
            Log.e(TAG, "hand action fingerHeart(单手比爱心)");
        }
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_GOOD)){
            Log.e(TAG, "hand action good(大拇哥)");
        }
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_LOVE)){
            Log.e(TAG, "hand action love(爱心)");
        }
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_PALM)){
            Log.e(TAG, "hand action palm(手掌)");
        }
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_FIST)){
            Log.e(TAG, "hand action fist(拳头)");
        }
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_PISTOL)){
            Log.e(TAG, "hand action love(手枪手势)");
        }
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_SCISSOR)){
            Log.e(TAG, "hand action love(剪刀手)");
        }
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_OK)){
            Log.e(TAG, "hand action love(OK手势)");
        }
        if(checkDetectedFlag(info.handAction, STMobileHumanActionNative.ST_MOBILE_HAND_FINGER_INDEX)){
            Log.e(TAG, "hand action love(食指指尖)");
        }

        if(info.keyPointsCount > 0){
            particlePos =  new NvsPosition2D(info.keyPoints[0].getX(),info.keyPoints[0].getY());
        }

        if(particlePos == null){
            Rect rc = info.handRect.convertToRect();
            particlePos =  new NvsPosition2D(rc.centerX(),rc.centerY());
        }

        if(particlePos != null && m_particleContextSelected != null){
            float normalX = particlePos.x - (width / 2);
            float normalY = (height / 2)- particlePos.y ;
            changeParticleParam(normalX, normalY, 1.0f, width, height);
        }
    }

    private void enableParticle(boolean enable)
    {
        synchronized (m_fxChangeObject) {
            if (m_particleContextSelected == null)
                return;

            if(m_fxEmitterList0 != null) {
                for (int i = 0; i < m_fxEmitterList0.size(); i++)
                    m_particleContextSelected.setEmitterEnabled(m_fxEmitterList0.get(i), enable);
            }

            if(m_fxEmitterList1 != null) {
                for (int i = 0; i < m_fxEmitterList1.size(); i++)
                    m_particleContextSelected.setEmitterEnabled(m_fxEmitterList1.get(i), enable);
            }
        }
    }

    private void enableRotateParticle(int faceRotation) {
        if (m_particleContextSelected == null) {
            return;
        }
        List<String> disableList = new ArrayList<>();
        List<String> enableList = new ArrayList<>();
        if(faceRotation ==  STRotateType.ST_CLOCKWISE_ROTATE_0) {
            enableList.addAll(m_currentEmitterList_0);
            disableList.addAll(m_currentEmitterList_90);
            disableList.addAll(m_currentEmitterList_180);
            disableList.addAll(m_currentEmitterList_270);
        } else if(faceRotation == STRotateType.ST_CLOCKWISE_ROTATE_90) {
            enableList.addAll(m_currentEmitterList_90);
            disableList.addAll(m_currentEmitterList_0);
            disableList.addAll(m_currentEmitterList_180);
            disableList.addAll(m_currentEmitterList_270);
        } else if(faceRotation == STRotateType.ST_CLOCKWISE_ROTATE_180) {
            enableList.addAll(m_currentEmitterList_180);
            disableList.addAll(m_currentEmitterList_0);
            disableList.addAll(m_currentEmitterList_90);
            disableList.addAll(m_currentEmitterList_270);
        }  else if(faceRotation == STRotateType.ST_CLOCKWISE_ROTATE_270) {
            enableList.addAll(m_currentEmitterList_270);
            disableList.addAll(m_currentEmitterList_0);
            disableList.addAll(m_currentEmitterList_90);
            disableList.addAll(m_currentEmitterList_180);
        }
        for(int i = 0; i < disableList.size(); ++i) {
            String emitterName = disableList.get(i);
            m_particleContextSelected.setEmitterEnabled(emitterName, false);
        }
        for(int i = 0; i < enableList.size(); ++i) {
            String emitterName = enableList.get(i);
            if(m_fxEmitterList0.contains(emitterName) || m_fxEmitterList1.contains(emitterName)) {
                Log.e("===>", "emitter name: " + emitterName);
                m_particleContextSelected.setEmitterEnabled(emitterName, true);
            } else {
                m_particleContextSelected.setEmitterEnabled(emitterName, false);
            }
        }
    }

    private void changeParticleParam(float x, float y, float rateGain, int imageWidth, int imageHeight)
    {
        Log.e(TAG, "Particle Position x:" + x + " Y:" + y);
        synchronized (m_fxChangeObject) {
            if(m_particleContextSelected == null)
                return;
            for(int i = 0; i < m_fxEmitterList0.size(); i++) {
                String emitterName = m_fxEmitterList0.get(i);

                PointF pointF = convertCoord(imageWidth, imageHeight, new PointF(x, y));
                m_particleContextSelected.setEmitterPosition(emitterName, pointF.x, pointF.y);
                if (rateGain > 0)
                    m_particleContextSelected.setEmitterRateGain(emitterName, rateGain);
            }
        }
    }

    private void changeParticleParamFroEye(STMobile106 facePoints, int imageWidth, int imageHeight)
    {
        STPoint[] points = facePoints.getPointsArray();
        STPoint leftEye = points[74];
        if(m_fxEmitter0Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_LEFT)
            leftEye = points[52];
        else if(m_fxEmitter0Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_RIGHT)
            leftEye = points[55];
        else if(m_fxEmitter0Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_TOP)
            leftEye = points[72];
        else if(m_fxEmitter0Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_BOTTOM)
            leftEye = points[73];

        STPoint rightEye = points[77];
        if(m_fxEmitter1Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_LEFT)
            rightEye = points[58];
        else if(m_fxEmitter1Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_RIGHT)
            rightEye = points[61];
        else if(m_fxEmitter1Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_TOP)
            rightEye = points[75];
        else if(m_fxEmitter1Place == NvsAssetPackageParticleDescParser.EMITTER_PLACE_BOTTOM)
            rightEye = points[76];

        float LeftEyeX = leftEye.getX() - (imageWidth / 2);
        float LeftEyeY = (imageHeight / 2)- leftEye.getY();

        float RightEyeX = rightEye.getX() - (imageWidth / 2);
        float RightEyeY = (imageHeight / 2)- rightEye.getY();

        synchronized (m_fxChangeObject) {
            if (m_particleContextSelected == null)
                return;

            if(m_fxEmitterList0 != null){
                for(int i = 0; i < m_fxEmitterList0.size(); i++) {
                    String emitterName = m_fxEmitterList0.get(i);

                    PointF pointF = convertCoord(imageWidth, imageHeight, new PointF(LeftEyeX, LeftEyeY));
                    m_particleContextSelected.setEmitterPosition(emitterName, pointF.x, pointF.y);
                }
            }

            if(m_fxEmitterList1 != null){
                for(int i = 0; i < m_fxEmitterList1.size(); i++) {
                    String emitterName = m_fxEmitterList1.get(i);

                    PointF pointF = convertCoord(imageWidth, imageHeight, new PointF(RightEyeX, RightEyeY));
                    m_particleContextSelected.setEmitterPosition(emitterName, pointF.x, pointF.y);
                }
            }
        }
    }

    private PointF convertCoord(int imageWidth, int imageHeight, PointF pointF) {
        return m_currentCaptureVideoFx.mapPointFromImageCoordToParticeSystemCoord(imageWidth, imageHeight, pointF);
    }

    public int cameraOrientation(int rotation) {
        int cameraOrientation = 0;
        if(rotation == 0) {
            cameraOrientation = 0;
        } else if(rotation == 1) {
            cameraOrientation = 90;
        } else if(rotation == 2) {
            cameraOrientation = 180;
        } else if(rotation == 3) {
            cameraOrientation = 270;
        }
        return cameraOrientation;
    }

    private int getHumanActionOrientation() {
        //获取重力传感器返回的方向
        int orientation = Accelerometer.getDirection();
        int cameraOrientation = cameraOrientation(orientation);

        int st_orientation = STRotateType.ST_CLOCKWISE_ROTATE_0;
        if(cameraOrientation == 90) {
            st_orientation = STRotateType.ST_CLOCKWISE_ROTATE_90;
        } else if(cameraOrientation == 180) {
            st_orientation = STRotateType.ST_CLOCKWISE_ROTATE_180;
        } else if(cameraOrientation == 270) {
            st_orientation = STRotateType.ST_CLOCKWISE_ROTATE_270;
        }
        return st_orientation;
    }
}
