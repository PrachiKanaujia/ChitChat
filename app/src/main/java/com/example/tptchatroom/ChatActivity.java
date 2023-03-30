package com.example.tptchatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ChatActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //let's display email id of current user in action bar
      //  FirebaseAuth auth=FirebaseAuth.getInstance();
      //  String email=auth.getCurrentUser().getEmail();
       // getSupportActionBar().setTitle("Welcome, "+email);


        //bind FragmentStateAdapter to the viewpager
        ViewPager2 view=findViewById(R.id.viewpager);
        ChatFragmentAdapter adapter=new ChatFragmentAdapter(getSupportFragmentManager(),getLifecycle());
        view.setAdapter(adapter);



        //replace viewpager position on change of tabs
        TabLayout tab=findViewById(R.id.chattab);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                view.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

      //change position of tab on change of viewpager position
        view.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            tab.getTabAt(position).select();
        }
    });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser()==null)
        {
            Intent i=new Intent(this,Sign_In.class);
            startActivity(i);
        }
    }
    //let's bind option menu in this activity
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu,menu);          //resource folder me menu folder ke andr chat_menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                break;
            case R.id.menu_logout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(this, Sign_In.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

