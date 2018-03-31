package com.example.anany.vnit_connect.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anany.vnit_connect.R;
import com.example.anany.vnit_connect.models.Ques_desc;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by shivali on 26/3/18.
 */

public class Ques_descAdapter extends RecyclerView.Adapter<Ques_descAdapter.MyViewHolder> {

    private List<Ques_desc> questionsList;
    private Context context;
    private FirebaseFirestore db;

    public Ques_descAdapter(List<Ques_desc> questionsList, Context context, FirebaseFirestore db) {
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
        Ques_desc question = questionsList.get(position);
        holder.qid.setText(Integer.toString(question.getQid()));
        holder.question.setText(question.getQuestion());
    }

    @Override
    public int getItemCount() {
        Log.v(Ques_descAdapter.class.getSimpleName(),""+questionsList.size());
        return questionsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView qid;
        public AppCompatTextView question;

        public MyViewHolder(View view) {
            super(view);
            qid = (AppCompatTextView) view.findViewById(R.id.textViewQid);
            question = (AppCompatTextView) view.findViewById(R.id.textViewQuestion);}
    }
}
