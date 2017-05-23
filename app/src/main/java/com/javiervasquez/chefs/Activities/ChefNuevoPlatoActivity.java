package com.javiervasquez.chefs.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javiervasquez.chefs.Constants.Constants;
import com.javiervasquez.chefs.Model.Plato;
import com.javiervasquez.chefs.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChefNuevoPlatoActivity extends AppCompatActivity {

    private Context context;
    private ImageView IV_Foto;
    private EditText ET_Name;
    private EditText ET_Descripcion;
    private EditText ET_Cantidad;
    private EditText ET_Ingredientes;
    private EditText ET_Fecha;
    private EditText ET_Precio;
    private EditText ET_Hora;
    private Button BT_Aceptar;
    private Cloudinary cloudinary;

    private List<Uri> images;



    private String nombre;
    private String descripcion;
    private String cantidad;
    private String ingredientes;
    private String fechayhora;
    private int precio;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_nuevo_plato);

        context  = ChefNuevoPlatoActivity.this;

        sharedPref = getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);

        Map config = new HashMap();
        config.put("cloud_name", "dgj19tc67");
        config.put("api_key", "493292745442149");
        config.put("api_secret", "mOvV1gtWzB_d3Zv2URgNMYfo6Z0");
        cloudinary = new Cloudinary(config);

//        cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        IV_Foto = (ImageView) findViewById(R.id.IV_Foto);
        ET_Name = (EditText) findViewById(R.id.ET_Name);
        ET_Descripcion = (EditText) findViewById(R.id.ET_Descripcion);
        ET_Cantidad = (EditText) findViewById(R.id.ET_Cantidad);
        ET_Ingredientes = (EditText) findViewById(R.id.ET_Ingredientes);
        ET_Fecha = (EditText) findViewById(R.id.ET_Fecha);
        ET_Precio = (EditText) findViewById(R.id.ET_Precio);
        ET_Hora = (EditText) findViewById(R.id.ET_Hora);
        BT_Aceptar = (Button) findViewById(R.id.BT_Aceptar);

        IV_Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(ChefNuevoPlatoActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(1)
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.photo_grid))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(1);
            }
        });




        BT_Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 nombre = ET_Name.getText().toString().trim();
                 descripcion = ET_Descripcion.getText().toString().trim();
                 cantidad = ET_Cantidad.getText().toString().trim();
                precio = Integer.parseInt(ET_Precio.getText().toString().trim());
                 ingredientes = ET_Ingredientes.getText().toString().trim();
                 fechayhora = ET_Fecha.getText().toString().trim()+" - "+ET_Hora.getText().toString().trim();

                UploadTask uploadTask = new UploadTask();
                uploadTask.execute();

            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //Display an error
                    return;
                }

                    images = Matisse.obtainResult(data);
                    Log.d("Matisse", "mSelected: " + images);

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(images.get(0));
                        Bitmap foto=BitmapFactory.decodeStream(inputStream);
                        IV_Foto.setImageBitmap(foto);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

        }
    }

    class UploadTask extends AsyncTask<String, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Creando plato ...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            Map config = new HashMap();

            config.put("cloud_name", "dgj19tc67");
            config.put("api_key", "493292745442149");
            config.put("api_secret", "mOvV1gtWzB_d3Zv2URgNMYfo6Z0");
            cloudinary = new Cloudinary(config);


                for (Uri entry : images) {
                    try {
                        Map tr = ObjectUtils.asMap("transformation", new Transformation().height(400).crop("scale"));
                        String url_image = (String) cloudinary.uploader().upload(getRealPathFromUri(context,entry),tr ).get("secure_url");


                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference dish = database.getReference();
                        String key = dish.push().getKey();


                        Plato new_plato = new Plato();
//                        new_plato.setId(user_id);
                        new_plato.setId(key);
                        new_plato.setName(nombre);
                        new_plato.setChef_id(sharedPref.getString("user_id",""));
                        new_plato.setDescription(descripcion);
                        new_plato.setQuantity(cantidad);
                        new_plato.setState(1);
                        new_plato.setPrice(precio);
                        new_plato.setIngredients(ingredientes);
                        new_plato.setDateAndHour(fechayhora);
                        new_plato.setUrl_img(url_image);




                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/Dish/published/" + key, new_plato);
//                        childUpdates.put("/Users/" + sharedPref.getString("user_id","") + "/dish/" + key, new_plato);

                        dish.updateChildren(childUpdates);


                        finish();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            return null;
        }

        public  String getRealPathFromUri(Context context, Uri contentUri) {
            Cursor cursor = null;
            try {
                String[] proj = { MediaStore.Images.Media.DATA };
                cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            finish();
        }
    }
}
