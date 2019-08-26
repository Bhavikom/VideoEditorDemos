package com.meishe.sdkdemo.douvideo.bean;

import android.util.Log;

import com.meishe.sdkdemo.utils.dataInfo.MusicInfo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class RecordClipsInfo implements Serializable {
    private static final String TAG = "RecordClipInfo";
    private boolean mIsConverted;//转码完成标志
    private ArrayList<RecordClip> mClipList;
    private ArrayList<RecordClip> mReverseClipList;
    private MusicInfo mMusicInfo;

    public RecordClipsInfo(){
        mClipList = new ArrayList<>();
        mReverseClipList = new ArrayList<>();
        mIsConverted = false;
    }

    public RecordClip getClipByPath(String path){
        if(mClipList == null){
            return null;
        }

        for(int i = 0; i < mClipList.size(); i++){
            RecordClip clip = mClipList.get(i);
            if(path.equals(clip.getFilePath())){
                return clip;
            }
        }

        return null;
    }

    public RecordClip getReverseClipByPath(String path){
        if(mReverseClipList == null){
            return null;
        }
        for(int i = 0; i < mReverseClipList.size(); i++){
            RecordClip clip = mReverseClipList.get(i);
            if(path.equals(clip.getFilePath())){
                return clip;
            }
        }
        return null;
    }

    public void setMusicInfo(MusicInfo musicInfo){
        mMusicInfo = musicInfo;
    }

    public MusicInfo getMusicInfo(){
        return mMusicInfo;
    }

    public void setIsConvert(boolean isConvert){
        mIsConverted = isConvert;
    }

    public boolean getIsConvert(){
        return mIsConverted;
    }

    public boolean addClip(RecordClip clip){
        mIsConverted = false;
        return mClipList.add(clip);
    }

    public RecordClip removeLastClip(){
        int size = mClipList.size();
        if( size > 0){
            final RecordClip clip = mClipList.remove(size-1);

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    File file = new File(clip.getFilePath());
                    if(file.exists()){
                        try {
                            file.delete();
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e(TAG, "删除视频失败!");
                        }
                    }
                }
            }.start();

            return clip;
        }
        return null;
    }

    public long getClipsDurationBySpeed(){
        long duration = 0;
        for(int i = 0; i < mClipList.size(); i++){
            RecordClip clip = mClipList.get(i);
            duration += clip.getDurationBySpeed();
        }
        return duration;
    }

    public ArrayList<RecordClip> getClipList(){
        return mClipList;
    }

    public ArrayList<RecordClip> getReverseClipList(){
        return mReverseClipList;
    }
}
