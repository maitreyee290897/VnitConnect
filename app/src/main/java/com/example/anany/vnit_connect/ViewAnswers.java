package com.example.anany.vnit_connect;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anany.vnit_connect.adapters.Ans_descAdapter;
import com.example.anany.vnit_connect.models.Answers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anany on 1/4/18.
 */

public class ViewAnswers extends AppCompatActivity {
    private AppCompatActivity activity = ViewAnswers.this;
    private AppCompatTextView ques;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewAnswers;

    private List<Answers> answersList;
    private Ans_descAdapter mAdapter;
    private ListenerRegistration firestoreListener;

    private static final String TAG = "ViewAnswers";

    private FirebaseFirestore db;
    private FirebaseUser user;

    private int quesId;
    private String question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answers);

        //Init views
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Answers");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ques = (AppCompatTextView) findViewById(R.id.Question);


        //Recycler view setup
        recyclerViewAnswers = findViewById(R.id.recyclerViewAnswers);
        answersList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        quesId = getIntent().getIntExtra("QID",0);
        question = getIntent().getStringExtra("QUESTION");

        progressBar.setVisibility(View.VISIBLE);
        ques.setText(question);

        getAllAnswers(quesId);

        firestoreListener = db.collection("answers").whereEqualTo("qid",quesId).orderBy("upvotes", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        List<Answers> answersList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            Answers a = doc.toObject(Answers.class);
                            a.setAid(doc.getId());
                            answersList.add(a);
                        }

                        mAdapter = new Ans_descAdapter(answersList, getApplicationContext(), db);
                        recyclerViewAnswers.setAdapter(mAdapter);

                    }
                });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        firestoreListener.remove();
    }

    private void getAllAnswers(int quesId)
    {

        db.collection("answers").whereEqualTo("qid",quesId).orderBy("upvotes", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Answers> aList = new ArrayList<>();
                        if(queryDocumentSnapshots.isEmpty()){
                            Log.d(TAG,"onSuccess: LIST EMPTY");
                        }
                        else {

                            List<Answers> list = new ArrayList<>();
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Answers ans = doc.toObject(Answers.class);
                                ans.setAid(doc.getId());
                                list.add(ans);
                            }

                            System.out.println(list.size());
                            //System.out.println("ques fetched : " + questionsList.get(0).getQuestion());
                            aList.addAll(list);
                            System.out.println(aList.size());
                            System.out.println("ans fetched : " + aList.get(0).getAnswer());
                            Log.d(TAG,"onSuccess" + aList);
                        }
                        mAdapter = new Ans_descAdapter(aList, getApplicationContext(), db);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerViewAnswers.setLayoutManager(mLayoutManager);
                        recyclerViewAnswers.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewAnswers.setAdapter(mAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error getting data!",Toast.LENGTH_LONG).show();
                    }
                });
        //System.out.println("ques fetched : " + qList.get(0).getQuestion());
        System.out.println("Hey");
    }
}
