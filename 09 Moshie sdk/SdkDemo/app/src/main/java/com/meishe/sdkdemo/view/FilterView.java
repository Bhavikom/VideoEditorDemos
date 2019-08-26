package com.meishe.sdkdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.data.FilterItem;
import com.meishe.sdkdemo.edit.filter.FilterAdapter;

import java.util.ArrayList;

/**
 * Created by admin on 2018/11/15.
 */

public class FilterView extends RelativeLayout {
    private SeekBar mIntensitySeekBar;
    private TextView mIntensityText;
    private LinearLayout mIntensityLayout;
    private LinearLayout mFilterFxList;
    private RecyclerView mFilterRecyclerList;
    private LinearLayout mMoreFilterButton;
    private ImageButton mMoreFilerImage;
    private TextView mMoreFilerText;
    private FilterAdapter mFilterAdapter;

    private OnFilterListener mFilterListener;
    private OnSeekBarTouchListener mSeekBarTouchListener;
    private OnRecyclerViewScrollListener mRecyclerScrollListener;

    public interface OnFilterListener {
        void onItmeClick(View v, int position);

        void onMoreFilter();

        void onIntensity(int value);
    }

    public interface OnSeekBarTouchListener {
        void onStartTrackingTouch();

        void onStopTrackingTouch();
    }

    public interface OnRecyclerViewScrollListener {
        void onRecyclerViewScroll(RecyclerView recyclerView, int dx, int dy);
    }

    public void setSeekBarTouchListener(OnSeekBarTouchListener seekBarTouchListener) {
        mSeekBarTouchListener = seekBarTouchListener;
    }

    public void setRecyclerScrollListener(OnRecyclerViewScrollListener recyclerScrollListener) {
        mRecyclerScrollListener = recyclerScrollListener;
    }

    public void setFilterFxListBackgroud(String strColor) {
        mFilterFxList.setBackgroundColor(Color.parseColor(strColor));
    }

    public void setIntensityTextVisible(int visible) {
        mIntensityText.setVisibility(visible);
    }

    public void setIntensityLayoutVisible(int visible) {
        mIntensityLayout.setVisibility(visible);
    }

    public void setFilterListener(OnFilterListener faceUPropListener) {
        this.mFilterListener = faceUPropListener;
    }

    public void setMoreFilterClickable(boolean clickable) {
        mMoreFilterButton.setClickable(clickable);
    }

    public void setIntensitySeekBarProgress(int progress) {
        mIntensitySeekBar.setProgress(progress);
    }

    public void setIntensitySeekBarMaxValue(int maxValue) {
        mIntensitySeekBar.setMax(maxValue);
    }

    public FilterView(Context context) {
        this(context, null);
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setSelectedPos(int selectedPos) {
        if (mFilterAdapter != null)
            mFilterAdapter.setSelectPos(selectedPos);
    }

    public void setFilterArrayList(ArrayList<FilterItem> filterDataList) {
        if (mFilterAdapter != null)
            mFilterAdapter.setFilterDataList(filterDataList);
    }

    public void notifyDataSetChanged() {
        if (mFilterAdapter != null) {
            mFilterAdapter.notifyDataSetChanged();
        }
    }

    public void initFilterRecyclerView(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mFilterRecyclerList.setLayoutManager(linearLayoutManager);
        mFilterRecyclerList.setAdapter(mFilterAdapter);

        mFilterAdapter.setOnItemClickListener(new FilterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mFilterListener != null) {
                    mFilterListener.onItmeClick(view, position);
                }
            }
        });

        mFilterRecyclerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mRecyclerScrollListener != null) {
                    mRecyclerScrollListener.onRecyclerViewScroll(recyclerView, dx, dy);
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        mFilterAdapter = new FilterAdapter(context);
        View rootView = LayoutInflater.from(context).inflate(R.layout.filter_list_view, this);
        mIntensityLayout = (LinearLayout) rootView.findViewById(R.id.intensityLayout);
        mIntensityText = (TextView) rootView.findViewById(R.id.intensityText);
        mIntensitySeekBar = (SeekBar) rootView.findViewById(R.id.intensitySeekBar);
        mIntensityLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect seekRect = new Rect();
                mIntensitySeekBar.getHitRect(seekRect);
                if ((event.getY() >= (seekRect.top - 10)) && (event.getY() <= (seekRect.bottom + 10))) {
                    float y = seekRect.top + seekRect.height() / 2;
                    //seekBar only accept relative x
                    float x = event.getX() - seekRect.left;
                    if (x < 0) {
                        x = 0;
                    } else if (x > seekRect.width()) {
                        x = seekRect.width();
                    }
                    MotionEvent me = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), x, y, event.getMetaState());
                    return mIntensitySeekBar.onTouchEvent(me);
                }
                return false;
            }
        });
        mIntensitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mFilterListener != null) {
                        mFilterListener.onIntensity(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mSeekBarTouchListener != null) {
                    mSeekBarTouchListener.onStartTrackingTouch();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mSeekBarTouchListener != null) {
                    mSeekBarTouchListener.onStopTrackingTouch();
                }
            }
        });
        mFilterFxList = (LinearLayout) rootView.findViewById(R.id.filterFxList);
        mFilterRecyclerList = (RecyclerView) rootView.findViewById(R.id.filterRecyclerList);
        mMoreFilerImage = (ImageButton) rootView.findViewById(R.id.moreFilerImage);
        mMoreFilerImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreFilterButton.callOnClick();
            }
        });
        mMoreFilerText = (TextView) rootView.findViewById(R.id.moreFilerText);
        mMoreFilerText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreFilterButton.callOnClick();
            }
        });
        mMoreFilterButton = (LinearLayout) rootView.findViewById(R.id.moreFilterButton);
        mMoreFilterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilterListener != null) {
                    mFilterListener.onMoreFilter();
                }
            }
        });
    }
}
