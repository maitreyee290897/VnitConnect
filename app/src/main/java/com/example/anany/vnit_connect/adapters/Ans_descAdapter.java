package com.example.anany.vnit_connect.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.anany.vnit_connect.R;
import com.example.anany.vnit_connect.models.Answers;
import com.example.anany.vnit_connect.models.Question;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by anany on 1/4/18.
 */

public class Ans_descAdapter extends RecyclerView.Adapter<Ans_descAdapter.MyViewHolder>{
    private List<Answers> answersList;
    private Context context;
    private FirebaseFirestore db;

    public Ans_descAdapter(List<Answers> answersList, Context context, FirebaseFirestore db) {
        this.answersList = answersList;
        this.context = context;
        this.db = db;
    }

    @Override
    public Ans_descAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_ans_card, parent, false);

        return new Ans_descAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Ans_descAdapter.MyViewHolder holder, final int position) {

        TextDrawable drawable;

        Answers answer = answersList.get(position);
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
                .buildRound(answer.getUser().substring(0,1), color1);

        holder.img.setImageDrawable(drawable);
        holder.ans.setText(answer.getAnswer());
        holder.date.setText(answer.getTimestamp().toString());
        holder.user.setText(answer.getUser());
        holder.uvote.setText(String.valueOf(answer.getUpvotes()));
        holder.dvote.setText(String.valueOf(answer.getDownvotes()));
    }

    @Override
    public int getItemCount() {
        Log.v(Ans_descAdapter.class.getSimpleName(),""+answersList.size());
        return answersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //public AppCompatTextView qid;
        public AppCompatTextView ans;
        public AppCompatTextView user;
        public AppCompatTextView date;
        public AppCompatTextView uvote;
        public AppCompatTextView dvote;
        public ImageView img;


        public MyViewHolder(View view) {
            super(view);
            //qid = (AppCompatTextView) view.findViewById(R.id.textViewQid);
            uvote = (AppCompatTextView) view.findViewById(R.id.upvote);
            dvote = (AppCompatTextView) view.findViewById(R.id.downvote);
            user = (AppCompatTextView) view.findViewById(R.id.textViewUser);
            date = (AppCompatTextView) view.findViewById(R.id.textViewDate);
            ans = (AppCompatTextView) view.findViewById(R.id.textViewAnswer);
            img = (ImageView) view.findViewById(R.id.viewProfileImage);

        }
    }

    private void castUpvote(String ansId, final int position)
    {
        Answers ans = answersList.get(position);
        db.collection("answers").whereEqualTo("aid",ansId);
                //.
    }
}
