package com.example.videoeditorcustom;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.videoeditorcustom.utils.FileUtils;
import com.greymax.android.sve.SVE;
/*import com.greymax.android.sve.app.databinding.ActivityVideoAddBinding;
import com.greymax.android.sve.app.utils.FileUtils;*/

import java.io.File;

public class VideoAddActivity extends AppCompatActivity  {

    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    //private ActivityVideoAddBinding binding;
    private SVE mSVE;

    ImageButton imgaddVideo1,imgAddvideo2,imgAddVideo3,imgAddVideo4;
    TextView gotoOutput;
    TextView videoInfo1,videoInfo2,videoInfo3,videoInfo4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_add);

        mSVE = SVE.getSVE(getApplicationContext());

        videoInfo1 = findViewById(R.id.video_info1);
        videoInfo2 = findViewById(R.id.video_info2);
        videoInfo3 = findViewById(R.id.video_info3);
        videoInfo4 = findViewById(R.id.video_info4);

        imgaddVideo1 = findViewById(R.id.addVideo1);
        imgAddvideo2 = findViewById(R.id.addVideo2);
        imgAddVideo3 = findViewById(R.id.addVideo3);
        imgAddVideo4 = findViewById(R.id.addVideo4);
        gotoOutput = findViewById(R.id.goto_output);

        imgaddVideo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSelectActivity.go(VideoAddActivity.this, 0);
            }
        });
        imgAddvideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSelectActivity.go(VideoAddActivity.this, 1);
            }
        });
        imgAddVideo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSelectActivity.go(VideoAddActivity.this, 2);
            }
        });
        imgAddVideo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSelectActivity.go(VideoAddActivity.this, 3);
            }
        });
        gotoOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoOutputActivity.go(VideoAddActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, "Storage read permission is needed", REQUEST_STORAGE_READ_ACCESS_PERMISSION);
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage write permission is needed", REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        }
        if (mSVE.getAllInputClips() == null || mSVE.getAllInputClips().size() == 0) {
            gotoOutput.setTextAppearance(this, R.style.gray_text_18_style);;
            gotoOutput.setEnabled(false);
        } else {
            gotoOutput.setEnabled(true);
            gotoOutput.setTextAppearance(this, R.style.blue_text_18_style);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String trimmedVideoPath = FileUtils.getSaveTrimVideoDir(this);
        if (!TextUtils.isEmpty(trimmedVideoPath)) {
            FileUtils.deleteFile(new File(trimmedVideoPath));
        }
        SVE.releaseSVE();
        mSVE = null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VideoSelectActivity.VIDEO_ADD_REQUEST_CODE
                && resultCode == VideoSelectActivity.VIDEO_ADD_RESPONSE_CODE) {
            int clipIndex = data.getIntExtra("index", 0);
            String clipPath = data.getStringExtra("path");
            int clipFilter = data.getIntExtra("filter", 0);
            mSVE.addOneInputClip(clipPath, clipFilter, clipIndex);
            updateVideoInfoText(clipIndex, clipPath);
        }
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Permission needed");
            builder.setMessage(rationale);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(VideoAddActivity.this, new String[]{permission}, requestCode);
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void updateVideoInfoText(int clipIndex, String clipPath) {
        switch (clipIndex) {
            case 0:
                videoInfo1.setText("path:" + clipPath);
                break;
            case 1:
                videoInfo2.setText("path:" + clipPath);
                break;
            case 2:
                videoInfo3.setText("path:" + clipPath);
                break;
            case 3:
                videoInfo4.setText("path:" + clipPath);
                /*addVideo4.setScaleType(CENTER_CROP);
                Glide.with(this)
                        .load(ThumbnailUtils.createVideoThumbnail(clipPath, MICRO_KIND))
                        .into(binding.addVideo4);*/
                break;
            default:
                return;
        }
    }
}
