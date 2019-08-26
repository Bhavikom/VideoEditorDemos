// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.filter;

//import org.lasque.tusdk.core.activity.TuSdkFragment;
//import org.lasque.tusdk.core.type.DownloadTaskStatus;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
//import org.lasque.tusdk.core.utils.ReflectUtils;
import android.support.v4.app.Fragment;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.StringHelper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Interpolator;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.support.v4.view.ViewCompat;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.seles.tusdk.FilterGroup;
import java.util.ArrayList;
import java.util.Iterator;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.task.FiltersTempTask;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
//import org.lasque.tusdk.core.task.FilterTaskInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity.TuSdkFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.FilterTaskInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.FiltersTempTask;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.TuSdkHelperComponent;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface;

import java.util.List;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;


public abstract class GroupFilterBarBase extends TuSdkRelativeLayout implements FilterLocalPackage.FilterLocalPackageDelegate, TuFilterOnlineFragmentInterface.TuFilterOnlineFragmentDelegate, GroupFilterBarInterface, GroupFilterGroupViewBase.GroupFilterGroupViewDelegate {
    private GroupFilterItemViewInterface.GroupFilterAction a;
    private FilterOption b;
    private long c;
    private GroupFilterBarDelegate d;
    private List<String> e;
    private boolean f;
    private boolean g;
    private boolean h;
    private boolean i;
    private boolean j = true;
    private boolean k;
    private boolean l;
    private int m = 20;
    private List<String> n;
    private FilterTaskInterface o;
    private boolean p;
    private boolean q;
    private Class<?> r;
    private int s;
    private TuSdkHelperComponent t;

