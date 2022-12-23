package com.seinwaihtut.animesh.Login.Register;

import static android.view.View.GONE;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.auth.FirebaseUser;
import com.seinwaihtut.animesh.R;
import com.seinwaihtut.animesh.Utils;


public class RegisterFragment extends Fragment {
    Toast toast;
    private static final String LOGTAG = "RegisterFragment";
    NavController navController;
    FirebaseAuth auth;
    TextInputEditText emailEditText, passwordEditText, confirmPasswordEditText;
    TextInputLayout emailLayout, passwordLayout, confirmPasswordLayout;
    TextView warningText;
    CheckBox checkBox;
    ProgressBar progressBar;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

//        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                Log.d(LOGTAG, "OnAuthStateChanged");
//                if (user != null) {
//                    if (!user.isEmailVerified()) {
//                        Log.d(LOGTAG, "User is not null and email is not verified.");
//                        sendVerificationEmail();
//                    }
//                }
//            }
//        };
//
//        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        progressBar = view.findViewById(R.id.register_progressbar);
        progressBar.setVisibility(GONE);

        TextView termsView = view.findViewById(R.id.register_terms_text);

        checkBox = view.findViewById(R.id.checkBox);
        warningText = view.findViewById(R.id.register_termsnandconditions_warning_text);

        emailEditText = view.findViewById(R.id.register_email_text);
        passwordEditText = view.findViewById(R.id.register_password_text);
        confirmPasswordEditText = view.findViewById(R.id.register_confirm_password_text);
        emailLayout = view.findViewById(R.id.register_email_layout);
        passwordLayout = view.findViewById(R.id.register_password_layout);
        confirmPasswordLayout = view.findViewById(R.id.register_confirm_password_layout);

        termsView.setMovementMethod(LinkMovementMethod.getInstance());

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
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordLayout.setHelperTextEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPasswordLayout.setHelperTextEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        auth = FirebaseAuth.getInstance();


