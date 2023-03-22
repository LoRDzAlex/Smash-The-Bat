package com.example.killgame;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

public class BatLoader extends androidx.appcompat.widget.AppCompatImageView {

    public BatLoader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BatLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BatLoader(Context context) {
        super(context);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.batloader);
        final AnimationDrawable frameAnimation = (AnimationDrawable) getBackground();
        post(new Runnable(){
            public void run(){
                frameAnimation.start();
            }
        });
    }
}
