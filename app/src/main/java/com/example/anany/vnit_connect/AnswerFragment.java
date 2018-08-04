package com.example.anany.vnit_connect;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.example.anany.vnit_connect.adapters.AdminQues_descAdapter;
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



public class AnswerFragment extends Fragment {
    protected View mView;
    private Context context;

    private RecyclerView recyclerViewAnswers;
    private Ans_descAdapter mAdapter;
    private ListenerRegistration firestoreListener;

    private static final String TAG = "ForumFragment";

    private String uid, email, name;
    private String docId;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ProgressBar progressBar;

    public AnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "oncreateView hello!");

        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_answer, container, false);
        this.mView = view;
        context = getActivity();
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
//Recycler view setup
        recyclerViewAnswers = (RecyclerView)view.findViewById(R.id.recyclerViewAnswers);


        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();

        }

        progressBar.setVisibility(View.VISIBLE);
        getAllAnswers();


       firestoreListener = db.collection("answers").whereEqualTo("abuse","true")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        List<Answers> quesList = new ArrayList<>();
                        //Recycler view setup
                        recyclerViewAnswers = (RecyclerView)view.findViewById(R.id.recyclerViewAnswers);

                        for (DocumentSnapshot doc : documentSnapshots) {

                            Answers q = doc.toObject(Answers.class);
                            q.setAid(doc.getId());
                            quesList.add(q);
                        }

                        mAdapter = new Ans_descAdapter(quesList, context, db);
                        recyclerViewAnswers.setAdapter(mAdapter);
                    }
                });


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        firestoreListener.remove();
    }

    private void getAllAnswers()
    {

        db.collection("answers").whereEqualTo("abuse","true").get()
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
                        mAdapter = new Ans_descAdapter(aList, context, db);



                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);

                        recyclerViewAnswers.setLayoutManager(mLayoutManager);
                        recyclerViewAnswers.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewAnswers.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Error getting data!",Toast.LENGTH_LONG).show();
                    }
                });
        //System.out.println("ques fetched : " + qList.get(0).getQuestion());
        System.out.println("Hey");
    }

}