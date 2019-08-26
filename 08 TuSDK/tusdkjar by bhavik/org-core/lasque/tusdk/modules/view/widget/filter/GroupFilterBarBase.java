package org.lasque.tusdk.modules.view.widget.filter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.activity.TuSdkFragment;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.seles.tusdk.FilterGroup;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage.FilterLocalPackageDelegate;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.task.FilterTaskInterface;
import org.lasque.tusdk.core.task.FiltersTempTask;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSdkSharedPreferences;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface;
import org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface.TuFilterOnlineFragmentDelegate;

public abstract class GroupFilterBarBase
  extends TuSdkRelativeLayout
  implements FilterLocalPackage.FilterLocalPackageDelegate, TuFilterOnlineFragmentInterface.TuFilterOnlineFragmentDelegate, GroupFilterBarInterface, GroupFilterGroupViewBase.GroupFilterGroupViewDelegate
{
  private GroupFilterItemViewInterface.GroupFilterAction a;
  private FilterOption b;
  private long c;
  private GroupFilterBarInterface.GroupFilterBarDelegate d;
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
  
  public GroupFilterBarBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public GroupFilterBarBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public GroupFilterBarBase(Context paramContext)
  {
    super(paramContext);
  }
  
  protected void initView()
  {
    super.initView();
    this.o = new FiltersTempTask();
  }
  
  public abstract <T extends View,  extends GroupFilterTableViewInterface> T getGroupTable();
  
  public abstract <T extends View,  extends GroupFilterTableViewInterface> T getFilterTable();
  
  public GroupFilterItemViewInterface.GroupFilterAction getAction()
  {
    return this.a;
  }
  
  public void setAction(GroupFilterItemViewInterface.GroupFilterAction paramGroupFilterAction)
  {
    this.a = paramGroupFilterAction;
    if (getGroupTable() != null)
    {
      ((GroupFilterTableViewInterface)getGroupTable()).setAction(paramGroupFilterAction);
      ((GroupFilterTableViewInterface)getGroupTable()).setFilterTask(this.o);
    }
    if (getFilterTable() != null)
    {
      ((GroupFilterTableViewInterface)getFilterTable()).setAction(paramGroupFilterAction);
      ((GroupFilterTableViewInterface)getFilterTable()).setFilterTask(this.o);
    }
  }
  
  public boolean isSaveLastFilter()
  {
    return this.f;
  }
  
  public void setSaveLastFilter(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
  
  public boolean isAutoSelectGroupDefaultFilter()
  {
    return this.h;
  }
  
  public void setAutoSelectGroupDefaultFilter(boolean paramBoolean)
  {
    this.h = paramBoolean;
  }
  
  public GroupFilterBarInterface.GroupFilterBarDelegate getDelegate()
  {
    return this.d;
  }
  
  public void setDelegate(GroupFilterBarInterface.GroupFilterBarDelegate paramGroupFilterBarDelegate)
  {
    this.d = paramGroupFilterBarDelegate;
  }
  
  protected void notifyDelegate(FilterOption paramFilterOption)
  {
    if (paramFilterOption == null) {
      return;
    }
    notifyDelegate(GroupFilterItem.createWithFilter(paramFilterOption), null);
  }
  
  protected boolean notifyDelegate(GroupFilterItem paramGroupFilterItem, GroupFilterItemViewBase paramGroupFilterItemViewBase)
  {
    if (this.d == null) {
      return true;
    }
    return this.d.onGroupFilterSelected(this, paramGroupFilterItemViewBase, paramGroupFilterItem);
  }
  
  public void setFilterGroup(List<String> paramList)
  {
    this.e = paramList;
    this.o.setFilerNames(paramList);
  }
  
  public void setThumbImage(Bitmap paramBitmap)
  {
    this.o.setInputImage(paramBitmap);
  }
  
  public boolean isEnableFilterConfig()
  {
    return this.i;
  }
  
  public void setEnableFilterConfig(boolean paramBoolean)
  {
    this.i = paramBoolean;
    if (getGroupTable() != null) {
      ((GroupFilterTableViewInterface)getGroupTable()).setDisplaySelectionIcon(paramBoolean);
    }
    if (getFilterTable() != null) {
      ((GroupFilterTableViewInterface)getFilterTable()).setDisplaySelectionIcon(paramBoolean);
    }
  }
  
  public boolean isEnableNormalFilter()
  {
    return this.j;
  }
  
  public void setEnableNormalFilter(boolean paramBoolean)
  {
    this.j = paramBoolean;
  }
  
  public final boolean isEnableOnlineFilter()
  {
    return this.k;
  }
  
  public final void setEnableOnlineFilter(boolean paramBoolean)
  {
    this.k = paramBoolean;
    if (getGroupTable() == null) {
      return;
    }
    ((GroupFilterTableViewInterface)getGroupTable()).setGroupDelegate(paramBoolean ? this : null);
  }
  
  public boolean isEnableHistory()
  {
    return this.l;
  }
  
  public void setEnableHistory(boolean paramBoolean)
  {
    this.l = paramBoolean;
  }
  
  public Class<?> getOnlineFragmentClazz()
  {
    return this.r;
  }
  
  public void setOnlineFragmentClazz(Class<?> paramClass)
  {
    this.r = paramClass;
  }
  
  public boolean isLoadFilters()
  {
    return this.p;
  }
  
  public boolean isRenderFilterThumb()
  {
    return this.o.isRenderFilterThumb();
  }
  
  public void setRenderFilterThumb(boolean paramBoolean)
  {
    this.o.setRenderFilterThumb(paramBoolean);
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.t = null;
    FilterLocalPackage.shared().removeDelegate(this);
    this.o.resetQueues();
  }
  
  public void loadFilters(FilterOption paramFilterOption)
  {
    this.b = paramFilterOption;
    this.g = (paramFilterOption != null);
    loadFilters();
  }
  
  public void loadFilters()
  {
    e();
    this.q = ((this.e == null) || (this.e.isEmpty()));
    if (this.q)
    {
      a();
      FilterLocalPackage.shared().appenDelegate(this);
    }
    else
    {
      c();
    }
    this.p = true;
  }
  
  private void a()
  {
    GroupFilterTableViewInterface localGroupFilterTableViewInterface = (GroupFilterTableViewInterface)getGroupTable();
    if (localGroupFilterTableViewInterface == null) {
      return;
    }
    int[] arrayOfInt = new int[1];
    List localList = a(arrayOfInt);
    localGroupFilterTableViewInterface.setModeList(localList);
    localGroupFilterTableViewInterface.setSelectedPosition(arrayOfInt[0]);
    localGroupFilterTableViewInterface.scrollToPosition(arrayOfInt[0] - 2);
  }
  
  private List<GroupFilterItem> a(int[] paramArrayOfInt)
  {
    List localList = b();
    FilterOption localFilterOption = null;
    if (this.g) {
      localFilterOption = this.b;
    } else if (this.f) {
      localFilterOption = d();
    }
    int i1 = -1;
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      GroupFilterItem localGroupFilterItem = (GroupFilterItem)localIterator.next();
      i1++;
      if (localGroupFilterItem.type == GroupFilterItem.GroupFilterItemType.TypeFilter) {
        paramArrayOfInt[0] = i1;
      } else if ((localFilterOption != null) && (localGroupFilterItem.filterGroup != null) && (localGroupFilterItem.type == GroupFilterItem.GroupFilterItemType.TypeGroup)) {
        if (localGroupFilterItem.filterGroup.groupId == localFilterOption.groupId)
        {
          paramArrayOfInt[0] = i1;
          this.b = localFilterOption;
          if (this.f) {
            notifyDelegate(localFilterOption);
          }
          a(localFilterOption);
        }
      }
    }
    return localList;
  }
  
  private List<GroupFilterItem> b()
  {
    ArrayList localArrayList = new ArrayList();
    if (isEnableHistory()) {
      localArrayList.add(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeHistory));
    }
    if (isEnableNormalFilter()) {
      localArrayList.add(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeFilter));
    }
    List localList = FilterLocalPackage.shared().getGroupsByAtionScen(1);
    if (localList != null)
    {
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        FilterGroup localFilterGroup = (FilterGroup)localIterator.next();
        if ((!localFilterGroup.disableRuntime) || (getAction() != GroupFilterItemViewInterface.GroupFilterAction.ActionCamera))
        {
          localArrayList.add(GroupFilterItem.createWithGroup(localFilterGroup));
          this.o.appendFilterCode(FilterLocalPackage.shared().getGroupDefaultFilterCode(localFilterGroup));
        }
      }
    }
    this.o.start();
    if (isEnableOnlineFilter()) {
      localArrayList.add(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeOnline));
    }
    return localArrayList;
  }
  
  protected void onGroupItemSeleced(GroupFilterItem paramGroupFilterItem, GroupFilterItemViewBase paramGroupFilterItemViewBase, int paramInt)
  {
    if (getFilterTable() == null) {
      return;
    }
    if (paramGroupFilterItem.isInActingType)
    {
      exitRemoveState();
      return;
    }
    boolean bool = notifyDelegate(paramGroupFilterItem, paramGroupFilterItemViewBase);
    if (paramGroupFilterItem.type == GroupFilterItem.GroupFilterItemType.TypeFilter) {
      a(paramGroupFilterItem, paramGroupFilterItemViewBase, paramInt);
    }
    if (!bool) {
      return;
    }
    switch (3.a[paramGroupFilterItem.type.ordinal()])
    {
    case 1: 
    case 2: 
      b(paramGroupFilterItem, paramGroupFilterItemViewBase, paramInt);
      break;
    case 3: 
      f();
      break;
    }
  }
  
  private void a(GroupFilterItem paramGroupFilterItem, GroupFilterItemViewBase paramGroupFilterItemViewBase, int paramInt)
  {
    if (!paramGroupFilterItemViewBase.isSelected()) {
      ((GroupFilterTableViewInterface)getGroupTable()).changeSelectedPosition(paramInt);
    }
    this.b = null;
    a(null);
  }
  
  private void b(GroupFilterItem paramGroupFilterItem, GroupFilterItemViewBase paramGroupFilterItemViewBase, int paramInt)
  {
    int i1 = 0;
    this.s = 0;
    if (paramGroupFilterItemViewBase != null)
    {
      TuSdkSize localTuSdkSize = TuSdkContext.getScreenSize();
      i1 = (localTuSdkSize.width - paramGroupFilterItemViewBase.getWidth()) / 2;
      this.s = (TuSdkViewHelper.locationInWindowLeft(paramGroupFilterItemViewBase) - i1);
    }
    a(paramGroupFilterItem, paramGroupFilterItemViewBase, paramInt, i1);
    showFilterTable(this.s, true);
  }
  
  protected void handleBackAction()
  {
    showFilterTable(this.s, false);
  }
  
  private void a(GroupFilterItem paramGroupFilterItem, GroupFilterItemViewBase paramGroupFilterItemViewBase, int paramInt1, int paramInt2)
  {
    if ((paramGroupFilterItemViewBase != null) && (paramGroupFilterItemViewBase.isSelected()) && (this.b == null)) {
      return;
    }
    ((GroupFilterTableViewInterface)getGroupTable()).changeSelectedPosition(paramInt1);
    List localList = null;
    long l1 = -1L;
    if (paramGroupFilterItem.type == GroupFilterItem.GroupFilterItemType.TypeHistory)
    {
      localList = FilterLocalPackage.shared().getFilters(e());
    }
    else if (paramGroupFilterItem.type == GroupFilterItem.GroupFilterItemType.TypeGroup)
    {
      localList = FilterLocalPackage.shared().getGroupFilters(paramGroupFilterItem.filterGroup);
      l1 = paramGroupFilterItem.filterGroup.defaultFilterId;
    }
    if (localList == null) {
      return;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeHolder));
    boolean bool = false;
    if (this.b != null)
    {
      l1 = this.b.id;
      bool = this.g;
      this.b = null;
    }
    else if (l1 > -1L)
    {
      bool = this.h;
    }
    int i1 = 0;
    int i2 = 0;
    Object localObject = null;
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      FilterOption localFilterOption = (FilterOption)localIterator.next();
      if ((!localFilterOption.disableRuntime) || (getAction() != GroupFilterItemViewInterface.GroupFilterAction.ActionCamera))
      {
        if (localFilterOption.id == this.c) {
          i2 = localArrayList.size();
        }
        if (localFilterOption.id == l1)
        {
          i1 = localArrayList.size();
          localObject = localFilterOption;
        }
        localArrayList.add(GroupFilterItem.createWithFilter(localFilterOption));
        this.o.appendFilterCode(localFilterOption.code);
      }
    }
    this.o.start();
    if (i2 > 0)
    {
      bool = true;
      i1 = i2;
    }
    else if (localObject != null)
    {
      if ((this.f) || (this.h)) {
        notifyDelegate((FilterOption)localObject);
      }
      a((FilterOption)localObject);
    }
    ((GroupFilterTableViewInterface)getFilterTable()).setModeList(localArrayList);
    if ((this.f) || (this.h) || (bool))
    {
      ((GroupFilterTableViewInterface)getFilterTable()).setSelectedPosition(i1);
      ((GroupFilterTableViewInterface)getFilterTable()).scrollToPositionWithOffset(i1, paramInt2);
    }
    else
    {
      ((GroupFilterTableViewInterface)getFilterTable()).setSelectedPosition(-1);
      ((GroupFilterTableViewInterface)getFilterTable()).scrollToPosition(0);
    }
  }
  
  private void c()
  {
    showViewIn(getGroupTable(), false);
    if (getFilterTable() == null) {
      return;
    }
    List localList = FilterLocalPackage.shared().getFilters(this.e);
    if ((localList == null) || (localList.isEmpty())) {
      return;
    }
    showViewIn(getFilterTable(), true);
    ArrayList localArrayList = new ArrayList();
    int i1 = localArrayList.size();
    FilterOption localFilterOption1 = null;
    if (this.g) {
      localFilterOption1 = this.b;
    } else {
      localFilterOption1 = d();
    }
    if (isEnableNormalFilter()) {
      localArrayList.add(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeFilter));
    }
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      FilterOption localFilterOption2 = (FilterOption)localIterator.next();
      if ((!localFilterOption2.disableRuntime) || (getAction() != GroupFilterItemViewInterface.GroupFilterAction.ActionCamera))
      {
        FilterGroup localFilterGroup = FilterLocalPackage.shared().getFilterGroup(localFilterOption2.groupId);
        if (localFilterGroup.canUseForAtionScenType(1))
        {
          if ((localFilterOption1 != null) && (localFilterOption2.id == localFilterOption1.id))
          {
            i1 = localArrayList.size();
            if (this.f) {
              notifyDelegate(localFilterOption2);
            }
            a(localFilterOption2);
          }
          localArrayList.add(GroupFilterItem.createWithFilter(localFilterOption2));
        }
      }
    }
    this.o.start();
    ((GroupFilterTableViewInterface)getFilterTable()).setModeList(localArrayList);
    ((GroupFilterTableViewInterface)getFilterTable()).setSelectedPosition(i1);
    ((GroupFilterTableViewInterface)getFilterTable()).scrollToPosition(i1 - 2);
  }
  
  protected void onFilterItemSeleced(GroupFilterItem paramGroupFilterItem, GroupFilterItemViewBase paramGroupFilterItemViewBase, int paramInt)
  {
    if (!notifyDelegate(paramGroupFilterItem, paramGroupFilterItemViewBase)) {
      return;
    }
    if (!paramGroupFilterItemViewBase.isSelected())
    {
      ((GroupFilterTableViewInterface)getFilterTable()).changeSelectedPosition(paramInt);
      ((GroupFilterTableViewInterface)getFilterTable()).smoothScrollByCenter(paramGroupFilterItemViewBase);
    }
    a(paramGroupFilterItem.filterOption);
    b(paramGroupFilterItem.filterOption);
  }
  
  protected void showFilterTable(int paramInt, boolean paramBoolean)
  {
    showViewIn(getFilterTable(), true);
    float f1 = 1.0F;
    int i1 = 0;
    Object localObject = null;
    ViewPropertyAnimatorListenerAdapter local1 = null;
    if (paramBoolean)
    {
      ViewCompat.setTranslationX(getFilterTable(), paramInt);
      ViewCompat.setScaleX(getFilterTable(), 0.0F);
      f1 = 0.0F;
      localObject = new OvershootInterpolator(1.0F);
    }
    else
    {
      localObject = new AnticipateInterpolator(1.0F);
      i1 = paramInt;
      local1 = new ViewPropertyAnimatorListenerAdapter()
      {
        public void onAnimationEnd(View paramAnonymousView)
        {
          paramAnonymousView.setVisibility(4);
        }
      };
    }
    ViewCompat.animate(getGroupTable()).alpha(f1).setDuration(50L);
    ViewCompat.animate(getFilterTable()).scaleX(1.0F - f1).translationX(i1).setDuration(220L).setInterpolator((Interpolator)localObject).setListener(local1);
  }
  
  private FilterOption d()
  {
    if (!this.f) {
      return FilterLocalPackage.shared().option(null);
    }
    String str1 = String.format("lsq_lastfilter_%s", new Object[] { this.a });
    String str2 = TuSdkContext.sharedPreferences().loadSharedCache(str1);
    FilterOption localFilterOption = FilterLocalPackage.shared().option(str2);
    return localFilterOption;
  }
  
  private void a(FilterOption paramFilterOption)
  {
    this.c = 0L;
    String str1 = null;
    if (paramFilterOption != null)
    {
      str1 = paramFilterOption.code;
      this.c = paramFilterOption.id;
    }
    if (!this.f) {
      return;
    }
    String str2 = String.format("lsq_lastfilter_%s", new Object[] { this.a });
    TuSdkContext.sharedPreferences().saveSharedCache(str2, str1);
  }
  
  private void b(FilterOption paramFilterOption)
  {
    String str1 = null;
    if (paramFilterOption != null) {
      str1 = paramFilterOption.code;
    }
    if ((StringHelper.isBlank(str1)) || (!this.l)) {
      return;
    }
    if (this.n == null) {
      this.n = new ArrayList(this.m);
    }
    this.n.remove(str1);
    this.n.add(0, str1);
    if (this.n.size() > this.m) {
      this.n = new ArrayList(this.n.subList(0, this.m));
    }
    String str2 = String.format("lsq_filter_history_%s", new Object[] { this.a });
    TuSdkContext.sharedPreferences().saveSharedCacheObject(str2, this.n);
  }
  
  private List<String> e()
  {
    if (!this.l) {
      return null;
    }
    if (this.n != null) {
      return this.n;
    }
    String str = String.format("lsq_filter_history_%s", new Object[] { this.a });
    this.n = ((List)TuSdkContext.sharedPreferences().loadSharedCacheObject(str));
    if (this.n == null) {
      this.n = new ArrayList(this.m);
    }
    return this.n;
  }
  
  public void setActivity(Activity paramActivity)
  {
    if (paramActivity == null) {
      return;
    }
    this.t = new TuSdkHelperComponent(paramActivity);
  }
  
  private void f()
  {
    if ((this.t == null) || (this.r == null))
    {
      TLog.w("You need set OnlineFragmentClazz: %s", new Object[] { getClass() });
      return;
    }
    if ((!TuFilterOnlineFragmentInterface.class.isAssignableFrom(this.r)) || (!Fragment.class.isAssignableFrom(this.r)))
    {
      TLog.w("You set OnlineFragmentClazz(%s) is not allow TuFilterOnlineFragmentInterface(%s) or Fragment(%s) in %s", new Object[] { this.r, Boolean.valueOf(TuFilterOnlineFragmentInterface.class.isAssignableFrom(this.r)), Boolean.valueOf(Fragment.class.isAssignableFrom(this.r)), getClass() });
      return;
    }
    TuFilterOnlineFragmentInterface localTuFilterOnlineFragmentInterface = (TuFilterOnlineFragmentInterface)ReflectUtils.classInstance(this.r);
    if (localTuFilterOnlineFragmentInterface == null) {
      return;
    }
    localTuFilterOnlineFragmentInterface.setAction(getAction());
    localTuFilterOnlineFragmentInterface.setDelegate(this);
    this.t.presentModalNavigationActivity((Fragment)localTuFilterOnlineFragmentInterface);
  }
  
  public void onFilterPackageStatusChanged(FilterLocalPackage paramFilterLocalPackage, TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus)
  {
    if ((paramTuSdkDownloadItem == null) || (paramDownloadTaskStatus == null) || (this.e != null)) {
      return;
    }
    switch (3.b[paramDownloadTaskStatus.ordinal()])
    {
    case 1: 
      a(paramTuSdkDownloadItem);
      break;
    case 2: 
      b(paramTuSdkDownloadItem);
      break;
    }
  }
  
  private void a(TuSdkDownloadItem paramTuSdkDownloadItem)
  {
    GroupFilterTableViewInterface localGroupFilterTableViewInterface = (GroupFilterTableViewInterface)getGroupTable();
    if (localGroupFilterTableViewInterface == null) {
      return;
    }
    List localList = b();
    int i1 = a(localList, paramTuSdkDownloadItem.id);
    if (i1 < 0)
    {
      String str = FilterLocalPackage.shared().getGroupNameKey(paramTuSdkDownloadItem.id);
      TLog.w("This filter group [ %s ] could not be used in Camere component", new Object[] { str });
      return;
    }
    localGroupFilterTableViewInterface.setModeList(localList);
    localGroupFilterTableViewInterface.getAdapter().notifyItemInserted(i1);
  }
  
  private void b(TuSdkDownloadItem paramTuSdkDownloadItem)
  {
    GroupFilterTableViewInterface localGroupFilterTableViewInterface = (GroupFilterTableViewInterface)getGroupTable();
    if (localGroupFilterTableViewInterface == null) {
      return;
    }
    List localList = g();
    int i1 = a(localGroupFilterTableViewInterface.getModeList(), paramTuSdkDownloadItem.id);
    int i2 = localGroupFilterTableViewInterface.getSelectedPosition();
    localGroupFilterTableViewInterface.setModeList(localList);
    localGroupFilterTableViewInterface.getAdapter().notifyItemRemoved(i1);
    if ((i2 == -1) || (i2 == i1))
    {
      i1 = a(localList);
      this.b = null;
      notifyDelegate(GroupFilterItem.create(GroupFilterItem.GroupFilterItemType.TypeFilter), null);
      a(null);
    }
    else if (i2 > i1)
    {
      i1 = i2 - 1;
    }
    else
    {
      i1 = i2;
    }
    if (i1 > -1) {
      localGroupFilterTableViewInterface.setSelectedPosition(i1, false);
    }
  }
  
  private int a(List<GroupFilterItem> paramList, long paramLong)
  {
    if (paramList == null) {
      return -1;
    }
    int i1 = -1;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      GroupFilterItem localGroupFilterItem = (GroupFilterItem)localIterator.next();
      i1++;
      if ((localGroupFilterItem.filterGroup != null) && (localGroupFilterItem.filterGroup.groupId == paramLong)) {
        return i1;
      }
    }
    return -1;
  }
  
  private int a(List<GroupFilterItem> paramList)
  {
    int i1 = -1;
    if (paramList == null) {
      return i1;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      GroupFilterItem localGroupFilterItem = (GroupFilterItem)localIterator.next();
      i1++;
      if (localGroupFilterItem.type == GroupFilterItem.GroupFilterItemType.TypeFilter) {
        return i1;
      }
    }
    return i1;
  }
  
  private List<GroupFilterItem> g()
  {
    List localList = b();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      GroupFilterItem localGroupFilterItem = (GroupFilterItem)localIterator.next();
      localGroupFilterItem.isInActingType = true;
    }
    return localList;
  }
  
  public void onGroupFilterGroupViewLongClick(GroupFilterGroupViewBase paramGroupFilterGroupViewBase)
  {
    a(true);
  }
  
  public void onGroupFilterGroupViewRemove(GroupFilterGroupViewBase paramGroupFilterGroupViewBase)
  {
    if ((paramGroupFilterGroupViewBase == null) || (paramGroupFilterGroupViewBase.getModel() == null) || (((GroupFilterItem)paramGroupFilterGroupViewBase.getModel()).filterGroup == null)) {
      return;
    }
    FilterLocalPackage.shared().removeDownloadWithIdt(((GroupFilterItem)paramGroupFilterGroupViewBase.getModel()).filterGroup.groupId);
  }
  
  public void exitRemoveState()
  {
    a(false);
  }
  
  private void a(boolean paramBoolean)
  {
    GroupFilterTableViewInterface localGroupFilterTableViewInterface = (GroupFilterTableViewInterface)getGroupTable();
    if ((localGroupFilterTableViewInterface == null) || (localGroupFilterTableViewInterface.getModeList() == null)) {
      return;
    }
    Iterator localIterator = localGroupFilterTableViewInterface.getModeList().iterator();
    while (localIterator.hasNext())
    {
      GroupFilterItem localGroupFilterItem = (GroupFilterItem)localIterator.next();
      localGroupFilterItem.isInActingType = paramBoolean;
    }
    localGroupFilterTableViewInterface.reloadData();
  }
  
  public void onTuFilterOnlineFragmentSelected(TuFilterOnlineFragmentInterface paramTuFilterOnlineFragmentInterface, final long paramLong)
  {
    if ((paramTuFilterOnlineFragmentInterface != null) && ((paramTuFilterOnlineFragmentInterface instanceof TuSdkFragment))) {
      ((TuSdkFragment)paramTuFilterOnlineFragmentInterface).dismissActivityWithAnim();
    }
    getHandler().postDelayed(new Runnable()
    {
      public void run()
      {
        GroupFilterBarBase.a(GroupFilterBarBase.this, paramLong);
      }
    }, 10L);
  }
  
  private void a(long paramLong)
  {
    if ((paramLong <= 0L) || (getGroupTable() == null)) {
      return;
    }
    List localList = ((GroupFilterTableViewInterface)getGroupTable()).getModeList();
    if ((localList == null) || (localList.isEmpty())) {
      return;
    }
    int i1 = a(localList, paramLong);
    if (i1 == -1) {
      return;
    }
    GroupFilterItem localGroupFilterItem = (GroupFilterItem)localList.get(i1);
    onGroupItemSeleced(localGroupFilterItem, null, i1);
    ((GroupFilterTableViewInterface)getGroupTable()).scrollToPosition(i1 - 2);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\GroupFilterBarBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */