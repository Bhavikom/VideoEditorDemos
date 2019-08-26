package com.meishe.sdkdemo.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by CaoZhiChao on 2018/10/16 10:37
 *
 * @author meishe
 */
@SuppressLint("ValidFragment")
public class MainViewPagerFragment extends BaseFragment<com.meishe.sdkdemo.main.OnItemClickListener> {
    private String TAG = "MainViewPagerFragment";
    RecyclerView main_fragment_recycle;
    ArrayList<MainViewPagerFragmentData> fragmentDataList;
    private int spanCount;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public MainViewPagerFragment() {
    }


    @Override
    protected int initRootView() {
        return R.layout.fragment_main_viewpager_item1;
    }

    @Override
    protected void initArguments(Bundle arguments) {
        if (arguments != null) {
            fragmentDataList = arguments.getParcelableArrayList("list");
            spanCount = arguments.getInt("span", 4);
        }
    }

    @Override
    protected void initView() {
        main_fragment_recycle = (RecyclerView) mRootView.findViewById(R.id.main_fragment_recycle);
        MainViewPagerFragmentAdapter adapter = new MainViewPagerFragmentAdapter(getContext(), fragmentDataList, listener);
        main_fragment_recycle.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        main_fragment_recycle.setLayoutManager(layoutManager);
        main_fragment_recycle.addItemDecoration(new GridSpacingItemDecoration(getContext(), spanCount, (int) getResources().getDimension(R.dimen.dp77)));
    }

    @Override
    protected void onLazyLoad() {
    }

    @Override
    protected void initListener() {
    }
}
