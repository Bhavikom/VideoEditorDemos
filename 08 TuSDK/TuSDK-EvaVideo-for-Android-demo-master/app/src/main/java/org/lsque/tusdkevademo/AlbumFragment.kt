/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/1$ 16:00$
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.movie_album_fragment.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.support.v4.startActivityForResult
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper
import org.lasque.tusdk.impl.view.widget.TuProgressHub
import java.util.*

class AlbumFragment  : Fragment(){

    /* 最小视频时长(单位：ms) */
    private val MIN_VIDEO_DURATION = 3000
    /* 最大视频时长(单位：ms) */
    private val MAX_VIDEO_DURATION = 60000 * 3

    private var mAlbumAdapter : AlbumAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_album_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridLayoutManager = GridLayoutManager(activity, 4)
        lsq_album_list.layoutManager = gridLayoutManager
    }

    override fun onResume() {
        super.onResume()
        var loadTask = LoadAlbumTask(this)
        loadTask.execute()
    }

    private fun getAlbumList() : LinkedList<AlbumInfo>{
        var albumList = LinkedList<AlbumInfo>()
        when {
            arguments!!.getBoolean("onlyImage") -> getImageList(albumList)
            arguments!!.getBoolean("onlyVideo") -> getVideoList(albumList)
            else -> {
                getVideoList(albumList)
                getImageList(albumList)
            }
        }
        albumList.sortBy { it.createDate }
        return albumList
    }

    /**
     * 将扫描的视频添加到集合中
     */
    private fun getVideoList(albumList : LinkedList<AlbumInfo>) {
        val cursor = activity!!.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, "date_added desc")
        while (cursor!!.moveToNext()) {
            val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
            val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
            val createDate = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED))
            //根据时间长短加入显示列表
            if (duration in 1 until MAX_VIDEO_DURATION) {
                albumList.add(AlbumInfo(path,AlbumItemType.Video, duration,createDate))
            }
        }
        cursor.close()
    }

    private fun getImageList(albumList: LinkedList<AlbumInfo>){
        var imageList = ImageSqlHelper.getPhotoList(activity!!.contentResolver, true)
        for (item in imageList){
            albumList.add(AlbumInfo(item.path,AlbumItemType.Image,0,item.createDate.timeInMillis))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    companion object {
        /**
         * 相册加载
         */
        internal class LoadAlbumTask(private val albumFragment: AlbumFragment) : AsyncTask<Void, Int, List<AlbumInfo>>() {
    
            override fun doInBackground(vararg voids: Void): List<AlbumInfo>? {
                return albumFragment.getAlbumList()
            }
    
            override fun onPreExecute() {
                TuProgressHub.showToast(albumFragment.activity, "数据加载中...")
                super.onPreExecute()
            }
    
            override fun onPostExecute(imageInfos: List<AlbumInfo>?) {
                var imageInfos = imageInfos
                TuProgressHub.dismiss()
                if (imageInfos == null) imageInfos = ArrayList()
                if (albumFragment.mAlbumAdapter == null) {
                    albumFragment.mAlbumAdapter = AlbumAdapter(albumFragment.activity!!.baseContext, imageInfos)
                    albumFragment.mAlbumAdapter!!.setOnItemClickListener(object : AlbumAdapter.OnItemClickListener{
                        override fun onClick(view: View, item: AlbumInfo, position: Int) {
                            when(item.type){
                                AlbumItemType.Image -> {
                                    albumFragment.activity!!.startActivityForResult<ImageCuterActivity>(ModelEditorActivity.ALBUM_REQUEST_CODE_IMAGE,"width" to albumFragment.arguments!!["width"],"height" to albumFragment.arguments!!["height"],"imagePath" to item.path)
                                }
                                AlbumItemType.Video -> {
                                    albumFragment.activity!!.startActivityForResult<MovieCuterActivity>(ModelEditorActivity.ALBUM_REQUEST_CODE_VIDEO,"width" to albumFragment.arguments!!["width"],"height" to albumFragment.arguments!!["height"],"videoPath" to item.path)
                                }
                            }
                        }
                    })
                    albumFragment.lsq_album_list.adapter = albumFragment.mAlbumAdapter
                }
                if (albumFragment.mAlbumAdapter!!.getAlbumList() != imageInfos){
                    albumFragment.mAlbumAdapter!!.setAlbumList(imageInfos)
                }
            }
        }
    }
}