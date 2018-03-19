package me.regalstreak.wallpapers;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
    Button setwallpaper;
    View apnaview;
    String fileplace;
    String fulldescPerm;
    final int writeStorageRequest = 0;
    FloatingActionButton wallstuffshare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_stuff);
        findStuff();
        setCopyrightText();
        setData();
        downloadWall();
        setPreview();
        setWallButton();
        setShareButton();
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
        setwallpaper = findViewById(R.id.setwallpaper);
        wallstuffshare = findViewById(R.id.wallstuffshare);
    }

    private void setData() {
        wallstufftitle.setText(sData.wallName);
        wallstuffsite.setText(sData.wallSite);
        wallstufflink.setText(sData.wallSiteUrl);

        if (sData.wallSite.matches("Pinimg") || sData.wallSite.matches("Imgur")) {
            wallstufflicense.setText(R.string.public_domain);
        } else {
            wallstufflicense.setText(R.string.creative_commons);
        }

        // Load images
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
        SpannableString ss = new SpannableString(getResources().getString(R.string.licence_text));
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

        directory = "/Pictures/" + getResources().getString(R.string.app_name) + "/";
        fileplace = Environment.getExternalStorageDirectory() + directory + sData.wallName + ".png";


        File direct = new File(Environment.getExternalStorageDirectory()
                + directory);

        if (!direct.exists()) {
            direct.mkdir();
        }

        GlideApp.with(this)
                .asBitmap()
                .load(sData.wallURL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, Transition<? super Bitmap> transition) {
                        new AsyncTask<Void, Void, Long>() {

                            @Override
                            protected Long doInBackground(Void... voids) {
                                File file = new File(fileplace);
                                long contentlength = resource.getByteCount();

                                try {
                                    OutputStream os = new FileOutputStream(file);
                                    resource.compress(Bitmap.CompressFormat.PNG, 100, os);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                return contentlength;
                            }

                            @Override
                            protected void onPostExecute(Long contentlength) {
                                super.onPostExecute(contentlength);

                                // Add downloaded wallie to completed downloads, show notif and all
                                DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                manager.addCompletedDownload(
                                        sData.wallName,
                                        "By" + sData.wallSite,
                                        true,
                                        "image/png",
                                        fileplace,
                                        contentlength,
                                        true
                                );

                                // Show snackbar too
                                Snackbar.make(apnaview, "Hello", Snackbar.LENGTH_LONG)
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
                        }.execute();
                    }
                });
    }

    private void downloadWall() {
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apnaview = view;
                if (isPermissionGranted()) {
                    setDownload(sData.wallURL);
                }
            }
        });
    }

    private boolean isPermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                // Explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    fulldescPerm = getResources().getString(R.string.storage_explanation_1)
                            + " "
                            + getResources().getString(R.string.app_name)
                            + " "
                            + getResources().getString(R.string.storage_explanation_2);

                    AlertDialog.Builder builder = new AlertDialog.Builder(WallpaperStuff.this);
                    builder.setMessage(fulldescPerm)
                            .setTitle(R.string.storage_required_title)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(WallpaperStuff.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            writeStorageRequest);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return false;

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            writeStorageRequest);
                    return false;
                }
            }
        } else {
            // Auto grant if pre-MM device
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case writeStorageRequest: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.received_permission, Toast.LENGTH_SHORT).show();
                    setDownload(sData.wallURL);
                } else {
                    Toast.makeText(this, R.string.no_permission, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private void setPreview() {
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WallpaperStuff.this, WallPreview.class);
                startActivity(intent);
            }
        });
    }

    private void setWallButton() {

        setwallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View ourView) {
                GlideApp.with(getApplicationContext())
                        .asBitmap()
                        .load(sData.wallURL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                WallSelectDialog wallSelectDialog = new WallSelectDialog(WallpaperStuff.this, resource, ourView);
                                wallSelectDialog.show(getFragmentManager(), "wallselectdialog");
                            }
                        });
            }
        });
    }

    private void setShareButton() {

        // N TODO: 21/1/18 Host url shortner and branding: https://github.com/YOURLS/YOURLS

        final String shareText = getString(R.string.share_text) + " "
                + getString(R.string.app_name) + " app by "
                + getString(R.string.dev) + ": \n\n"
                + sData.wallName + "\n"
                + sData.wallURL;

        wallstuffshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(sendIntent, "Kaisa bheju bhsdk"));
            }
        });

    }

}
