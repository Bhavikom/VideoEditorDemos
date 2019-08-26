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

public class CaptionColorFragment extends Fragment {
    private RecyclerView mCaptionColorRecyerView;
    private CaptionColorRecyclerAdaper mCaptionColorRecycleAdapter;
    private SeekBar mCaptonOpacitySeekBar;
    private TextView mSeekBarOpacityValue;
    private OnCaptionColorListener mCaptionColorListener;
    private ArrayList<CaptionColorInfo> mCaptionColorInfolist = new ArrayList<>();
    private LinearLayout mApplyToAll;
    private ImageView mApplyToAllImage;
    private TextView mApplyToAllText;
    private boolean mIsApplyToAll = false;
    public interface OnCaptionColorListener{
        void onFragmentLoadFinished();
        void onCaptionColor(int pos);
        void onCaptionOpacity(int progress);
        void onIsApplyToAll(boolean isApplyToAll);
    }

    public void setCaptionColorListener(OnCaptionColorListener captionColorListener) {
        this.mCaptionColorListener = captionColorListener;
    }
    public void updateCaptionOpacityValue(int progress){
        mSeekBarOpacityValue.setText(String.valueOf(progress));
        mCaptonOpacitySeekBar.setProgress(progress);
    }

    public void setCaptionColorInfolist(ArrayList<CaptionColorInfo> captionColorInfolist) {
        this.mCaptionColorInfolist = captionColorInfolist;
        if(mCaptionColorRecycleAdapter != null)
            mCaptionColorRecycleAdapter.setCaptionColorList(captionColorInfolist);
    }

    public void applyToAllCaption(boolean isApplyToAll){
        mApplyToAllImage.setImageResource(isApplyToAll ? R.mipmap.applytoall : R.mipmap.unapplytoall);
        mApplyToAllText.setTextColor(isApplyToAll ? Color.parseColor("#ff4a90e2") : Color.parseColor("#ff909293"));
        mIsApplyToAll = isApplyToAll;
    }

    public void notifyDataSetChanged(){
        mCaptionColorRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootParent = inflater.inflate(R.layout.caption_color_fragment, container, false);
        mCaptionColorRecyerView = (RecyclerView)rootParent.findViewById(R.id.captionColorRecyerView);
        mCaptonOpacitySeekBar = (SeekBar)rootParent.findViewById(R.id.captonOpacitySeekBar);
        mCaptonOpacitySeekBar.setMax(100);
        mSeekBarOpacityValue = (TextView) rootParent.findViewById(R.id.seekBarOpacityValue);
        mApplyToAll = (LinearLayout)rootParent.findViewById(R.id.applyToAll);
        mApplyToAllImage = (ImageView)rootParent.findViewById(R.id.applyToAllImage);
        mApplyToAllText = (TextView)rootParent.findViewById(R.id.applyToAllText);
        return rootParent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCaptionColorRecycleAdapter();
        initCaptionColorSeekBar();
        if(mCaptionColorListener != null){
            mCaptionColorListener.onFragmentLoadFinished();
        }
    }

    private void initCaptionColorSeekBar() {
        mCaptonOpacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    updateCaptionOpacityValue(progress);
                    if(mCaptionColorListener != null){
                        mCaptionColorListener.onCaptionOpacity(progress);
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
    }

    private void initCaptionColorRecycleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mCaptionColorRecyerView.setLayoutManager(layoutManager);
        mCaptionColorRecycleAdapter = new CaptionColorRecyclerAdaper(getActivity());
        mCaptionColorRecycleAdapter.setCaptionColorList(mCaptionColorInfolist);
        mCaptionColorRecyerView.setAdapter(mCaptionColorRecycleAdapter);
        mCaptionColorRecyerView.addItemDecoration(new SpaceItemDecoration(0, ScreenUtils.dip2px(getActivity(),29)));
        mCaptionColorRecycleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if(mCaptionColorListener != null){
                    mCaptionColorListener.onCaptionColor(pos);
                }
            }
        });

        mApplyToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsApplyToAll = !mIsApplyToAll;
                applyToAllCaption(mIsApplyToAll);
                if(mCaptionColorListener != null){
                    mCaptionColorListener.onIsApplyToAll(mIsApplyToAll);
                }
            }
        });
    }
}
