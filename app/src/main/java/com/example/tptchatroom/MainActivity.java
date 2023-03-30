package com.example.tptchatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create a progree dialog
        ProgressDialog pr=new ProgressDialog(this);
        pr.setTitle("Please Wait here..");
        pr.setMessage("we are creating your account..");
        //get reference of all elements
        EditText txtname=findViewById(R.id.txtname);
        EditText txtemail=findViewById(R.id.txtemail);
        EditText txtpass=findViewById(R.id.txtpass);
        Button btn=findViewById(R.id.btnregister);

        //click event of button
       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //show the progree dialog at activity
               pr.show();
               FirebaseAuth auth=FirebaseAuth.getInstance();
               auth.createUserWithEmailAndPassword(txtemail.getText().toString(),txtpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                           String id=task.getResult().getUser().getUid();
                           HashMap<String,String> hashMap=new HashMap<>();
                           hashMap.put("name",txtname.getText().toString());
                           hashMap.put("email",txtemail.getText().toString());
                           hashMap.put("password",txtpass.getText().toString());
                           FirebaseDatabase.getInstance().getReference().child("user").child(id).setValue(hashMap);
                           Toast.makeText(MainActivity.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
                           Intent i=new Intent(MainActivity.this,Sign_In.class);
                           startActivity(i);
                       }
                       else
                       {
                          Toast.makeText(MainActivity.this,task.getException().getMessage()+"",Toast.LENGTH_LONG).show();
                       }
                   }
               });
           }
       });

    }
    //redirect to login onclick of create new account
    public void OpenSignIn(View v)
    {
        Intent i=new Intent(this,Sign_In.class);
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