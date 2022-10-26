package com.seinwaihtut.animesh.Watching;

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
import com.seinwaihtut.animesh.DB.AnimeViewModel;
import com.seinwaihtut.animesh.R;

import java.util.List;

public class WatchingFragment extends Fragment {

    private AnimeViewModel mAnimeViewModel;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public WatchingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fav_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        WatchingAdapter adapter = new WatchingAdapter();
        recyclerView.setAdapter(adapter);

        mAnimeViewModel = new ViewModelProvider(this).get(AnimeViewModel.class);
        mAnimeViewModel.getAllAnime().observe(getViewLifecycleOwner(), new Observer<List<Anime>>() {
            @Override
            public void onChanged(List<Anime> anime) {
                adapter.setAnimes(anime);
            }
        });

        adapter.setOnItemClickListener(new WatchingAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                launchAnimeActivity(adapter.getAnimeAtPosition(position));
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

    public static WatchingFragment newInstance(String param1, String param2) {
        WatchingFragment fragment = new WatchingFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watching, container, false);
    }


}