/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/1$ 18:10$
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.RectF
import android.media.MediaCodecInfo
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.text.TextUtils
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.RelativeLayout
import com.alexvasilkov.gestures.GestureController
import com.alexvasilkov.gestures.State
import kotlinx.android.synthetic.main.activity_image_cuter.*
import kotlinx.android.synthetic.main.activity_movie_editor_cut.*
import kotlinx.android.synthetic.main.activity_movie_editor_cut.lsq_change_media
import kotlinx.android.synthetic.main.activity_movie_editor_cut.lsq_cutRegionView
import kotlinx.android.synthetic.main.title_item_layout.*
import org.jetbrains.anko.sdk27.coroutines.onAttachStateChangeListener
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.lasque.tusdk.core.TuSdk
import org.lasque.tusdk.core.TuSdkContext
import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice
import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaFilesCuterImpl
import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaMutableFilePlayer
import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaMutableFilePlayerImpl
import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkMediaMutableFilePlayerImpl.TuSdkMediaPlayerStatus.*
import org.lasque.tusdk.core.media.codec.suit.mutablePlayer.TuSdkVideoImageExtractor
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality
import org.lasque.tusdk.core.media.suit.TuSdkMediaSuit
import org.lasque.tusdk.core.seles.output.SelesSmartView
import org.lasque.tusdk.core.seles.output.SelesView
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource
import org.lasque.tusdk.core.struct.TuSdkSize
import org.lasque.tusdk.core.utils.StringHelper
import org.lasque.tusdk.core.utils.TLog
import org.lasque.tusdk.core.utils.ThreadHelper
import org.lasque.tusdk.core.utils.ThreadHelper.postDelayed
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper.getVideoInfo
import org.lasque.tusdk.core.view.TuSdkViewHelper
import org.lasque.tusdk.core.view.widget.TuMaskRegionView
import org.lasque.tusdk.impl.activity.TuFragmentActivity
import org.lsque.tusdkevademo.playview.TuSdkMovieScrollContent
import org.lsque.tusdkevademo.playview.TuSdkRangeSelectionBar
import org.lsque.tusdkevademo.views.TouchSelesView
import java.io.File
import java.io.IOException
import java.util.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class MovieCuterActivity : ScreenAdapterActivity() {

    //播放器
    private var mVideoPlayer: TuSdkMediaMutableFilePlayer? = null

    //播放视图
    private var mVideoView: SelesView? = null


    /** 当前剪裁后的持续时间   微秒  */
    private var mDurationTimeUs: Long = 0
    /** 左边控件选择的时间     微秒  */
    private var mLeftTimeRangUs: Long = 0
    /** 右边控件选择的时间     微秒 */
    private var mRightTimeRangUs: Long = 0
    /** 最小裁切时间  */
    private val mMinCutTimeUs = (3 * 1000000).toLong()
    /** 裁切工具  */
    private var cuter: TuSdkMediaFilesCuterImpl? = null
    /** 是否已经设置总时间  */
    private var isSetDuration = false
    /** 是否正在裁剪中  */
    private var isCutting = false

    private var mScaleDetector: ScaleGestureDetector? = null

    private var mCurrentZoom = 1.0f
    private var mCurrentX = 0.0f
    private var mCurrentY = 0.0f

    private var mDefaultCropRect: RectF = RectF(0f, 0f, 1f, 1f)
    private var mCurrentCropRect: RectF = RectF(0f, 0f, 1f, 1f)


    //播放器回调
    private val mMediaPlayerListener = object : TuSdkMediaPlayerListener {
        override fun onStateChanged(state: Int) {
            if (!isCutting) lsq_play_btn.setVisibility(if (state == Playing.ordinal) View.GONE else View.VISIBLE)
            mDurationTimeUs = mVideoPlayer!!.durationUs()
        }

        override fun onFrameAvailable() {
            mVideoView!!.requestRender()
        }

        override fun onProgress(playbackTimeUs: Long, mediaDataSource: TuSdkMediaDataSource?, totalTimeUs: Long) {
            val playPercent = playbackTimeUs.toFloat() / totalTimeUs.toFloat()
            lsq_range_line.setPercent(playPercent)
        }

        override fun onCompleted(e: Exception?, mediaDataSource: TuSdkMediaDataSource?) {
            if (e != null) TLog.e(e)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_editor_cut)
        initView()
    }

    private fun initView() {
        lsq_range_line.setType(1)
        lsq_range_line.isShowSelectBar = true
        lsq_range_line.setNeedShowCursor(true)
        lsq_range_line.setProgressChangeListener { percent ->
            if (!mVideoPlayer!!.isPause()) {
                mVideoPlayer!!.pause()
                lsq_play_btn.setVisibility(View.VISIBLE)
            }
            mVideoPlayer!!.seekToPercentage(percent)
        }

        lsq_range_line.setSelectRangeChangedListener { leftPercent, rightPercent, type ->
            if (type == 0) {
                mLeftTimeRangUs = (leftPercent * mVideoPlayer!!.durationUs()).toLong()
                var selectTime = (mRightTimeRangUs - mLeftTimeRangUs) / 1000000.0f
                if (!mVideoPlayer!!.isPause()) {
                    mVideoPlayer!!.pause()
                    lsq_play_btn.setVisibility(View.VISIBLE)
                }
                lsq_range_line.setPercent(leftPercent)
                mVideoPlayer!!.seekToPercentage(leftPercent)
            } else if (type == 1) {
                mRightTimeRangUs = (rightPercent * mVideoPlayer!!.durationUs()).toLong()
                var selectTime = (mRightTimeRangUs - mLeftTimeRangUs) / 1000000.0f
                if (!mVideoPlayer!!.isPause()) {
                    mVideoPlayer!!.pause()
                    lsq_play_btn.setVisibility(View.VISIBLE)
                }
                lsq_range_line.setPercent(rightPercent)
                mVideoPlayer!!.seekToPercentage(rightPercent)
            }
        }

        lsq_range_line.setExceedCriticalValueListener(object : TuSdkRangeSelectionBar.OnExceedCriticalValueListener {
            override fun onMinValueExceed() {

            }

            override fun onMaxValueExceed() {
                val minTime = (mMinCutTimeUs / 1000000).toInt()
                @SuppressLint("StringFormatMatches") val tips = String.format(getString(R.string.lsq_min_time_effect_tips), minTime)
                TuSdk.messageHub()!!.showToast(this@MovieCuterActivity, tips)
            }
        })

        lsq_back.setOnClickListener { finish() }
        lsq_next.setOnClickListener {
            setEnable(false)
            mVideoPlayer!!.pause()
            lsq_play_btn.visibility = View.GONE
            startCompound()
            lsq_editor_cut_load.visibility = View.VISIBLE
        }
        lsq_change_media.setOnClickListener { finish() }
        lsq_play_btn.setOnClickListener {
            if (mVideoPlayer == null) return@setOnClickListener
            if (mVideoPlayer!!.elapsedUs() == mVideoPlayer!!.durationUs()) {
                mVideoPlayer!!.pause()
                //增加延时等待seek时间
                postDelayed({ mVideoPlayer!!.resume() }, 100)
            }
            if (mVideoPlayer!!.isPause()) {
                mVideoPlayer!!.resume()
            } else {
                mVideoPlayer!!.pause()
            }
        }

        lsq_scroll_wrap.controller.addOnStateChangeListener(object : GestureController.OnStateChangeListener {
            override fun onStateReset(oldState: State?, newState: State?) {
                TLog.e("oldState = $oldState newState = $newState")

            }

            override fun onStateChanged(state: State?) {
                TLog.e("state = " + state.toString())
                if (mVideoView!!.width == 0 || mVideoView!!.height == 0) return
                if (state?.zoom!! >1f && mCurrentZoom != state.zoom) {
                    var top = mDefaultCropRect.top
                    var left = mDefaultCropRect.left
                    var bottom = mDefaultCropRect.bottom
                    var right = mDefaultCropRect.right
                    val zoom = (state.zoom - 1) / 4
                    //放大
                    if (mCurrentZoom > state.zoom) {
                        top += (zoom)
                        left += (zoom)
                        bottom -= (zoom)
                        right -= (zoom)
                    } else
                    //缩小
                        if (mCurrentZoom < state.zoom) {
                            top += zoom
                            left += zoom
                            bottom -= zoom
                            right -= zoom
                        }
                    mCurrentCropRect .set(left, top, right, bottom)
                    mCurrentZoom = state.zoom
                } else
                //移动
                    if (mCurrentZoom == state.zoom) {
                        var top = mCurrentCropRect.top
                        var left = mCurrentCropRect.left
                        var bottom = mCurrentCropRect.bottom
                        var right = mCurrentCropRect.right
                        //横向移动
                        if (abs(mCurrentX) > abs(state.x)) {
                            left += abs((abs(mCurrentX) - abs(state.x)) / state.zoom / mVideoView!!.width)
                            left = max(0f,left)
                            right += abs((abs(mCurrentX) - abs(state.x)) / state.zoom / mVideoView!!.width)
                            right = min(1f,right)

                        } else if (abs(mCurrentX) < abs(state.x)) {
                            left -= abs((abs(mCurrentX) - abs(state.x)) / state.zoom /mVideoView!!.width)
                            left = max(0f,left)
                            right -= abs((abs(mCurrentX) - abs(state.x)) /state.zoom / mVideoView!!.width)
                            right = min(1f,right)
                        }
                        //竖向移动
                        if (abs(mCurrentY) > abs(state.y)) {
                            top -= abs((abs(mCurrentY) - abs(state.y)) / state.zoom /mVideoView!!.height)
                            top = max(0f,top)
                            bottom -= abs((abs(mCurrentY) - abs(state.y)) / state.zoom /mVideoView!!.height)
                            bottom = min(1f,bottom)
                        } else if (abs(mCurrentY) < abs(state.y)) {
                            top += abs((abs(mCurrentY) - abs(state.y)) / state.zoom /mVideoView!!.height)
                            top = max(0f,top)
                            bottom += abs((abs(mCurrentY) - abs(state.y)) / state.zoom /mVideoView!!.height)
                            bottom = min(1f,bottom)
                        }
                        mCurrentCropRect = RectF(left, top, right, bottom)
                    } else {
                        mCurrentCropRect = RectF(mDefaultCropRect)
                    }
                mCurrentX = state.x
                mCurrentY = state.y

                TLog.e("currentCrop = $mCurrentCropRect videoViewHeight = ${mVideoView?.height} videoViewWidth = ${mVideoView?.width}")
            }

        })
        val width = intent.getIntExtra("width", 0)
        val height = intent.getIntExtra("height", 0)
        getCutRegionView()
        initPlayer()
        val regionRatio = lsq_cutRegionView.regionRatio
        val regionRect = lsq_cutRegionView.regionRect
        lsq_cutRegionView.visibility = View.VISIBLE
        TLog.e("regionRect = $regionRect regionRatio = $regionRatio width = $width height = $height")
        lsq_scroll_wrap.visibility = View.INVISIBLE
        postDelayed({
            lsq_scroll_wrap.visibility = View.VISIBLE
            val params = lsq_scroll_wrap.layoutParams as ConstraintLayout.LayoutParams
            params.width = lsq_cutRegionView.regionRect.width()
            params.height = lsq_cutRegionView.regionRect.height()
            params.topToBottom = R.id.lsq_title
            params.bottomToTop = R.id.lsq_range_line
            lsq_scroll_wrap.layoutParams  = params
            var percent = lsq_cutRegionView.regionRect.height().toFloat() / mVideoView!!.height.toFloat()
            mCurrentCropRect.bottom = percent
            TLog.e("mDefaultCropRect = $mDefaultCropRect")
//            TuSdkViewHelper.setViewWidth(mVideoView,lsq_cutRegionView.regionRect.width())
//            TuSdkViewHelper.setViewHeight(mVideoView,lsq_cutRegionView.regionRect.height() * w_h_p.toInt())
        },1000)
        loadVideoThumbList()
    }

    override fun onResume() {
        super.onResume()
    }


    private fun startCompound() {
        if (cuter != null) {
            return
        }
        val videoPath = intent.getStringExtra("videoPath")
        isCutting = true

        val sourceList = ArrayList<TuSdkMediaDataSource>()
        sourceList.add(TuSdkMediaDataSource(videoPath))

        // 准备切片时间
        val tuSdkMediaTimeSlice = TuSdkMediaTimeSlice((mLeftTimeRangUs).toLong(), (mRightTimeRangUs).toLong())
        tuSdkMediaTimeSlice.speed = mVideoPlayer!!.speed()

        // 准备裁剪对象
        cuter = TuSdkMediaFilesCuterImpl()
        // 设置裁剪切片时间
        cuter!!.setTimeSlice(tuSdkMediaTimeSlice)
        // 设置数据源
        cuter!!.setMediaDataSources(sourceList)
        // 设置文件输出路径
        cuter!!.setOutputFilePath(getOutputTempFilePath().path)

        // 准备视频格式
        val videoFormat = TuSdkMediaFormat.buildSafeVideoEncodecFormat(cuter!!.preferredOutputSize()!!.width, cuter!!.preferredOutputSize()!!.height,
                30, TuSdkVideoQuality.RECORD_MEDIUM2.bitrate, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface, 0, 0)

        // 设置视频输出格式
        cuter!!.setOutputVideoFormat(videoFormat)
        // 设置音频输出格式
        cuter!!.setOutputAudioFormat(TuSdkMediaFormat.buildSafeAudioEncodecFormat())

        // 开始裁剪
        cuter!!.run(object : TuSdkMediaProgress {
            /**
             * 裁剪进度回调
             * @param progress        进度百分比 0-1
             * @param mediaDataSource 当前处理的视频媒体源
             * @param index           当前处理的视频索引
             * @param total           总共需要处理的文件数
             */
            override fun onProgress(progress: Float, mediaDataSource: TuSdkMediaDataSource?, index: Int, total: Int) {
                ThreadHelper.post {
                    lsq_editor_cut_load.setVisibility(View.VISIBLE)
                    lsq_editor_cut_load_parogress.setValue(progress * 100)
                }
            }

            /**
             * 裁剪结束回调
             * @param e 如果成功则为Null
             * @param outputFile 输出文件路径
             * @param total 处理文件总数
             */
            override fun onCompleted(e: Exception?, outputFile: TuSdkMediaDataSource?, total: Int) {
                isCutting = false
                ThreadHelper.post {
                    setEnable(true)
                    lsq_editor_cut_load.setVisibility(View.GONE)
                    lsq_editor_cut_load_parogress.setValue(0f)
                    lsq_play_btn.setVisibility(if (mVideoPlayer!!.isPause()) View.VISIBLE else View.GONE)
                }
                var intent = Intent()
                var rectf = mCurrentCropRect
                intent.putExtra("videoPath", outputFile?.path)
                intent.putExtra("zoom", floatArrayOf(rectf.left, rectf.top, rectf.right, rectf.bottom))
                setResult(ModelEditorActivity.ALBUM_REQUEST_CODE_VIDEO, intent)
                cuter = null
                finish()
            }
        })
    }

    private var w_h_p = 0f

    /** 初始化播放器  */
    fun initPlayer() {
        val videoPath = intent.getStringExtra("videoPath")
        val sourceList = ArrayList<TuSdkMediaDataSource>()

        val video = TuSdkMediaDataSource(videoPath)
        sourceList.add(video)

        mDurationTimeUs = getVideoFormat(videoPath)!!.getLong(MediaFormat.KEY_DURATION)


        val duration = mDurationTimeUs / 1000000.0f
        mRightTimeRangUs = mDurationTimeUs


        /** 创建预览视图  */
        mVideoView = SelesView(this)
        mVideoView!!.fillMode = SelesView.SelesFillModeType.PreserveAspectRatioAndFill

        mVideoPlayer = TuSdkMediaSuit.playMedia(sourceList, false, mMediaPlayerListener) as TuSdkMediaMutableFilePlayer
        if (mVideoPlayer == null) {
            TLog.e("%s directorPlayer create failed.", "TAG")
            return
        }
        mVideoView!!.isEnableRenderer = true
        mVideoView!!.setRenderer(mVideoPlayer!!.getExtenalRenderer())


        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.height = video.mediaMetadataRetriever.frameAtTime.height
        params.width = video.mediaMetadataRetriever.frameAtTime.width
        w_h_p = video.mediaMetadataRetriever.frameAtTime.width.toFloat() / video.mediaMetadataRetriever.frameAtTime.height.toFloat()
        lsq_scroll_wrap.addView(mVideoView!!, 0, params)
        /** Step 3: 连接视图对象  */
        mVideoPlayer!!.getFilterBridge().addTarget(mVideoView, 0)

    }

    /**
     * 获取视频格式信息
     *
     * @param dataSource
     * 文件地址
     * @return
     */
    private fun getVideoFormat(videoPath: String): MediaFormat? {

        val extractor = MediaExtractor()

        try {
            if (!TextUtils.isEmpty(videoPath))
                extractor.setDataSource(videoPath)
            else
                return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        val videoFormat = getVideoFormat(extractor)
        extractor.release()

        return videoFormat
    }

    /**
     * 获取视频格式
     *
     * @param extractor
     * @return
     */
    private fun getVideoFormat(extractor: MediaExtractor): MediaFormat? {
        for (index in 0 until extractor.trackCount) {
            if (isVideoFormat(extractor.getTrackFormat(index))) {
                return extractor.getTrackFormat(index)
            }
        }
        return null
    }

    /**
     * 判断是否是视频格式
     *
     * @param format
     * @return
     */
    private fun isVideoFormat(format: MediaFormat): Boolean {
        return getMimeTypeFor(format).startsWith("video/")
    }

    /**
     * @param format
     * @return
     */
    private fun getMimeTypeFor(format: MediaFormat): String {
        return format.getString(MediaFormat.KEY_MIME)
    }


    /** 加载视频缩略图  */
    private fun loadVideoThumbList() {
        val videoPath = intent.getStringExtra("videoPath")
        val sourceList = ArrayList<TuSdkMediaDataSource>()
        sourceList.add(TuSdkMediaDataSource(videoPath))

        /** 准备视频缩略图抽取器  */
        val imageThumbExtractor = TuSdkVideoImageExtractor(sourceList)
        imageThumbExtractor
                //.setOutputImageSize(TuSdkSize.create(50,50)) // 设置抽取的缩略图大小
                .setExtractFrameCount(20) // 设置抽取的图片数量
                .setImageListener(object : TuSdkVideoImageExtractor.TuSdkVideoImageExtractorListener {

                    /**
                     * 输出一帧略图信息
                     *
                     * @param videoImage 视频图片
                     * @since v3.2.1
                     */
                    override fun onOutputFrameImage(videoImage: TuSdkVideoImageExtractor.VideoImage) {
                        ThreadHelper.post {
                            lsq_range_line.addBitmap(videoImage.bitmap)
                            lsq_range_line.setMinWidth((mMinCutTimeUs / mDurationTimeUs).toFloat())
                        }
                    }

                    /**
                     * 抽取器抽取完成
                     *
                     * @since v3.2.1
                     */
                    override fun onImageExtractorCompleted(videoImagesList: List<TuSdkVideoImageExtractor.VideoImage>) {
                        /** 注意： videoImagesList 需要开发者自己释放 bitmap  */
                        imageThumbExtractor.release()

                    }
                })
                .extractImages() // 抽取图片
    }

    private fun setEnable(enable: Boolean) {
        lsq_back.isEnabled = enable
        lsq_next.isEnabled = enable
        lsq_play_btn.isEnabled = enable
        lsq_range_line.isEnabled = enable
        lsq_scroll_wrap.isEnabled = enable
    }

    private fun getCutRegionView(): TuMaskRegionView? {
        val width = intent.getIntExtra("width", 0)
        val height = intent.getIntExtra("height", 0)
        lsq_cutRegionView.edgeMaskColor = Color.parseColor("#00ffffff")
        lsq_cutRegionView.edgeSideColor = 0x80FFFFFF.toInt()
        lsq_cutRegionView.setEdgeSideWidthDP(2)
        lsq_cutRegionView.regionSize = TuSdkSize(width, height)
        lsq_cutRegionView.addOnLayoutChangeListener(mRegionLayoutChangeListener)
        return lsq_cutRegionView
    }

    /** 裁剪选取视图布局改变  */
    private var mRegionLayoutChangeListener: View.OnLayoutChangeListener = View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
        // 视图布局改变
        if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
//            onRegionLayoutChanged(getCutRegionView())
        }
    }

    /** 裁剪选取视图布局改变  */
    private fun onRegionLayoutChanged(cutRegionView: TuMaskRegionView?) {
        mVideoView?.displayRect = RectF(cutRegionView!!.regionRect)
    }

    /** 获取临时文件路径  */
    protected fun getOutputTempFilePath(): File {
        return File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", StringHelper.timeStampString()))
    }

    override fun onPause() {
        super.onPause()
        mVideoPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoPlayer?.release()
    }
}