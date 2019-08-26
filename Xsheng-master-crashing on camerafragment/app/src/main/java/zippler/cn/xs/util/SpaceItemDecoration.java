package zippler.cn.xs.util;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Zipple on 2018/5/5.
 * To control RecyclerView item space
 * But it worked not so good... at 2018/5/5 21:48
 * It will add space in top 1 item...
 * After I modified some code  in this class ,It worked well.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
    private int leftRight;
    private int topBottom;

    public SpaceItemDecoration(int leftRight, int topBottom) {
        super();
        this.leftRight = leftRight;
        this.topBottom = topBottom;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            outRect.top = topBottom;
            outRect.left = leftRight;
            outRect.right = leftRight;
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.bottom = 0;
            }
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = 0;
            }

        } else {
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.right = leftRight;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
            outRect.bottom = topBottom;
        }
    }

    public int getLeftRight() {
        return leftRight;
    }

    public void setLeftRight(int leftRight) {
        this.leftRight = leftRight;
    }

    public int getTopBottom() {
        return topBottom;
    }

    public void setTopBottom(int topBottom) {
        this.topBottom = topBottom;
    }
}
