package zippler.cn.xs.listener;

import android.support.design.widget.TabLayout;

import zippler.cn.xs.component.NoPreloadViewPager;

/**
 * Created by Zipple on 2018/5/5.
 * Setting a listener for view page changed
 */
public class MainOnPageChangeListener implements NoPreloadViewPager.OnPageChangeListener {

    private TabLayout tabLayout;
    public MainOnPageChangeListener(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
