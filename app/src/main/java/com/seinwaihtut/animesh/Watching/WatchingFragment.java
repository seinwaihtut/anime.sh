package com.seinwaihtut.animesh.Watching;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class WatchingFragment extends Fragment {
    private static final String LOGTAG = "WATCHINGFRAGMENT";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference watchingCollection;
    private WatchingFirestoreAdapter watchingFirestoreAdapter;

    NavController navController;

    private SharedViewModel sharedViewModel;
    private WatchingAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String BIOMETRIC_SHARED_PREFERENCES = "BIOMETRIC_SHARED_PREFERENCES";
    private static final String BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG = "BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG";
    SharedPreferences sharedPreferences;


    public WatchingFragment() {
        // Required empty public constructor
    }

    public WatchingAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(BIOMETRIC_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            watchingCollection = db.collection("users").document(user.getEmail()).collection("watching");
            //setUpRecyclerView(view);
            setUpOldRecyclerView(view);

        } else {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG, false);
            editor.apply();

            navController.navigate(R.id.login_nested_graph);
        }


        swipeRefreshLayout = view.findViewById(R.id.watching_swipe_refresh_layout);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sharedViewModel.getCurrentSeason().observe(getViewLifecycleOwner(), new Observer<List<Anime>>() {
                    @Override
                    public void onChanged(List<Anime> networkList) {
                        SeasonAdapter seasonAdapter = SeasonAdapter.getInstance();
                        seasonAdapter.setData(networkList);
                        List<Anime> listFromNetwork = networkList;

                        Query q = watchingCollection.orderBy("title", Query.Direction.ASCENDING);

                        q.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error == null) {
                                    List<DocumentSnapshot> documents = value.getDocuments();
                                    for (DocumentSnapshot document : documents) {
                                        Anime a_f = document.toObject(Anime.class);
                                        for (Anime anime : listFromNetwork) {
                                            if (anime.getMal_id().equals(a_f.getMal_id())) {
                                                watchingCollection.document(anime.getMal_id().toString()).set(anime);

                                            }
                                        }
                                    }
                                }
                            }
                        });

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

//    @Override
//    public void onStart() {
//        super.onStart();
//        watchingFirestoreAdapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        watchingFirestoreAdapter.stopListening();
//    }

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

    public void setUpOldRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.fav_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = WatchingAdapter.getInstance();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new WatchingAdapter.ClickListener() {
            @Override
            public void onItemClick(Anime anime) {
                navController.navigate(MainFragmentDirections.actionMainFragmentToAnimeFragment(anime));
            }
        });

        Query q = watchingCollection.orderBy("title", Query.Direction.ASCENDING);
        q.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    List<Anime> animeListForAdapter = new ArrayList<>();
                    List<DocumentSnapshot> documents = value.getDocuments();
                    for (DocumentSnapshot document : documents) {
                        Anime a = document.toObject(Anime.class);
                        animeListForAdapter.add(a);

                        Log.d(LOGTAG, "Inside setup "+a.getMal_id());
                    }
                    adapter.setAnimes(animeListForAdapter);
                }
            }
        });
    }


}