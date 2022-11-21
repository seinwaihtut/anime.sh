package com.seinwaihtut.animesh;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriPermission;
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
import java.util.List;
import java.util.Map;

public class MainFragment extends Fragment {

    private static final String LOG_TAG = "MainFragment";

    MainFragmentAdapter adapter;
    ViewPager2 viewPager2;
    TabLayout tabLayout;

    SeasonFragment seasonFragment;
    WatchingFragment watchingFragment;

    SeasonAdapter seasonAdapter;
    WatchingAdapter watchingAdapter;

    //FirebaseUser user;

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

        //user = FirebaseAuth.getInstance().getCurrentUser();

        navController = Navigation.findNavController(view);
    }

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

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

            case R.id.action_bar_document_tree:{
                askPermission();

                //openDocumentTree();
                return true;
            }

//            case R.id.action_bar_logout:{
//                Log.i("MainFragment", "logout");
//                    FirebaseAuth.getInstance().signOut();
//                    Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
//                    navController.popBackStack();
//                    navController.navigate(R.id.login_nested_graph);
//
//                return true;}

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    private void askPermission() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 9999);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9999 & resultCode == RESULT_OK) {
            //final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            final int takeFlags = (Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getContext().getContentResolver().takePersistableUriPermission(data.getData(), takeFlags);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("URIPermissions", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("download_uri", data.getDataString());
            editor.apply();
            Log.i("MainFragment", "request:ok");
        }

    }

    private void openDocumentTree() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("URIPermissions", Context.MODE_PRIVATE);
        String download_uri = sharedPreferences.getString("download_uri", "-1");

        if (download_uri.equals("-1")) {
            askPermission();
            Log.i(LOG_TAG, "uri not stored in SharedPreferences");
        }
    }

}