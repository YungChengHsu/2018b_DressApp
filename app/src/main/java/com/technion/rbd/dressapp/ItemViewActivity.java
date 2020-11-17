package com.technion.rbd.dressapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.technion.rbd.dressapp.BackEnd.Checker;
import com.technion.rbd.dressapp.BackEnd.DatabaseAPI;
import com.technion.rbd.dressapp.BackEnd.FirebaseCallback;
import com.technion.rbd.dressapp.BackEnd.Order;
import com.technion.rbd.dressapp.FrontEnd.Item;
import com.technion.rbd.dressapp.FrontEnd.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemViewActivity extends AppCompatActivity {


    // Firebase auth
    private FirebaseAuth mAuth;
    DatabaseAPI api;
    String currItemId;
    FirebaseUser fire_user;
    // Firebase database
    private DatabaseReference databaseUsers, databaseItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        // Getting firebase auth client
        mAuth = FirebaseAuth.getInstance();
        api = new DatabaseAPI();
        fire_user = mAuth.getCurrentUser();
        // get the current iem id!
        currItemId = getIntent().getStringExtra("itemId");

        inflateItemInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("UI logx", "Here for some reasons!");
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void ToProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    public void contactOwner(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String message = "Hello,\nI would like to get the item please.";
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    // Intent emailIntent = new Intent(Intent.ACTION_SEND);

    public void GoBack(View view) {
        this.finish();
    }

    void inflateItemInfo() {
        api.getItemById(currItemId,
                new FirebaseCallback<Item>() {
                    @Override
                    public void onSuccess(final Item item) {
                        final String owner_id = item.getItemOwnerId();
                        api.getUserById(owner_id, new FirebaseCallback<User>() {
                            @Override
                            public void onSuccess(User user) {
                                ((TextView) findViewById(R.id.itemViewOwnerName)).setText(item.getItemOwnerName());
                                ((TextView) findViewById(R.id.itemViewOwnerLocation)).setText(item.getItemLocation());
                                ((TextView) findViewById(R.id.itemViewOwnerLocation)).setText(user.getUserLocation());

                                Picasso.get().load(user.getUserProfilePic()).into((CircleImageView) findViewById(R.id.itemViewOwnerPic));
                                Picasso.get().load(item.getItemPicUrl())
                                        .into((ImageView) findViewById(R.id.itemView_activity_image));
                                ((TextView) findViewById(R.id.item_description))
                                        .setText(item.getItemDescription());
//                                ((TextView) findViewById(R.id.profileItemGender))
//                                        .setText(item.getItemGender());
                                ((TextView) findViewById(R.id.itemViewItemName))
                                        .setText(String.valueOf(item.getItemName()));
                                //TODO: fix the measurements view according to the category!
                                ((TextView) findViewById(R.id.itemView_activity_bust))
                                        .setText(String.valueOf(item.getItemMeasurement().getDress_chest()));
                                ((TextView) findViewById(R.id.itemView_activity_waist))
                                        .setText(String.valueOf(item.getItemMeasurement().getDress_chest()));
                                ((TextView) findViewById(R.id.itemView_activity_length))
                                        .setText(String.valueOf(item.getItemMeasurement().getDress_chest()));
                                ((TextView) findViewById(R.id.itemViewOwnerLocation)).setText(item.getItemLocation());

                                ((CircleImageView) findViewById(R.id.itemViewOwnerPic)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ItemViewActivity.this, ProfileActivity.class);
                                        intent.putExtra("userId", item.getItemOwnerId());
                                        ItemViewActivity.this.startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onFailure() {

                            }

                            @Override
                            public void onServerFailure() {

                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(ItemViewActivity.this, "Item with id " + currItemId + " does not exist", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onServerFailure() {
                        Toast.makeText(ItemViewActivity.this, "Server Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void orderItem(View view) {
        //todo maybe dynamicly bind the order button.
        api.getItemById(currItemId, new FirebaseCallback<Item>() {
            @Override
            public void onSuccess(Item item) {
                String owner_id = fire_user.getUid();
                Order order = new Order(item.getItemId(), item.getItemOwnerId(), owner_id);
                api.addNewOrder(order, new FirebaseCallback<Order>() {
                    @Override
                    public void onSuccess(Order order) {
                        //todo: show alert / refresh activity to show that item is ordrered now!
                        Toast.makeText(ItemViewActivity.this, "Item was ordered successfully, contact the owner to the arrange the delivery!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onServerFailure() {
                        Toast.makeText(ItemViewActivity.this, "Server Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onServerFailure() {
                Toast.makeText(ItemViewActivity.this, "Server Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowHideBar(View view) {
        RelativeLayout rl = findViewById(R.id.itemView_bar);
        rl.setVisibility((rl.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE);
    }

}
