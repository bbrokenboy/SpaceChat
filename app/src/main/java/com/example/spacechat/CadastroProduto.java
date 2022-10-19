package com.example.spacechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class CadastroProduto extends AppCompatActivity {

    private BottomNavigationView menu;
    private ImageView imgProduto;
    private Uri imageProdutoPath;
    private TextView NomeProduto, DescricaoProduto, ValorProduto;
    private Button btnPubProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);
        imgProduto = findViewById(R.id.imgProduto);
        NomeProduto = findViewById(R.id.NomeProduto);
        DescricaoProduto = findViewById(R.id.DescricaoProduto);
        ValorProduto = findViewById(R.id.ValorProduto);
        btnPubProduto = findViewById(R.id.btnPubProduto);
        menu = findViewById(R.id.bottom_navigation2);
        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.mnMarketPlace):
                        startActivity(new Intent(CadastroProduto.this, MarketPlaceActivity.class));
                        break;
                    case (R.id.mnPerfil):
                        startActivity(new Intent(CadastroProduto.this, Perfil.class));
                        break;
                    case (R.id.mnChats):
                        startActivity(new Intent(CadastroProduto.this, AmigosActivity.class));
                        break;
                }
                return true;
            }
        });
        btnPubProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NomeProduto.toString().isEmpty() || DescricaoProduto.toString().isEmpty() || ValorProduto.toString().isEmpty() || imageProdutoPath.toString().isEmpty()) {
                    Toast.makeText(CadastroProduto.this, "Por favor preencha todos os campos", Toast.LENGTH_SHORT).show();
                }else{
                cadastrarProduto();
                AlterarFotodePerfil();
            }}
        });
        imgProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageProdutoPath = data.getData();
            imgProduto.setImageURI(imageProdutoPath);
            getImageInImageView();


        } else {
        }
    }

    public void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageProdutoPath);
        }catch (IOException e){
            e.printStackTrace();
        }
        imgProduto.setImageBitmap(bitmap);
    }


    public void AlterarFotodePerfil(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Alterando...");
        progressDialog.show();
        String idProduto = (NomeProduto.getText().toString() + String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        FirebaseStorage.getInstance().getReference("ImagensProdutos/"+ idProduto.toString()).putFile(imageProdutoPath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                alterarFotoDoUsuario(task.getResult().toString());
                            }
                        }
                    });
                    Toast.makeText(CadastroProduto.this, "Imagem cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CadastroProduto.this, "Ocorreu um erro, por favor tente novamente mais tarde!"+ task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                progressDialog.setMessage("Alterando... "+ (int) progress + "%");
            }
        });


    }
    private void alterarFotoDoUsuario(String url) {
        String idProduto = (NomeProduto.getText().toString() + String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        FirebaseDatabase.getInstance().getReference("produto/"+ idProduto + "/imgProduto").setValue(url);

    }
    private void cadastrarProduto() {
        String idProduto = (NomeProduto.getText().toString() + String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        FirebaseDatabase.getInstance().getReference("produto/" + idProduto).setValue(new Produto(idProduto.toString(), NomeProduto.getText().toString(), "", ValorProduto.getText().toString(), DescricaoProduto.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(CadastroProduto.this, MarketPlaceActivity.class));
                    Toast.makeText(CadastroProduto.this, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CadastroProduto.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }}




