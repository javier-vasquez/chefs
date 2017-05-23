package com.javiervasquez.chefs.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.javiervasquez.chefs.Constants.Constants;
import com.javiervasquez.chefs.Holders.PlatoHolder;
import com.javiervasquez.chefs.Model.Plato;
import com.javiervasquez.chefs.Model.Score;
import com.javiervasquez.chefs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSolicitudesActivasFragment extends Fragment {


    public UserSolicitudesActivasFragment() {
        // Required empty public constructor
    }


    private Context context;
    private View view;

    private RecyclerView.Adapter adapter;
    private RatingBar RB_Rating_1;
    private RatingBar RB_Rating_2;
    private RatingBar RB_Rating_3;
    private Button BT_Enviar;
    private Button BT_Cancelar;

    private Dialog dialog_score;


    private FragmentTransaction transaction;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_solicitudes_activas, container, false);
        context = getActivity();

        sharedPref = context.getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dish = database.getReference("Dish/bought/");
        Query queryRef = dish.orderByChild("buyer_id").equalTo(sharedPref.getString("user_id",""));

        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.RV_Platos);
        recycler.setHasFixedSize(false);
        recycler.setLayoutManager(new LinearLayoutManager(context));

        adapter = new FirebaseRecyclerAdapter<Plato, PlatoHolder>(Plato.class, R.layout.item_platos, PlatoHolder.class, queryRef) {
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


                if(plato.getState()==2) {
                    platoHolder.getBT_Comprar().setText(getString(R.string.calificar));
                    platoHolder.getBT_Comprar().setTextColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDark,null));
                    platoHolder.getBT_Comprar().setBackgroundResource(R.drawable.purple_rounded_frame);
                    platoHolder.getBT_Comprar().setVisibility(View.VISIBLE);

                    platoHolder.getBT_Comprar().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            dialog_score = new Dialog(context,R.style.CustomDialogTheme);
                            dialog_score.setContentView(R.layout.dialog_score);


                            RB_Rating_1 = (RatingBar) dialog_score.findViewById(R.id.RB_Rating_1);
                            RB_Rating_2 = (RatingBar) dialog_score.findViewById(R.id.RB_Rating_2);
                            RB_Rating_3 = (RatingBar) dialog_score.findViewById(R.id.RB_Rating_3);
                            BT_Enviar = (Button) dialog_score.findViewById(R.id.BT_Enviar);
                            BT_Cancelar = (Button) dialog_score.findViewById(R.id.BT_Cancelar);


                            BT_Enviar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Score s = new Score();
                                    s.setScore_1(RB_Rating_1.getRating());
                                    s.setScore_2(RB_Rating_2.getRating());
                                    s.setScore_3(RB_Rating_3.getRating());


                                    double score = (RB_Rating_1.getRating() + RB_Rating_1.getRating() + RB_Rating_1.getRating())/3;
                                    Plato plato_1 = plato;
                                    plato_1.setScore(score);
                                    plato_1.setSpecificScore(s);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference plato_ref = database.getReference("Dish/bought/"+plato_1.getId());
                                    plato_ref.removeValue();

                                    database.getReference().child("Dish/delivered/"+plato_1.getId()).setValue(plato_1);
                                    dialog_score.dismiss();
                                }
                            });

                            BT_Cancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog_score.dismiss();
                                }
                            });

                            dialog_score.show();

                        }
                    });
                }else if(plato.getState()==3){
                    platoHolder.getBT_Comprar().setText(getString(R.string.calificar));
                    platoHolder.getBT_Comprar().setTextColor(ResourcesCompat.getColor(getResources(),android.R.color.darker_gray,null));
                    platoHolder.getBT_Comprar().setBackgroundResource(R.drawable.gray_rounded_frame);
                    platoHolder.getBT_Comprar().setVisibility(View.VISIBLE);
                }
            }
        };
        recycler.setAdapter(adapter);


        return view;
    }

}
