package zippler.cn.xs.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zipple on 2018/5/12.
 * For write txt in file which can help to concat videos.
 */

public class FileUtil {
    private static final String TAG = "FileUtil";
    private final static String PREFIX_VIDEO="video/";
    static boolean writeContentsToTxt(String path, List<String> content){
        boolean result = false;
        String strContent = "";
        for(int i = 0; i < content.size(); ++i) {
            strContent += "file " + content.get(i) + "\r\n";
        }
        try {
            File file = new File(path);
            if(file.exists()) {
                Log.d(TAG, "writeContentsToTxt: concat file exist");
                if (file.delete()){
                    Log.d(TAG, "writeContentsToTxt: deleted concat file success");
                    if (file.createNewFile()){
                        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                        raf.seek(file.length());
                        raf.write(strContent.getBytes());
                        raf.close();
                        Log.d(TAG, "writeContentsToTxt: create concat file success");
                        result = true;
                    }else{
                        Log.e(TAG, "writeContentsToTxt: create concat file failed");
                    }
                }else{
                    Log.e(TAG, "writeContentsToTxt: deleted concat file failed");
                }
            }else{
                Log.d(TAG, "writeContentsToTxt: concat file is not exist.");
                if (file.createNewFile()){
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    raf.seek(file.length());
                    raf.write(strContent.getBytes());
                    raf.close();
                    Log.d(TAG, "writeContentsToTxt: create concat file success");
                    result = true;
                }else{
                    Log.e(TAG, "writeContentsToTxt: create concat file failed");
                }
            }
        } catch (Exception var7) {
            Log.e("TestFile", "Error on write File:" + var7);
        }
        return result;
    }

    /**
     * delete file
     * @param path file path
     * @return is successful
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() && file.delete();
    }

    /**
     * get all file path
     * @param path root
     * @return return list of file
     */
    public static List<String> traverseFolder(String path) {

        List<String> paths = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return null;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        traverseFolder(file2.getAbsolutePath());
                    } else {
                        paths.add(file2.getAbsolutePath());
                    }
                }
            }
        } else {
            return null;
        }
        return paths;
    }


    private static String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(fileName);
    }

    public static boolean isVideoFile(String fileName){
        String mimeType = getMimeType(fileName);
        if (mimeType!=null){
            if (!TextUtils.isEmpty(fileName)&&mimeType.contains(PREFIX_VIDEO)){
                return true;
            }
        }
        return false;
    }


    public static String getCamera2Path() {
        String picturePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xsheng/";
        File file = new File(picturePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return picturePath;
    }

    public static void createSavePath(String path){
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String move2Folders(String pathName,String ansPath){
        String result = "";
        try {
            File startFile = new File(pathName);
            File tmpFile = new File(ansPath);
            if(!tmpFile.exists()){
                tmpFile.mkdirs();
            }
            result = ansPath + startFile.getName();
            if (startFile.renameTo(new File(result))) {
                System.out.println("File is moved successful!");
            } else {
                System.out.println("File is failed to move!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Timestamp getLastModifiedTime(String filePath){
        File file = new File(filePath);
        long time = file.lastModified();
        return new Timestamp(time);
    }
}
