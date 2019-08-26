package com.meishe.sdkdemo.edit.watermark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseFragment;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;
import static com.meishe.sdkdemo.edit.watermark.WaterMarkItemData.ASSETSPICTURE;
import static com.meishe.sdkdemo.edit.watermark.WaterMarkItemData.SDCARDPICTURE;

/**
 * Created by CaoZhiChao on 2018/10/16 10:37
 */
@SuppressLint("ValidFragment")
public class WaterMarkSelectFragment extends BaseFragment {
    private String TAG = "WaterMarkSelectFragment";
    Context context;
    int type = 0;
    RecyclerView recyclerWatermark;
    ArrayList<WaterMarkItemData> imageList = new ArrayList<>();
    OnItemClickListener onItemClickListener;
    RecyclerView.Adapter homeAdapter;
    ArrayList<String> cafFiles = new ArrayList();
    ArrayList<String> cafJPG = new ArrayList();
    List<WaterMarkItemData> cacheList = new ArrayList<>();
    private int nowPosition = -1;

    public int getNowPosition() {
        return nowPosition;
    }

    public void refreshNowPosition() {
        int position = nowPosition;
        nowPosition = -1;
        homeAdapter.notifyItemChanged(position);
    }

    public WaterMarkSelectFragment(Context context, OnItemClickListener listener) {
        this.context = context;
        onItemClickListener = listener;
    }


    @Override
    protected int initRootView() {
        return R.layout.fragment_watermari_select;
    }

    @Override
    protected void initArguments(Bundle arguments) {
        if (arguments != null) {
            type = arguments.getInt(WaterMarkConstant.WATERMARKTYPE_TYPE);
        }
    }

