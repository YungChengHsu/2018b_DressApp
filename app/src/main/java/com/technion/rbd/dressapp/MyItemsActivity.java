package com.technion.rbd.dressapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MyItemsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MyOrderItems>  piList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        Toolbar toolbar = findViewById(R.id.toolbar_MyItems);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Items");

        // Set up scrollview in profile page, which shows what this user has
        recyclerView = findViewById(R.id.myItemsRV);

        piList = new ArrayList<MyOrderItems>();
        piList.add(new MyOrderItems(R.drawable.example_jeans_1,
                R.drawable.default_profile_pic, "Example Item Name", "Delivering", "Owner Name", "Technion, Haifa"));
        piList.add(new MyOrderItems(R.drawable.example_jeans_1,
                R.drawable.default_profile_pic, "Example Item Name", "Arrived", "Owner Name", "Technion, Haifa"));
        piList.add(new MyOrderItems(R.drawable.example_jeans_1,
                R.drawable.default_profile_pic, "Example Item Name", "Pending", "Owner Name", "Technion, Haifa"));
        piList.add(new MyOrderItems(R.drawable.example_jeans_1,
                R.drawable.default_profile_pic, "Example Item Name", "Delivering", "Owner Name", "Technion, Haifa"));


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvLayoutManager);

        MyItemAdapter adapter = new MyItemAdapter(this, piList);
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.my_items_activity_toolbar, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void ToItemView(View view) {
        Intent intent = new Intent(this, MyItemDetailActivity.class);
        startActivity(intent);
    }
}
