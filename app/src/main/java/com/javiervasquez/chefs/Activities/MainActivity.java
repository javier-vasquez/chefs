package com.javiervasquez.chefs.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javiervasquez.chefs.Constants.Constants;
import com.javiervasquez.chefs.Fragments.ChefMisPlatosFragment;
import com.javiervasquez.chefs.Fragments.ChefSolicitudesActivasFragment;
import com.javiervasquez.chefs.Fragments.UserPlatosDisponiblesFragment;
import com.javiervasquez.chefs.Fragments.UserSolicitudesActivasFragment;
import com.javiervasquez.chefs.Model.User;
import com.javiervasquez.chefs.R;

public class MainActivity extends AppCompatActivity {


    private LinearLayout BT_Solicitudes_Activas;
    private LinearLayout BT_User_Solicitudes_Activas;
    private LinearLayout BT_Cerrar_Sesion;
    private LinearLayout BT_Platos;
    private LinearLayout BT_Tus_Platos;

    private FirebaseAuth mAuth;
    private  Toolbar toolbar;
    private String user_id;
    private FirebaseDatabase database;
    private TextView TV_Name;
    private FragmentTransaction transaction;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        sharedPref = getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);
        editor=sharedPref.edit();

        TV_Name = (TextView) findViewById(R.id.TV_Name);


        setSupportActionBar(toolbar);

        user_id = mAuth.getCurrentUser().getUid();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);


        database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("Users").child(user_id);

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                TV_Name.setText("Hola "+user.getName().split(" ")[0]);
                if(user.getChef()){
                    editor.putBoolean("isChef",true);
                    editor.commit();
                    Fragment fragment = new ChefMisPlatosFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commitAllowingStateLoss();

                    BT_User_Solicitudes_Activas.setVisibility(View.GONE);
                    BT_Platos.setVisibility(View.GONE);

                }else{
                    editor.putBoolean("isChef",false);
                    editor.commit();
                    Fragment fragment = new UserPlatosDisponiblesFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commitAllowingStateLoss();

                    BT_Solicitudes_Activas.setVisibility(View.GONE);
                    BT_Tus_Platos.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        BT_Cerrar_Sesion = (LinearLayout) findViewById(R.id.BT_Cerrar_Sesion);
        BT_Tus_Platos = (LinearLayout) findViewById(R.id.BT_Tus_Platos);
        BT_Solicitudes_Activas = (LinearLayout) findViewById(R.id.BT_Solicitudes_Activas);
        BT_User_Solicitudes_Activas = (LinearLayout) findViewById(R.id.BT_User_Solicitudes_Activas);
        BT_Platos = (LinearLayout) findViewById(R.id.BT_Platos);



        BT_Cerrar_Sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,FacadeActivity.class));
            }
        });

        BT_Tus_Platos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChefMisPlatosFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();

            }
        });

        BT_Platos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new UserPlatosDisponiblesFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();

            }
        });

        BT_Solicitudes_Activas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChefSolicitudesActivasFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();

            }
        });

        BT_User_Solicitudes_Activas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new UserSolicitudesActivasFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

}
