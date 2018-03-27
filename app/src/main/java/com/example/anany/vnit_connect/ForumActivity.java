package com.example.anany.vnit_connect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ForumActivity extends AppCompatActivity {

    private static final String TAG = "ForumActivity";
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

        /*Query query = db.collection("user").whereEqualTo("email", email);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                    List<User> types = documentSnapshots.toObjects(User.class);
                    name = types.get(0).getUsername();
                }
            }
        });*/

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

}
