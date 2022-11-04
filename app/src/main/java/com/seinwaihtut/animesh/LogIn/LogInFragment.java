package com.seinwaihtut.animesh.LogIn;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.seinwaihtut.animesh.MainFragment;
import com.seinwaihtut.animesh.R;


public class LogInFragment extends Fragment {

    private FirebaseAuth mAuth;
    public LogInFragment() {
        // Required empty public constructor
    }

    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        EditText emailEditText = view.findViewById(R.id.login_email_edit_text);
        EditText passwordEditText = view.findViewById(R.id.login_password_edit_text);

        Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Boolean email_validated = isValidEmail(email);
                if(!email.isEmpty() && !password.isEmpty() && email_validated){
                mAuth = FirebaseAuth.getInstance();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);
                                    navController.navigate(LogInFragmentDirections.actionLogInFragmentToMainFragment2());
                                    Log.d("LogInFragment", "signInWithEmail:success");
                                }

                                else{
                                    Log.d("LogInFragment", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getContext(), "Please check your email and password", Toast.LENGTH_SHORT);
                                }
                            }
                        });}
                else if(email_validated == false){
                    Toast.makeText(getContext(), "Please check your email", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Please enter email and password", Toast.LENGTH_SHORT);
                }
            }
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}