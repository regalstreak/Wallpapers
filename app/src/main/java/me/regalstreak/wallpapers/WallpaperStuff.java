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
import android.widget.ImageView;
import android.widget.Toast;

public class WallpaperStuff extends AppCompatActivity {

    protected static DataUrl sData;
    TextView copyright;
    TextView wallstufftitle;
    TextView wallstuffsite;
    TextView wallstufflink;
    ImageView wallstuffimage;
    TextView wallstufflicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_stuff);
        findStuff();
        setCopyrightText();
        setData();
    }

    private void findStuff() {
        copyright = findViewById(R.id.copyright);
        wallstufftitle = findViewById(R.id.wallstufftitle);
        wallstuffsite = findViewById(R.id.wallstuffsite);
        wallstufflink = findViewById(R.id.wallstufflink);
        wallstuffimage = findViewById(R.id.wallstuffimage);
        wallstufflicense = findViewById(R.id.wallstufflicense);
    }

    private void setData() {
        wallstufftitle.setText(sData.wallName);
        wallstuffsite.setText(sData.wallSite);
        wallstufflink.setText(sData.wallSiteUrl);

        if (sData.wallSite.matches("Pinimg") || sData.wallSite.matches("Imgur")) {
            wallstufflicense.setText("Public Domain");
        } else {
            wallstufflicense.setText("Creative Commons License ©");
        }

        // Load images
        // TODO: 2/10/17 caching
        // TODO: 2/10/17 Loading stuff remove mipmap
        // TODO: 2/10/17 Loading percentage
        GlideApp.with(this)
                .asBitmap()
                .load(sData.wallURL)
                .error(R.mipmap.ic_launcher_round)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(wallstuffimage);

    }

    private void setCopyrightText() {
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

        copyright.setText(ss);
        copyright.setMovementMethod(LinkMovementMethod.getInstance());
        copyright.setHighlightColor(Color.TRANSPARENT);
    }

}
