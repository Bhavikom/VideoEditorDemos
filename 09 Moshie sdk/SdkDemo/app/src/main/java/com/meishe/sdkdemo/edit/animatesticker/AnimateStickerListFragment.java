package com.meishe.sdkdemo.edit.animatesticker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;

import java.util.ArrayList;

public class AnimateStickerListFragment extends Fragment {
    private final String TAG = "ASListFragment";
    private RecyclerView mAssetRecycleView;
    private StickerRecycleViewAdaper mStickerRecycleAdapter;
    private LinearLayout mCustomStickerAddButton;
    private ArrayList<NvAsset> mAssetInfolist = new ArrayList<>();
    private AnimateStickerClickerListener mAnimateStickerClickerListener;

    public interface AnimateStickerClickerListener{
        void onFragmentLoadFinish();
        void onItemClick(View view, int pos);
        void onAddCustomSticker();
    }

    public void setAnimateStickerClickerListener(AnimateStickerClickerListener animateStickerClickerListener) {
        this.mAnimateStickerClickerListener = animateStickerClickerListener;
    }

    public void setAssetInfolist(ArrayList<NvAsset> assetInfolist) {
        mAssetInfolist = assetInfolist;
        if(mStickerRecycleAdapter != null)
            mStickerRecycleAdapter.setAssetList(assetInfolist);
    }

    public void setIsCutomStickerAsset(boolean isCutomStickerAsset) {
        if(mStickerRecycleAdapter != null)
            mStickerRecycleAdapter.setIsCutomStickerAsset(isCutomStickerAsset);
    }

    public void setCustomStickerAssetInfolist(ArrayList<NvAssetManager.NvCustomStickerInfo> assetInfolist) {
        if(mStickerRecycleAdapter != null)
            mStickerRecycleAdapter.setCustomStickerAssetList(assetInfolist);
    }

    public void setSelectedPos(int selectedPos) {
        if(mStickerRecycleAdapter != null)
            mStickerRecycleAdapter.setSelectedPos(selectedPos);
    }
    public void setIsStickerInPlay(boolean isStickerInPlay) {
        if(mStickerRecycleAdapter != null)
            mStickerRecycleAdapter.setIsStickerInPlay(isStickerInPlay);
    }
    public void notifyItemChanged(int pos){
        if(mStickerRecycleAdapter != null)
            mStickerRecycleAdapter.notifyItemChanged(pos);
    }
    public void notifyDataSetChanged(){
        if(mStickerRecycleAdapter != null)
            mStickerRecycleAdapter.notifyDataSetChanged();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootParent = inflater.inflate(R.layout.animatesticker_asset_list_fragment, container, false);
        mCustomStickerAddButton = (LinearLayout) rootParent.findViewById(R.id.customStickerAddButton);
        mAssetRecycleView = (RecyclerView) rootParent.findViewById(R.id.assetRecycleView);
        return rootParent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAssetRecycleAdapter();
        mCustomStickerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnimateStickerClickerListener != null){
                    mAnimateStickerClickerListener.onAddCustomSticker();
                }
            }
        });

        if(mAnimateStickerClickerListener != null){
            mAnimateStickerClickerListener.onFragmentLoadFinish();
        }
    }

    @Override
    public void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
    }

    public void setCustomStickerButtonVisible(int visible){
        if(mCustomStickerAddButton != null)
            mCustomStickerAddButton.setVisibility(visible);
    }
    private void initAssetRecycleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mAssetRecycleView.setLayoutManager(layoutManager);
        mStickerRecycleAdapter = new StickerRecycleViewAdaper(getActivity());
        mStickerRecycleAdapter.setAssetList(mAssetInfolist);
        mAssetRecycleView.setAdapter(mStickerRecycleAdapter);
        mAssetRecycleView.addItemDecoration(new SpaceItemDecoration(26, 14));
        mStickerRecycleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if(mAnimateStickerClickerListener != null){
                    mAnimateStickerClickerListener.onItemClick(view,pos);
                }
            }
        });
    }
}
