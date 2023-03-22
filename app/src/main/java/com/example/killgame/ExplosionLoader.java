package com.example.killgame;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

/**
 * The ExplosionLoader class is a custom ImageView that displays an animated bat loading icon.
 */
public class ExplosionLoader extends androidx.appcompat.widget.AppCompatImageView {

    /**
     * Constructs a new ExplosionLoader with a specified context, attribute set, and default style.
     *
     * @param context The context used to create the view.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyle The default style to apply to this view.
     */
    public ExplosionLoader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Constructs a new ExplosionLoader with a specified context and attribute set.
     *
     * @param context The context used to create the view.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public ExplosionLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructs a new ExplosionLoader with a specified context.
     *
     * @param context The context used to create the view.
     */
    public ExplosionLoader(Context context) {
        super(context);
        init();
    }

    /**
     * Initializes the ExplosionLoader by setting its background resource and starting the animation.
     */
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
