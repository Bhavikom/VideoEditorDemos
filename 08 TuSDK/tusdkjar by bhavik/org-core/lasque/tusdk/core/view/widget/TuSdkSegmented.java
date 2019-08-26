package org.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import org.lasque.tusdk.core.view.TuSdkLinearLayout;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;

public abstract class TuSdkSegmented
  extends TuSdkRelativeLayout
{
  protected ArrayList<TuSdkSegmentedButton> buttons = new ArrayList();
  protected TuSdkSegmentedButton currentButton;
  protected TuSdkLinearLayout segmentedWrap;
  protected TuSdkSegmentedDelegate mDelegate;
  private TuSdkViewHelper.OnSafeClickListener a = new TuSdkViewHelper.OnSafeClickListener()
  {
    public void onSafeClick(View paramAnonymousView)
    {
      TuSdkSegmented.this.onSegmentedClicked((TuSdkSegmented.TuSdkSegmentedButton)paramAnonymousView);
    }
  };
  
  public TuSdkSegmented(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkSegmented(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkSegmented(Context paramContext)
  {
    super(paramContext);
  }
  
  public void loadView()
  {
    super.loadView();
    this.segmentedWrap = findSegmentedWrap();
    this.segmentedWrap.removeAllViews();
  }
  
  protected abstract TuSdkLinearLayout findSegmentedWrap();
  
  protected abstract <T extends View,  extends TuSdkSegmentedButton> T buildSegmented(String paramString);
  
  protected View buildSplitView()
  {
    return null;
  }
  
  public void addSegmentedText(int... paramVarArgs)
  {
    String[] arrayOfString = new String[paramVarArgs.length];
    int i = 0;
    for (int m : paramVarArgs)
    {
      arrayOfString[i] = getResString(m);
      i++;
    }
    addSegmentedText(arrayOfString);
  }
  
  public void addSegmentedText(String... paramVarArgs)
  {
    View localView1 = null;
    for (String str : paramVarArgs)
    {
      View localView2 = buildSegmented(str);
      localView2.setOnClickListener(this.a);
      this.segmentedWrap.addView(localView2);
      TuSdkSegmentedButton localTuSdkSegmentedButton = (TuSdkSegmentedButton)localView2;
      localTuSdkSegmentedButton.setTitle(str);
      this.buttons.add(localTuSdkSegmentedButton);
      localView1 = buildSplitView();
      if (localView1 != null) {
        this.segmentedWrap.addView(localView1);
      }
    }
    if (localView1 != null) {
      this.segmentedWrap.removeView(localView1);
    }
  }
  
  protected void onSegmentedClicked(TuSdkSegmentedButton paramTuSdkSegmentedButton)
  {
    if (paramTuSdkSegmentedButton.isSegmentSelected()) {
      return;
    }
    int i = this.buttons.indexOf(paramTuSdkSegmentedButton);
    changeSelected(i);
    if (this.mDelegate != null) {
      this.mDelegate.onLasqueSegmentedSelected(this, i);
    }
  }
  
  public void setSegmentedDelegate(TuSdkSegmentedDelegate paramTuSdkSegmentedDelegate)
  {
    this.mDelegate = paramTuSdkSegmentedDelegate;
  }
  
  public void changeSelected(int paramInt)
  {
    if (paramInt >= this.buttons.size()) {
      return;
    }
    int i = 0;
    Iterator localIterator = this.buttons.iterator();
    while (localIterator.hasNext())
    {
      TuSdkSegmentedButton localTuSdkSegmentedButton = (TuSdkSegmentedButton)localIterator.next();
      localTuSdkSegmentedButton.onChangeSelected(i == paramInt);
      i++;
    }
  }
  
  public void viewWillDestory()
  {
    super.viewWillDestory();
    this.mDelegate = null;
    this.buttons.clear();
  }
  
  public static abstract interface TuSdkSegmentedButton
  {
    public abstract void onChangeSelected(boolean paramBoolean);
    
    public abstract boolean isSegmentSelected();
    
    public abstract void setTitle(String paramString);
  }
  
  public static abstract interface TuSdkSegmentedDelegate
  {
    public abstract void onLasqueSegmentedSelected(TuSdkSegmented paramTuSdkSegmented, int paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuSdkSegmented.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */