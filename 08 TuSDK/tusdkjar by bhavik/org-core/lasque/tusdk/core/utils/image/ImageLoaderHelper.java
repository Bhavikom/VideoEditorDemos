package org.lasque.tusdk.core.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.utils.StorageUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.HardwareHelper;

public class ImageLoaderHelper
{
  public static void remove(String paramString)
  {
    if (paramString == null) {
      return;
    }
    DiskCacheUtils.removeFromCache(paramString, ImageLoader.getInstance().getDiskCache());
    MemoryCacheUtils.removeFromCache(paramString, ImageLoader.getInstance().getMemoryCache());
  }
  
  public static void remove(String paramString, TuSdkSize paramTuSdkSize)
  {
    if (paramString == null) {
      return;
    }
    DiskCacheUtils.removeFromCache(paramString, ImageLoader.getInstance().getDiskCache());
    if (paramTuSdkSize == null) {
      return;
    }
    ImageSize localImageSize = new ImageSize(paramTuSdkSize.width, paramTuSdkSize.height);
    String str = MemoryCacheUtils.generateKey(paramString, localImageSize);
    ImageLoader.getInstance().getMemoryCache().remove(str);
    localImageSize = new ImageSize(paramTuSdkSize.height, paramTuSdkSize.width);
    str = MemoryCacheUtils.generateKey(paramString, localImageSize);
    ImageLoader.getInstance().getMemoryCache().remove(str);
  }
  
  public static void save(String paramString, Bitmap paramBitmap)
  {
    save(paramString, paramBitmap, 0);
  }
  
  public static void save(String paramString, Bitmap paramBitmap, int paramInt)
  {
    save(paramString, paramBitmap, null, paramInt);
  }
  
  public static void save(String paramString, Bitmap paramBitmap, TuSdkSize paramTuSdkSize, int paramInt)
  {
    saveToDiskCache(paramString, paramBitmap, paramInt);
    saveToMemoryCache(paramString, paramBitmap, paramTuSdkSize);
  }
  
  public static void saveToDiskCache(String paramString, Bitmap paramBitmap, int paramInt)
  {
    if ((paramBitmap == null) || (paramString == null)) {
      return;
    }
    DiskCache localDiskCache = ImageLoader.getInstance().getDiskCache();
    if (localDiskCache == null) {
      return;
    }
    File localFile = localDiskCache.get(paramString);
    if (paramInt == 0) {
      BitmapHelper.saveBitmapAsPNG(localFile, paramBitmap, 0);
    } else {
      BitmapHelper.saveBitmap(localFile, paramBitmap, paramInt);
    }
  }
  
  public static void saveToMemoryCache(String paramString, Bitmap paramBitmap, TuSdkSize paramTuSdkSize)
  {
    if ((paramBitmap == null) || (paramString == null)) {
      return;
    }
    MemoryCache localMemoryCache = ImageLoader.getInstance().getMemoryCache();
    if (localMemoryCache == null) {
      return;
    }
    ImageSize localImageSize = null;
    if (paramTuSdkSize != null) {
      localImageSize = new ImageSize(paramTuSdkSize.width, paramTuSdkSize.height);
    } else {
      localImageSize = new ImageSize(paramBitmap.getWidth(), paramBitmap.getHeight());
    }
    String str = MemoryCacheUtils.generateKey(paramString, localImageSize);
    localMemoryCache.put(str, paramBitmap);
  }
  
  public static boolean exists(String paramString)
  {
    File localFile = loadDiskCache(paramString);
    return (localFile != null) && (localFile.exists()) && (localFile.isFile());
  }
  
  public static Bitmap loadDiscBitmap(String paramString)
  {
    File localFile = loadDiskCache(paramString);
    if ((localFile == null) || (!localFile.isFile()) || (!localFile.exists())) {
      return null;
    }
    Bitmap localBitmap = BitmapHelper.getBitmap(localFile);
    return localBitmap;
  }
  
  public static File loadDiskCache(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    DiskCache localDiskCache = ImageLoader.getInstance().getDiskCache();
    File localFile = DiskCacheUtils.findInCache(paramString, localDiskCache);
    return localFile;
  }
  
  public static Bitmap loadMemoryBitmap(String paramString, TuSdkSize paramTuSdkSize)
  {
    if (paramString == null) {
      return null;
    }
    MemoryCache localMemoryCache = ImageLoader.getInstance().getMemoryCache();
    if (localMemoryCache == null) {
      return null;
    }
    if (paramTuSdkSize != null)
    {
      localObject = new ImageSize(paramTuSdkSize.width, paramTuSdkSize.height);
      String str = MemoryCacheUtils.generateKey(paramString, (ImageSize)localObject);
      Bitmap localBitmap = localMemoryCache.get(str);
      if (localBitmap != null) {
        return localBitmap;
      }
    }
    Object localObject = findCachedBitmapsForImageUri(paramString, localMemoryCache);
    if ((localObject == null) || (((List)localObject).isEmpty())) {
      return null;
    }
    return (Bitmap)((List)localObject).get(0);
  }
  
  public static List<Bitmap> findCachedBitmapsForImageUri(String paramString, MemoryCache paramMemoryCache)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramMemoryCache.keys().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      int i = str.lastIndexOf("_");
      if (str.substring(0, i).equals(paramString)) {
        localArrayList.add(paramMemoryCache.get(str));
      }
    }
    return localArrayList;
  }
  
  public static void clearAllCache()
  {
    clearDiskCache();
    clearMemoryCache();
  }
  
  public static void clearDiskCache()
  {
    ImageLoader.getInstance().clearDiskCache();
  }
  
  public static void clearMemoryCache()
  {
    ImageLoader.getInstance().clearMemoryCache();
  }
  
  public static void initImageCache(Context paramContext, TuSdkSize paramTuSdkSize)
  {
    File localFile1 = StorageUtils.getCacheDirectory(paramContext);
    File localFile2 = new File(localFile1, "imageCache");
    if ((!localFile2.exists()) && (!localFile2.mkdirs())) {
      localFile2 = localFile1;
    }
    int i = Math.min((int)(HardwareHelper.appMemoryBit() / 8L), 1703936);
    ImageLoaderConfiguration localImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(paramContext).memoryCacheExtraOptions(paramTuSdkSize.width, paramTuSdkSize.height).diskCacheExtraOptions(paramTuSdkSize.width, paramTuSdkSize.height, null).threadPoolSize(2).threadPriority(4).tasksProcessingOrder(QueueProcessingType.FIFO).denyCacheImageMultipleSizesInMemory().memoryCache(new LargestLimitedMemoryCache(i)).memoryCacheSize(i).memoryCacheSizePercentage(13).diskCache(new UnlimitedDiskCache(localFile2)).diskCacheSize(209715200).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).imageDownloader(new BaseImageDownloader(paramContext)).imageDecoder(new BaseImageDecoder(false)).defaultDisplayImageOptions(DisplayImageOptions.createSimple()).build();
    ImageLoader.getInstance().init(localImageLoaderConfiguration);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\image\ImageLoaderHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */