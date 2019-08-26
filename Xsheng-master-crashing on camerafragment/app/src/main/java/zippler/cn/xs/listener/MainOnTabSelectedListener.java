package zippler.cn.xs.listener;

import android.content.Context;
import android.support.design.widget.TabLayout;

import zippler.cn.xs.R;
import zippler.cn.xs.component.NoPreloadViewPager;

/**
 * Created by Zipple on 2018/5/5.
 * Setting a listener for tab layout item
 */
public class MainOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
    private  Context context;
    private NoPreloadViewPager pager;
    private TabLayout tablayout;

    public MainOnTabSelectedListener(NoPreloadViewPager pager, TabLayout tablayout,Context context) {
        this.pager = pager;
        this.tablayout = tablayout;
        this.context = context;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
         switch(position){
             case 0:
                 tab.setIcon(R.mipmap.refresh1);
                 tablayout.setBackgroundColor(context.getResources().getColor(R.color.colorDark));
                 break;
             case 1:
                 tab.setIcon(R.mipmap.camera1);
                 break;
             case 2:
                 tablayout.setBackgroundColor(context.getResources().getColor(R.color.colorDark2));
                 tab.setIcon(R.mipmap.person1);
                 break;
         }
        pager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        switch (tab.getPosition()){
            case 0:
                tab.setIcon(R.mipmap.refresh);
                tablayout.setBackground(context.getDrawable(R.drawable.tab_background));
                break;
            case 1:
                tab.setIcon(R.mipmap.camera);
                break;
            case 2:
                tablayout.setBackground(context.getDrawable(R.drawable.tab_background));
                tab.setIcon(R.mipmap.person);
                break;
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
           onTabSelected(tab);
    }
}
