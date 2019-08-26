package com.meishe.sdkdemo.douvideo;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;

/*import com.meicam.nvconvertorlib.NvFileConvertConfig;
import com.meicam.nvconvertorlib.NvFileConvertProcess;*/
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.sdkdemo.douvideo.bean.RecordClip;
import com.meishe.sdkdemo.douvideo.bean.RecordClipsInfo;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.PathUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by ms on 2018/9/7.
 * 将拍摄的视频片段转换为倒放片段
 */

public class ConvertFiles /*implements NvFileConvertProcess.NvFileConvertProcessNotify, NvFileConvertProcess.NvFileConvertProgressNotify*/ {

    private static final String TAG = "ConvertFiles";
    private static final int MESSAGE_CONVERT_START = 1;
    private static final int MESSAGE_CONVERT_FINISH = 2;
    private static final int MESSAGE_CONVERT_CANCEL = 3;
    private RecordClipsInfo mClipsInfo;
   // private NvFileConvertProcess mFileConvertor;
    private ArrayList<RecordClip> mClipList;
    private ArrayList<RecordClip> mReverseClipList;
    //private NvFileConvertConfig mConfig;
    private String mDir;
    private int mIndex = 0;
    private ConvertThread mConvertThread;
    private Handler mConvertHandler;
    private Handler mUIHandler;
    private int mFinishCode;
    private Activity mActivity;

    private class ConvertThread extends HandlerThread{

        public ConvertThread(String name) {
            super(name);
        }

        public ConvertThread(String name, int priority) {
            super(name, priority);
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            convertPrePare();
        }
    }

