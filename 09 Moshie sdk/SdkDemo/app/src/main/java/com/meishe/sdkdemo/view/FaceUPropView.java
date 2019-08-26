package com.meishe.sdkdemo.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.data.FilterItem;
import com.meishe.sdkdemo.edit.filter.FilterAdapter;
import com.meishe.sdkdemo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/11/15.
 */

public class FaceUPropView extends RelativeLayout {
    private RecyclerView mFaceUPropList;
    private LinearLayout mMoreFaceUProp;
    private ImageButton mMoreFaceUPropImage;
    private TextView mMoreFaceUPropText;
    private FilterAdapter mFaceUPropAdapter;

    private OnFaceUPropListener mFaceUPropListener;

    public interface OnFaceUPropListener {
        void onItmeClick(View v, int position);

        void onMoreFaceUProp();
    }

    public void setFaceUPropListener(OnFaceUPropListener faceUPropListener) {
        this.mFaceUPropListener = faceUPropListener;
    }

    public void setMoreFaceUPropClickable(boolean clickable) {
        mMoreFaceUProp.setClickable(clickable);
    }

    public FaceUPropView(Context context) {
        this(context, null);
    }

    public FaceUPropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setSelectedPos(int selectedPos) {
        if (mFaceUPropAdapter != null)
            mFaceUPropAdapter.setSelectPos(selectedPos);
    }

    public void setPropDataArrayList(ArrayList<FilterItem> propDataList) {
        if (mFaceUPropAdapter != null)
            mFaceUPropAdapter.setFilterDataList(propDataList);
    }

    public void notifyDataSetChanged() {
        if (mFaceUPropAdapter != null) {
            mFaceUPropAdapter.notifyDataSetChanged();
        }
    }

    public void initPropRecyclerView(final Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mFaceUPropList.setLayoutManager(linearLayoutManager);
        mFaceUPropList.setAdapter(mFaceUPropAdapter);

        mFaceUPropAdapter.setOnItemClickListener(new FilterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mFaceUPropListener != null) {
                    showPackagePromptToast(context, position);
                    mFaceUPropListener.onItmeClick(view, position);
                }
            }
        });
    }

    private void showPackagePromptToast(Context context, int position) {
        List<FilterItem> filterItemList = mFaceUPropAdapter.getFilterDataList();
        if (filterItemList.isEmpty()) {
            return;
        }
        FilterItem item = filterItemList.get(position);
        String sceneId = item.getPackageId();
        String packagePrompt = getARSceneAssetPackagePrompt(sceneId);
        if (!TextUtils.isEmpty(packagePrompt)) {
            ToastUtil.showToastCenter(context.getApplicationContext(), packagePrompt);
        }
    }

    private String getARSceneAssetPackagePrompt(String packageID) {
        if (packageID == null || packageID.isEmpty()) {
            return "";
        }
        NvsStreamingContext streamingContext = NvsStreamingContext.getInstance();
        if (streamingContext == null) {
            return "";
        }
        NvsAssetPackageManager manager = streamingContext.getAssetPackageManager();
        if (manager == null) {
            return "";
        }
        return manager.getARSceneAssetPackagePrompt(packageID);
    }

    private void init(Context context) {
        mFaceUPropAdapter = new FilterAdapter(context);
        mFaceUPropAdapter.isArface(true);
        View rootView = LayoutInflater.from(context).inflate(R.layout.faceu_prop_view, this);
        mFaceUPropList = (RecyclerView) rootView.findViewById(R.id.faceUPropList);
        mMoreFaceUProp = (LinearLayout) rootView.findViewById(R.id.moreFaceUProp);
        mMoreFaceUProp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFaceUPropListener != null) {
                    mFaceUPropListener.onMoreFaceUProp();
                }
            }
        });
        mMoreFaceUPropImage = (ImageButton) rootView.findViewById(R.id.moreFaceUPropImage);
        mMoreFaceUPropImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreFaceUProp.callOnClick();
            }
        });
        mMoreFaceUPropText = (TextView) rootView.findViewById(R.id.moreFaceUPropText);
        mMoreFaceUPropText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreFaceUProp.callOnClick();
            }
        });
    }
}
