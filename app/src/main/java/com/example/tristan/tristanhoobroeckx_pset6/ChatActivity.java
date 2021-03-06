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

/**
 * In this activity chatMessages are displayed. It also contains an input box and button.
 * The activity receives a string (Red or Blue) from TeamActivity and changes the colors of this
 * screen accordingly.
 */

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    Intent returnToMain;
    FloatingActionButton fab;
    EditText messageEdit;
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
        messageEdit = (EditText) findViewById(R.id.edittext);

        mAuth = FirebaseAuth.getInstance();

        listenerHelper = new CreateFireListener(this);
        mAuthListener = listenerHelper.createFireListener(false, true);

        Bundle extras = getIntent().getExtras();
        team = extras.getString("team");

        database = FirebaseDatabase.getInstance();
        displayChats();
    }

    // This is the method that creates initializes the chatList and sets the adapter.
    public void displayChats(){
        chatList = (ListView) findViewById(R.id.chatlist);
        red = getResources().getString(R.string.teamRed);
        blue = getResources().getString(R.string.teamBlue);

        if(team.equals(blue)){
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.bluefab));
            messageEdit.setTextColor(getResources().getColorStateList(R.color.bluefab));
        }
        else if(team.equals(red)){
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.redfab));
            messageEdit.setTextColor(getResources().getColorStateList(R.color.redfab));
        }


        chatList.setAdapter(createFireBaseAdapter());
    }

    /*
    This is the method that returns a FirebaseListAdapter. This is an adapter from Firebase-UI
    that updates as the realtime database changes.
    */
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

    /*
     In order to keep methods small I moved some components into their own methods and call them.
      */
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

    /*
    This is the listener from the FAB. When clicked a new message-object is created and added to
    the database.
    */
    private class sendMessage implements View.OnClickListener {

        private sendMessage(){

        }
        @Override
        public void onClick(View view){

            database.getInstance().getReference().push()
                    .setValue(new DebateMessage(messageEdit.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), team));
            messageEdit.getText().clear();
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
