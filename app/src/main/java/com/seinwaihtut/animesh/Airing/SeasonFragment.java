package com.seinwaihtut.animesh.Airing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.MainFragmentDirections;
import com.seinwaihtut.animesh.R;
import com.seinwaihtut.animesh.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class SeasonFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    SeasonAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    NavController navController;

    private static final String BIOMETRIC_SHARED_PREFERENCES = "BIOMETRIC_SHARED_PREFERENCES";
    private static final String BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG = "BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG";
    SharedPreferences sharedPreferences;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference watchingCollection;


    public SeasonFragment() {
    }

    public static SeasonFragment newInstance(String param1, String param2) {
        SeasonFragment fragment = new SeasonFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_season, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);
        swipeRefreshLayout = view.findViewById(R.id.season_swipe_refresh_layout);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        sharedPreferences = getActivity().getSharedPreferences(BIOMETRIC_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        if (user!=null){
            watchingCollection = db.collection("users").document(user.getEmail()).collection("watching");

        }else{
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG, false);
            editor.apply();

            navController.navigate(R.id.login_nested_graph);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.season_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = SeasonAdapter.getInstance();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SeasonAdapter.ClickListener() {
            @Override
            public void onItemClick(Anime anime) {
                navController.navigate(MainFragmentDirections.actionMainFragmentToAnimeFragment(anime));
            }
        });
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getCurrentSeason().observe(getViewLifecycleOwner(), new Observer<List<Anime>>() {

            @Override
            public void onChanged(List arrayList) {
                List<Anime> listFromNetwork = arrayList;
                adapter.setData(arrayList);
                Query q = watchingCollection.orderBy("title", Query.Direction.ASCENDING);

                q.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error==null){
                            List<DocumentSnapshot> documents = value.getDocuments();
                            for(DocumentSnapshot document: documents){
                                Anime a_f = document.toObject(Anime.class);
                                for (Anime anime: listFromNetwork){
                                    if (anime.getMal_id().equals(a_f.getMal_id())){
                                        watchingCollection.document(anime.getMal_id().toString()).set(anime);
                                    }
                                }
                            }
                        }
                    }
                });

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //swipeRefreshLayout.setRefreshing(true);
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
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
