package com.example.anany.vnit_connect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.example.anany.vnit_connect.models.Question;
import com.example.anany.vnit_connect.models.User;
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
        setContentView(R.layout.activity_forum);

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


    private void getAllQuestions()
    {

        db.collection("ques_desc").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Ques_desc> qList = new ArrayList<>();
                        if(queryDocumentSnapshots.isEmpty()){
                            Log.d(TAG,"onSuccess: LIST EMPTY");
                        }
                        else {
                            List<Ques_desc> list = queryDocumentSnapshots.toObjects(Ques_desc.class);
                            System.out.println(list.size());
                            //System.out.println("ques fetched : " + questionsList.get(0).getQuestion());
                            qList.addAll(list);
                            System.out.println(qList.size());
                            System.out.println("ques fetched : " + qList.get(0).getQuestion());
                            Log.d(TAG,"onSuccess" + qList);
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
                        Toast.makeText(getApplicationContext(),"Error getting data!",Toast.LENGTH_LONG).show();
                    }
                });
        //System.out.println("ques fetched : " + qList.get(0).getQuestion());
        System.out.println("Hey");
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent profilescreen=new Intent(this,ProfileActivity.class);
                profilescreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(profilescreen);
                this.finish();
                return true;

            case R.id.my_questions:
                Intent ques_screen=new Intent(this,MainActivity.class);
                ques_screen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ques_screen);
                this.finish();
                return true;

            case R.id.logout:
                Intent loginscreen=new Intent(this,MainActivity.class);
                loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginscreen);
                this.finish();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
