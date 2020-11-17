package com.technion.rbd.dressapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseListAdapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.auth.FirebaseAuth;
import com.technion.rbd.dressapp.BackEnd.DatabaseAPI;

import java.util.ArrayList;
import java.util.Random;


public class SendMessageActivity extends AppCompatActivity {

    //private FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder> adapter;
    private FirebaseListAdapter<ChatMessage> adapter;
    private String user_id;
    private String thread_id;

    private FirebaseAuth mAuth;
    FirebaseUser fire_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Toolbar toolbar = findViewById(R.id.toolbar_sendMessage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        fire_user = mAuth.getCurrentUser();
        final String sender_id = fire_user.getUid();


        user_id = getIntent().getStringExtra("userId");
        thread_id = getIntent().getStringExtra("threadId");

        getSupportActionBar().setTitle(user_id);


        ImageButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);

                if(input.getText().toString().equals("")) {
                    Toast.makeText(SendMessageActivity.this,
                            "Cannot send empty messgae", Toast.LENGTH_SHORT).show();
                } else {

                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    FirebaseDatabase.getInstance()
                            .getReference("chats/threads/" + thread_id)
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName(), sender_id, user_id)
                            );

                    // Clear the input
                    input.setText("");
                }
            }
        });
        displayChatMessages();
    }

    private void displayChatMessages() {
//        RecyclerView mRV = findViewById(R.id.list_of_messages);
//
//        adapter = new FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>(this, ChatMessage.class,
//                R.layout.message, MessageViewHolder.class, FirebaseDatabase.getInstance().getReference("chats/threads/" + thread_id)) {
//            @Override
//            protected void populateViewHolder(MessageViewHolder viewHolder, ChatMessage model, int position) {
//
//            }
//        }


        ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);

        //int layout = (new Random().nextInt(10) % 2 == 0) ? R.layout.message_from_the_other : R.layout.message;
                adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference("chats/threads/" + thread_id)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm)",
                        model.getMessageTime()));

                Log.d("fireUSer", fire_user.getDisplayName() + " " + model.getMessageUser());

                if(!fire_user.getDisplayName().equals(model.getMessageUser())) {
                    messageText.setBackground(getResources().getDrawable(R.drawable.tag_round_corner_grey_chat));
                    messageText.setTextColor(getResources().getColor(R.color.black));
                }

                if(fire_user.getDisplayName().equals(model.getMessageUser())) {
                    messageText.setBackground(getResources().getDrawable(R.drawable.tag_round_corner_gold_chat));
                    messageText.setTextColor(getResources().getColor(R.color.white));
                }
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

//    public static class MessageViewHolder extends RecyclerView.ViewHolder{
//        TextView messageText;
//        TextView messageUser;
//        TextView messageTime;
//
//        public MessageViewHolder(View itemView) {
//            super(itemView);
//            messageText = (TextView) itemView.findViewById(R.id.message_text);
//            messageUser = (TextView) itemView.findViewById(R.id.message_user);
//            messageTime = (TextView) itemView.findViewById(R.id.message_time);
//        }
//    }
}
