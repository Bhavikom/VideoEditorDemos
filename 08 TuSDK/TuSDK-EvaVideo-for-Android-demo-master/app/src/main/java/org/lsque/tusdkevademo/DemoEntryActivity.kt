/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/1$ 10:05$
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.demo_entry_activity.*
import org.jetbrains.anko.startActivity
import org.lasque.tusdk.core.view.recyclerview.TuSdkRecyclerView
import org.lasque.tusdk.impl.activity.TuFragmentActivity
import org.lsque.tusdkevademo.utils.PermissionUtils
import java.util.ArrayList


class DemoEntryActivity : ScreenAdapterActivity() {

    val LAYOUT_ID: Int = R.layout.demo_entry_activity

    val MODEL_LIST: ArrayList<String> = arrayListOf("01-jiugongge","03-quweixiatlianlvxing","02-shilitaohua","04-happywedding","05-happywedding","07-lianyuanjie","08-happybirthday","09-shishangchaoliu")
    val NAME_LIST : ArrayList<String> = arrayListOf("九宫格","趣味夏天旅程","十里桃花","HappyWedding","HappyWedding","良缘结","生日快乐","时尚潮流")

    var modelAdapter: ModelAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT_ID)
        initView()
    }

    private fun initView() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE)
        PermissionUtils.requestRequiredPermissions(this, permissions)
        setModelView()
    }

    private fun setModelView() {
        modelAdapter = ModelAdapter(this, ModelItemFactory.getModelItem(MODEL_LIST,NAME_LIST))
        modelAdapter!!.setOnItemClickListener(object : ModelAdapter.OnItemClickListener {
            override fun onClick(view: View, item: ModelItem, position: Int) {
                startActivity<ModelDetailActivity>("model" to item)
            }
        })
        lsq_model_list.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        lsq_model_list.adapter = modelAdapter
    }
}