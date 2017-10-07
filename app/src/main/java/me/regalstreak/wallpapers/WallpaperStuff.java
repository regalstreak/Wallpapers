package me.regalstreak.wallpapers;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class WallpaperStuff extends AppCompatActivity {

    protected static DataUrl sData;
    TextView copyright;
    TextView wallstufftitle;
    TextView wallstuffsite;
    TextView wallstufflink;
    ImageView wallstuffimage;
    TextView wallstufflicense;
    Button download;
    Button preview;
    String directory;
    String extension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_stuff);
        findStuff();
        setCopyrightText();
        setData();
        downloadWall();
        setPreview();
    }

    private void findStuff() {
        copyright = findViewById(R.id.copyright);
        wallstufftitle = findViewById(R.id.wallstufftitle);
        wallstuffsite = findViewById(R.id.wallstuffsite);
        wallstufflink = findViewById(R.id.wallstufflink);
        wallstuffimage = findViewById(R.id.wallstuffimage);
        wallstufflicense = findViewById(R.id.wallstufflicense);
        download = findViewById(R.id.download);
        preview = findViewById(R.id.preview);
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
                .load(sData.wallURL)
                .error(R.mipmap.ic_launcher_round)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(wallstuffimage);

    }

    private void setCopyrightText() {
        SpannableString ss = new SpannableString("All the wallpapers shown in the app are either in public domain or under Creative Commons license for which proper attribution is given to the respective uploaders. No wallpaper is distributed with our app. If an image violates any copyright, please report it to us and we will remove it from our database. For more information, click here.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WallpaperStuff.this, DisclaimerActivity.class);
                startActivity(intent);
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

    private void setDownload(String uRl) {

        // TODO: 4/10/17 Ask for write on external storage permission

        directory = "/Pictures/" + getResources().getString(R.string.app_name);
        extension = sData.wallURL.substring(sData.wallURL.length() - 4);

        if (extension.equals("jpeg")) {
            extension = ".jpeg";
        }

        File direct = new File(Environment.getExternalStorageDirectory()
                + directory);

        if (!direct.exists()) {
            direct.mkdir();
        }

        DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true)
                .setTitle(sData.wallName)
                .setDescription("By " + sData.wallSite)
                .setDestinationInExternalPublicDir(directory, sData.wallName + extension)
                .allowScanningByMediaScanner();

        manager.enqueue(request);
    }

    protected void downloadWall() {
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadImageTask(view).execute();
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Void> {

        // TODO: 2/10/17 Set listener for watching download and snackbar... instead of this asynctask
        private View ourView;

        public DownloadImageTask(View v) {
            ourView = v;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setDownload(sData.wallURL);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Snackbar.make(ourView, "Hello", Snackbar.LENGTH_LONG)
                    // TODO: 4/10/17 Change the below hacky code
                    .setAction("Open", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory() + directory + "/" + sData.wallName + extension), "image/*");
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }

    private void setPreview(){
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WallpaperStuff.this, WallPreview.class);
                startActivity(intent);
            }
        });
    }

}
