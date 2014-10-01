package com.kameti.kameti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

public class CallAPI extends AsyncTask<String, String, String> {

    private ApiHandler handler = null;
    private Context app = null;

    CallAPI(ApiHandler handler, Context app) {
        this.handler = handler;
        this.app = app;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0];
        String urlMethod = "GET";
        String payload = null;
        if(params.length > 1) {
            urlMethod = params[1];
        }
        if(params.length > 2) {
            payload = params[2];
        }
        HttpResponse response = null;
        String resultToDisplay = "";
        InputStream in = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            if(urlMethod == "POST" && payload != null) {
                HttpPost httpPost = new HttpPost(urlString);
                StringEntity entity = new StringEntity(payload);
                entity.setContentType("application/json;charset=UTF-8");
                entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httpPost.setEntity(entity);
                response = httpClient.execute(httpPost);
            }
            else if(urlMethod == "PUT" && payload != null) {
                HttpPut httpPut = new HttpPut(urlString);
                StringEntity entity = new StringEntity(payload);
                entity.setContentType("application/json;charset=UTF-8");
                entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httpPut.setEntity(entity);
                response = httpClient.execute(httpPut);
            }
            else if(urlMethod == "DELETE") {
                HttpDelete httpDelete = new HttpDelete(urlString);
                response = httpClient.execute(httpDelete);
            }
            else if(urlMethod == "GET") {
                HttpGet httpGet = new HttpGet(urlString);
                response = httpClient.execute(httpGet);
            }
            if(response != null) {
                in = response.getEntity().getContent();
                resultToDisplay = convertStreamToString(in);
            }
        } catch (UnsupportedEncodingException e) {
            resultToDisplay = "";
        } catch (ClientProtocolException e) {
            resultToDisplay = "";
        } catch (IOException e) {
            resultToDisplay = "";
        }
        return resultToDisplay;
    }

    protected void onPostExecute(String response) {
        if(response != null && !response.isEmpty()) {
            handler.execute(response);
        }
        else{
            Toast.makeText(app, "Connection Failure", Toast.LENGTH_SHORT).show();
        }
        handler.postExecute();
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