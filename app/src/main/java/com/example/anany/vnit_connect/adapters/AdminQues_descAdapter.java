package com.example.anany.vnit_connect.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.anany.vnit_connect.models.Question;
import com.google.firebase.firestore.QuerySnapshot;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.anany.vnit_connect.R;
import com.example.anany.vnit_connect.ViewAnswers;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.anany.vnit_connect.R;
import com.example.anany.vnit_connect.ViewAnswers;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by csevnitnagpur on 18-04-2018.
 */

public class AdminQues_descAdapter extends RecyclerView.Adapter<AdminQues_descAdapter.MyViewHolder> {

    private List<Question> questionsList;
    private Context context;
    private FirebaseFirestore db;

    public AdminQues_descAdapter(List<Question> questionsList, Context context, FirebaseFirestore db) {
        this.questionsList = questionsList;
        this.context = context;
        this.db = db;
    }

    @Override
    public AdminQues_descAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adminquestion_card, parent, false);

        return new AdminQues_descAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdminQues_descAdapter.MyViewHolder holder, final int position) {

        TextDrawable drawable;
        final int itemPosition = position;
        final Question question = questionsList.get(position);
        //holder.qid.setText(String.valueOf(question.getQid()));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .bold()
                .toUpperCase()
                .fontSize(30)
                .endConfig()
                .buildRound(question.getUser().substring(0,1), color1);

        holder.img.setImageDrawable(drawable);
        holder.question.setText(question.getQuestion());
        holder.abuse.setText(question.getAbuse());
        holder.date.setText(question.getTimestamp().toString());
        holder.user.setText(question.getUser());
        holder.viewAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ansIntent = new Intent(context , ViewAnswers.class);
                //This sends the email of the user to the new activity
                ansIntent.putExtra("QID",question.getQid());
                ansIntent.putExtra("QUESTION",question.getQuestion());
                context.startActivity(ansIntent);
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteQuestion(String.valueOf(question.getAutoId()), itemPosition);
            }
        });
        holder.discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardQuestion(String.valueOf(question.getAutoId()),itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(AdminQues_descAdapter.class.getSimpleName(),""+questionsList.size());
        return questionsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        //public AppCompatTextView qid;
        public AppCompatTextView question;
        public AppCompatTextView abuse;
        public AppCompatTextView user;
        public AppCompatTextView date;
        public Button viewAnswer;
        public Button del;
        public Button discard;
        public ImageView img;


        public MyViewHolder(View view) {
            super(view);
            //qid = (AppCompatTextView) view.findViewById(R.id.textViewQid);
            user = (AppCompatTextView) view.findViewById(R.id.textViewUser);
            date = (AppCompatTextView) view.findViewById(R.id.textViewDate);
            question = (AppCompatTextView) view.findViewById(R.id.textViewQuestion);
            abuse = (AppCompatTextView) view.findViewById(R.id.textViewAbuse);

            img = (ImageView) view.findViewById(R.id.viewProfileImage);
            viewAnswer = (Button) view.findViewById(R.id.btn_view_answers);
            del = (Button) view.findViewById(R.id.btn_del);
            discard = (Button) view.findViewById(R.id.btn_discard);
        }
    }


    private void deleteQuestion(String id, final int position) {
        db.collection("questions")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        questionsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, questionsList.size());

                    }
                });
    }

    private void discardQuestion(String id, final int position) {
        db.collection("questions")
                .document(id)
                .update("abuse","false")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        questionsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, questionsList.size());

                    }
                });
    }
}
