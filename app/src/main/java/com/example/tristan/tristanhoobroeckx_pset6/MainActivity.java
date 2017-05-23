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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    FirebaseUser postUser;
    Intent intent;
    String email;
    String password;
    String displayName;
    EditText signupEmail;
    EditText signupPassword;
    EditText loginEmail;
    EditText loginPassword;
    EditText signupDisplayName;
    EditText loginDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signupEmail = (EditText) findViewById(R.id.signemailinput);
        signupPassword = (EditText) findViewById(R.id.signpasswordinput);
        loginEmail = (EditText) findViewById(R.id.logemailinput);
        loginPassword = (EditText) findViewById(R.id.logpasswordinput);
        signupDisplayName = (EditText) findViewById(R.id.displaynamesignup);
        loginDisplayName = (EditText) findViewById(R.id.displaynamelogin);

        intent = new Intent(MainActivity.this, TeamActivity.class);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){
                    // User is signed in
                    Log.d("signed in", "onAuthStateChanged:signed_in:" + user.getUid());

                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d("signed out", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
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

    public void SignUp(View view){
        if (signupEmail.equals("") || signupPassword.equals("")){
            Toast.makeText(this, "Missing email/password", Toast.LENGTH_SHORT).show();
            signupEmail.getText().clear();
            signupPassword.getText().clear();
        }
        else if(signupPassword.length()<6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            signupEmail.getText().clear();
            signupPassword.getText().clear();
        }
        else {
            email = signupEmail.getText().toString();
            password = signupPassword.getText().toString();
            displayName = signupDisplayName.getText().toString();
            if (displayName.equals("")){
                displayName = getResources().getString(R.string.defaultDisplayName);
            }
            signupEmail.getText().clear();
            signupPassword.getText().clear();
            signupDisplayName.getText().clear();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("sign up", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Authentication Failed",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Created User: " + email,
                                        Toast.LENGTH_SHORT).show();

                                postUser = FirebaseAuth.getInstance().getCurrentUser();

                                // Set Displayname
                                UserProfileChangeRequest updateToRedOrBlue = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName).build();
                                postUser.updateProfile(updateToRedOrBlue);
                                Toast.makeText(MainActivity.this, "You are " + displayName, Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
            // Force signout signin because firebase bug
            mAuth.signOut();
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
                                Toast.makeText(MainActivity.this, "Authentication Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Logged In User: " + email,
                                        Toast.LENGTH_SHORT).show();

                                startActivity(intent);
                            }
                        }
                    });

        }
    }

    public void LogIn(View view){
        if (loginEmail.equals("") || loginPassword.equals("")){
            Toast.makeText(this, "Missing email/password", Toast.LENGTH_SHORT).show();
            loginEmail.getText().clear();
            loginPassword.getText().clear();
        }
        else if(loginPassword.length()<6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            loginEmail.getText().clear();
            loginPassword.getText().clear();
        }
        else {
            email = loginEmail.getText().toString();
            password = loginPassword.getText().toString();
            displayName = loginDisplayName.getText().toString();
            if (displayName.equals("")){
                displayName = getResources().getString(R.string.defaultDisplayName);
            }
            loginEmail.getText().clear();
            loginPassword.getText().clear();
            loginDisplayName.getText().clear();

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
                                Toast.makeText(MainActivity.this, "Authentication Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Logged In User: " + email,
                                        Toast.LENGTH_SHORT).show();

                                postUser = FirebaseAuth.getInstance().getCurrentUser();

                                // Set Displayname
                                UserProfileChangeRequest updateToRedOrBlue = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName).build();
                                postUser.updateProfile(updateToRedOrBlue);
                                Toast.makeText(MainActivity.this, "You are " + displayName, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Force signout signin because firebase bug
            mAuth.signOut();
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
                                Toast.makeText(MainActivity.this, "Authentication Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Logged In User: " + email,
                                        Toast.LENGTH_SHORT).show();

                                startActivity(intent);
                            }
                        }
                    });
        }
    }
}
