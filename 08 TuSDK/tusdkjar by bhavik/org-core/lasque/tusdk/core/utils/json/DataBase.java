package org.lasque.tusdk.core.utils.json;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface DataBase
{
  String value();
  
  String sub() default "";
  
  boolean needSub() default false;
  
  boolean read() default true;
  
  boolean write() default true;
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\json\DataBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */