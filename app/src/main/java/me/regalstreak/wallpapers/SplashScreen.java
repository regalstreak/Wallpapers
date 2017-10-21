package me.regalstreak.wallpapers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    protected static List<DataUrl> data = new ArrayList<>();
    private final String ourDataFilenameNoSlash = "ourdata.json";
    private final String ourDataFilename = "/" + ourDataFilenameNoSlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new AsyncFetch().execute();
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

    private void writeJsonToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(ourDataFilenameNoSlash, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.i("io", "Wrote file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String buffToString(Reader ourReader, boolean save) {
        try {
            BufferedReader reader = new BufferedReader(ourReader);
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // write it to ourdata.json
            if (save) {
                if (!result.toString().equals(null)) {
                    writeJsonToFile(result.toString(), SplashScreen.this);
                }
            }

            return (result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {

        HttpURLConnection connection;
        URL url = null;

        @Override
        protected String doInBackground(String... strings) {

            if (isNetworkAvailable(SplashScreen.this)) {
                try {
                    url = new URL("https://api.myjson.com/bins/11vt6x");
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
                        return buffToString(new InputStreamReader(inputStream), true);

                    } else {
                        File ourData = new File(SplashScreen.this.getFilesDir().getPath() + ourDataFilename);
                        if (ourData.exists()) {
                            return buffToString(new FileReader(ourData), false);
                        } else {
                            Toast.makeText(SplashScreen.this, "Cannot connect to server, please reopen app and try again.", Toast.LENGTH_SHORT).show();
                            return ("unsuccessful");
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return e2.toString();
                } finally {
                    connection.disconnect();
                }
            } else {
                File ourData = new File(SplashScreen.this.getFilesDir().getPath() + ourDataFilename);
                try {
                    return buffToString(new FileReader(ourData), false);

                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
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

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

            } catch (JSONException e) {
                Toast.makeText(SplashScreen.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
