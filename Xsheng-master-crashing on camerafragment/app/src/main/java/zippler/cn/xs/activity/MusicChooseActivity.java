package zippler.cn.xs.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.adapter.MusicChooseAdapter;
import zippler.cn.xs.entity.Music;
import zippler.cn.xs.util.LinearScrollLayoutManager;
import zippler.cn.xs.util.RemoveLastLineDividerItemDecoration;

import static zippler.cn.xs.util.FileUtil.getCamera2Path;

public class MusicChooseActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private LinearLayout record;
    private RelativeLayout search;


    //data
    private List<Music> musicList;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_choose);
        initViews();
        registerListeners();
        LinearScrollLayoutManager linerLayoutManager = new LinearScrollLayoutManager(this);
        recyclerView.setLayoutManager(linerLayoutManager);
        recyclerView.addItemDecoration(new RemoveLastLineDividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initMusic();
        MusicChooseAdapter musicAdapter = new MusicChooseAdapter(this,musicList);
        recyclerView.setAdapter(musicAdapter);
        recyclerView.setFocusable(false); //fixed scrollview scrolling to top after loading data in recycler view
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_c:
                Intent intent = new Intent(this, RecorderActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this,"default clicked",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * instantiate views
     */
    private void initViews(){
        recyclerView = findViewById(R.id.synthetic_music_c);
        record = findViewById(R.id.record_c);
        search = findViewById(R.id.search_c);
        scrollView = findViewById(R.id.scroll_view_c);
    }

    /**
     * register listeners
     */
    private void registerListeners(){
        record.setOnClickListener(this);
        search.setOnClickListener(this);
    }

    /**
     * init music
     */
    private void initMusic(){
        musicList = new ArrayList<>();
        MediaPlayer player;
        //add music data here . it also can be load from internete
        String[] names= {"Tank Battle","Horizontal","Duang"};
        for (int i = 1; i <= 1; i++) {
            Music temp = new Music();
            temp.setName(names[i-1]);
            temp.setLocalStorageUrl(getCamera2Path()+"test.mp3");
            player = MediaPlayer.create(this, Uri.parse(temp.getLocalStorageUrl()));

            temp.setDuration(player.getDuration());
            temp.setLength(player.getDuration());

            musicList.add(temp);
        }

        for (int i = 2; i <= 2; i++) {
            Music s = new Music();
            s.setName(names[i-1]);
            s.setLocalStorageUrl(getCamera2Path()+"j.mp3");
            player = MediaPlayer.create(this,Uri.parse(s.getLocalStorageUrl()));
            s.setDuration(player.getDuration());
            s.setLength(player.getDuration());

            musicList.add(s);
        }
        for (int i = 3; i <= 3; i++) {
            Music s = new Music();
            s.setName(names[i-1]);
            s.setLocalStorageUrl(getCamera2Path()+"duang.mp3");
            player = MediaPlayer.create(this,Uri.parse(s.getLocalStorageUrl()));
            s.setDuration(player.getDuration());
            s.setLength(player.getDuration());

            musicList.add(s);
        }
    }

}
