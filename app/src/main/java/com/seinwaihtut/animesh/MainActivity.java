package com.seinwaihtut.animesh;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.seinwaihtut.animesh.LogIn.LogInMainFragment;

public class MainActivity extends AppCompatActivity {
    private Boolean loggedIn = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            currentUser.reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if(loggedIn){
            displayMainFragment();
        }
        else{
            displayLogInFragment();
        }



    }
    public void displayMainFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment fragment = new MainFragment();
        fragmentTransaction.add(R.id.main_activity_fragment_container, fragment);
        fragmentTransaction.commit();
    }
    public void displayLogInFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LogInMainFragment logInFragment = LogInMainFragment.newInstance();
        fragmentTransaction.add(R.id.main_activity_fragment_container, logInFragment);
        fragmentTransaction.commit();
    }
}