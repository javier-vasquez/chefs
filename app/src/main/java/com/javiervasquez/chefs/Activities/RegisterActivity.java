package com.javiervasquez.chefs.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javiervasquez.chefs.Constants.Constants;
import com.javiervasquez.chefs.Model.User;
import com.javiervasquez.chefs.R;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText ET_Full_Name;
    private EditText ET_Documento;
    private EditText ET_Email;
    private EditText ET_Password;
    private EditText ET_Repeat_Password;
    private EditText ET_Address;
    private EditText ET_Phone;
    private EditText ET_Cuenta_Ahorros;
    private EditText ET_Banco;
    private EditText ET_Escuela_Gastronomica;
    private Button BT_Aceptar;
    private RadioButton RB_User;
    private RadioButton RB_Chef;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPref = getSharedPreferences(Constants.SharedPreferencesData, Context.MODE_PRIVATE);
        editor=sharedPref.edit();


        ET_Full_Name = (EditText) findViewById(R.id.ET_Full_Name);
        ET_Documento = (EditText) findViewById(R.id.ET_Documento);
        ET_Email = (EditText) findViewById(R.id.ET_Email);
        ET_Password = (EditText) findViewById(R.id.ET_Password);
        ET_Repeat_Password = (EditText) findViewById(R.id.ET_Repeat_Password);
        ET_Address = (EditText) findViewById(R.id.ET_Address);
        ET_Phone = (EditText) findViewById(R.id.ET_Phone);
        ET_Escuela_Gastronomica = (EditText) findViewById(R.id.ET_Escuela_Gastronomica);
        ET_Cuenta_Ahorros = (EditText) findViewById(R.id.ET_Cuenta_Ahorros);
        ET_Banco = (EditText) findViewById(R.id.ET_Banco);
        BT_Aceptar = (Button) findViewById(R.id.BT_Aceptar);
        RB_User = (RadioButton) findViewById(R.id.RB_User);
        RB_Chef = (RadioButton) findViewById(R.id.RB_Chef);

        RB_User.setChecked(true);

        RB_Chef.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ET_Escuela_Gastronomica.setVisibility(View.VISIBLE);
                    ET_Cuenta_Ahorros.setVisibility(View.VISIBLE);
                    ET_Banco.setVisibility(View.VISIBLE);
                }else{
                    ET_Escuela_Gastronomica.setVisibility(View.GONE);
                    ET_Cuenta_Ahorros.setVisibility(View.GONE);
                    ET_Banco.setVisibility(View.GONE);

                }
            }
        });

        BT_Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ET_Email.getText().toString().trim().length()!=0 &&
                        ET_Password.getText().toString().trim().length()!=0 &&
                        ET_Full_Name.getText().toString().trim().length()!=0 &&
                        ET_Documento.getText().toString().trim().length()!=0 &&
                        ET_Address.getText().toString().trim().length()!=0 &&
                        ET_Phone.getText().toString().trim().length()!=0 &&
                        (ET_Repeat_Password.getText().toString().trim().equals(ET_Password.getText().toString().trim()))

                        ) {
                    mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(ET_Email.getText().toString().trim(), ET_Password.getText().toString().trim())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("FB-AUTH", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (task.isSuccessful()) {

                                    String user_id = task.getResult().getUser().getUid();
                                    String user_email = ET_Email.getText().toString().trim();
                                    String user_full_name = ET_Full_Name.getText().toString().trim();
                                    String user_document = ET_Documento.getText().toString().trim();
                                    String address = ET_Address.getText().toString().trim();
                                    String phone = ET_Phone.getText().toString().trim();


                                    String school = "";

                                    if(RB_Chef.isChecked()) {
                                        school = ET_Escuela_Gastronomica.getText().toString().trim();
                                    }




                                        User new_user = new User();
                                        new_user.setId(user_id);
                                        new_user.setDocument(user_document);
                                        new_user.setEmail(user_email);
                                        new_user.setName(user_full_name);
                                        new_user.setAddress(address);
                                        new_user.setPhone(phone);
                                        if(RB_Chef.isChecked()) {
                                            new_user.setSchool(school);
                                        }
                                        new_user.setChef(RB_Chef.isChecked());

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference users = database.getReference("Users").child(user_id);
                                        users.setValue(new_user);

                                    editor.putString("user_id",user_id);
                                    editor.commit();


                                    startActivity(new Intent(RegisterActivity.this,FacadeActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(RegisterActivity.this, R.string.fallo_el_registro_intente_de_nuevo,
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
                }else{
                    Toast.makeText(RegisterActivity.this, R.string.debes_completar_los_campos,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
