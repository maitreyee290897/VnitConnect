package com.example.anany.vnit_connect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForumActivity extends AppCompatActivity {

    private QuestionsAdapter mAdapter;
    private List<Question> questionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private Question ques;
    private FirebaseUser user;
    private String uid;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("vnit-connect");

        user = FirebaseAuth.getInstance().getCurrentUser();
        usersRef = mDatabase.child("users");

        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new QuestionsAdapter(questionList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();
        writeNewQuestion(ques);
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
                Intent profilescreen=new Intent(this,MainActivity.class);
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

    private void prepareMovieData() {
        Question question1 = new Question("Q1", "1", "abc");
        questionList.add(question1);

        Question question2 = new Question("Q2", "2", "xyz");
        questionList.add(question2);

        writeNewQuestion(question1);

    }

    private void writeNewQuestion(Question ques) {
        if (user != null) {
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();
        }
        Map<String, Question> questions = new HashMap<>();
        questions.put(uid, ques);
        //mDatabase.child("test-users").child(uid).setValue(ques);
        usersRef.setValue(questions);
    }
}
