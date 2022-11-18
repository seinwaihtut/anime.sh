package com.seinwaihtut.animesh;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    Toolbar toolbar;
    private FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        navController = navHostFragment.getNavController();

        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.mainFragment);
        topLevelDestinations.add(R.id.logInMainFragment);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            navController.popBackStack();
            navController.navigate(R.id.login_nested_graph);

        }
    }
}