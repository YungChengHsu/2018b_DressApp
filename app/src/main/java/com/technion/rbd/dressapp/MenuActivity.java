package com.technion.rbd.dressapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.technion.rbd.dressapp.BackEnd.Checker;
import com.technion.rbd.dressapp.BackEnd.DatabaseAPI;
import com.technion.rbd.dressapp.BackEnd.FcmAPI;
import com.technion.rbd.dressapp.BackEnd.FirebaseCallback;
import com.technion.rbd.dressapp.FrontEnd.Item;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.technion.rbd.dressapp.FrontEnd.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    // Firebase auth
    private FirebaseAuth mAuth;

    RecyclerView recyclerView;
    ArrayList<MenuItems> miList;
    DatabaseAPI api;
    FirebaseUser fire_user;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Target tgt;
    private Menu mMenu;
    Picasso mPicasso = Picasso.get();
    // FirebaseMessaging fbs;

    FloatingActionMenu genFam;
    FloatingActionMenu catFam;


    String catLabelText;
    String genLabelText;

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // inflate toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        FirebaseMessaging.getInstance().subscribeToTopic("haifa");


        // Instantiate firebase connection objects
        mAuth = FirebaseAuth.getInstance();
        fire_user = mAuth.getCurrentUser();
        api = new DatabaseAPI();

        genFam = findViewById(R.id.menu_genFam);
        catFam = findViewById(R.id.menu_catFam);

        // Set up scrollview in profile page, which shows what this user has
        recyclerView = findViewById(R.id.menuRV);

        // load profile icon pic.
        setProfileIcon();

        // load items
        reloadMenu(); //TODO use AsyncTask

        // testing FCM
        FcmAPI fcm = new FcmAPI();
        fcm.onTokenRefresh();

