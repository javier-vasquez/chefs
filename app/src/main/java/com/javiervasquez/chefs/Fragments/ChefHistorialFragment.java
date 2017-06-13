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


public class ChefHistorialFragment extends Fragment {

    private View view;
    private Context context;
    private RecyclerView.Adapter adapter;


    private FragmentTransaction transaction;
    private SharedPreferences sharedPref;

    public ChefHistorialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chef_historial, container, false);
        context = getActivity();
        sharedPref = context.getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dish = database.getReference("Dish/delivered/");
        Query queryRef = dish.orderByChild("chef_id").equalTo(sharedPref.getString("user_id",""));

        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.RV_Platos);
        recycler.setHasFixedSize(false);
        recycler.setLayoutManager(new LinearLayoutManager(context));

        adapter = new FirebaseRecyclerAdapter<Plato, PlatoHolder>(Plato.class, R.layout.item_platos, PlatoHolder.class, queryRef) {
            @Override
            public void populateViewHolder(PlatoHolder platoHolder, final Plato plato, int position) {

                Uri uri = Uri.parse(plato.getUrl_img());
                platoHolder.getIV_Foto().setImageURI(uri);
                platoHolder.getTV_Name().setText(plato.getName());
                platoHolder.getTV_Descripcion().setText(plato.getAddress());
                platoHolder.getTV_Ingredientes().setText(plato.getUser_name());
                platoHolder.getTV_Fecha_y_Hora().setText(plato.getDateAndHour());
                platoHolder.getTV_Cantidad().setText(plato.getQuantity());
                platoHolder.getTV_Precio().setText("$"+String.format("%,d", plato.getPrice()).replace(",","."));
                platoHolder.getTV_Score().setText(String.valueOf(plato.getScore()));
                platoHolder.getTV_Score().setVisibility(View.VISIBLE);

                platoHolder.getCV_Item().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new PlatoDetallesFragment();
                        Bundle b = new Bundle();
                        b.putString("plato_id",plato.getId());
                        fragment.setArguments(b);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                    }
                });

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
