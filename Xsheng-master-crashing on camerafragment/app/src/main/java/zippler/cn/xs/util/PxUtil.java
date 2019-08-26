package zippler.cn.xs.util;

import android.content.Context;

/**
 * Created by Zipple on 2018/5/15.
 */

public class PxUtil {
    public static float dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }
    public static float pxTodp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }

}
