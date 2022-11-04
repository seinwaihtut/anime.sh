package com.seinwaihtut.animesh.LogIn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.seinwaihtut.animesh.R;


public class LogInMainFragment extends Fragment {


    public LogInMainFragment() {
        // Required empty public constructor
    }


    public static LogInMainFragment newInstance() {
        LogInMainFragment fragment = new LogInMainFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);


        Button loginButton = view.findViewById(R.id.login_main_login_button);
        Button signUpButton = view.findViewById(R.id.login_main_sign_up_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(LogInMainFragmentDirections.actionLogInMainFragmentToLogInFragment());

            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate((LogInMainFragmentDirections.actionLogInMainFragmentToSignUpFragment()));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in_main, container, false);
    }


}