package com.javiervasquez.chefs.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.javiervasquez.chefs.Activities.ChefNuevoPlatoActivity;
import com.javiervasquez.chefs.Constants.Constants;
import com.javiervasquez.chefs.Holders.PlatoHolder;
import com.javiervasquez.chefs.Model.Plato;
import com.javiervasquez.chefs.R;


public class ChefMisPlatosFragment extends Fragment {

    private View view;
    private Context context;
    private RecyclerView.Adapter adapter;


    private Button BT_Nuevo_Plato;
    private FragmentTransaction transaction;
    private SharedPreferences sharedPref;

    public ChefMisPlatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chef_mis_platos, container, false);
        context = getActivity();
        sharedPref = context.getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);
        BT_Nuevo_Plato = (Button) view.findViewById(R.id.BT_Nuevo_Plato);


        BT_Nuevo_Plato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(context, ChefNuevoPlatoActivity.class));
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dish = database.getReference("Dish/published/");
        Query queryRef = dish.orderByChild("chef_id").equalTo(sharedPref.getString("user_id",""));

        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.RV_Platos);
        recycler.setHasFixedSize(false);
        recycler.setLayoutManager(new LinearLayoutManager(context));

        adapter = new FirebaseRecyclerAdapter<Plato, PlatoHolder>(Plato.class, R.layout.item_platos, PlatoHolder.class, queryRef) {
            @Override
            public void populateViewHolder(PlatoHolder platoHolder, Plato plato, int position) {

                Uri uri = Uri.parse(plato.getUrl_img());
                platoHolder.getIV_Foto().setImageURI(uri);
                platoHolder.getTV_Name().setText(plato.getName());
                platoHolder.getTV_Descripcion().setText(plato.getDescription());
                platoHolder.getTV_Ingredientes().setText(plato.getIngredients());
                platoHolder.getTV_Fecha_y_Hora().setText(plato.getDateAndHour());
                platoHolder.getTV_Cantidad().setText(plato.getQuantity());
                platoHolder.getTV_Precio().setText("$"+String.format("%,d", plato.getPrice()).replace(",","."));

            }
        };
        recycler.setAdapter(adapter);


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }


}