package zippler.cn.xs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.util.FileUtil;

public class SettingsActivity extends BaseActivity {
    private RelativeLayout clearCache;
    private RelativeLayout about_layout;
    private TextView cacheSize;
    private List<String> videoCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();

        registerListeners();
    }

    private void initViews(){
        clearCache = findViewById(R.id.clear_cache);
        about_layout = findViewById(R.id.about_layout);
        cacheSize = findViewById(R.id.cache_size);
        cacheSize.setText(getCacheSize()+"MB");
    }


    private void registerListeners() {
        clearCache.setOnClickListener(this);
        about_layout.setOnClickListener(this);
    }

    private String getCacheSize(){
        //ergodic videoCache.xsheng.
        String result;
        videoCache = FileUtil.traverseFolder(FileUtil.getCamera2Path()+"videoCache"+ File.separator);
        File file = new File(FileUtil.getCamera2Path());
        File[] files = file.listFiles();
        if (videoCache==null){
            videoCache = new ArrayList<>();
        }
        if (files.length != 0) {
            for (File file2 : files) {
                if (!file2.isDirectory()) {
                    if (FileUtil.isVideoFile(file2.getAbsolutePath())){
                        videoCache.add(file2.getAbsolutePath());
                    }
                }
            }
        }
        long size=0;
        for (String path:videoCache) {
            size+=new File(path).length();
        }
        if (size==0){
            result = "0.0 ";
        }else{
            DecimalFormat df = new DecimalFormat("#.00");
            result = df.format((double)size/1024/1024);
        }
        return result;
    }

    private void clearCache(){
        if (videoCache!=null&&videoCache.size()>0){
            for (String path:videoCache) {
                File temp = new File(path);
                if (temp.exists()){
                    temp.delete();
                }
            }
            cacheSize.setText("0.0MB");
            toastView("成功清理缓存",R.mipmap.correct);
        }else{
            toastView("无需清理缓存",R.mipmap.error);
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.clear_cache:
                clearCache();
                break;
            case R.id.about_layout:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
            default:
                Log.d(TAG, "onClick: default clicked");
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.acticity_open_anim,R.anim.acticity_close_anim);
    }
}
