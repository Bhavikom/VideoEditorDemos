package org.lasque.tusdk.core.mergefilter;

import android.graphics.RectF;
import java.util.Iterator;
import java.util.LinkedList;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;

public class TuSDKComboFilterWrapChain
  extends FilterWrap
  implements SelesParameters.FilterFacePositionInterface
{
  protected LinkedList<FilterWrap> mFilterWrapList = new LinkedList();
  private FilterWrap a;
  private FilterWrap b;
  private Object c = new Object();
  private boolean d;
  
  public TuSDKComboFilterWrapChain()
  {
    FilterOption localFilterOption1 = FilterLocalPackage.shared().option("None");
    changeOption(localFilterOption1);
    this.a = FilterWrap.creat(localFilterOption1);
    addTarget(this.a.getFilter(), 0);
    FilterOption localFilterOption2 = FilterLocalPackage.shared().option("None");
    this.b = FilterWrap.creat(localFilterOption2);
    this.a.getLastFilter().addTarget(this.b.getFilter(), 0);
  }
  
  public void addFilterWrap(FilterWrap paramFilterWrap)
  {
    if (paramFilterWrap == null) {
      return;
    }
    a(paramFilterWrap);
    synchronized (this.c)
    {
      this.mFilterWrapList.add(paramFilterWrap);
      a();
    }
  }
  
  public void insertFilterWrap(FilterWrap paramFilterWrap, int paramInt)
  {
    if ((paramInt < 0) || (paramFilterWrap == null)) {
      return;
    }
    a(paramFilterWrap);
    synchronized (this.c)
    {
      if (paramInt >= this.mFilterWrapList.size()) {
        this.mFilterWrapList.addLast(paramFilterWrap);
      } else {
        this.mFilterWrapList.add(paramInt, paramFilterWrap);
      }
      a();
    }
  }
  
  private void a(FilterWrap paramFilterWrap)
  {
    if ((paramFilterWrap instanceof Face2DComboFilterWrap)) {
      ((Face2DComboFilterWrap)paramFilterWrap).setIsEnablePlastic(this.d);
    }
  }
  
  public void removeFilterWrap(FilterWrap paramFilterWrap)
  {
    if ((paramFilterWrap == null) || (!this.mFilterWrapList.contains(paramFilterWrap))) {
      return;
    }
    synchronized (this.c)
    {
      this.mFilterWrapList.remove(paramFilterWrap);
      a();
    }
  }
  
  public void addTerminalNode(SelesContext.SelesInput paramSelesInput)
  {
    if (paramSelesInput == null) {
      return;
    }
    synchronized (this.c)
    {
      this.b.addTarget(paramSelesInput, 0);
    }
  }
  
  public void removeAllFilterWrapNode()
  {
    this.mFilterWrapList.clear();
    a();
  }
  
  public FilterWrap getFirstFilterWarp()
  {
    if (this.mFilterWrapList.isEmpty()) {
      return null;
    }
    return (FilterWrap)this.mFilterWrapList.getFirst();
  }
  
  public TuSDKComboFilterWrapChain clone()
  {
    TuSDKComboFilterWrapChain localTuSDKComboFilterWrapChain = new TuSDKComboFilterWrapChain();
    if (localTuSDKComboFilterWrapChain != null)
    {
      localTuSDKComboFilterWrapChain.mFilterWrapList = this.mFilterWrapList;
      localTuSDKComboFilterWrapChain.a = this.a;
      localTuSDKComboFilterWrapChain.b = this.b;
    }
    return localTuSDKComboFilterWrapChain;
  }
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    if ((this.mFilterWrapList == null) || (this.mFilterWrapList.size() <= 0)) {
      return;
    }
    Iterator localIterator = this.mFilterWrapList.iterator();
    while (localIterator.hasNext())
    {
      FilterWrap localFilterWrap = (FilterWrap)localIterator.next();
      if ((localFilterWrap instanceof SelesParameters.FilterFacePositionInterface)) {
        ((SelesParameters.FilterFacePositionInterface)localFilterWrap).updateFaceFeatures(paramArrayOfFaceAligment, paramFloat);
      }
    }
  }
  
  private void a()
  {
    this.a.getLastFilter().removeAllTargets();
    Iterator localIterator = this.mFilterWrapList.iterator();
    while (localIterator.hasNext())
    {
      FilterWrap localFilterWrap = (FilterWrap)localIterator.next();
      localFilterWrap.getLastFilter().removeAllTargets();
    }
    if (this.mFilterWrapList.size() > 0)
    {
      this.a.getLastFilter().addTarget(((FilterWrap)this.mFilterWrapList.getFirst()).getFilter(), 0);
      for (int i = 0; i < this.mFilterWrapList.size() - 1; i++) {
        ((FilterWrap)this.mFilterWrapList.get(i)).addTarget(((FilterWrap)this.mFilterWrapList.get(i + 1)).getFilter(), 0);
      }
      ((FilterWrap)this.mFilterWrapList.getLast()).addTarget(this.b.getFilter(), 0);
    }
    else
    {
      this.a.getLastFilter().addTarget(this.b.getFilter(), 0);
    }
  }
  
  public void setIsEnablePlastic(boolean paramBoolean)
  {
    this.d = paramBoolean;
    if ((this.mFilterWrapList == null) || (this.mFilterWrapList.size() == 0)) {
      return;
    }
    Iterator localIterator = this.mFilterWrapList.iterator();
    while (localIterator.hasNext())
    {
      FilterWrap localFilterWrap = (FilterWrap)localIterator.next();
      if ((localFilterWrap != null) && ((localFilterWrap instanceof Face2DComboFilterWrap))) {
        ((Face2DComboFilterWrap)localFilterWrap).setIsEnablePlastic(paramBoolean);
      }
    }
  }
  
  public FilterWrap getTerminalFilterWrap()
  {
    return this.b;
  }
  
  public int getFilterWrapListSize()
  {
    if (this.mFilterWrapList == null) {
      return 0;
    }
    return this.mFilterWrapList.size();
  }
  
  public void destroy()
  {
    super.destroy();
    if (this.a != null)
    {
      this.a.destroy();
      this.a = null;
    }
    if (this.b != null)
    {
      this.b.destroy();
      this.b = null;
    }
  }
  
  public void setDisplayRect(RectF paramRectF, float paramFloat)
  {
    if (this.mFilterWrapList.size() == 0) {
      return;
    }
    Iterator localIterator = this.mFilterWrapList.iterator();
    while (localIterator.hasNext())
    {
      FilterWrap localFilterWrap = (FilterWrap)localIterator.next();
      if ((localFilterWrap instanceof Face2DComboFilterWrap)) {
        ((Face2DComboFilterWrap)localFilterWrap).setDisplayRect(paramRectF, paramFloat);
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\mergefilter\TuSDKComboFilterWrapChain.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */