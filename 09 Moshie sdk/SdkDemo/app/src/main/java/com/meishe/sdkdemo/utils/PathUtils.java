package com.meishe.sdkdemo.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.meishe.sdkdemo.utils.asset.NvAsset;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by admin on 2018-6-5.
 */

public class PathUtils {

    private static final String TAG = PathUtils.class.getName();

    private static String SDK_FILE_ROOT_DIRECTORY = "NvStreamingSdk" + File.separator;
    private static String RECORDING_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "Record";
    private static String AUDIO_RECORD_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "AudioRecord";
    private static String DOUYIN_RECORDING_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "DouYinRecord";
    private static String DOUYIN_CONVERT_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "DouYinConvert";
    private static String COVER_IMAGE_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "Cover";
    private static String PARTICLE_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "Particle";
    private static String CREASH_LOG_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "Log";
    private static String WATERMARK_CAF_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "WaterMark";
    private static String PICINPIC_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "PicInPic";
    private static String VIDEOCOMPILE_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "Compile";
    private static String CAPTURESCENE_RECORDING_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "CaptureScene";
    private static String BOOMRANG_RECORDING_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "BoomRang";
    private static String SUPERZOOM_RECORDING_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "SuperZoom";

    private static String ASSET_DOWNLOAD_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "Asset";
    private static String ASSET_DOWNLOAD_DIRECTORY_FILTER = ASSET_DOWNLOAD_DIRECTORY + File.separator + "Filter";
    private static String ASSET_DOWNLOAD_DIRECTORY_THEME = ASSET_DOWNLOAD_DIRECTORY + File.separator + "Theme";
    private static String ASSET_DOWNLOAD_DIRECTORY_CAPTION = ASSET_DOWNLOAD_DIRECTORY + File.separator + "Caption";
    private static String ASSET_DOWNLOAD_DIRECTORY_ANIMATEDSTICKER = ASSET_DOWNLOAD_DIRECTORY + File.separator + "AnimatedSticker";
    private static String ASSET_DOWNLOAD_DIRECTORY_TRANSITION = ASSET_DOWNLOAD_DIRECTORY + File.separator + "Transition";
    private static String ASSET_DOWNLOAD_DIRECTORY_FONT = ASSET_DOWNLOAD_DIRECTORY + File.separator + "Font";
    private static String ASSET_DOWNLOAD_DIRECTORY_CAPTURE_SCENE = ASSET_DOWNLOAD_DIRECTORY + File.separator + "CaptureScene";
    private static String ASSET_DOWNLOAD_DIRECTORY_PARTICLE = ASSET_DOWNLOAD_DIRECTORY + File.separator + "Particle";
    private static String ASSET_DOWNLOAD_DIRECTORY_FACE_STICKER = ASSET_DOWNLOAD_DIRECTORY + File.separator + "FaceSticker";
    private static String ASSET_DOWNLOAD_DIRECTORY_CUSTOM_ANIMATED_STICKER = ASSET_DOWNLOAD_DIRECTORY + File.separator + "CustomAnimatedSticker";
    private static String ASSET_DOWNLOAD_DIRECTORY_FACE1_STICKER = ASSET_DOWNLOAD_DIRECTORY + File.separator + "Face1Sticker";
    private static String ASSET_DOWNLOAD_DIRECTORY_SUPER_ZOOM = ASSET_DOWNLOAD_DIRECTORY + File.separator + "Meicam";
    private static String ASSET_DOWNLOAD_DIRECTORY_ARSCENE = ASSET_DOWNLOAD_DIRECTORY + File.separator + "ArScene";
    private static String ASSET_DOWNLOAD_DIRECTORY_GIFCONVERT = ASSET_DOWNLOAD_DIRECTORY + File.separator + "GifConvert";
    private static String ASSET_DOWNLOAD_DIRECTORY_COMPOUND_CAPTION = ASSET_DOWNLOAD_DIRECTORY + File.separator + "CompoundCaption";
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null){
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteDirectoryFile(f);
                }
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteDirectoryFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteDirectoryFile(f);
                }
            }
            //file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    public static String getDouYinRecordDir(){
        return getFolderDirPath(DOUYIN_RECORDING_DIRECTORY);
    }

    public static String getGifConvertDir(){
        return getFolderDirPath(ASSET_DOWNLOAD_DIRECTORY_GIFCONVERT);
    }
    public static String getDouYinRecordVideoPath() {
        String dstDirPath = getDouYinRecordDir();
        if(dstDirPath == null)
            return null;
        String fileName = getCharacterAndNumber() + ".mp4";
        return getFileDirPath(dstDirPath,fileName);
    }

    public static String getDouYinConvertDir() {
        return getFolderDirPath(DOUYIN_CONVERT_DIRECTORY);
    }

    public static String getLogDir() {
        return getFolderDirPath(CREASH_LOG_DIRECTORY);
    }
    public static String getWatermarkCafDirectoryDir() {
        return getFolderDirPath(WATERMARK_CAF_DIRECTORY);
    }

    //获取画中画文件目录
    public static String getPicInPicDirPath() {
        return getFolderDirPath(PICINPIC_DIRECTORY);
    }

    //获取视频生成目录
    public static String getVideoCompileDirPath() {
        return getFolderDirPath(VIDEOCOMPILE_DIRECTORY);
    }

    public static String getRecordVideoPath() {
        String dstDirPath = getFolderDirPath(RECORDING_DIRECTORY);
        if(dstDirPath == null)
            return null;
        String fileName = getCharacterAndNumber() + ".mp4";
        return getFileDirPath(dstDirPath,fileName);
    }

    public static String getCaptureSceneRecordVideoPath() {
        String dstDirPath = getFolderDirPath(CAPTURESCENE_RECORDING_DIRECTORY);
        if(dstDirPath == null)
            return null;
        String fileName = getCharacterAndNumber() + ".mp4";
        return getFileDirPath(dstDirPath,fileName);
    }

    /**
     * 获取boomrang的文件名称
     * @param endName _record 是录制名 _compile是生成文件名
     * @return
     */
    public static String getBoomrangRecordingDirectory(String endName) {
        String dstDirPath = getFolderDirPath(BOOMRANG_RECORDING_DIRECTORY);
        if(dstDirPath == null)
            return null;
        String fileName = getCharacterAndNumber() + ".mp4";
        return getFileDirPath(dstDirPath,fileName);
    }

    /**
     * 获取superZoom的文件名称
     */
    public static String getSuperZoomRecordingDirectory(String endName) {
        String dstDirPath = getFolderDirPath(SUPERZOOM_RECORDING_DIRECTORY);
        if(dstDirPath == null)
            return null;
        String fileName = getCharacterAndNumber() + ".mp4";
        return getFileDirPath(dstDirPath,fileName);
    }

    public static String getRecordPicturePath() {
        String dstDirPath = getFolderDirPath(RECORDING_DIRECTORY);
        if(dstDirPath == null)
            return null;
        String fileName = getCharacterAndNumber() + ".jpg";
        return getFileDirPath(dstDirPath,fileName);
    }

    public static String getCoverImagePath() {
        String dstDirPath = getFolderDirPath(COVER_IMAGE_DIRECTORY);
        if(dstDirPath == null)
            return null;
        String fileName = getCharacterAndNumber() + ".jpg";
        return getFileDirPath(dstDirPath,fileName);
    }

    public static String getParticleRecordPath() {
        String dstDirPath = getFolderDirPath(PARTICLE_DIRECTORY);
        if(dstDirPath == null)
            return null;
        String fileName = getCharacterAndNumber() + ".mp4";
        return getFileDirPath(dstDirPath,fileName);
    }

    public static String getAudioRecordFilePath() {
        return getFolderDirPath(AUDIO_RECORD_DIRECTORY);
    }

    public static String getCharacterAndNumber() {
        return String.valueOf(System.nanoTime());
    }

    public static String getAssetDownloadPath(int assetType) {
        String assetDownloadDir = getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY);
        if(assetDownloadDir == null)
            return null;
        switch (assetType) {
            case NvAsset.ASSET_THEME: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_THEME);
            }
            case NvAsset.ASSET_FILTER: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_FILTER);
            }
            case NvAsset.ASSET_CAPTION_STYLE: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_CAPTION);
            }
            case NvAsset.ASSET_ANIMATED_STICKER: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_ANIMATEDSTICKER);
            }
            case NvAsset.ASSET_VIDEO_TRANSITION: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_TRANSITION);
            }
            case NvAsset.ASSET_FONT: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_FONT);
            }
            case NvAsset.ASSET_CAPTURE_SCENE: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_CAPTURE_SCENE);
            }
            case NvAsset.ASSET_PARTICLE: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_PARTICLE);
            }
            case NvAsset.ASSET_FACE_STICKER: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_FACE_STICKER);
            }
            case NvAsset.ASSET_CUSTOM_ANIMATED_STICKER: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_CUSTOM_ANIMATED_STICKER);
            }
            case NvAsset.ASSET_FACE1_STICKER: {
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_FACE1_STICKER);
            }
            case NvAsset.ASSET_SUPER_ZOOM:{
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_SUPER_ZOOM);
            }
            case NvAsset.ASSET_ARSCENE_FACE:{
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_ARSCENE);
            }
            case NvAsset.ASSET_COMPOUND_CAPTION:{
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_COMPOUND_CAPTION);
            }
        }
        return assetDownloadDir;
    }

    private static String getAssetDownloadDirPath(String assetDirPathToCreate){
        return getFolderDirPath(assetDirPathToCreate);
    }

    //获取文件生成目录
    public static String getFileDirPath(String dstDirPathToCreate,String fileName) {
        File file = new File(dstDirPathToCreate, fileName);
        if (file.exists()) {
            file.delete();
        }

        return file.getAbsolutePath();
    }

    //获取文件夹生成目录
    public static String getFolderDirPath(String dstDirPathToCreate){
        File dstFileDir = new File(Environment.getExternalStorageDirectory(), dstDirPathToCreate);
        if (!dstFileDir.exists() && !dstFileDir.mkdirs()) {
            Logger.e(TAG, "Failed to create file dir path--->" + dstDirPathToCreate);
            return null;
        }
        return dstFileDir.getAbsolutePath();
    }

    public static boolean unZipAsset(Context context, String assetName, String outputDirectory, boolean isReWrite) {
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            // 打开压缩文件
            InputStream inputStream = context.getAssets().open(assetName);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            // 读取一个进入点
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            // 使用1Mbuffer
            byte[] buffer = new byte[1024 * 1024];
            // 解压时字节计数
            int count = 0;
            // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
            while (zipEntry != null) {
                // 如果是一个目录
                if (zipEntry.isDirectory()) {
                    file = new File(outputDirectory + File.separator + zipEntry.getName());
                    // 文件需要覆盖或者是文件不存在
                    if (isReWrite || !file.exists()) {
                        file.mkdir();
                    }
                } else {
                    // 如果是文件
                    file = new File(outputDirectory + File.separator + zipEntry.getName());
                    // 文件需要覆盖或者文件不存在，则解压文件
                    if (isReWrite || !file.exists()) {
                        file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        while ((count = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, count);
                        }
                        fileOutputStream.close();
                    }
                }
                // 定位到下一个文件入口
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean unZipFile(String zipFile, String folderPath) {
        ZipFile zfile = null;
        try {
            // 转码为GBK格式，支持中文
            zfile = new ZipFile(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            // 列举的压缩文件里面的各个文件，判断是否为目录
            if (ze.isDirectory()) {
                String dirstr = folderPath + ze.getName();
                dirstr.trim();
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os = null;
            FileOutputStream fos = null;
            // ze.getName()会返回 script/start.script这样的，是为了返回实体的File
            File realFile = getRealFileName(folderPath, ze.getName());
            try {
                fos = new FileOutputStream(realFile);
            } catch (FileNotFoundException e) {
                return false;
            }
            os = new BufferedOutputStream(fos);
            InputStream is = null;
            try {
                is = new BufferedInputStream(zfile.getInputStream(ze));
            } catch (IOException e) {
                return false;
            }
            int readLen = 0;
            // 进行一些内容复制操作
            try {
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                return false;
            }
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                return false;
            }
        }
        try {
            zfile.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static File getRealFileName(String baseDir, String absFileName) {
        absFileName = absFileName.replace("\\", "/");
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                ret = new File(ret, substr);
            }

            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            ret = new File(ret, substr);
            return ret;
        } else {
            ret = new File(ret, absFileName);
        }
        return ret;
    }

    public static String getFileNameNoExt(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.indexOf('.');
            int lastSeparator = filename.lastIndexOf('/');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(lastSeparator + 1, dot);
            }
        }
        return filename;
    }

    public static String getFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.indexOf('.');
            int lastSeparator = filename.lastIndexOf('/');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(lastSeparator + 1);
            }
        }
        return filename;
    }

    public static int getAssetVersionWithPath(String path) {
        String[] strings = path.split("/");
        if (strings.length > 0) {
            String filename = strings[strings.length - 1];
            String[] parts = filename.split(".");
            if (parts.length == 3) {
                return Integer.parseInt(parts[1]);
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public static long getFileModifiedTime(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        } else {
            return file.lastModified();
        }
    }

    public static String getCaptureSceneLocalFilePath(){
        File assetDownloadDirCaptureScene = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_CAPTURE_SCENE);
        if (!assetDownloadDirCaptureScene.exists()) {
            if (!assetDownloadDirCaptureScene.mkdirs()) {
                Log.e(TAG, "Failed to make asset download capture scene directory");
                return "";
            }
        }
        return assetDownloadDirCaptureScene.getPath();
    }
}
