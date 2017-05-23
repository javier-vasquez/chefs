package com.javiervasquez.chefs.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.javiervasquez.chefs.R;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText ET_Email;
    private EditText ET_Password;
    private Button BT_Login;
    private Button BT_Register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ET_Email = (EditText) findViewById(R.id.ET_Email);
        ET_Password = (EditText) findViewById(R.id.ET_Password);
        BT_Login = (Button) findViewById(R.id.BT_Login);
        BT_Register = (Button) findViewById(R.id.BT_Register);


        BT_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ET_Email.getText().toString().trim().length()!=0 &&
                        ET_Password.getText().toString().trim().length()!=0 ) {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(ET_Email.getText().toString().trim(), ET_Password.getText().toString().trim())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("FB_AUTH", "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w("FB_AUTH", "signInWithEmail:failed", task.getException());
                                        Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                                Toast.LENGTH_SHORT).show();
                                    }else{
                                        Log.i("USER",""+task.getResult().getUser().getUid());
                                        startActivity(new Intent(LoginActivity.this,FacadeActivity.class));
                                        finish();
                                    }

                                    // ...
                                }
                            });
                }else{
                    Toast.makeText(LoginActivity.this, R.string.debes_completar_los_campos,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        BT_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });





    }
}
