package zippler.cn.xs.component;

/**
 * Created by zipple on 2018/3/4.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class CircleImageView extends ImageView {
    private Paint mPaintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mRawBitmap;
    private BitmapShader mShader;
    private Matrix mMatrix = new Matrix();
    private float mBorderWidth = dip2px(15);
    private int mBorderColor = 0x00FFFFFF;

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap rawBitmap = getBitmap(getDrawable());
        if (rawBitmap != null){
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            int viewMinSize = Math.min(viewWidth, viewHeight);
            float dstWidth = viewMinSize;
            float dstHeight = viewMinSize;
            if (mShader == null || !rawBitmap.equals(mRawBitmap)){
                mRawBitmap = rawBitmap;
                mShader = new BitmapShader(mRawBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            }
            if (mShader != null){
                mMatrix.setScale((dstWidth - mBorderWidth * 2) / rawBitmap.getWidth(), (dstHeight - mBorderWidth * 2) / rawBitmap.getHeight());
                mShader.setLocalMatrix(mMatrix);
            }
            mPaintBitmap.setShader(mShader);
            mPaintBorder.setStyle(Paint.Style.STROKE);
            mPaintBorder.setStrokeWidth(mBorderWidth);
            mPaintBorder.setColor(mBorderColor);
            float radius = viewMinSize / 2.0f;
            canvas.drawCircle(radius, radius, radius - mBorderWidth / 2.0f, mPaintBorder);//drawRoundRect
            canvas.translate(mBorderWidth, mBorderWidth);
            canvas.drawCircle(radius - mBorderWidth, radius - mBorderWidth, radius - mBorderWidth, mPaintBitmap);
        } else {
            super.onDraw(canvas);
        }
    }

    private Bitmap getBitmap(Drawable drawable){
        if (drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        } else if (drawable instanceof ColorDrawable){
            Rect rect = drawable.getBounds();
            int width = rect.right - rect.left;
            int height = rect.bottom - rect.top;
            int color = ((ColorDrawable)drawable).getColor();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
            return bitmap;
        } else {
            return null;
        }
    }

    private int dip2px(int dipVal)
    {
        float scale = getResources().getDisplayMetrics().density;
        return (int)(dipVal * scale + 0.5f);
    }
}
