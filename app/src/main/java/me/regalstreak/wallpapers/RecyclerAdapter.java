package me.regalstreak.wallpapers;

import me.regalstreak.wallpapers.MyAppGlideModule;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    List<DataUrl> data = Collections.emptyList();
    DataUrl current;
    int currentPos = 0;

    public RecyclerAdapter(Context context, List<DataUrl> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_wallpaper, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        DataUrl current = data.get(position);
        myHolder.textView.setText(current.wallName);

        // Load images
        // TODO: 20/9/17 caching 
        // TODO: 20/9/17 Loading stuff remove mipmap
        GlideApp.with(context).load(current.wallURL)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(myHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;


        public MyHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.wallpaperThumb);
            textView = (TextView) itemView.findViewById(R.id.wallpaperTitle);
        }
    }
}
