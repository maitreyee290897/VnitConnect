package com.example.anany.vnit_connect.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.anany.vnit_connect.R;
import com.example.anany.vnit_connect.models.Answers;
import com.example.anany.vnit_connect.models.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ai.api.android.AIDataService.TAG;

/**
 * Created by anany on 1/4/18.
 */

public class AdminAns_descAdapter extends RecyclerView.Adapter<AdminAns_descAdapter.MyViewHolder>{
    private List<Answers> answersList;
    private Context context;
    private FirebaseFirestore db;

    public AdminAns_descAdapter(List<Answers> answersList, Context context, FirebaseFirestore db) {
        this.answersList = answersList;
        this.context = context;
        this.db = db;
    }

    @Override
    public AdminAns_descAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_anscard, parent, false);

        return new AdminAns_descAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdminAns_descAdapter.MyViewHolder holder, final int position) {

        TextDrawable drawable;
        final int itemPosition = position;
        final Answers answer = answersList.get(position);
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
        holder.date.setText(answer.getTimestamp().toString().substring(0,19));
        holder.user.setText(answer.getUser());
        holder.uvote.setText(String.valueOf(answer.getUpvotes()));
        holder.dvote.setText(String.valueOf(answer.getDownvotes()));
        holder.btn_upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                db.collection("votes").whereEqualTo("aid",answer.getAid())
                        .whereEqualTo("user",user.getDisplayName())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.isEmpty())
                                {
                                    castUpvote(position);

                                }
                                else
                                {
                                    Toast.makeText(context, "you have already voted!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "some error occured!");
                            }
                        });

            }
        });
        holder.btn_downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                db.collection("votes").whereEqualTo("aid",answer.getAid())
                        .whereEqualTo("user",user.getDisplayName())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.isEmpty())
                                {
                                    castDownvote(position);
                                    //holder.dvote.setText(String.valueOf(answer.getDownvotes()+1));
                                }
                                else
                                {
                                    Toast.makeText(context, "you have already voted!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "some error occured!");
                            }
                        });

            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAnswer(String.valueOf(answer.getAid()), itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(AdminAns_descAdapter.class.getSimpleName(),""+answersList.size());
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
        public Button btn_upvote;
        public Button btn_downvote;
        public Button del;

        public MyViewHolder(View view) {
            super(view);
            //qid = (AppCompatTextView) view.findViewById(R.id.textViewQid);
            uvote = (AppCompatTextView) view.findViewById(R.id.upvote);
            dvote = (AppCompatTextView) view.findViewById(R.id.downvote);
            user = (AppCompatTextView) view.findViewById(R.id.textViewUser);
            date = (AppCompatTextView) view.findViewById(R.id.textViewDate);
            ans = (AppCompatTextView) view.findViewById(R.id.textViewAnswer);
            img = (ImageView) view.findViewById(R.id.viewProfileImage);
            btn_upvote = (Button) view.findViewById(R.id.castUpvote);
            btn_downvote = (Button) view.findViewById(R.id.castDownvote);
            del = (Button) view.findViewById(R.id.btn_del);
        }
    }

    private void castUpvote(final int position)
    {
        Answers ans = answersList.get(position);
        final String ansId = ans.getAid();
        db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        int uvote = ans.getUpvotes();

        db.collection("answers").document(ansId)
                .update("upvotes",uvote+1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Map<String, Object> data = new HashMap<>();
                        data.put("aid", ansId);
                        data.put("user", user.getDisplayName());
                        data.put("vote",1);
                        db.collection("votes")
                                .add(data);
                        Log.d(TAG, "upvoted");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "upvote failed", e);
                    }
                });
    }

    private void castDownvote(final int position)
    {
        Answers ans = answersList.get(position);
        final String ansId = ans.getAid();
        int dvote = ans.getDownvotes();
        db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        db.collection("answers").document(ansId)
                .update("downvotes",dvote+1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Map<String, Object> data = new HashMap<>();
                        data.put("aid", ansId);
                        data.put("user", user.getDisplayName());
                        data.put("vote",-1);
                        db.collection("votes")
                                .add(data);
                        Log.d(TAG, "downvoted");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "downvote failed", e);
                    }
                });
    }

    private void deleteAnswer(String id, final int position) {
        db.collection("answers")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        answersList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, answersList.size());

                    }
                });
    }
}
