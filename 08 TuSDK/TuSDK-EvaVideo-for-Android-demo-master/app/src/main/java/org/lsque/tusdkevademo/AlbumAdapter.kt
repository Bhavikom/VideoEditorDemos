/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/1$ 16:42$
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.jetbrains.anko.find
import org.lasque.tusdk.eva.TuSdkEvaImageEntity
import org.lasque.tusdk.eva.TuSdkEvaTextEntity
import org.lasque.tusdk.eva.TuSdkEvaVideoEntity

class AlbumAdapter(context: Context, albumList: List<AlbumInfo>) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private val mContext: Context = context
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mAlbumList = albumList

    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): ViewHolder {
        val itemView = mInflater.inflate(R.layout.lsq_album_select_video_item, p0, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mAlbumList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, p1: Int) {
        val currentItem = mAlbumList[p1]
        when (currentItem.type) {
            AlbumItemType.Image -> {
                viewHolder!!.textView.visibility = View.GONE
            }
            AlbumItemType.Video -> {
                viewHolder!!.textView.visibility = View.VISIBLE
                viewHolder.textView.text = String.format("%02d:%02d", currentItem.duration / 1000 / 60, currentItem.duration / 1000 % 60)
            }
        }
        Glide.with(mContext).load(currentItem.path).into(viewHolder.imageView)
        viewHolder.itemView.setOnClickListener { mItemClickListener!!.onClick(viewHolder.itemView, currentItem, p1) }
    }

    public fun getAlbumList() : List<AlbumInfo>{
        return mAlbumList;
    }

    public fun setAlbumList(albumList: List<AlbumInfo>){
        mAlbumList = albumList
    }

    private var mItemClickListener: OnItemClickListener? = null

    public fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mItemClickListener = onItemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.find(R.id.lsq_video_thumb_view)
        val textView: TextView = itemView.find(R.id.lsq_movie_time)
    }

    interface OnItemClickListener {
        fun onClick(view: View, item: AlbumInfo, position: Int)
    }
}