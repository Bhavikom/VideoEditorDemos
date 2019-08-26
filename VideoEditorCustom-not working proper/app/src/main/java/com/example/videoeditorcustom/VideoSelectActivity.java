package com.example.videoeditorcustom;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
//import android.dataDataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.example.videoeditorcustom.utils.VideoInfo;
import com.example.videoeditorcustom.utils.VideoInfoLoader;
import com.example.videoeditorcustom.widget.GridSpacingItemDecoration;
/*import com.greymax.android.sve.app.dataVideoSelectLayoutBinding;
import com.greymax.android.sve.app.utils.VideoInfo;
import com.greymax.android.sve.app.utils.VideoInfoLoader;
import com.greymax.android.sve.app.widget.GridSpacingItemDecoration;*/

import java.util.ArrayList;


public class VideoSelectActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<VideoInfo>>{

    private static final int LOADER_ID_MEDIA_STORE_DATA = 1001;
    public static final int VIDEO_ADD_REQUEST_CODE = 0x001;
    public static final int VIDEO_ADD_RESPONSE_CODE = 0x002;
    //private VideoSelectLayoutBinding binding;
    private String videoPath;
    private int clipIndex;

    RecyclerView videoSelectRecyclerview;
    TextView nextStep;
    public static void go(FragmentActivity from, int clipIndex){
        if(clipIndex != -1) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", clipIndex);
            Intent intent = new Intent(from,VideoSelectActivity.class);
            intent.putExtras(bundle);
            from.startActivityForResult(intent,VIDEO_ADD_REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView( R.layout.video_select_layout);

        videoSelectRecyclerview = findViewById(R.id.video_select_recyclerview);
        nextStep = findViewById(R.id.next_step);
        nextStep.setOnClickListener(this);
        Bundle bd = getIntent().getExtras();
        if(bd != null)
            clipIndex = bd.getInt("index");

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
        getLoaderManager().initLoader(LOADER_ID_MEDIA_STORE_DATA, null, this);

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        videoSelectRecyclerview.addItemDecoration(new GridSpacingItemDecoration(5));
        videoSelectRecyclerview.setHasFixedSize(true);
        videoSelectRecyclerview.setLayoutManager(manager);

        nextStep.setOnClickListener(this);

        nextStep.setTextAppearance(this, R.style.gray_text_18_style);
        nextStep.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == nextStep.getId()) {
            TrimmerActivity.go(VideoSelectActivity.this, videoPath);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TrimmerActivity.VIDEO_TRIM_REQUEST_CODE
                && resultCode == TrimmerActivity.VIDEO_TRIM_RESPONSE_CODE) {
            String clipPath = data.getStringExtra("path");
            int clipFilter = data.getIntExtra("filter", 0);
            Intent intent = new Intent();
            intent.putExtra("path", clipPath);
            intent.putExtra("filter", clipFilter);
            intent.putExtra("index", clipIndex);
            setResult(VIDEO_ADD_RESPONSE_CODE, intent);
            finish();
        }
    }

    @Override
    public Loader<ArrayList<VideoInfo>> onCreateLoader(int i, Bundle bundle) {
        return new VideoInfoLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<VideoInfo>> loader,
                               ArrayList<VideoInfo> allVideos) {
        RequestManager requestManager = Glide.with(this);
        VideoGridViewAdapter adapter =
                new VideoGridViewAdapter(this, allVideos, requestManager);
        RecyclerViewPreloader<VideoInfo> preloader =
                new RecyclerViewPreloader<>(requestManager, adapter, adapter, 3);
        videoSelectRecyclerview.addOnScrollListener(preloader);
        videoSelectRecyclerview.setAdapter(adapter);
        adapter.setItemClickCallback(new VideoGridViewAdapter.ItemClickCallback<Boolean, VideoInfo>() {
            @Override
            public void onItemClickCallback(Boolean isSelected, VideoInfo video) {
                if (video != null)
                    videoPath = video.getVideoPath();
                nextStep.setEnabled(isSelected);
                nextStep.setTextAppearance(VideoSelectActivity.this, isSelected ? R.style.blue_text_18_style : R.style.gray_text_18_style);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<VideoInfo>> loader) {
        // Do nothing.
    }
}
