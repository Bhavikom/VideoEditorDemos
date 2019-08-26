package zippler.cn.xs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import zippler.cn.xs.fragment.UserFansFragment;
import zippler.cn.xs.fragment.UserFavoriteFragment;
import zippler.cn.xs.fragment.UserVideoFragment;

/**
 * Created by Zipple on 2018/5/14.
 */
public class UserViewPagerAdapter extends FragmentPagerAdapter {


    public UserViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new UserVideoFragment();
        }else if (position==1){
            return new UserFavoriteFragment();
        }else if (position==2){
            return new UserFansFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
