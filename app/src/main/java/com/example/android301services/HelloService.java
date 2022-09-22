package com.example.android301services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HelloService extends IntentService {
    private static final String serviceName = "HelloService";

    public HelloService() {
        super(serviceName);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String url = intent.getData().toString();
        Log.d("HelloService", url);
        String urlContent = "";
        //urlContent = ConnectionHelper.getUrl(url);
        try {
            URLConnection conn = new URL(url).openConnection();

            InputStream in = conn.getInputStream();
            urlContent = convertStreamToString(in);
        } catch (MalformedURLException e) {
            System.out.println("MALFORMED URL EXCEPTION");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("HelloService :" + urlContent);

        Intent serviceIntent = new Intent(serviceName);
        serviceIntent.putExtra("urlContent", urlContent);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(serviceIntent);
    }

    private static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
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
