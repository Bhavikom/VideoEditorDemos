package com.meishe.sdkdemo.edit.Caption;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.data.AssetItem;
import java.util.ArrayList;

public class CaptionFontFragment extends Fragment {
    private RecyclerView mCaptionFontRecycleView;
    private CaptionFontRecyclerAdaper mCaptionFontRecycleAdapter;
    private Button mBoldButton;
    private Button mItalicButton;
    private Button mShadowButton;
    private LinearLayout mApplyToAll;
    private ImageView mApplyToAllImage;
    private TextView mApplyToAllText;
    private boolean mIsBold = false;
    private boolean mIsItalic = false;
    private boolean mIsShadow = false;
    private boolean mIsApplyToAll = false;
    private ArrayList<AssetItem> mCaptionFontInfolist = new ArrayList<>();
    //private RelativeLayout mDownloadMoreCapionSytle;
    private OnCaptionFontListener mCaptionFontListener;

    public interface OnCaptionFontListener{
        void onFragmentLoadFinished();
        void onItemClick(int pos);
        void onBold();
        void onItalic();
        void onShadow();
        void onIsApplyToAll(boolean isApplyToAll);
        void onFontDownload(int pos);
    }
    public void setCaptionFontListener(OnCaptionFontListener captionFontListener) {
        this.mCaptionFontListener = captionFontListener;
    }

    public void setSelectedPos(int selectedPos){
        if (mCaptionFontRecycleAdapter != null)
            mCaptionFontRecycleAdapter.setSelectedPos(selectedPos);
    }
    public void setFontInfolist(ArrayList<AssetItem> fontInfolist) {
        mCaptionFontInfolist = fontInfolist;
        if (mCaptionFontRecycleAdapter != null)
            mCaptionFontRecycleAdapter.setAssetInfoList(fontInfolist);
    }

    public void applyToAllCaption(boolean isApplyToAll){
        mApplyToAllImage.setImageResource(isApplyToAll ? R.mipmap.applytoall : R.mipmap.unapplytoall);
        mApplyToAllText.setTextColor(isApplyToAll ? Color.parseColor("#ff4a90e2") : Color.parseColor("#ff909293"));
        mIsApplyToAll = isApplyToAll;
    }
    public void updateBoldButton(boolean isBold){
        mBoldButton.setBackgroundResource(isBold ? R.drawable.shape_caption_font_corner_button_selected : R.drawable.shape_caption_font_corner_button);
        mBoldButton.setTextColor(isBold ? Color.parseColor("#ff4a90e2") : Color.parseColor("#ffffffff"));
        mIsBold = isBold;
    }
    public void updateItalicButton(boolean isItalic){
        mItalicButton.setBackgroundResource(isItalic ? R.drawable.shape_caption_font_corner_button_selected : R.drawable.shape_caption_font_corner_button);
        mItalicButton.setTextColor(isItalic ? Color.parseColor("#ff4a90e2") : Color.parseColor("#ffffffff"));
        mIsItalic = isItalic;
    }
    public void updateShadowButton(boolean isShadow){
        mShadowButton.setBackgroundResource(isShadow ? R.drawable.shape_caption_font_corner_button_selected : R.drawable.shape_caption_font_corner_button);
        mShadowButton.setTextColor(isShadow ? Color.parseColor("#ff4a90e2") : Color.parseColor("#ffffffff"));
        mIsShadow = isShadow;
    }

    public void notifyDataSetChanged(){
        if(mCaptionFontRecycleAdapter != null)
            mCaptionFontRecycleAdapter.notifyDataSetChanged();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootParent = inflater.inflate(R.layout.caption_font_list_fragment, container, false);
        //mDownloadMoreCapionSytle = (RelativeLayout) rootParent.findViewById(R.id.download_more);
        mCaptionFontRecycleView = (RecyclerView) rootParent.findViewById(R.id.captionFontRecycleView);
        mBoldButton = (Button) rootParent.findViewById(R.id.boldButton);
        mItalicButton = (Button) rootParent.findViewById(R.id.italicButton);
        mShadowButton = (Button) rootParent.findViewById(R.id.shadowButton);
        mApplyToAll = (LinearLayout)rootParent.findViewById(R.id.applyToAll);
        mApplyToAllImage = (ImageView)rootParent.findViewById(R.id.applyToAllImage);
        mApplyToAllText = (TextView)rootParent.findViewById(R.id.applyToAllText);
        return rootParent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAssetRecycleAdapter();
        if(mCaptionFontListener != null){
            mCaptionFontListener.onFragmentLoadFinished();
        }
    }

    private void initAssetRecycleAdapter() {
        mBoldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsBold = !mIsBold;
                updateBoldButton(mIsBold);
                if(mCaptionFontListener != null){
                    mCaptionFontListener.onBold();
                }
            }
        });
        mItalicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsItalic = !mIsItalic;
                updateItalicButton(mIsItalic);
                if(mCaptionFontListener != null){
                    mCaptionFontListener.onItalic();
                }
            }
        });
        mShadowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsShadow = !mIsShadow;
                updateShadowButton(mIsShadow);
                if(mCaptionFontListener != null){
                    mCaptionFontListener.onShadow();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mCaptionFontRecycleView.setLayoutManager(layoutManager);
        mCaptionFontRecycleAdapter = new CaptionFontRecyclerAdaper(getActivity());
        mCaptionFontRecycleAdapter.setAssetInfoList(mCaptionFontInfolist);
        mCaptionFontRecycleView.setAdapter(mCaptionFontRecycleAdapter);
        mCaptionFontRecycleView.addItemDecoration(new SpaceItemDecoration(0, 8));
        mCaptionFontRecycleAdapter.setOnItemClickListener(new CaptionFontRecyclerAdaper.OnFontItemClickListener() {
            @Override
            public void onItemDownload(View view, int position) {
                if(mCaptionFontListener != null){
                    mCaptionFontListener.onFontDownload(position);
                }
            }

            @Override
            public void onItemClick(View view, int pos) {
                if(mCaptionFontListener != null){
                    mCaptionFontListener.onItemClick(pos);
                }
            }
        });

        mApplyToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsApplyToAll = !mIsApplyToAll;
                applyToAllCaption(mIsApplyToAll);
                if(mCaptionFontListener != null){
                    mCaptionFontListener.onIsApplyToAll(mIsApplyToAll);
                }
            }
        });
    }
}
