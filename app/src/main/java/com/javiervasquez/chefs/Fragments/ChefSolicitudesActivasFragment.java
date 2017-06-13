package com.javiervasquez.chefs.Fragments;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.javiervasquez.chefs.Activities.ChefNuevoPlatoActivity;
import com.javiervasquez.chefs.Constants.Constants;
import com.javiervasquez.chefs.Holders.PlatoHolder;
import com.javiervasquez.chefs.Model.Plato;
import com.javiervasquez.chefs.Model.User;
import com.javiervasquez.chefs.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChefSolicitudesActivasFragment extends Fragment {


    public ChefSolicitudesActivasFragment() {
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
        view = inflater.inflate(R.layout.fragment_solicitudes_activas, container, false);
        context = getActivity();

        sharedPref = context.getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dish = database.getReference("Dish/bought/");
        Query queryRef = dish.orderByChild("chef_id").equalTo(sharedPref.getString("user_id",""));
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.chefs_logo_2)
                                .setContentTitle("Tienes un nuevo comprador")
                                .setContentText("Ingresa para ver");
                int mNotificationId = 001;
                NotificationManager mNotifyMgr =
                        (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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


                if(plato.getState()==1) {
                    platoHolder.getBT_Comprar().setText(getString(R.string.despachar));
                    platoHolder.getBT_Comprar().setVisibility(View.VISIBLE);

                    platoHolder.getBT_Comprar().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Plato plato_1 = plato;
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            plato_1.setState(2);
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("Dish/bought/" + plato.getId(), plato);
                            database.getReference().updateChildren(childUpdates);
                        }
                    });
                }else if(plato.getState()==2){
                    platoHolder.getBT_Comprar().setText(getString(R.string.despachado));
                    platoHolder.getBT_Comprar().setTextColor(ResourcesCompat.getColor(getResources(),android.R.color.darker_gray,null));
                    platoHolder.getBT_Comprar().setBackgroundResource(R.drawable.gray_rounded_frame);
                    platoHolder.getBT_Comprar().setVisibility(View.VISIBLE);
                }

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

}
