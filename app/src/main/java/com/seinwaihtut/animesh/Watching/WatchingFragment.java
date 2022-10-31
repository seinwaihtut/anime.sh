package com.seinwaihtut.animesh.Watching;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.seinwaihtut.animesh.Airing.SeasonAdapter;
import com.seinwaihtut.animesh.Anime.AnimeActivity;
import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.R;
import com.seinwaihtut.animesh.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class WatchingFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private WatchingAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public WatchingFragment() {
        // Required empty public constructor
    }

    public WatchingAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.watching_swipe_refresh_layout);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fav_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WatchingAdapter();
        recyclerView.setAdapter(adapter);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getAllAnimeWatching().observe(getViewLifecycleOwner(), new Observer<List<Anime>>() {
            @Override
            public void onChanged(List<Anime> anime) {
                adapter.setAnimes(anime);
            }
        });

        adapter.setOnItemClickListener(new WatchingAdapter.ClickListener() {
            @Override
            public void onItemClick(Anime anime) {
                launchAnimeActivity(anime);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sharedViewModel.getTestJSON().observe(getViewLifecycleOwner(), new Observer<List<Anime>>() {
                    @Override
                    public void onChanged(List<Anime> networkList) {
                        SeasonAdapter seasonAdapter = SeasonAdapter.getInstance();
                        seasonAdapter.setData(networkList);

                        List<Anime> listFromNetwork = networkList;

                        Observer tempWatchingObserver = new Observer<List<Anime>>() {
                            @Override
                            public void onChanged(List<Anime> listDB) {
                                List<Anime> listFromDB = listDB;
                                for (Anime anime: listFromDB){
                                    Integer id = anime.getMal_id();
                                    for (Anime a: listFromNetwork){
                                        if (id.equals(a.getMal_id())){
                                            sharedViewModel.update(a);
                                        }
                                    }
                                }
                            }
                        };
                        sharedViewModel.getAllAnimeWatching().observe(getViewLifecycleOwner(), tempWatchingObserver);
                        sharedViewModel.getAllAnimeWatching().removeObserver(tempWatchingObserver);
                    }
                });
                Log.i("31102022:watchingFragment", "SwipeRefreshLayout");
                swipeRefreshLayout.setRefreshing(false);
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watching, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.watching_search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);
                        return false;
                    }
                });


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.watching_action_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}