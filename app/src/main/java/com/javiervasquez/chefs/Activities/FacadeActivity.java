package com.javiervasquez.chefs.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javiervasquez.chefs.Constants.Constants;
import com.javiervasquez.chefs.Model.User;
import com.javiervasquez.chefs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacadeActivity extends AppCompatActivity {


    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Context context;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facade);

        checkAndRequestPermissions();

        context=FacadeActivity.this;

        Fresco.initialize(this);

        sharedPref = getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);
        editor=sharedPref.edit();

        Map config = new HashMap();
        config.put("cloud_name", "dgj19tc67");
        config.put("api_key", "493292745442149");
        config.put("api_secret", "mOvV1gtWzB_d3Zv2URgNMYfo6Z0");
        Cloudinary cloudinary = new Cloudinary(config);

//        cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap())


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

//        myRef.setValue("Hello, World!, Lobo");


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("FB-AUTH", "onAuthStateChanged:signed_in:" + user.getUid());


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference users = database.getReference("Users").child(user.getUid());

                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            editor.putString("user_name",user.getName());
                            editor.putString("address",user.getAddress());
                            editor.commit();
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });

                    editor.putString("user_id",user.getUid());
                    editor.commit();
                    Intent it = new Intent(context, MainActivity.class);
                    startActivity(it);
                    finish();
                } else {
                    // User is signed out
                    Log.d("FB-AUTH", "onAuthStateChanged:signed_out");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent it = new Intent(context, LoginActivity.class);
                            startActivity(it);
                            finish();
                        }
                    }, 2000);
                }
                // ...
            }
        };


//        sharedPref = getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);
//        editor=sharedPref.edit();
//
//        if((sharedPref.getString("username",null)!=null)&&(sharedPref.getString("password",null)!=null)){
//
//
//        }else{
//        Log.w("NADA","==================");
//
//    }


    }

    //--------------------------------------REQUEST PERMISSIONS------------------------------------------------------

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int callerPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (callerPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    //--------------------------------------REQUEST PERMISSIONS------------------------------------------------------
}
