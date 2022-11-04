package com.seinwaihtut.animesh;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.seinwaihtut.animesh.Airing.SeasonAdapter;
import com.seinwaihtut.animesh.Airing.SeasonFragment;
import com.seinwaihtut.animesh.Watching.WatchingAdapter;
import com.seinwaihtut.animesh.Watching.WatchingFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment {

    MainFragmentAdapter adapter;
    ViewPager2 viewPager2;
    TabLayout tabLayout;

    SeasonFragment seasonFragment;
    WatchingFragment watchingFragment;

    SeasonAdapter seasonAdapter;
    WatchingAdapter watchingAdapter;

    FirebaseUser user;

    NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final Map<Integer, String> tabLayoutTitles = new HashMap<>();
        tabLayoutTitles.put(0, "Currently Airing");
        tabLayoutTitles.put(1, "Watching");

        watchingFragment = new WatchingFragment();
        seasonFragment = new SeasonFragment();

        ArrayList<Object> fragments = new ArrayList<>();
        fragments.add(seasonFragment);
        fragments.add(watchingFragment);

        adapter = new MainFragmentAdapter(this, fragments);
        viewPager2 = view.findViewById(R.id.main_fragment_viewpager2);
        tabLayout = view.findViewById(R.id.main_fragment_tablayout);


        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabLayoutTitles.get(position))
        ).attach();

        user = FirebaseAuth.getInstance().getCurrentUser();

        navController = Navigation.findNavController(view);
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        seasonAdapter = SeasonAdapter.getInstance();
        watchingAdapter = WatchingAdapter.getInstance();
        switch (item.getItemId()) {

            case R.id.action_bar_search:
                Log.i("MainFragment", "search");
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        seasonAdapter.getFilter().filter(newText);
                        watchingAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                return false;
            case R.id.action_bar_logout:
                Log.i("MainFragment", "logout");
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
                    navController.popBackStack();
                    navController.navigate(R.id.login_nested_graph);

                return false;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

}