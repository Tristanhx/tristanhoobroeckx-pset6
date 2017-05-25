package com.example.tristan.tristanhoobroeckx_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TeamActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Intent returnToMain, toChat;
    FirebaseUser user;
    String team;
    CreateFireListener listenerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        returnToMain = new Intent(TeamActivity.this, MainActivity.class);
        toChat = new Intent(TeamActivity.this, ChatActivity.class);


        mAuth = FirebaseAuth.getInstance();
        listenerHelper = new CreateFireListener(TeamActivity.this);
        mAuthListener = listenerHelper.createFireListener(false, false);
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

    public void beBlue(View view){
        team = "Blue";
        toChat.putExtra("team", team);

        startActivity(toChat);

    }

    public void beRed(View view){
        team = "Red";
        toChat.putExtra("team", team);

        startActivity(toChat);

    }

    public void logOut(View view){
        mAuth.signOut();
        startActivity(returnToMain);
        finish();

    }
}
