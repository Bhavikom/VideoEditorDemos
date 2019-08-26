// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles;

import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
//import org.lasque.tusdk.core.face.FaceAligment;
import android.graphics.RectF;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
//import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;

//import org.lasque.tusdk.core.utils.StringHelper;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class SelesParameters
{
    private List<FilterArg> a;
    private Map<String, String> b;
    private boolean c;
    
    public boolean isInited() {
        return this.c;
    }
    
    public List<FilterArg> getArgs() {
        return this.a;
    }
    
    public List<String> getArgKeys() {
        final ArrayList<String> list = new ArrayList<String>(this.a.size());
        final Iterator<FilterArg> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getKey());
        }
        return list;
    }
    
    public int size() {
        if (this.a == null) {
            return 0;
        }
        return this.a.size();
    }
    
    public SelesParameters() {
        this.a = new ArrayList<FilterArg>();
    }
    
    public SelesParameters(final Map<String, String> b) {
        this.a = new ArrayList<FilterArg>();
        this.b = b;
    }
    
    public void appendFloatArg(final String s, final float n) {
        this.appendFloatArg(s, n, 0.0f, 1.0f);
    }
    
    public void appendFloatArg(final String s, final float n, final float n2, final float n3) {
        if (s == null) {
            return;
        }
        this.c = true;
        final FilterArg filterArg = new FilterArg();
        filterArg.b = s;
        filterArg.d = (filterArg.c = n);
        filterArg.e = n2;
        filterArg.f = n3;
        if (this.b != null && this.b.containsKey(s)) {
            final float float1 = StringHelper.parseFloat(this.b.get(s));
            if (0.0f <= float1 && float1 <= 1.0f) {
                filterArg.setPrecentValue(float1);
                filterArg.g = false;
                filterArg.d = filterArg.getValue();
            }
        }
        this.a.add(filterArg);
    }
    
    public ArrayList<FilterArg> changedArgs() {
        final ArrayList<FilterArg> list = new ArrayList<FilterArg>();
        for (final FilterArg e : this.a) {
            if (e.g) {
                e.g = false;
                list.add(e);
            }
        }
        return list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof SelesParameters) || this.a.size() != ((SelesParameters)o).getArgs().size()) {
            return false;
        }
        final List<FilterArg> args = ((SelesParameters)o).getArgs();
        for (int i = 0; i < args.size(); ++i) {
            if (!args.get(i).b.equalsIgnoreCase(this.a.get(i).b)) {
                return false;
            }
        }
        return true;
    }
    
    public void reset() {
        final Iterator<FilterArg> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            iterator.next().reset();
        }
    }
    
    public void reset(final String s) {
        final FilterArg filterArg = this.getFilterArg(s);
        if (filterArg == null) {
            return;
        }
        filterArg.reset();
    }
    
    public FilterArg getFilterArg(final String s) {
        if (StringHelper.isBlank(s)) {
            return null;
        }
        for (final FilterArg filterArg : this.a) {
            if (filterArg.equalsKey(s)) {
                return filterArg;
            }
        }
        return null;
    }
    
    public float getDefaultArg(final String s) {
        if (this.b != null && this.b.containsKey(s)) {
            return StringHelper.parseFloat(this.b.get(s));
        }
        return 0.0f;
    }
    
    public void setFilterArg(final String s, final float precentValue) {
        final FilterArg filterArg = this.getFilterArg(s);
        if (filterArg == null) {
            return;
        }
        filterArg.setPrecentValue(precentValue);
    }
    
    public void stepFilterArg(final String s, final float n) {
        final FilterArg filterArg = this.getFilterArg(s);
        if (filterArg == null) {
            return;
        }
        filterArg.setPrecentValue(filterArg.getPrecentValue() + n);
    }
    
    public void merge(final SelesParameters selesParameters) {
        if (selesParameters == null) {
            return;
        }
        this.a.addAll(selesParameters.a);
    }
    
    public void syncArgs(final SelesParameters selesParameters) {
        if (selesParameters == null) {
            return;
        }
        for (final FilterArg filterArg : selesParameters.a) {
            final FilterArg filterArg2 = this.getFilterArg(filterArg.getKey());
            if (filterArg2 == null) {
                continue;
            }
            filterArg2.setPrecentValue(filterArg.getPrecentValue());
            filterArg2.g = false;
        }
    }
    
    public class FilterArg
    {
        private String b;
        private float c;
        private float d;
        private float e;
        private float f;
        private boolean g;
        
        public void setPrecentValue(float n) {
            if (n < 0.0f) {
                n = 0.0f;
            }
            else if (n > 1.0f) {
                n = 1.0f;
            }
            if (this.getPrecentValue() != n) {
                this.g = true;
                this.c = (this.f - this.e) * n + this.e;
            }
        }
        
        public float getPrecentValue() {
            return (this.c - this.e) / (this.f - this.e);
        }
        
        public float getValue() {
            return this.c;
        }
        
        public void setValue(final float c) {
            this.c = c;
            this.g = true;
        }
        
        public void reset() {
            if (this.c != this.d) {
                this.c = this.d;
                this.g = true;
            }
        }
        
        public String getKey() {
            return this.b;
        }
        
        public boolean equalsKey(final String anotherString) {
            return this.getKey().equalsIgnoreCase(anotherString);
        }
        
        public void setMaxValueFactor(float n) {
            if (n < 0.0f) {
                n = 0.0f;
            }
            if (n > 1.0f) {
                n = 1.0f;
            }
            final float n2 = this.c / (this.f - this.e);
            this.f = this.e + Float.valueOf(String.format("%.2f", (this.f - this.e) * n));
            this.c = (this.f - this.e) * n2;
        }
    }
    
    public interface TileStickerInterface
    {
        void updateTileStickers(final List<TuSdkImage2DSticker> p0);
    }
    
    public interface FilterStickerInterface extends FilterFacePositionInterface
    {
        void updateStickers(final List<TuSDKLiveStickerImage> p0);
        
        void setDisplayRect(final RectF p0, final float p1);
        
        void setEnableAutoplayMode(final boolean p0);
        
        void seekStickerToFrameTime(final long p0);
        
        void setBenchmarkTime(final long p0);
        
        void setStickerVisibility(final boolean p0);
    }
    
    public interface FilterFacePositionInterface
    {
        void updateFaceFeatures(final FaceAligment[] p0, final float p1);
    }
    
    public interface FilterTexturesInterface
    {
        void appendTextures(final List<SelesPicture> p0);
    }
    
    public interface FilterTexturesInterface2
    {
        void appendTextures(final List<Bitmap> p0);
    }
    
    public interface FilterParameterInterface
    {
        SelesParameters getParameter();
        
        void setParameter(final SelesParameters p0);
        
        void submitParameter();
    }
}
