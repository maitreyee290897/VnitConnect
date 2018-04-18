package com.example.anany.vnit_connect;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anany.vnit_connect.models.Question;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
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

        btnSearch = (Button) findViewById(R.id.search_ques_button);
        btnAsk = (Button) findViewById(R.id.ask_ques_button);
        inputQues = (EditText) findViewById(R.id.question);


        btnSearch.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //ques = inputQues.getText().toString().trim();
                 ques = "Where is VNIT?";
                 if (TextUtils.isEmpty(ques)) {
                     Toast.makeText(getApplicationContext(), "Enter question!", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 System.out.println("1:"+ques);
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

                postQuestion(ques);



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

    private void postQuestion(String ques)
    {
        Question q = new Question();
        Date date = new Date();
        List<String> tags = new ArrayList<>();
        q.setQuestion(ques);
        q.setTags(tags);
        q.setTimestamp(date);
        q.setUser(username);

        //adding new question to database
        db.collection("questions")
                .add(q)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "question added!", Toast.LENGTH_SHORT).show();

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
