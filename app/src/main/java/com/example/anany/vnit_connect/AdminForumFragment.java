
package com.example.anany.vnit_connect;

/**
 * Created by csevnitnagpur on 17-04-2018.
 */

import android.content.Context;
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

public class AdminForumFragment extends Fragment {
    protected View mView;
    private Context context;

    private RecyclerView recyclerViewQuestions;
    private AdminQues_descAdapter mAdapter;
    private ListenerRegistration firestoreListener;

    private static final String TAG = "ForumFragment";

    private String uid, email, name;
    private String docId;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ProgressBar progressBar;

    public AdminForumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "oncreateView hello!");

        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_forumadmin, container, false);
        this.mView = view;
        context = getActivity();
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

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

        firestoreListener = db.collection("questions").whereEqualTo("abuse","true")
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

                        mAdapter = new AdminQues_descAdapter(quesList, context, db);
                        recyclerViewQuestions.setAdapter(mAdapter);
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

        db.collection("questions").whereEqualTo("abuse","true").get()
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
                            qList.addAll(quesList);
                            System.out.println(qList.size());
                            System.out.println("ques fetched : " + qList.get(0).getQuestion());
                            Log.d(TAG, "onSuccess" + qList);
                        }
                        mAdapter = new AdminQues_descAdapter(qList, context, db);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        recyclerViewQuestions.setLayoutManager(mLayoutManager);
                        recyclerViewQuestions.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewQuestions.setAdapter(mAdapter);
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

