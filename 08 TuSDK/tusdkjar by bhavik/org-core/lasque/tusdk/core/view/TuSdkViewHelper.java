package org.lasque.tusdk.core.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import org.lasque.tusdk.core.utils.ContextUtils;

public class TuSdkViewHelper
{
  public static int alertViewIcon = 0;
  private static long a;
  
  public static boolean isFastDoubleClick()
  {
    return isFastDoubleClick(500L);
  }
  
  public static boolean isFastDoubleClick(long paramLong)
  {
    long l = System.currentTimeMillis();
    if ((l - a < paramLong) && (l - a >= 0L)) {
      return true;
    }
    a = l;
    return false;
  }
  
  public static <T extends View> T buildView(Context paramContext, int paramInt)
  {
    return buildView(paramContext, paramInt, null);
  }
  
  public static <T extends View> T buildView(Context paramContext, int paramInt, ViewGroup paramViewGroup)
  {
    if (paramInt == 0) {
      return null;
    }
    if ((paramViewGroup != null) && (paramViewGroup.getContext() != null)) {
      paramContext = paramViewGroup.getContext();
    }
    View localView = LayoutInflater.from(paramContext).inflate(paramInt, paramViewGroup, false);
    return loadView(localView);
  }
  
  public static <T extends View> T loadView(View paramView)
  {
    if (paramView == null) {
      return null;
    }
    if ((paramView instanceof TuSdkViewInterface)) {
      ((TuSdkViewInterface)paramView).loadView();
    }
    return paramView;
  }
  
  public static void showView(View paramView, boolean paramBoolean)
  {
    if (paramView == null) {
      return;
    }
    paramView.setVisibility(paramBoolean ? 0 : 8);
  }
  
  public static void showViewIn(View paramView, boolean paramBoolean)
  {
    if (paramView == null) {
      return;
    }
    paramView.setVisibility(paramBoolean ? 0 : 4);
  }
  
  public static String getTextViewText(TextView paramTextView)
  {
    if ((paramTextView == null) || (paramTextView.getText() == null)) {
      return null;
    }
    return paramTextView.getText().toString();
  }
  
  public static void setTextViewText(TextView paramTextView, String paramString)
  {
    if (paramTextView == null) {
      return;
    }
    if (paramString == null) {
      paramString = "";
    }
    paramTextView.setText(paramString);
  }
  
  public static Rect getDisplayFrame(Activity paramActivity)
  {
    Rect localRect = new Rect();
    paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
    return localRect;
  }
  
  public static void setViewHeightConfirmWidth(View paramView)
  {
    if (paramView == null) {
      return;
    }
    setViewHeight(paramView, paramView.getWidth());
  }
  
  public static void setViewRect(View paramView, Rect paramRect)
  {
    if ((paramView == null) || (paramRect == null)) {
      return;
    }
    ViewGroup.MarginLayoutParams localMarginLayoutParams = getMarginLayoutParams(paramView);
    if (localMarginLayoutParams == null) {
      return;
    }
    localMarginLayoutParams.width = paramRect.width();
    localMarginLayoutParams.height = paramRect.height();
    localMarginLayoutParams.leftMargin = paramRect.left;
    localMarginLayoutParams.topMargin = paramRect.top;
    paramView.setLayoutParams(localMarginLayoutParams);
  }
  
  public static Rect getViewRect(View paramView)
  {
    if (paramView == null) {
      return null;
    }
    ViewGroup.MarginLayoutParams localMarginLayoutParams = getMarginLayoutParams(paramView);
    if (localMarginLayoutParams == null) {
      return null;
    }
    Rect localRect = new Rect(localMarginLayoutParams.leftMargin, localMarginLayoutParams.topMargin, localMarginLayoutParams.leftMargin + localMarginLayoutParams.width, localMarginLayoutParams.topMargin + localMarginLayoutParams.height);
    return localRect;
  }
  
  public static void setViewHeight(View paramView, int paramInt)
  {
    if (paramView == null) {
      return;
    }
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    if (localLayoutParams == null) {
      return;
    }
    localLayoutParams.height = paramInt;
    paramView.setLayoutParams(localLayoutParams);
  }
  
  public static void setViewWidth(View paramView, int paramInt)
  {
    if (paramView == null) {
      return;
    }
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    if (localLayoutParams == null) {
      return;
    }
    localLayoutParams.width = paramInt;
    paramView.setLayoutParams(localLayoutParams);
  }
  
