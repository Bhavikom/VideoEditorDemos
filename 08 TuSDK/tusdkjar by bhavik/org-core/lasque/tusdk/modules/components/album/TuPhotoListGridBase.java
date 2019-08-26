package org.lasque.tusdk.modules.components.album;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import org.lasque.tusdk.core.task.AlbumTaskManager;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

public abstract class TuPhotoListGridBase
  extends TuSdkCellRelativeLayout<ImageSqlInfo>
{
  public TuPhotoListGridBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuPhotoListGridBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuPhotoListGridBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public abstract ImageView getPosterView();
  
  protected void bindModel()
  {
    AlbumTaskManager.shared.loadThumbImage(getPosterView(), (ImageSqlInfo)getModel());
  }
  
  public void viewNeedRest()
  {
    ViewCompat.setAlpha(getPosterView(), 1.0F);
    AlbumTaskManager.shared.cancelLoadImage(getPosterView());
    if (getPosterView() != null) {
      getPosterView().setImageBitmap(null);
    }
    super.viewNeedRest();
  }
  
  public void viewWillDestory()
  {
    viewNeedRest();
    super.viewWillDestory();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\album\TuPhotoListGridBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */