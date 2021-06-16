package com.seinwaihtut.animesh.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {
    static public String get(String url){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = "";

        try{
            URL requestURL = new URL(url);
            urlConnection = (HttpsURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                builder.append(line);
                builder.append("\n");
            }
            if(builder.length()==0){
                return null;
            }
            response = builder.toString();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try{
                    reader.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return response;
    }
}