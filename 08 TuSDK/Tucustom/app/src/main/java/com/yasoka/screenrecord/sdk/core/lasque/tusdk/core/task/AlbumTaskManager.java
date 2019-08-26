// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task;

//import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.TuSdkContext;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import android.widget.ImageView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;

public class AlbumTaskManager extends ImageViewTask<AlbumTaskManager.AlbumTaskImageWare>
{
    public static final AlbumTaskManager shared;
    public static int TypeMiniThumbSize;
    public static int TypeThumbSize;
    
    private AlbumTaskManager() {
    }
    
    @Override
    protected void finalize() {
        this.resetQueues();
        super.finalize();
    }
    
    public void loadThumbMiniImage(final ImageView imageView, final ImageSqlInfo imageSqlInfo) {
        this.loadImage(imageView, imageSqlInfo, AlbumTaskType.TypeMiniThumb);
    }
    
    public void loadThumbImage(final ImageView imageView, final ImageSqlInfo imageSqlInfo) {
        this.loadImage(imageView, imageSqlInfo, AlbumTaskType.TypeThumb);
    }
    
    public void loadFullScreenImage(final ImageView imageView, final ImageSqlInfo imageSqlInfo) {
        this.loadImage(imageView, imageSqlInfo, AlbumTaskType.TypeFullScreen);
    }
    
    public void loadResolution(final ImageView imageView, final ImageSqlInfo imageSqlInfo) {
        this.loadImage(imageView, imageSqlInfo, AlbumTaskType.TypeResolution);
    }
    
    public void loadImage(final ImageView imageView, final ImageSqlInfo imageSqlInfo, final AlbumTaskType albumTaskType) {
        if (imageView == null || albumTaskType == null || imageSqlInfo == null || imageSqlInfo.id == 0L || imageSqlInfo.path == null) {
            return;
        }
        this.loadImage(new AlbumTaskImageWare(imageView, imageSqlInfo, albumTaskType));
    }
    
    @Override
    protected String getCacheKey(final AlbumTaskImageWare albumTaskImageWare) {
        return StringHelper.md5(String.format("%s_%s", albumTaskImageWare.imageSqlInfo.identify(), albumTaskImageWare.taskType));
    }
    
    @Override
    protected Bitmap asyncTaskLoadImage(final AlbumTaskImageWare albumTaskImageWare) {
        Bitmap bitmap = null;
        switch (albumTaskImageWare.taskType.ordinal()) {
            case 1: {
                bitmap = BitmapHelper.getBitmap(albumTaskImageWare.imageSqlInfo, TuSdkContext.getScreenSize());
                break;
            }
            case 2: {
                bitmap = BitmapHelper.getBitmap(albumTaskImageWare.imageSqlInfo);
                break;
            }
            default: {
                bitmap = this.a(albumTaskImageWare);
                break;
            }
        }
        return bitmap;
    }
    
    private Bitmap a(final AlbumTaskImageWare albumTaskImageWare) {
        if (albumTaskImageWare == null) {
            return null;
        }
        if (!TuSdkContext.hasAvailableExternal()) {
            return ImageSqlHelper.getThumbnail(TuSdkContext.ins().getContext(), albumTaskImageWare.imageSqlInfo, 1);
        }
        if (albumTaskImageWare.taskType == AlbumTaskType.TypeThumb) {
            return BitmapHelper.getBitmap(albumTaskImageWare.imageSqlInfo, AlbumTaskManager.TypeThumbSize, false);
        }
        return BitmapHelper.getBitmap(albumTaskImageWare.imageSqlInfo, AlbumTaskManager.TypeMiniThumbSize, false);
    }
    
    static {
        shared = new AlbumTaskManager();
        AlbumTaskManager.TypeMiniThumbSize = 100;
        AlbumTaskManager.TypeThumbSize = 200;
    }
    
    public static class AlbumTaskImageWare extends ImageViewTaskWare
    {
        public ImageSqlInfo imageSqlInfo;
        public AlbumTaskType taskType;
        
        public AlbumTaskImageWare(final ImageView imageView, final ImageSqlInfo imageSqlInfo, final AlbumTaskType taskType) {
            this.setImageView(imageView);
            this.imageSqlInfo = imageSqlInfo;
            this.taskType = taskType;
            if (taskType == null) {
                this.taskType = AlbumTaskType.TypeMiniThumb;
            }
        }
        
        @Override
        public void imageLoaded(final Bitmap bitmap, final LoadType loadType) {
            if (bitmap != null || this.isCancel()) {
                super.imageLoaded(bitmap, loadType);
                return;
            }
            final ImageView imageView = this.getImageView();
            if (imageView == null) {
                return;
            }
            imageView.setImageResource(TuSdkContext.getDrawableResId("lsq_style_default_image_none"));
        }
    }
    
    public enum AlbumTaskType
    {
        TypeMiniThumb, 
        TypeThumb, 
        TypeFullScreen, 
        TypeResolution;
    }
}
