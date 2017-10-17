package me.regalstreak.wallpapers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.io.IOException;

/**
 * Created by regalstreak on 17/10/17.
 */

public class WallSelectDialog extends DialogFragment {

    Context context;
    Bitmap resource;
    View view;

    // **NIMP** TODO: 18/10/17 Use bundle instead of this constructor
    public WallSelectDialog(Context context, Bitmap resource, View view) {
        this.context = context;
        this.resource = resource;
        this.view = view;
    }

    private void setOurWall(int which, int sbMessage) {

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                WallpaperManager.getInstance(context)
                        .setBitmap(resource, null, true, which);
                Snackbar.make(view, sbMessage, Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setOurWall(int sbMessage) {

        try {
            WallpaperManager.getInstance(context)
                    .setBitmap(resource);
            Snackbar.make(view, sbMessage, Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder setWall = new AlertDialog.Builder(context);
        setWall.setTitle(R.string.set_wallpaper)
                .setItems(R.array.set_wallpaper_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch (i) {
                            case 0: {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setOurWall(WallpaperManager.FLAG_SYSTEM, R.string.home_set);
                                    }
                                });
                                break;
                            }
                            case 1: {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setOurWall(WallpaperManager.FLAG_LOCK, R.string.lock_screen_set);
                                    }
                                });
                                break;
                            }
                            case 2: {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setOurWall(R.string.both_set);
                                    }
                                });
                                break;
                            }
                        }

                    }
                });

        return setWall.create();
    }
}
