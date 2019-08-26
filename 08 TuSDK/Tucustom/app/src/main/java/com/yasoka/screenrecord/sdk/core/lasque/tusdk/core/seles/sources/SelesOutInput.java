// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;

import java.util.Iterator;
import java.util.List;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.SelesContext;

public abstract class SelesOutInput extends SelesOutput implements SelesContext.SelesInput
{
    private SelesParameters a;
    
    @Override
    public void mountAtGLThread(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        this.runOnDraw(runnable);
    }
    
    protected SelesParameters initParams(SelesParameters selesParameters) {
        if (selesParameters == null) {
            selesParameters = new SelesParameters();
        }
        return selesParameters;
    }
    
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
    }
    
    public SelesParameters getParameter() {
        if (this.a == null) {
            this.a = this.initParams(null);
        }
        return this.a;
    }
    
    public void setParameter(final SelesParameters a) {
        if (a == null) {
            return;
        }
        if (!a.isInited()) {
            this.a = this.initParams(a);
        }
        else {
            if (!this.getParameter().equals(a)) {
                return;
            }
            this.a = a;
        }
        this.a(this.a.getArgs());
    }
    
    public void submitParameter() {
        this.a(this.getParameter().changedArgs());
    }
    
    private void a(final List<SelesParameters.FilterArg> list) {
        if (list == null) {
            return;
        }
        final Iterator<SelesParameters.FilterArg> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.submitFilterArg(iterator.next());
        }
    }
}
