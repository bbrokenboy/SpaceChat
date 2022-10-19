package com.example.spacechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Perfil extends AppCompatActivity {
    private Button btnSair, btnAlterarFotoPerfil;
    private ImageView imgPerfil;
    private Uri imagePath;
    private TextView email, username, number, nome;
    DatabaseReference reference;
    StorageReference storageReference;
    private BottomNavigationView menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        btnSair = findViewById(R.id.btnLogOut);
        imgPerfil = findViewById(R.id.perfil_img);
        btnAlterarFotoPerfil = findViewById(R.id.btnAlterarFotoPerfil);
        menu = findViewById(R.id.bottom_navigation2);
        email = findViewById(R.id.txtUserEmail);
        username = findViewById(R.id.txtUserName);
        number = findViewById(R.id.txtNumber);
        nome = findViewById(R.id.txtNomeUser);
        email.setText("\nEmail\n"+FirebaseAuth.getInstance().getCurrentUser().getEmail());
        reference = FirebaseDatabase.getInstance().getReference("user");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String UserName = String.valueOf(dataSnapshot.child("username").getValue());
                        String NomeUser = String.valueOf(dataSnapshot.child("nome").getValue());
                        String numero = String.valueOf(dataSnapshot.child("number").getValue());
                        number.setText("\nNúmero\n"+numero);
                        nome.setText("Nome Completo\n"+NomeUser);
                        username.setText("\nUsername\n"+UserName);
                    }

                }
            }
        });
        String imageID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());

        storageReference = FirebaseStorage.getInstance().getReference("images/"+imageID);

        try {
            File localfile = File.createTempFile("tempfile",".jpeg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    imgPerfil.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Perfil.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.mnMarketPlace):
                        startActivity(new Intent(Perfil.this, MarketPlaceActivity.class));break;
                    case (R.id.mnChats):
                        startActivity(new Intent(Perfil.this, AmigosActivity.class));break;
                }
                return true;
            }});
        btnAlterarFotoPerfil.setOnClickListener(new View.OnClickListener() {
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
            imagePath = data.getData();
            imgPerfil.setImageURI(imagePath);
            getImageInImageView();


        } else {
        }
    }

    public void getImageInImageView() {
        Bitmap  bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
        }catch (IOException e){
            e.printStackTrace();
        }
        imgPerfil.setImageBitmap(bitmap);
        AlterarFotodePerfil();
    }
    public void AlterarFotodePerfil(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Alterando...");
        progressDialog.show();
        String imageID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseStorage.getInstance().getReference("images/"+ imageID.toString()).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(Perfil.this, "Imagem alterada com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Perfil.this, "Ocorreu um erro, por favor tente novamente mais tarde!"+ task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profilePicture").setValue(url);

    }

}
