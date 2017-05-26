package com.example.tristan.tristanhoobroeckx_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * This is the first activity that starts when you launch the app. It may not always seem so, because
 * when the user is already logged in MainActivity starts TeamActivity. MainActivity's layout is
 * divided vertically and contains Sign Up and Login options as well as the opportunity to set your
 * Displayname.
 */

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser postUser;
    Intent intent;
    String email, password, displayName;
    EditText signUpEmail, signUpPassword, loginEmail, loginPassword, signUpDisplayName,
            loginDisplayName;
    CreateFireListener listenerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpEmail = (EditText) findViewById(R.id.signemailinput);
        signUpPassword = (EditText) findViewById(R.id.signpasswordinput);
        loginEmail = (EditText) findViewById(R.id.logemailinput);
        loginPassword = (EditText) findViewById(R.id.logpasswordinput);
        signUpDisplayName = (EditText) findViewById(R.id.displaynamesignup);
        loginDisplayName = (EditText) findViewById(R.id.displaynamelogin);

        intent = new Intent(MainActivity.this, TeamActivity.class);

        mAuth = FirebaseAuth.getInstance();

        /*
        I moved the creation of the FirebaseAuth.AuthStateListener to an external class to
        save on lines and make the code more readable.
        */
        listenerHelper = new CreateFireListener(MainActivity.this);
        mAuthListener = listenerHelper.createFireListener(true, false);
    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signUp(View view){
        if (signUpEmail.getText().toString().equals("") || signUpPassword.getText().toString().equals("")){
            makeToast("Missing email/password");

            clearText(signUpEmail, signUpPassword, false);
        }
        else if(signUpPassword.length()<6){
            makeToast("Password must be at least 6 characters");

            clearText(signUpEmail, signUpPassword, false);
        }
        else {
            email = signUpEmail.getText().toString();
            password = signUpPassword.getText().toString();
            displayName = signUpDisplayName.getText().toString();
            if (displayName.equals("")){
                displayName = getResources().getString(R.string.defaultDisplayName);
            }
            clearText(signUpEmail, signUpPassword, true);

            createUser(email, password);

            signOutSignIn(email, password);

        }
    }

    public void createUser(final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("sign up", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            makeToast("Authentication Failed");

                        } else {
                            makeToast("Created User: " + email);

                            postUser = FirebaseAuth.getInstance().getCurrentUser();

                            // Set Displayname
                            setDisplayName(false);
                        }
                    }
                });
    }

    public void logIn(View view){
        email = loginEmail.getText().toString();
        password = loginPassword.getText().toString();
        displayName = loginDisplayName.getText().toString();
        if (email.equals("") || password.equals("")){
            makeToast("Missing email/password");
            clearText(loginEmail, loginPassword, false);
        }
        else if(password.length()<6){
            makeToast("Password must be at least 6 characters");
            clearText(loginEmail, loginPassword, false);
        }
        else {
            // If no displayname is given, displayname is 'Human'.
            if (displayName.equals("")){
                displayName = getResources().getString(R.string.defaultDisplayName);
            }
            clearText(loginEmail, loginPassword, true);

            emailPassword(email, password, false);

            /* Force signOut signIn because FireBase bug, and added sleep because apparently it
            needs to wait a while
            */
            signOutSignIn(email, password);
        }
    }

    // I moved the login to this method, because it is used multiple times.
    public void emailPassword(final String email, String password, final boolean startIntent){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("log in", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("log in", "signInWithEmail:failed", task.getException());
                            makeToast("Authentication Failed");
                        }
                        else{
                            makeToast("Logged In User: " + email);

                            postUser = FirebaseAuth.getInstance().getCurrentUser();

                            setDisplayName(startIntent);
                        }
                    }
                });
    }

    public void setDisplayName(boolean startIntent){
        UserProfileChangeRequest updateToRedOrBlue = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName).build();
        postUser.updateProfile(updateToRedOrBlue);
        makeToast("You are " + displayName);

        if (startIntent){
            startActivity(intent);
        }
    }

    /*
    Attempt to circumvent a bug in Firebase.
    Force signOut signIn because FireBase bug, and added sleep because apparently it
    needs to wait a while. Still doesn't always works, but is better than never works.
    */
    public void signOutSignIn(final String email, String password){
        mAuth.signOut();
        android.os.SystemClock.sleep(2000);
        emailPassword(email, password, true);
    }

    // Multiple editTexts are cleared in this Activity, so why not have it in a method.
    public void clearText(EditText ed1, EditText ed2, boolean clearDisplayName){
        ed1.getText().clear();
        ed2.getText().clear();

        if (clearDisplayName){
            signUpDisplayName.getText().clear();
            loginDisplayName.getText().clear();
        }
    }

    // Multiple Toasts are made in this Activity, so I put it in a method.
    public void makeToast(String message){
        Toast.makeText(MainActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }
}
