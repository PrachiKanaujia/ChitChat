package com.example.tptchatroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tptchatroom.adapter.UserListAdapter;
import com.example.tptchatroom.model.usermodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatFragment extends Fragment {

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_chat, container, false);
        ArrayList<usermodel> list=new ArrayList<usermodel>();
        //Let's bind a adapter in recyclerview
        RecyclerView recycle=v.findViewById(R.id.userrecycle);
        UserListAdapter adapter=new UserListAdapter(getContext(),list);
        recycle.setAdapter(adapter);
        recycle.setLayoutManager(new LinearLayoutManager(getContext()));

        //get current user email id
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String email=auth.getCurrentUser().getEmail();

        //Let's get all records from FireabseDatabse user table
        FirebaseDatabase.getInstance().getReference().child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot user:snapshot.getChildren())
                {
                    usermodel u=new usermodel();
                    u.name=user.child("name").getValue(String.class);
                    u.email=user.child("email").getValue(String.class);
                    u.uid=user.getKey();
                    u.profilepic=user.child("profilepic").getValue(String.class);
                    if (!(u.email.equals(email)))
                        list.add(u);                             //add all users of table in array list

                }
                adapter.notifyDataSetChanged();
                //Toast.makeText(getContext(),list.size(), Toast.LENGTH_SHORT).show();
                //Log.e("Data",snapshot+"");
                //Toast.makeText(getContext(),snapshot+"",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               // Toast.makeText(getContext(),"Error Occured",Toast.LENGTH_LONG).show();
            }
        });
        return v;

    }

}