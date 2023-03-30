package com.example.tptchatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_In extends AppCompatActivity {

    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //get reference of all element
        EditText txtemail=findViewById(R.id.email);
        EditText txtpass=findViewById(R.id.txtname);
        Button btnsignin=findViewById(R.id.btnsignin);

        //create onclicklistener of button signin
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(txtemail.getText().toString(),txtpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(Sign_In.this,"Welcome to TPTCHATROOM",Toast.LENGTH_LONG).show();
                             Intent i =new Intent(Sign_In.this,ChatActivity.class);
                             startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(Sign_In.this, task.getException().getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null)
        {
            Intent i=new Intent(this,ChatActivity.class);
            startActivity(i);
        }
    }
    public void openSignUp(View v)
    {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }



    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}
