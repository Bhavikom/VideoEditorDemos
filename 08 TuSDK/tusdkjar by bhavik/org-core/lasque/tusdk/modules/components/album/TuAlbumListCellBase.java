package org.lasque.tusdk.modules.components.album;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import org.lasque.tusdk.core.task.AlbumTaskManager;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

public abstract class TuAlbumListCellBase
  extends TuSdkCellRelativeLayout<AlbumSqlInfo>
{
  public TuAlbumListCellBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuAlbumListCellBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuAlbumListCellBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public abstract ImageView getPosterView();
  
  protected void bindModel()
  {
    AlbumSqlInfo localAlbumSqlInfo = (AlbumSqlInfo)getModel();
    if (localAlbumSqlInfo == null) {
      return;
    }
    a(localAlbumSqlInfo);
  }
  
  private void a(AlbumSqlInfo paramAlbumSqlInfo)
  {
    ImageView localImageView = getPosterView();
    if (localImageView == null) {
      return;
    }
    if (paramAlbumSqlInfo == null)
    {
      localImageView.setImageBitmap(null);
      return;
    }
    AlbumTaskManager.shared.loadThumbImage(localImageView, paramAlbumSqlInfo.cover);
  }
  
  public void viewNeedRest()
  {
    AlbumTaskManager.shared.cancelLoadImage(getPosterView());
    a(null);
    super.viewNeedRest();
  }
  
  public void viewWillDestory()
  {
    viewNeedRest();
    super.viewWillDestory();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\album\TuAlbumListCellBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */