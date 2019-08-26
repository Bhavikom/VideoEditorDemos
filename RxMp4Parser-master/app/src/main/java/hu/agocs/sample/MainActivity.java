package hu.agocs.sample;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import hu.agocs.rxmp4parser.RxMp4Parser;
import hu.agocs.rxmp4parser.operators.CropMovie;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final File f = new File(getCacheDir() + "/sample.mp4");
        if (!f.exists())
            try {

                InputStream is = getAssets().open("sample.mp4");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();


                FileOutputStream fos = new FileOutputStream(f);
                fos.write(buffer);
                fos.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        File output = new File(Environment.getExternalStorageDirectory() + "/temp.mp4");

        RxMp4Parser.concatenateInto(
                //The output, where should be stored the resulting Movie object
                output,
                //A full video
                RxMp4Parser.from(f),
                //Cropped video
                RxMp4Parser.crop(f, 8.5f, 13f),
                //Cropped video
                RxMp4Parser.crop(f.getAbsolutePath(), 5, 10),
                //Another full video
                RxMp4Parser.from(f.getAbsolutePath())
                        .lift(new CropMovie(18f, 20f))
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        if (file.exists()) {
                            Toast.makeText(MainActivity.this, "Test successful: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                            MediaScannerConnection.scanFile(MainActivity.this, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                                @Override
                                public void onMediaScannerConnected() {

                                }

                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Scan complete", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "Test failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MainActivity.this, "Test failed! " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