    public ConvertFiles(RecordClipsInfo info, String dir, Handler handler, int finishCode, Activity activity){
        mActivity = activity;
        mFinishCode = finishCode;
        mUIHandler = handler;
        mClipsInfo = info;
        mClipList = mClipsInfo.getClipList();
        mReverseClipList = mClipsInfo.getReverseClipList();
        mReverseClipList.clear();
        mDir = dir;
        File fileDir = new File(dir);
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        convertPrePare();
        mConvertThread = new ConvertThread("convert thread");
        mConvertThread.start();
        mConvertHandler = new Handler(mConvertThread.getLooper()){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case MESSAGE_CONVERT_START:{
                        if(!convertFile()){
                            clipConvertComplete(null, false);
                        }
                        break;
                    }
                    case MESSAGE_CONVERT_FINISH:{
                        finishConvert();
                        mConvertThread.quit();
                        break;
                    }
                    case MESSAGE_CONVERT_CANCEL:{
                        cancelConvert();
                        mConvertThread.quit();
                        break;
                    }
                }

            }
        };
    }

    private void convertPrePare(){
        /*mFileConvertor =  new NvFileConvertProcess(this);
        mFileConvertor.SetProgressNotify(this);
        mConfig = new NvFileConvertConfig();
        //缩放设置
        mConfig.videoResolution = NvFileConvertConfig.RESIZE_OUTPUT_VIDEO_RESOLUTION_NOT_RESIZE;
        //转换长度设置,以秒为单位
        mConfig.convertStart = 0;
        //可以设置为MAX_VALUE,convertor会根据输入的文件长度自动设置
        mConfig.convertEnd = Integer.MAX_VALUE;
        float tmp = mConfig.convertStart;
        mConfig.convertStart = mConfig.convertEnd;
        mConfig.convertEnd = tmp;*/


//        InputStream is = null;
//
//        try {
//            is = mActivity.getAssets().open("meishesdk.lic");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        byte[] licdata = new byte[1024];
//        int count = 0;
//        int idex = 0;
//
//        try {
//            count = is.available();
//            while (count != 0) {
//                is.read(licdata, idex, count);
//                idex +=count;
//                count = is.available();
//            }
//            NvFileConvertProcess.InstallLicense(mActivity, licdata);
//        } catch (Exception e) {
//            count = 0;
//            e.printStackTrace();
//        }

    }

    public void sendConvertFileMsg(){
        mConvertHandler.sendEmptyMessage(MESSAGE_CONVERT_START);
    }

    public void sendFinishConvertMsg(){
        mConvertHandler.sendEmptyMessage(MESSAGE_CONVERT_FINISH);
    }

    public void sendCancelConvertMsg(){
        /**
         * 这里通过反射解决异常：Handler sending message to a Handler on a dead thread
         */
        Field messageQueueField = null;
        try {
            messageQueueField = Looper.class.getDeclaredField("mQueue");
            messageQueueField.setAccessible(true);
            Class<MessageQueue> messageQueueClass = (Class<MessageQueue>) Class.forName("android.os.MessageQueue");
            Constructor<MessageQueue>[] messageQueueConstructor = (Constructor<MessageQueue>[]) messageQueueClass.getDeclaredConstructors();
            for(Constructor<MessageQueue> constructor : messageQueueConstructor){
                constructor.setAccessible(true);
                Class[] types = constructor.getParameterTypes();
                for(Class clazz : types){
                    if(clazz.getName().equalsIgnoreCase("boolean")){
                        messageQueueField.set(mConvertHandler.getLooper(), constructor.newInstance(true));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mConvertHandler.sendEmptyMessage(MESSAGE_CONVERT_CANCEL);
    }

    private void finishConvert(){
//        mFileConvertor.Close();
    }

    private void cancelConvert(){
        //mFileConvertor.cancle();
        Log.d("1234", "cancelConvert: mFileConvertor.cancle()" );
    }

    public boolean convertFile(){
        if(mIndex > mClipList.size() - 1 || mClipList.isEmpty()){
            return false;
        }
        String mSrcPath = mClipList.get(mIndex).getFilePath();
        String mDstPath = mDir + File.separator + PathUtils.getFileName(mSrcPath);
       /* mConfig.convertStart = microSecondToSecond(mClipList.get(mIndex).getTrimOut());
        mConfig.convertEnd = microSecondToSecond(mClipList.get(mIndex).getTrimIn());
        mConfig.gopsize = 5;//
        Logger.e(TAG,"video convertStart =  " + mConfig.convertStart + "-->convertEnd" + mConfig.convertEnd);*/

        /*mFileConvertor.setHasAudioOutput(false);//去掉音頻
        //设置转换路径,和转换设置
        int nRet = mFileConvertor.Open(mSrcPath, mDstPath, mConfig);
        if(nRet != NvFileConvertProcess.NV_NOERROR) {
            Log.d(TAG, "转码失败！" + nRet);
            return false;
        }

        int startRet = mFileConvertor.Start();
        if(startRet != NvFileConvertProcess.NV_NOERROR){
            Log.d(TAG, "转码失败！！" + startRet);
            return false;
        }*/

        return true;
    }

    /*@Override
    public void convertComplete(String newPath) {
        clipConvertComplete(newPath, true);
    }*/

    public void clipConvertComplete(String newPath, boolean isSuccess){
        //mFileConvertor.Close();
        RecordClip clip = mClipList.get(mIndex);
        clip.setIsConvertSuccess(isSuccess);
        RecordClip reverseClip = new RecordClip();
        boolean isCaptureVideo = clip.isCaptureVideo();
        reverseClip.setFilePath(isSuccess ? newPath : clip.getFilePath());
        reverseClip.setIsConvertSuccess(isSuccess);
        long duration = clip.getDuration();
        if(isSuccess){//转码成功,获取转码后视频时长
            NvsAVFileInfo info = NvsStreamingContext.getInstance().getAVFileInfo(newPath);
            if(info != null){
                duration = info.getDuration();
            }
        }
        reverseClip.setDuration(duration);
        reverseClip.setCaptureVideo(isCaptureVideo);
        if(!isCaptureVideo && !isSuccess){//本地素材视频,如果转码失败，则设置裁剪点
            reverseClip.setTrimIn(clip.getTrimIn());
            reverseClip.setTrimOut(clip.getTrimOut());
        }
        reverseClip.setDurationBySpeed(clip.getDurationBySpeed());
        reverseClip.setSpeed(clip.getSpeed());
        reverseClip.setRotateAngle(clip.getRotateAngle());
        mReverseClipList.add(reverseClip);
        mIndex++;
        if(mIndex < mClipList.size()){
            sendConvertFileMsg();
            Logger.e(TAG,"当前转码视频Index-->" + mIndex);
        }else {
            sendFinishConvertMsg();
            mUIHandler.sendEmptyMessage(mFinishCode);
        }
    }

    /*@Override
    public void convertProgress(int i) {
        Log.d(TAG, "convertProgress: " + i);
    }*/

    //微秒转化成秒
    private float microSecondToSecond(long timeStamp){
        float second = timeStamp / (float) Constants.NS_TIME_BASE;
        return second;
    }

    public void onConvertDestory(){
        if(null != mConvertHandler){
            mConvertHandler.removeCallbacksAndMessages(null);
        }
    }
}
