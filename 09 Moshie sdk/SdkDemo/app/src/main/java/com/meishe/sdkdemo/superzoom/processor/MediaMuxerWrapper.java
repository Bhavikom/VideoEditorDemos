package com.meishe.sdkdemo.superzoom.processor;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MediaMuxerWrapper {
    private static final boolean DEBUG = false;    // TODO set false on release
    private static final String TAG = "MediaMuxerWrapper";

    private static final String DIR_NAME = "SenseMeEffects";
    private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);

    private String mOutputPath;
    private final MediaMuxer mMediaMuxer;    // API >= 18
    private int mEncoderCount, mStatredCount;
    private boolean mIsStarted;
    private MediaEncoder mVideoEncoder, mAudioEncoder;
    public Handler mHandler;
    public static final int MSG_FINISH_RECORDING = 100;

    private long index0StartTime;
    private long index1StartTime;
    private boolean hasGetIndex0StartTime = false;
    private boolean hasGetIndex1StartTime = false;

    /**
     * Constructor
     *
     * @param ext extension of output file
     * @throws IOException
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public MediaMuxerWrapper(String ext) throws IOException {
        if (TextUtils.isEmpty(ext)) ext = ".mp4";
        try {
            mOutputPath = getCaptureFile(Environment.DIRECTORY_MOVIES, ext).toString();
        } catch (final NullPointerException e) {
            throw new RuntimeException("This app has no permission of writing external storage");
        }
        mMediaMuxer = new MediaMuxer(mOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        mEncoderCount = mStatredCount = 0;
        mIsStarted = false;
    }

    public String getOutputPath() {
        return mOutputPath;
    }

    public void prepare() throws IOException {
        if (mVideoEncoder != null)
            mVideoEncoder.prepare();
        if (mAudioEncoder != null)
            mAudioEncoder.prepare();
    }

    public void startRecording() {
        hasGetIndex0StartTime = false;
        hasGetIndex1StartTime = false;
        if (mVideoEncoder != null)
            mVideoEncoder.startRecording();
        if (mAudioEncoder != null)
            mAudioEncoder.startRecording();
    }

    public void stopRecording() {
        if (mVideoEncoder != null)
            mVideoEncoder.stopRecording();
        mVideoEncoder = null;
        if (mAudioEncoder != null)
            mAudioEncoder.stopRecording();
        mAudioEncoder = null;
    }

    public synchronized boolean isStarted() {
        return mIsStarted;
    }

//**********************************************************************
//**********************************************************************

    /**
     * assign encoder to this calss. this is called from encoder.
     *
     * @param encoder instance of MediaVideoEncoder or MediaAudioEncoder
     */
    /*package*/ void addEncoder(final MediaEncoder encoder) {
        if (encoder instanceof MediaVideoEncoder) {
            if (mVideoEncoder != null)
                throw new IllegalArgumentException("Video encoder already added.");
            mVideoEncoder = encoder;
        } else if (encoder instanceof MediaAudioEncoder) {
            if (mAudioEncoder != null)
                throw new IllegalArgumentException("Video encoder already added.");
            mAudioEncoder = encoder;
        } else
            throw new IllegalArgumentException("unsupported encoder");
        mEncoderCount = (mVideoEncoder != null ? 1 : 0) + (mAudioEncoder != null ? 1 : 0);
    }

    /**
     * request start recording from encoder
     *
     * @return true when muxer is ready to write
     */
    /*package*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    synchronized boolean start() {
        if (DEBUG) Log.v(TAG, "start:");
        mStatredCount++;
        if ((mEncoderCount > 0) && (mStatredCount == mEncoderCount)) {
            mMediaMuxer.start();
            mIsStarted = true;
            notifyAll();
            if (DEBUG) Log.v(TAG, "MediaMuxer started:");
        }
        return mIsStarted;
    }

    /**
     * request stop recording from encoder when encoder received EOS
     */
    /*package*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    synchronized void stop() {
        if (DEBUG) Log.v(TAG, "stop:mStatredCount=" + mStatredCount);
        mStatredCount--;
        if ((mEncoderCount > 0) && (mStatredCount <= 0)) {
            mMediaMuxer.stop();
            mMediaMuxer.release();
            mIsStarted = false;
            if (DEBUG) Log.v(TAG, "MediaMuxer stopped:");
            if (mHandler != null) {
                Message msg = mHandler.obtainMessage(MSG_FINISH_RECORDING);
                mHandler.sendMessage(msg);
            }
        }
    }

    /**
     * assign encoder to muxer
     *
     * @param format
     * @return minus value indicate error
     */
    /*package*/
    synchronized int addTrack(final MediaFormat format) {
        if (mIsStarted)
            throw new IllegalStateException("muxer already started");
        final int trackIx = mMediaMuxer.addTrack(format);
        if (DEBUG)
            Log.i(TAG, "addTrack:trackNum=" + mEncoderCount + ",trackIx=" + trackIx + ",format=" + format);
        return trackIx;
    }

    /**
     * write encoded data to muxer
     *
     * @param trackIndex
     * @param byteBuf
     * @param bufferInfo
     */
    /*package*/
    synchronized void writeSampleData(final int trackIndex, final ByteBuffer byteBuf, final MediaCodec.BufferInfo bufferInfo) {
        if (trackIndex == 0) {
            if (!hasGetIndex0StartTime) {
                index0StartTime = bufferInfo.presentationTimeUs;
                hasGetIndex0StartTime = true;
            }
            bufferInfo.presentationTimeUs -= index0StartTime;
        }

        if (trackIndex == 1) {
            if (!hasGetIndex1StartTime) {
                index1StartTime = bufferInfo.presentationTimeUs;
                hasGetIndex1StartTime = true;
            }
            bufferInfo.presentationTimeUs -= index1StartTime;
        }

        if (mStatredCount > 0)
            mMediaMuxer.writeSampleData(trackIndex, byteBuf, bufferInfo);
    }

//**********************************************************************
//**********************************************************************

    /**
     * generate output file
     *
     * @param type Environment.DIRECTORY_MOVIES / Environment.DIRECTORY_DCIM etc.
     * @param ext  .mp4(.m4a for audio) or .png
     * @return return null when this app has no writing permission to external storage.
     */
    public static final File getCaptureFile(final String type, final String ext) {
        final File dir = new File(Environment.getExternalStoragePublicDirectory(type), DIR_NAME);
        Log.d(TAG, "path=" + dir.toString());
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, getDateTimeString() + ext);
        }
        return null;
    }

    /**
     * get current date and time as String
     *
     * @return
     */
    private static final String getDateTimeString() {
        final GregorianCalendar now = new GregorianCalendar();
        return mDateTimeFormat.format(now.getTime());
    }

    public String getFilePath() {
        return mOutputPath;
    }

}