    public GroupFilterBarBase(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public GroupFilterBarBase(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public GroupFilterBarBase(Context var1) {
        super(var1);
    }

    protected void initView() {
        super.initView();
        this.o = new FiltersTempTask();
    }

    public abstract <T extends View & GroupFilterTableViewInterface> T getGroupTable();

    public abstract <T extends View & GroupFilterTableViewInterface> T getFilterTable();

    public GroupFilterItemViewInterface.GroupFilterAction getAction() {
        return this.a;
    }

    public void setAction(GroupFilterItemViewInterface.GroupFilterAction var1) {
        this.a = var1;
        if (this.getGroupTable() != null) {
            ((GroupFilterTableViewInterface)this.getGroupTable()).setAction(var1);
            ((GroupFilterTableViewInterface)this.getGroupTable()).setFilterTask(this.o);
        }

        if (this.getFilterTable() != null) {
            ((GroupFilterTableViewInterface)this.getFilterTable()).setAction(var1);
            ((GroupFilterTableViewInterface)this.getFilterTable()).setFilterTask(this.o);
        }

    }

    public boolean isSaveLastFilter() {
        return this.f;
    }

    public void setSaveLastFilter(boolean var1) {
        this.f = var1;
    }

    public boolean isAutoSelectGroupDefaultFilter() {
        return this.h;
    }

    public void setAutoSelectGroupDefaultFilter(boolean var1) {
        this.h = var1;
    }

    public GroupFilterBarDelegate getDelegate() {
        return this.d;
    }

    public void setDelegate(GroupFilterBarDelegate var1) {
        this.d = var1;
    }

    protected void notifyDelegate(FilterOption var1) {
        if (var1 != null) {
            this.notifyDelegate(GroupFilterItem.createWithFilter(var1), (GroupFilterItemViewBase)null);
        }
    }

    protected boolean notifyDelegate(GroupFilterItem var1, GroupFilterItemViewBase var2) {
        return this.d == null ? true : this.d.onGroupFilterSelected(this, var2, var1);
    }

    public void setFilterGroup(List<String> var1) {
        this.e = var1;
        this.o.setFilerNames(var1);
    }

    public void setThumbImage(Bitmap var1) {
        this.o.setInputImage(var1);
    }

    public boolean isEnableFilterConfig() {
        return this.i;
    }

    public void setEnableFilterConfig(boolean var1) {
        this.i = var1;
        if (this.getGroupTable() != null) {
            ((GroupFilterTableViewInterface)this.getGroupTable()).setDisplaySelectionIcon(var1);
        }

        if (this.getFilterTable() != null) {
            ((GroupFilterTableViewInterface)this.getFilterTable()).setDisplaySelectionIcon(var1);
        }

    }

    public boolean isEnableNormalFilter() {
        return this.j;
    }

    public void setEnableNormalFilter(boolean var1) {
        this.j = var1;
    }

    public final boolean isEnableOnlineFilter() {
        return this.k;
    }

    public final void setEnableOnlineFilter(boolean var1) {
        this.k = var1;
        if (this.getGroupTable() != null) {
            ((GroupFilterTableViewInterface)this.getGroupTable()).setGroupDelegate(var1 ? this : null);
        }
    }

    public boolean isEnableHistory() {
        return this.l;
    }

    public void setEnableHistory(boolean var1) {
        this.l = var1;
    }

    public Class<?> getOnlineFragmentClazz() {
        return this.r;
    }

    public void setOnlineFragmentClazz(Class<?> var1) {
        this.r = var1;
    }

    public boolean isLoadFilters() {
        return this.p;
    }

    public boolean isRenderFilterThumb() {
        return this.o.isRenderFilterThumb();
    }

    public void setRenderFilterThumb(boolean var1) {
        this.o.setRenderFilterThumb(var1);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.t = null;
        FilterLocalPackage.shared().removeDelegate(this);
        this.o.resetQueues();
    }

    public void loadFilters(FilterOption var1) {
        this.b = var1;
        this.g = var1 != null;
        this.loadFilters();
    }

    public void loadFilters() {
        this.e();
        this.q = this.e == null || this.e.isEmpty();
        if (this.q) {
            this.a();
            FilterLocalPackage.shared().appenDelegate(this);
        } else {
            this.c();
        }

        this.p = true;
    }

    private void a() {
        GroupFilterTableViewInterface var1 = (GroupFilterTableViewInterface)this.getGroupTable();
        if (var1 != null) {
            int[] var2 = new int[1];
            List var3 = this.a(var2);
            var1.setModeList(var3);
            var1.setSelectedPosition(var2[0]);
            var1.scrollToPosition(var2[0] - 2);
        }
    }

    private List<GroupFilterItem> a(int[] var1) {
        List var2 = this.b();
        FilterOption var3 = null;
        if (this.g) {
            var3 = this.b;
        } else if (this.f) {
            var3 = this.d();
        }

        int var4 = -1;
        Iterator var5 = var2.iterator();

        while(var5.hasNext()) {
            GroupFilterItem var6 = (GroupFilterItem)var5.next();
            ++var4;
            if (var6.type == GroupFilterItem.GroupFilterItemType.TypeFilter) {
                var1[0] = var4;
            } else if (var3 != null && var6.filterGroup != null && var6.type == GroupFilterItem.GroupFilterItemType.TypeGroup && var6.filterGroup.groupId == var3.groupId) {
                var1[0] = var4;
                this.b = var3;
                if (this.f) {
                    this.notifyDelegate(var3);
                }

                this.a(var3);
            }
        }

        return var2;
    }

    private List<GroupFilterItem> b() {
        ArrayList var1 = new ArrayList();
        if (this.isEnableHistory()) {
            var1.add(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeHistory));
        }

        if (this.isEnableNormalFilter()) {
            var1.add(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeFilter));
        }

        List var2 = FilterLocalPackage.shared().getGroupsByAtionScen(1);
        if (var2 != null) {
            Iterator var3 = var2.iterator();

            label30:
            while(true) {
                FilterGroup var4;
                do {
                    if (!var3.hasNext()) {
                        break label30;
                    }

                    var4 = (FilterGroup)var3.next();
                } while(var4.disableRuntime && this.getAction() == GroupFilterItemViewInterface.GroupFilterAction.ActionCamera);

                var1.add(GroupFilterItem.createWithGroup(var4));
                this.o.appendFilterCode(FilterLocalPackage.shared().getGroupDefaultFilterCode(var4));
            }
        }

        this.o.start();
        if (this.isEnableOnlineFilter()) {
            var1.add(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeOnline));
        }

