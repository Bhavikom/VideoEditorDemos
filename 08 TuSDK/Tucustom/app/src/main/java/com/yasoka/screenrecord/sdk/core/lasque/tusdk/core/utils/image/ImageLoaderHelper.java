// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image;

/*import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;*/
//import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
//import com.nostra13.universalimageloader.utils.StorageUtils;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
//import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import java.io.File;

//import com.bumptech.glide.load.engine.cache.MemoryCache;
//import com.nostra13.universalimageloader.cache.disc.DiskCache;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;

//import com.nostra13.universalimageloader.core.assist.ImageSize;
//import org.lasque.tusdk.core.struct.TuSdkSize;
/*import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.core.ImageLoader;*/
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.HardwareHelper;

public class ImageLoaderHelper
{
    public static void remove(final String s) {
        if (s == null) {
            return;
        }
        DiskCacheUtils.removeFromCache(s, ImageLoader.getInstance().getDiskCache());
        MemoryCacheUtils.removeFromCache(s, ImageLoader.getInstance().getMemoryCache());
    }
    
    public static void remove(final String s, final TuSdkSize tuSdkSize) {
        if (s == null) {
            return;
        }
        DiskCacheUtils.removeFromCache(s, ImageLoader.getInstance().getDiskCache());
        if (tuSdkSize == null) {
            return;
        }
        ImageLoader.getInstance().getMemoryCache().remove(MemoryCacheUtils.generateKey(s, new ImageSize(tuSdkSize.width, tuSdkSize.height)));
        ImageLoader.getInstance().getMemoryCache().remove(MemoryCacheUtils.generateKey(s, new ImageSize(tuSdkSize.height, tuSdkSize.width)));
    }
    
    public static void save(final String s, final Bitmap bitmap) {
        save(s, bitmap, 0);
    }
    
    public static void save(final String s, final Bitmap bitmap, final int n) {
        save(s, bitmap, null, n);
    }
    
    public static void save(final String s, final Bitmap bitmap, final TuSdkSize tuSdkSize, final int n) {
        saveToDiskCache(s, bitmap, n);
        saveToMemoryCache(s, bitmap, tuSdkSize);
    }
    
    public static void saveToDiskCache(final String s, final Bitmap bitmap, final int n) {
        if (bitmap == null || s == null) {
            return;
        }
        final com.nostra13.universalimageloader.cache.disc.DiskCache diskCache = ImageLoader.getInstance().getDiskCache();
        if (diskCache == null) {
            return;
        }
        final File value = diskCache.get(s);
        if (n == 0) {
            BitmapHelper.saveBitmapAsPNG(value, bitmap, 0);
        }
        else {
            BitmapHelper.saveBitmap(value, bitmap, n);
        }
    }
    
    public static void saveToMemoryCache(final String s, final Bitmap bitmap, final TuSdkSize tuSdkSize) {
        if (bitmap == null || s == null) {
            return;
        }
        final com.nostra13.universalimageloader.cache.memory.MemoryCache memoryCache = ImageLoader.getInstance().getMemoryCache();
        if (memoryCache == null) {
            return;
        }
        ImageSize imageSize;
        if (tuSdkSize != null) {
            imageSize = new ImageSize(tuSdkSize.width, tuSdkSize.height);
        }
        else {
            imageSize = new ImageSize(bitmap.getWidth(), bitmap.getHeight());
        }
        memoryCache.put(MemoryCacheUtils.generateKey(s, imageSize), bitmap);
    }
    
    public static boolean exists(final String s) {
        final File loadDiskCache = loadDiskCache(s);
        return loadDiskCache != null && loadDiskCache.exists() && loadDiskCache.isFile();
    }
    
    public static Bitmap loadDiscBitmap(final String s) {
        final File loadDiskCache = loadDiskCache(s);
        if (loadDiskCache == null || !loadDiskCache.isFile() || !loadDiskCache.exists()) {
            return null;
        }
        return BitmapHelper.getBitmap(loadDiskCache);
    }
    
    public static File loadDiskCache(final String s) {
        if (s == null) {
            return null;
        }
        return DiskCacheUtils.findInCache(s, ImageLoader.getInstance().getDiskCache());
    }
    
    public static Bitmap loadMemoryBitmap(final String s, final TuSdkSize tuSdkSize) {
        if (s == null) {
            return null;
        }
        final com.nostra13.universalimageloader.cache.memory.MemoryCache memoryCache = ImageLoader.getInstance().getMemoryCache();
        if (memoryCache == null) {
            return null;
        }
        if (tuSdkSize != null) {
            final Bitmap value = memoryCache.get(MemoryCacheUtils.generateKey(s, new ImageSize(tuSdkSize.width, tuSdkSize.height)));
            if (value != null) {
                return value;
            }
        }
        final List<Bitmap> cachedBitmapsForImageUri = findCachedBitmapsForImageUri(s, memoryCache);
        if (cachedBitmapsForImageUri == null || cachedBitmapsForImageUri.isEmpty()) {
            return null;
        }
        return cachedBitmapsForImageUri.get(0);
    }
    
    public static List<Bitmap> findCachedBitmapsForImageUri(final String anObject, final com.nostra13.universalimageloader.cache.memory.MemoryCache memoryCache) {
        final ArrayList<Bitmap> list = new ArrayList<Bitmap>();
        for (final String s : memoryCache.keys()) {
            if (s.substring(0, s.lastIndexOf("_")).equals(anObject)) {
                list.add(memoryCache.get(s));
            }
        }
        return list;
    }
    
    public static void clearAllCache() {
        clearDiskCache();
        clearMemoryCache();
    }
    
    public static void clearDiskCache() {
        ImageLoader.getInstance().clearDiskCache();
    }
    
    public static void clearMemoryCache() {
        ImageLoader.getInstance().clearMemoryCache();
    }
    
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void initImageCache(final Context context, final TuSdkSize tuSdkSize) {
        final File cacheDirectory = StorageUtils.getCacheDirectory(context);
        File file = new File(cacheDirectory, "imageCache");
        if (!file.exists() && !file.mkdirs()) {
            file = cacheDirectory;
        }
        final int min = Math.min((int)(HardwareHelper.appMemoryBit() / 8L), 1703936);
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).
                memoryCacheExtraOptions(tuSdkSize.width, tuSdkSize.height).diskCacheExtraOptions(tuSdkSize.width, tuSdkSize.height,
                (BitmapProcessor)null).threadPoolSize(2).threadPriority(4).tasksProcessingOrder(QueueProcessingType.FIFO).denyCacheImageMultipleSizesInMemory()
                .memoryCache((com.nostra13.universalimageloader.cache.memory.MemoryCache) new LargestLimitedMemoryCache(min)).memoryCacheSize(min).memoryCacheSizePercentage(13).diskCache((com.nostra13.universalimageloader.cache.disc.DiskCache) new UnlimitedDiskCache(file)).diskCacheSize(209715200).diskCacheFileNameGenerator((FileNameGenerator)
                        new HashCodeFileNameGenerator()).imageDownloader((ImageDownloader)new BaseImageDownloader(context))
                .imageDecoder((ImageDecoder) new BaseImageDecoder(false)).defaultDisplayImageOptions(DisplayImageOptions.createSimple()).build());
    }
}
