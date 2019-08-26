package org.lasque.tusdk.core.network.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class ImageMark5FaceArgument<T extends JsonBaseBean>
  extends ArrayList<T>
{
  public static class ImageMarksPoints
    extends JsonBaseBean
    implements Serializable
  {
    @DataBase("x")
    public float x;
    @DataBase("y")
    public float y;
  }
  
  public static class ImageMarks
    extends JsonBaseBean
    implements Serializable
  {
    @DataBase("eye_left")
    public ImageMark5FaceArgument.ImageMarksPoints eye_left;
    @DataBase("eye_right")
    public ImageMark5FaceArgument.ImageMarksPoints eye_right;
    @DataBase("nose")
    public ImageMark5FaceArgument.ImageMarksPoints nose;
    @DataBase("mouth_left")
    public ImageMark5FaceArgument.ImageMarksPoints mouth_left;
    @DataBase("mouth_right")
    public ImageMark5FaceArgument.ImageMarksPoints mouth_right;
  }
  
  public static class ImageItems
    extends JsonBaseBean
    implements Serializable
  {
    @DataBase("marks")
    public ImageMark5FaceArgument.ImageMarks marks;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\analysis\ImageMark5FaceArgument.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */