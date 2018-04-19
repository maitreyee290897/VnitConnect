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
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.anany.vnit_connect.R;
import com.example.anany.vnit_connect.ViewAnswers;
import com.example.anany.vnit_connect.WriteAnswer;
import com.example.anany.vnit_connect.models.Question;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by shivali on 26/3/18.
 */

public class Ques_descAdapter extends RecyclerView.Adapter<Ques_descAdapter.MyViewHolder> {

    private List<Question> questionsList;
    private Context context;
    private FirebaseFirestore db;

    public Ques_descAdapter(List<Question> questionsList, Context context, FirebaseFirestore db) {
        this.questionsList = questionsList;
        this.context = context;
        this.db = db;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_ques_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {

        TextDrawable drawable;

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
        holder.date.setText(question.getTimestamp().toString().substring(0,19));
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
        holder.writeAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ansIntent = new Intent(context , WriteAnswer.class);
                //This sends the email of the user to the new activity
                ansIntent.putExtra("QID",question.getQid());
                ansIntent.putExtra("QUESTION",question.getQuestion());
                context.startActivity(ansIntent);

            }
        });
        holder.reportAbuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("questions").document(question.getAutoId())
                        .update("abuse","true")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context,"You have marked this as abuse",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(Ques_descAdapter.class.getSimpleName(),""+questionsList.size());
        return questionsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //public AppCompatTextView qid;
        public AppCompatTextView question;
        public AppCompatTextView user;
        public AppCompatTextView date;
        public Button viewAnswer;
        public Button writeAnswer;
        public Button reportAbuse;
        public ImageView img;


        public MyViewHolder(View view) {
            super(view);
            //qid = (AppCompatTextView) view.findViewById(R.id.textViewQid);
            user = (AppCompatTextView) view.findViewById(R.id.textViewUser);
            date = (AppCompatTextView) view.findViewById(R.id.textViewDate);
            question = (AppCompatTextView) view.findViewById(R.id.textViewQuestion);
            img = (ImageView) view.findViewById(R.id.viewProfileImage);
            viewAnswer = (Button) view.findViewById(R.id.btn_view_answers);
            writeAnswer = (Button) view.findViewById(R.id.btn_write_answer);
            reportAbuse = (Button) view.findViewById(R.id.btn_report_abuse);
        }
    }
}
