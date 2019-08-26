package zippler.cn.xs.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.adapter.MainViewPagerAdapter;
import zippler.cn.xs.component.NoPreloadViewPager;
import zippler.cn.xs.entity.Video;
import zippler.cn.xs.fragment.CameraFragment;
import zippler.cn.xs.fragment.UserFragment;
import zippler.cn.xs.fragment.VideoFragment;
import zippler.cn.xs.listener.MainOnPageChangeListener;
import zippler.cn.xs.listener.MainOnTabSelectedListener;

public class MainActivity extends BaseActivity {
    private TabLayout tabLayout;
    private NoPreloadViewPager viewPager;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: " + TAG);//TAG is defined in base activity
        tabLayout = findViewById(R.id.toolbar_tab);
        viewPager = findViewById(R.id.view_pager);
        initFragment();
        initListener();
        viewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(),fragments));
//        tabLayout.setupWithViewPager(viewPager);
    }

    private void initFragment(){
        fragments = new ArrayList<>();
        //add fragments here
        VideoFragment fragment = new VideoFragment();
        fragment.setContext(this);
        Video video = (Video) getIntent().getSerializableExtra("video");
        fragment.setDeployedVideo(video);
        fragments.add(fragment);
        fragments.add(new CameraFragment());
        fragments.add(new UserFragment());
    }

    private void initListener(){
        tabLayout.setOnTabSelectedListener(new MainOnTabSelectedListener(viewPager,tabLayout,this));
        viewPager.setOnPageChangeListener(new MainOnPageChangeListener(tabLayout));
    }
}