package com.seinwaihtut.animesh.Anime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.DB.EpisodePOJO;
import com.seinwaihtut.animesh.R;
import com.seinwaihtut.animesh.SharedViewModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimeFragment extends Fragment {
    ImageView poster;
    TextView titleTV;
    TextView synopsisTV;
    TextView jpTitle;
    TextView type;
    TextView source;
    TextView score;
    TextView episodes;
    TextView season;
    TextView aired_string;
    TextView broadcast;
    TextView genres;

    SharedViewModel sharedViewModel;

    ToggleButton favToggleButton;
    TextView favTextView;
    ImageButton malImageButton;

    EditText searchEditText;
    Button searchButton;

    String SHARED_PREFS_EPISODES_FILE = "SHARED_PREFS_EPISODES_FILE";

    public AnimeFragment() {
        // Required empty public constructor
    }


    public static AnimeFragment newInstance(String param1, String param2) {
        AnimeFragment fragment = new AnimeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anime, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Anime anime = AnimeFragmentArgs.fromBundle(getArguments()).getAnimeParcelable();
        poster = view.findViewById(R.id.iv_fragment_anime_image);
        titleTV = view.findViewById(R.id.tv_fragment_anime_title);
        synopsisTV = view.findViewById(R.id.tv_fragment_anime_synopsis);
        jpTitle = view.findViewById(R.id.tv_fragment_anime_jp_title);
        type = view.findViewById(R.id.tv_anime_fragment_type);
        source = view.findViewById(R.id.tv_anime_fragment_source);
        score = view.findViewById(R.id.tv_anime_fragment_score);
        episodes = view.findViewById(R.id.tv_anime_fragment_episodes);
        season = view.findViewById(R.id.tv_anime_fragment_season);
        aired_string = view.findViewById(R.id.tv_anime_fragment_aired_string);
        broadcast = view.findViewById(R.id.tv_anime_fragment_broadcast);
        genres = view.findViewById(R.id.tv_anime_fragment_genres);

        favToggleButton = view.findViewById(R.id.anime_fragment_fav_button);
        favTextView = view.findViewById(R.id.tv_anime_fragment_fav_text);
        malImageButton = view.findViewById(R.id.anime_fragment_open_mal_button);

        searchEditText = view.findViewById(R.id.et_anime_fragment_anime_search);
        searchButton = view.findViewById(R.id.bt_anime_fragment_search_button);

        Glide.with(poster).load(anime.getImage_url()).into(poster);
        titleTV.setText(anime.getTitle());
        synopsisTV.setText("Synopsis: " + anime.getSynopsis());
        jpTitle.setText(anime.getTitle_jp());
        type.setText(anime.getType());
        source.setText(anime.getSource());
        score.setText("Score: "+anime.getScore().toString());
        episodes.setText("Episodes: "+anime.getEpisodes().toString());
        if(anime.getSeason().isEmpty()||anime.getYear().equals(0)){

        }else{
            season.setText("Season: "+anime.getSeason()+" "+anime.getYear().toString());
        }
        aired_string.setText("Aired From: "+anime.getAired_string());
        broadcast.setText(anime.getBroadcast_string());
        genres.setText("Genres: "+anime.getGenres());

        //Fav button initial state
        sharedViewModel.queryAnimeInDB(anime.getMal_id()).observe(getViewLifecycleOwner(), new Observer<Anime>() {
            @Override
            public void onChanged(Anime a) {
                if (a!=null){
                if (anime.getMal_id().equals(a.getMal_id())){
                    favToggleButton.setChecked(true);
                    favToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fav));
                    favTextView.setText("In Watching");
                }else{
                    favToggleButton.setChecked(false);
                    favToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fav_grey));
                    favTextView.setText("Add to Watching");
                }
            }}
        });

        favToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sharedViewModel.insert(anime);
                    favToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fav));
                    favTextView.setText("In Watching");
                }else{
                    sharedViewModel.delete(anime);
                    favToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fav_grey));
                    favTextView.setText("Add to Watching");
                }
            }
        });

        malImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(anime.getMal_url().isEmpty())){
                    openMAL(anime.getMal_url());
                }
            }
        });


        //The following code block deals with episode recycler view and its related trigger events
        List<EpisodePOJO> nyaaList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.rv_anime_fragment_episodes_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        EpisodeAdapter adapter = EpisodeAdapter.getInstance();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new EpisodeAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                openMagnet(adapter.getItemAtPosition(position).getMagnet_url());
            }
        });


        searchEditText.setText(anime.getTitle());
        if (checkSharedPrefs(SHARED_PREFS_EPISODES_FILE,anime.getMal_id().toString())){
            //When shared prefs is not empty, when user already used search
            searchEditText.setText(readSharedPrefs(SHARED_PREFS_EPISODES_FILE, anime.getMal_id().toString()));
        }
        new NyaaAsyncTask().execute(searchEditText.getText().toString());         //Load the data upon opening the fragment

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_string = searchEditText.getText().toString();
                editSharedPrefs(SHARED_PREFS_EPISODES_FILE, anime.getMal_id().toString(), search_string);
                new NyaaAsyncTask().execute(search_string);
            }
        });
    }



    private void openMagnet(String url){
        Uri magnet = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, magnet);
        if (intent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(intent);
        }
    }

    private void openMAL(String url){
        Uri mal_url = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, mal_url);
        if (intent.resolveActivity(getContext().getPackageManager())!=null){
            startActivity(intent);
        }
    }

    private void editSharedPrefs(String file_name, String mal_id, String search_string){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mal_id, search_string);
        editor.apply();
    }

    private String readSharedPrefs(String file_name, String mal_id){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(file_name, Context.MODE_PRIVATE);
        String search_string = sharedPreferences.getString(mal_id, "");
        return search_string;
    }

    private Boolean checkSharedPrefs(String file_name, String mal_id){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(file_name, Context.MODE_PRIVATE);
        return sharedPreferences.contains(mal_id);
    }


    private static class NyaaAsyncTask extends AsyncTask<String, Void, List<EpisodePOJO>> {
        String NYAA_BASE_URL = "https://nyaa.si/"; //"https://nyaa.si/?f=0&c=1_2&q=yourself"
        private NyaaAsyncTask() {
        }

        @Override
        protected List<EpisodePOJO> doInBackground(String... s) {
            List<EpisodePOJO> episodeList = new ArrayList<>();
            //String search_string = s[0].replace(" ", "+");
            Uri uri = Uri.parse(NYAA_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter("f", "0")
                    .appendQueryParameter("c", "1_2")
                    .appendQueryParameter("q", s[0])
                    .appendQueryParameter("p", "1").build();

            String url = uri.toString();
            try {
                Document document = Jsoup.connect(url).get();
                Elements rows = document.select("table tbody tr");
                for (Element row : rows) {
                    String nyaa_url = row.select("td:nth-of-type(2)>a:last-of-type").attr("href");
                    String title = row.select("td:nth-of-type(2)>a:last-of-type").text();
                    String torrent_url = row.select("td:nth-of-type(3) > a:first-of-type").attr("href");
                    String magnet_url = row.select("td:nth-of-type(3) > a:last-of-type").attr("href");
                    String upload_time = row.select("td:nth-of-type(5)").text();
                    String size = row.select("td:nth-of-type(4)").text();
                    Integer seeders = Integer.valueOf(row.select("td:nth-of-type(6)").text());
                    Integer leechers = Integer.valueOf(row.select("td:nth-of-type(7").text());
                    Integer completed_downloads = Integer.valueOf(row.select("td:nth-of-type(8)").text());

                    EpisodePOJO episodePOJO = new EpisodePOJO(title, magnet_url);
                    episodePOJO.setNyaa_url(nyaa_url);
                    episodePOJO.setTorrent_url(torrent_url);
                    episodePOJO.setUpload_time(upload_time);
                    episodePOJO.setSize(size);
                    episodePOJO.setSeeders(seeders);
                    episodePOJO.setLeechers(leechers);
                    episodePOJO.setCompleted_downloads(completed_downloads);
                    episodeList.add(episodePOJO);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return episodeList;
        }

        @Override
        protected void onPostExecute(List<EpisodePOJO> episodePOJOS) {
            super.onPostExecute(episodePOJOS);
            EpisodeAdapter adapter = EpisodeAdapter.getInstance();
            adapter.setData(episodePOJOS);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}