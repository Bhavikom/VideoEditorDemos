package org.lasque.tusdk.impl.components.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
import org.lasque.tusdk.impl.view.widget.TuSeekBar;
import org.lasque.tusdk.impl.view.widget.TuSeekBar.TuSeekBarDelegate;

public class FilterConfigSeekbar
  extends TuSdkRelativeLayout
{
  private TuSeekBar a;
  private TextView b;
  private TextView c;
  private SelesParameters.FilterArg d;
  private String e = "%02d";
  private FilterConfigSeekbarDelegate f;
  private TuSeekBar.TuSeekBarDelegate g = new TuSeekBar.TuSeekBarDelegate()
  {
    public void onTuSeekBarChanged(TuSeekBar paramAnonymousTuSeekBar, float paramAnonymousFloat)
    {
      FilterConfigSeekbar.a(FilterConfigSeekbar.this, paramAnonymousFloat);
    }
  };
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_filter_config_seekbar");
  }
  
  public FilterConfigSeekbar(Context paramContext)
  {
    super(paramContext);
  }
  
  public FilterConfigSeekbar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public FilterConfigSeekbar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSeekBar getSeekbar()
  {
    if (this.a == null)
    {
      this.a = ((TuSeekBar)getViewById("lsq_seekView"));
      if (this.a != null) {
        this.a.setDelegate(this.g);
      }
    }
    return this.a;
  }
  
  public void setValueFormatString(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return;
    }
    this.e = paramString;
  }
  
  private void a(float paramFloat)
  {
    b(paramFloat);
    if (this.f != null) {
      this.f.onSeekbarDataChanged(this, this.d);
    }
  }
  
  public final TextView getTitleView()
  {
    if (this.b == null) {
      this.b = ((TextView)getViewById("lsq_titleView"));
    }
    return this.b;
  }
  
  public final TextView getNumberView()
  {
    if (this.c == null) {
      this.c = ((TextView)getViewById("lsq_numberView"));
    }
    return this.c;
  }
  
  public FilterConfigSeekbarDelegate getDelegate()
  {
    return this.f;
  }
  
  public void setDelegate(FilterConfigSeekbarDelegate paramFilterConfigSeekbarDelegate)
  {
    this.f = paramFilterConfigSeekbarDelegate;
  }
  
  public void setFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    this.d = paramFilterArg;
    if (this.d == null) {
      return;
    }
    TuSeekBar localTuSeekBar = getSeekbar();
    if (localTuSeekBar == null) {
      return;
    }
    localTuSeekBar.setProgress(paramFilterArg.getPrecentValue());
    if (getTitleView() != null) {
      getTitleView().setText(TuSdkContext.getString("lsq_filter_set_" + paramFilterArg.getKey()));
    }
    b(paramFilterArg.getPrecentValue());
  }
  
  private void b(float paramFloat)
  {
    if (this.d != null) {
      this.d.setPrecentValue(paramFloat);
    }
    if (getNumberView() != null) {
      getNumberView().setText(String.format(this.e, new Object[] { Integer.valueOf((int)(paramFloat * 100.0F)) }));
    }
  }
  
  public void reset()
  {
    if (this.d == null) {
      return;
    }
    this.d.reset();
    setFilterArg(this.d);
  }
  
  public static abstract interface FilterConfigSeekbarDelegate
  {
    public abstract void onSeekbarDataChanged(FilterConfigSeekbar paramFilterConfigSeekbar, SelesParameters.FilterArg paramFilterArg);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\FilterConfigSeekbar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */