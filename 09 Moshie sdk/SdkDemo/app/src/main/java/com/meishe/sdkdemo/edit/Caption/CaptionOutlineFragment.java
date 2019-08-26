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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.data.CaptionColorInfo;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.utils.ScreenUtils;

import java.util.ArrayList;

public class CaptionOutlineFragment extends Fragment {
    private RecyclerView mCaptionOutlineRecyerView;
    private SeekBar mCaptonOutlineWidthSeekBar;
    private TextView mSeekBarOutlineWidthValue;
    private SeekBar mCaptonOutlineOpacitySeekBar;
    private TextView mSeekBarOutlineOpacityValue;
    private LinearLayout mApplyToAll;
    private ImageView mApplyToAllImage;
    private TextView mApplyToAllText;
    private boolean mIsApplyToAll = false;
    private CaptionOutlineRecyclerAdaper mCaptionOutlineRecycleAdapter;
    private ArrayList<CaptionColorInfo> mCaptionOutlineInfolist = new ArrayList<>();
    private OnCaptionOutlineListener mCaptionOutlineListener;
    public interface OnCaptionOutlineListener{
        void onFragmentLoadFinished();
        void onCaptionOutlineColor(int pos);
        void onCaptionOutlineWidth(int width);
        void onCaptionOutlineOpacity(int opacity);
        void onIsApplyToAll(boolean isApplyToAll);
    }
    public void setCaptionOutlineInfolist(ArrayList<CaptionColorInfo> captionOutlineInfolist) {
        this.mCaptionOutlineInfolist = captionOutlineInfolist;
        if (mCaptionOutlineRecycleAdapter != null)
            mCaptionOutlineRecycleAdapter.setCaptionOutlineColorList(captionOutlineInfolist);
    }
    public void setCaptionOutlineListener(OnCaptionOutlineListener captionOutlineListener) {
        this.mCaptionOutlineListener = captionOutlineListener;
    }

    public void updateCaptionOutlineWidthValue(int progress){
        mSeekBarOutlineWidthValue.setText(String.valueOf(progress));
        mCaptonOutlineWidthSeekBar.setProgress(progress);
    }

    public void updateCaptionOutlineOpacityValue(int progress){
        mSeekBarOutlineOpacityValue.setText(String.valueOf(progress));
        mCaptonOutlineOpacitySeekBar.setProgress(progress);
    }

    public void applyToAllCaption(boolean isApplyToAll){
        mApplyToAllImage.setImageResource(isApplyToAll ? R.mipmap.applytoall : R.mipmap.unapplytoall);
        mApplyToAllText.setTextColor(isApplyToAll ? Color.parseColor("#ff4a90e2") : Color.parseColor("#ff909293"));
        mIsApplyToAll = isApplyToAll;
    }
    public void notifyDataSetChanged(){
        mCaptionOutlineRecycleAdapter.notifyDataSetChanged();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootParent = inflater.inflate(R.layout.caption_outline_fragment, container, false);
        mCaptionOutlineRecyerView = (RecyclerView)rootParent.findViewById(R.id.captionOutlineRecyerView);
        mCaptonOutlineWidthSeekBar = (SeekBar)rootParent.findViewById(R.id.captonOutlineWidthSeekBar);
        mCaptonOutlineWidthSeekBar.setMax(16);
        mSeekBarOutlineWidthValue = (TextView) rootParent.findViewById(R.id.seekBarOutlineWidthValue);
        mCaptonOutlineOpacitySeekBar = (SeekBar)rootParent.findViewById(R.id.captonOutlineOpacitySeekBar);
        mCaptonOutlineOpacitySeekBar.setMax(100);
        mSeekBarOutlineOpacityValue = (TextView) rootParent.findViewById(R.id.seekBarOutlineOpacityValue);
        mApplyToAll = (LinearLayout)rootParent.findViewById(R.id.applyToAll);
        mApplyToAllImage = (ImageView)rootParent.findViewById(R.id.applyToAllImage);
        mApplyToAllText = (TextView)rootParent.findViewById(R.id.applyToAllText);
        return rootParent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCaptionColorRecycleAdapter();
        initCaptionOutlineSeekBar();
        if(mCaptionOutlineListener != null){
            mCaptionOutlineListener.onFragmentLoadFinished();
        }
    }
    private void initCaptionColorRecycleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mCaptionOutlineRecyerView.setLayoutManager(layoutManager);
        mCaptionOutlineRecycleAdapter = new CaptionOutlineRecyclerAdaper(getActivity());
        mCaptionOutlineRecycleAdapter.setCaptionOutlineColorList(mCaptionOutlineInfolist);
        mCaptionOutlineRecyerView.setAdapter(mCaptionOutlineRecycleAdapter);
        mCaptionOutlineRecyerView.addItemDecoration(new SpaceItemDecoration(0, ScreenUtils.dip2px(getActivity(),16)));
        mCaptionOutlineRecycleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if(mCaptionOutlineListener != null){
                    mCaptionOutlineListener.onCaptionOutlineColor(pos);
                }
            }
        });
    }
    private void initCaptionOutlineSeekBar() {
        mCaptonOutlineWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    updateCaptionOutlineWidthValue(progress);
                    if(mCaptionOutlineListener != null){
                        mCaptionOutlineListener.onCaptionOutlineWidth(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mCaptonOutlineOpacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    updateCaptionOutlineOpacityValue(progress);
                    if(mCaptionOutlineListener != null){
                        mCaptionOutlineListener.onCaptionOutlineOpacity(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mApplyToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsApplyToAll = !mIsApplyToAll;
                applyToAllCaption(mIsApplyToAll);
                if(mCaptionOutlineListener != null){
                    mCaptionOutlineListener.onIsApplyToAll(mIsApplyToAll);
                }
            }
        });
    }
}
