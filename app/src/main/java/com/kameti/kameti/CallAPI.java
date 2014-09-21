package com.kameti.kameti;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class CallAPI extends AsyncTask<String, String, String> {

    private ApiHandler handler;
    private Context app;

    CallAPI(ApiHandler h, Context context) {
        handler = h;
        app = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0];
        String resultToDisplay = "";
        InputStream in = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            resultToDisplay = convertStreamToString(in);
        } catch (Exception e ) {
            resultToDisplay = "";
            Toast.makeText(app, "Connection Failure", Toast.LENGTH_SHORT).show();
        }
        return resultToDisplay;
    }

    protected void onPostExecute(String response) {
        if(response != null && !response.isEmpty()) {
            handler.execute(response);
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}