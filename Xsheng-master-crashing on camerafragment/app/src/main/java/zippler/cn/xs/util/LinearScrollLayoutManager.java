package zippler.cn.xs.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Zipple on 2018/5/9.
 */

public class LinearScrollLayoutManager extends LinearLayoutManager {
    public LinearScrollLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
