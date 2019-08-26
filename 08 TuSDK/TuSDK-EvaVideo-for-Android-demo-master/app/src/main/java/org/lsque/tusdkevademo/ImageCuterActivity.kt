/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/1$ 19:36$
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.content.Intent
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import kotlinx.android.synthetic.main.activity_image_cuter.*
import kotlinx.android.synthetic.main.title_item_layout.*
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textSizeDimen
import org.lasque.tusdk.core.TuSdkContext
import org.lasque.tusdk.core.struct.TuSdkSize
import org.lasque.tusdk.core.utils.TLog
import org.lasque.tusdk.core.utils.image.BitmapHelper
import org.lasque.tusdk.core.view.TuSdkTouchImageView
import org.lasque.tusdk.core.view.TuSdkTouchImageViewInterface
import org.lasque.tusdk.core.view.TuSdkViewHelper
import org.lasque.tusdk.core.view.widget.TuMaskRegionView
import java.io.File

class ImageCuterActivity : ScreenAdapterActivity() {

    /*************************** view *******************************/
    /** 图片视图  */
    private var mImageView: TuSdkTouchImageView? = null

    /** 图片视图  */
    fun getImageView(): TuSdkTouchImageView? {
        if (mImageView == null) {
            val wrap = this.getImageWrapView()
            if (wrap != null) {
                mImageView = TuSdkTouchImageView(this)
                wrap!!.addView(mImageView, RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                // 需要刷新的关联视图 在某些机型无法刷新上层遮罩透明视图
                mImageView!!.setInvalidateTargetView(this.getCutRegionView())
            }
        }
        return mImageView
    }

    private fun getCutRegionView(): TuMaskRegionView? {
        val width = intent.getIntExtra("width", 0)
        val height = intent.getIntExtra("height", 0)
        lsq_cutRegionView.edgeMaskColor = 0x80000000.toInt()
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
            onRegionLayoutChanged(getCutRegionView())
        }
    }

    /** 裁剪选取视图布局改变  */
    private fun onRegionLayoutChanged(cutRegionView: TuMaskRegionView?) {
        if (cutRegionView == null || this.getImageView() == null) return

        this.getImageView()!!.changeRegionRatio(cutRegionView.regionRect, cutRegionView.regionRatio)
    }

    private fun getImageWrapView(): RelativeLayout {
        lsq_imageWrapView.isClickable = false
        lsq_imageWrapView.clipChildren = false
        return lsq_imageWrapView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_cuter)
        initView()
    }

    private fun initView() {
        val imagePath = intent.getStringExtra("imagePath")
        val width = intent.getIntExtra("width", 0)
        val height = intent.getIntExtra("height", 0)
        getImageView()!!.setImageBitmap(BitmapHelper.getBitmap(File(imagePath)))
        // 选取范围
        val regionRect = this.getCutRegionView()?.setRegionRatio(TuSdkSize(width, height).ratioFloat)
        TLog.e("regionRect = ${regionRect}")
        this.getImageView()?.scaleType = ImageView.ScaleType.CENTER_CROP

        TuSdkViewHelper.setViewRect(this.getImageView(), regionRect)

        var rect: RectF? = RectF(0f, 0f, width.toFloat(), height.toFloat())
        if (rect == null) {
            this.getImageView()!!.setZoom(1f)
        } else if (rect != null) {
            this.getImageView()!!.setZoom(TuSdkSize(width, height).ratioFloat / BitmapHelper.getBitmapSize(File(imagePath)).ratioFloat, (rect.left + rect.right) * 0.5f, (rect.top + rect.bottom) * 0.5f)
        }

        lsq_next.text = "确定"
        lsq_next.textColor = Color.parseColor("#007aff")
        lsq_next.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        lsq_next.textSize = 17f
        lsq_next.setOnClickListener {
            var zoomRect = getImageView()!!.zoomedRect
            var intent = intent
            var bundle = Bundle()
            bundle.putFloatArray("zoom", floatArrayOf(zoomRect.left, zoomRect.top, zoomRect.right, zoomRect.bottom))
            bundle.putString("imagePath",imagePath)
            intent.putExtras(bundle)
            setResult(ModelEditorActivity.ALBUM_REQUEST_CODE_IMAGE, intent)
            finish()
        }
        lsq_back.setOnClickListener { finish() }
        lsq_title_item_title.text = "编辑"
        lsq_change_media.setOnClickListener { finish() }
    }
}