package com.meishe.sdkdemo.utils.dataInfo;


import android.graphics.Bitmap;

import java.io.Serializable;

public class MusicInfo implements Serializable{
    private String m_title;
    private String m_artist;
    private String m_imagePath;
    private String m_filePath;
    private String m_fileUrl;
    private String m_assetPath;
    private Bitmap m_image;
    private long m_duration;
    private long m_inPoint;
    private long m_outPoint;
    private long m_trimIn;
    private long m_trimOut;
    private int m_mimeType;
    private boolean m_prepare;
    private boolean m_isHttpMusic;
    private boolean m_isAsset;
    private boolean m_play;
    private float m_volume;
    private long m_originalInPoint;
    private long m_originalOutPoint;
    private long m_originalTrimIn;
    private long m_originalTrimOut;
    private int m_extraMusic;
    private long m_extraMusicLeft;
    private long m_fadeDuration;

    private String m_lrcPath;

    // 给exoplayer使用的path
    private String m_exoPlayerPath;

    public MusicInfo() {
        m_title = null;
        m_artist = null;
        m_imagePath = null;
        m_filePath = null;
        m_fileUrl = null;
        m_assetPath = null;
        m_image = null;
        m_duration = 0;
        m_inPoint = 0;
        m_outPoint = 0;
        m_trimIn = 0;
        m_trimOut = 0;
        m_originalInPoint = 0;
        m_originalOutPoint = 0;
        m_originalTrimIn = 0;
        m_originalTrimOut = 0;
        m_mimeType = 0;
        m_prepare = false;
        m_isHttpMusic = false;
        m_isAsset = false;
        m_play = false;
        m_volume = 1.0f;
        m_extraMusic = 0;
        m_extraMusicLeft = 0;
        m_fadeDuration = 0;
        m_lrcPath = "";
    }

    public MusicInfo clone() {
        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setFileUrl(this.getFileUrl());
        musicInfo.setFilePath(this.getFilePath());
        musicInfo.setDuration(this.getDuration());
        musicInfo.setArtist(this.getArtist());
        musicInfo.setImage(this.getImage());
        musicInfo.setImagePath(this.getImagePath());
        musicInfo.setTitle(this.getTitle());
        musicInfo.setTrimIn(this.getTrimIn());
        musicInfo.setTrimOut(this.getTrimOut());
        musicInfo.setMimeType(this.getMimeType());
        musicInfo.setIsAsset(this.isAsset());
        musicInfo.setPrepare(this.isPrepare());
        musicInfo.setPlay(this.isPlay());
        musicInfo.setIsHttpMusic(this.isHttpMusic());
        musicInfo.setAssetPath(this.getAssetPath());
        musicInfo.setInPoint(this.getInPoint());
        musicInfo.setOutPoint(this.getOutPoint());
        musicInfo.setVolume(this.getVolume());
        musicInfo.setOriginalInPoint(this.getOriginalInPoint());
        musicInfo.setOriginalOutPoint(this.getOriginalOutPoint());
        musicInfo.setOriginalTrimIn(this.getOriginalTrimIn());
        musicInfo.setOriginalTrimOut(this.getOriginalTrimOut());
        musicInfo.setExtraMusic(this.getExtraMusic());
        musicInfo.setExtraMusicLeft(this.getExtraMusicLeft());
        musicInfo.setFadeDuration(this.getFadeDuration());
        musicInfo.setLrcPath(this.getLrcPath());
        return musicInfo;
    }

    public void setTitle(String title) {
        m_title = title;
    }

    public String getTitle() {
        return m_title;
    }

    public void setArtist(String artist) {
        m_artist = artist;
    }

    public String getArtist() {
        return m_artist;
    }

    public void setImagePath(String filePath) {
        m_imagePath = filePath;
    }

    public String getImagePath() {
        return m_imagePath;
    }

    public void setFilePath(String filePath) {
        m_filePath = filePath;
    }

