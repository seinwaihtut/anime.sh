package com.seinwaihtut.animesh;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private static final String LOGTAG  = "MainActivity";

    private FirebaseUser user;
    private ConstraintLayout mainLayout;

    private static final String BIOMETRIC_SHARED_PREFERENCES = "BIOMETRIC_SHARED_PREFERENCES";
    private static final String BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG = "BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG";

    private Executor executor;
    private BiometricPrompt.PromptInfo promptInfo;
    private BiometricPrompt biometricPrompt;


    NavController navController;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        navController = navHostFragment.getNavController();

        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.mainFragment);
        topLevelDestinations.add(R.id.loginFragment);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences(BIOMETRIC_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences userSharedPreferences = null;
        if (user!=null){
            userSharedPreferences = getSharedPreferences(user.getEmail(), Context.MODE_PRIVATE);
        }

        Boolean biometricEnabled = sharedPreferences.getBoolean(BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG, false);
        if (biometricEnabled == false) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG, false);
            editor.apply();
            if (userSharedPreferences!=null){userSharedPreferences.edit().clear().apply();}
            FirebaseAuth.getInstance().signOut();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Boolean userIsVerified = true;
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userIsVerified = user.isEmailVerified();
        }

        if ((user == null) | !userIsVerified) {
            //navigate to nested login graph if 1) user is not signed in. 2) user's email is not verified.
            FirebaseAuth.getInstance().signOut();
            navController.popBackStack();
            navController.navigate(R.id.login_nested_graph);
        } else {

            SharedPreferences sharedPreferences = getSharedPreferences(BIOMETRIC_SHARED_PREFERENCES, Context.MODE_PRIVATE);
            Boolean biometricEnabled = sharedPreferences.getBoolean(BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG, false);


            promptInfo = new BiometricPrompt.PromptInfo
                    .Builder()
                    .setTitle("Login with Fingerprint")
                    //.setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Login with email and password.")
                    .build();

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {

                BiometricManager biometricManager = BiometricManager.from(this);

                switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
                    case BiometricManager.BIOMETRIC_SUCCESS:
                        Log.d(LOGTAG, "App can authenticate using biometrics.");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Log.d(LOGTAG, "No biometric features available on this device");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Log.d(LOGTAG, "Biometric features are currently unavailable");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                        startActivityForResult(enrollIntent, 9999);
                        break;
                }
            } else {
                Log.d(LOGTAG, "Android version is Q or lower");

                BiometricManager biometricManager = BiometricManager.from(this);

                switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
                    case BiometricManager.BIOMETRIC_SUCCESS:
                        Log.d(LOGTAG, "App can authenticate using biometrics.");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Log.d(LOGTAG, "No biometric features available on this device");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Log.d(LOGTAG, "Biometric features are currently unavailable");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG);
                        startActivityForResult(enrollIntent, 9999);
                        break;
                }
            }

            executor = ContextCompat.getMainExecutor(this);

            BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Log.d(LOGTAG, Integer.toString(errorCode)+errString);


                    //errorCode 10 authentication cancelled - back/ home is pressed without using fingerprint
                    //errorCode 7 too many attempts try again later
                    if (errorCode!=10){

                        FirebaseAuth.getInstance().signOut();
                        sharedPreferences.edit().putBoolean(BIOMETRIC_SHARED_PREFERENCES_ENABLED_FLAG, false).apply();
                        navController.popBackStack();
                        navController.navigate(R.id.login_nested_graph);
                        mainLayout.setVisibility(View.VISIBLE);}
                    else{//Code is 10, exit app
                        finishAffinity();
                    }
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    mainLayout.setVisibility(View.VISIBLE);
//                    Toast.makeText(getApplicationContext(),
//                            "Authentication succeeded!",
//                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();

                    Toast.makeText(getApplicationContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            };

            biometricPrompt = new BiometricPrompt(this,
                    executor,
                    callback
            );

            if (biometricEnabled) {
                mainLayout.setVisibility(View.INVISIBLE);
                biometricPrompt.authenticate(promptInfo);
            }
        }
    }
}