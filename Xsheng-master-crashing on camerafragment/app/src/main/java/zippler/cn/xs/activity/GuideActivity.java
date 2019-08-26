package zippler.cn.xs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import zippler.cn.xs.R;
import zippler.cn.xs.util.StateBarUtil;

/**
 * The app guidance page
 */
public class GuideActivity extends BaseActivity {
    private static final int DELAY = 1000;//delay 2 seconds
    private Handler x;
    private GuideActivity.SplashHandler splashHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StateBarUtil.translucentStatusBar(this,true);
        showSplash();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(x!=null&&splashHandler!=null){
            x.removeCallbacks(splashHandler);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(x!=null&&splashHandler!=null){
            x.postDelayed(splashHandler, DELAY);
        }
    }

    /**
     * It is used to jump pages
     */
    class SplashHandler implements Runnable{
        @Override
        public void run() {
            //Here we should determine whether the user is the first time to enter the program
            //GuideActivity
            Intent intent = new Intent(GuideActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void showSplash(){
        x = new Handler();
        splashHandler = new GuideActivity.SplashHandler();
    }
}
