package zippler.cn.xs.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by zipple on 2018/3/2.
 */

public class FullVideoView extends VideoView {

    public FullVideoView(Context context) {
        super(context);
    }

    public FullVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FullVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        this.setMeasuredDimension(width, height);
    }
}
