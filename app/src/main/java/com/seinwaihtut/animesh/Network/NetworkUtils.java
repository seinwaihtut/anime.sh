package com.seinwaihtut.animesh.Network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {
    static public String get(String url, Boolean season){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = "";


        String rawResponseHTML = "";


        Boolean hasNextPage = false;
        Integer currentPageNo = 1;
        Integer lastVisiblePageNo = 100;

        Boolean doPage = true;
        JSONArray animeJSONArray = new JSONArray();
        String tempAnimeArrayString = "[";

        if(season){
//        while(!(currentPageNo == lastVisiblePageNo) || hasNextPage){
        while(doPage){
//            try {
//                JSONObject page = new JSONObject(response);
//                JSONObject pagination = page.getJSONObject("pagination");
//                Boolean has_next_page = pagination.getBoolean("has_next_page");
//
//                if(has_next_page){
//                    String next_page_url = url + "?page="+Integer.toString(pagination.getInt("current_page")+1);
                    String pageUrl = url + "?page="+Integer.toString(currentPageNo);
                    Log.d("NetworkUtils_url", pageUrl);
                    HttpURLConnection pageUrlConnection = null;
                    BufferedReader bufferedReader = null;

                    try {
                        Log.d("NetworkUtils_currentPageNo", Integer.toString(currentPageNo));
                        URL pageRequestURL = new URL(pageUrl);
                        pageUrlConnection = (HttpsURLConnection) pageRequestURL.openConnection();
                        pageUrlConnection.setRequestMethod("GET");
                        pageUrlConnection.connect();
                        InputStream pageInputStream = pageUrlConnection.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(pageInputStream));
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while((line=bufferedReader.readLine())!=null){
                            builder.append(line);
                            builder.append("\n");
                        }
                        if(builder.length()==0){
                            return null;
                        }

                        rawResponseHTML = builder.toString();
                        JSONObject currentPage = new JSONObject(rawResponseHTML);

                        JSONArray tempArray = currentPage.getJSONArray("data");

                        String animeJSONArrayString = currentPage.getJSONArray("data").toString();
                        String tempString = animeJSONArrayString.substring(1, animeJSONArrayString.length()-1);
                        Log.d("NetworkUtils_tempstring", tempString);
                        Log.d("NetworkUtils_tempstring", tempString.substring(tempString.length()-20,  tempString.length()));
                        tempAnimeArrayString +=tempString;


                        JSONObject pagination = currentPage.getJSONObject("pagination");
                        hasNextPage = pagination.getBoolean("has_next_page");
                        currentPageNo = pagination.getInt("current_page");
                        lastVisiblePageNo = pagination.getInt("last_visible_page");
                        if (hasNextPage){
                            currentPageNo+=1;
                            tempAnimeArrayString+=",";
                        }
                        else{
                            doPage = false;
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (pageUrlConnection != null) {
                            pageUrlConnection.disconnect();
                        }
                        if (bufferedReader != null) {
                            try{
                                bufferedReader.close();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }

                        response = tempAnimeArrayString +"]";
                    }

                }

        }
        else{
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
        }
        return response;
    }
}