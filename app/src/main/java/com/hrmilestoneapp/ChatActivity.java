package com.hrmilestoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hrmilestoneapp.utils.PreferenceManager;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private String recieverId;
    private Firebase firebase;
    private Firebase firebase1;
    ArrayList<ChatMessage> chatMessageArrayList;
    ListView listOfMessages;
    private Firebase firebaseget;
    FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
    DatabaseReference fref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Firebase.setAndroidContext(this);
        final String USER_KEY = PreferenceManager.getprefUserKey(this);
        chatMessageArrayList = new ArrayList<ChatMessage>();
        Log.i("USER_KEY", "USER_KEY : " + USER_KEY);
        listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        fref = fdatabase.getReference("message");
        String userId = fref.push().getKey();
        Intent i = getIntent();
        if (i.hasExtra("userId")) {
            recieverId = i.getStringExtra("userId");

        }

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);

        firebaseget = new Firebase("https://hrmilestoneapp.firebaseio.com/" + "message" + "/" + USER_KEY + "_" + recieverId);
        firebaseget.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    String chatmsg = chatMessage.getMessageText();
                    Log.e("chatmsg", "+_+_+_+_+_+chatmsg+_+_+_+_+_+_" + chatmsg);
                    chatMessageArrayList.add(chatMessage);

                    adddata(chatMessageArrayList);

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);
                String data = DateTime.Date();
                String time = DateTime.Time();
                firebase = new Firebase("https://hrmilestoneapp.firebaseio.com/" + "message" + "/" + USER_KEY + "_" + recieverId);
                firebase1 = new Firebase("https://hrmilestoneapp.firebaseio.com/" + "message" + "/" + recieverId + "_" + USER_KEY);

                ChatMessage chatMessage = new ChatMessage();
                String strId = firebase.push().getKey();
                chatMessage.setMessageText(input.getText().toString());
                chatMessage.setReceiverId(recieverId);
                chatMessage.setSenderID(USER_KEY);
                chatMessage.setUserType("sender");
                chatMessage.setDate(data);
                chatMessage.setTime(time);
                // chatMessage.setMessageTime();
                firebase.child(strId).setValue(chatMessage);

                ChatMessage chatMessage1 = new ChatMessage();
                String strId1 = firebase1.push().getKey();
                chatMessage1.setMessageText(input.getText().toString());
                chatMessage1.setReceiverId(recieverId);
                chatMessage1.setSenderID(USER_KEY);
                chatMessage1.setUserType("Receiver");
                chatMessage1.setDate(data);
                chatMessage1.setTime(time);
                // chatMessage.setMessageTime();
                firebase1.child(strId1).setValue(chatMessage1);
                input.setText("");

            }


            // Clear the input
        });


    }

    private void adddata(ArrayList<ChatMessage> chatMessageArrayList) {

        ChattingAdapter  chattingAdapter = new ChattingAdapter(this,chatMessageArrayList);
        listOfMessages.setAdapter(chattingAdapter);
        chattingAdapter.notifyDataSetChanged();

    }
}
