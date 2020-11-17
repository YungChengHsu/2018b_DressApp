package com.technion.rbd.dressapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;

import android.util.Log;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;
import com.technion.rbd.dressapp.BackEnd.Checker;
import com.technion.rbd.dressapp.BackEnd.DatabaseAPI;
import com.technion.rbd.dressapp.BackEnd.FirebaseCallback;
import com.technion.rbd.dressapp.FrontEnd.Item;
import com.technion.rbd.dressapp.FrontEnd.Measurement;
import com.technion.rbd.dressapp.FrontEnd.User;
import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    String user_id;
    Menu mMenu;
    DatabaseAPI api;
    String filter_cat = "Tops";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // inflate toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get firebase refs
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fire_user = mAuth.getCurrentUser();
        api = new DatabaseAPI();

        // get the id of the user whom should be shown
        user_id = getIntent().getStringExtra("userId");


        // Set up scrollview in profile page, which shows what this user has
        recyclerView = (RecyclerView) findViewById(R.id.profileRV);

        // inflate the profile info
        inflateProfileInfo(api);
    }

    @Override
    public void onStart() {
        inflateProfileInfo(api);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_activity_toolbar, menu);
        MenuItem logout_btn = menu.findItem(R.id.profile_toolbar_logout);
        logout_btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SignOut();
                return false;
            }
        });

        MenuItem chat_btn = menu.findItem(R.id.profile_toolbar_contact);
        chat_btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String fire_user_id = mAuth.getCurrentUser().getUid();

                api.startChat(fire_user_id, user_id, new FirebaseCallback<String>() {
                    @Override
                    public void onSuccess(String thread_id) {
                        Intent intent = new Intent(ProfileActivity.this, SendMessageActivity.class);
                        intent.putExtra("threadId", thread_id);
                        intent.putExtra("userId", user_id);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure() {
                        //todo:needed?
                    }

                    @Override
                    public void onServerFailure() {
                        //todo:needed?
                    }
                });


                return true;
                //todo: check for return type
            }
        });


        // save menu ref
        mMenu = menu;

        // update the toolbar icons
        updateUI();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        if (id == R.id.profile_toolbar_edit) {
            String fire_user_id = mAuth.getCurrentUser().getUid();
            api.getUserById(fire_user_id, new FirebaseCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("oldLocation", user.getUserLocation());
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure() {

                }

                @Override
                public void onServerFailure() {

                }
            });

        }

        return super.onOptionsItemSelected(item);
    }


    public void ToItemView(View view) {
        Intent intent = new Intent(this, ItemViewActivity.class);
        ProfileAdapter.ViewHolder viewHolder = (ProfileAdapter.ViewHolder) recyclerView.findContainingViewHolder(view);
        intent.putExtra("itemId", viewHolder.gethItemId());
        startActivity(intent);
    }

    public void ChangeTab(View view) {
        TextView selected = (TextView) view;
        ArrayList<TextView> tabs = new ArrayList<TextView>();
        tabs.add((TextView) findViewById(R.id.profile_tops));
        tabs.add((TextView) findViewById(R.id.profile_pants));
        tabs.add((TextView) findViewById(R.id.profile_shoes));
        tabs.add((TextView) findViewById(R.id.profile_accessories));
        tabs.add((TextView) findViewById(R.id.profile_dress));
        tabs.add((TextView) findViewById(R.id.profile_skirts));
        filter_cat = selected.getText().toString();
        inflateProfileInfo(api);
        for (TextView textview : tabs) {
            textview.setBackground((textview == selected) ?
                    getResources().getDrawable(R.drawable.bg_gradient_gold) :
                    getResources().getDrawable(R.drawable.bg_grey_unselected));
        }
    }

    public void ToEditProfile(MenuItem item) {
        startActivity(new Intent(this, EditProfileActivity.class));
    }

    void SignOut() {
        Toast.makeText(ProfileActivity.this, "You are loged out!", Toast.LENGTH_SHORT).show();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_app_client_id))
                .requestEmail()
                .build();
        // Google login SDK
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(ProfileActivity.this, gso);

        mGoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();

//                            findViewById(R.id.google_signin_btn).setEnabled(true);
        Toast.makeText(ProfileActivity.this, "Signed out!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    /**
     * this function inflates the info of the user's profile using the api
     *
     * @param api
     */
    void inflateProfileInfo(DatabaseAPI api) {
        api.getUserById(user_id, new FirebaseCallback<User>() {
            @Override
            public void onSuccess(User user) { // manged successfully
                getSupportActionBar().setTitle(user.getUserName() + "\'s Profile");
                ((TextView) findViewById(R.id.profile_name)).setText(user.getUserName());
                ((TextView) findViewById(R.id.profile_address)).setText(user.getUserLocation());
                ((TextView) findViewById(R.id.profile_rating)).setText("4.2");
                ImageView pic = findViewById(R.id.activityProfilePic);
                Picasso.get().load(user.getUserProfilePic()).into(pic);
            }

            @Override
            public void onFailure() { // failure retrieving user obj from DB - no such user
                Toast.makeText(ProfileActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerFailure() {
                Toast.makeText(ProfileActivity.this, "Server Failure", Toast.LENGTH_SHORT).show();
            }
        });

        // retrieving current user's list of items
        api.getFilteredItems(new Checker<Item>() {
            @Override
            public boolean check(Item item) {
                return item.getItemOwnerId().equals(user_id) && item.getItemCategory().equals(filter_cat);
            }
        }, new FirebaseCallback<List<Item>>() {
            @Override
            public void onSuccess(List<Item> items) { // retrieved successfully
                ArrayList<ProfileItems> items_list = new ArrayList<ProfileItems>();
                // inflating the recycleView with the items found,
                //     after converting to correct type
                for (Item item : items) {
                    ProfileItems profileItem =
                            new ProfileItems(item.getItemPicUrl(),
                                    item.getItemName(),
                                    //TODO fix measurements
                                    String.valueOf(item.getItemMeasurement().getDress_chest()),
                                    String.valueOf(item.getItemMeasurement().getDress_chest()),
                                    String.valueOf(item.getItemMeasurement().getDress_chest()),
                                    item.getItemId());
                    items_list.add(profileItem);
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView.LayoutManager rvLayoutManager = layoutManager;
                recyclerView.setLayoutManager(rvLayoutManager);

                ProfileAdapter adapter = new ProfileAdapter(ProfileActivity.this, items_list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                Toast.makeText(ProfileActivity.this, "User has no " + filter_cat + " items!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerFailure() {
                Toast.makeText(ProfileActivity.this, "Connection error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * this method is used to update the toolbar icons according to whom the profile belongs
     * Current user vs another user
     */
    void updateUI() {
        String fire_user_id = mAuth.getCurrentUser().getUid();
        if (!user_id.equals(fire_user_id)) {
            MenuItem logout_btn = mMenu.findItem(R.id.profile_toolbar_logout);
            MenuItem edit_btn = mMenu.findItem(R.id.profile_toolbar_edit);
            logout_btn.setVisible(false);
            edit_btn.setVisible(false);
        } else {
            MenuItem cntct_btn = mMenu.findItem(R.id.profile_toolbar_contact);
            cntct_btn.setVisible(false);
        }
    }
}