/*
        createNotificationChannel();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MenuActivity.this, "1337").setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Title");
        bigTextStyle.bigText("Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in.");

        notificationBuilder.setStyle(bigTextStyle);

        NotificationManager mNotifyMgr = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, notificationBuilder.build());*/


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "blablachannel";
            String description = "chatchannel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1337", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void ToItemView(View view) {
        Intent intent = new Intent(this, ItemViewActivity.class);
        startActivity(intent);
    }

    public void ToAddItem(View view) {

        Intent intent = new Intent(MenuActivity.this, AddItemActivity.class);
        startActivity(intent);

    }

    public void setCatLabel(View view) {
        FloatingActionButton fab = (FloatingActionButton) view;
        final String catLabelText = fab.getLabelText();
        TextView textView = (TextView) findViewById(R.id.menu_categoryTab);
        textView.setText(catLabelText);
        textView.setBackground(getResources().getDrawable(R.drawable.tag_round_corner_gold));
        textView.setTextColor(getResources().getColor(R.color.white));
        catFam.close(true);
        reloadMenu();
    }

    public void setGenderLabel(View view) {
        FloatingActionButton fab = (FloatingActionButton) view;
        final String genderLabelText = fab.getLabelText();
        TextView textView = (TextView) findViewById(R.id.menu_genderTab);
        textView.setText(genderLabelText);
        textView.setBackground(getResources().getDrawable(R.drawable.tag_round_corner_gold));
        textView.setTextColor(getResources().getColor(R.color.white));
        genFam.close(true);
        reloadMenu();
    }

    public void openDialog(String catLabelText) {
        Log.d("Cat Text", catLabelText);
        Bundle bundle = new Bundle();
        bundle.putString("cat", catLabelText);
        AdvancedSearchDialog orderdialog = new AdvancedSearchDialog();
        orderdialog.setArguments(bundle);
        orderdialog.show(getSupportFragmentManager(), "advancedSearch");
    }

    public void setProfileIcon() {
        tgt = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                MenuItem mi = mMenu.findItem(R.id.menu_toolbar_userPic);
                //mi.setIcon(d);
                Log.d("UI logx", "Inside");
                final int width = bitmap.getWidth();
                final int height = bitmap.getHeight();
                final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                float y = (height / 2);
                float x = (width / 2);

                final Path path = new Path();
                path.addCircle(
                        x//  (float) (width / 2)
                        , y//(float) (height / 2)
                        , (float) Math.min((width / 2), (height / 2))
                        , Path.Direction.CCW);

                final Canvas canvas = new Canvas(outputBitmap);
                canvas.clipPath(path);
                canvas.drawBitmap(bitmap, 0, 0, null);

                Drawable ff = new BitmapDrawable(getResources(), outputBitmap);

                mi.setIcon(ff);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e("UI error logx", "Picasso failed! 11");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        api.getUserById(fire_user.getUid().toString(), new FirebaseCallback<User>() {
            @Override
            public void onSuccess(User user) {
                Log.d("UI logx", "Loading profile pic!");
                //   final ActionBar ab = getSupportActionBar();
                mPicasso
                        .load(user.getUserProfilePic())
                        .into(tgt);
            }

            @Override
            public void onFailure() {
                mPicasso
                        .load(fire_user.getPhotoUrl())
                        .into(tgt);
                //Toast.makeText(MenuActivity.this, "Please update your missing profile info!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerFailure() {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_toolbar, menu);
        Toolbar filter = findViewById(R.id.toolbar_filter);
        filter.inflateMenu(R.menu.menu_activity_filter_clear);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_toolbar_inbox) {
            Log.d("Inbox", catLabelText);
            startActivity(new Intent(this, ChatMenuActivity.class));
        }

        if (id == R.id.menu_toolbar_myOrder) {
            startActivity(new Intent(this, MyOderActivity.class));
        }

        if (id == R.id.menu_toolbar_search) {
            //startActivity(new Intent(this, SendMessageActivity.class));
            Log.d("Before Search", catLabelText);
            openDialog(catLabelText);
        }

        if (id == R.id.menu_toolbar_userPic) {
            Intent intent = new Intent(new Intent(this, ProfileActivity.class));
            intent.putExtra("userId", mAuth.getCurrentUser().getUid());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void ClearFilter(MenuItem item) {
        TextView textView = (TextView) findViewById(R.id.menu_categoryTab);
        textView.setText("Category");
        textView.setBackground(getResources().getDrawable(R.drawable.tag_round_corner_dummy));
        textView.setTextColor(getResources().getColor(R.color.filter_grey));

        TextView textView_2 = (TextView) findViewById(R.id.menu_genderTab);
        textView_2.setText("Gender");
        textView_2.setBackground(getResources().getDrawable(R.drawable.tag_round_corner_dummy));
        textView_2.setTextColor(getResources().getColor(R.color.filter_grey));

        reloadMenu();
    }

    /**
     * this function loads the items on the menu, with considering the user filter options.
     */
    public void reloadMenu() {
        catLabelText = ((TextView) findViewById(R.id.menu_categoryTab)).getText().toString();
        genLabelText = ((TextView) findViewById(R.id.menu_genderTab)).getText().toString();
        final DatabaseAPI api = new DatabaseAPI();
        final String category_flag = ((TextView) findViewById(R.id.menu_categoryTab)).getText().toString();
        final String gender_flag = ((TextView) findViewById(R.id.menu_genderTab)).getText().toString();

        api.getFilteredItems(new Checker<Item>() {
            final String gender_filter = gender_flag;
            final String cat_filter = category_flag;

            /**
             * predicate to filter items based on the FAB filter options!
             * @param item - the item on which we are checking if it will pass or not
             * @return
             */
            @Override
            public boolean check(Item item) {
                String item_gender = item.getItemGender();
                String item_cat = item.getItemCategory();
                if (cat_filter.equals("Category")) {

                    if (gender_filter.equals("Gender")) {
                        return true;
                    } else {
                        return (item_gender.equals(gender_filter));
                    }
                } else {
                    if (!gender_filter.equals("Gender")) {
                        return (gender_filter.equals(item_gender) && cat_filter.equals(item_cat));
                    } else {
                        return cat_filter.equals(item_cat);
                    }
                }
            }
        }, new FirebaseCallback<List<Item>>() {
            @Override
            public void onSuccess(List<Item> items) { // retrieved successfully

                final ArrayList<MenuItems> items_list = new ArrayList<MenuItems>();
                // inflating the recycleView with the items found,
                //     after converting to correct type
                for (final Item item : items) {
                    final String owner_id = item.getItemOwnerId();
                    api.getUserById(owner_id, new FirebaseCallback<User>() {
                        @Override
                        public void onSuccess(User user) {
                            // get the current item owner info to show in the item view.
                            final MenuItems menuItem =
                                    new MenuItems(item.getItemPicUrl(), user.getUserProfilePic().toString()
                                            ,
                                            item.getItemName(),
                                            // fix getting the right Measurement
                                            String.valueOf(item.getItemMeasurement().getDress_chest()),
                                            String.valueOf(item.getItemMeasurement().getDress_chest()),
                                            String.valueOf(item.getItemMeasurement().getDress_chest()),
                                            item.getItemOwnerName(),
                                            item.getItemLocation()
                                            , item.getItemId()
                                    );
                            items_list.add(menuItem);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(MenuActivity.this);
                            RecyclerView.LayoutManager rvLayoutManager = layoutManager;
                            recyclerView.setLayoutManager(rvLayoutManager);

                            MenuAdapter adapter = new MenuAdapter(MenuActivity.this, items_list);
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onFailure() {
                        }

                        @Override
                        public void onServerFailure() {

                        }
                    });
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(MenuActivity.this,
                        "No matching items! please refine your filter", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServerFailure() {
                Toast.makeText(MenuActivity.this,
                        "Connection error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

