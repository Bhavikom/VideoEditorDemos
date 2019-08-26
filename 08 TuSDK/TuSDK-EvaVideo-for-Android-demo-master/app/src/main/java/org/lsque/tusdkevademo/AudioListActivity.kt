/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/2$ 12:42$
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_audio_list.*


class AudioListActivity : ScreenAdapterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_list)
        initView()
    }

    private fun initView() {
        lsq_audio_list.layoutManager = LinearLayoutManager(this)
        val audioAdapter = AudioListAdapter(this,AudioItemFactory.getAudioItemList(this))
        audioAdapter.setOnItemClickListener(object : AudioListAdapter.OnItemClickListener{
            override fun onClick(view: View, item: AudioItem, position: Int) {
                val intent = intent
                val bundle = Bundle()
                intent.setClass(this@AudioListActivity,ModelEditorActivity.javaClass)
                bundle.putString("audioPath",item.audioPath)
                intent.putExtras(bundle)
                setResult(33,intent)
                finish()
            }
        })
        lsq_audio_list.adapter = audioAdapter
        lsq_close.setOnClickListener { finish() }
    }

    override fun finish() {
        overridePendingTransition(0,R.anim.activity_close_from_top_to_bottom)
        super.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
    }
}