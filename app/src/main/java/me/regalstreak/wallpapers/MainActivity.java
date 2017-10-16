package me.regalstreak.wallpapers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Timeouts are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternet();
    }

    private void checkInternet(){
        if (isNetworkAvailable(this)) {
            new AsyncFetch().execute();
        }else{
            Toast.makeText(this, "Please enable WiFi or Mobile Data", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {

        // TODO: 8/10/17 implement the 'newer' progressdialog alternative
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        HttpURLConnection connection;
        URL url = null;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL("https://api.myjson.com/bins/cxjuh");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            }

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setRequestMethod("GET");

            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.toString();
            }

            try {
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    // Pass to onPostExecute
                    return (result.toString());

                } else {
                    Toast.makeText(MainActivity.this, "Cannot connect to server, please reopen app and try again.", Toast.LENGTH_SHORT).show();
                    return ("unsuccessful");
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                return e2.toString();
            } finally {
                connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            List<DataUrl> data = new ArrayList<>();
            progressDialog.dismiss();

            try {
                JSONArray jsonArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    DataUrl dataUrl = new DataUrl();
                    dataUrl.wallIndex = jsonData.getInt("wall_index");
                    dataUrl.wallName = jsonData.getString("wall_name");
                    dataUrl.wallSite = jsonData.getString("wall_site");
                    dataUrl.wallURL = jsonData.getString("wall_url");
                    dataUrl.wallSiteUrl = jsonData.getString("wall_site_url");
                    data.add(dataUrl);
                }

                // Pass all this to recyclerView
                recyclerView = findViewById(R.id.wallpaperList);
                recyclerAdapter = new RecyclerAdapter(MainActivity.this, data);
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

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
