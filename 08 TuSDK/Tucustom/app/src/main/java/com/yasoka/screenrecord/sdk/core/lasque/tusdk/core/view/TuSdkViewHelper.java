// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;

//import org.lasque.tusdk.core.utils.ContextUtils;

public class TuSdkViewHelper
{
    public static int alertViewIcon;
    private static long a;
    
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(500L);
    }
    
    public static boolean isFastDoubleClick(final long n) {
        final long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - TuSdkViewHelper.a < n && currentTimeMillis - TuSdkViewHelper.a >= 0L) {
            return true;
        }
        TuSdkViewHelper.a = currentTimeMillis;
        return false;
    }
    
    public static <T extends View> T buildView(final Context context, final int n) {
        return buildView(context, n, null);
    }
    
    public static <T extends View> T buildView(Context context, final int n, final ViewGroup viewGroup) {
        if (n == 0) {
            return null;
        }
        if (viewGroup != null && viewGroup.getContext() != null) {
            context = viewGroup.getContext();
        }
        return loadView(LayoutInflater.from(context).inflate(n, viewGroup, false));
    }
    
    public static <T extends View> T loadView(final View view) {
        if (view == null) {
            return null;
        }
        if (view instanceof TuSdkViewInterface) {
            ((TuSdkViewInterface)view).loadView();
        }
        return (T)view;
    }
    
    public static void showView(final View view, final boolean b) {
        if (view == null) {
            return;
        }
        view.setVisibility(b ? View.VISIBLE : View.GONE);
    }
    
    public static void showViewIn(final View view, final boolean b) {
        if (view == null) {
            return;
        }
        view.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }
    
    public static String getTextViewText(final TextView textView) {
        if (textView == null || textView.getText() == null) {
            return null;
        }
        return textView.getText().toString();
    }
    
    public static void setTextViewText(final TextView textView, String text) {
        if (textView == null) {
            return;
        }
        if (text == null) {
            text = "";
        }
        textView.setText((CharSequence)text);
    }
    
    public static Rect getDisplayFrame(final AppCompatActivity activity) {
        final Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect;
    }
    
    public static void setViewHeightConfirmWidth(final View view) {
        if (view == null) {
            return;
        }
        setViewHeight(view, view.getWidth());
    }
    
    public static void setViewRect(final View view, final Rect rect) {
        if (view == null || rect == null) {
            return;
        }
        final ViewGroup.MarginLayoutParams marginLayoutParams = getMarginLayoutParams(view);
        if (marginLayoutParams == null) {
            return;
        }
        marginLayoutParams.width = rect.width();
        marginLayoutParams.height = rect.height();
        marginLayoutParams.leftMargin = rect.left;
        marginLayoutParams.topMargin = rect.top;
        view.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
    }
    
    public static Rect getViewRect(final View view) {
        if (view == null) {
            return null;
        }
        final ViewGroup.MarginLayoutParams marginLayoutParams = getMarginLayoutParams(view);
        if (marginLayoutParams == null) {
            return null;
        }
        return new Rect(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.leftMargin + marginLayoutParams.width, marginLayoutParams.topMargin + marginLayoutParams.height);
    }
    
    public static void setViewHeight(final View view, final int height) {
        if (view == null) {
            return;
        }
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }
    
    public static void setViewWidth(final View view, final int width) {
        if (view == null) {
            return;
        }
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }
    
    public static void setViewMarginLeft(final View view, final int n) {
        view.post((Runnable)new Runnable() {
            @Override
            public void run() {
                final ViewGroup.MarginLayoutParams marginLayoutParams = TuSdkViewHelper.getMarginLayoutParams(view);
                if (marginLayoutParams == null) {
                    return;
                }
                marginLayoutParams.leftMargin = n;
                view.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
            }
        });
    }
    
    public static void setViewMarginTop(final View view, final int topMargin) {
        final ViewGroup.MarginLayoutParams marginLayoutParams = getMarginLayoutParams(view);
        if (marginLayoutParams == null) {
            return;
        }
        marginLayoutParams.topMargin = topMargin;
        view.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
    }
    
    public static void setViewMarginRight(final View view, final int rightMargin) {
        final ViewGroup.MarginLayoutParams marginLayoutParams = getMarginLayoutParams(view);
        if (marginLayoutParams == null) {
            return;
        }
        marginLayoutParams.rightMargin = rightMargin;
        view.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
    }
    
    public static void setViewMarginBottom(final View view, final int bottomMargin) {
        final ViewGroup.MarginLayoutParams marginLayoutParams = getMarginLayoutParams(view);
        if (marginLayoutParams == null) {
            return;
        }
        marginLayoutParams.bottomMargin = bottomMargin;
        view.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
    }
    
    public static void setViewMargin(final View view, final int n, final int n2, final int n3, final int n4) {
        final ViewGroup.MarginLayoutParams marginLayoutParams = getMarginLayoutParams(view);
        if (marginLayoutParams == null) {
            return;
        }
        marginLayoutParams.setMargins(n, n2, n3, n4);
        view.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
    }
    
    public static ViewGroup.MarginLayoutParams getMarginLayoutParams(final View view) {
        if (view == null) {
            return null;
        }
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return null;
        }
        return (ViewGroup.MarginLayoutParams)layoutParams;
    }
    
    public static void toast(final Context context, final String s) {
        Toast.makeText(context, (CharSequence)s, Toast.LENGTH_SHORT).show();
    }
    
    public static void toast(final Context context, final int n) {
        Toast.makeText(context, n, Toast.LENGTH_SHORT).show();
    }
    
    public static void removeGlobalLayoutListener(final ViewTreeObserver viewTreeObserver, final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        if (viewTreeObserver == null || onGlobalLayoutListener == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 16) {
            viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener);
        }
        else {
            a(viewTreeObserver, onGlobalLayoutListener);
        }
    }
    
    @TargetApi(16)
    private static void a(final ViewTreeObserver viewTreeObserver, final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }
    
    public static int locationInWindowTop(final View view) {
        final Rect locationInWindow = locationInWindow(view);
        if (locationInWindow == null) {
            return 0;
        }
        return locationInWindow.top;
    }
    
    public static int locationInWindowLeft(final View view) {
        final Rect locationInWindow = locationInWindow(view);
        if (locationInWindow == null) {
            return 0;
        }
        return locationInWindow.left;
    }
    
    public static Rect locationInWindow(final View view) {
        if (view == null) {
            return null;
        }
        final int[] array = new int[2];
        view.getLocationInWindow(array);
        return new Rect(array[0], array[1], array[0] + view.getWidth(), array[1] + view.getHeight());
    }
    
    public static void viewWillDestory(final View view) {
        if (view == null) {
            return;
        }
        if (view instanceof TuSdkViewInterface) {
            ((TuSdkViewInterface)view).viewWillDestory();
        }
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                viewWillDestory(viewGroup.getChildAt(i));
            }
        }
    }
    
    public static void setBackgroundCornerRadiusDP(final View view, final int n) {
        if (view == null) {
            return;
        }
        setBackgroundCornerRadius(view, ContextUtils.dip2px(view.getContext(), (float)n));
    }
    
    public static void setBackgroundCornerRadius(final View view, final int n) {
        if (view == null) {
            return;
        }
        final Drawable background = view.getBackground();
        GradientDrawable gradientDrawable;
        if (background != null && background instanceof GradientDrawable) {
            gradientDrawable = (GradientDrawable)background;
        }
        else {
            gradientDrawable = new GradientDrawable();
        }
        gradientDrawable.setCornerRadius((float)n);
        if (background != null && background instanceof ColorDrawable) {
            gradientDrawable.setColor(((ColorDrawable)background).getColor());
        }
        setBackground(view, (Drawable)gradientDrawable);
    }
    
    @TargetApi(16)
    public static void setBackground(final View view, final Drawable drawable) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(drawable);
        }
        else {
            view.setBackground(drawable);
        }
    }
    
    public static void alert(final AlertDelegate alertDelegate, final Context context, final int n, final int n2, final int n3, final int n4) {
        if (context == null) {
            return;
        }
        alert(alertDelegate, context, (n != 0) ? context.getResources().getString(n) : null, (n2 != 0) ? context.getResources().getString(n2) : null, (n3 != 0) ? context.getResources().getString(n3) : null, (n4 != 0) ? context.getResources().getString(n4) : null);
    }
    
    public static void alert(final AlertDelegate alertDelegate, final Context context, final String title, final String message, final String s, final String s2) {
        final AlertDialog create = new AlertDialog.Builder(context).create();
        create.setCancelable(false);
        if (TuSdkViewHelper.alertViewIcon != 0) {
            create.setIcon(TuSdkViewHelper.alertViewIcon);
        }
        if (title != null) {
            create.setTitle((CharSequence)title);
        }
        if (message != null) {
            create.setMessage((CharSequence)message);
        }
        final DialogInterface.OnClickListener onClickListener = (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (alertDelegate == null) {
                    return;
                }
                switch (n) {
                    case -1: {
                        alertDelegate.onAlertConfirm(create);
                        break;
                    }
                    case -2: {
                        alertDelegate.onAlertCancel(create);
                        break;
                    }
                }
            }
        };
        create.setButton(-1, (CharSequence)s2, (DialogInterface.OnClickListener)onClickListener);
        create.setButton(-2, (CharSequence)s, (DialogInterface.OnClickListener)onClickListener);
        create.show();
    }
    
    public static void alert(final Context context, final String title, final String message, final String s) {
        final AlertDialog create = new AlertDialog.Builder(context).create();
        if (TuSdkViewHelper.alertViewIcon != 0) {
            create.setIcon(TuSdkViewHelper.alertViewIcon);
        }
        if (title != null) {
            create.setTitle((CharSequence)title);
        }
        if (message != null) {
            create.setMessage((CharSequence)message);
        }
        create.setButton(-2, (CharSequence)s, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
            }
        });
        create.show();
    }
    
    public static WindowManager.LayoutParams buildApplicationPanelParams(final String title) {
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = -1;
        layoutParams.width = -1;
        layoutParams.flags = 128;
        layoutParams.gravity = 17;
        layoutParams.format = -3;
        layoutParams.type = 1000;
        layoutParams.setTitle((CharSequence)title);
        return layoutParams;
    }
    
    static {
        TuSdkViewHelper.alertViewIcon = 0;
    }
    
    public interface EditTextAlertDelegate
    {
        void onEditTextAlertConfirm(final AlertDialog p0, final String p1);
    }
    
    public abstract static class AlertDelegate
    {
        public abstract void onAlertConfirm(final AlertDialog p0);
        
        public void onAlertCancel(final AlertDialog alertDialog) {
        }
    }
    
    public abstract static class OnSafeClickListener implements View.OnClickListener
    {
        private long a;
        
        public OnSafeClickListener() {
            this.a = 500L;
        }
        
        public OnSafeClickListener(final long a) {
            this.a = 500L;
            this.a = a;
        }
        
        public void onClick(final View view) {
            if (TuSdkViewHelper.isFastDoubleClick(this.a)) {
                return;
            }
            this.onSafeClick(view);
        }
        
        public abstract void onSafeClick(final View p0);
    }
}
