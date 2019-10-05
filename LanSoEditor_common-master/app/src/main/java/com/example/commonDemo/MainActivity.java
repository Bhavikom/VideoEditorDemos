package com.example.commonDemo;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.example.commonDemo.advanceSuggest.ListAdvanceDemoActivity;
import com.example.commonDemo.noFree.ListNoFreeActivity;
import com.lansoeditor.demo.R;
import com.lansosdk.NoFree.AudioPadExecute;
import com.lansosdk.NoFree.LSOCompressVideo;
import com.lansosdk.box.AudioLayer;
import com.lansosdk.box.OnCompressCompletedListener;
import com.lansosdk.box.OnCompressProgressListener;
import com.lansosdk.videoeditor.CopyFileFromAssets;
import com.lansosdk.videoeditor.LanSoEditor;
import com.lansosdk.videoeditor.LanSongFileUtil;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.videoeditor.VideoEditor;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {

    private DemoInfo[] mTestCmdArray = {

            new DemoInfo(R.string.demo_id_avmerge, R.string.demo_more_avmerge, true, false),
            new DemoInfo(R.string.demo_id_cutaudio, R.string.demo_more_cutaudio, false, true),
            new DemoInfo(R.string.demo_id_cutvideo, R.string.demo_more_cutvideo, true, false),
            new DemoInfo(R.string.demo_id_avsplit, R.string.demo_id_avsplit, true, false),

            new DemoInfo(R.string.demo_id_concatvideo, R.string.demo_more_concatvideo, true, false),

            new DemoInfo(R.string.demo_id_videocrop, R.string.demo_more_videocrop, true, false),
            new DemoInfo(R.string.demo_id_videocompress, R.string.demo_id_videocompress, true, false),

            new DemoInfo(R.string.demo_id_videowatermark, R.string.demo_more_videowatermark, true, false),
            new DemoInfo(R.string.demo_id_videocropwatermark, R.string.demo_more_videocropwatermark, true, false),
            new DemoInfo(R.string.demo_id_videoclockwise90, R.string.demo_more_videoclockwise90, true, false),
            new DemoInfo(R.string.demo_id_videocounterClockwise90, R.string.demo_more_videocounterClockwise90, true, false),
            new DemoInfo(R.string.demo_id_videoaddanglemeta, R.string.demo_more_videoaddanglemeta, true, false),
            new DemoInfo(R.string.demo_id_audiodelaymix, R.string.demo_more_audiodelaymix, false, true),

            new DemoInfo(R.string.demo_id_audiovolumemix, R.string.demo_more_audiovolumemix, false, true),

            new DemoInfo(R.string.demo_id_videopad, R.string.demo_more_videopad, true, false),

            new DemoInfo(R.string.demo_id_videoadjustspeed, R.string
                    .demo_more_videoadjustspeed, true, false),
            new DemoInfo(R.string.demo_id_videomirrorh, R.string
                    .demo_more_videomirrorh, true, false),
            new DemoInfo(R.string.demo_id_videomirrorv, R.string
                    .demo_more_videomirrorv, true, false),
            new DemoInfo(R.string.demo_id_videorotateh, R.string
                    .demo_more_videorotateh, true, false),
            new DemoInfo(R.string.demo_id_videorotatev, R.string
                    .demo_more_videorotatev, true, false),

            new DemoInfo(R.string.demo_id_videoreverse, R.string
                    .demo_more_videoreverse, true, false),

            new DemoInfo(R.string.demo_id_avreverse, R.string
                    .demo_more_avreverse, true, false),


            new DemoInfo(R.string.demo_id_videolayout, R.string
                    .demo_id_videolayout, true, false),

            new DemoInfo(R.string.demo_id_expend_cmd, R.string.demo_id_expend_cmd, false, false),
            new DemoInfo(R.string.direct_play_video, R.string.direct_play_video, false, false),
            new DemoInfo(R.string.demo_id_nofree, R.string.demo_id_nofree, false, false)
    };


    private ListView mListView = null;
    private TextView tvVideoPath;
    private boolean isPermissionOk = false;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_layout);

        //加载so库,并初始化.
        LanSoEditor.initSDK(getApplicationContext(), null);

        checkPermissions();

        initView();

        showHintDialog();

        testFile();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LanSongFileUtil.deleteDir(new File(LanSongFileUtil.getPath())); //删除dir
    }

    /**
     * 各种item, 从这里传递到 AVEditorDemoActivity中
     *
     * @param position
     */
    private void startActivity(int position) {
        DemoInfo demo = mTestCmdArray[position];
        Intent intent = new Intent(MainActivity.this, AVEditorDemoActivity.class);

        intent.putExtra("videopath1", tvVideoPath.getText().toString());
        intent.putExtra("outvideo", demo.isOutVideo);
        intent.putExtra("outaudio", demo.isOutAudio);
        intent.putExtra("demoID", demo.mHintId);
        intent.putExtra("textID", demo.mTextId);
        startActivity(intent);
    }

    //直接播放视频.
    private void startVideoPlayer() {
        Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
        intent.putExtra("videopath", tvVideoPath.getText().toString());
        startActivity(intent);
    }

    private void startVideoLayout() {
        Intent intent = new Intent(MainActivity.this, VideoLayoutDemoActivity.class);
        intent.putExtra("videopath", tvVideoPath.getText().toString());
        startActivity(intent);
    }

    private void checkPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                isPermissionOk = true;
            }

            @Override
            public void onDenied(String permission) {
                isPermissionOk = false;
            }
        });
    }

    private void initView() {
        findViewById(R.id.id_main_advance_demo).setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListAdvanceDemoActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.id_main_nofree_demo).setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                if (checkPath()) {
                    Intent intent = new Intent(MainActivity.this, ListNoFreeActivity.class);
                    intent.putExtra("videopath", tvVideoPath.getText().toString());
                    startActivity(intent);
                }
            }
        });


        tvVideoPath = (TextView) findViewById(R.id.id_main_tvvideo);

        findViewById(R.id.id_main_select_video).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startSelectVideoActivity();
            }
        });
        findViewById(R.id.id_main_use_default_videobtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new CopyVideoAsync(MainActivity.this, tvVideoPath, "dy_xialu2.mp4")
                        .execute();
            }
        });
        mListView = (ListView) findViewById(R.id.id_demo_list);
        mListView.setAdapter(new SoftApAdapter(MainActivity.this));

        /**
         * 在这里相应每个item的按下;
         */
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mTestCmdArray.length - 1) {  //付费功能
                        if (checkPath()) {
                            Intent intent = new Intent(MainActivity.this, ListNoFreeActivity.class);
                            intent.putExtra("videopath", tvVideoPath.getText().toString());
                            startActivity(intent);
                        }
                }else if (position == mTestCmdArray.length - 2) {  //直接视频播放
                    startVideoPlayer();
                } else if (position == mTestCmdArray.length - 3) {  //自定义
                    Intent intent = new Intent(MainActivity.this, CustomFunctionActivity.class);
                    startActivity(intent);

                } else if (position == mTestCmdArray.length - 4) {  //视频布局
                    startVideoLayout();
                } else {
                    if (checkPath()) {
                        startActivity(position);
                    }
                }
            }
        });
    }

    //-----------------------------------------
    private final static int SELECT_FILE_REQUEST_CODE = 10;

    private void startSelectVideoActivity() {
        Intent i = new Intent(this, FileExplorerActivity.class);
        i.putExtra("SELECT_MODE", "video");
        startActivityForResult(i, SELECT_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == SELECT_FILE_REQUEST_CODE) {
                    Bundle b = data.getExtras();
                    String string = b.getString("SELECT_VIDEO");
                    Log.i("sno", "SELECT_VIDEO is:" + string);
                    if (tvVideoPath != null)
                        tvVideoPath.setText(string);
                }
                break;
            default:
                break;
        }
    }

    private boolean checkPath() {
        if (isPermissionOk == false) {
            testPermission();
        }

        if (tvVideoPath.getText() != null && tvVideoPath.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "请输入视频地址", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            String path = tvVideoPath.getText().toString();
            if ((new File(path)).exists() == false) {
                Toast.makeText(MainActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                MediaInfo info = new MediaInfo(path);
                boolean ret = info.prepare();
                if (ret == false) {
                    showHintDialog("可能不是音视频文件");
                }
                return ret;
            }
        }
    }

    //--------------------------------------------------------------
    @SuppressLint("NewApi")
    public static boolean selfPermissionGranted(Context context, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        int targetSdkVersion = 0;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            } else {
                result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker
                        .PERMISSION_GRANTED;
            }
        }
        return result;
    }

    private void showHintDialog() {

        String timeHint = "欢迎使用蓝松短视频SDK 免费版本.\n 当前版本是:" + VideoEditor.getSDKVersion() +
                "\n\n 我们编写了34个常见功能, 一下仅是部分举例,详情请参考我们的PDF功能列表; ";

        new AlertDialog.Builder(this).setTitle("提示").setMessage(timeHint).setPositiveButton("确定", new DialogInterface
                .OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void showHintDialog(String hint) {
        new AlertDialog.Builder(this).setTitle("提示").setMessage(hint).setPositiveButton("确定", new DialogInterface
                .OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    //------------------------------------------
    private class SoftApAdapter extends BaseAdapter {

        private Activity mActivity;

        public SoftApAdapter(Activity activity) {
            mActivity = activity;
        }

        @Override
        public int getCount() {
            return mTestCmdArray.length;
        }

        @Override
        public Object getItem(int position) {
            return mTestCmdArray[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = mActivity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.test_cmd_item, parent, false);
            }
            TextView tvNumber = (TextView) convertView.findViewById(R.id.id_test_cmditem_cnt);
            TextView tvName = (TextView) convertView.findViewById(R.id.id_test_cmditem_tv);
            DemoInfo cmdInfo = mTestCmdArray[position];
            String str = "NO.";
            str += String.valueOf(position + 1);

            tvNumber.setText(str);

            tvName.setText(getResources().getString(cmdInfo.mHintId));

            return convertView;
        }
    }

    boolean isTestedPermission = false;

    private void testPermission() {
        if (isTestedPermission) {
            showHintDialog("Demo没有读写权限,请关闭后重新打开demo,并在弹出框中选中[允许]");
            return;
        }
        Log.d(TAG, "再次检查权限");
        isTestedPermission = true;
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                isPermissionOk = true;
            }

            @Override
            public void onDenied(String permission) {
                isPermissionOk = false;
            }
        });
    }

    public class CopyVideoAsync extends AsyncTask<Object, Object, Boolean> {
        private ProgressDialog mProgressDialog;
        private Context mContext = null;
        private TextView tvHint;
        private String fileName;

        /**
         * @param ctx
         * @param tvhint 拷贝后, 把拷贝到的目标完整路径显示到这个TextView上.
         * @param file   需要拷贝的文件名字.
         */
        public CopyVideoAsync(Context ctx, TextView tvhint, String file) {
            mContext = ctx;
            tvHint = tvhint;
            fileName = file;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("正在拷贝...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected synchronized Boolean doInBackground(Object... params) {
            CopyFileFromAssets.copyAssets(mContext, fileName);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (mProgressDialog != null) {
                mProgressDialog.cancel();
                mProgressDialog = null;
            }

            String str = LanSongFileUtil.getPath() + fileName;
            if (LanSongFileUtil.fileExist(str)) {
                Toast.makeText(mContext, "默认视频文件拷贝完成.视频样片路径:" + str, Toast.LENGTH_SHORT).show();
                if (tvHint != null)
                    tvHint.setText(str);
            } else {
                Toast.makeText(mContext, "抱歉! 默认视频文件拷贝失败,请联系我们:视频样片路径:" + str, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private  void testFile(){
    }
}
