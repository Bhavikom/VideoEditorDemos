package com.meishe.sdkdemo.edit.compoundcaption;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.Caption.CaptionFontRecyclerAdaper;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.data.AssetItem;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.data.ParseJsonFile;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.dataInfo.CompoundCaptionInfo;

import java.util.ArrayList;
import java.util.List;

import static com.meishe.sdkdemo.edit.compoundcaption.CompoundCaptionActivity.select_caption_index;
import static com.meishe.sdkdemo.edit.compoundcaption.CompoundCaptionActivity.select_caption_text;
import static com.meishe.sdkdemo.utils.Constants.CaptionColors;

public class CompoundCaptionStyleActivity extends BaseActivity {
    private static final String TAG = "CompoundCaptionStyleActivity";
    private CustomTitleBar mTitleBar;
    private ImageView mCaptionFontDownload;
    private RecyclerView mFontRecyclerList;
    private EditText mCaptionInput;
    private RecyclerView mColorRecyclerList;
    private ImageView mCancel;
    private ImageView mFinish;

    private CompoundCaptionColorAdaper mCaptionColorRecycleAdapter;
    private CaptionFontRecyclerAdaper mCaptionFontRecycleAdapter;
    private ArrayList<String> mCaptionColorInfolist = new ArrayList<>();
    private ArrayList<AssetItem> mCaptionFontInfolist = new ArrayList<>();
    private NvsStreamingContext mStreamingContext;

    private int mCatpionIndex = -1;
    private String mCatpionText = "";

