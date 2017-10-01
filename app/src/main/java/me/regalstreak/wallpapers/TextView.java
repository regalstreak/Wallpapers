package me.regalstreak.wallpapers;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by regalstreak on 30/9/17.
 */

public class TextView extends android.support.v7.widget.AppCompatTextView {
    public TextView(Context context) {
        super(context);
        setFont();
    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    private void setFont(){
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
