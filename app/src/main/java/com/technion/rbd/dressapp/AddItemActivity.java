package com.technion.rbd.dressapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.technion.rbd.dressapp.BackEnd.DatabaseAPI;
import com.technion.rbd.dressapp.BackEnd.FirebaseCallback;
import com.technion.rbd.dressapp.FrontEnd.Item;
import com.technion.rbd.dressapp.FrontEnd.Measurement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.technion.rbd.dressapp.FrontEnd.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AddItemActivity extends AppCompatActivity implements
        FragmentAddItemTops.OnFragmentInteractionListener, FragmentAddItemPants.OnFragmentInteractionListener,
        FragmentAddItemShoes.OnFragmentInteractionListener, FragmentAddItemAccessories.OnFragmentInteractionListener,
        FragmentAddItemDress.OnFragmentInteractionListener, FragmentAddItemSkirts.OnFragmentInteractionListener {

    // Firebase auth
    private FirebaseAuth mAuth;

    private StorageReference mStorageRef;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    String chosen_cat = "none";
    String chosen_gen = "none";

    TextView tvTops;
    TextView tvPants;
    TextView tvShoes;
    TextView tvAccessories;
    TextView tvDress;
    TextView tvSkirts;

    //Category Fragments
    FragmentAddItemTops fait;
    FragmentAddItemPants faip;
    FragmentAddItemShoes fais;
    FragmentAddItemAccessories faia;
    FragmentAddItemDress faid;
    FragmentAddItemSkirts faisk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_AddItem);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add a New Item");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton btn = findViewById(R.id.addItem_uploadPic);
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

        tvTops = findViewById(R.id.addItem_tops);
        tvPants = findViewById(R.id.addItem_pants);
        tvShoes = findViewById(R.id.addItem_shoes);
        tvAccessories = findViewById(R.id.addItem_accessories);
        tvDress = findViewById(R.id.addItem_dress);
        tvSkirts = findViewById(R.id.addItem_skirts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item_activity_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.addItem_post) {
            postNewItem();
        }
        else if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView addItem_itemPic = findViewById(R.id.itemPic);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                addItem_itemPic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void postNewItem(/*View view*/) { //view = btn
        final EditText nameEditText = (EditText) findViewById(R.id.addItem_itemName);
        final EditText descriptionEditText = (EditText) findViewById(R.id.addItem_description);
//        final EditText lengthEditText = (EditText) findViewById(R.id.addItem_length);
//        final Spinner bustSpinner = (Spinner) findViewById(R.id.addItem_bustSize);
//        final Spinner waistSpinner = (Spinner) findViewById(R.id.addItem_waistSize);
        final ImageView itemImageView = (ImageView) findViewById(R.id.itemPic);

        final String itemName = nameEditText.getText().toString();

        final String itemGender = chosen_gen;

//        String itemBust = bustSpinner.getSelectedItem().toString();
//        String itemWaist = waistSpinner.getSelectedItem().toString();

        final String itemCategory = chosen_cat;
        final String itemDescription = descriptionEditText.getText().toString();
//        String itemLength = lengthEditText.getText().toString();

        FirebaseUser fire_user = mAuth.getCurrentUser();
        final String userUid = fire_user.getUid();
        final DatabaseAPI api = new DatabaseAPI();

        final String someId = (new Random()).toString();

        if (null == filePath) {
            Toast.makeText(AddItemActivity.this,
                    "Please add a picture before posting", Toast.LENGTH_SHORT).show();

        } else if (itemCategory.equals("none") || itemGender.equals("none") ||
                /*itemLength.matches("") || itemBust.matches("") ||
                itemWaist.matches("") ||*/ itemDescription.matches("")) {
            Toast.makeText(AddItemActivity.this,
                    "Please fill out all the fields", Toast.LENGTH_SHORT).show();
        } else {
            //TODO : assign the right measurement according to the category!
//            final Measurement itemMeasurement =
//                    new Measurement(
//                            Double.valueOf(itemLength),
//                            Double.valueOf(itemBust),
//                            Double.valueOf(itemWaist),
//                            Double.valueOf("-1"));
            final Measurement itemMeasurement = new Measurement();
            api.getUserById(userUid, new FirebaseCallback<User>() {
                @Override
                public void onSuccess(User user) {

                    Item item = new Item(someId, user.getUserId(), itemGender,
                            itemCategory, itemDescription, itemMeasurement,
                            "x", itemName, user.getUserName(), user.getUserLocation());

                    api.addNewItemWithPic(item,
                            filePath,
                            new FirebaseCallback<Item>() {
                                @Override
                                public void onSuccess(Item item) {
                                    Toast.makeText(AddItemActivity.this,
                                            "Item added successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddItemActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    //editx
                                    finish();
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(AddItemActivity.this,
                                            "Item with same ID already exists!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onServerFailure() {
                                    Toast.makeText(AddItemActivity.this,
                                            "Connection error!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onFailure() {
                    Toast.makeText(AddItemActivity.this,
                            "Please update your profile before posting!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onServerFailure() {
                }
            });


        }
    }

    public void ChooseGender(View view) {
        TextView selected = (TextView) view;
        ArrayList<TextView> gens = new ArrayList<TextView>();
        gens.add((TextView) findViewById(R.id.addItem_men));
        gens.add((TextView) findViewById(R.id.addItem_women));
        gens.add((TextView) findViewById(R.id.addItem_neutral));
        chosen_gen = selected.getText().toString();
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

    public void ChooseCat(View view) {
        TextView selected = (TextView) view;
        ArrayList<TextView> cats = new ArrayList<TextView>();
        cats.add(tvTops);
        cats.add(tvPants);
        cats.add(tvShoes);
        cats.add(tvAccessories);
        cats.add(tvDress);
        cats.add(tvSkirts);
        chosen_cat = selected.getText().toString();

        for(TextView textView : cats) {
            textView.setBackground(getResources().getDrawable(
                    (selected == textView) ?
                            R.drawable.tag_round_corner_gold :
                            R.drawable.tag_round_corner_dummy));
            textView.setTextColor(getResources().getColor(
                    (selected == textView) ?
                            R.color.white :
                            R.color.filter_grey));
        }

        if(selected.getId() == R.id.addItem_tops) {
            fait = new FragmentAddItemTops();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.addItemFragmentTops, fait);
            ft.commit();
        } else if(selected.getId() == R.id.addItem_pants) {
            faip = new FragmentAddItemPants();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.addItemFragmentTops, faip);
            ft.commit();
        } else if(selected.getId() == R.id.addItem_shoes) {
            fais = new FragmentAddItemShoes();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.addItemFragmentTops, fais);
            ft.commit();
        } else if(selected.getId() == R.id.addItem_accessories) {
            faia = new FragmentAddItemAccessories();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.addItemFragmentTops, faia);
            ft.commit();
        } else if(selected.getId() == R.id.addItem_dress) {
            faid = new FragmentAddItemDress();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.addItemFragmentTops, faid);
            ft.commit();
        } else if(selected.getId() == R.id.addItem_skirts) {
            faisk = new FragmentAddItemSkirts();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.addItemFragmentTops, faisk);
            ft.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
