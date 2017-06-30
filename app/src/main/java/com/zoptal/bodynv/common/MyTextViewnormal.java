package com.zoptal.bodynv.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zotal.102 on 07/03/17.
 */
public class MyTextViewnormal extends TextView {

    public MyTextViewnormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewnormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewnormal(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/kozuka-gothic-pro-el-58d3a58f649d9.otf");
        setTypeface(tf ,1);

    }
}