    public String getFilePath() {
        return m_filePath;
    }

    public void setFileUrl(String url) {
        m_fileUrl = url;
    }

    public String getFileUrl() {
        return m_fileUrl;
    }

    public String getAssetPath() {
        return m_assetPath;
    }

    public void setAssetPath(String m_assetPath) {
        this.m_assetPath = m_assetPath;
    }

    public void setImage(Bitmap bitmap) {
        m_image = bitmap;
    }

    public Bitmap getImage() {
        return m_image;
    }

    public void setInPoint(long time) {
        m_inPoint = time;
    }

    public long getInPoint() {
        return m_inPoint;
    }

    public void setOutPoint(long time) {
        m_outPoint = time;
    }

    public long getOutPoint() {
        return m_outPoint;
    }

    public void setDuration(long time) {
        m_duration = time;
    }

    public long getDuration() {
        return m_duration;
    }

    public void setTrimIn(long time) {
        m_trimIn = time;
    }

    public long getTrimIn() {
        return m_trimIn;
    }

    public void setTrimOut(long time) {
        m_trimOut = time;
    }

    public long getTrimOut() {
        return m_trimOut;
    }

    public int getMimeType() {
        return m_mimeType;
    }

    public void setMimeType(int m_mimeType) {
        this.m_mimeType = m_mimeType;
    }

    public boolean isPrepare() {
        return m_prepare;
    }

    public void setPrepare(boolean m_prepare) {
        this.m_prepare = m_prepare;
    }

    public boolean isHttpMusic() {
        return m_isHttpMusic;
    }

    public void setIsHttpMusic(boolean m_isHttpMusic) {
        this.m_isHttpMusic = m_isHttpMusic;
    }

    public boolean isAsset() {
        return m_isAsset;
    }

    public void setIsAsset(boolean m_isAsset) {
        this.m_isAsset = m_isAsset;
    }

    public boolean isPlay() {
        return m_play;
    }

    public void setPlay(boolean m_play) {
        this.m_play = m_play;
    }

    public float getVolume() {
        return m_volume;
    }

    public void setVolume(float volume) {
        this.m_volume = volume;
    }

    public long getOriginalInPoint() {
        return m_originalInPoint;
    }

    public void setOriginalInPoint(long m_originalTrimIn) {
        this.m_originalInPoint = m_originalTrimIn;
    }

    public long getOriginalOutPoint() {
        return m_originalOutPoint;
    }

    public void setOriginalOutPoint(long m_originalTrimOut) {
        this.m_originalOutPoint = m_originalTrimOut;
    }

    public long getOriginalTrimIn() {
        return m_originalTrimIn;
    }

    public void setOriginalTrimIn(long m_originalTrimIn) {
        this.m_originalTrimIn = m_originalTrimIn;
    }

    public long getOriginalTrimOut() {
        return m_originalTrimOut;
    }

    public void setOriginalTrimOut(long m_originalTrimOut) {
        this.m_originalTrimOut = m_originalTrimOut;
    }

    public int getExtraMusic() {
        return m_extraMusic;
    }

    public void setExtraMusic(int m_extraMusic) {
        this.m_extraMusic = m_extraMusic;
    }

    public long getExtraMusicLeft() {
        return m_extraMusicLeft;
    }

    public void setExtraMusicLeft(long m_extraMusicLeft) {
        this.m_extraMusicLeft = m_extraMusicLeft;
    }

    public long getFadeDuration() {
        return m_fadeDuration;
    }

    public void setFadeDuration(long m_fadeDuration) {
        this.m_fadeDuration = m_fadeDuration;
    }

    public void setExoplayerPath(String path){
        m_exoPlayerPath = path;
    }

    public String getExoPlayerPath(){
        return m_exoPlayerPath;
    }

    public String getLrcPath() {
        return m_lrcPath;
    }

    public void setLrcPath(String m_lrcPath) {
        this.m_lrcPath = m_lrcPath;
    }
}
