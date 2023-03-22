package com.example.killgame;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

public class ExplosionLoader extends androidx.appcompat.widget.AppCompatImageView {

    public ExplosionLoader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ExplosionLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExplosionLoader(Context context) {
        super(context);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.explostionloader);
        final AnimationDrawable frameAnimation = (AnimationDrawable) getBackground();
        frameAnimation.setOneShot(true);
        post(new Runnable(){
            public void run(){
                frameAnimation.start();
            }
        });
    }
}
