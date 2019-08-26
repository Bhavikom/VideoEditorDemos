package org.lasque.tusdk.core.secret;

import android.widget.ImageView;
import org.lasque.tusdk.core.seles.tusdk.FilterGroup;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.task.ImageViewTaskWare;

public class FilterThumbTaskImageWare
  extends ImageViewTaskWare
{
  public FilterGroup group;
  public FilterOption option;
  public FilterThumbTaskType taskType;
  
  public FilterThumbTaskImageWare(ImageView paramImageView, FilterThumbTaskType paramFilterThumbTaskType, FilterOption paramFilterOption, FilterGroup paramFilterGroup)
  {
    setImageView(paramImageView);
    this.taskType = paramFilterThumbTaskType;
    this.option = paramFilterOption;
    this.group = paramFilterGroup;
  }
  
  public static enum FilterThumbTaskType
  {
    private FilterThumbTaskType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\FilterThumbTaskImageWare.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */