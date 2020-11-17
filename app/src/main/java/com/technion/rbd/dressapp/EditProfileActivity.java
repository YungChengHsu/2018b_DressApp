package com.technion.rbd.dressapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;
import com.technion.rbd.dressapp.BackEnd.DatabaseAPI;
import com.technion.rbd.dressapp.BackEnd.FirebaseCallback;
import com.technion.rbd.dressapp.FrontEnd.Measurement;
import com.technion.rbd.dressapp.FrontEnd.User;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    FirebaseAuth mAuth;
    DatabaseAPI api;
    FirebaseUser fire_user;
    String chosen_gender = "none";
    Context context;
    String old_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        fire_user = mAuth.getCurrentUser();
        api = new DatabaseAPI();
        old_location = getIntent().getStringExtra("oldLocation");

        context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar_EditProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        ImageButton btn = findViewById(R.id.editProfile_uploadPic);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*").putExtra(Intent.ACTION_GET_CONTENT, mimeTypes);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }

    /**
     * this function updates the profile via the api
     */
    private void update_profile() {

        EditText edit_name = findViewById(R.id.editProfile_name);
        EditText edit_location = findViewById(R.id.editProfile_location);
        EditText about_mex = findViewById(R.id.editProfile_aboutMe);

        String name = edit_name.getText().toString();
        String location = edit_location.getText().toString();
        String about = about_mex.getText().toString();

        final String gender = chosen_gender;

        if (location.matches("") || about.matches("")
                || gender.equals("none")) {
            Toast.makeText(EditProfileActivity.this, "Please fill in missing info!", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = fire_user.getUid();
        String email = fire_user.getEmail();
        String pic_url = fire_user.getPhotoUrl().toString();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

//        Measurement msr = new Measurement(-1, 0.0, 0.0, -1);

        final User user = new User(id, email, name, gender, pic_url, location);
        user.setUserToken(refreshedToken);

        if (filePath == null) {
            api.updateUserInfo(user, true, new FirebaseCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    Toast.makeText(EditProfileActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();

                    FirebaseMessaging.getInstance().unsubscribeFromTopic(old_location);
                    FirebaseMessaging.getInstance().subscribeToTopic(user.getUserLocation());
                    //todo: on complete listener

                    finish();
                }

                @Override
                public void onFailure() {
                    api.updateUserInfo(user, false, new FirebaseCallback<User>() {
                        @Override
                        public void onSuccess(User user) {
                            Toast.makeText(EditProfileActivity.this, "User updated successfully!", Toast.LENGTH_SHORT).show();
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(old_location);
                            FirebaseMessaging.getInstance().subscribeToTopic(user.getUserLocation());
                            //todo: on complete listener
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

                @Override
                public void onServerFailure() {

                }
            });
        } else {

            // elsee !!!!!!!!
            api.updateUserInfoWithPic(user, true, filePath, new FirebaseCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    Toast.makeText(EditProfileActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(old_location);
                    FirebaseMessaging.getInstance().subscribeToTopic(user.getUserLocation());
                    //todo: on complete listener
                    finish();
                }

                @Override
                public void onFailure() {
                    api.updateUserInfoWithPic(user, false, filePath, new FirebaseCallback<User>() {
                        @Override
                        public void onSuccess(User user) {
                            Toast.makeText(EditProfileActivity.this, "User updated successfully!", Toast.LENGTH_SHORT).show();
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

                @Override
                public void onServerFailure() {
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile_activity_toolbar, menu);
        MenuItem save_btn = menu.findItem(R.id.profile_toolbar_edit);
        save_btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                update_profile();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView editProfile_pic = findViewById(R.id.editProfilePic);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                editProfile_pic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ChooseGender(View view) {
        TextView selected = (TextView) view;
        ArrayList<TextView> gens = new ArrayList<TextView>();
        gens.add((TextView) findViewById(R.id.editProfile_men));
        gens.add((TextView) findViewById(R.id.editProfile_women));
        gens.add((TextView) findViewById(R.id.editProfile_other));

        chosen_gender = selected.getText().toString();

        for (TextView textView : gens) {
            textView.setBackground(getResources().getDrawable(
                    (selected == textView) ?
                            R.drawable.tag_round_corner_gold :
                            R.drawable.tag_round_corner_dummy));
            textView.setTextColor(getResources().getColor(
                    (selected == textView) ?
                            R.color.white :
                            R.color.filter_grey));
        }
    }
}
