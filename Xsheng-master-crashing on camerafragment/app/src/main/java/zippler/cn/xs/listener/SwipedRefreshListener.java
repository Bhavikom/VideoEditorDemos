package zippler.cn.xs.listener;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

/**
 * Created by Zipple on 2018/5/5.
 * To control refresh data.
 */
public class SwipedRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swip;
    private Context context;
    private static final int DELAY = 2000;
    public SwipedRefreshListener(SwipeRefreshLayout swip,Context context) {
        this.swip = swip;
        this.context = context;
    }

    @Override
    public void onRefresh() {
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Toast.makeText(context,"无更多内容",Toast.LENGTH_SHORT).show();
        swip.setRefreshing(false);//隐藏进度条
    }
}
