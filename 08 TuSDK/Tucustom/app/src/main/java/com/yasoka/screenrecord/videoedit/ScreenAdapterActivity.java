package com.yasoka.screenrecord.videoedit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
/*import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;*/
/*
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;*/

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.TuSpecialScreenHelper;
import com.yasoka.screenrecord.videoedit.utils.AppManager;

//import org.lasque.tusdk.impl.TuSpecialScreenHelper;
//import org.lasque.tusdkvideodemo.utils.AppManager;

/**
 * @author xujie
 * @Date 2018/10/29
 */

public class ScreenAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppManager.getInstance().addActivity(this); //添加到栈中
        if(TuSpecialScreenHelper.isNotchScreen())
        {
            setTheme(android.R.style.Theme_NoTitleBar);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this); //从栈中移除
    }
}
