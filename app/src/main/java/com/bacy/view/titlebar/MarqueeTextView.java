package com.bacy.view.titlebar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义跑马灯TextView
 * Created by liyanxi on 2016/11/4.
 * Copyright (c) 2016 www.zhengshijr.com. All rights reserved.
 */

public class MarqueeTextView extends TextView {

    public MarqueeTextView(Context context) {
        this(context, null);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSingleLine();
        setMarqueeRepeatLimit(-1);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
