package com.seinwaihtut.animesh.Watching;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.seinwaihtut.animesh.Airing.SeasonAdapter;
import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.DB.Test;
import com.seinwaihtut.animesh.MainFragmentDirections;
import com.seinwaihtut.animesh.R;
import com.seinwaihtut.animesh.SharedViewModel;

import java.util.List;

public class WatchingFragment extends Fragment {
    private static final String LOGTAG = "WATCHINGFRAGMENT";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference watchingCollection = db.collection("users").document("seinw.htut@gmail.com").collection("watching");
    private WatchingFirestoreAdapter watchingFirestoreAdapter;

    NavController navController;

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

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);

        setUpRecyclerView(view);

        swipeRefreshLayout = view.findViewById(R.id.watching_swipe_refresh_layout);
//
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fav_recycler_view);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = WatchingAdapter.getInstance();
//        recyclerView.setAdapter(adapter);
//
//        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
//        sharedViewModel.getAllAnimeWatching().observe(getViewLifecycleOwner(), new Observer<List<Anime>>() {
//            @Override
//            public void onChanged(List<Anime> anime) {
//                adapter.setAnimes(anime);
//            }
//        });
//
//        adapter.setOnItemClickListener(new WatchingAdapter.ClickListener() {
//            @Override
//            public void onItemClick(Anime anime) {
//                navController.navigate(MainFragmentDirections.actionMainFragmentToAnimeFragment(anime));
//
//            }
//        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sharedViewModel.getCurrentSeason().observe(getViewLifecycleOwner(), new Observer<List<Anime>>() {
                    @Override
                    public void onChanged(List<Anime> networkList) {
                        SeasonAdapter seasonAdapter = SeasonAdapter.getInstance();
                        seasonAdapter.setData(networkList);

                        List<Anime> listFromNetwork = networkList;

                        Observer tempWatchingObserver = new Observer<List<Anime>>() {
                            @Override
                            public void onChanged(List<Anime> listDB) {
                                List<Anime> listFromDB = listDB;
                                for (Anime anime : listFromDB) {
                                    Integer id = anime.getMal_id();
                                    for (Anime a : listFromNetwork) {
                                        if (id.equals(a.getMal_id())) {
                                            watchingCollection.document(anime.getMal_id().toString()).set(anime);
                                        }
                                    }
                                }
                            }
                        };
                        sharedViewModel.getAllAnimeWatching().observe(getViewLifecycleOwner(), tempWatchingObserver);
                        sharedViewModel.getAllAnimeWatching().removeObserver(tempWatchingObserver);
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        watchingFirestoreAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        watchingFirestoreAdapter.stopListening();
    }

    public void setUpRecyclerView(View view) {

        Query q = watchingCollection.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Anime> options = new FirestoreRecyclerOptions.Builder<Anime>()
                .setQuery(q, Anime.class)
                .build();
        watchingFirestoreAdapter = new WatchingFirestoreAdapter(options);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fav_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(watchingFirestoreAdapter);

        watchingFirestoreAdapter.setOnItemClickListener(new WatchingFirestoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Anime anime = documentSnapshot.toObject(Anime.class);
                navController.navigate(MainFragmentDirections.actionMainFragmentToAnimeFragment(anime));

            }
        });


    }
}