package me.regalstreak.wallpapers;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by regalstreak on 30/9/17.
 */

public class Button extends android.support.v7.widget.AppCompatButton {
    public Button(Context context) {
        super(context);
        setFont();
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    private void setFont(){
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
