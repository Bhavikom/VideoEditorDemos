// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.mergefilter;

import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;

import java.util.Iterator;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import java.util.LinkedList;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

public class TuSDKComboFilterWrapChain extends FilterWrap implements SelesParameters.FilterFacePositionInterface
{
    protected LinkedList<FilterWrap> mFilterWrapList;
    private FilterWrap a;
    private FilterWrap b;
    private Object c;
    private boolean d;
    
    public TuSDKComboFilterWrapChain() {
        this.mFilterWrapList = new LinkedList<FilterWrap>();
        this.c = new Object();
        final FilterOption option = FilterLocalPackage.shared().option("None");
        this.changeOption(option);
        this.a = FilterWrap.creat(option);
        this.addTarget((SelesContext.SelesInput)this.a.getFilter(), 0);
        this.b = FilterWrap.creat(FilterLocalPackage.shared().option("None"));
        this.a.getLastFilter().addTarget((SelesContext.SelesInput)this.b.getFilter(), 0);
    }
    
    public void addFilterWrap(final FilterWrap e) {
        if (e == null) {
            return;
        }
        this.a(e);
        synchronized (this.c) {
            this.mFilterWrapList.add(e);
            this.a();
        }
    }
    
    public void insertFilterWrap(final FilterWrap filterWrap, final int index) {
        if (index < 0 || filterWrap == null) {
            return;
        }
        this.a(filterWrap);
        synchronized (this.c) {
            if (index >= this.mFilterWrapList.size()) {
                this.mFilterWrapList.addLast(filterWrap);
            }
            else {
                this.mFilterWrapList.add(index, filterWrap);
            }
            this.a();
        }
    }
    
    private void a(final FilterWrap filterWrap) {
        if (filterWrap instanceof Face2DComboFilterWrap) {
            ((Face2DComboFilterWrap)filterWrap).setIsEnablePlastic(this.d);
        }
    }
    
    public void removeFilterWrap(final FilterWrap filterWrap) {
        if (filterWrap == null || !this.mFilterWrapList.contains(filterWrap)) {
            return;
        }
        synchronized (this.c) {
            this.mFilterWrapList.remove(filterWrap);
            this.a();
        }
    }
    
    public void addTerminalNode(final SelesContext.SelesInput selesInput) {
        if (selesInput == null) {
            return;
        }
        synchronized (this.c) {
            this.b.addTarget(selesInput, 0);
        }
    }
    
    public void removeAllFilterWrapNode() {
        this.mFilterWrapList.clear();
        this.a();
    }
    
    public FilterWrap getFirstFilterWarp() {
        if (this.mFilterWrapList.isEmpty()) {
            return null;
        }
        return this.mFilterWrapList.getFirst();
    }
    
    public TuSDKComboFilterWrapChain clone() {
        final TuSDKComboFilterWrapChain tuSDKComboFilterWrapChain = new TuSDKComboFilterWrapChain();
        if (tuSDKComboFilterWrapChain != null) {
            tuSDKComboFilterWrapChain.mFilterWrapList = this.mFilterWrapList;
            tuSDKComboFilterWrapChain.a = this.a;
            tuSDKComboFilterWrapChain.b = this.b;
        }
        return tuSDKComboFilterWrapChain;
    }
    
    public void updateFaceFeatures(final FaceAligment[] array, final float n) {
        if (this.mFilterWrapList == null || this.mFilterWrapList.size() <= 0) {
            return;
        }
        for (final FilterWrap filterWrap : this.mFilterWrapList) {
            if (filterWrap instanceof SelesParameters.FilterFacePositionInterface) {
                ((SelesParameters.FilterFacePositionInterface)filterWrap).updateFaceFeatures(array, n);
            }
        }
    }
    
    private void a() {
        this.a.getLastFilter().removeAllTargets();
        final Iterator<FilterWrap> iterator = this.mFilterWrapList.iterator();
        while (iterator.hasNext()) {
            iterator.next().getLastFilter().removeAllTargets();
        }
        if (this.mFilterWrapList.size() > 0) {
            this.a.getLastFilter().addTarget((SelesContext.SelesInput)this.mFilterWrapList.getFirst().getFilter(), 0);
            for (int i = 0; i < this.mFilterWrapList.size() - 1; ++i) {
                this.mFilterWrapList.get(i).addTarget((SelesContext.SelesInput)this.mFilterWrapList.get(i + 1).getFilter(), 0);
            }
            this.mFilterWrapList.getLast().addTarget((SelesContext.SelesInput)this.b.getFilter(), 0);
        }
        else {
            this.a.getLastFilter().addTarget((SelesContext.SelesInput)this.b.getFilter(), 0);
        }
    }
    
    public void setIsEnablePlastic(final boolean b) {
        this.d = b;
        if (this.mFilterWrapList == null || this.mFilterWrapList.size() == 0) {
            return;
        }
        for (final FilterWrap filterWrap : this.mFilterWrapList) {
            if (filterWrap != null && filterWrap instanceof Face2DComboFilterWrap) {
                ((Face2DComboFilterWrap)filterWrap).setIsEnablePlastic(b);
            }
        }
    }
    
    public FilterWrap getTerminalFilterWrap() {
        return this.b;
    }
    
    public int getFilterWrapListSize() {
        if (this.mFilterWrapList == null) {
            return 0;
        }
        return this.mFilterWrapList.size();
    }
    
    public void destroy() {
        super.destroy();
        if (this.a != null) {
            this.a.destroy();
            this.a = null;
        }
        if (this.b != null) {
            this.b.destroy();
            this.b = null;
        }
    }
    
    public void setDisplayRect(final RectF rectF, final float n) {
        if (this.mFilterWrapList.size() == 0) {
            return;
        }
        for (final FilterWrap filterWrap : this.mFilterWrapList) {
            if (filterWrap instanceof Face2DComboFilterWrap) {
                ((Face2DComboFilterWrap)filterWrap).setDisplayRect(rectF, n);
            }
        }
    }
}
