package com.example.anany.vnit_connect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anany.vnit_connect.adapters.Ans_descAdapter;
import com.example.anany.vnit_connect.adapters.Ques_descAdapter;
import com.example.anany.vnit_connect.models.Answers;
import com.example.anany.vnit_connect.models.Question;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by anany on 18/4/18.
 */

public class WriteAnswer extends AppCompatActivity{
    private static final String TAG = "WriteAnswerActivity";
    private EditText inputAns;
    private Button btnSend;
    private AppCompatTextView ques;
    private String username,email;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ProgressBar progressBar;
    private String question,ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_answer);

        //Init views
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("New Answer");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ques = (AppCompatTextView) findViewById(R.id.Question);
        btnSend = (Button) findViewById(R.id.send_ans_button);
        inputAns = (EditText) findViewById(R.id.answer);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            if(user.getDisplayName()!=null ) {
                username = user.getDisplayName().toString();
            }
            else
            {
                username = user.getEmail().toString();
            }
        }
        final int quesId = getIntent().getIntExtra("QID",0);
        question = getIntent().getStringExtra("QUESTION");

        ques.setText(question);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans = inputAns.getText().toString().trim();
                System.out.println("Hi");
                if (TextUtils.isEmpty(ans)) {
                    Toast.makeText(getApplicationContext(), "Enter answer!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                postAnswer(ans,quesId);

            }
        });

    }
    private void postAnswer(String ans,int qid)
    {
        Answers a = new Answers();
        Date date = new Date();
        a.setQid(qid);
        a.setAnswer(ans);
        a.setDownvotes(0);
        a.setUpvotes(0);
        a.setUser(username);
        a.setTimestamp(date);


        //adding new question to database
        db.collection("answers")
                .add(a)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "answer added!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WriteAnswer.this, UserDashboard.class));
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "Error adding answer!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WriteAnswer.this, UserDashboard.class));
                    }
                });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
