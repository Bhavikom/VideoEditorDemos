package org.lasque.tusdk.impl.view.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Calendar;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.listview.TuSdkRefreshListHeaderView;
import org.lasque.tusdk.core.view.listview.TuSdkRefreshListHeaderView.TuSdkRefreshState;
import org.lasque.tusdk.impl.TuDateHelper;

public class TuRefreshListHeaderView
  extends TuSdkRefreshListHeaderView
{
  private RelativeLayout a;
  private TextView b;
  private TextView c;
  private ImageView d;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_view_widget_list_view_refresh_header_view");
  }
  
  public TuRefreshListHeaderView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuRefreshListHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuRefreshListHeaderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public RelativeLayout getHeadWrap()
  {
    if (this.a == null) {
      this.a = ((RelativeLayout)getViewById("lsq_headWrap"));
    }
    return this.a;
  }
  
  public void setHeadWrap(RelativeLayout paramRelativeLayout)
  {
    this.a = paramRelativeLayout;
  }
  
  public TextView getTitleLabel()
  {
    if (this.b == null) {
      this.b = ((TextView)getViewById("lsq_titleLabel"));
    }
    return this.b;
  }
  
  public void setTitleLabel(TextView paramTextView)
  {
    this.b = paramTextView;
  }
  
  public TextView getLastLoadTime()
  {
    if (this.c == null) {
      this.c = ((TextView)getViewById("lsq_lastLoadTime"));
    }
    return this.c;
  }
  
  public void setLastLoadTime(TextView paramTextView)
  {
    this.c = paramTextView;
  }
  
  public ImageView getLoadIcon()
  {
    if (this.d == null) {
      this.d = ((ImageView)getViewById("lsq_loadIcon"));
    }
    return this.d;
  }
  
  public void setLoadIcon(ImageView paramImageView)
  {
    this.d = paramImageView;
  }
  
  public void setLastDate(Calendar paramCalendar)
  {
    super.setLastDate(paramCalendar);
    TextView localTextView = getLastLoadTime();
    if ((paramCalendar == null) || (localTextView == null)) {
      return;
    }
    localTextView.setText(TuSdkContext.getString("lsq_refresh_list_view_state_lasttime") + TuDateHelper.timestampSNS(paramCalendar));
  }
  
  public void setState(TuSdkRefreshListHeaderView.TuSdkRefreshState paramTuSdkRefreshState)
  {
    super.setState(paramTuSdkRefreshState);
    if ((paramTuSdkRefreshState == null) || (this.b == null)) {
      return;
    }
    int i = 0;
    switch (1.a[paramTuSdkRefreshState.ordinal()])
    {
    case 1: 
      i = TuSdkContext.getStringResId("lsq_refresh_list_view_state_hidden");
      break;
    case 2: 
      i = TuSdkContext.getStringResId("lsq_refresh_list_view_state_visible");
      break;
    case 3: 
      i = TuSdkContext.getStringResId("lsq_refresh_list_view_state_triggered");
      break;
    case 4: 
      i = TuSdkContext.getStringResId("lsq_refresh_list_view_state_loading");
      break;
    }
    this.b.setText(i);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\listview\TuRefreshListHeaderView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */