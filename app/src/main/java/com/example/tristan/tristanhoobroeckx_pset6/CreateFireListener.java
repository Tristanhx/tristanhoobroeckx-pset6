package com.example.tristan.tristanhoobroeckx_pset6;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Tristan on 25/05/2017.
 * This class contains a method that creates and returns an AuthStateListener from Firebase.
 * It contains two booleans which indicate whether or not an activity is started through intent.
 */

public class CreateFireListener {
    private final Activity context;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    private Intent intent, returnToMain;


    public CreateFireListener(Activity context){
        this.context = context;

    }

    public FirebaseAuth.AuthStateListener createFireListener(final boolean startIntent, final boolean toMain){
        returnToMain = new Intent(context, MainActivity.class);
        intent = new Intent(context, TeamActivity.class);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){
                    // User is signed in
                    Log.d("signed in", "onAuthStateChanged:signed_in:" + user.getUid());

                    if(startIntent){
                        context.startActivity(intent);
                    }

                } else {
                    // User is signed out
                    Log.d("signed out", "onAuthStateChanged:signed_out");

                    if (toMain){
                        context.startActivity(returnToMain);
                        context.finish();
                    }
                }
            }
        };
        return mAuthListener;
    }
}
