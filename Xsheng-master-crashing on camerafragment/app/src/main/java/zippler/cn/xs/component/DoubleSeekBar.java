package zippler.cn.xs.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import zippler.cn.xs.R;

/**
 * Created by Zipple on 2018/5/21.
 */

public class DoubleSeekBar extends View {
    private int preX,preY;
    private int currentX,currentX2,currentY;//进度条左右位置
    private Bitmap bitmap1,bitmap2,bitmap3;
    private Canvas canvas;
    private Paint paint;
    private int mScollBarWidth,mScollBarHeight;  //控件宽度=滑动条宽度+滑动块宽度
    private int thumbTop,thunbBootom;//滑块顶部与底部高
    private int offset;//控件的偏移量
    private int progressLow,progressHigh;
    private OnSeekBarChangeListener mBarChangeListener;
    public DoubleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(20);
        bitmap1= BitmapFactory.decodeResource(getResources(),R.mipmap.dark);
        bitmap2= BitmapFactory.decodeResource(getResources(), R.mipmap.red);
        bitmap3= BitmapFactory.decodeResource(getResources(),R.mipmap.thumb);
    }

    //默认执行，计算view的宽高,在onDraw()之前
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        mScollBarWidth = width-offset;
        mScollBarHeight=10;
        currentX=offset;
        currentX2=width-offset;
        offset=20;
        thumbTop=40;
        thunbBootom=100;
        progressLow =0;
        progressHigh=100;
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // 声明一个临时变量来存储计算出的测量值
        int resultWidth = 0;
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {
        }
        return specSize;
    }
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int defaultHeight = 100;
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {
            // defaultHeight = specSize-getPaddingLeft()-getPaddingRight();
            defaultHeight = specSize;
        }
        return defaultHeight;
    }
    //处理控件位置


//    @Override
//    public void layout(int l, int t, int r, int b) {
//        super.layout(l, t, r, b);
//    }

    //处理手势
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x= (int) event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                preX=x;
                break;
            case MotionEvent.ACTION_MOVE:

                if(preX>mScollBarWidth/2){
                    if(x>mScollBarWidth){
                        currentX2=mScollBarWidth;
                    }else{
                        currentX2=x;
                    }
                    //右滑块进度

                    progressHigh=(currentX2)*100/mScollBarWidth;
//
//                   Log.e("tag","currentX2"+currentX2);
//                   Log.e("tag","mScollBarWidth"+mScollBarWidth);
//                   Log.e("tag","progressHigh"+progressHigh);
                }else {
                    if(x<offset){
                        currentX=offset;
                    }else{
                        currentX=x;
                    }
                    //左滑块进度
                    progressLow = (currentX-offset)*100/mScollBarWidth;
//                   Log.e("tag","currentX"+currentX);
//                   Log.e("tag","mScollBarWidth"+mScollBarWidth);
//                   Log.e("tag","progressLow"+progressLow);
                }

                if(mBarChangeListener!=null){
                    mBarChangeListener.onProgressChanged(this,progressLow,progressHigh);
                }
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        Rect rectBack=new Rect(offset+getPaddingLeft(),0,mScollBarWidth-getPaddingRight(),mScollBarHeight);
        canvas.drawBitmap(bitmap1,null,rectBack,paint);
        //绘制前景
        Rect rectRed=new Rect(currentX+getPaddingLeft(),0,currentX2-getPaddingRight(),mScollBarHeight);
        canvas.drawBitmap(bitmap2,null,rectRed,paint);
        //绘制左滑块
        Rect rect1=new Rect(currentX-offset+getPaddingLeft(),thumbTop,currentX+offset+getPaddingLeft(),thunbBootom);
        canvas.drawBitmap(bitmap3,null,rect1,paint);
        //绘制右滑块、getPaddingRight()设置paddingRight,其他同理
        Rect rect2=new Rect(currentX2-offset-getPaddingRight(),thumbTop,currentX2+offset-getPaddingRight(),thunbBootom);
        canvas.drawBitmap(bitmap3,null,rect2,paint);

        invalidate();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
        this.mBarChangeListener = mListener;
    }
    //回调函数，在滑动时实时调用，改变输入框的值
    public interface OnSeekBarChangeListener {
        //滑动时
        public void onProgressChanged(DoubleSeekBar seekBar, int progressLow,
                                      int progressHigh);
    }
}
