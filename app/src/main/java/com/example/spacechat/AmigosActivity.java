package com.example.spacechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AmigosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private ProgressBar progressBar;
    private User user;
    private UsersAdapter usersAdapter;
    UsersAdapter.OnUserClickListener onUserClickListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    String myImageUrl;
    private BottomNavigationView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        progressBar = findViewById(R.id.progressBar);
        users = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        menu = findViewById(R.id.bottom_navigation);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.mnMarketPlace):
                        startActivity(new Intent(AmigosActivity.this, MarketPlaceActivity.class));break;
                    case (R.id.mnPerfil):
                        startActivity(new Intent(AmigosActivity.this, Perfil.class));break;
                }return true;
            }});
        onUserClickListener = new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(int position) {
                startActivity(new Intent(AmigosActivity.this,MessageActivity.class)
                        .putExtra("username_of_roommate", users.get(position).getUsername())
                        .putExtra("email_of_roommate", users.get(position).getEmail())
                        .putExtra("img_of_roommate", users.get(position).getProfilePicture())
                        .putExtra("my_img", myImageUrl)
                );
            }
        };
            getUsers();
    }
    private void getUsers(){
        users.clear();
        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user = dataSnapshot.getValue(User.class);
                    if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){}else{
                        users.add(dataSnapshot.getValue(User.class));
                     }
                usersAdapter = new UsersAdapter(users, AmigosActivity.this,onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(AmigosActivity.this));
                recyclerView.setAdapter(usersAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}