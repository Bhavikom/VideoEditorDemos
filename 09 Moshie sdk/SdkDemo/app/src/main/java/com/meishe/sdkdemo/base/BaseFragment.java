package com.meishe.sdkdemo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ${gexinyu} on 2018/5/24.
 */

public abstract class  BaseFragment<T> extends Fragment {
    /**
     * 贴附的activity
     */
    protected FragmentActivity mActivity;

    /**
     * 根view
     */
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;
    public T listener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        listener = (T)mActivity;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(initRootView(), container, false);

        initArguments(getArguments());

        initView();

        mIsPrepare = true;

        onLazyLoad();

        initListener();

        return mRootView;
    }





    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        this.mIsVisible = isVisibleToUser;

        if (isVisibleToUser) {
            onVisibleToUser();
        }
    }

    /**
     * 用户可见时执行的操作
     *
     * @author 漆可
     * @date 2016-5-26 下午4:09:39
     */
    protected void onVisibleToUser() {
        if (mIsPrepare && mIsVisible) {
            onLazyLoad();
        }
    }



    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int id) {
        if (mRootView == null) {
            return null;
        }

        return (T) mRootView.findViewById(id);
    }

    /**
     * 设置根布局资源id
     *
     * @return
     * @author 漆可
     * @date 2016-5-26 下午3:57:09
     */
    protected abstract int initRootView();


    /**
     * 初始化数据
     *
     * @param arguments 接收到的从其他地方传递过来的参数
     * @author 漆可
     * @date 2016-5-26 下午3:57:48
     */
    protected abstract void initArguments(Bundle arguments);


    /**
     * 初始化View
     *
     * @author 漆可
     * @date 2016-5-26 下午3:58:49
     */
    protected abstract void initView();


    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     *
     * @author 漆可
     * @date 2016-5-26 下午4:10:20
     */
    protected abstract void onLazyLoad();


    /**
     * 设置监听事件
     *
     * @author 漆可
     * @date 2016-5-26 下午3:59:36
     */
    protected abstract void initListener();

}