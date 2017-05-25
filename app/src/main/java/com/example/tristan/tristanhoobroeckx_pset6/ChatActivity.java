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
    TextView messageText, messageTeam, messageName, messageTime;
    String team, red, blue;
    CreateFireListener listenerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        returnToMain = new Intent(ChatActivity.this, MainActivity.class);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new sendMessage());

        mAuth = FirebaseAuth.getInstance();
        listenerHelper = new CreateFireListener(this);
        mAuthListener = listenerHelper.createFireListener(false, true);

        Bundle extras = getIntent().getExtras();
        team = extras.getString("team");

        database = FirebaseDatabase.getInstance();
        displayChats();
    }

    public void displayChats(){
        chatList = (ListView) findViewById(R.id.chatlist);
        red = getResources().getString(R.string.teamRed);
        blue = getResources().getString(R.string.teamBlue);

        if(team.equals(blue)){
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.bluefab));
        }
        else if(team.equals(red)){
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.redfab));
        }


        chatList.setAdapter(createFireBaseAdapter());
    }

    public FirebaseListAdapter<DebateMessage> createFireBaseAdapter(){
        FirebaseListAdapter<DebateMessage> chatAdapter = new FirebaseListAdapter<DebateMessage>(this,
                DebateMessage.class, R.layout.message, database.getReference()) {
            @Override
            protected void populateView(View view, DebateMessage message, int position) {
                getViews(view);

                setViews(message);
            }
        };
        return chatAdapter;
    }

    public void getViews(View view){
        // get Views from message.xml
        messageText = (TextView) view.findViewById(R.id.message_text);
        messageTeam = (TextView) view.findViewById(R.id.message_team);
        messageTime = (TextView) view.findViewById(R.id.message_time);
        messageName = (TextView) view.findViewById(R.id.message_name);
    }

    public void setViews(DebateMessage message){
        // Set textViews from message.xml
        messageText.setText(message.getMessageText());
        messageName.setText(message.getMessageUser());

        if(message.getMessageTeam().equals(blue)){
            setTextAndColor(blue, getResources().getColor(R.color.blue1));
        }
        else if(message.getMessageTeam().equals(red)){
            setTextAndColor(red, getResources().getColor(R.color.red2));
        }
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                message.getMessageTime()));
    }

    public void setTextAndColor(String text, int color){
        messageTeam.setText(text);
        messageTeam.setTextColor(color);
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
