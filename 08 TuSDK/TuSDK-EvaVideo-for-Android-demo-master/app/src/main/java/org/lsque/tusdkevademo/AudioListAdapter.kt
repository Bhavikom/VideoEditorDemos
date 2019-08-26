/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/2$ 13:27$
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
import org.jetbrains.anko.find


class AudioListAdapter(context: Context, audioList: List<AudioItem>) : RecyclerView.Adapter<AudioListAdapter.ViewHolder>(){

    var mAudioList = audioList
    var mContext = context
    var mInflater : LayoutInflater = LayoutInflater.from(mContext)
    var mItemClickListener : OnItemClickListener? = null
    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_audio_list,p0,false))
    }

    override fun getItemCount(): Int {
        return mAudioList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        var currentItem = mAudioList[position]
        viewHolder!!.textView.text = currentItem.audioName
        viewHolder!!.itemView.setOnClickListener { mItemClickListener!!.onClick(viewHolder.itemView,currentItem,position) }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.find(R.id.lsq_audio_name)
    }

    public fun setOnItemClickListener(itemClickListener: OnItemClickListener){
        this.mItemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun onClick(view: View, item: AudioItem, position: Int)
    }

}