package zippler.cn.xs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import zippler.cn.xs.R;
import zippler.cn.xs.util.ActivityCollection;
import zippler.cn.xs.util.PxUtil;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollection.addActivity(this);
//        StateBarUtil.translucentStatusBar(this,true);//convert the status bar to transparency ...hide the title
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ActivityCollection.removeActivity(this)){
            Log.d("info",this.getClass().getSimpleName()+"destroyed...");
        }
    }

    /**
     * 发布toast
     * @param msg 信息
     */
    protected void toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
    }

    protected void toastView(String msg,int drawable){
        Toast customToast = new Toast(this.getApplicationContext());
        View customView = LayoutInflater.from(this).inflate(R.layout.toast_img,null);

        LinearLayout relativeLayout = customView.findViewById(R.id.toast_linear);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) PxUtil.dpToPx(this, 130), (int)PxUtil.dpToPx(this, 130));
        relativeLayout.setLayoutParams(layoutParams);


        ImageView img = customView.findViewById(R.id.img_correct);
        TextView tv = customView.findViewById(R.id.tips_right);
        img.setBackgroundResource(drawable);
        tv.setText(msg);
        customToast.setView(customView);
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setGravity(Gravity.CENTER,0,0);
        customToast.show();
    }
}
