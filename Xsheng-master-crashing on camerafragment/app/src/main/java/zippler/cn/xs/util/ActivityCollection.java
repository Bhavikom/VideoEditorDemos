package zippler.cn.xs.util;

/**
 * Created by zipple on 2018/3/1.
 */

import android.app.Activity;

import java.util.ArrayList;

/**
 * 管理活动
 */
public class ActivityCollection {
    public static ArrayList<Activity> activities = new ArrayList<>();//必须是static，用于保存activity实例

    /**
     * 保存活动
     * @param activity 活动
     */
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    /**
     * 删除活动，boolean是否删除成功
     * @param activity 待删除活动
     * @return 返回是否删除成功
     */
    public static boolean removeActivity(Activity activity){
        return activities.remove(activity);
    }

    /**
     * 销毁所有活动
     */
    public static void finishAll(){
        for (Activity activity:activities) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
