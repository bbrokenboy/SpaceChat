package com.example.spacechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText edtEmail, edtdtaNascimento, edtPassword, edtNome, edtNumber, edtUsername;
    private Button btnSubmit;
    private TextView txttitleSignUp, txtLoginInfo;
    private boolean isSigningUp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUsername = findViewById(R.id.edtUsername);
        edtNome = findViewById(R.id.edtNome);
        edtNumber = findViewById(R.id.edtNumber);
        edtEmail = findViewById(R.id.edtEmail);
        edtdtaNascimento = findViewById(R.id.edtdtaNascimento);
        edtPassword = findViewById(R.id.edtPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        txttitleSignUp = findViewById(R.id.txttitleSignUp);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,AmigosActivity.class));
            finish();
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isSigningUp){
                   if(edtNome.getText().toString().isEmpty() || edtNumber.getText().toString().isEmpty() || edtEmail.getText().toString().isEmpty() ||
                           edtPassword.getText().toString().isEmpty() || edtdtaNascimento.getText().toString().isEmpty()){
                       Toast.makeText(MainActivity.this,"Informações inválidas, por favor tente novamente", Toast.LENGTH_SHORT).show();
                       return;
                   }
                   handleSignUp();
               }else{
                   handleLogIn();
               }
            }
        });
        txtLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSigningUp){
                    edtUsername.setVisibility(View.GONE);
                    edtdtaNascimento.setVisibility(View.GONE);
                    edtNumber.setVisibility(View.GONE);
                    edtNome.setVisibility(View.GONE);
                    isSigningUp = false;
                    txttitleSignUp.setText("Entre no SpaceChat!");
                    btnSubmit.setText("Entrar");
                    txtLoginInfo.setText("Não tem conta? Cadastre-se aqui!");
                }else{
                    isSigningUp = true;
                    edtUsername.setVisibility(View.VISIBLE);
                    btnSubmit.setText("Cadastrar");
                    txtLoginInfo.setText("Já tem uma conta? Entre aqui!");
                    edtdtaNascimento.setVisibility(View.VISIBLE);
                    txttitleSignUp.setText("Se Cadastre no SpaceChat!");
                    edtNumber.setVisibility(View.VISIBLE);
                    edtNome.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(edtUsername.getText().toString(),edtEmail.getText().toString(), edtNome.getText().toString(),edtdtaNascimento.getText().toString(),edtNumber.getText().toString(),""));
                    startActivity(new Intent(MainActivity.this,AmigosActivity.class));
                    Toast.makeText(MainActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void handleLogIn(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this,AmigosActivity.class));
                    Toast.makeText(MainActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}