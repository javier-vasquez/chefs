package com.javiervasquez.chefs.Holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.javiervasquez.chefs.R;

/**
 * Created by javiervasquez on 19/05/17.
 */

public class PlatoHolder extends RecyclerView.ViewHolder {


    private CardView CV_Item;
    private SimpleDraweeView IV_Foto;
    private TextView TV_Name;
    private TextView TV_Descripcion;
    private TextView TV_Ingredientes;
    private TextView TV_Fecha_y_Hora;
    private TextView TV_Cantidad;
    private TextView TV_Precio;
    private Button BT_Comprar;

    public PlatoHolder(View itemView) {
        super(itemView);

        CV_Item = (CardView) itemView.findViewById(R.id.CV_Item);
        IV_Foto = (SimpleDraweeView) itemView.findViewById(R.id.IV_Foto);
        TV_Name = (TextView) itemView.findViewById(R.id.TV_Name);
        TV_Descripcion = (TextView) itemView.findViewById(R.id.TV_Descripcion);
        TV_Ingredientes = (TextView) itemView.findViewById(R.id.TV_Ingredientes);
        TV_Fecha_y_Hora = (TextView) itemView.findViewById(R.id.TV_Fecha_y_Hora);
        TV_Cantidad = (TextView) itemView.findViewById(R.id.TV_Cantidad);
        TV_Precio = (TextView) itemView.findViewById(R.id.TV_Precio);
        BT_Comprar = (Button) itemView.findViewById(R.id.BT_Comprar);

    }


    public Button getBT_Comprar() {
        return BT_Comprar;
    }

    public void setBT_Comprar(Button BT_Comprar) {
        this.BT_Comprar = BT_Comprar;
    }

    public TextView getTV_Precio() {
        return TV_Precio;
    }

    public void setTV_Precio(TextView TV_Precio) {
        this.TV_Precio = TV_Precio;
    }

    public CardView getCV_Item() {
        return CV_Item;
    }

    public void setCV_Item(CardView CV_Item) {
        this.CV_Item = CV_Item;
    }

    public SimpleDraweeView getIV_Foto() {
        return IV_Foto;
    }

    public void setIV_Foto(SimpleDraweeView IV_Foto) {
        this.IV_Foto = IV_Foto;
    }

    public TextView getTV_Name() {
        return TV_Name;
    }

    public void setTV_Name(TextView TV_Name) {
        this.TV_Name = TV_Name;
    }

    public TextView getTV_Descripcion() {
        return TV_Descripcion;
    }

    public void setTV_Descripcion(TextView TV_Descripcion) {
        this.TV_Descripcion = TV_Descripcion;
    }

    public TextView getTV_Ingredientes() {
        return TV_Ingredientes;
    }

    public void setTV_Ingredientes(TextView TV_Ingredientes) {
        this.TV_Ingredientes = TV_Ingredientes;
    }

    public TextView getTV_Fecha_y_Hora() {
        return TV_Fecha_y_Hora;
    }

    public void setTV_Fecha_y_Hora(TextView TV_Fecha_y_Hora) {
        this.TV_Fecha_y_Hora = TV_Fecha_y_Hora;
    }

    public TextView getTV_Cantidad() {
        return TV_Cantidad;
    }

    public void setTV_Cantidad(TextView TV_Cantidad) {
        this.TV_Cantidad = TV_Cantidad;
    }
}
