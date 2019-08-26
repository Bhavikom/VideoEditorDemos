package com.meishe.sdkdemo.utils;

import android.content.Context;
import android.text.TextUtils;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.data.FilterItem;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;
import com.meishe.sdkdemo.utils.dataInfo.VideoClipFxInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/11/15.
 */

public class AssetFxUtil {
    //获取人脸道具数据
    public static ArrayList<FilterItem> getFaceUDataList(ArrayList<NvAsset> faceULocalDataList, ArrayList<NvAsset> faceUBundleDataList) {
        ArrayList<FilterItem> faceUPropDataList = new ArrayList<>();
        FilterItem filterItem = new FilterItem();
        //filterItem.setFilterName("");
        filterItem.setPackageId("");
        filterItem.setImageId(R.mipmap.no);
        faceUPropDataList.add(filterItem);

        if (faceULocalDataList != null) {
            for (NvAsset asset : faceULocalDataList) {
                FilterItem newFilterItem = new FilterItem();
                if (asset.isReserved()) {
                    String coverPath = "file:///android_asset/arface/";
                    coverPath += asset.uuid;
                    coverPath += ".png";
                    asset.coverUrl = coverPath;//加载assets/arface文件夹下的图片
                    newFilterItem.setFilterName(asset.bundledLocalDirPath);
                }else {
                    newFilterItem.setFilterName(asset.localDirPath);
                }
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_PACKAGE);
                newFilterItem.setPackageId(asset.uuid);
                newFilterItem.setImageUrl(asset.coverUrl);
                newFilterItem.setCategoryId(asset.categoryId);
                faceUPropDataList.add(newFilterItem);
            }
        }

        if (faceUBundleDataList != null) {
            for (NvAsset asset : faceUBundleDataList) {
                FilterItem newFilterItem = new FilterItem();
                if (asset.isReserved()) {
                    String coverPath = "file:///android_asset/arface/";
                    coverPath += asset.uuid;
                    coverPath += ".png";
                    asset.coverUrl = coverPath;//加载assets/arface文件夹下的图片
                }
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUNDLE);
                newFilterItem.setFilterName(asset.bundledLocalDirPath);
                newFilterItem.setPackageId(asset.uuid);
                newFilterItem.setImageUrl(asset.coverUrl);
                newFilterItem.setCategoryId(asset.categoryId);
                faceUPropDataList.add(newFilterItem);
            }
        }

        return faceUPropDataList;
    }

    //获取人脸道具数据选中位置
    public static int getSelectedFaceUPropPos(ArrayList<FilterItem> faceUDataArrayList, String curFaceArSceneId) {
        if (faceUDataArrayList == null || faceUDataArrayList.size() == 0)
            return -1;

        if (TextUtils.isEmpty(curFaceArSceneId))
            return 0;

        for (int i = 0; i < faceUDataArrayList.size(); i++) {
            FilterItem newFilterItem = faceUDataArrayList.get(i);
            if (newFilterItem == null)
                continue;
            String faceArSceneId = newFilterItem.getPackageId();
            if (curFaceArSceneId.equals(faceArSceneId)) {
                return i;
            }
        }

        return 0;
    }


    // 获取滤镜数据
    public static ArrayList<FilterItem> getFilterData(Context context,
                                                      ArrayList<NvAsset> filterAssetList,
                                                      List<String> builtinVideoFxList,
                                                      boolean isAddCartoon,
                                                      boolean isFitRatio) {
        ArrayList<FilterItem> filterList = new ArrayList<>();
        FilterItem filterItem = new FilterItem();
        filterItem.setFilterName("无");
        filterItem.setImageId(R.mipmap.no);
        filterList.add(filterItem);

        if (isAddCartoon) {
            // 新增漫画特效
            FilterItem cartoon1 = new FilterItem();
            cartoon1.setIsCartoon(true);
            cartoon1.setFilterName("水墨");
            cartoon1.setImageId(R.mipmap.sage);
            cartoon1.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
            cartoon1.setStrokenOnly(true);
            cartoon1.setGrayScale(true);
            filterList.add(cartoon1);

            FilterItem cartoon2 = new FilterItem();
            cartoon2.setIsCartoon(true);
            cartoon2.setFilterName("漫画书");
            cartoon2.setImageId(R.mipmap.maid);
            cartoon2.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
            cartoon2.setStrokenOnly(false);
            cartoon2.setGrayScale(false);
            filterList.add(cartoon2);

            FilterItem cartoon3 = new FilterItem();
            cartoon3.setIsCartoon(true);
            cartoon3.setFilterName("单色");
            cartoon3.setImageId(R.mipmap.mace);
            cartoon3.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
            cartoon3.setStrokenOnly(false);
            cartoon3.setGrayScale(true);

            filterList.add(cartoon3);
        }

        String bundlePath = "filter/info.txt";
        Util.getBundleFilterInfo(context, filterAssetList, bundlePath);

        int ratio = TimelineData.instance().getMakeRatio();
        for (NvAsset asset : filterAssetList) {
            if (isFitRatio && (ratio & asset.aspectRatio) == 0)
                continue;

            FilterItem newFilterItem = new FilterItem();
            if (asset.isReserved()) {
                String coverPath = "file:///android_asset/filter/";
                coverPath += asset.uuid;
                coverPath += ".png";
                asset.coverUrl = coverPath;//加载assets/filter文件夹下的图片
            }
            newFilterItem.setFilterMode(FilterItem.FILTERMODE_PACKAGE);
            newFilterItem.setFilterName(asset.name);
            newFilterItem.setPackageId(asset.uuid);
            newFilterItem.setImageUrl(asset.coverUrl);
            filterList.add(newFilterItem);
        }

        //暂时先注掉内建滤镜特效
        int[] resImags = {
                R.mipmap.sage,
                R.mipmap.maid,
                R.mipmap.mace,
                R.mipmap.lace,
                R.mipmap.mall,
                R.mipmap.sap,
                R.mipmap.sara,
                R.mipmap.pinky,
                R.mipmap.sweet,
                R.mipmap.fresh
        };
        if (builtinVideoFxList != null) {
            for (int i = 0; i < builtinVideoFxList.size(); i++) {
                String transitionName = builtinVideoFxList.get(i);
                FilterItem newFilterItem = new FilterItem();
                newFilterItem.setFilterName(transitionName);
                if (i < resImags.length) {
                    newFilterItem.setImageId(resImags[i]);
                }
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
                filterList.add(newFilterItem);
            }
        }

        return filterList;
    }


    //获取滤镜当前选择位置
    public static int getSelectedFilterPos(ArrayList<FilterItem> filterDataArrayList, VideoClipFxInfo videoClipFxInfo) {
        if (filterDataArrayList == null || filterDataArrayList.size() == 0)
            return -1;

        String fxName = videoClipFxInfo.getFxId();
        if (TextUtils.isEmpty(fxName))
            return 0;

        for (int i = 0; i < filterDataArrayList.size(); i++) {
            FilterItem newFilterItem = filterDataArrayList.get(i);
            if (newFilterItem == null)
                continue;

            int filterMode = newFilterItem.getFilterMode();
            String filterName;
            if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                filterName = newFilterItem.getFilterName();
            } else {
                filterName = newFilterItem.getPackageId();
            }

            if (fxName.equals(filterName)) {
                return i;
            }
        }

        return 0;
    }

}
