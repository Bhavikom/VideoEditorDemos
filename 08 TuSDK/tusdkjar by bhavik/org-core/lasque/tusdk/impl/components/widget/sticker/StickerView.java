package org.lasque.tusdk.impl.components.widget.sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.TuSdkSizeF;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.view.TuSdkImageView;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
import org.lasque.tusdk.core.view.TuSdkTextView;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.impl.view.widget.TuMessageHubInterface;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData.StickerType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerImageData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface;
import org.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface.StickerItemViewDelegate;
import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
import org.lasque.tusdk.modules.view.widget.sticker.StickerResult;

public final class StickerView
  extends TuSdkRelativeLayout
  implements StickerItemViewInterface.StickerItemViewDelegate
{
  private StickerViewDelegate a;
  private int b;
  private LinkedList<StickerItemViewInterface> c = new LinkedList();
  private StickerItemViewInterface d;
  private boolean e;
  private StickerType f = StickerType.Normal;
  
  public StickerView(Context paramContext)
  {
    super(paramContext);
  }
  
  public StickerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public StickerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public StickerViewDelegate getDelegate()
  {
    return this.a;
  }
  
  public void setDelegate(StickerViewDelegate paramStickerViewDelegate)
  {
    this.a = paramStickerViewDelegate;
  }
  
  public StickerItemViewInterface getCurrentItemViewSelected()
  {
    return this.d;
  }
  
  public void changeOrUpdateStickerType(StickerType paramStickerType)
  {
    this.f = paramStickerType;
    Iterator localIterator = this.c.iterator();
    while (localIterator.hasNext())
    {
      StickerItemViewInterface localStickerItemViewInterface = (StickerItemViewInterface)localIterator.next();
      localStickerItemViewInterface.setStickerViewType(paramStickerType);
    }
  }
  
  private int a(StickerData.StickerType paramStickerType)
  {
    if (paramStickerType == StickerData.StickerType.TypeImage) {
      this.b = StickerImageItemView.getLayoutId();
    } else if (paramStickerType == StickerData.StickerType.TypeText) {
      this.b = StickerTextItemView.getLayoutId();
    }
    return this.b;
  }
  
  public int stickersCount()
  {
    return this.c.size();
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.a = null;
  }
  
  public void updateText(String paramString, boolean paramBoolean)
  {
    if (this.d == null) {
      return;
    }
    ((StickerTextItemView)this.d).updateText(paramString, paramBoolean);
  }
  
  public void onSelectedColorChanged(int paramInt, String paramString)
  {
    if (this.d == null) {
      return;
    }
    ((StickerTextItemView)this.d).onSelectedColorChanged(paramInt, paramString);
  }
  
  public void toggleTextUnderlineStyle()
  {
    if (this.d == null) {
      return;
    }
    ((StickerTextItemView)this.d).toggleTextUnderlineStyle();
  }
  
  public void toggleTextReverse()
  {
    if (this.d == null) {
      return;
    }
    ((StickerTextItemView)this.d).toggleTextReverse();
  }
  
  public void changeTextAlignment(int paramInt)
  {
    if (this.d == null) {
      return;
    }
    ((StickerTextItemView)this.d).changeTextAlignment(paramInt);
  }
  
  public void changeTextAlpha(float paramFloat)
  {
    if (this.d == null) {
      return;
    }
    ((StickerTextItemView)this.d).getTextView().setAlpha(paramFloat);
  }
  
  public void changeTextStrokeWidth(int paramInt)
  {
    if (this.d == null) {
      return;
    }
    ((StickerTextItemView)this.d).setTextStrokeWidth(paramInt);
  }
  
  public void changeTextStrokeColor(int paramInt)
  {
    if (this.d == null) {
      return;
    }
    ((StickerTextItemView)this.d).setTextStrokeColor(paramInt);
  }
  
  private boolean a(StickerData paramStickerData)
  {
    if (paramStickerData == null) {
      return false;
    }
    boolean bool = true;
    if (this.a != null) {
      bool = this.a.canAppendSticker(this, paramStickerData);
    }
    if (!bool) {
      return false;
    }
    if (stickersCount() >= SdkValid.shared.maxStickers())
    {
      String str = TuSdkContext.getString("lsq_sticker_over_limit", new Object[] { Integer.valueOf(SdkValid.shared.maxStickers()) });
      TuSdkViewHelper.alert(getContext(), null, str, TuSdkContext.getString("lsq_button_done"));
      return false;
    }
    return true;
  }
  
  public void appendSticker(final StickerData paramStickerData)
  {
    if (!a(paramStickerData)) {
      return;
    }
    TuSdk.messageHub().setStatus(getContext(), TuSdkContext.getString("lsq_sticker_loading"));
    new Thread(new Runnable()
    {
      public void run()
      {
        StickerView.a(StickerView.this, paramStickerData);
      }
    }).start();
  }
  
  public void appendSticker(Bitmap paramBitmap)
  {
    if (paramBitmap == null) {
      return;
    }
    final StickerData localStickerData = new StickerData();
    localStickerData.setImage(paramBitmap);
    localStickerData.height = TuSdkContext.px2dip(paramBitmap.getHeight());
    localStickerData.width = TuSdkContext.px2dip(paramBitmap.getWidth());
    if (!a(localStickerData)) {
      return;
    }
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        StickerView.b(StickerView.this, localStickerData);
      }
    });
  }
  
  public void appendSticker(final StickerImageData paramStickerImageData)
  {
    if (paramStickerImageData == null) {
      return;
    }
    if (!a(paramStickerImageData)) {
      return;
    }
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        StickerView.b(StickerView.this, paramStickerImageData);
      }
    });
  }
  
  private void b(final StickerData paramStickerData)
  {
    StickerLocalPackage.shared().loadStickerItem(paramStickerData);
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        StickerView.b(StickerView.this, paramStickerData);
      }
    });
  }
  
  private void c(StickerData paramStickerData)
  {
    if ((paramStickerData == null) || ((paramStickerData.getImage() == null) && (paramStickerData.texts == null)))
    {
      TuSdk.messageHub().showError(getContext(), TuSdkContext.getString("lsq_sticker_load_unexsit"));
      return;
    }
    TuSdk.messageHub().dismissRightNow();
    StickerItemViewInterface localStickerItemViewInterface = d(paramStickerData);
    onStickerItemViewSelected(localStickerItemViewInterface);
  }
  
  public List<StickerItemViewInterface> getStickerItems()
  {
    return this.c;
  }
  
  public List<StickerItemViewInterface> getStickerTextItems()
  {
    LinkedList localLinkedList = new LinkedList();
    Iterator localIterator = this.c.iterator();
    while (localIterator.hasNext())
    {
      StickerItemViewInterface localStickerItemViewInterface = (StickerItemViewInterface)localIterator.next();
      if (localStickerItemViewInterface.getStickerType() == StickerType.Text) {
        localLinkedList.add(localStickerItemViewInterface);
      }
    }
    return localLinkedList;
  }
  
  public List<StickerItemViewInterface> getStickerImageItems()
  {
    LinkedList localLinkedList = new LinkedList();
    Iterator localIterator = this.c.iterator();
    while (localIterator.hasNext())
    {
      StickerItemViewInterface localStickerItemViewInterface = (StickerItemViewInterface)localIterator.next();
      if (localStickerItemViewInterface.getStickerType() == StickerType.Image) {
        localLinkedList.add(localStickerItemViewInterface);
      }
    }
    return localLinkedList;
  }
  
  private StickerItemViewInterface d(StickerData paramStickerData)
  {
    if (paramStickerData == null) {
      return null;
    }
    View localView = buildView(a(paramStickerData.getType()), this);
    if (!(localView instanceof StickerItemViewInterface)) {
      localView = buildView(a(paramStickerData.getType()), this);
    }
    StickerItemViewInterface localStickerItemViewInterface = (StickerItemViewInterface)localView;
    StickerType localStickerType = StickerType.Normal;
    if ((paramStickerData instanceof StickerImageData)) {
      localStickerType = StickerType.Image;
    } else if ((paramStickerData.getType().equals(StickerData.StickerType.TypeImage)) || (paramStickerData.getType().equals(StickerData.StickerType.TypeDynamic))) {
      localStickerType = StickerType.Image;
    } else {
      localStickerType = StickerType.Text;
    }
    localStickerItemViewInterface.setStickerViewType(this.f);
    localStickerItemViewInterface.setStickerType(localStickerType);
    localStickerItemViewInterface.setSticker(paramStickerData);
    localStickerItemViewInterface.setStroke(-1, ContextUtils.dip2px(getContext(), 2.0F));
    localStickerItemViewInterface.setParentFrame(TuSdkViewHelper.locationInWindow(this));
    localStickerItemViewInterface.setDelegate(this);
    RelativeLayout.LayoutParams localLayoutParams1 = new RelativeLayout.LayoutParams(TuSdkContext.dip2px(paramStickerData.width), TuSdkContext.dip2px(paramStickerData.height));
    RelativeLayout.LayoutParams localLayoutParams2;
    if (((localView instanceof StickerTextItemView)) && (((StickerTextItemView)localView).getTextView() != null))
    {
      localLayoutParams2 = (RelativeLayout.LayoutParams)((StickerTextItemView)localView).getTextView().getLayoutParams();
      localLayoutParams1.width += localLayoutParams2.rightMargin + localLayoutParams2.leftMargin;
      localLayoutParams1.height += TuSdkContext.dip2px(32.0F) + localLayoutParams2.topMargin + localLayoutParams2.bottomMargin;
    }
    else if ((localView instanceof StickerImageItemView))
    {
      localLayoutParams2 = (RelativeLayout.LayoutParams)((StickerImageItemView)localView).getImageView().getLayoutParams();
      localLayoutParams1.width = paramStickerData.width;
      localLayoutParams1.height = paramStickerData.height;
    }
    addView(localView, localLayoutParams1);
    this.c.add(localStickerItemViewInterface);
    if ((this.a != null) && ((localStickerItemViewInterface.getStickerType() == StickerType.Normal) || (localStickerItemViewInterface.getStickerType() == this.f))) {
      this.a.onStickerCountChanged(paramStickerData, (StickerItemViewInterface)localView, 1, this.c.size());
    }
    return localStickerItemViewInterface;
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    super.addView(paramView, paramLayoutParams);
  }
  
  public void onStickerItemViewClose(StickerItemViewInterface paramStickerItemViewInterface)
  {
    if (paramStickerItemViewInterface == null) {
      return;
    }
    if ((paramStickerItemViewInterface.getStickerType() != StickerType.Normal) && (paramStickerItemViewInterface.getStickerType() != this.f)) {
      return;
    }
    if (this.c.remove(paramStickerItemViewInterface))
    {
      this.d = null;
      removeView((View)paramStickerItemViewInterface);
      cancelAllStickerSelected();
      if (this.a != null) {
        this.a.onStickerCountChanged(paramStickerItemViewInterface.getStickerData(), paramStickerItemViewInterface, 0, this.c.size());
      }
    }
  }
  
  public void onStickerItemViewSelected(StickerItemViewInterface paramStickerItemViewInterface)
  {
    if (paramStickerItemViewInterface == null) {
      return;
    }
    if ((paramStickerItemViewInterface.getStickerType() != StickerType.Normal) && (paramStickerItemViewInterface.getStickerType() != this.f)) {
      return;
    }
    this.e = false;
    if (paramStickerItemViewInterface.equals(this.d)) {
      this.e = true;
    }
    this.d = paramStickerItemViewInterface;
    Object localObject1 = this.c.iterator();
    Object localObject2;
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (StickerItemViewInterface)((Iterator)localObject1).next();
      ((StickerItemViewInterface)localObject2).setSelected(paramStickerItemViewInterface.equals(localObject2));
    }
    if (this.a == null) {
      return;
    }
    localObject1 = ((StickerItemViewBase)paramStickerItemViewInterface).mSticker;
    if ((paramStickerItemViewInterface instanceof StickerTextItemView))
    {
      localObject2 = ((StickerTextItemView)paramStickerItemViewInterface).getTextView().getText().toString();
      this.a.onStickerItemViewSelected((StickerData)localObject1, (String)localObject2, ((StickerTextItemView)paramStickerItemViewInterface).isNeedReverse());
    }
    else
    {
      this.a.onStickerItemViewSelected((StickerData)localObject1, null, false);
    }
  }
  
  public void onStickerItemViewReleased(StickerItemViewInterface paramStickerItemViewInterface)
  {
    if ((paramStickerItemViewInterface.getStickerType() != StickerType.Normal) && (paramStickerItemViewInterface.getStickerType() != this.f)) {
      return;
    }
    if (!this.e) {
      return;
    }
    if (this.a != null) {
      this.a.onStickerItemViewReleased();
    }
  }
  
  public List<StickerResult> getResults(Rect paramRect)
  {
    ArrayList localArrayList = new ArrayList(this.c.size());
    Iterator localIterator = this.c.iterator();
    while (localIterator.hasNext())
    {
      StickerItemViewInterface localStickerItemViewInterface = (StickerItemViewInterface)localIterator.next();
      localArrayList.add(localStickerItemViewInterface.getResult(paramRect));
    }
    return localArrayList;
  }
  
  public void cancelAllStickerSelected()
  {
    Iterator localIterator = this.c.iterator();
    while (localIterator.hasNext())
    {
      StickerItemViewInterface localStickerItemViewInterface = (StickerItemViewInterface)localIterator.next();
      localStickerItemViewInterface.setSelected(false);
    }
    this.d = null;
    if (this.a == null) {
      return;
    }
    this.a.onCancelAllStickerSelected();
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0) {
      cancelAllStickerSelected();
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void resizeForVideo(TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)getLayoutParams();
    TuSdkSizeF localTuSdkSizeF;
    float f1;
    if (paramBoolean)
    {
      localTuSdkSizeF = new TuSdkSizeF();
      f1 = TuSdkContext.getScreenSize().width / TuSdkContext.getScreenSize().height;
      float f2 = paramTuSdkSize.width / paramTuSdkSize.height;
      if (f1 == f2)
      {
        localTuSdkSizeF.width = TuSdkContext.getScreenSize().width;
        localTuSdkSizeF.height = TuSdkContext.getScreenSize().height;
      }
      else
      {
        float f3;
        if (f2 > f1)
        {
          f3 = TuSdkContext.getScreenSize().width / paramTuSdkSize.width;
          localTuSdkSizeF.width = TuSdkContext.getScreenSize().width;
          localTuSdkSizeF.height = (paramTuSdkSize.height * f3);
        }
        else if (f2 < f1)
        {
          f3 = TuSdkContext.getScreenSize().height / paramTuSdkSize.height;
          localTuSdkSizeF.width = (paramTuSdkSize.width * f3);
          localTuSdkSizeF.height = TuSdkContext.getScreenSize().height;
        }
      }
      localLayoutParams.width = ((int)localTuSdkSizeF.width);
      localLayoutParams.height = ((int)localTuSdkSizeF.height);
      localLayoutParams.leftMargin = ((TuSdkContext.getScreenSize().width - localLayoutParams.width) / 2);
      localLayoutParams.topMargin = ((TuSdkContext.getScreenSize().height - localLayoutParams.height) / 2);
    }
    else
    {
      localTuSdkSizeF = new TuSdkSizeF();
      f1 = paramTuSdkSize.width / paramTuSdkSize.height;
      if (f1 < 1.0F)
      {
        localTuSdkSizeF.width = (TuSdkContext.getScreenSize().width * f1);
        localTuSdkSizeF.height = TuSdkContext.getScreenSize().width;
      }
      else if (f1 > 1.0F)
      {
        localTuSdkSizeF.width = TuSdkContext.getScreenSize().width;
        localTuSdkSizeF.height = (TuSdkContext.getScreenSize().width / f1);
      }
      else if (f1 == 1.0F)
      {
        localTuSdkSizeF.width = TuSdkContext.getScreenSize().width;
        localTuSdkSizeF.height = TuSdkContext.getScreenSize().width;
      }
      localLayoutParams.width = ((int)localTuSdkSizeF.width);
      localLayoutParams.height = ((int)localTuSdkSizeF.height);
      localLayoutParams.leftMargin = ((TuSdkContext.getScreenSize().width - localLayoutParams.width) / 2);
      localLayoutParams.topMargin = ((TuSdkContext.getScreenSize().width - localLayoutParams.height) / 2);
    }
  }
  
  public void addSticker(StickerItemViewInterface paramStickerItemViewInterface)
  {
    this.c.add(paramStickerItemViewInterface);
  }
  
  public void resize(TuSdkSize paramTuSdkSize, ViewGroup paramViewGroup)
  {
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)getLayoutParams();
    localLayoutParams.width = paramTuSdkSize.width;
    localLayoutParams.height = paramTuSdkSize.height;
    localLayoutParams.leftMargin = ((paramViewGroup.getWidth() - localLayoutParams.width) / 2);
    localLayoutParams.topMargin = ((paramViewGroup.getHeight() - localLayoutParams.height) / 2);
    Iterator localIterator = this.c.iterator();
    while (localIterator.hasNext())
    {
      StickerItemViewInterface localStickerItemViewInterface = (StickerItemViewInterface)localIterator.next();
      localStickerItemViewInterface.setParentFrame(TuSdkViewHelper.locationInWindow(this));
    }
  }
  
  public void removeAllSticker()
  {
    this.c.clear();
    removeAllViews();
  }
  
  public static enum StickerType
  {
    private StickerType() {}
  }
  
  public static abstract interface StickerViewDelegate
  {
    public abstract boolean canAppendSticker(StickerView paramStickerView, StickerData paramStickerData);
    
    public abstract void onStickerItemViewSelected(StickerData paramStickerData, String paramString, boolean paramBoolean);
    
    public abstract void onStickerItemViewReleased();
    
    public abstract void onCancelAllStickerSelected();
    
    public abstract void onStickerCountChanged(StickerData paramStickerData, StickerItemViewInterface paramStickerItemViewInterface, int paramInt1, int paramInt2);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\sticker\StickerView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */