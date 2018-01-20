package me.regalstreak.wallpapers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    // TODO: 17/10/17 offline retry and all

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRecyclerView();

    }

    private void setRecyclerView() {
        SplashScreen splashScreen = new SplashScreen();

        // Pass all this to recyclerView
        recyclerView = findViewById(R.id.wallpaperList);
        recyclerAdapter = new RecyclerAdapter(MainActivity.this, splashScreen.data);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        WallpaperStuff.sData = recyclerAdapter.data.get(position);
                        Intent intent = new Intent(MainActivity.this, WallpaperStuff.class);
                        startActivity(intent);
                    }
                })
        );


    }


}
