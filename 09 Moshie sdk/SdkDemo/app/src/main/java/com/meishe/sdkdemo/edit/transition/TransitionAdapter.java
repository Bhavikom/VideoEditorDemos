package com.meishe.sdkdemo.edit.transition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.data.FilterItem;

import java.io.InputStream;
import java.util.List;

public class TransitionAdapter extends RecyclerView.Adapter<TransitionAdapter.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mClickListener;
    private List<FilterItem> mFilterList;
    private int mSelectPos = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onResetTransition(FilterItem filterItem);

        void onSameItemClick();
    }

    public void setSelectPos(int pos) {
        this.mSelectPos = pos;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout item_assetLayout;
        private ImageView item_assetImage;
        private TextView item_assetName;

        public ViewHolder(View view) {
            super(view);
            item_assetLayout = (RelativeLayout) view.findViewById(R.id.layoutAsset);
            item_assetName = (TextView) view.findViewById(R.id.nameAsset);
            item_assetImage = (ImageView) view.findViewById(R.id.imageAsset);
        }
    }

    public TransitionAdapter(Context context) {
        mContext = context;
    }

    public void setFilterList(List<FilterItem> filterList) {
        this.mFilterList = filterList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transition, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FilterItem itemData = mFilterList.get(position);
        if (itemData == null)
            return;

        String name = itemData.getFilterName();
        if (name != null) {
            holder.item_assetName.setText(name);
        }

        int filterMode = itemData.getFilterMode();
        if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
            int imageId = itemData.getImageId();
            if (imageId != 0)
                holder.item_assetImage.setImageResource(imageId);
        }

        String imageUrl = itemData.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            if (filterMode == FilterItem.FILTERMODE_BUNDLE) {
                try {
                    InputStream inStream = mContext.getAssets().open(imageUrl);
                    Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                    holder.item_assetImage.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //加载图片
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                Glide.with(mContext)
                        .asBitmap()
                        .load(imageUrl)
                        .apply(options)
                        .into(holder.item_assetImage);
            }
        }

        if (mSelectPos == position) {
            holder.item_assetLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.fx_item_radius_border_select));
        } else {
            holder.item_assetLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.fx_item_radius_border_unselect));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectPos == position) {
                    if (mClickListener != null) {
                        mClickListener.onSameItemClick();
                    }
                    return;
                }

                notifyItemChanged(mSelectPos);
                mSelectPos = position;
                notifyItemChanged(mSelectPos);

                if (mClickListener != null) {
                    mClickListener.onItemClick(view, position);
                    FilterItem item = mFilterList.get(position);
                    mClickListener.onResetTransition(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }
}