    private ArrayList<CompoundCaptionInfo> mCaptionDataListClone;
    private int mSelectFontPos = 0;
    private int mSelectColorPos = -1;
    private CompoundCaptionInfo.CompoundCaptionAttr mCurCatpionAttr;
    private ArrayList<FontInfo> infoList;
    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_compound_caption_style;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mCaptionFontDownload = (ImageView) findViewById(R.id.captionFontDownload);
        mFontRecyclerList = (RecyclerView) findViewById(R.id.fontRecyclerList);
        mCaptionInput = (EditText) findViewById(R.id.captionInput);
        mColorRecyclerList = (RecyclerView) findViewById(R.id.colorRecyclerList);
        mCancel = (ImageView) findViewById(R.id.cancel);
        mFinish = (ImageView) findViewById(R.id.finish);
    }

    @Override
    protected void initTitle() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mCatpionIndex = bundle.getInt(select_caption_index);
                mCatpionText = bundle.getString(select_caption_text);
            }
        }
        mTitleBar.setBackImageVisible(View.GONE);
        mTitleBar.setTextCenter(R.string.compoundcaption);
        if (!TextUtils.isEmpty(mCatpionText)) {
            mCaptionInput.setHint(mCatpionText);
        }
        mCaptionInput.setText(mCatpionText);
        mCaptionInput.setSelection(mCaptionInput.getText().length());
        showInputMethod();
        mCaptionDataListClone = BackupData.instance().cloneCompoundCaptionData();
    }

    @Override
    protected void initData() {
        initCaptionFontInfoList();
        initCaptionFontRecycleAdapter();
        initCaptionColorRecycleAdapter();
        updateViewUI();
    }

    @Override
    protected void initListener() {
        mCaptionFontDownload.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.captionFontDownload:
                break;
            case R.id.cancel:
                finishActivity();
                break;
            case R.id.finish:
                String captionText = mCaptionInput.getText().toString();
                if (captionText.isEmpty()) {
                    captionText = mCatpionText;
                }
                mCurCatpionAttr.setCaptionText(captionText);
                BackupData.instance().setCompoundCaptionList(mCaptionDataListClone);
                Intent intent = new Intent();
                intent.putExtra(select_caption_index, mCatpionIndex);
                setResult(RESULT_OK, intent);
                finishActivity();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    private void finishActivity() {
        hideInputMethod();
        AppManager.getInstance().finishActivity();
    }

    private CompoundCaptionInfo.CompoundCaptionAttr getCaptionAttr() {
        CompoundCaptionInfo captionInfo = getCurCaptionInfo();
        if (captionInfo == null) {
            return null;
        }
        ArrayList<CompoundCaptionInfo.CompoundCaptionAttr> captionAttributeList = captionInfo.getCaptionAttributeList();
        if (captionAttributeList == null) {
            return null;
        }
        int captionAttrCount = captionAttributeList.size();
        if (captionAttrCount == 0 || mCatpionIndex < 0 || mCatpionIndex >= captionAttrCount) {
            return null;
        }
        return captionAttributeList.get(mCatpionIndex);
    }

    private void updateViewUI() {
        mCurCatpionAttr = getCaptionAttr();
        String fontName = mCurCatpionAttr.getCaptionFontName();
        int fontCount = mCaptionFontInfolist.size();
        for (int idx = 0; idx < fontCount; idx++) {
            AssetItem assetItem = mCaptionFontInfolist.get(idx);
            if (assetItem == null) {
                continue;
            }
            NvAsset asset = assetItem.getAsset();
            if (asset == null) {
                continue;
            }
            if (!TextUtils.isEmpty(asset.name)
                    && !TextUtils.isEmpty(fontName)
                    && asset.name.equals(fontName)) {
                mSelectFontPos = idx;
                break;
            }
        }

        String captionColor = mCurCatpionAttr.getCaptionColor();
        int colorCount = mCaptionColorInfolist.size();
        for (int idx = 0; idx < colorCount; idx++) {
            String colorStr = mCaptionColorInfolist.get(idx);
            if (colorStr == null) {
                continue;
            }
            if (!TextUtils.isEmpty(colorStr)
                    && !TextUtils.isEmpty(captionColor)
                    && colorStr.equals(captionColor)) {
                mSelectColorPos = idx;
                break;
            }
        }

        fontNotifyDataSetChanged(mSelectFontPos);
        colorNotifyDataSetChanged(mSelectColorPos);
    }

    private void fontNotifyDataSetChanged(int selectPos) {
        if (mCaptionFontRecycleAdapter != null) {
            mCaptionFontRecycleAdapter.setSelectedPos(selectPos);
            mCaptionFontRecycleAdapter.notifyDataSetChanged();
        }
    }

    private void colorNotifyDataSetChanged(int selectPos) {
        if (mCaptionColorRecycleAdapter != null) {
            mCaptionColorRecycleAdapter.setSelectedPos(selectPos);
            mCaptionColorRecycleAdapter.notifyDataSetChanged();
        }
    }

    private CompoundCaptionInfo getCurCaptionInfo() {
        int curCaptionZVal = BackupData.instance().getCaptionZVal();
        int captionCount = mCaptionDataListClone.size();
        for (int idx = 0; idx < captionCount; idx++) {
            CompoundCaptionInfo captionInfo = mCaptionDataListClone.get(idx);
            if (captionInfo == null) {
                continue;
            }
            int captionZVal = captionInfo.getCaptionZVal();
            if (curCaptionZVal == captionZVal) {
                return captionInfo;
            }
        }
        return null;
    }

    private void showInputMethod() {
        //弹出键盘
        mCaptionInput.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCaptionInput.requestFocus();//获取焦点
                InputMethodManager inputManager = (InputMethodManager) mCaptionInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mCaptionInput, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    private void hideInputMethod() {
        //隐藏键盘
        InputMethodManager inputManager = (InputMethodManager) mCaptionInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(mCaptionInput.getWindowToken(), 0);
    }

    private void initCaptionFontInfoList() {
        String fontJsonPath = "font/info.json";
        String fontJsonText = ParseJsonFile.readAssetJsonFile(this, fontJsonPath);
        if (TextUtils.isEmpty(fontJsonText)) {
            return;
        }
        infoList = ParseJsonFile.fromJson(fontJsonText, new TypeToken<List<FontInfo>>() {
        }.getType());
        if (infoList == null) {
            return;
        }
        int fontCount = infoList.size();
        for (int idx = 0; idx < fontCount; idx++) {
            FontInfo fontInfo = infoList.get(idx);
            if (fontInfo == null) {
                continue;
            }
            String fontAssetPath = "assets:/font/" + fontInfo.getFontFileName();
            String fontName = mStreamingContext.registerFontByFilePath(fontAssetPath);
            AssetItem assetItem = new AssetItem();
            NvAsset asset = new NvAsset();
            String fontCoverPath = "file:///android_asset/font/" + fontInfo.getImageName();
            asset.coverUrl = fontCoverPath;
            asset.isReserved = true;
            asset.bundledLocalDirPath = fontAssetPath;
            asset.name = fontName;
            assetItem.setAsset(asset);
            assetItem.setAssetMode(AssetItem.ASSET_LOCAL);
            mCaptionFontInfolist.add(assetItem);
        }
        AssetItem assetItem = new AssetItem();
        NvAsset asset = new NvAsset();
        assetItem.setImageRes(R.mipmap.comp_caption_default);
        assetItem.setAssetMode(AssetItem.ASSET_NONE);
        assetItem.setAsset(asset);
        mCaptionFontInfolist.add(0, assetItem);
    }

    private void initCaptionFontRecycleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mFontRecyclerList.setLayoutManager(layoutManager);
        mCaptionFontRecycleAdapter = new CaptionFontRecyclerAdaper(this);
        mCaptionFontRecycleAdapter.setAssetInfoList(mCaptionFontInfolist);
        mFontRecyclerList.setAdapter(mCaptionFontRecycleAdapter);
        mFontRecyclerList.addItemDecoration(new SpaceItemDecoration(0, ScreenUtils.dip2px(this, 8)));
        mCaptionFontRecycleAdapter.setOnItemClickListener(new CaptionFontRecyclerAdaper.OnFontItemClickListener() {
            @Override
            public void onItemDownload(View view, int position) {

            }

            @Override
            public void onItemClick(View view, int position) {
                int fontCount = mCaptionFontInfolist.size();
                if (position < 0 || position >= fontCount) {
                    return;
                }
                if (mSelectFontPos == position) {
                    return;
                }
                mSelectFontPos = position;
                AssetItem assetItem = mCaptionFontInfolist.get(position);
                if (assetItem == null) {
                    return;
                }
                NvAsset asset = assetItem.getAsset();
                if (asset == null) {
                    return;
                }

                //设置字体
                String assetLocalDirPath = asset.bundledLocalDirPath;
                Typeface newTypeface = null;
                if (!TextUtils.isEmpty(assetLocalDirPath)) {
                    int index = assetLocalDirPath.indexOf('/');
                    String fontPath = assetLocalDirPath.substring(index + 1);
                    newTypeface = Typeface.createFromAsset(getAssets(), fontPath);
                }
                mCaptionInput.setTypeface(newTypeface);
                if (mCurCatpionAttr != null) {
                    mCurCatpionAttr.setCaptionFontName(asset.name);
                }
            }
        });
    }

    private void initCaptionColorRecycleAdapter() {
        for (int index = 0; index < CaptionColors.length; ++index) {
            mCaptionColorInfolist.add(CaptionColors[index]);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mColorRecyclerList.setLayoutManager(layoutManager);
        mCaptionColorRecycleAdapter = new CompoundCaptionColorAdaper(this);
        mCaptionColorRecycleAdapter.setCaptionColorList(mCaptionColorInfolist);
        mColorRecyclerList.setAdapter(mCaptionColorRecycleAdapter);
        mColorRecyclerList.addItemDecoration(new SpaceItemDecoration(0, ScreenUtils.dip2px(this, 30)));
        mCaptionColorRecycleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                int colorCount = mCaptionColorInfolist.size();
                if (pos < 0 || pos >= colorCount) {
                    return;
                }
                if (mSelectColorPos == pos) {
                    return;
                }
                mSelectColorPos = pos;
                String color = mCaptionColorInfolist.get(pos);
                mCaptionInput.setTextColor(Color.parseColor(color));
                if (mCurCatpionAttr != null) {
                    mCurCatpionAttr.setCaptionColor(color);
                }
            }
        });
    }
}
