package com.example.tristan.tristanhoobroeckx_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class TeamActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Intent returntoMain;
    Intent toChat;
    FirebaseUser user;
    String displayname;
    String team;
    EditText nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        nameView = (EditText) findViewById(R.id.namefield);

        returntoMain = new Intent(TeamActivity.this, MainActivity.class);
        toChat = new Intent(TeamActivity.this, ChatActivity.class);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){
                    // User is signed in
                    Log.d("signed in", "onAuthStateChanged:signed_in:" + user.getUid());

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

    public void BeBlue(View view){
        displayname = nameView.getText().toString();
        if (displayname.equals("")){
            Toast.makeText(this, "What is your displayname?", Toast.LENGTH_SHORT).show();
        }
        else{
            FirebaseUser teamuserblue = FirebaseAuth.getInstance().getCurrentUser();

            Log.d("team", getResources().getString(R.string.teamBlue));

            if (teamuserblue != null) {

                // Set Displayname
                UserProfileChangeRequest updateToRedOrBlue = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayname).build();
                teamuserblue.updateProfile(updateToRedOrBlue);
                Toast.makeText(this, "You are Blue " + displayname, Toast.LENGTH_SHORT).show();

                team = "Blue";
                toChat.putExtra("team", team);

                startActivity(toChat);

            }
            else{
                Toast.makeText(this, "BlueUser = null", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void BeRed(View view){
        displayname = nameView.getText().toString();
        if (displayname.equals("")){
            Toast.makeText(this, "What is your displayname?", Toast.LENGTH_SHORT).show();
        }
        else{
            FirebaseUser teamuserred = FirebaseAuth.getInstance().getCurrentUser();

            Log.d("team", getResources().getString(R.string.teamBlue));

            if (teamuserred != null) {

                // Set Displayname
                UserProfileChangeRequest updateToRedOrBlue = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayname).build();
                teamuserred.updateProfile(updateToRedOrBlue);
                Toast.makeText(this, "You are Red " + displayname, Toast.LENGTH_SHORT).show();

                team = "Red";
                toChat.putExtra("team", team);

                startActivity(toChat);

            }
            else{
                Toast.makeText(this, "RedUser = null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Logout(View view){
        mAuth.signOut();
        startActivity(returntoMain);
        finish();

    }
}
