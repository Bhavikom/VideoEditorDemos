package zippler.cn.xs.fragment;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.activity.PreviewActivity;
import zippler.cn.xs.activity.RecorderActivity;
import zippler.cn.xs.adapter.RecyclerChooseMusicAdapter;
import zippler.cn.xs.entity.Music;
import zippler.cn.xs.util.LinearScrollLayoutManager;
import zippler.cn.xs.util.SpaceItemDecoration;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;//display the Synthetic music
    private LinearLayout upload;
    private LinearLayout record;
    private RelativeLayout search;

    private ScrollView scrollView;

    //local video code
    private static final int REQUEST_VIDEO_CODE = 0;


    //data
    private List<Music> musicList;

    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        initViews(view);
        registerListeners();

        LinearScrollLayoutManager linerLayoutManager = new LinearScrollLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linerLayoutManager);
//        recyclerView.addItemDecoration(new RemoveLastLineDividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new SpaceItemDecoration(0,4));

        Log.d(TAG, "onCreateView: music init");
        initMusic();
        Log.d(TAG, "onCreateView: music init " +musicList.size());
        RecyclerChooseMusicAdapter musicAdapter = new RecyclerChooseMusicAdapter(this.getContext(),musicList);
        recyclerView.setAdapter(musicAdapter);

        recyclerView.setFocusable(false); //fixed scrollview scrolling to top after loading data in recycler view
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.upload:
                openLocalVideos();
                break;
            case R.id.record:
                Intent intent = new Intent(this.getActivity(), RecorderActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this.getContext(),"default clicked",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * instantiate views
     * @param view the root view
     */
    private void initViews(View view){
        recyclerView = view.findViewById(R.id.synthetic_music);
        upload = view.findViewById(R.id.upload);
        record = view.findViewById(R.id.record);
        search = view.findViewById(R.id.search);
        scrollView = view.findViewById(R.id.scroll_view);
    }

    /**
     * register listeners
     */
    private void registerListeners(){
        upload.setOnClickListener(this);
        record.setOnClickListener(this);
        search.setOnClickListener(this);
    }

    /**
     * init music
     */
    private void initMusic(){
        musicList = new ArrayList<>();
        MediaPlayer player;
        //add music data here . it also can be load from internet
        for (int i = 1; i <= 1; i++) {
            Music temp = new Music();
            temp.setName("合成曲_night");
//            temp.setLocalStorageUrl(getCamera2Path()+"test.mp3");
            temp.setLocalStorageUrl(getCamera2Path()+"night.mp3");
            player = MediaPlayer.create(this.getContext(),Uri.parse(temp.getLocalStorageUrl()));

            temp.setDuration(player.getDuration());
            temp.setLength(player.getDuration());

            musicList.add(temp);
        }

        for (int i = 2; i <= 2; i++) {
            Music s = new Music();
            s.setName("合成曲__brother");
//            s.setLocalStorageUrl(getCamera2Path()+"j.mp3");
            s.setLocalStorageUrl(getCamera2Path()+"brother.mp3");
            player = MediaPlayer.create(this.getContext(),Uri.parse(s.getLocalStorageUrl()));
            s.setDuration(player.getDuration());
            s.setLength(player.getDuration());

            musicList.add(s);
        }
    }

    /**
     * open local videos view
     */
    private void openLocalVideos(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_VIDEO_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==REQUEST_VIDEO_CODE){
            if (resultCode == RESULT_OK){
                Uri uri = data.getData();
                ContentResolver cr = this.getActivity().getContentResolver();
                assert uri!=null;
                Cursor cursor = cr.query(uri, null, null, null, null);
                if (cursor != null) {
                    String videoPath = "initial video path";
                    if (cursor.moveToFirst()) {
                        int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                        videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                        Bitmap bitmap1 = MediaStore.Video.Thumbnails.getThumbnail(cr, imageId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

                        Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MICRO_KIND);
                    }
                    cursor.close();
                    Intent intent = new Intent(this.getActivity(), PreviewActivity.class);
                    intent.putExtra("videoPath",videoPath);
                    startActivity(intent);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getCamera2Path() {
        String picturePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"xsheng"+File.separator;
        File file = new File(picturePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return picturePath;
    }


    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public LinearLayout getUpload() {
        return upload;
    }

    public void setUpload(LinearLayout upload) {
        this.upload = upload;
    }

    public LinearLayout getRecord() {
        return record;
    }

    public void setRecord(LinearLayout record) {
        this.record = record;
    }

    public RelativeLayout getSearch() {
        return search;
    }

    public void setSearch(RelativeLayout search) {
        this.search = search;
    }

}
