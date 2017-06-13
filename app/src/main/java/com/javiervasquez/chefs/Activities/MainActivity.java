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
import com.javiervasquez.chefs.Adapters.MySlidingPanelLayout;
import com.javiervasquez.chefs.Constants.Constants;
import com.javiervasquez.chefs.Fragments.ChefHistorialFragment;
import com.javiervasquez.chefs.Fragments.ChefMisPlatosFragment;
import com.javiervasquez.chefs.Fragments.ChefSolicitudesActivasFragment;
import com.javiervasquez.chefs.Fragments.UserHistorialFragment;
import com.javiervasquez.chefs.Fragments.UserPlatosDisponiblesFragment;
import com.javiervasquez.chefs.Fragments.UserSolicitudesActivasFragment;
import com.javiervasquez.chefs.Model.Plato;
import com.javiervasquez.chefs.Model.User;
import com.javiervasquez.chefs.R;

public class MainActivity extends AppCompatActivity {


    private LinearLayout BT_Solicitudes_Activas;
    private LinearLayout BT_User_Solicitudes_Activas;
    private LinearLayout BT_Cerrar_Sesion;
    private LinearLayout BT_Platos;
    private LinearLayout BT_Historial;
    private LinearLayout BT_Tus_Platos;
    private LinearLayout BT_Historial_User;

    private MySlidingPanelLayout SPL_Menu;


    private TextView TV_Username;
    private TextView TV_Score;

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
        TV_Username = (TextView) findViewById(R.id.TV_Username);
        TV_Score = (TextView) findViewById(R.id.TV_Score);


        SPL_Menu = (MySlidingPanelLayout) findViewById(R.id.SPL_Menu);

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
                TV_Username.setText("Hola, "+user.getName());
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
                    BT_Historial_User.setVisibility(View.GONE);

                }else{
                    editor.putBoolean("isChef",false);
                    editor.commit();
                    Fragment fragment = new UserPlatosDisponiblesFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commitAllowingStateLoss();

                    BT_Solicitudes_Activas.setVisibility(View.GONE);
                    BT_Historial.setVisibility(View.GONE);
                    BT_Tus_Platos.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        if(sharedPref.getBoolean("isChef",false)) {



            FirebaseDatabase.getInstance().getReference().child("Dish/delivered/")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            float score_sum = 0;
                            int i = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Plato p = snapshot.getValue(Plato.class);
                                score_sum += p.getScore();
                                i++;
                            }

                            float score = score_sum/i;

                            TV_Score.setText("Tu calificacion general es: "+score);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

        }


        BT_Cerrar_Sesion = (LinearLayout) findViewById(R.id.BT_Cerrar_Sesion);
        BT_Tus_Platos = (LinearLayout) findViewById(R.id.BT_Tus_Platos);
        BT_Solicitudes_Activas = (LinearLayout) findViewById(R.id.BT_Solicitudes_Activas);
        BT_User_Solicitudes_Activas = (LinearLayout) findViewById(R.id.BT_User_Solicitudes_Activas);
        BT_Platos = (LinearLayout) findViewById(R.id.BT_Platos);
        BT_Historial = (LinearLayout) findViewById(R.id.BT_Historial);
        BT_Historial_User = (LinearLayout) findViewById(R.id.BT_Historial_User);



        BT_Cerrar_Sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,FacadeActivity.class));
                SPL_Menu.closePane();
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
                SPL_Menu.closePane();

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
                SPL_Menu.closePane();

            }
        });

        BT_Historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChefHistorialFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
                SPL_Menu.closePane();

            }
        });

        BT_Historial_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new UserHistorialFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
                SPL_Menu.closePane();

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
                SPL_Menu.closePane();

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
                SPL_Menu.closePane();

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

}