        return var1;
    }

    protected void onGroupItemSeleced(GroupFilterItem var1, GroupFilterItemViewBase var2, int var3) {
        if (this.getFilterTable() != null) {
            if (var1.isInActingType) {
                this.exitRemoveState();
            } else {
                boolean var4 = this.notifyDelegate(var1, var2);
                if (var1.type == GroupFilterItem.GroupFilterItemType.TypeFilter) {
                    this.a(var1, var2, var3);
                }

                if (var4) {
                    switch(var1.type.ordinal()) {
                        case 1:
                        case 2:
                            this.b(var1, var2, var3);
                            break;
                        case 3:
                            this.f();
                    }

                }
            }
        }
    }

    private void a(GroupFilterItem var1, GroupFilterItemViewBase var2, int var3) {
        if (!var2.isSelected()) {
            ((GroupFilterTableViewInterface)this.getGroupTable()).changeSelectedPosition(var3);
        }

        this.b = null;
        this.a((FilterOption)null);
    }

    private void b(GroupFilterItem var1, GroupFilterItemViewBase var2, int var3) {
        int var4 = 0;
        this.s = 0;
        if (var2 != null) {
            TuSdkSize var5 = TuSdkContext.getScreenSize();
            var4 = (var5.width - var2.getWidth()) / 2;
            this.s = TuSdkViewHelper.locationInWindowLeft(var2) - var4;
        }

        this.a(var1, var2, var3, var4);
        this.showFilterTable(this.s, true);
    }

    protected void handleBackAction() {
        this.showFilterTable(this.s, false);
    }

    private void a(GroupFilterItem var1, GroupFilterItemViewBase var2, int var3, int var4) {
        if (var2 == null || !var2.isSelected() || this.b != null) {
            ((GroupFilterTableViewInterface)this.getGroupTable()).changeSelectedPosition(var3);
            List var5 = null;
            long var6 = -1L;
            if (var1.type == GroupFilterItem.GroupFilterItemType.TypeHistory) {
                var5 = FilterLocalPackage.shared().getFilters(this.e());
            } else if (var1.type == GroupFilterItem.GroupFilterItemType.TypeGroup) {
                var5 = FilterLocalPackage.shared().getGroupFilters(var1.filterGroup);
                var6 = var1.filterGroup.defaultFilterId;
            }

            if (var5 != null) {
                ArrayList var8 = new ArrayList();
                var8.add(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeHolder));
                boolean var9 = false;
                if (this.b != null) {
                    var6 = this.b.id;
                    var9 = this.g;
                    this.b = null;
                } else if (var6 > -1L) {
                    var9 = this.h;
                }

                int var10 = 0;
                int var11 = 0;
                FilterOption var12 = null;
                Iterator var13 = var5.iterator();

                while(true) {
                    FilterOption var14;
                    do {
                        if (!var13.hasNext()) {
                            this.o.start();
                            if (var11 > 0) {
                                var9 = true;
                                var10 = var11;
                            } else if (var12 != null) {
                                if (this.f || this.h) {
                                    this.notifyDelegate(var12);
                                }

                                this.a(var12);
                            }

                            ((GroupFilterTableViewInterface)this.getFilterTable()).setModeList(var8);
                            if (!this.f && !this.h && !var9) {
                                ((GroupFilterTableViewInterface)this.getFilterTable()).setSelectedPosition(-1);
                                ((GroupFilterTableViewInterface)this.getFilterTable()).scrollToPosition(0);
                            } else {
                                ((GroupFilterTableViewInterface)this.getFilterTable()).setSelectedPosition(var10);
                                ((GroupFilterTableViewInterface)this.getFilterTable()).scrollToPositionWithOffset(var10, var4);
                            }

                            return;
                        }

                        var14 = (FilterOption)var13.next();
                    } while(var14.disableRuntime && this.getAction() == GroupFilterItemViewInterface.GroupFilterAction.ActionCamera);

                    if (var14.id == this.c) {
                        var11 = var8.size();
                    }

                    if (var14.id == var6) {
                        var10 = var8.size();
                        var12 = var14;
                    }

                    var8.add(GroupFilterItem.createWithFilter(var14));
                    this.o.appendFilterCode(var14.code);
                }
            }
        }
    }

    private void c() {
        this.showViewIn(this.getGroupTable(), false);
        if (this.getFilterTable() != null) {
            List var1 = FilterLocalPackage.shared().getFilters(this.e);
            if (var1 != null && !var1.isEmpty()) {
                this.showViewIn(this.getFilterTable(), true);
                ArrayList var2 = new ArrayList();
                int var3 = var2.size();
                FilterOption var4 = null;
                if (this.g) {
                    var4 = this.b;
                } else {
                    var4 = this.d();
                }

                if (this.isEnableNormalFilter()) {
                    var2.add(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeFilter));
                }

                Iterator var5 = var1.iterator();

                while(true) {
                    FilterOption var6;
                    do {
                        if (!var5.hasNext()) {
                            this.o.start();
                            ((GroupFilterTableViewInterface)this.getFilterTable()).setModeList(var2);
                            ((GroupFilterTableViewInterface)this.getFilterTable()).setSelectedPosition(var3);
                            ((GroupFilterTableViewInterface)this.getFilterTable()).scrollToPosition(var3 - 2);
                            return;
                        }

                        var6 = (FilterOption)var5.next();
                    } while(var6.disableRuntime && this.getAction() == GroupFilterItemViewInterface.GroupFilterAction.ActionCamera);

                    FilterGroup var7 = FilterLocalPackage.shared().getFilterGroup(var6.groupId);
                    if (var7.canUseForAtionScenType(1)) {
                        if (var4 != null && var6.id == var4.id) {
                            var3 = var2.size();
                            if (this.f) {
                                this.notifyDelegate(var6);
                            }

                            this.a(var6);
                        }

                        var2.add(GroupFilterItem.createWithFilter(var6));
                    }
                }
            }
        }
    }

    protected void onFilterItemSeleced(GroupFilterItem var1, GroupFilterItemViewBase var2, int var3) {
        if (this.notifyDelegate(var1, var2)) {
            if (!var2.isSelected()) {
                ((GroupFilterTableViewInterface)this.getFilterTable()).changeSelectedPosition(var3);
                ((GroupFilterTableViewInterface)this.getFilterTable()).smoothScrollByCenter(var2);
            }

            this.a(var1.filterOption);
            this.b(var1.filterOption);
        }
    }

    protected void showFilterTable(int var1, boolean var2) {
        this.showViewIn(this.getFilterTable(), true);
        float var3 = 1.0F;
        int var4 = 0;
        Object var5 = null;
        ViewPropertyAnimatorListenerAdapter var6 = null;
        if (var2) {
            ViewCompat.setTranslationX(this.getFilterTable(), (float)var1);
            ViewCompat.setScaleX(this.getFilterTable(), 0.0F);
            var3 = 0.0F;
            var5 = new OvershootInterpolator(1.0F);
        } else {
            var5 = new AnticipateInterpolator(1.0F);
            var4 = var1;
            var6 = new ViewPropertyAnimatorListenerAdapter() {
                public void onAnimationEnd(View var1) {
                    var1.setVisibility(INVISIBLE);
                }
            };
        }

        ViewCompat.animate(this.getGroupTable()).alpha(var3).setDuration(50L);
        ViewCompat.animate(this.getFilterTable()).scaleX(1.0F - var3).translationX((float)var4).setDuration(220L).setInterpolator((Interpolator)var5).setListener(var6);
    }

    private FilterOption d() {
        if (!this.f) {
            return FilterLocalPackage.shared().option((String)null);
        } else {
            String var1 = String.format("lsq_lastfilter_%s", this.a);
            String var2 = TuSdkContext.sharedPreferences().loadSharedCache(var1);
            FilterOption var3 = FilterLocalPackage.shared().option(var2);
            return var3;
        }
    }

    private void a(FilterOption var1) {
        this.c = 0L;
        String var2 = null;
        if (var1 != null) {
            var2 = var1.code;
            this.c = var1.id;
        }

        if (this.f) {
            String var3 = String.format("lsq_lastfilter_%s", this.a);
            TuSdkContext.sharedPreferences().saveSharedCache(var3, var2);
        }
    }

    private void b(FilterOption var1) {
        String var2 = null;
        if (var1 != null) {
            var2 = var1.code;
        }

        if (!StringHelper.isBlank(var2) && this.l) {
            if (this.n == null) {
                this.n = new ArrayList(this.m);
            }

            this.n.remove(var2);
            this.n.add(0, var2);
            if (this.n.size() > this.m) {
                this.n = new ArrayList(this.n.subList(0, this.m));
            }

            String var3 = String.format("lsq_filter_history_%s", this.a);
            TuSdkContext.sharedPreferences().saveSharedCacheObject(var3, this.n);
        }
    }

    private List<String> e() {
        if (!this.l) {
            return null;
        } else if (this.n != null) {
            return this.n;
        } else {
            String var1 = String.format("lsq_filter_history_%s", this.a);
            this.n = (List)TuSdkContext.sharedPreferences().loadSharedCacheObject(var1);
            if (this.n == null) {
                this.n = new ArrayList(this.m);
            }

            return this.n;
        }
    }

    public void setActivity(AppCompatActivity var1) {
        if (var1 != null) {
            this.t = new TuSdkHelperComponent(var1);
        }
    }

    private void f() {
        if (this.t != null && this.r != null) {
            if (TuFilterOnlineFragmentInterface.class.isAssignableFrom(this.r) && Fragment.class.isAssignableFrom(this.r)) {
                TuFilterOnlineFragmentInterface var1 = (TuFilterOnlineFragmentInterface)ReflectUtils.classInstance(this.r);
                if (var1 != null) {
                    var1.setAction(this.getAction());
                    var1.setDelegate(this);
                    this.t.presentModalNavigationActivity((Fragment)var1);
                }
            } else {
                TLog.w("You set OnlineFragmentClazz(%s) is not allow TuFilterOnlineFragmentInterface(%s) or Fragment(%s) in %s", new Object[]{this.r, TuFilterOnlineFragmentInterface.class.isAssignableFrom(this.r), Fragment.class.isAssignableFrom(this.r), this.getClass()});
            }
        } else {
            TLog.w("You need set OnlineFragmentClazz: %s", new Object[]{this.getClass()});
        }
    }

    public void onFilterPackageStatusChanged(FilterLocalPackage var1, TuSdkDownloadItem var2, DownloadTaskStatus var3) {
        if (var2 != null && var3 != null && this.e == null) {
            switch(var3.ordinal()) {
                case 1:
                    this.a(var2);
                    break;
                case 2:
                    this.b(var2);
            }

        }
    }

    private void a(TuSdkDownloadItem var1) {
        GroupFilterTableViewInterface var2 = (GroupFilterTableViewInterface)this.getGroupTable();
        if (var2 != null) {
            List var3 = this.b();
            int var4 = this.a(var3, var1.id);
            if (var4 < 0) {
                String var5 = FilterLocalPackage.shared().getGroupNameKey(var1.id);
                TLog.w("This filter group [ %s ] could not be used in Camere component", new Object[]{var5});
            } else {
                var2.setModeList(var3);
                var2.getAdapter().notifyItemInserted(var4);
            }
        }
    }

    private void b(TuSdkDownloadItem var1) {
        GroupFilterTableViewInterface var2 = (GroupFilterTableViewInterface)this.getGroupTable();
        if (var2 != null) {
            List var3 = this.g();
            int var4 = this.a(var2.getModeList(), var1.id);
            int var5 = var2.getSelectedPosition();
            var2.setModeList(var3);
            var2.getAdapter().notifyItemRemoved(var4);
            if (var5 != -1 && var5 != var4) {
                if (var5 > var4) {
                    var4 = var5 - 1;
                } else {
                    var4 = var5;
                }
            } else {
                var4 = this.a(var3);
                this.b = null;
                this.notifyDelegate(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeFilter), (GroupFilterItemViewBase)null);
                this.a((FilterOption)null);
            }

            if (var4 > -1) {
                var2.setSelectedPosition(var4, false);
            }

        }
    }

    private int a(List<GroupFilterItem> var1, long var2) {
        if (var1 == null) {
            return -1;
        } else {
            int var4 = -1;
            Iterator var5 = var1.iterator();

            GroupFilterItem var6;
            do {
                if (!var5.hasNext()) {
                    return -1;
                }

                var6 = (GroupFilterItem)var5.next();
                ++var4;
            } while(var6.filterGroup == null || var6.filterGroup.groupId != var2);

            return var4;
        }
    }

    private int a(List<GroupFilterItem> var1) {
        int var2 = -1;
        if (var1 == null) {
            return var2;
        } else {
            Iterator var3 = var1.iterator();

            GroupFilterItem var4;
            do {
                if (!var3.hasNext()) {
                    return var2;
                }

                var4 = (GroupFilterItem)var3.next();
                ++var2;
            } while(var4.type != GroupFilterItem.GroupFilterItemType.TypeFilter);

            return var2;
        }
    }

    private List<GroupFilterItem> g() {
        List var1 = this.b();

        GroupFilterItem var3;
        for(Iterator var2 = var1.iterator(); var2.hasNext(); var3.isInActingType = true) {
            var3 = (GroupFilterItem)var2.next();
        }

        return var1;
    }

    public void onGroupFilterGroupViewLongClick(GroupFilterGroupViewBase var1) {
        this.a(true);
    }

    public void onGroupFilterGroupViewRemove(GroupFilterGroupViewBase var1) {
        if (var1 != null && var1.getModel() != null && ((GroupFilterItem)var1.getModel()).filterGroup != null) {
            FilterLocalPackage.shared().removeDownloadWithIdt(((GroupFilterItem)var1.getModel()).filterGroup.groupId);
        }
    }

    public void exitRemoveState() {
        this.a(false);
    }

    private void a(boolean var1) {
        GroupFilterTableViewInterface var2 = (GroupFilterTableViewInterface)this.getGroupTable();
        if (var2 != null && var2.getModeList() != null) {
            GroupFilterItem var4;
            for(Iterator var3 = var2.getModeList().iterator(); var3.hasNext(); var4.isInActingType = var1) {
                var4 = (GroupFilterItem)var3.next();
            }

            var2.reloadData();
        }
    }

    public void onTuFilterOnlineFragmentSelected(TuFilterOnlineFragmentInterface var1, final long var2) {
        if (var1 != null && var1 instanceof TuSdkFragment) {
            ((TuSdkFragment)var1).dismissActivityWithAnim();
        }

        this.getHandler().postDelayed(new Runnable() {
            public void run() {
                GroupFilterBarBase.this.a(var2);
            }
        }, 10L);
    }

    private void a(long var1) {
        if (var1 > 0L && this.getGroupTable() != null) {
            List var3 = ((GroupFilterTableViewInterface)this.getGroupTable()).getModeList();
            if (var3 != null && !var3.isEmpty()) {
                int var4 = this.a(var3, var1);
                if (var4 != -1) {
                    GroupFilterItem var5 = (GroupFilterItem)var3.get(var4);
                    this.onGroupItemSeleced(var5, (GroupFilterItemViewBase)null, var4);
                    ((GroupFilterTableViewInterface)this.getGroupTable()).scrollToPosition(var4 - 2);
                }
            }
        }
    }
}

