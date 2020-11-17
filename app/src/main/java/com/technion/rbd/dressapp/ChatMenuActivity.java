package com.technion.rbd.dressapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

public class ChatMenuActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ChatMessage> piList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chatMenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Conversation(s)");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.chatMenuRV);

        piList = new ArrayList<ChatMessage>();
        piList.add(new ChatMessage("Hello, I wanna get your item", "Another User", "ID","ID"));
        piList.add(new ChatMessage("Hello, I wanna get your item", "Another User", "ID","ID"));
        piList.add(new ChatMessage("Hello, I wanna get your item", "Another User", "ID","ID"));
        piList.add(new ChatMessage("Hello, I wanna get your item", "Another User", "ID","ID"));



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvLayoutManager);

        ChatMenuAdapter adapter = new ChatMenuAdapter(this, piList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
