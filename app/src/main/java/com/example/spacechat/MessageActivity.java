package com.example.spacechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class MessageActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private EditText edtText;
    private TextView txtUsernameChat;
    private ProgressBar progressBar;
    private ImageView img_toolbar, imgSendMessage;
    private ArrayList<Message> messages;
    private MessageAdapter messageAdapter;
    String userNameOfTheRoomate, emailOfRoomate, chatRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        userNameOfTheRoomate = getIntent().getStringExtra("username_of_roommate");
        emailOfRoomate = getIntent().getStringExtra("email_of_roommate");
        imgSendMessage = findViewById(R.id.imgSendMessage);
        img_toolbar = findViewById(R.id.img_toolbar);
        recyclerView = findViewById(R.id.recyclerMessages);
        edtText = findViewById(R.id.edtText);
        txtUsernameChat = findViewById(R.id.txtUsernameChat);
        progressBar = findViewById(R.id.progressMessages);
        txtUsernameChat.setText(userNameOfTheRoomate);
        messages = new ArrayList<>();


        imgSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).push().setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),emailOfRoomate,edtText.getText().toString()));

                edtText.setText("");
            }
        });


        messageAdapter = new MessageAdapter(messages,getIntent().getStringExtra("my_img"), getIntent().getStringExtra("img_of_roommate"), MessageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("img_of_roommate")).placeholder(R.drawable.ic_account_img).error(R.drawable.ic_account_img).into(img_toolbar);


        setUpChatRoom();
    }

    private void setUpChatRoom(){
        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername = snapshot.getValue(User.class).getUsername();
                if(userNameOfTheRoomate.compareTo(myUsername) > 0){
                    chatRoomId = myUsername + userNameOfTheRoomate;
                }else if (userNameOfTheRoomate.compareTo(myUsername)==0){
                    chatRoomId = userNameOfTheRoomate + myUsername;
                }else{
                    chatRoomId = userNameOfTheRoomate + myUsername;
                }
                attachMessageListener(chatRoomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void attachMessageListener(String chatRoomId){
        FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue(Message.class));
                }

                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}