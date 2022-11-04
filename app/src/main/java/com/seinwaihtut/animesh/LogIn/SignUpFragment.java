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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.seinwaihtut.animesh.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpFragment extends Fragment {

    FirebaseUser user;
    private FirebaseAuth mAuth;


    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();

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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        EditText usernameEditText = view.findViewById(R.id.register_edit_text_username);
        EditText emailAddressEditText = view.findViewById(R.id.register_edit_text_email_address);
        EditText passwordEditText = view.findViewById(R.id.register_edit_text_password);
        EditText confirmPasswordEditText = view.findViewById(R.id.register_edit_text_confirm_password);
        EditText phoneEditText = view.findViewById(R.id.register_edit_text_phone_no);
        TextView loginTextView = view.findViewById(R.id.register_login_text_button);
        Button registerButton = view.findViewById(R.id.register_register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailAddressEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm_password = confirmPasswordEditText.getText().toString();
                String phoneNo = phoneEditText.getText().toString();
                Boolean email_validated = isValidEmail(email);
                Boolean phone_validated = isValidPhone(phoneNo);


                if (email_validated && phone_validated && password.equals(confirm_password)) {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("SignUpFragment", "createUserWithEmail:success");
                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);
                                navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment());
                            } else {
                                Log.w("SignUpFragment", "createUserWithEmail:failure", task.getException());

                            }
                        }
                    });


                } else {
                    Toast.makeText(getContext(), "Please check your input fields", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPhone(String phoneNo) {
        Pattern phonePattern = Pattern.compile("^((09|\\+?959)?\\d{7,10})$");
        Matcher matcher = phonePattern.matcher(phoneNo);
        return matcher.matches();
    }
}