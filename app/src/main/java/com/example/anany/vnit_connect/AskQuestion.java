package com.example.anany.vnit_connect;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ai.api.http.HttpClient;

/**
 * Created by anany on 15/4/18.
 */

public class AskQuestion extends AppCompatActivity {
    private static final String TAG = "AskQuestionActivity";
    private EditText inputQues;
    private Button btnSearch, btnAsk;
    private String ques;
    private String username,email;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewQuestions;
    private Ques_descAdapter mAdapter;
    private ListenerRegistration firestoreListener;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_question);

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

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("New Question");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSearch = (Button) findViewById(R.id.search_ques_button);
        btnAsk = (Button) findViewById(R.id.ask_ques_button);
        inputQues = (EditText) findViewById(R.id.question);
        recyclerViewQuestions = (RecyclerView) findViewById(R.id.recyclerViewSearch);

        List<Question> qlist = new ArrayList<>();

        mAdapter = new Ques_descAdapter(qlist, AskQuestion.this, db);
        System.out.println("Hi");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AskQuestion.this);
        recyclerViewQuestions.setLayoutManager(mLayoutManager);
        recyclerViewQuestions.setItemAnimator(new DefaultItemAnimator());
        recyclerViewQuestions.setAdapter(mAdapter);



        btnSearch.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 ques = inputQues.getText().toString().trim();
                 if (TextUtils.isEmpty(ques)) {
                     Toast.makeText(getApplicationContext(), "Enter question!", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 System.out.println("1:"+ques);
                 progressBar.setVisibility(View.VISIBLE);
                 sendPostRequest(ques);
             }
         });

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ques = inputQues.getText().toString().trim();
                System.out.println("Hi");
                if (TextUtils.isEmpty(ques)) {
                    Toast.makeText(getApplicationContext(), "Enter question!", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendPostRequest2(ques);




            }
        });
        
    }

    private void sendPostRequest(String ques) {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(String... params) {

                String paramQues = params[0];

                System.out.println("*** doInBackground ** paramQues " + paramQues);


                try {
                    String url = "https://apiai-vnitconnect-webhook.herokuapp.com/questions";
                    URL obj = new URL(url);
                    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                    //add reuqest header
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Accept", "application/json");
                    con.setRequestProperty("Content-type", "application/json");

                    System.out.println(paramQues);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("question", paramQues);

                    System.out.print(jsonObject.toString());

                    // Send post request
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(jsonObject.toString());
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'POST' request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                        System.out.println("reading line");
                    }
                    in.close();

                    //Read JSON response and print
                    JSONObject myResponse = new JSONObject(response.toString());
                    System.out.println("result after Reading JSON Response");
                    JSONArray qArray = myResponse.getJSONArray("question_list");
                    Question arr;
                    System.out.println("qid list:");

                    List<Question> qlist = new ArrayList<>();
                    System.out.println(qArray.length());

                    for(int i = 0; i < qArray.length(); i++) {
                        JSONObject o = qArray.getJSONObject(i);
                        String user = o.getString("user");
                        System.out.println(user);
                        Question q = new Question();
                        q.setUser(user);
                        System.out.println("Hey");
                        /*String dateStr = o.getString("timestamp");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date d = sdf.parse(dateStr);
                        q.setTimestamp(d);*/
                        q.setQuestion(o.getString("question"));
                        System.out.println(q.getUser());
                        q.setQid(o.getInt("qid"));
                        System.out.println(q.getQid());
                        qlist.add(q);
                        System.out.println("Hi");
                        System.out.println(q.getQuestion());
                    }

                    System.out.println(qlist.size());

                    if(qlist.size()==0)
                    {
                        Toast.makeText(AskQuestion.this,"no matches found!",Toast.LENGTH_SHORT).show();
                    }

                    mAdapter = new Ques_descAdapter(qlist, AskQuestion.this, db);
                    System.out.println("Hi");
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AskQuestion.this);
                    System.out.println("Hi");
                    //recyclerViewQuestions.setLayoutManager(mLayoutManager);
                    System.out.println("Hi");
                    recyclerViewQuestions.setHasFixedSize(true);
                    recyclerViewQuestions.setItemAnimator(new DefaultItemAnimator());
                    System.out.println("Hi");
                    recyclerViewQuestions.setAdapter(mAdapter);
                    System.out.println("Hi");
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    //print result
                    System.out.println(response.toString());
                } catch (Exception e) {

                }
                return null;
            }

            protected void onPostExecute(String content) {
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        System.out.println("2:"+ques);
        sendPostReqAsyncTask.execute(ques);
    }

    private void sendPostRequest2(String ques) {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(String... params) {

                String paramQues = params[0];

                System.out.println("*** doInBackground ** paramQues " + paramQues);


                try {
                    String url = "https://apiai-vnitconnect-webhook.herokuapp.com/category";
                    URL obj = new URL(url);
                    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                    //add reuqest header
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Accept", "application/json");
                    con.setRequestProperty("Content-type", "application/json");

                    System.out.println(paramQues);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("question", paramQues);

                    System.out.print(jsonObject.toString());

                    // Send post request
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(jsonObject.toString());
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'POST' request to URL (category) : " + url);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                        System.out.println("reading line");
                    }
                    in.close();

                    //Read JSON response and print
                    JSONObject myResponse = new JSONObject(response.toString());
                    System.out.println("result after Reading JSON Response(category)");
                    String category = myResponse.getString("category");
                    System.out.println("category:"+category);

                    //print result
                    System.out.println(response.toString());
                    postQuestion(paramQues,category);
                } catch (Exception e) {

                }
                return null;
            }

            protected void onPostExecute(String content) {
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        System.out.println("2c:"+ques);
        sendPostReqAsyncTask.execute(ques);
    }


    private void postQuestion(String ques, String category)
    {
        Question q = new Question();
        Date date = new Date();
        List<String> tags = new ArrayList<>();
        q.setQuestion(ques);
        q.setTags(tags);
        q.setTimestamp(date);
        q.setUser(username);
        q.setCategory(category);
        q.setAbuse("false");

        //adding new question to database
        db.collection("questions")
                .add(q)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "question added!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AskQuestion.this, UserDashboard.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "Error adding question!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AskQuestion.this, UserDashboard.class));
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
