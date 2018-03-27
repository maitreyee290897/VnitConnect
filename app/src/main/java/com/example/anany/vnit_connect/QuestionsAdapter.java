package com.example.anany.vnit_connect; /**
 * Created by shivali on 25/3/18.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.MyViewHolder> {

        private List<Question> questionsList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView question, qid, user;

            public MyViewHolder(View view) {
                super(view);
                question = (TextView) view.findViewById(R.id.question);
                qid = (TextView) view.findViewById(R.id.qid);
                user = (TextView) view.findViewById(R.id.user);
            }
        }


        public QuestionsAdapter(List<Question> questionsList) {
            this.questionsList = questionsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.question_list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Question movie = questionsList.get(position);
            holder.question.setText(movie.getQuestion());
            holder.qid.setText(movie.getQid());
            holder.user.setText(movie.getUser());
        }

        @Override
        public int getItemCount() {
            return questionsList.size();
        }
}
