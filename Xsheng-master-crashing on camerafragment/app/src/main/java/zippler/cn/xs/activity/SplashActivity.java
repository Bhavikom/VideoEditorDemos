package zippler.cn.xs.activity;

import android.content.Intent;
import android.os.Bundle;

/**
 * This activity is used for splash_bg screen
 * to show some information about the xs app
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*try {
            Thread.sleep(500);//loading 500 ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        startActivity(new Intent(this, GuideActivity.class));
        finish();
    }

}
