package com.example.killgame;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

/**
 * The BatLoader class is a custom ImageView that displays an animated bat loading icon.
 */
public class BatLoader extends androidx.appcompat.widget.AppCompatImageView {

    /**
     * Constructs a new BatLoader with a specified context, attribute set, and default style.
     *
     * @param context The context used to create the view.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyle The default style to apply to this view.
     */
    public BatLoader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Constructs a new BatLoader with a specified context and attribute set.
     *
     * @param context The context used to create the view.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public BatLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructs a new BatLoader with a specified context.
     *
     * @param context The context used to create the view.
     */
    public BatLoader(Context context) {
        super(context);
        init();
    }

    /**
     * Initializes the BatLoader by setting its background resource and starting the animation.
     */
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
