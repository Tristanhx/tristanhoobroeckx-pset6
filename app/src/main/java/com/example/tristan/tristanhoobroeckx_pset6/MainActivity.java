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

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Intent intent;
    String email;
    String password;
    EditText signupemail;
    EditText signuppassword;
    EditText loginemail;
    EditText loginpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signupemail = (EditText) findViewById(R.id.signemailinput);
        signuppassword = (EditText) findViewById(R.id.signpasswordinput);
        loginemail = (EditText) findViewById(R.id.logemailinput);
        loginpassword = (EditText) findViewById(R.id.logpasswordinput);
        intent = new Intent(MainActivity.this, TeamActivity.class);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
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
        if (signupemail.equals("") || signuppassword.equals("")){
            Toast.makeText(this, "Missing email/password", Toast.LENGTH_SHORT).show();
            signupemail.getText().clear();
            signuppassword.getText().clear();
        }
        else if(signuppassword.length()<6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            signupemail.getText().clear();
            signuppassword.getText().clear();
        }
        else {
            email = signupemail.getText().toString();
            password = signuppassword.getText().toString();
            signupemail.getText().clear();
            signuppassword.getText().clear();

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
                            }

                            // ...
                        }
                    });
        }
    }

    public void LogIn(View view){
        if (loginemail.equals("") || loginpassword.equals("")){
            Toast.makeText(this, "Missing email/password", Toast.LENGTH_SHORT).show();
            loginemail.getText().clear();
            loginpassword.getText().clear();
        }
        else if(loginpassword.length()<6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            loginemail.getText().clear();
            loginpassword.getText().clear();
        }
        else {
            email = loginemail.getText().toString();
            password = loginpassword.getText().toString();
            loginemail.getText().clear();
            loginpassword.getText().clear();

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
