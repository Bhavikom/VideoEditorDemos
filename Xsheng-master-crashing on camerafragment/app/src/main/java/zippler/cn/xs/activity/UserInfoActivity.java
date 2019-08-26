package zippler.cn.xs.activity;

import android.os.Bundle;

import zippler.cn.xs.R;

public class UserInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }
    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.acticity_open_anim,R.anim.acticity_close_anim);
    }
}
