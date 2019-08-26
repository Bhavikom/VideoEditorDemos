package zippler.cn.xs.activity;

import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import zippler.cn.xs.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.logo)
                .setDescription("Version 1.0")
                .addEmail("csu_zipple@gmail.com")
//                .addWebsite("http://www.zippler.cn/labor/shop/main.html")
                .addFacebook("csuZipple")
                .addTwitter("zipple")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("csuZipple")
                .create();
        setContentView(aboutPage);
    }
}
