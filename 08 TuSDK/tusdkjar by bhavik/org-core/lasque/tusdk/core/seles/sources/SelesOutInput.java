package org.lasque.tusdk.core.seles.sources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;

public abstract class SelesOutInput
  extends SelesOutput
  implements SelesContext.SelesInput
{
  private SelesParameters a;
  
  public void mountAtGLThread(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    runOnDraw(paramRunnable);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    if (paramSelesParameters == null) {
      paramSelesParameters = new SelesParameters();
    }
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg) {}
  
  public SelesParameters getParameter()
  {
    if (this.a == null) {
      this.a = initParams(null);
    }
    return this.a;
  }
  
  public void setParameter(SelesParameters paramSelesParameters)
  {
    if (paramSelesParameters == null) {
      return;
    }
    if (!paramSelesParameters.isInited()) {
      this.a = initParams(paramSelesParameters);
    } else if (getParameter().equals(paramSelesParameters)) {
      this.a = paramSelesParameters;
    } else {
      return;
    }
    a(this.a.getArgs());
  }
  
  public void submitParameter()
  {
    ArrayList localArrayList = getParameter().changedArgs();
    a(localArrayList);
  }
  
  private void a(List<SelesParameters.FilterArg> paramList)
  {
    if (paramList == null) {
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SelesParameters.FilterArg localFilterArg = (SelesParameters.FilterArg)localIterator.next();
      submitFilterArg(localFilterArg);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesOutInput.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */