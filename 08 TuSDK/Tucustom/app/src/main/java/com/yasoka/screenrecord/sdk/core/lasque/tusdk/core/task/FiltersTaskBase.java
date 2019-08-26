// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task;

import android.widget.ImageView;
import java.util.concurrent.Executors;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.SdkValid;
import java.util.Iterator;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import android.os.Handler;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.lang.ref.SoftReference;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public abstract class FiltersTaskBase implements FilterTaskInterface
{
    public static final String TAG = "FiltersTaskBase";
    private File a;
    private ArrayList<FiltersTaskImageWare> b;
    private List<String> c;
    private Hashtable<String, SoftReference<Bitmap>> d;
    private Bitmap e;
    private Handler f;
    private ExecutorService g;
    private boolean h;
    
    public FiltersTaskBase() {
        this.b = new ArrayList<FiltersTaskImageWare>();
        this.c = new ArrayList<String>();
        this.d = new Hashtable<String, SoftReference<Bitmap>>();
        this.f = new Handler(Looper.getMainLooper());
    }
    
    public File getSampleRootPath() {
        return this.a;
    }
    
    public void setSampleRootPath(final File a) {
        this.a = a;
    }
    
    private ArrayList<FiltersTaskImageWare> a() {
        if (this.b == null) {
            this.b = new ArrayList<FiltersTaskImageWare>();
        }
        return new ArrayList<FiltersTaskImageWare>(this.b);
    }
    
    public List<String> getFilerNames() {
        return this.c;
    }
    
    @Override
    public void setFilerNames(final List<String> list) {
        final List<String> verifyCodes = FilterLocalPackage.shared().verifyCodes(list);
        if (verifyCodes == null) {
            return;
        }
        final Iterator<String> iterator = verifyCodes.iterator();
        while (iterator.hasNext()) {
            this.appendFilterCode(iterator.next());
        }
    }
    
    @Override
    public void appendFilterCode(final String s) {
        if (s != null && !this.c.contains(s)) {
            this.c.add(s);
        }
    }
    
    @Override
    public boolean isRenderFilterThumb() {
        return this.b();
    }
    
    private boolean b() {
        return SdkValid.shared.renderFilterThumb() && this.h;
    }
    
    @Override
    public void setRenderFilterThumb(final boolean h) {
        this.h = h;
    }
    
    public Bitmap getInputImage() {
        return this.e;
    }
    
    @Override
    public void setInputImage(final Bitmap e) {
        this.e = e;
    }
    
    public Bitmap getCache(final String key) {
        if (key == null) {
            return null;
        }
        final SoftReference<Bitmap> softReference = this.d.get(key);
        if (softReference != null) {
            return softReference.get();
        }
        return null;
    }
    
    public void addCache(final String key, final Bitmap referent) {
        if (key == null || referent == null) {
            return;
        }
        this.d.put(key, new SoftReference<Bitmap>(referent));
    }
    
    @Override
    public void start() {
        this.c();
    }

    private void c() {
        if (this.b()) {
            if (this.getInputImage() == null) {
                TLog.w("%s You need set inputImage.", new Object[]{"FiltersTaskBase"});
            } else if (this.c == null) {
                TLog.w("%s You need set filerNames.", new Object[]{"FiltersTaskBase"});
            } else if (this.getSampleRootPath() == null) {
                TLog.w("%s Can not found SampleRootPath: %s", new Object[]{"FiltersTaskBase", this.a});
            } else {
                ExecutorService var1 = this.d();
                ArrayList var2 = new ArrayList(this.c);
                Iterator var3 = var2.iterator();

                while(var3.hasNext()) {
                    final String var4 = (String)var3.next();
                    var1.execute(new Runnable() {
                        public void run() {
                            FiltersTaskBase.this.asyncBuildWithFilterName(var4);
                        }
                    });
                }

            }
        }
    }
    
    protected void asyncBuildWithFilterName(final String filterName) {
        final File b = this.b(filterName);
        if (b.exists()) {
            return;
        }
        final FiltersTaskImageResult filtersTaskImageResult = new FiltersTaskImageResult();
        filtersTaskImageResult.setFilterName(filterName);
        final Bitmap a = this.a(this.e, filterName);
        if (a != null) {
            BitmapHelper.saveBitmap(b, a, 90);
            filtersTaskImageResult.setImage(a);
        }
        this.f.post((Runnable)new Runnable() {
            @Override
            public void run() {
                FiltersTaskBase.this.a(filtersTaskImageResult);
            }
        });
    }
    
    private Bitmap a(final Bitmap bitmap, final String s) {
        final FilterWrap filterWrap = FilterLocalPackage.shared().getFilterWrap(s);
        if (filterWrap == null) {
            return bitmap;
        }
        filterWrap.setFilterParameter(null);
        return filterWrap.process(bitmap, ImageOrientation.Up, 0.0f);
    }
    
    private void a(final FiltersTaskImageResult imageResult) {
        this.addCache(imageResult.getFilterName(), imageResult.getImage());
        for (final FiltersTaskImageWare o : this.a()) {
            if (o.setImageResult(imageResult)) {
                this.b.remove(o);
            }
        }
    }
    
    private ExecutorService d() {
        if (this.g == null) {
            this.g = Executors.newFixedThreadPool(1);
        }
        return this.g;
    }
    
    @Override
    public void resetQueues() {
        if (this.g != null) {
            this.g.shutdownNow();
        }
        this.b.clear();
        this.d.clear();
    }
    
    private Bitmap a(final String s) {
        final Bitmap cache = this.getCache(s);
        if (cache != null) {
            return cache;
        }
        final Bitmap bitmap = BitmapHelper.getBitmap(this.b(s));
        this.addCache(s, bitmap);
        return bitmap;
    }
    
    private File b(final String s) {
        return new File(this.getSampleRootPath(), String.format("%s.%s", s, "lfs"));
    }
    
    @Override
    protected void finalize() {
        this.resetQueues();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public void loadImage(final ImageView imageView, final String s) {
        if (!this.b()) {
            FilterLocalPackage.shared().loadFilterThumb(imageView, FilterLocalPackage.shared().option(s));
            return;
        }
        if (imageView == null || s == null) {
            return;
        }
        this.cancelLoadImage(imageView);
        final Bitmap a = this.a(s);
        if (a != null) {
            imageView.setImageBitmap(a);
            return;
        }
        this.b.add(new FiltersTaskImageWare(imageView, s));
    }
    
    @Override
    public void cancelLoadImage(final ImageView imageView) {
        if (!this.b()) {
            FilterLocalPackage.shared().cancelLoadImage(imageView);
            return;
        }
        if (imageView == null) {
            return;
        }
        for (final FiltersTaskImageWare o : this.a()) {
            if (o.isEqualView(imageView)) {
                this.b.remove(o);
                break;
            }
        }
    }
}
