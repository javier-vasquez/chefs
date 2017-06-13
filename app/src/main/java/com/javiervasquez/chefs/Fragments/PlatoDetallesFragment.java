package com.javiervasquez.chefs.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javiervasquez.chefs.Constants.Constants;
import com.javiervasquez.chefs.Model.Plato;
import com.javiervasquez.chefs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlatoDetallesFragment extends Fragment {

    private SimpleDraweeView IV_Foto;
    private TextView TV_Title;
    private TextView TV_Descripcion;
    private TextView TV_Ingredientes;
    private TextView TV_Fecha_y_Hora;
    private TextView TV_Cantidad;
    private TextView TV_Precio;
    private View view;
    private Context context;
    private SharedPreferences sharedPref;


    public PlatoDetallesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plato_detalles, container, false);
        context = getActivity();
        sharedPref = context.getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);

        IV_Foto = (SimpleDraweeView) view.findViewById(R.id.IV_Foto);
        TV_Title = (TextView) view.findViewById(R.id.TV_Title);
        TV_Descripcion = (TextView) view.findViewById(R.id.TV_Descripcion);
        TV_Ingredientes = (TextView) view.findViewById(R.id.TV_Ingredientes);
        TV_Fecha_y_Hora = (TextView) view.findViewById(R.id.TV_Fecha_y_Hora);
        TV_Cantidad = (TextView) view.findViewById(R.id.TV_Cantidad);
        TV_Precio = (TextView) view.findViewById(R.id.TV_Precio);

        FirebaseDatabase.getInstance().getReference().child("Dish/")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshot_1 : snapshot.getChildren()) {
                                Plato p = snapshot_1.getValue(Plato.class);

                                if (p.getId().equalsIgnoreCase(getArguments().getString("plato_id"))){

                                Log.i("INFO====",p.getName());
                                    Uri uri = Uri.parse(p.getUrl_img());
                                    IV_Foto.setImageURI(uri);
                                    TV_Title.setText(p.getName());
                                    TV_Descripcion.setText(p.getDescription());
                                    TV_Ingredientes.setText(p.getIngredients());
                                    TV_Fecha_y_Hora.setText(p.getDateAndHour());
                                    TV_Cantidad.setText(p.getQuantity());
                                    TV_Precio.setText("$"+String.format("%,d", p.getPrice()).replace(",","."));

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        return view;
    }

}
