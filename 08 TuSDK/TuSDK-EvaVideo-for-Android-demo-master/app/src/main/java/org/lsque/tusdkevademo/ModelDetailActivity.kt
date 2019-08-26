/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/1$ 11:58$
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.DisplayMetrics
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.model_detail_activity.*
import kotlinx.android.synthetic.main.title_item_layout.*
import org.jetbrains.anko.startActivity
import org.lasque.tusdk.core.TuSdk
import org.lasque.tusdk.core.seles.output.SelesView
import org.lasque.tusdk.core.utils.TLog
import org.lasque.tusdk.core.utils.ThreadHelper
import org.lasque.tusdk.eva.TuSdkEvaAssetManager
import org.lasque.tusdk.eva.TuSdkEvaPlayerImpl
import org.lasque.tusdk.impl.view.widget.TuMessageHubImpl
import org.lasque.tusdk.utils.TuSdkEvaException
import java.lang.Exception
import kotlin.math.min


class ModelDetailActivity : ScreenAdapterActivity() {

    private val mEvaPlayer = EvaPlayerManager.getInstance()
    private var mEvaAssetManager: TuSdkEvaAssetManager? = null
    private var mPlayerProcessListener: TuSdkEvaPlayerImpl.TuSdkEvaPlayerProgressListener = TuSdkEvaPlayerImpl.TuSdkEvaPlayerProgressListener {
        progress, currentTimeNN, durationNN ->
        lsq_seek.progress = (progress * 100).toInt()
        if (currentTimeNN == durationNN){
            playerPause()
        }
    }

    private fun playerPause() {
        mEvaPlayer.pause()
        lsq_player_img.visibility = View.VISIBLE
    }

    private var isFirst = true

    private var mCurrentModelItem: ModelItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.model_detail_activity)
        initView()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun initView() {
        lsq_title_item_title.text = "模板详情"
        lsq_back.setOnClickListener { finish() }
        val modelItem = intent.getParcelableExtra<ModelItem>("model")
        mCurrentModelItem = modelItem

        mEvaPlayer.setResource(mCurrentModelItem?.modelDir)
        /** 资源加载进度回调 */
        mEvaPlayer.assetManager.setAssetLoadCallback(object : TuSdkEvaAssetManager.TuSdkEvaAssetLoadCallback {

            override fun onError(e: TuSdkEvaException?) {
                TLog.e("[error] ${e!!.message}")
            }

            override fun onLoadCompleted() {
                ThreadHelper.post {
                    TuSdk.messageHub().dismiss()
                    mEvaPlayer.setDisplayContent(lsq_model_seles)
                    mEvaAssetManager = mEvaPlayer.assetManager
                    lsq_model_replace_info.text = "可替换内容：\n" +
                            "\n" +
                            "文字 ${mEvaPlayer.assetManager.replaceTextList.size()}段\n" +
                            "\n" +
                            "图片/视频 ${mEvaPlayer.assetManager.replaceImageList.size() + mEvaPlayer.assetManager.replaceVideoList.size()}个\n" +
                            "\n" +
                            "音频 ${mEvaPlayer.assetManager.replaceAudioList.size()}个"
                    var metrics = DisplayMetrics()
                    windowManager.defaultDisplay.getRealMetrics(metrics)
                    val videoHeight = mEvaPlayer.assetManager.inputSize.height.toFloat()
                    val videoWidth = mEvaPlayer.assetManager.inputSize.width.toFloat()
                    if (videoWidth > videoHeight) {
                        val whp = videoHeight / videoWidth
                        val selesLayoutParame = (lsq_model_seles.layoutParams as ConstraintLayout.LayoutParams)
                        selesLayoutParame.height = (metrics.widthPixels * whp).toInt()
                    } else {
                        if (videoHeight < lsq_model_seles.layoutParams.height){
                            lsq_model_seles.layoutParams.height = videoHeight.toInt()
                            lsq_model_seles.layoutParams.width = min(metrics.widthPixels,videoWidth.toInt())
                        } else {
                            lsq_model_seles.layoutParams.width = min(metrics.widthPixels,(metrics.widthPixels * (lsq_model_seles.layoutParams.height / videoHeight)).toInt())

                        }
                    }
                    mEvaPlayer.selesView.fillMode = SelesView.SelesFillModeType.PreserveAspectRatioAndFill
                    mEvaPlayer.play()
                }
            }

        })
        mEvaPlayer.load()
        lsq_model_name.text = modelItem.modelName
        lsq_seek.progress = 0
        lsq_seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) return
                mEvaPlayer.seek(progress.toFloat() / 100f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                playerPause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                playerPlaying()
            }

        })

        lsq_model_seles.setOnClickListener {
            if (mEvaPlayer.isPause) {
                lsq_player_img.visibility = View.GONE; mEvaPlayer.play()
            } else {
                lsq_player_img.visibility = View.VISIBLE; mEvaPlayer.pause()
            }
        }
        lsq_player_img.setOnClickListener {
            if (mEvaPlayer.isPause) {
                playerPlaying()
            } else {
                playerPause()
            }
        }

        lsq_next_step.setOnClickListener {
            startActivity<ModelEditorActivity>("model" to modelItem)
            finish()
        }
        lsq_next.visibility = View.GONE

        TuSdk.messageHub().setStatus(this@ModelDetailActivity,R.string.lsq_assets_loading)
    }

    private fun playerPlaying() {
        lsq_player_img.visibility = View.GONE
        mEvaPlayer.play()
    }

    override fun onResume() {
        super.onResume()
        mEvaPlayer.setProgressListener(mPlayerProcessListener)
        if (!mEvaPlayer.isPause && !isFirst) {
            playerPlaying()
        }
    }

    override fun onPause() {
        super.onPause()
        playerPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mEvaPlayer.release()
    }

}