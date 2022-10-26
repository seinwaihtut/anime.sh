package com.seinwaihtut.animesh.Airing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.seinwaihtut.animesh.Anime.AnimeActivity;
import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.R;
import com.seinwaihtut.animesh.SharedViewModel;

import java.util.ArrayList;

public class SeasonFragment extends Fragment {

    private SharedViewModel sharedViewModel;
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




        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getCurrentSeason().observe(getViewLifecycleOwner(), new Observer<ArrayList<Anime>>(){

            @Override
            public void onChanged(ArrayList arrayList) {
                    adapter.setData(arrayList);
            }
        });


    }

    public void launchAnimeActivity(Anime anime) {
        Intent intent = new Intent(getActivity(), AnimeActivity.class);
        intent.putExtra("mal_id", anime.getMal_id());
        intent.putExtra("mal_url", anime.getMal_url());
        intent.putExtra("image_url", anime.getImage_url());
        intent.putExtra("title", anime.getTitle());
        startActivity(intent);
    }






}