  public static void setViewMarginLeft(View paramView, final int paramInt)
  {
    paramView.post(new Runnable()
    {
      public void run()
      {
        ViewGroup.MarginLayoutParams localMarginLayoutParams = TuSdkViewHelper.getMarginLayoutParams(this.a);
        if (localMarginLayoutParams == null) {
          return;
        }
        localMarginLayoutParams.leftMargin = paramInt;
        this.a.setLayoutParams(localMarginLayoutParams);
      }
    });
  }
  
  public static void setViewMarginTop(View paramView, int paramInt)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = getMarginLayoutParams(paramView);
    if (localMarginLayoutParams == null) {
      return;
    }
    localMarginLayoutParams.topMargin = paramInt;
    paramView.setLayoutParams(localMarginLayoutParams);
  }
  
  public static void setViewMarginRight(View paramView, int paramInt)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = getMarginLayoutParams(paramView);
    if (localMarginLayoutParams == null) {
      return;
    }
    localMarginLayoutParams.rightMargin = paramInt;
    paramView.setLayoutParams(localMarginLayoutParams);
  }
  
  public static void setViewMarginBottom(View paramView, int paramInt)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = getMarginLayoutParams(paramView);
    if (localMarginLayoutParams == null) {
      return;
    }
    localMarginLayoutParams.bottomMargin = paramInt;
    paramView.setLayoutParams(localMarginLayoutParams);
  }
  
  public static void setViewMargin(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = getMarginLayoutParams(paramView);
    if (localMarginLayoutParams == null) {
      return;
    }
    localMarginLayoutParams.setMargins(paramInt1, paramInt2, paramInt3, paramInt4);
    paramView.setLayoutParams(localMarginLayoutParams);
  }
  
  public static ViewGroup.MarginLayoutParams getMarginLayoutParams(View paramView)
  {
    if (paramView == null) {
      return null;
    }
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    if ((localLayoutParams == null) || (!(localLayoutParams instanceof ViewGroup.MarginLayoutParams))) {
      return null;
    }
    return (ViewGroup.MarginLayoutParams)localLayoutParams;
  }
  
  public static void toast(Context paramContext, String paramString)
  {
    Toast.makeText(paramContext, paramString, 0).show();
  }
  
  public static void toast(Context paramContext, int paramInt)
  {
    Toast.makeText(paramContext, paramInt, 0).show();
  }
  
  public static void removeGlobalLayoutListener(ViewTreeObserver paramViewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener paramOnGlobalLayoutListener)
  {
    if ((paramViewTreeObserver == null) || (paramOnGlobalLayoutListener == null)) {
      return;
    }
    if (Build.VERSION.SDK_INT < 16) {
      paramViewTreeObserver.removeGlobalOnLayoutListener(paramOnGlobalLayoutListener);
    } else {
      a(paramViewTreeObserver, paramOnGlobalLayoutListener);
    }
  }
  
  @TargetApi(16)
  private static void a(ViewTreeObserver paramViewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener paramOnGlobalLayoutListener)
  {
    paramViewTreeObserver.removeOnGlobalLayoutListener(paramOnGlobalLayoutListener);
  }
  
  public static int locationInWindowTop(View paramView)
  {
    Rect localRect = locationInWindow(paramView);
    if (localRect == null) {
      return 0;
    }
    return localRect.top;
  }
  
  public static int locationInWindowLeft(View paramView)
  {
    Rect localRect = locationInWindow(paramView);
    if (localRect == null) {
      return 0;
    }
    return localRect.left;
  }
  
  public static Rect locationInWindow(View paramView)
  {
    if (paramView == null) {
      return null;
    }
    int[] arrayOfInt = new int[2];
    paramView.getLocationInWindow(arrayOfInt);
    Rect localRect = new Rect(arrayOfInt[0], arrayOfInt[1], arrayOfInt[0] + paramView.getWidth(), arrayOfInt[1] + paramView.getHeight());
    return localRect;
  }
  
  public static void viewWillDestory(View paramView)
  {
    if (paramView == null) {
      return;
    }
    if ((paramView instanceof TuSdkViewInterface)) {
      ((TuSdkViewInterface)paramView).viewWillDestory();
    }
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int i = 0;
      int j = localViewGroup.getChildCount();
      while (i < j)
      {
        View localView = localViewGroup.getChildAt(i);
        viewWillDestory(localView);
        i++;
      }
    }
  }
  
  public static void setBackgroundCornerRadiusDP(View paramView, int paramInt)
  {
    if (paramView == null) {
      return;
    }
    int i = ContextUtils.dip2px(paramView.getContext(), paramInt);
    setBackgroundCornerRadius(paramView, i);
  }
  
  public static void setBackgroundCornerRadius(View paramView, int paramInt)
  {
    if (paramView == null) {
      return;
    }
    Drawable localDrawable = paramView.getBackground();
    GradientDrawable localGradientDrawable = null;
    if ((localDrawable != null) && ((localDrawable instanceof GradientDrawable))) {
      localGradientDrawable = (GradientDrawable)localDrawable;
    } else {
      localGradientDrawable = new GradientDrawable();
    }
    localGradientDrawable.setCornerRadius(paramInt);
    if ((localDrawable != null) && ((localDrawable instanceof ColorDrawable)))
    {
      int i = ((ColorDrawable)localDrawable).getColor();
      localGradientDrawable.setColor(i);
    }
    setBackground(paramView, localGradientDrawable);
  }
  
  @TargetApi(16)
  public static void setBackground(View paramView, Drawable paramDrawable)
  {
    if (paramView == null) {
      return;
    }
    if (Build.VERSION.SDK_INT < 16) {
      paramView.setBackgroundDrawable(paramDrawable);
    } else {
      paramView.setBackground(paramDrawable);
    }
  }
  
  public static void alert(AlertDelegate paramAlertDelegate, Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramContext == null) {
      return;
    }
    String str1 = paramInt1 != 0 ? paramContext.getResources().getString(paramInt1) : null;
    String str2 = paramInt2 != 0 ? paramContext.getResources().getString(paramInt2) : null;
    String str3 = paramInt3 != 0 ? paramContext.getResources().getString(paramInt3) : null;
    String str4 = paramInt4 != 0 ? paramContext.getResources().getString(paramInt4) : null;
    alert(paramAlertDelegate, paramContext, str1, str2, str3, str4);
  }
  
  public static void alert(AlertDelegate paramAlertDelegate, Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    final AlertDialog localAlertDialog = new AlertDialog.Builder(paramContext).create();
    localAlertDialog.setCancelable(false);
    if (alertViewIcon != 0) {
      localAlertDialog.setIcon(alertViewIcon);
    }
    if (paramString1 != null) {
      localAlertDialog.setTitle(paramString1);
    }
    if (paramString2 != null) {
      localAlertDialog.setMessage(paramString2);
    }
    DialogInterface.OnClickListener local2 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        if (this.a == null) {
          return;
        }
        switch (paramAnonymousInt)
        {
        case -1: 
          this.a.onAlertConfirm(localAlertDialog);
          break;
        case -2: 
          this.a.onAlertCancel(localAlertDialog);
          break;
        }
      }
    };
    localAlertDialog.setButton(-1, paramString4, local2);
    localAlertDialog.setButton(-2, paramString3, local2);
    localAlertDialog.show();
  }
  
  public static void alert(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    AlertDialog localAlertDialog = new AlertDialog.Builder(paramContext).create();
    if (alertViewIcon != 0) {
      localAlertDialog.setIcon(alertViewIcon);
    }
    if (paramString1 != null) {
      localAlertDialog.setTitle(paramString1);
    }
    if (paramString2 != null) {
      localAlertDialog.setMessage(paramString2);
    }
    localAlertDialog.setButton(-2, paramString3, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {}
    });
    localAlertDialog.show();
  }
  
  public static WindowManager.LayoutParams buildApplicationPanelParams(String paramString)
  {
    WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
    localLayoutParams.height = -1;
    localLayoutParams.width = -1;
    localLayoutParams.flags = 128;
    localLayoutParams.gravity = 17;
    localLayoutParams.format = -3;
    localLayoutParams.type = 1000;
    localLayoutParams.setTitle(paramString);
    return localLayoutParams;
  }
  
  public static abstract interface EditTextAlertDelegate
  {
    public abstract void onEditTextAlertConfirm(AlertDialog paramAlertDialog, String paramString);
  }
  
  public static abstract class AlertDelegate
  {
    public abstract void onAlertConfirm(AlertDialog paramAlertDialog);
    
    public void onAlertCancel(AlertDialog paramAlertDialog) {}
  }
  
  public static abstract class OnSafeClickListener
    implements View.OnClickListener
  {
    private long a = 500L;
    
    public OnSafeClickListener() {}
    
    public OnSafeClickListener(long paramLong)
    {
      this.a = paramLong;
    }
    
    public void onClick(View paramView)
    {
      if (TuSdkViewHelper.isFastDoubleClick(this.a)) {
        return;
      }
      onSafeClick(paramView);
    }
    
    public abstract void onSafeClick(View paramView);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TuSdkViewHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */