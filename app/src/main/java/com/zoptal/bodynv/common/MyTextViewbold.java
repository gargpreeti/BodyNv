package com.zoptal.bodynv.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zotal.102 on 07/03/17.
 */
public class MyTextViewbold extends TextView {

    public MyTextViewbold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewbold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewbold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/kozuka-gothic-std-m-58d3a5b155623.otf");
        setTypeface(tf ,1);

    }
}