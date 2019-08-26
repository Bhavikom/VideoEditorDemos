package zippler.cn.xs.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.media.MediaMetadataRetriever.OPTION_CLOSEST;

/**
 * Created by Zipple on 2018/5/8.
 * help to get pictures
 */
public class ImageFileUtil {
    /**
     * @return
     */
    public static List<String> getImagesInPath(String filePath) {
        List<String> imagePathList = new ArrayList<>();
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        for (File file : files) {
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getAbsolutePath());
            }
        }
        return imagePathList;
    }

    /**
     * check the extension name
     * @param fName  file name
     * @return is or not
     */
    private static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        isImageFile = FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp");
        return isImageFile;
    }

    /**
     *  set first frame from video
     * @param img which img to show
     * @param videoPath video path
     */
    public static void setFirstFrame(ImageView img,String videoPath){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        Bitmap bitmap = retriever.getFrameAtTime(0,OPTION_CLOSEST);
        img.setImageBitmap(bitmap);//Is there a more efficient way？
    }

    public static void setFirstFrame(ImageView img, Context context, Uri videoPath){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context,videoPath);
        Bitmap bitmap = retriever.getFrameAtTime(0,OPTION_CLOSEST);
        img.setImageBitmap(bitmap);//Is there a more efficient way？
    }

    public static long getDuration(String videoPath){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        return Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }
    public static long getDuration(Context context, Uri videoPath){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context,videoPath);
        return Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

}
