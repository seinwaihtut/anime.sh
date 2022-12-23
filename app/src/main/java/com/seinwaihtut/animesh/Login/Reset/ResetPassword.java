package com.seinwaihtut.animesh.Login.Reset;

import static android.view.View.GONE;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.seinwaihtut.animesh.R;
import com.seinwaihtut.animesh.Utils;


public class ResetPassword extends Fragment {
    TextInputLayout emailLayout;
    TextInputEditText emailEditText;
    Button resetButton;
    FirebaseAuth auth;
    Toast toast;
    NavController navController;
    ProgressBar progressBar;


    private static final String LOGTAG = "ResetPassword";

    public ResetPassword() {
        // Required empty public constructor
    }

    public static ResetPassword newInstance(String param1, String param2) {
        ResetPassword fragment = new ResetPassword();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailLayout = view.findViewById(R.id.reset_email_layout);
        emailEditText = view.findViewById(R.id.reset_email_edit_text);
        resetButton = view.findViewById(R.id.reset_reset_button);
        progressBar = view.findViewById(R.id.reset_progressbar);
        progressBar.setVisibility(GONE);
        auth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailLayout.setHelperTextEnabled(false);
                if (Utils.isValidEmail(charSequence)) {
                    emailLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    emailLayout.setEndIconDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_outline_check, null));
                    emailLayout.setEndIconVisible(true);
                } else if (!Utils.isValidEmail(charSequence)) {
                    emailLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                    emailLayout.setEndIconVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Utils.hideKeyboard(getActivity());

                String email;
                email = emailEditText.getText().toString();
                Boolean emailIsValid = false;

                if (email.isEmpty()) {
                    emailLayout.setHelperTextEnabled(true);
                    emailLayout.setHelperText("*Required");
                } else if (!Utils.isValidEmail(email)) {
                    emailLayout.setHelperTextEnabled(true);
                    emailLayout.setHelperText("Email is not valid");
                } else {
                    emailIsValid = Utils.isValidEmail(email);
                    emailLayout.setHelperTextEnabled(false);
                }

                if (emailIsValid) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {


                                new MaterialAlertDialogBuilder(getContext())
                                        .setTitle("Password reset email sent to your email address.")
                                        .setMessage("Check your inbox and click on the email reset link to reset your password.")
                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                    progressBar.setVisibility(GONE);
                                            }
                                        })
                                        .setPositiveButton("Return to Login", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                progressBar.setVisibility(GONE);
                                                FirebaseAuth.getInstance().signOut();
                                                navController.navigate(ResetPasswordDirections.actionResetPasswordToLoginFragment());
                                            }
                                        })
                                        .show();

                            } else {
                                progressBar.setVisibility(GONE);
                                try {
                                    throw task.getException();

                                } catch (FirebaseAuthInvalidUserException e) {
                                    //You should consider this error condition as if the user had signed out of your app, and thus should require them to sign in again if they need to perform any action that requires authentication.
                                    emailLayout.setHelperTextEnabled(true);
                                    emailLayout.setHelperText("Email do not exist.");
                                    e.printStackTrace();
                                } catch (FirebaseNetworkException e) {
                                    Log.d(LOGTAG, "FirebaseNetworkException: " + e.getMessage());
                                    showToast(e.getMessage());
                                } catch (FirebaseTooManyRequestsException e) {
                                    showToast(e.getMessage());
                                } catch (Exception e) {
                                    Log.d(LOGTAG, "Exception: " + e.toString());
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

}