package com.meishe.sdkdemo.edit.music;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by ms on 2018/9/10 0010.
 */

public class HandImageView extends AppCompatImageView {
    private Context m_context;

    public HandImageView(Context context) {
        super(context);
        init(context);
    }

    public HandImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        m_context = context;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
