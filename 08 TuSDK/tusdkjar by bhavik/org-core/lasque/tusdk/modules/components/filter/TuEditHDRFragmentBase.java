package org.lasque.tusdk.modules.components.filter;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterOption.RunTimeTextureDelegate;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorHDRFilter;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFilterResultFragment;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuEditHDRFragmentBase
  extends TuFilterResultFragment
{
  private ByteBuffer a;
  private FilterOption.RunTimeTextureDelegate b = new FilterOption.RunTimeTextureDelegate()
  {
    public List<SelesPicture> getRunTimeTextures()
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(new SelesPicture(TuEditHDRFragmentBase.a(TuEditHDRFragmentBase.this), 256, 64));
      return localArrayList;
    }
  };
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    super.loadView(paramViewGroup);
    StatisticsManger.appendComponent(ComponentActType.editHDRFragment);
    FilterWrap localFilterWrap = FilterLocalPackage.shared().getFilterWrap(null);
    setFilterWrap(localFilterWrap);
  }
  
  private FilterWrap a()
  {
    FilterOption local1 = new FilterOption(this.b)
    {
      public SelesOutInput getFilter()
      {
        return new TuSDKColorHDRFilter();
      }
    };
    local1.id = Long.MAX_VALUE;
    local1.canDefinition = true;
    local1.isInternal = true;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("d78aa55b64bb63f97bc5feb3c6ba5600");
    local1.internalTextures = localArrayList;
    FilterWrap localFilterWrap = FilterWrap.creat(local1);
    return localFilterWrap;
  }
  
  protected boolean preProcessWithImage(Bitmap paramBitmap)
  {
    if (!SdkValid.shared.hdrFilterEnabled())
    {
      TLog.e("You are not allowed to use the HDR feature, please see http://tusdk.com", new Object[0]);
      return false;
    }
    this.a = TuSDKColorHDRFilter.getClipHistBuffer(paramBitmap);
    return true;
  }
  
  protected void postProcessWithImage(Bitmap paramBitmap)
  {
    setImageViewFilter(a());
    refreshConfigView();
    super.postProcessWithImage(paramBitmap);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\filter\TuEditHDRFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */