package zippler.cn.xs.util;

/**
 * Created by zipple on 2018/3/2.
 */

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Control the slide speed of LinearLayoutManager
 */
public class LinerLayoutManager extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH = 0.03f;
    private Context context;

    public LinerLayoutManager(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return LinerLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    //This returns the milliseconds it takes to
                    //scroll one pixel.
                    @Override
                    protected float calculateSpeedPerPixel
                    (DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.density;
                        //return how many milliseconds will it take to return a sliding pixel
                    }

                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    public void setSpeedSlow() {
        MILLISECONDS_PER_INCH = context.getResources().getDisplayMetrics().density * 0.3f;
    }

    public void setSpeedFast() {
        MILLISECONDS_PER_INCH = context.getResources().getDisplayMetrics().density * 0.03f;
    }
}