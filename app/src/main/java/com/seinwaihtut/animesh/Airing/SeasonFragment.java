package com.seinwaihtut.animesh.Airing;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.seinwaihtut.animesh.Anime.AnimeActivity;
import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.Network.NetworkUtils;
import com.seinwaihtut.animesh.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SeasonFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ArrayList<Anime> animeObjectArray = new ArrayList<Anime>();

    public SeasonFragment() {
    }

    public static SeasonFragment newInstance(String param1, String param2) {
        SeasonFragment fragment = new SeasonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_season, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.season_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SeasonRecyclerAdapter adapter = new SeasonRecyclerAdapter(animeObjectArray);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SeasonRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                launchAnimeActivity(animeObjectArray.get(position));
            }
        });
        getAnimeList(adapter, "https://api.jikan.moe/v3/season");
    }

    public void launchAnimeActivity(Anime anime) {
        Intent intent = new Intent(getActivity(), AnimeActivity.class);
        intent.putExtra("mal_id", anime.getMal_id());
        intent.putExtra("mal_url", anime.getMal_url());
        intent.putExtra("image_url", anime.getImage_url());
        intent.putExtra("title", anime.getTitle());
        intent.putExtra("score", anime.getScore());
        intent.putExtra("no_episodes", anime.getNo_episodes());
        intent.putExtra("genres", anime.getGenres());
        intent.putExtra("synopsis", anime.getSynopsis());

        startActivity(intent);
    }

    public void getAnimeList(SeasonRecyclerAdapter adapter, String url) {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()
        ) {
            new SeasonAsyncTask(adapter, url).execute();
        } else {
            Log.e("main_activity", "connection error");
        }
    }

    private class SeasonAsyncTask extends AsyncTask<Void, Void, String> {
        SeasonRecyclerAdapter mAdapter;
        String seasonURL;

        SeasonAsyncTask(SeasonRecyclerAdapter adapter, String url) {
            mAdapter = adapter;
            seasonURL = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.get(seasonURL);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            animeObjectArray.clear();
            jikanSeasonHelper(s);
            mAdapter.notifyDataSetChanged();
        }
    }

    private String getDay(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String day = "";
        try {
            Date d1 = df.parse(date);
            DateFormat df2 = new SimpleDateFormat("E");
            day = df2.format(d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    private String getDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String day = "";
        try {
            Date d1 = df.parse(date);
            DateFormat df2 = new SimpleDateFormat("dd-MM-YYYY");
            day = df2.format(d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    public void jikanSeasonHelper(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray animeArray = jsonObject.getJSONArray("anime");

            String mal_id = "";
            String mal_url = "";
            String title = "";
            String image_url = "";
            String synopsis = "N/A";
            String type = "";
            String day = "N/A";
            String episodes = "N/A";
            String score = "N/A";
            String genres = "N/A";
            String date = "N/A";

            for (int i = 0; i < animeArray.length(); i++) {
                ArrayList<String> singleAnimeArray = new ArrayList<>();

                JSONObject anime = animeArray.getJSONObject(i);

                mal_id = anime.getString("mal_id");
                mal_url = anime.getString("url");
                title = anime.getString("title");
                image_url = anime.getString("image_url");
                synopsis = anime.getString("synopsis");
                type = anime.getString("type");
                day = getDay(anime.getString("airing_start"));
                date = getDate(anime.getString("airing_start"));
                episodes = anime.getString("episodes");
                score = anime.getString("score");

                JSONArray genre = anime.getJSONArray("genres");
                genres = "";
                for (int j = 0; j < genre.length(); j++) {
                    genres += genre.getJSONObject(j).getString("name") + ", ";
                }
                if (genres == null) {
                    genres = "";
                }

                singleAnimeArray.add(mal_id);
                singleAnimeArray.add(mal_url);
                singleAnimeArray.add(title);
                singleAnimeArray.add(image_url);
                singleAnimeArray.add(synopsis);
                singleAnimeArray.add(type);
                singleAnimeArray.add(day);
                singleAnimeArray.add(episodes);
                singleAnimeArray.add(score);
                singleAnimeArray.add(genres);

                if (type.equals("TV")) {
                    Anime animeObject = new Anime(mal_id, mal_url, image_url, title, score, episodes, genres, synopsis, date, day);
                    animeObjectArray.add(animeObject);
                }
            }
        } catch (Exception e) {

        }
    }
}