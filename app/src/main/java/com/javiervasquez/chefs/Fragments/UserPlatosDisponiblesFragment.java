package com.javiervasquez.chefs.Fragments;


import android.content.Context;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.javiervasquez.chefs.Constants.Constants;
import com.javiervasquez.chefs.Holders.PlatoHolder;
import com.javiervasquez.chefs.Model.Plato;
import com.javiervasquez.chefs.Model.User;
import com.javiervasquez.chefs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPlatosDisponiblesFragment extends Fragment {


    public UserPlatosDisponiblesFragment() {
        // Required empty public constructor
    }
    private Context context;
    private View view;

    private RecyclerView.Adapter adapter;



    private FragmentTransaction transaction;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_platos_disponibles, container, false);

        context = getActivity();

        sharedPref = context.getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dish = database.getReference("Dish/published/");


        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.RV_Platos);
        recycler.setHasFixedSize(false);
        recycler.setLayoutManager(new LinearLayoutManager(context));

        adapter = new FirebaseRecyclerAdapter<Plato, PlatoHolder>(Plato.class, R.layout.item_platos, PlatoHolder.class, dish) {
            @Override
            public void populateViewHolder(PlatoHolder platoHolder, final Plato plato, int position) {

                Uri uri = Uri.parse(plato.getUrl_img());
                platoHolder.getIV_Foto().setImageURI(uri);
                platoHolder.getTV_Name().setText(plato.getName());
                platoHolder.getTV_Descripcion().setText(plato.getDescription());
                platoHolder.getTV_Ingredientes().setText(plato.getIngredients());
                platoHolder.getTV_Fecha_y_Hora().setText(plato.getDateAndHour());
                platoHolder.getTV_Cantidad().setText(plato.getQuantity());
                platoHolder.getTV_Precio().setText("$"+String.format("%,d", plato.getPrice()).replace(",","."));
                platoHolder.getBT_Comprar().setVisibility(View.VISIBLE);

                platoHolder.getBT_Comprar().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Plato plato_1 = plato;
                        plato_1.setBuyer_id(sharedPref.getString("user_id",""));
                        plato_1.setAddress(sharedPref.getString("address",""));
                        plato_1.setUser_name(sharedPref.getString("user_name",""));

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference plato_ref = database.getReference("Dish/published/"+plato_1.getId());
                        plato_ref.removeValue();

                        database.getReference().child("Dish/bought/"+plato_1.getId()).setValue(plato_1);



                    }
                });

            }
        };
        recycler.setAdapter(adapter);


        return view;
    }

}
