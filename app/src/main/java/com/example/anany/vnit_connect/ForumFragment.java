package com.example.anany.vnit_connect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anany.vnit_connect.adapters.Ques_descAdapter;
import com.example.anany.vnit_connect.models.Question;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ForumFragment extends Fragment {
    protected View mView;
    private Context context;

    private RecyclerView recyclerViewQuestions;
    private FloatingActionButton addQues;
    private Ques_descAdapter mAdapter;
    private ListenerRegistration firestoreListener;

    private static final String TAG = "ForumFragment";

    private String uid, email, name;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ProgressBar progressBar;

    public ForumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "oncreateView hello!");

        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_forum, container, false);
        this.mView = view;
        context = getActivity();
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        addQues = (FloatingActionButton) view.findViewById(R.id.button_addq);


        //Recycler view setup
        recyclerViewQuestions = view.findViewById(R.id.recyclerViewQuestions);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
        }

        progressBar.setVisibility(View.VISIBLE);
        getAllQuestions();

        firestoreListener = db.collection("questions")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        List<Question> quesList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            Question q = doc.toObject(Question.class);
                            q.setAutoId(doc.getId());
                            quesList.add(q);
                        }

                        mAdapter = new Ques_descAdapter(quesList, context, db);
                        recyclerViewQuestions.setAdapter(mAdapter);
                    }
                });

        addQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AskQuestion.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        firestoreListener.remove();
    }

    private void getAllQuestions() {

        db.collection("questions").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<Question> qList = new ArrayList<>();
                        if(queryDocumentSnapshots.isEmpty()){
                            Log.d(TAG,"onSuccess: LIST EMPTY");
                        }
                        else {
                            List<Question> quesList = new ArrayList<>();

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Question q = doc.toObject(Question.class);
                                q.setAutoId(doc.getId());
                                quesList.add(q);
                            }


                            System.out.println(quesList.size());
                            //System.out.println("ques fetched : " + questionsList.get(0).getQuestion());
                            qList.addAll(quesList);
                            System.out.println(qList.size());
                            System.out.println("ques fetched : " + qList.get(0).getQuestion());
                            Log.d(TAG, "onSuccess" + qList);
                        }
                        mAdapter = new Ques_descAdapter(qList, context, db);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        recyclerViewQuestions.setLayoutManager(mLayoutManager);
                        recyclerViewQuestions.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewQuestions.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error getting data!", Toast.LENGTH_LONG).show();
                    }
                });
        //System.out.println("ques fetched : " + qList.get(0).getQuestion());
        System.out.println("Hey");
    }


}
