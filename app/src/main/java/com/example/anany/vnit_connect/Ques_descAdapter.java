package com.example.anany.vnit_connect;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shivali on 26/3/18.
 */

public class Ques_descAdapter extends RecyclerView.Adapter<Ques_descAdapter.MyViewHolder> {

    private List<Ques_desc> questionsList;

    public Ques_descAdapter(List<Ques_desc> questionsList) {
        this.questionsList = questionsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quesdesc_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Ques_desc question = questionsList.get(position);
        holder.qid.setText(Integer.toString(question.getQid()));
        holder.question.setText(question.getQuestion());
        //holder.year.setText(question.getTags());
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView qid, year, question;

        public MyViewHolder(View view) {
            super(view);
            qid = view.findViewById(R.id.qid);
            question = view.findViewById(R.id.question);
            //year = (TextView) view.findViewById(R.id.year);
        }
    }







}
