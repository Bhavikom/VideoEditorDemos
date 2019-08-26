package zippler.cn.xs.listener;

import android.support.v7.widget.RecyclerView;

import zippler.cn.xs.util.LinerLayoutManager;

/**
 * Created by Zipple on 2018/5/5.
 * listen the Recycler view
 */
public class RecyclerScrollListener extends RecyclerView.OnScrollListener {
    private LinerLayoutManager linerLayoutManager;
    public RecyclerScrollListener(LinerLayoutManager linerLayoutManager) {
        super();
        this.linerLayoutManager = linerLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        linerLayoutManager.setSpeedSlow();//set slide speed
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }
}
