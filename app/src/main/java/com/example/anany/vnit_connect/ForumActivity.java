package com.example.anany.vnit_connect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import com.example.anany.vnit_connect.adapters.Ques_descAdapter;
import com.example.anany.vnit_connect.models.Ques_desc;


public class ForumActivity extends AppCompatActivity {

    private AppCompatActivity activity = ForumActivity.this;
    private RecyclerView recyclerViewQuestions;
    private List<Ques_desc> questionsList;
    private Ques_descAdapter mAdapter;
    private ListenerRegistration firestoreListener;

    private static final String TAG = "ForumActivity";

    private Ques_desc ques;
    private String uid, email, name;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_forum);

        //Init views
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Forum");


        //Recycler view setup
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        questionsList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
        }

        getAllQuestions();

        firestoreListener = db.collection("ques_desc")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        List<Ques_desc> quesList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            Ques_desc q = doc.toObject(Ques_desc.class);
                            quesList.add(q);
                        }

                        mAdapter = new Ques_descAdapter(quesList, getApplicationContext(), db);
                        recyclerViewQuestions.setAdapter(mAdapter);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        firestoreListener.remove();
    }


    private void getAllQuestions() {

        db.collection("ques_desc").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Ques_desc> qList = new ArrayList<>();
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                        } else {
                            List<Ques_desc> list = queryDocumentSnapshots.toObjects(Ques_desc.class);
                            System.out.println(list.size());
                            //System.out.println("ques fetched : " + questionsList.get(0).getQuestion());
                            qList.addAll(list);
                            System.out.println(qList.size());
                            System.out.println("ques fetched : " + qList.get(0).getQuestion());
                            Log.d(TAG, "onSuccess" + qList);
                        }
                        mAdapter = new Ques_descAdapter(qList, getApplicationContext(), db);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerViewQuestions.setLayoutManager(mLayoutManager);
                        recyclerViewQuestions.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewQuestions.setAdapter(mAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!", Toast.LENGTH_LONG).show();
                    }
                });
        //System.out.println("ques fetched : " + qList.get(0).getQuestion());
        System.out.println("Hey");
    }
}

    /*private static final String TAG = "ForumActivity";
    private Ques_descAdapter mAdapter;
    private ArrayList<Ques_desc> questionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Question ques;
    private String uid, email, name;
    private FirebaseFirestore db;
    private FirebaseUser user;
    //private ArrayList<User> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Forum");

        //Recycler view setup
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new Ques_descAdapter(questionList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
        }

        ArrayList<String> interests = new ArrayList<String>();
        interests.add("A");
        interests.add("B");
        interests.add("D");
        final User user = new User(name, email, interests);

        final Map<String, Object> mapuser = new HashMap<>();
        mapuser.put("email", email);
        mapuser.put("interests",interests);

        DocumentReference docRef = db.collection("user").document("iupAgvor1ptsvTz5ahrp");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        User u = document.toObject(User.class);
                        name = u.getUsername();
                        user.setUsername(name);
                        mapuser.put("username", name);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        System.out.println(user.getUsername());

                        // Add a new document with a generated ID
                        db.collection("user").document("map_user1")
                                .set(mapuser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, user.getUsername());
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        getListItems();

    }

    public void getListItems() {
        db.collection("ques_desc").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        }
                        else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<Ques_desc> types = documentSnapshots.toObjects(Ques_desc.class);

                            // Add all to your list
                            questionList.addAll(types);
                            Log.d(TAG, "onSuccess: Yay! " + questionList);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure (@NonNull Exception e){
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
    };
    */