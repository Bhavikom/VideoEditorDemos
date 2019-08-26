package org.lasque.tusdk.core.secret;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.core.http.HttpHeader;
import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import org.lasque.tusdk.core.network.TuSdkHttpHandler;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.json.JsonHelper;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

public class TuSDKOnlineStickerFetcher
{
  private TuSDKOnlineStickerFetcherDelegate a;
  private TuSdkHttpHandler b = new TuSdkHttpHandler()
  {
    public void onSuccess(int paramAnonymousInt, List<HttpHeader> paramAnonymousList, String paramAnonymousString)
    {
      try
      {
        if (TextUtils.isEmpty(paramAnonymousString))
        {
          TuSDKOnlineStickerFetcher.a(TuSDKOnlineStickerFetcher.this);
          return;
        }
        JSONArray localJSONArray = new JSONArray(paramAnonymousString);
        ArrayList localArrayList = new ArrayList(localJSONArray.length());
        int i = 0;
        int j = localJSONArray.length();
        while (i < j)
        {
          JSONObject localJSONObject = JsonHelper.getJSONObject(localJSONArray, i);
          if (localJSONObject != null)
          {
            StickerGroup localStickerGroup = (StickerGroup)ReflectUtils.classInstance(StickerGroup.class);
            if (localStickerGroup != null)
            {
              localStickerGroup.setJson(localJSONObject);
              localArrayList.add(localStickerGroup);
            }
          }
          i++;
        }
        TuSDKOnlineStickerFetcher.a(TuSDKOnlineStickerFetcher.this, localArrayList);
      }
      catch (JSONException localJSONException)
      {
        TuSDKOnlineStickerFetcher.a(TuSDKOnlineStickerFetcher.this);
      }
    }
    
    public void onFailure(int paramAnonymousInt, List<HttpHeader> paramAnonymousList, String paramAnonymousString, Throwable paramAnonymousThrowable)
    {
      TuSDKOnlineStickerFetcher.a(TuSDKOnlineStickerFetcher.this);
    }
    
    protected void onRequestedSucceed(TuSdkHttpHandler paramAnonymousTuSdkHttpHandler) {}
  };
  
  public TuSDKOnlineStickerFetcher setDelegate(TuSDKOnlineStickerFetcherDelegate paramTuSDKOnlineStickerFetcherDelegate)
  {
    this.a = paramTuSDKOnlineStickerFetcherDelegate;
    return this;
  }
  
  public TuSDKOnlineStickerFetcherDelegate getDelegate()
  {
    return this.a;
  }
  
  public final void fetchStickerGroupWithCursor(int paramInt, boolean paramBoolean)
  {
    if (!SdkValid.shared.isVaild()) {
      return;
    }
    String str = a(paramInt, paramBoolean);
    TuSdkHttpEngine.webAPIEngine().get(str, null, true, this.b);
  }
  
  private String a(int paramInt, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("/stickerGroup/index");
    localStringBuilder.append("?id=1").append("&devid=").append(TuSdkHttpEngine.shared().getDevId()).append("&uuid=").append(TuSdkHttpEngine.shared().uniqueDeviceID());
    if (paramInt > 0) {
      localStringBuilder.append("&cursor=").append(paramInt);
    }
    if (paramBoolean) {
      localStringBuilder.append("&is_smart=1");
    }
    return localStringBuilder.toString();
  }
  
  private void a(List<StickerGroup> paramList)
  {
    if (this.a == null) {
      return;
    }
    this.a.onFetchCompleted(paramList);
  }
  
  private void a()
  {
    if (this.a == null) {
      return;
    }
    this.a.onFetchFailed();
  }
  
  public static abstract interface TuSDKOnlineStickerFetcherDelegate
  {
    public abstract void onFetchFailed();
    
    public abstract void onFetchCompleted(List<StickerGroup> paramList);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\TuSDKOnlineStickerFetcher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */