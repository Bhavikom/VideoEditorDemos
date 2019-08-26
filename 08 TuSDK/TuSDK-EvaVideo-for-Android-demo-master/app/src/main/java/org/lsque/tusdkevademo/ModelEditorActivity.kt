/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/1$ 13:19$
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.RectF
import android.media.AudioManager
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.model_detail_activity.lsq_model_seles
import kotlinx.android.synthetic.main.model_detail_activity.lsq_player_img
import kotlinx.android.synthetic.main.model_detail_activity.lsq_seek
import kotlinx.android.synthetic.main.model_editor_activity.*
import kotlinx.android.synthetic.main.title_item_layout.*
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.textColor
import org.lasque.tusdk.core.TuSdk
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality
import org.lasque.tusdk.core.seles.output.SelesView
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource
import org.lasque.tusdk.core.utils.TLog
import org.lasque.tusdk.core.utils.ThreadHelper
import org.lasque.tusdk.eva.*
import org.lasque.tusdk.eva.structure.TuSdkEvaEntityCompari
import org.lasque.tusdk.eva.structure.TuSdkEvaEntityQueue
import org.lasque.tusdk.utils.TuSdkEvaException
import java.util.*
import kotlin.math.min


class ModelEditorActivity : ScreenAdapterActivity() {

    companion object {
        public const val ALBUM_REQUEST_CODE_IMAGE = 1
        public const val ALBUM_REQUEST_CODE_VIDEO = 2
        public const val AUDIO_REQUEST_CODE = 3
    }

    /**  Eva播放器 */
    private val mEvaPlayer = EvaPlayerManager.getInstance()
    /**  Eva AssetManager */
    private var mEvaAssetManager: TuSdkEvaAssetManager? = null
    /**  Eva播放器进度监听 */
    private var mPlayerProcessListener: TuSdkEvaPlayerImpl.TuSdkEvaPlayerProgressListener = TuSdkEvaPlayerImpl.TuSdkEvaPlayerProgressListener { progress, currentTimeNN, durationNN ->
        lsq_seek.progress = (progress * 100).toInt()
        if (currentTimeNN == durationNN) {
            mEvaPlayer.pause()
            lsq_player_img.visibility = View.VISIBLE
        }
    }

    /**  可修改项列表 */
    private var mEditorList = LinkedList<EditorModelItem>()

    /**  当前图片修改项 */
    private var mCurrentImageEntity: TuSdkEvaImageEntity? = null
    /**  当前视频修改项 */
    private var mCurrentVideoEntity: TuSdkEvaVideoEntity? = null
    /**  当前文字修改项 */
    private var mCurrentTextEntity: TuSdkEvaTextEntity? = null

    /**  当前修改位置 */
    private var mCurrentEditPostion = 0

    /**  修改项列表Adapter */
    private var mEditorAdapter: ModelEditorAdapter? = null

    private var isEnable = true

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) return
        when (requestCode) {
            /**  图片裁剪回调*/
            ALBUM_REQUEST_CODE_IMAGE -> {
                val rectArray = data!!.extras.getFloatArray("zoom")
                mCurrentImageEntity!!.setCropRectF(RectF(rectArray[0], rectArray[1], rectArray[2], rectArray[3]))
                mCurrentImageEntity!!.setReplaceImagePath(data!!.getStringExtra("imagePath"))
            }
            /** 视频裁剪回调 */
            ALBUM_REQUEST_CODE_VIDEO -> {
                when (resultCode) {
                    ALBUM_REQUEST_CODE_VIDEO -> {
                        var rectArray = data!!.getFloatArrayExtra("zoom")
                        mCurrentVideoEntity!!.setCropRectF(RectF(rectArray[0], rectArray[1], rectArray[2], rectArray[3]))
                        mCurrentVideoEntity!!.setVideoPath(data!!.getStringExtra("videoPath"))
                    }

                    ALBUM_REQUEST_CODE_IMAGE -> {
                        var rectArray = data!!.getFloatArrayExtra("zoom")
                        mCurrentVideoEntity!!.setCropRectF(RectF(rectArray[0], rectArray[1], rectArray[2], rectArray[3]))
                        mCurrentVideoEntity!!.setImagePath(data!!.getStringExtra("imagePath"))
                    }
                }
            }
            /** 音频选择回调 */
            AUDIO_REQUEST_CODE -> {
                if (mEvaPlayer.assetManager.replaceAudioList.size() > 0)
                    mEvaPlayer.assetManager.replaceAudioList.queue.peek().audioPath = data!!.extras.getString("audioPath")
            }
        }
        mEditorAdapter?.notifyItemChanged(mCurrentEditPostion)
        /** 资源替换之后,需重新播放 */
        playerReplay()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.model_editor_activity)
        initView()
    }

    private fun playerReplay() {
        mEvaPlayer.seek(0f)
        mEvaPlayer.pause()
        playerPlaying()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun initView() {
        lsq_title_item_title.text = "融合预览"
        lsq_back.setOnClickListener { finish() }
        val modelItem = intent.getParcelableExtra<ModelItem>("model")
        /** 设置资源路径 */
        mEvaPlayer.setResource(modelItem.modelDir)
        mEvaPlayer.assetManager.setAssetLoadCallback(object : TuSdkEvaAssetManager.TuSdkEvaAssetLoadCallback{
            override fun onLoadCompleted() {
                /**  加载可替换项资源完成时回调 */
                ThreadHelper.post {
                    TuSdk.messageHub().dismiss()
                    mEvaAssetManager = mEvaPlayer.assetManager
                    /** 设置播放控件 */
                    mEvaPlayer.setDisplayContent(lsq_model_seles)
                    setEditorList(mEvaPlayer.assetManager.replaceImageList, mEvaPlayer.assetManager.replaceVideoList, mEvaPlayer.assetManager.replaceTextList)
                    mEditorAdapter!!.setEditorModelList(mEditorList)
                    /** 如果没有音频可替换项,则隐藏音频替换按钮 */
                    if (mEvaPlayer.assetManager.replaceAudioList.size() == 0) {
                        lsq_editor_change_bgm.visibility = View.GONE
                    }

                    var metrics = DisplayMetrics()
                    windowManager.defaultDisplay.getRealMetrics(metrics)
                    val videoHeight = mEvaPlayer.assetManager.inputSize.height.toFloat()
                    val videoWidth = mEvaPlayer.assetManager.inputSize.width.toFloat()

                    if (videoWidth > videoHeight) {
                        val whp = videoHeight / videoWidth
                        (lsq_model_seles.layoutParams as ConstraintLayout.LayoutParams).height = (metrics.widthPixels * whp).toInt()
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

            override fun onError(e: TuSdkEvaException?) {
               TLog.e("[error] ${e!!.message}")
            }

        })
        /** 加载资源 */
        mEvaPlayer.load()



        var editorAdapter = ModelEditorAdapter(this, mEditorList)
        editorAdapter.setOnItemClickListener(object : ModelEditorAdapter.OnItemClickListener {

            /** 图片修改项点击事件 */
            override fun onClick(view: View, item: TuSdkEvaImageEntity, position: Int, type: EditType) {
                if (!isEnable) return
                var isOnlyImage = false
                mCurrentEditPostion = position
                when (item.assetType) {
                    EvaAsset.TuSdkEvaAssetType.EvaOnlyImage -> isOnlyImage = true
                    EvaAsset.TuSdkEvaAssetType.EvaVideoImage -> isOnlyImage = false
                }
                mCurrentImageEntity = item
                startActivityForResult<AlbumActivity>(ALBUM_REQUEST_CODE_IMAGE, "onlyImage" to isOnlyImage, "onlyVideo" to false, "width" to item.evaImageAsset.width(), "height" to item.evaImageAsset.height())
            }

            /** 视频修改项点击时间 */
            override fun onClick(view: View, item: TuSdkEvaVideoEntity, position: Int, type: EditType) {
                if (!isEnable) return
                var isOnlyVideo = false
                mCurrentEditPostion = position
                when (item.assetType) {
                    EvaAsset.TuSdkEvaAssetType.EvaOnlyVideo -> isOnlyVideo = true
                    EvaAsset.TuSdkEvaAssetType.EvaVideoImage -> isOnlyVideo = false
                }
                mCurrentVideoEntity = item
                startActivityForResult<AlbumActivity>(ALBUM_REQUEST_CODE_VIDEO, "onlyImage" to false, "onlyVideo" to isOnlyVideo, "width" to mEvaPlayer.assetManager.inputSize.width, "height" to mEvaPlayer.assetManager.inputSize.height)
            }

            /** 文字修改项点击事件 */
            override fun onClick(view: View, item: TuSdkEvaTextEntity, position: Int, type: EditType) {
                if (!isEnable) return
                mCurrentTextEntity = item
                lsq_editor_replace_text.setText(item.displayText)
                lsq_text_editor_layout.visibility = View.VISIBLE
                lsq_editor_replace_text.requestFocus()
                mCurrentEditPostion = position
            }

        })
        mEditorAdapter = editorAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        lsq_editor_item_list.layoutManager = linearLayoutManager
        lsq_editor_item_list.adapter = mEditorAdapter
        lsq_seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) return
                /** seek 到指定位置 播放器内范围(0-1) */
                mEvaPlayer.seek((progress.toFloat() / 100f))
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
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val volume = audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
        lsq_voice_seek.progress = (volume * 10)
        lsq_voice_seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mEvaPlayer.setVolume((progress / 10).toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        lsq_editor_change_bgm.setOnClickListener {
            startActivityForResult<AudioListActivity>(AUDIO_REQUEST_CODE)
            this.overridePendingTransition(R.anim.activity_open_from_bottom_to_top, R.anim.activity_keep_status)
        }

        lsq_editor_text_commit.setOnClickListener {
            mCurrentTextEntity!!.setReplaceText(lsq_editor_replace_text.text.toString())
            lsq_text_editor_layout.visibility = View.GONE
            editorAdapter.notifyItemChanged(mCurrentEditPostion)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(lsq_editor_replace_text.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            playerReplay()
        }

        lsq_next.text = "保存"
        lsq_next.textColor = Color.parseColor("#007aff")


        lsq_next.setOnClickListener {
            setEnable(false)
            /** 保存时必须把播放停止 */
            mEvaPlayer.pause()
            lsq_player_img.visibility = View.VISIBLE
            /** 保存功能 */
            val saver = TuSdkEvaSaverImpl()
            /**  设置保存路径 */
            saver.setOutputFilePath("${Environment.getExternalStorageDirectory().path}/eva_output${System.currentTimeMillis()}.mp4")

            /** 设置保存属性 **/
            var options = TuSdkEvaSaver
                    .TuSdkEvaSaverOptions.getOption()
                    .setOutputSize(mEvaPlayer.assetManager.inputSize)
                    .setQuality(TuSdkVideoQuality.RECORD_HIGH1)
            saver.setSaveOptions(options)

            try {
                /**  设置当前要保存的AssetManager */
                saver.setAssetManager(mEvaPlayer.assetManager.clone())
            } catch (e: CloneNotSupportedException) {
                e.printStackTrace()
            }


            saver.run(object : TuSdkMediaProgress {
                override fun onProgress(progress: Float, mediaDataSource: TuSdkMediaDataSource, index: Int, total: Int) {
                    TLog.e("[debug] progress : %s", progress)
                    ThreadHelper.post {
                        lsq_editor_cut_load.setVisibility(View.VISIBLE)
                        lsq_editor_cut_load_parogress.setValue(progress * 100)
                    }
                }

                override fun onCompleted(e: Exception?, outputFile: TuSdkMediaDataSource, total: Int) {
                    TLog.e("[debug] save completed !!! : %s", outputFile.path)
                    ThreadHelper.post {
                        setEnable(true)
                        lsq_editor_cut_load.setVisibility(View.GONE)
                        lsq_editor_cut_load_parogress.setValue(0f)
                    }
                    finish()
                }
            })
        }

        TuSdk.messageHub().setStatus(this,R.string.lsq_assets_loading)
    }

    private fun setEditorList(replaceImageList: TuSdkEvaEntityQueue<TuSdkEvaImageEntity>, replaceVideoList: TuSdkEvaEntityQueue<TuSdkEvaVideoEntity>, replaceTextList: TuSdkEvaEntityQueue<TuSdkEvaTextEntity>) {
        mEditorList.clear()
        val editorList = TuSdkEvaEntityQueue<TuSdkEvaEntityCompari>()
        /**  遍历可替换图片项列表 */
        for (compari in replaceImageList) {
            editorList.add(compari)
        }
        /**  遍历可替换视频项列表 */
        for (compari in replaceVideoList) {
            editorList.add(compari)
        }
        /**  遍历可替换文字项列表 */
        for (compari in replaceTextList) {
            editorList.add(compari)
        }
        for (compari in editorList) {
            if (compari is TuSdkEvaImageEntity) {
                mEditorList.add(EditorModelItem(compari, EditType.Image))
            } else if (compari is TuSdkEvaVideoEntity) {
                mEditorList.add(EditorModelItem(compari, EditType.Video))
            } else if (compari is TuSdkEvaTextEntity) {
                mEditorList.add(EditorModelItem(compari, EditType.Text))
            }
        }
    }

    private fun setEnable(b: Boolean) {
        lsq_seek.isEnabled = b
        lsq_editor_item_list.isEnabled = b
        lsq_editor_change_bgm.isEnabled = b
        lsq_next.isEnabled = b
        lsq_back.isEnabled = b
        lsq_model_seles.isEnabled = b
        lsq_player_img.isEnabled = b
        isEnable = false
    }

    override fun onResume() {
        super.onResume()
        /**  设置播放进度回调 */
        mEvaPlayer.setProgressListener(mPlayerProcessListener)
    }

    override fun onPause() {
        super.onPause()
        playerPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mEvaPlayer.release()
        TuSdk.getAppTempPath().deleteOnExit()
    }

    private fun playerPause() {
        mEvaPlayer.pause()
        lsq_player_img.visibility = View.VISIBLE
    }

    private fun playerPlaying() {
        lsq_player_img.visibility = View.GONE
        mEvaPlayer.play()
    }
}