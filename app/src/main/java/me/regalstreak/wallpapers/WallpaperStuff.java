package me.regalstreak.wallpapers;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class WallpaperStuff extends AppCompatActivity {

    TextView copyright;
    TextView wallstufftitle;
    protected static DataUrl sData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_stuff);
        setCopyright();
        wallstufftitle = findViewById(R.id.wallstufftitle);
        wallstufftitle.setText(sData.wallName);
    }

    private void setCopyright() {
        SpannableString ss = new SpannableString("All the wallpapers shown in the app are either in public domain or under Creative Commons license for which proper attribution is given to the respective uploaders. No wallpaper is distributed with our app. If an image violates any copyright, please report it to us and we will remove it from our database. For more information, click here.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WallpaperStuff.this, "HELLO", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        };

        ss.setSpan(clickableSpan, 335, 339, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        copyright = findViewById(R.id.copyright);
        copyright.setText(ss);
        copyright.setMovementMethod(LinkMovementMethod.getInstance());
        copyright.setHighlightColor(Color.TRANSPARENT);
    }

}
