package com.yasoka.screenrecord;

import android.content.Context;
import android.util.Log;

/*import com.crashlytics.android.Crashlytics;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;*/
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkApplication;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.monitor.TuSdkMonitor;
//import com.tencent.bugly.crashreport.CrashReport;

/*import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkApplication;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.monitor.TuSdkMonitor;*/

//import io.fabric.sdk.android.Fabric;


public class MyApplication extends TuSdkApplication {
    private static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        //Fabric.with(this, new Crashlytics());

        /*try {
            FFmpeg.getInstance((Context)this).loadBinary((FFmpegLoadBinaryResponseHandler)(new FFmpegLoadBinaryResponseHandler() {
                public void onFailure() {
                    Log.e("FFMpeg", "Failed to load FFMpeg library.");
                }

                public void onSuccess() {
                    Log.i("FFMpeg", "FFMpeg Library loaded!");
                }

                public void onStart() {
                    Log.i("FFMpeg", "FFMpeg Started");
                }

                public void onFinish() {
                    Log.i("FFMpeg", "FFMpeg Stopped");
                }
            }));
        } catch (FFmpegNotSupportedException var2) {
            var2.printStackTrace();
        } catch (Exception var3) {
            var3.printStackTrace();
        }*/

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        // initialization Bugly SDK
        CrashReport.initCrashReport(getApplicationContext(), "09e008786d", true);

        // Set the resource class. When the Application id is not the same as the Package Name, the method must be called manually and executed before init.
        TuSdk.setResourcePackageClazz(R.class);

        // Custom .so file path, called before init
        // NativeLibraryHelper.shared().mapLibrary(NativeLibType.LIB_CORE, "libtusdk-library.so 文件路径");
        // NativeLibraryHelper.shared().mapLibrary(NativeLibType.LIB_IMAGE, "libtusdk-image.so 文件路径");

        // To set the output status, it is recommended to enable this option during the access phase in order to locate the problem.
        this.setEnableLog(true);
        // Setting the Log Output as a Problem It is recommended to open it during the access phase or test phase to test the problem.
        TLog.enableLog2File(false);

        //
        // During the debugging integration test phase, there may be some problems that are difficult to locate.
        // Developers can open the following configuration, try to run on the real machine, reproduce the problem,
        // Then report the log/tusdk directory log in the sdcard root directory to the mapping developer so that we can better locate the problem.
        TuSdkMonitor.setEnableCheckGLError(false) // Enable GL log detection
                .setEnableCheckFrameImage(false); // Enable GL image frame detection

        /**
         *
         * Initialize the SDK, the application key is the unique identifier of your app in TuSDK.
         * Each application's package name (Bundle Identifier), key, resource package (filter, sticker, etc.)
         * need to match, otherwise it will report an error.
         *
         *  @param appkey Application key (please go to http://tusdk.com to apply for the key)
         */
           //this.initPreLoader(this.getApplicationContext(), "c863a73a6e0294bc-04-ewdjn1");
        this.initPreLoader(this.getApplicationContext(), "12aa4847a3a9ce68-04-ewdjn1", "debug");
    }
    public static MyApplication getInstance() {
        return myApplication;
    }
}
