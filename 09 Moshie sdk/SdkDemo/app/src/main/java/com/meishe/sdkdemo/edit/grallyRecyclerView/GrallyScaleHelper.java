package com.meishe.sdkdemo.edit.grallyRecyclerView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meishe.sdkdemo.MSApplication;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseConstants;
import com.meishe.sdkdemo.utils.ScreenUtils;

/**
 * Created by jameson on 8/30/16.
 */
public class GrallyScaleHelper {
    private final static String TAG = "GrallyScaleHelper";
    private int scrollState = 0;
    private LinearLayoutManager layoutManager;
    private int mCurrentItemOffset = 0;
    private int mOnePageWidth = 0;
    private GrallyAdapter cardAdapter;
    private RecyclerView mRecyclerView;
    private int mCurrentItemPos = 0;//计算的位置
    private LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
    //最多和最少的子控件个数
    private int minChild;
    private int maxChild;

    private OnGrallyItemSelectListener m_onItemSelectListener = null;

    public interface OnGrallyItemSelectListener {
        void onItemSelect(int pos);
    }

    public void setOnItemSelectedListener(OnGrallyItemSelectListener m_onItemSelectListener) {
        this.m_onItemSelectListener = m_onItemSelectListener;
    }

    public void attachToRecyclerView(final RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        layoutManager = (LinearLayoutManager) this.mRecyclerView.getLayoutManager();
        cardAdapter = (GrallyAdapter) this.mRecyclerView.getAdapter();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if(position < 0)
                        return;
                    Log.e(TAG,"position = " +  position );
                    int childCount = layoutManager.getChildCount();
                    if (position == 0){
                        setViewVisibleOrGone(position, View.VISIBLE);
                        setViewVisibleOrGone(position + 1, View.GONE);
                    }else if(position == childCount - 1){
                        setViewVisibleOrGone(position, View.VISIBLE);
                        setViewVisibleOrGone(position -1, View.GONE);
                    }else {
                        setViewVisibleOrGone(position - 1, View.GONE);
                        setViewVisibleOrGone(position, View.VISIBLE);
                        setViewVisibleOrGone(position + 1, View.GONE);
                    }
                    if(m_onItemSelectListener != null){
                        m_onItemSelectListener.onItemSelect(position);
                    }
                    cardAdapter.setSelectPos(position);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mOnePageWidth == 0) {
                    View view = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition());
                    mOnePageWidth = view.getWidth();
                    minChild = layoutManager.getChildCount();
                }
                int a = layoutManager.getChildCount();
                maxChild = a > maxChild ? a : maxChild;
                minChild = a < minChild ? a : minChild;
                // dx>0则表示右滑, dx<0表示左滑, dy<0表示上滑, dy>0表示下滑
                if (dx != 0) {//去掉奇怪的内存疯涨问题
                    mCurrentItemOffset += dx;
                    computeCurrentItemPos();
                    onScrolledChangedCallback();
                }
                if (dx > 0) {
                    //向后滑动了
                    scrollState = 1;
                } else {
                    scrollState = 2;
                    //向前滑动了
                }
            }
        });
        mLinearSnapHelper.attachToRecyclerView(recyclerView);
    }

    public void resetCurrentOffset(int currentPos) {
        mCurrentItemOffset = currentPos * mOnePageWidth;
    }

    public void setViewVisibleOrGone(int position, int visible) {
        Log.e("setViewVisibleOrGone",position+"");
        View view = layoutManager.findViewByPosition(position);
        if (view != null) {
            view.findViewById(R.id.line_left).setVisibility(visible);
            view.findViewById(R.id.line_right).setVisibility(visible);
            view.findViewById(R.id.addImage_left).setVisibility(visible);
            view.findViewById(R.id.addImage_right).setVisibility(visible);
            if (visible == View.VISIBLE) {
                setViewAnimas(view, 1F);
            } else {
                view.setScaleY(BaseConstants.EDITGRALLYSCALE);
            }
        }
    }

    /**
     * 计算mCurrentItemOffset
     */
    private void computeCurrentItemPos() {
        if (mOnePageWidth <= 0) return;
        boolean pageChanged = false;
        // 滑动超过一页说明已翻页
        if (Math.abs(mCurrentItemOffset - mCurrentItemPos * mOnePageWidth) >= mOnePageWidth) {
            pageChanged = true;
        }
        if (pageChanged) {
            mCurrentItemPos = mCurrentItemOffset / mOnePageWidth;
        }

    }

    /**
     * RecyclerView位移事件监听, view大小随位移事件变化
     */
    private void onScrolledChangedCallback() {
        int offset = mCurrentItemOffset - mCurrentItemPos * mOnePageWidth;
        float percent = (float) Math.max(Math.abs(offset) * 1.0 / mOnePageWidth, 0.0001);
        if (percent > 1) {
            percent = 1;
        } else if (percent < 0.0001) {
            percent = 0;
        }
        View leftView = null;

        View currentView;
        View rightView = null;
        if (mCurrentItemPos > 0) {
            leftView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos - 1);
        }
        currentView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos);
        if (mCurrentItemPos < mRecyclerView.getAdapter().getItemCount() - 1) {
            rightView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos + 1);
        }

        if (scrollState == 1) {//向后滑动了
            leftView = null;
        } else if (scrollState == 2) {//前
            rightView = null;
        }
        float degree =  (1 - BaseConstants.EDITGRALLYSCALE) * percent + BaseConstants.EDITGRALLYSCALE;
        Log.e("1234","设置缩放： "+degree);
        if (leftView != null) {
            leftView.setScaleY((1 - BaseConstants.EDITGRALLYSCALE) * percent + BaseConstants.EDITGRALLYSCALE);
        }
        if (currentView != null) {
            float currentScaleY = (BaseConstants.EDITGRALLYSCALE - 1) * percent + 1;
            currentView.setScaleY(currentScaleY);
            float y = 1 - percent * 5;
            if (y > 1) {
                y = 1;
            } else if (y < 0.0001) {
                y = 0;
            }
            setViewAnimas(currentView, y);
        }
        if (rightView != null) {
            rightView.setScaleY((1 - BaseConstants.EDITGRALLYSCALE) * percent + BaseConstants.EDITGRALLYSCALE);
        }
    }


    private void setViewAnimas(View view, float percent) {
        if (view != null) {
            Log.e("11111111","setViewAnimas "+percent);
            view.findViewById(R.id.line_left).setScaleX(percent);
            view.findViewById(R.id.line_right).setScaleX(percent);

            view.findViewById(R.id.addImage_left).setScaleX(percent);
            view.findViewById(R.id.addImage_right).setScaleX(percent);
            view.findViewById(R.id.addImage_left).setScaleY(percent);
            view.findViewById(R.id.addImage_right).setScaleY(percent);

            view.findViewById(R.id.line_left).setAlpha(percent);
            view.findViewById(R.id.line_right).setAlpha(percent);
            view.findViewById(R.id.addImage_left).setAlpha(percent);
            view.findViewById(R.id.addImage_right).setAlpha(percent);
        }
    }

    public void onBindViewHolder(View itemView, final int position, int itemCount, ImageView imageView) {
        float mPagePadding = (ScreenUtils.getScreenWidth(MSApplication.getmContext()) - ScreenUtils.dip2px(MSApplication.getmContext(), 212)) / 2;
        int padding = (int) mPagePadding;
        int leftMarin = position == 0 ? padding : 0;
        int rightMarin = position == itemCount - 1 ? padding : 0;
        setViewMargin(itemView, leftMarin, 0, rightMarin, 0);
    }

    private void setViewMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            view.setLayoutParams(lp);
        }
    }

    public int getmOnePageWidth() {
        return mOnePageWidth;
    }

}
