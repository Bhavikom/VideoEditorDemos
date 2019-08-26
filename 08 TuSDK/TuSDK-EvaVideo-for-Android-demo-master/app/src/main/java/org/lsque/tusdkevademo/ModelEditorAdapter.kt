/**
 *  TuSDK
 *  droid-sdk-eva
 *  org.lsque.tusdkevademo
 *  @author  H.ys
 *  @Date    2019/7/1 13:20
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor
import org.lasque.tusdk.core.utils.StringHelper
import org.lasque.tusdk.eva.EvaAsset
import org.lasque.tusdk.eva.TuSdkEvaImageEntity
import org.lasque.tusdk.eva.TuSdkEvaTextEntity
import org.lasque.tusdk.eva.TuSdkEvaVideoEntity
import java.util.*


class ModelEditorAdapter(context: Context, modelList: LinkedList<EditorModelItem>) : RecyclerView.Adapter<ModelEditorAdapter.ViewHolder>() {

    protected val STORAGE = "/storage/"

    private val mContext = context
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mModelList: LinkedList<EditorModelItem> = modelList

    private var mItemClickListener: OnItemClickListener? = null

    private val IMAGE_TYPE = 1
    private val TEXT_TYPE = 2
    private val VIDEO_TYPE = 3

    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): ViewHolder {
        val viewHolder = when (p1) {
            IMAGE_TYPE -> {
                ImageViewHolder(mInflater.inflate(R.layout.item_editor_image, p0, false))
            }
            TEXT_TYPE -> {
                TextViewHolder(mInflater.inflate(R.layout.item_editor_text, p0, false))
            }
            VIDEO_TYPE -> {
                ImageViewHolder(mInflater.inflate(R.layout.item_editor_image, p0, false))
            }
            else -> {
                null
            }
        }
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return mModelList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        when (mModelList[position].modelType) {
            EditType.Image -> {
                val item = mModelList[position]
                (viewHolder as ImageViewHolder).textView.text = mContext.getString(R.string.lsq_editor_item_image)
                viewHolder!!.itemView.setOnClickListener { mItemClickListener!!.onClick(viewHolder.itemView, mModelList[position].modelItem as TuSdkEvaImageEntity, position, EditType.Image) }
                if (!StringHelper.isBlank((item.modelItem as TuSdkEvaImageEntity).replacePath)) {
                    Glide.with(mContext).asBitmap().load((item.modelItem as TuSdkEvaImageEntity).replacePath).into(viewHolder.imageView)
                } else {
                    if ((item.modelItem as TuSdkEvaImageEntity).originalPath.startsWith(STORAGE)) {
                        Glide.with(mContext).asBitmap().load((item.modelItem as TuSdkEvaImageEntity).originalPath).into((viewHolder.imageView))
                    } else {
                        Glide.with(mContext).asBitmap().load("file:///android_asset/${(item.modelItem as TuSdkEvaImageEntity).originalPath}").into((viewHolder.imageView))
                    }

                }

            }
            EditType.Text -> {
                val item = mModelList[position]
                val itemTextView = (viewHolder as TextViewHolder).textView
                itemTextView.text = (item.modelItem as TuSdkEvaTextEntity).displayText
                itemTextView.textColor = (item.modelItem as TuSdkEvaTextEntity).displayTextColor
                viewHolder!!.itemView.setOnClickListener { mItemClickListener!!.onClick(viewHolder.itemView, mModelList[position].modelItem as TuSdkEvaTextEntity, position, EditType.Text) }
            }
            EditType.Video -> {
                viewHolder!!.itemView.setOnClickListener { mItemClickListener!!.onClick(viewHolder.itemView, mModelList[position].modelItem as TuSdkEvaVideoEntity, position, EditType.Video) }
                val item = mModelList[position]
                val currentItem = item.modelItem as TuSdkEvaVideoEntity
                var showText = ""
                if (currentItem.assetType == EvaAsset.TuSdkEvaAssetType.EvaVideoImage) {
                    showText = mContext.getString(R.string.lsq_editor_item_video_image)
                } else if (currentItem.assetType == EvaAsset.TuSdkEvaAssetType.EvaOnlyVideo) {
                    showText = mContext.getString(R.string.lsq_editor_item_video)
                }
                (viewHolder as ImageViewHolder).textView.text = showText
                if (!StringHelper.isBlank((item.modelItem as TuSdkEvaVideoEntity).displayVideoPath)) {
                    if ((item.modelItem as TuSdkEvaVideoEntity).displayVideoPath.startsWith(STORAGE)) {
                        Glide.with(mContext).asBitmap().load((item.modelItem as TuSdkEvaVideoEntity).displayVideoPath).into((viewHolder.imageView))
                    } else {
                        Glide.with(mContext).asBitmap().load("file:///android_asset/${(item.modelItem as TuSdkEvaVideoEntity).displayVideoPath}").into((viewHolder.imageView))
                    }
                } else if (!StringHelper.isBlank((item.modelItem as TuSdkEvaVideoEntity).displayImagePath)) {
                    if ((item.modelItem as TuSdkEvaVideoEntity).displayImagePath.startsWith(STORAGE)) {
                        Glide.with(mContext).asBitmap().load((item.modelItem as TuSdkEvaVideoEntity).displayImagePath).into((viewHolder.imageView))
                    } else {
                        Glide.with(mContext).asBitmap().load("file:///android_asset/${(item.modelItem as TuSdkEvaVideoEntity).displayImagePath}").into((viewHolder.imageView))
                    }
                } else {
                    if ((item.modelItem as TuSdkEvaVideoEntity).originalPath.startsWith(STORAGE)) {
                        Glide.with(mContext).asBitmap().load((item.modelItem as TuSdkEvaVideoEntity).originalPath).into((viewHolder.imageView))
                    } else {
                        Glide.with(mContext).asBitmap().load("file:///android_asset/${(item.modelItem as TuSdkEvaVideoEntity).originalPath}").into((viewHolder.imageView))
                    }

                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (mModelList[position].modelType) {
            EditType.Image -> {
                IMAGE_TYPE
            }
            EditType.Text -> {
                TEXT_TYPE
            }
            EditType.Video -> {
                VIDEO_TYPE
            }
        }
    }

    public fun setEditorModelList(modelList: LinkedList<EditorModelItem>) {
        mModelList = modelList
        notifyDataSetChanged()
    }

    public fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mItemClickListener = onItemClickListener
    }

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ImageViewHolder(itemView: View) : ViewHolder(itemView) {
        val imageView = itemView.find<ImageView>(R.id.lsq_editor_icon)
        val textView = itemView.find<TextView>(R.id.lsq_editor_icon_num)
    }

    inner class TextViewHolder(itemView: View) : ViewHolder(itemView) {
        val textView = itemView.find<TextView>(R.id.lsq_edior_text)
    }


    interface OnItemClickListener {
        fun onClick(view: View, item: TuSdkEvaImageEntity, position: Int, type: EditType)
        fun onClick(view: View, item: TuSdkEvaVideoEntity, position: Int, type: EditType)
        fun onClick(view: View, item: TuSdkEvaTextEntity, position: Int, type: EditType)
    }


}