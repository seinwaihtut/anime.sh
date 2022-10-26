package com.seinwaihtut.animesh.Anime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.DB.AnimeViewModel;
import com.seinwaihtut.animesh.Network.NetworkUtils;
import com.seinwaihtut.animesh.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnimeActivity extends AppCompatActivity {
    ImageView poster;
    TextView title;
    TextView score;
    TextView day_time;
    TextView no_episodes;
    TextView airing_start;
    TextView synopsis;
    TextView genres;
    ImageButton malButton;
    TextView fav_tv;

    private EditText editTextSearch;
    private Button buttonSearch;
    ArrayList<ArrayList<String>> nyaaList;
    private ToggleButton fav;
    private AnimeViewModel mAnimeViewModel;
    Anime animeObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        // Set up the AnimeViewModel.
        mAnimeViewModel = new ViewModelProvider(this).get(AnimeViewModel.class);

        poster = findViewById(R.id.anime_poster);
        title = (TextView) findViewById(R.id.anime_title);
        score = findViewById(R.id.anime_score);
        day_time = findViewById(R.id.anime_day_time); //getanime
        no_episodes = findViewById(R.id.anime_no_episodes);
        airing_start = findViewById(R.id.anime_airing_start); //getanime
        synopsis = findViewById(R.id.anime_synopsis);
        genres = findViewById(R.id.anime_genres);
        malButton = findViewById(R.id.anime_mal_button);
        fav_tv = findViewById(R.id.anime_fav_text);

        editTextSearch = findViewById(R.id.anime_search_plaintext);
        buttonSearch = findViewById(R.id.anime_search_button);
        nyaaList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.anime_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        EpisodeAdapter adapter = new EpisodeAdapter(nyaaList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new EpisodeAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                openMagnet(nyaaList.get(position).get(1));
            }
        });

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {

            animeObject = new Anime((extras.getInt("mal_id")),
                    extras.getString("mal_url"),
                    extras.getString("image_url"),
                    extras.getString("title")
                    );

            Glide.with(poster).load(animeObject.getImage_url()).into(poster);
            title.setText(animeObject.getTitle());

            editTextSearch.setText(animeObject.getTitle());
        }

        if (!(readSharedPrefs("Search_terms", Integer.toString(animeObject.getMal_id())).isEmpty())) {
            editTextSearch.setText(readSharedPrefs("Search_terms", Integer.toString(animeObject.getMal_id())));
        }

        getAnime(airing_start, day_time, buildJikanAnimeQueryString(Integer.toString(animeObject.getMal_id())));


        String searchString = NyaaSearchURLBuilder(editTextSearch.getText().toString());
        search(adapter, searchString);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSharedPrefs("Search_terms", Integer.toString(animeObject.getMal_id()), editTextSearch.getText().toString());
                String search = NyaaSearchURLBuilder(editTextSearch.getText().toString());
                search(adapter, search);
            }
        });

        malButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(animeObject.getMal_url().isEmpty())) {
                    openMAL(animeObject.getMal_url());
                }
            }
        });

        fav = (ToggleButton) findViewById(R.id.add_to_fav);

        if (getToggleButtonStatus(Integer.toString(animeObject.getMal_id()))){
            fav.setChecked(getToggleButtonStatus(Integer.toString(animeObject.getMal_id())));
            fav.setBackgroundDrawable(getDrawable(R.drawable.ic_fav));
            fav_tv.setText("In Watching");
        }else {
            fav.setChecked(getToggleButtonStatus(Integer.toString(animeObject.getMal_id())));
            fav.setBackgroundDrawable(getDrawable(R.drawable.ic_fav_grey));
            fav_tv.setText("Add to Watching");
        }

        fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toggle is enabled
                    mAnimeViewModel.insert(animeObject);

                    fav.setBackgroundDrawable(getDrawable(R.drawable.ic_fav));
                    fav_tv.setText("In Watching");

                    setToggleButtonStatus(Integer.toString(animeObject.getMal_id()), true);
                } else {
                    //Toggle is disabled
                    mAnimeViewModel.deleteAnime(animeObject);
                    fav.setBackgroundDrawable(getDrawable(R.drawable.ic_fav_grey));
                    fav_tv.setText("Add to Watching");

                    setToggleButtonStatus(Integer.toString(animeObject.getMal_id()), false);
                }
            }
        });

    }

    private Boolean getToggleButtonStatus(String mal_id){
        SharedPreferences sharedPreferences = getSharedPreferences("toggle_status", Context.MODE_PRIVATE);
        Boolean status = sharedPreferences.getBoolean(mal_id, false);
        return status;
    }
    private void setToggleButtonStatus(String mal_id, Boolean status){
        SharedPreferences sharedPreferences = getSharedPreferences("toggle_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(mal_id, status);
        editor.apply();
    }


    private void editSharedPrefs(String file_name, String mal_id, String search_string) {
        SharedPreferences sharedPreferences = getSharedPreferences(file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mal_id, search_string);
        editor.apply();
    }

    private String readSharedPrefs(String file_name, String mal_id) {
        SharedPreferences sharedPreferences = getSharedPreferences(file_name, Context.MODE_PRIVATE);
        String searchTerm = sharedPreferences.getString(mal_id, "");

        return searchTerm;
    }


    public void openMAL(String url) {
        Uri mal_url = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, mal_url);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public String buildJikanAnimeQueryString(String id) {
        //return "https://api.jikan.moe/v3/anime/" + id;
        return "https://api.jikan.moe/v4/anime/" + id; // 5/7/2022 updated for jikan api v4
    }

    public void getAnime(TextView airing_start, TextView day_time, String url) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()
        ) {
            new AnimeAsyncTask(airing_start, day_time, url).execute();
            Log.i("AnimeActivity getAnime", "AnimeAsynctask executed");
        } else {
            Log.e("AnimeActiviy getAnime", "connection error");
        }
    }

    public void jikanAnimeHelper(TextView airing_start, TextView day_time, String s) {

        try {
            Log.i("jikanAnimeHelper", s);
            JSONObject jsonObject = new JSONObject(s);
            String broadcast = jsonObject.getJSONObject("data").getJSONObject("broadcast").getString("string");
            Log.i("AnimeActivity jikanAnimeHelper", broadcast);

            String air_start_end = jsonObject.getJSONObject("data").getJSONObject("aired").getString("string");
            Log.i("AnimeActivity jikanAnimeHelper", air_start_end);

            airing_start.setText(air_start_end);
            day_time.setText(broadcast);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private class AnimeAsyncTask extends AsyncTask<Void, Void, String> {
        private String url;
        private TextView airing_start;
        private TextView day_time;

        AnimeAsyncTask(TextView a_start, TextView dt, String jikan_url) {
            url = jikan_url;
            airing_start = a_start;
            day_time = dt;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.get(url, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jikanAnimeHelper(airing_start, day_time, s);
            Log.i("AnimeActivity onPostExecute", "onPostExecute");
        }
    }

    public void openMagnet(String url) {
        Uri magnet = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, magnet);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public String NyaaSearchURLBuilder(String usrSearchString) {
        String nyaa_english_anime_query_url = "https://nyaa.si/?f=0&c=1_2&q=";
        usrSearchString = usrSearchString.replace(" ", "+");
        return nyaa_english_anime_query_url + usrSearchString;
    }

    public void search(EpisodeAdapter adapter, String url) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()
        ) {
            new NyaaAsyncTask(adapter, url).execute();
        } else {
            Log.e("AnimeActivity ", "connection error");
        }
    }

    private class NyaaAsyncTask extends AsyncTask<Void, Void, String> {
        EpisodeAdapter localAdapter;
        String url;

        public NyaaAsyncTask(EpisodeAdapter adapter, String string) {
            localAdapter = adapter;
            url = string;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.get(url, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            nyaaList.clear();
            NyaaHelper(s);
            localAdapter.notifyDataSetChanged();
        }
    }

    private void NyaaHelper(String s) {
        if (!(s == null || s.equals("") || s.trim().equals(""))) {
            try {
                String table = s.substring(s.indexOf("<tbody>") + 7, s.lastIndexOf("</tbody>"));
                String[] tableRows = table.split("</tr>");
                for (String t : tableRows) {
                    ArrayList<String> temp;
                    temp = getSingleRow(t);
                    if (!(temp.isEmpty())) {
                        nyaaList.add(temp);
                    }
                }
            } catch (Exception e) {
                Log.e("NyaaHelper Exception", e.toString());
            }
        }
    }

    private ArrayList<String> getSingleRow(String string) {
        ArrayList<String> single_row = new ArrayList<>();
        if (!(string == null || string.equals("") || string.trim().equals(""))) {
            try {
                String[] td = string.split("</td>");
                single_row.add(td[1].substring(td[1].lastIndexOf("\">") + 2, td[1].lastIndexOf("</a>")));
                single_row.add(td[2].substring(td[2].indexOf("magnet:?"), td[2].lastIndexOf("\"><i class=")));
                single_row.add(td[3].substring(td[3].indexOf("\">") + 2));
                single_row.add(td[4].substring(td[4].lastIndexOf("\">") + 2));
                single_row.add(td[5].substring(td[5].indexOf("\">") + 2));
                single_row.add(td[6].substring(td[6].indexOf("\">") + 2));
            } catch (Exception e) {
                Log.e("getSingleRow Exception", string);
            }
        }
        return single_row;
    }

}