package com.example.spacechat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private android.content.Context context;
    private ArrayList<Message> messages;
    private String senderImg, receiverImg;

    public MessageAdapter(ArrayList<Message> messages, String senderImg, String receiverImg, android.content.Context context) {
        this.messages = messages;
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_holder, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.txtMessages.setText(messages.get(position).getConteudo());
        ConstraintLayout constraintLayout = holder.ccll;

        if(messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            Glide.with(context).load(senderImg).error(R.drawable.ic_account_img).placeholder(R.drawable.ic_account_img).into(holder.img_perfil);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.msg_cv, ConstraintSet.LEFT);
            constraintSet.clear(R.id.txtMessages, ConstraintSet.LEFT);
            constraintSet.connect(R.id.msg_cv, constraintSet.RIGHT, R.id.ccLayout,ConstraintSet.RIGHT,0);
            constraintSet.connect(R.id.txtMessages, constraintSet.RIGHT, R.id.msg_cv,ConstraintSet.LEFT,0);
            constraintSet.applyTo(constraintLayout);
            }else{
            Glide.with(context).load(receiverImg).error(R.drawable.ic_account_img).placeholder(R.drawable.ic_account_img).into(holder.img_perfil);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.msg_cv, ConstraintSet.RIGHT);
            constraintSet.clear(R.id.txtMessages, ConstraintSet.RIGHT);
            constraintSet.connect(R.id.msg_cv, constraintSet.LEFT, R.id.ccLayout,ConstraintSet.LEFT,0);
            constraintSet.connect(R.id.txtMessages, constraintSet.LEFT, R.id.msg_cv,ConstraintSet.RIGHT,0);
            constraintSet.applyTo(constraintLayout);

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        ConstraintLayout ccll;
        TextView txtMessages;
        ImageView img_perfil;
        public MessageHolder(@NonNull View itemView){
            super(itemView);

            ccll = itemView.findViewById(R.id.ccLayout);
            txtMessages = itemView.findViewById(R.id.txtMessages);
            img_perfil = itemView.findViewById(R.id.small_img_perfil);

        }
    }
}
