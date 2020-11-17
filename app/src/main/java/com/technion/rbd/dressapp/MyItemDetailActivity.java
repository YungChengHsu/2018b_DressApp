package com.technion.rbd.dressapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MyItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_MyItemDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Item Detail");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_item_detail_activity_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        } else if(id == R.id.my_item_detail_contact) {
            //TODO: GO TO CONTACT OWNER
        }

        return super.onOptionsItemSelected(item);
    }

    public void ChangeDeliveryStatus(View view) {
        Bundle bundle = new Bundle();
        ChangeDeliveryStatusDialog cddialog = new ChangeDeliveryStatusDialog();
        cddialog.setArguments(bundle);
        cddialog.show(getSupportFragmentManager(), "changeDeliveryStatus");
    }

    public void Rate(View view) {
        Bundle bundle = new Bundle();
        RatingDialog rdialog = new RatingDialog();
        rdialog.setArguments(bundle);
        rdialog.show(getSupportFragmentManager(), "rate");
    }
}