    @Override
    protected void initView() {
        recyclerWatermark = (RecyclerView) mRootView.findViewById(R.id.recycler_watermark);
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initListener() {
        String extraCafDir = PathUtils.getWatermarkCafDirectoryDir();
        if (extraCafDir != null) {
            File extraCafDirFiles = new File(extraCafDir);
            List<String> fileNames = Arrays.asList(extraCafDirFiles.list());
            if (!fileNames.isEmpty()) {
                for (String fileName : fileNames) {
                    if (FileUtil.getExtensionName(fileName).equals("caf")) {
                        cafFiles.add(fileName);
                    } else if (FileUtil.getExtensionName(fileName).equals("jpg") || FileUtil.getExtensionName(fileName).equals("png")) {
                        cafJPG.add(fileName);
                    }
                }
            }
        }
        if (type == WaterMarkConstant.WATERMARKTYPE_STATIC) {
            imageList.add(new WaterMarkItemData());
            imageList.add(new WaterMarkItemData(ASSETSPICTURE, type, "static_watermark.png", "static_watermark_item.png"));
            String cache = FileUtil.readWaterMarkCacheFile();
            if (cache != null) {
                try {
                    Gson gson = new Gson();
                    WaterMarkCacheData waterMarkCacheData = gson.fromJson(cache, WaterMarkCacheData.class);
                    cacheList = waterMarkCacheData.getPicturePathName();
                    imageList.addAll(cacheList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            imageList.add(new WaterMarkItemData(ASSETSPICTURE, type, "banner.caf", "dynamic_watermark.jpg"));
            if (!cafFiles.isEmpty() && !cafJPG.isEmpty()) {
                for (String cafFile : cafFiles) {
                    String cafFileName = FileUtil.getFileName(cafFile);
                    if (checkListContainString(cafJPG, cafFileName + ".jpg")) {
                        imageList.add(new WaterMarkItemData(SDCARDPICTURE, type, cafFile, cafFileName + ".jpg"));
                    } else if (checkListContainString(cafJPG, cafFileName + ".png")) {
                        imageList.add(new WaterMarkItemData(SDCARDPICTURE, type, cafFile, cafFileName + ".png"));
                    } else {
                        imageList.add(new WaterMarkItemData(ASSETSPICTURE, type, cafFile, "default_dynamic_picture.png"));
                    }
                }
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(HORIZONTAL);
        recyclerWatermark.addItemDecoration(new SpaceItemDecoration(0, ScreenUtils.dip2px(getActivity(), 29)));
        recyclerWatermark.setLayoutManager(linearLayoutManager);//必须要加的
        recyclerWatermark.setAdapter(homeAdapter = new HomeAdapter());
    }

    private boolean checkListContainString(List<String> list, String s) {
        for (String s1 : list) {
            if (s1.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public List<WaterMarkItemData> getCacheList() {
        if (cacheList == null) {
            return new ArrayList<>();
        }
        return cacheList;
    }

    /**
     * 从列表添加的都是静态水印类型
     */
    public void addDataAndRefresh(String picturePath) {
        if (!imageList.contains(new WaterMarkItemData(0, WaterMarkConstant.WATERMARKTYPE_STATIC, picturePath, picturePath))) {
            imageList.add(new WaterMarkItemData(SDCARDPICTURE, WaterMarkConstant.WATERMARKTYPE_STATIC, picturePath, picturePath));
            cacheList.add(new WaterMarkItemData(SDCARDPICTURE, WaterMarkConstant.WATERMARKTYPE_STATIC, picturePath, picturePath));
            homeAdapter.notifyDataSetChanged();
        }
    }

    //适配器
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//布局绑定
            return new MyViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.item_watermark_recycleview, parent,
                    false));
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {//view获取和赋值
            int drawableId = R.drawable.watermark_item_add;
            String able = getResources().getConfiguration().locale.getCountry();
            if (able.equals("CN")) {
                drawableId = R.drawable.watermark_item_add;
            }else if (able.equals("US")){
                drawableId = R.drawable.watermark_recycle_add_us;
            }
            if (position == 0 && type == WaterMarkConstant.WATERMARKTYPE_STATIC) {
                Glide.with(context).load(drawableId).into(holder.imageView);
            } else {
                glideLoadPicture(imageList.get(position), holder.imageView);
            }
            holder.itemWatermarkCover.setVisibility(nowPosition == position ? View.VISIBLE : View.INVISIBLE);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == WaterMarkConstant.WATERMARKTYPE_STATIC && position == 0) {
                        //do nothing
                    } else {
                        homeAdapter.notifyItemChanged(nowPosition);
                        homeAdapter.notifyItemChanged(position);
                        nowPosition = position;
                    }
                    String picturePath = imageList.get(position).getItemPicturePath();
                    String waterMarkPicturePath = imageList.get(position).getWaterMarkpath();
                    if (imageList.get(position).getItemPictureType() == ASSETSPICTURE) {
                        picturePath = "assets:/watermark/" + imageList.get(position).getItemPicturePath();
                        if (type == WaterMarkConstant.WATERMARKTYPE_STATIC) {
                            waterMarkPicturePath = "assets:/watermark/" + imageList.get(position).getWaterMarkpath();
                        }
                    }
                    onItemClickListener.onClick(holder, position, imageList.get(position).getItemPictureType(), picturePath, type, waterMarkPicturePath);
                }
            });
        }

        private void glideLoadPicture(WaterMarkItemData waterMarkItemData, ImageView view) {
            if (waterMarkItemData.getItemPictureType() == SDCARDPICTURE) {
                if (waterMarkItemData.getItemPicturePath().length() == 0) {
                    Glide.with(context).load(R.drawable.default_dynamic_picture).into(view);
                } else {
                    Glide.with(context).load(waterMarkItemData.getItemPicturePath()).into(view);
                }
            } else {
                Glide.with(context).load("file:///android_asset/watermark/" + waterMarkItemData.getItemPicturePath()).into(view);
            }
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageView itemWatermarkCover;

            MyViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.item_watermark_image);
                itemWatermarkCover = (ImageView) view.findViewById(R.id.item_watermark_cover);
            }
        }
    }
}