        view.findViewById(R.id.register_register_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Utils.hideKeyboard(getActivity());


                String email, password, confirmPassword;
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();

                Boolean passwordsAreEqual = false;
                Boolean emailIsValid = false;

                if (email.isEmpty()) {
                    emailLayout.setHelperTextEnabled(true);
                    emailLayout.setHelperText("*Required");
                } else if (!Utils.isValidEmail(email)) {
                    emailLayout.setHelperTextEnabled(true);
                    emailLayout.setHelperText("Email is not valid.");
                } else {
                    emailIsValid = Utils.isValidEmail(email);
                    emailLayout.setHelperTextEnabled(false);
                }

                if (password.isEmpty()) {
                    passwordLayout.setHelperTextEnabled(true);
                    passwordLayout.setHelperText("*Required");
                } else {
                    passwordLayout.setHelperTextEnabled(false);
                }

                if (confirmPassword.isEmpty()) {
                    confirmPasswordLayout.setHelperTextEnabled(true);
                    confirmPasswordLayout.setHelperText("*Required");
                } else {
                    confirmPasswordLayout.setHelperTextEnabled(false);
                }

                if (!password.isEmpty() & !confirmPassword.isEmpty()) {
                    passwordsAreEqual = password.equals(confirmPassword);
                    if (!passwordsAreEqual) {
                        passwordLayout.setHelperTextEnabled(true);
                        confirmPasswordLayout.setHelperTextEnabled(true);
                        passwordLayout.setHelperText("*Passwords do not match.");
                        confirmPasswordLayout.setHelperText("*Passwords do not match");
                    } else {
                        passwordLayout.setHelperTextEnabled(false);
                        confirmPasswordLayout.setHelperTextEnabled(false);
                    }
                }

                if (!checkBox.isChecked()){
                    warningText.setVisibility(View.VISIBLE);
                }else{
                    warningText.setVisibility(GONE);
                }

                if (passwordsAreEqual & emailIsValid & !email.isEmpty() & !password.isEmpty() & !confirmPassword.isEmpty() & checkBox.isChecked()) {

                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Log.d(LOGTAG, "Register task is successful.");

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    if (!user.isEmailVerified()) {
                                        Log.d(LOGTAG, "User is not null and email is not verified.");
                                        sendVerificationEmail();
                                    }
                                }

                                //FirebaseAuth.getInstance().signOut(); //Send verification email to signed in user.
                                //navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToRegisterCheckEmailFragment());
                            } else {
                                progressBar.setVisibility(GONE);

                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthActionCodeException e) {
                                    //Represents the exception which is a result of an expired or an invalid out of band code.
                                    showToast(e.getMessage());
                                } catch (FirebaseAuthEmailException e) {
                                    //Represents the exception which is a result of an attempt to send an email via Firebase Auth (e.g. a password reset email)
                                    e.printStackTrace();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    //Thrown when using a weak password (less than 6 chars) to create a new account or to update an existing account's password. Use getReason() to get a message with the reason the validation failed that you can display to your users.
                                    passwordLayout.setHelperTextEnabled(true);
                                    passwordLayout.setHelperText(e.getReason());
                                    confirmPasswordLayout.setHelperTextEnabled(true);
                                    confirmPasswordLayout.setHelperText(e.getReason());
                                    e.printStackTrace();
                                } catch (FirebaseAuthInvalidCredentialsException e) {//Not applicable to register
                                    //Thrown when one or more of the credentials passed to a method fail to identify and/or authenticate the user subject of that operation. Inspect the error code and message to find out the specific cause.
                                    e.printStackTrace();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    //You should consider this error condition as if the user had signed out of your app, and thus should require them to sign in again if they need to perform any action that requires authentication.
                                    e.printStackTrace();
                                } catch (FirebaseAuthMultiFactorException e) {
                                    //This exception is returned when a user that previously enrolled a second factor tries to sign in and passes the first factor successfully. This exception will provide a MultiFactorResolver to help resolve the sign-in by providing information to the user on the second factor challenge required to complete the sign-in operation and providing the method for finishing the sign in attempt.
                                } catch (FirebaseAuthRecentLoginRequiredException e) {
                                    //Thrown on security sensitive operations on a FirebaseUser instance that require the user to have signed in recently, when the requirement isn't met.

                                    //Resolve this exception by asking the user for their credentials again, and verifying them by calling FirebaseUser#reauthenticate(AuthCredential).
                                    e.printStackTrace();
                                } catch (FirebaseAuthUserCollisionException e) {
                                    //Thrown when an operation on a FirebaseUser instance couldn't be completed due to a conflict with another existing user.
                                    //This could happen in the following cases:
                                    //
                                    //    ERROR_EMAIL_ALREADY_IN_USE when trying to create a new account with FirebaseAuth#createUserWithEmailAndPassword(String, String) or to change a user's email address, if the email is already in use by a different account
                                    //    ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL when calling FirebaseAuth#signInWithCredential(AuthCredential) with a credential that asserts an email address in use by another account. This error will only be thrown if the "One account per email address" setting is enabled in the Firebase console (recommended).
                                    //    ERROR_CREDENTIAL_ALREADY_IN_USE when trying to link a user with an AuthCredential corresponding to another account already in use
                                    //Inspect the error code and message to find out the specific cause.

                                    //Resolve this exception by asking the user to sign in again with valid credentials. In the case that this is thrown when using a PhoneAuthCredential, you can retrieve an updated credential from getUpdatedCredential() and use it to sign-in.
                                    switch (e.getErrorCode()) {//Only applicable to register fragment
                                        case "ERROR_EMAIL_ALREADY_IN_USE": {
                                            emailLayout.setHelperTextEnabled(true);
                                            emailLayout.setHelperText("Email is already in use.");

                                            new MaterialAlertDialogBuilder(getContext())
                                                    .setTitle("Email is already in use.")
                                                    .setMessage("Email is already in use. Do you want to login or reset password.")
                                                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            //Do nothing
                                                        }
                                                    })
                                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            FirebaseAuth.getInstance().signOut();
                                                            navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
                                                        }
                                                    })
                                                    .setNegativeButton("Reset", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToResetPassword());
                                                        }
                                                    })
                                                    .show();
                                            break;
                                        }
                                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL": {
                                            Log.d(LOGTAG, e.getErrorCode());
                                            break;
                                        }
                                        case "ERROR_CREDENTIAL_ALREADY_IN_USE": {
                                            Log.d(LOGTAG, e.getErrorCode());
                                            break;
                                        }
                                    }

                                } catch (FirebaseAuthWebException e) {
                                    //Thrown when a web operation couldn't be completed.
                                    //
                                    //This could happen in the following cases:
                                    //
                                    //    ERROR_WEB_CONTEXT_ALREADY_PRESENTED thrown when another web operation is still in progress.
                                    //    ERROR_WEB_CONTEXT_CANCELED thrown when the pending operation was canceled by the user.
                                    //    ERROR_WEB_STORAGE_UNSUPPORTED thrown when the browser is not supported, or when 3rd party cookies or data are disabled in the browser.
                                    //    ERROR_WEB_INTERNAL_ERROR when there was a problem that occurred inside of the web widget that hosts the operation. Details should always accompany this message.
                                    //
                                    //Inspect the error code and message to find out the specific cause.
                                    //
                                    //Resolve this exception by waiting for the in-progress operation to complete, or by asking the user to try signing-in via the web context again.
                                    e.printStackTrace();
                                } catch (FirebaseNetworkException e) {
                                    Log.d(LOGTAG, "FirebaseNetworkException: " + e.getMessage());
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

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (!user.isEmailVerified()) {//user is not null and email is not verified
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(GONE);
                        if (task.isSuccessful()) {
                            Log.d(LOGTAG, "Email is sent.");
                            //verificationTextView.setText("Email sent.");

                            new MaterialAlertDialogBuilder(getContext())
                                    .setTitle("Verification email sent to your email address.")
                                    .setMessage("Check your inbox and click on the verification link to verify your email.")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .setPositiveButton("Return to Login", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            FirebaseAuth.getInstance().signOut();
                                            navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
                                        }
                                    })
                                    .show();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseNetworkException e) {
                                Log.d(LOGTAG, e.getMessage());
                                showToast(e.getMessage());
                            } catch (Exception e) {
                                Log.d(LOGTAG, "Exception: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
    }
}