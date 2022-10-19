package com.example.spacechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MarketPlaceActivity extends AppCompatActivity {
    private BottomNavigationView mns;
    private Button novoProduto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);
        mns = findViewById(R.id.bottom_navigation3);
        novoProduto = findViewById(R.id.btnnovoProduto);
        mns.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.mnChats):
                        startActivity(new Intent(MarketPlaceActivity.this, AmigosActivity.class));break;
                    case (R.id.mnPerfil):
                        startActivity(new Intent(MarketPlaceActivity.this, Perfil.class));break;
                }
                return true;
            }
        });
        novoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MarketPlaceActivity.this, CadastroProduto.class));
            }
        });
    }
}