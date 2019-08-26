package zippler.cn.xs.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import zippler.cn.xs.listener.OnPageChangedListener;


/**
 * Created by Zipple on 2018/5/12.
 */
public class PagingScrollHelper {
    private static final String TAG = "PagingScrollHelper";
    RecyclerView mRecyclerView = null;
    private int currentPage = -1;
    private int oldPage;

    private RecyclerView.LayoutManager layoutManager;

    private MyOnScrollListener mOnScrollListener = new MyOnScrollListener();

    private MyOnFlingListener mOnFlingListener = new MyOnFlingListener();

    private OnPageChangedListener pageChangedListener;


    private int offsetY = 0;
    private int offsetX = 0;

    int startY = 0;
    int startX = 0;


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public OnPageChangedListener getPageChangedListener() {
        return pageChangedListener;
    }

    public void setOnPageChangedListener(OnPageChangedListener pageChangedListener) {
        this.pageChangedListener = pageChangedListener;
    }

    public int getOldPage() {
        return oldPage;
    }

    public void setOldPage(int oldPage) {
        this.oldPage = oldPage;
    }


    enum ORIENTATION {
        HORIZONTAL, VERTICAL, NULL
    }

    ORIENTATION mOrientation = ORIENTATION.HORIZONTAL;

    public PagingScrollHelper(){

    }


    public void setUpRecycleView(RecyclerView recycleView) {
        if (recycleView == null) {
            throw new IllegalArgumentException("recycleView must be not null");
        }
        mRecyclerView = recycleView;
        recycleView.setOnFlingListener(mOnFlingListener);
        recycleView.addOnScrollListener(mOnScrollListener);
        recycleView.setOnTouchListener(mOnTouchListener);
        updateLayoutManger();
    }

    public void updateLayoutManger() {
        layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.canScrollVertically()) {
                mOrientation = ORIENTATION.VERTICAL;
            } else if (layoutManager.canScrollHorizontally()) {
                mOrientation = ORIENTATION.HORIZONTAL;
            } else {
                mOrientation = ORIENTATION.NULL;
            }
            if (mAnimator != null) {
                mAnimator.cancel();
            }
            startX = 0;
            startY = 0;
            offsetX = 0;
            offsetY = 0;

        }

    }

    ValueAnimator mAnimator = null;

    public class MyOnFlingListener extends RecyclerView.OnFlingListener {

        @Override
        public boolean onFling(int velocityX, int velocityY) {
            if (mOrientation == ORIENTATION.NULL) {
                return false;
            }
            Log.e(TAG, "onFling: 开始计算当前页面p时的startY:"+startY);
            int p = getStartPageIndex();
            Log.e(TAG, "onFling: 开始滚动时的index="+p );

            int endPoint = 0;
            int startPoint = 0;

            if (mOrientation == ORIENTATION.VERTICAL) {
                startPoint = offsetY;

                if (velocityY < 0) {
                    p--;
                } else if (velocityY > 0) {
                    p++;
                }

                Log.e(TAG, "onFling: 处理后的index="+p );
                endPoint = p * mRecyclerView.getHeight();

            } else {
                startPoint = offsetX;
                if (velocityX < 0) {
                    p--;
                } else if (velocityX > 0) {
                    p++;
                }
                endPoint = p * mRecyclerView.getWidth();

            }
            oldPage = currentPage;
            currentPage = p;
            if (endPoint < 0) {
                endPoint = 0;
            }

            if (mAnimator == null) {
                mAnimator = new ValueAnimator().ofInt(startPoint, endPoint);

                mAnimator.setDuration(300);
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int nowPoint = (int) animation.getAnimatedValue();

                        if (mOrientation == ORIENTATION.VERTICAL) {
                            int dy = nowPoint - offsetY;
                            mRecyclerView.scrollBy(0, dy);
                        } else {
                            int dx = nowPoint - offsetX;
                            mRecyclerView.scrollBy(dx, 0);
                        }
                    }
                });
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (null != mOnPageChangeListener) {
                            mOnPageChangeListener.onPageChange(getPageIndex());
                        }
                        if (pageChangedListener!=null){
                            if (currentPage!=oldPage){
                                pageChangedListener.onChanged(currentPage);
                            }
                        }
                        startY = offsetY;
                    }
                });
            } else {
                mAnimator.cancel();
                mAnimator.setIntValues(startPoint, endPoint);
            }

            mAnimator.start();

            return true;
        }
    }

    public class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            Log.d(TAG, "onScrollStateChanged: newState="+newState);
            if (newState == 0 && mOrientation != ORIENTATION.NULL) {
                boolean move;
                int vX = 0, vY = 0;
                if (mOrientation == ORIENTATION.VERTICAL) {
                    int absY = Math.abs(offsetY - startY);
                    move = absY > recyclerView.getHeight() / 2;
                    vY = 0;
                    if (move) {
                        vY = offsetY - startY < 0 ? -1000 : 1000;
                    }
                } else {
                    int absX = Math.abs(offsetX - startX);
                    move = absX > recyclerView.getWidth() / 2;
                    if (move) {
                        vX = offsetX - startX < 0 ? -1000 : 1000;
                    }

                }
                mOnFlingListener.onFling(vX, vY);
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            Log.e(TAG, "onScrolled: 滚动后 offsetY:"+offsetY);
            offsetY += dy;
            offsetX += dx;
        }
    }

    private MyOnTouchListener mOnTouchListener = new MyOnTouchListener();


    public class MyOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.e(TAG, "onTouch: 记录的offsetY,赋值给startY:"+offsetY );
                startY = offsetY;
                startX = offsetX;
            }
            return false;
        }

    }

    private int getPageIndex() {
        int p = 0;
        if (mOrientation == ORIENTATION.VERTICAL) {
            p = offsetY / mRecyclerView.getHeight();
        } else {
            p = offsetX / mRecyclerView.getWidth();
        }
        return p;
    }

    private int getStartPageIndex() {
        int p = 0;
        if (mOrientation == ORIENTATION.VERTICAL) {
            p = startY / mRecyclerView.getHeight();
        } else {
            p = startX / mRecyclerView.getWidth();
        }
        return p;
    }

    onPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(onPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    public interface onPageChangeListener {
        void onPageChange(int index);
    }

}
