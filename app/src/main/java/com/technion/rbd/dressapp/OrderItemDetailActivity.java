package com.technion.rbd.dressapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class OrderItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_detail_activity);

        Toolbar toolbar = findViewById(R.id.toolbar_OrderItemDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ordered Item Detail");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_item_detail_activity_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        } else if(id == R.id.order_item_detail_contact) {
            //TODO: GO TO CONTACT OWNER
        }

        return super.onOptionsItemSelected(item);
    }

    public void Rate(View view) {
        Bundle bundle = new Bundle();
        RatingDialog rdialog = new RatingDialog();
        rdialog.setArguments(bundle);
        rdialog.show(getSupportFragmentManager(), "rate");
    }
}
