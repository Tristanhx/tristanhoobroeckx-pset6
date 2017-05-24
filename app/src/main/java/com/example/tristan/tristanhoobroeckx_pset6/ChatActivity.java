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
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    FirebaseUser user;
    Intent returnToMain;
    FloatingActionButton fab;
    EditText message;
    ListView chatList;
    TextView messageText;
    TextView messageTeam;
    TextView messageName;
    TextView messageTime;
    String team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        returnToMain = new Intent(ChatActivity.this, MainActivity.class);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new sendMessage());

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
                    startActivity(returnToMain);
                    finish();
                }
                // ...
            }
        };

        Bundle extras = getIntent().getExtras();
        team = extras.getString("team");

        database = FirebaseDatabase.getInstance();
        displayChats();
    }

    public void displayChats(){
        chatList = (ListView) findViewById(R.id.chatlist);

        if(team.equals("Blue")){
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.bluefab));
        }
        else if(team.equals("Red")){
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.redfab));
        }

        FirebaseListAdapter<DebateMessage> chatAdapter = new FirebaseListAdapter<DebateMessage>(this,
                DebateMessage.class, R.layout.message, database.getReference()) {
            @Override
            protected void populateView(View view, DebateMessage message, int position) {
                // get Views from message.xml
                messageText = (TextView) view.findViewById(R.id.message_text);
                messageTeam = (TextView) view.findViewById(R.id.message_team);
                messageTime = (TextView) view.findViewById(R.id.message_time);
                messageName = (TextView) view.findViewById(R.id.message_name);

                // Set textViews from message.xml
                messageText.setText(message.getMessageText());
                messageName.setText(message.getMessageUser());

                if(message.getMessageTeam().equals("Blue")){
                    messageTeam.setText(getResources().getString(R.string.teamBlue));
                    messageTeam.setTextColor(getResources().getColor(R.color.blue1));
                }
                else if(message.getMessageTeam().equals("Red")){
                    messageTeam.setText(getResources().getString(R.string.teamRed));
                    messageTeam.setTextColor(getResources().getColor(R.color.red2));
                }
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        message.getMessageTime()));

            }
        };
        chatList.setAdapter(chatAdapter);
    }

    private class sendMessage implements View.OnClickListener {

        private sendMessage(){

        }
        @Override
        public void onClick(View view){
            message = (EditText) findViewById(R.id.edittext);

            database.getInstance().getReference().push()
                    .setValue(new DebateMessage(message.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), team));
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
