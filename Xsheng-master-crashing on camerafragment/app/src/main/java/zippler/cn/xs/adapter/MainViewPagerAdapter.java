package zippler.cn.xs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Zipple on 2018/5/5.
 * It is used to inject data in tab layout
 * Determined which page to select
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public MainViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }


}
