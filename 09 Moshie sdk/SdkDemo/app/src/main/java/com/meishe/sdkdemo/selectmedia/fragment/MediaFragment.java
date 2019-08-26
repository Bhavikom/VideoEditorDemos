package com.meishe.sdkdemo.selectmedia.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseFragment;
import com.meishe.sdkdemo.selectmedia.SelectMediaActivity;
import com.meishe.sdkdemo.selectmedia.adapter.AgendaSimpleSectionAdapter;
import com.meishe.sdkdemo.selectmedia.adapter.GridSpacingItemDecoration;
import com.meishe.sdkdemo.selectmedia.adapter.SectionedSpanSizeLookup;
import com.meishe.sdkdemo.selectmedia.bean.MediaData;
import com.meishe.sdkdemo.selectmedia.interfaces.OnTotalNumChange;
import com.meishe.sdkdemo.selectmedia.interfaces.OnTotalNumChangeForActivity;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.MediaConstant;
import com.meishe.sdkdemo.utils.MediaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meishe.sdkdemo.utils.MediaConstant.KEY_CLICK_TYPE;
import static com.meishe.sdkdemo.utils.MediaConstant.LIMIT_COUNT;

/**
 * Created by CaoZhiChao on 2018/6/5 10:59
 */
@SuppressLint("ValidFragment")
public class MediaFragment extends BaseFragment implements OnTotalNumChange {
    private final String TAG = getClass().getName();
    RecyclerView mediaRecycler;
    GridLayoutManager layoutManager;
    List<List<MediaData>> lists = new ArrayList<>();
    List<MediaData> listOfOut = new ArrayList<>();
    private AgendaSimpleSectionAdapter adapter;

    public int getIndex() {
        return index;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    private int totalSize;
    private int index;
    private int mLimitMediaCount = -1;//-1值表示无限选择视频或者图片数量，若是大于0某一具体值，则表示选中这一数值的视频或者图片
    private OnTotalNumChangeForActivity mOnTotalNumChangeForActivity;
    private int clickType;
    public static final int GRIDITEMCOUNT = 4;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTotalNumChangeForActivity){
            mOnTotalNumChangeForActivity = (OnTotalNumChangeForActivity) context;
        }
    }

    public MediaFragment() {
    }

    @Override
    protected int initRootView() {
        return R.layout.fragment_media;
    }

    @Override
    protected void initArguments(Bundle arguments) {
        if (arguments != null) {
            index = arguments.getInt(MediaConstant.MEDIA_TYPE);
            mLimitMediaCount = arguments.getInt(LIMIT_COUNT, -1);
            clickType = arguments.getInt(KEY_CLICK_TYPE, 0);
        }
    }

    @Override
    protected void initView() {
        mediaRecycler = (RecyclerView) mRootView.findViewById(R.id.media_recycleView);
        showLocalMediaByMediaType();
    }

    @Override
    protected void onLazyLoad() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
    }

    /**
     * 根据当前的media的类型显示不同的media
     */
    private void showLocalMediaByMediaType() {
        //要判断是否有权限，无权限的时候不能去读写SD卡
        MediaUtils.getMediasByType(mActivity, index, new MediaUtils.LocalMediaCallback() {
            @Override
            public void onLocalMediaCallback(final List<MediaData> allMediaTemp) {
                MediaUtils.ListOfAllMedia listOfAllMedia=MediaUtils.groupListByTime(allMediaTemp);
                lists=listOfAllMedia.getListOfAll();
                listOfOut=listOfAllMedia.getListOfParent();
                adapter = new AgendaSimpleSectionAdapter(lists, listOfOut, mediaRecycler, MediaFragment.this, index, mActivity,clickType,mLimitMediaCount,MediaFragment.this);
                mediaRecycler.setAdapter(adapter);
                layoutManager = new GridLayoutManager(getContext(), GRIDITEMCOUNT);
                SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
                layoutManager.setSpanSizeLookup(lookup);
                mediaRecycler.setLayoutManager(layoutManager);
                mediaRecycler.addItemDecoration(new GridSpacingItemDecoration(getContext(),GRIDITEMCOUNT));
            }
        });
    }

    @Override
    public void onTotalNumChange(@NonNull List selectList, Object TAG) {
        int total = selectList.size();
        Logger.e("onTotalNumChange", "当前碎片的标签：   " + TAG + "     总数据：    " + total);
        if (mOnTotalNumChangeForActivity != null) {
            mOnTotalNumChangeForActivity.onTotalNumChangeForActivity(selectList, TAG);
        }
    }

    public AgendaSimpleSectionAdapter getAdapter() {
        return adapter;
    }

    public void refreshSelect(List<MediaData> listOfOther, int from) {
        if (index != 0) {
            if (mActivity instanceof SelectMediaActivity){
                SelectMediaActivity activity = (SelectMediaActivity) mActivity;
                listOfOther = activity.getMediaDataList();
            }
        }
        if (isAdded() && adapter != null && adapter.getSelectList() != null) {
            List<MediaData> needRefreshList = getNeedRefreshList(getSameTypeData(adapter.getSelectList(), from), getSameTypeData(listOfOther, index), from);
            Logger.e("2222", "不同个数：    " + needRefreshList.size());
            for (int i = 0; i < needRefreshList.size(); i++) {
                if (needRefreshList.get(i).getType() == index || index == 0) {
                    Logger.e("2222", "更新数据：    " + needRefreshList.get(i).getPath());
                    Point point = adapter.getPointByData(lists, needRefreshList.get(i));
                    adapter.itemClick(mediaRecycler.getChildAt(adapter.getPositionByData(lists, needRefreshList.get(i))), point.x, point.y, true);
                }
            }
        }
    }

    private List<MediaData> getSameTypeData(List<MediaData> list, int index) {
        List<MediaData> newList = new ArrayList<>();
        for (MediaData mediaData : list) {
            if (mediaData.getType() == index || index == 0) {
                newList.add(mediaData);
            }
        }
        return newList;
    }

    /**
     * 获取两个List的不同元素
     *
     * @param adapterList
     * @param listFormList
     * @return
     */
    private List<MediaData> getNeedRefreshList(List<MediaData> adapterList, List<MediaData> listFormList, int from) {
        List<MediaData> diff = new ArrayList<>();
        //如果数据都和全部内容无关，暂时不做处理
        if (from != 0 && index != 0) {
            return diff;
        }
        Map<String, Integer> map = new HashMap<>(adapterList.size());
        for (MediaData mediaData : adapterList) {
            map.put(mediaData.getPath(), 1);
        }
        for (MediaData string : listFormList) {
            if (map.get(string.getPath()) != null) {
                map.put(string.getPath(), 2);
                if (string.isState() != getStateByPathInList(adapterList, string.getPath())) {
                    diff.add(string);
                }
                continue;
            }
            diff.add(string);
        }
        if (adapterList.size() > listFormList.size()) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 1 && !diff.contains(getDataByPath(entry.getKey()))) {
                    diff.add(getDataByPath(entry.getKey()));
                }
            }
        }
        return diff;
    }

    private boolean getStateByPathInList(List<MediaData> list, String path) {
        for (MediaData mediaData : list) {
            if (mediaData.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    private MediaData getDataByPath(String path) {
        return adapter.getDataByPath(lists, path);
    }

}
