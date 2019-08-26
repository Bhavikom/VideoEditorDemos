package org.lasque.tusdk.modules.view.widget.paintdraw;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.hardware.TuSdkSharedPreferences;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class PaintBarViewBase
  extends TuSdkRelativeLayout
{
  private List<PaintData> a;
  private List<String> b;
  private boolean c = true;
  
  public abstract <T extends View,  extends PaintTableViewInterface> T getTableView();
  
  protected abstract void notifySelectedPaint(PaintData paramPaintData);
  
  protected abstract void refreshPaintDatas();
  
  public PaintBarViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public PaintBarViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public PaintBarViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void loadPaints()
  {
    List localList = null;
    if ((this.a == null) || (this.a.size() == 0))
    {
      localList = buildPaintItems();
      this.a = localList;
    }
    PaintData localPaintData = null;
    int i = a(PaintData.PaintType.Color);
    if (i == -1)
    {
      if (!localList.isEmpty()) {
        localPaintData = (PaintData)localList.get(1);
      }
    }
    else {
      localPaintData = (PaintData)localList.get(i);
    }
    if (getTableView() != null)
    {
      ((PaintTableViewInterface)getTableView()).setModeList(localList);
      i = localList.indexOf(localPaintData);
      ((PaintTableViewInterface)getTableView()).setSelectedPosition(i, true);
      ((PaintTableViewInterface)getTableView()).scrollToPosition(i);
    }
    notifySelectedPaint(localPaintData);
  }
  
  private int a(PaintData.PaintType paramPaintType)
  {
    int i = -1;
    if (this.c)
    {
      String str1 = String.format("lsq_lastpaint_%s", new Object[] { paramPaintType });
      String str2 = TuSdkContext.sharedPreferences().loadSharedCache(str1);
      if (str2 != null)
      {
        Iterator localIterator = this.a.iterator();
        while (localIterator.hasNext())
        {
          PaintData localPaintData = (PaintData)localIterator.next();
          if (((Integer)localPaintData.getData()).equals(Integer.valueOf(str2)))
          {
            i = this.a.indexOf(localPaintData);
            break;
          }
        }
      }
    }
    return i;
  }
  
  protected List<PaintData> buildPaintItems()
  {
    ArrayList localArrayList = new ArrayList();
    if ((this.b == null) || (this.b.isEmpty())) {
      a(localArrayList);
    } else {
      try
      {
        Iterator localIterator = this.b.iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          localArrayList.add(new PaintData(Color.parseColor(str), PaintData.PaintType.Color));
        }
      }
      catch (Exception localException)
      {
        localArrayList.clear();
        this.b.clear();
        a(localArrayList);
      }
    }
    return localArrayList;
  }
  
  private void a(List<PaintData> paramList)
  {
    paramList.add(new PaintData(Color.parseColor("#f9f9f9"), PaintData.PaintType.Color));
    paramList.add(new PaintData(Color.parseColor("#2b2b2b"), PaintData.PaintType.Color));
    paramList.add(new PaintData(Color.parseColor("#ff1d12"), PaintData.PaintType.Color));
    paramList.add(new PaintData(Color.parseColor("#fbf606"), PaintData.PaintType.Color));
    paramList.add(new PaintData(Color.parseColor("#14e213"), PaintData.PaintType.Color));
    paramList.add(new PaintData(Color.parseColor("#199bff"), PaintData.PaintType.Color));
    paramList.add(new PaintData(Color.parseColor("#8c06ff"), PaintData.PaintType.Color));
  }
  
  protected void addColorItem(PaintData paramPaintData)
  {
    this.a.add(paramPaintData);
    refreshPaintDatas();
  }
  
  protected void addBrushGroup(List<String> paramList)
  {
    this.b = paramList;
  }
  
  public void clearColors()
  {
    if (this.a == null) {
      return;
    }
    this.a.clear();
    refreshPaintDatas();
  }
  
  public void selectPaint(PaintData paramPaintData, PaintDrawBarItemCellBase paramPaintDrawBarItemCellBase, int paramInt)
  {
    ((PaintTableViewInterface)getTableView()).changeSelectedPosition(paramInt);
    ((PaintTableViewInterface)getTableView()).smoothScrollByCenter(paramPaintDrawBarItemCellBase);
    if (paramPaintData != null) {
      a(paramPaintData);
    }
  }
  
  private void a(PaintData paramPaintData)
  {
    if (paramPaintData == null) {
      return;
    }
    String str1 = String.valueOf(paramPaintData.getData());
    if (!this.c) {
      return;
    }
    String str2 = String.format("lsq_lastpaint_%s", new Object[] { paramPaintData.getPaintType() });
    TuSdkContext.sharedPreferences().saveSharedCache(str2, str1);
  }
  
  public List<PaintData> getCurrentColors()
  {
    return this.a;
  }
  
  public boolean isSaveLastPaint()
  {
    return this.c;
  }
  
  public void setSaveLastPaint(boolean paramBoolean)
  {
    this.c = paramBoolean;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\paintdraw\PaintBarViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */