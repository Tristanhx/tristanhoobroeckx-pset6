package com.example.tristan.tristanhoobroeckx_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    FirebaseUser user;
    Intent returntoMain;
    FloatingActionButton fab;
    EditText message;
    ListView chatlist;
    TextView messageText;
    TextView messageUser;
    TextView messageTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        returntoMain = new Intent(ChatActivity.this, MainActivity.class);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new SendMessage());



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
                    startActivity(returntoMain);
                    finish();
                }
                // ...
            }
        };

        database = FirebaseDatabase.getInstance();
        DisplayChats();
    }

    public void DisplayChats(){
        chatlist = (ListView) findViewById(R.id.chatlist);

        FirebaseListAdapter<DebateMessage> chatadapter = new FirebaseListAdapter<DebateMessage>(this, DebateMessage.class, R.layout.message, database.getReference()) {
            @Override
            protected void populateView(View view, DebateMessage message, int position) {
                // get Views from message.xml
                messageText = (TextView) view.findViewById(R.id.message_text);
                messageUser = (TextView) view.findViewById(R.id.message_user);
                messageTime = (TextView) view.findViewById(R.id.message_time);

                // Set textViews from message.xml
                messageText.setText(message.getMessageText());
                messageUser.setText(message.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));

            }
        };
        chatlist.setAdapter(chatadapter);
    }

    public class SendMessage implements View.OnClickListener {

        public SendMessage(){

        }
        @Override
        public void onClick(View view){
            message = (EditText) findViewById(R.id.edittext);

            database.getInstance().getReference().push()
                    .setValue(new DebateMessage(message.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
            message.getText().clear();
        }
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
}
