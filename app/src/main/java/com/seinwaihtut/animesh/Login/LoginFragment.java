package com.seinwaihtut.animesh.Login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class LoginFragment extends Fragment {
    NavController navController;
    TextInputEditText emailText, passwordText;
    TextInputLayout emailLayout, passwordLayout;

    Toast toast;

    private static final String LOGTAG = "LOGINFRAGMENT";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(getActivity(), androidx.navigation.fragment.R.id.nav_host_fragment_container);

        emailText = view.findViewById(R.id.login_email_text);
        passwordText = view.findViewById(R.id.login_password_text);
        emailLayout = view.findViewById(R.id.login_email_layout);
        passwordLayout = view.findViewById(R.id.login_password_layout);

        emailLayout.setEndIconVisible(false);
        emailLayout.setHelperTextEnabled(false);
        passwordLayout.setHelperTextEnabled(false);

        emailText.addTextChangedListener(new TextWatcher() {
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

        passwordText.addTextChangedListener(new TextWatcher() {
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

        view.findViewById(R.id.login_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.hideKeyboard(getActivity());

                String email, password;
                email = emailText.getText().toString();
                password = passwordText.getText().toString();
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

                if (password.isEmpty()) {
                    passwordLayout.setHelperTextEnabled(true);
                    passwordLayout.setHelperText("*Required");
                } else {
                    passwordLayout.setHelperTextEnabled(false);
                }

                if (emailIsValid & !email.isEmpty() & !password.isEmpty()) {

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if (user!=null) {
                                    if (user.isEmailVerified()) {
                                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment());
                                    } else {
                                        emailLayout.setHelperTextEnabled(true);
                                        FirebaseAuth.getInstance().signOut();

                                        //TODO Popup with verify email text
                                        //TODO Remove verify button in login fragment layout
                                        emailLayout.setHelperText("Verify your email.");

                                        new MaterialAlertDialogBuilder(getContext())
                                                .setTitle("Verify your email")
                                                .setMessage("You need to verify your email before logging in.")
                                                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                })
                                                .setPositiveButton("Verify Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        FirebaseAuth.getInstance().signOut();
                                                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToVerificationFragment());
                                                    }
                                                })
                                                .show();

                                    }
                                }


                            } else {
                                try {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthActionCodeException e) {
                                    //Represents the exception which is a result of an expired or an invalid out of band code.
                                    showToast(e.getMessage());
                                } catch (FirebaseAuthEmailException e) {
                                    //Represents the exception which is a result of an attempt to send an email via Firebase Auth (e.g. a password reset email)
                                    e.printStackTrace();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    //Thrown when using a weak password (less than 6 chars) to create a new account or to update an existing account's password. Use getReason() to get a message with the reason the validation failed that you can display to your users.
                                    //Not applicable to login fragment
                                    e.printStackTrace();
                                } catch (FirebaseAuthInvalidCredentialsException e) {//Not applicable to register
                                    //Thrown when one or more of the credentials passed to a method fail to identify and/or authenticate the user subject of that operation. Inspect the error code and message to find out the specific cause.

                                    Log.d(LOGTAG, "errorCode: "+ e.getErrorCode());
                                    switch (e.getErrorCode()){
                                        case "ERROR_WRONG_PASSWORD": {
                                            emailLayout.setHelperTextEnabled(true);
                                            emailLayout.setHelperText("Invalid credentials.");
                                            passwordLayout.setHelperTextEnabled(true);
                                            passwordLayout.setHelperText("Invalid credentials.");
                                            break;
                                        }
                                    }

                                    e.printStackTrace();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    //You should consider this error condition as if the user had signed out of your app, and thus should require them to sign in again if they need to perform any action that requires authentication.
                                    emailLayout.setHelperTextEnabled(true);
                                    emailLayout.setHelperText("Invalid credentials.");
                                    passwordLayout.setHelperTextEnabled(true);
                                    passwordLayout.setHelperText("Invalid credentials.");
                                    e.printStackTrace();
                                } catch (FirebaseAuthMultiFactorException e) {
                                    //This exception is returned when a user that previously enrolled a second factor tries to sign in and passes the first factor successfully. This exception will provide a MultiFactorResolver to help resolve the sign-in by providing information to the user on the second factor challenge required to complete the sign-in operation and providing the method for finishing the sign in attempt.
                                    e.printStackTrace();
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
                                        case "ERROR_EMAIL_ALREADY_IN_USE":{
                                            emailLayout.setHelperTextEnabled(true);
                                            emailLayout.setHelperText("Email is already in use.");
                                            break;}
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
                                }
                                catch(FirebaseNetworkException e){
                                    Log.d(LOGTAG, "FirebaseNetworkException: "+ e.getMessage());
                                    showToast(e.getMessage());
                                }catch(FirebaseTooManyRequestsException e){

                                    new MaterialAlertDialogBuilder(getContext())
                                            .setTitle("Too many failed login attempts.")
                                            .setMessage("Try again later or reset your password.")
                                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    //Do nothing
                                                }
                                            })
                                            .setPositiveButton("Reset password", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    FirebaseAuth.getInstance().signOut();
                                                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToResetPassword());
                                                }
                                            })
                                            .show();

                                    showToast(e.getMessage());
                                }
                                catch (Exception e) {
                                    Log.d(LOGTAG, "Exception: "+e.toString());
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    Log.i(LOGTAG, "Email is invalid!");
                }
            }
        });

        view.findViewById(R.id.login_signup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());

                navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
            }
        });
        view.findViewById(R.id.login_biometric_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());

                navController.navigate(LoginFragmentDirections.actionLoginFragmentToBiometricSetupFragment());
            }
        });
        view.findViewById(R.id.login_biometric_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());

                navController.navigate(LoginFragmentDirections.actionLoginFragmentToBiometricSetupFragment());
            }
        });
        view.findViewById(R.id.login_reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                Utils.hideKeyboard(getActivity());


                navController.navigate(LoginFragmentDirections.actionLoginFragmentToResetPassword());
            }
        });

    }

    private void showToast(String message){
        if (toast!=null){
            toast.cancel();
        }
        toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}