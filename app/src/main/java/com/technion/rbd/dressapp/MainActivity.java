package com.technion.rbd.dressapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.technion.rbd.dressapp.BackEnd.DatabaseAPI;
import com.technion.rbd.dressapp.BackEnd.FirebaseCallback;
import com.technion.rbd.dressapp.FrontEnd.Measurement;
import com.technion.rbd.dressapp.FrontEnd.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // BACKEND CODE
    // Firebase auth
    private FirebaseAuth mAuth;

    // Firebase database
    private DatabaseReference databaseUsers;

    // Google login SDK
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton google_signin_button;
    private static Integer GOOGLE_RC_SIGN_IN = 1;

    String user_id;

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout r = findViewById(R.id.mask);
        r.setAlpha(0.8f);

        // Getting firebase auth client
        mAuth = FirebaseAuth.getInstance();
        // Getting facebook callback

        // Getting database reference
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");


        //-------------- Google login ------------------
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_app_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_signin_button = (SignInButton) findViewById(R.id.google_signin_btn);
        // this class implements OnClickListener and we have a wrapper fun for onClick
        google_signin_button.setOnClickListener(this);
    }

    // Sign in using google handler aux
    private void signIn() {
        if (mAuth.getCurrentUser() != null) {
            //todo: check if such scenario is possible
            Toast.makeText(MainActivity.this, "Another account is logged in!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Google login dialog + UI update
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_RC_SIGN_IN);
        findViewById(R.id.google_signin_btn).setEnabled(false);
    }

    // onClick handler for Google's login\out buttons
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_signin_btn:
                signIn();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        check_firebase_auth();
    }


    private void check_firebase_auth() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            findViewById(R.id.google_signin_btn).setEnabled(false);
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }

        // Check if any user is logged on, if not toast "Please login to continue!"
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
//            Toast.makeText(MainActivity.this, "Please login to continue!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking if coming from google login dialog
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google login", "Google sign in failed", e);
            }
        }


    }

    // Auth with google account to firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Google Auth logx", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Google Auth logx", "signInWithCredential:success");
                            FirebaseUser fire_user = mAuth.getCurrentUser();
                            String id = fire_user.getUid();
                            checkFirebaseProfile();
                            //todo: check if token is changed and then update it
//                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
//                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Google Auth logx", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void ToMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void checkFirebaseProfile() {
        DatabaseAPI api = new DatabaseAPI();
        FirebaseUser fire_user = mAuth.getCurrentUser();
        String user_id = fire_user.getUid();
        api.getUserById(user_id, new FirebaseCallback<User>() {
            @Override
            public void onSuccess(User user) {
                //todo: check if token is changed and then update it
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure() {
                Toast.makeText(MainActivity.this, "Welcome new user, please complete your profile!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }

            @Override
            public void onServerFailure() {

            }
        });
    }
}
