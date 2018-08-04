package com.example.anany.vnit_connect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import com.example.anany.vnit_connect.models.ChatHolder;
import com.example.anany.vnit_connect.models.ChatMessage;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;


public class ChatbotFragment extends Fragment implements AIListener {

    private Context context;
    private TextView resultTextView;
    private AIService aiService;
    private AIConfiguration config;
    private AIDataService aiDataService;
    private AIRequest aiRequest;
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EditText editText;
    private RelativeLayout addBtn;
    private DatabaseReference ref;
    private Boolean flagFab = true;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseRecyclerAdapter welcomeAdapter;
    private Query welcomeQuery;
    private Query query;
    private String uid, email, name;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ChatMessage welcomeMsg;


    public ChatbotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_chatbot, container, false);

        context = getActivity();
        editText = view.findViewById(R.id.editText);
        addBtn = view.findViewById(R.id.addBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
            System.out.println(uid);
            name = user.getDisplayName();

        }

        configureAI();

        //Get firebase reference
        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);


        query = ref.child(uid).limitToLast(50);
        FirebaseRecyclerOptions<ChatMessage> options =
                new FirebaseRecyclerOptions.Builder<ChatMessage>()
                        .setQuery(query, ChatMessage.class)
                        .build();


        //Button listener
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString().trim();
                if (!message.equals("")) {

                    ChatMessage chatMessage = new ChatMessage(message, "user");
                    ref.child(uid).push().setValue(chatMessage);
                    adapter.notifyDataSetChanged();

                    aiRequest.setQuery(message);
                    new AsyncTask<AIRequest,Void,AIResponse>(){

                        @Override
                        protected AIResponse doInBackground(AIRequest... aiRequests) {
                            final AIRequest request = aiRequests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(AIResponse response) {
                            if (response != null) {

                                Result result = response.getResult();
                                String reply = result.getFulfillment().getSpeech();
                                ChatMessage chatMessage = new ChatMessage(reply, "bot");
                                ref.child(uid).push().setValue(chatMessage);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }.execute(aiRequest);
                }
                else {
                    aiService.startListening();
                }

                editText.setText("");

            }
        });


        //Textbox listener
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e(TAG, "!!!On editText changed!!!");

                ImageView fab_img = view.findViewById(R.id.fab_img);
                Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.ic_send_white_24dp);
                Bitmap img1 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_mic_white_24dp);


                if (s.toString().trim().length()!=0 && flagFab){
                    ImageViewAnimatedChange(context,fab_img,img);
                    flagFab=false;
                }
                else if (s.toString().trim().length()==0){
                    ImageViewAnimatedChange(context,fab_img,img1);
                    flagFab=true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatHolder>(options) {

            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Log.e(TAG, "!!!Inside adapter onCreate!!!");

                // Create a new instance of the ViewHolder using a custom
                // layout called R.layout.msglist for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.msglist, parent, false);

                return new ChatHolder(view);
            }

            @Override
            protected void onBindViewHolder(ChatHolder viewHolder, int position, ChatMessage model) {

                // Bind the Chat object to the ChatHolder
                if (model.getMsgUser().equals("user")) {

                    viewHolder.rightText.setText(model.getMsgText());
                    viewHolder.rightText.setVisibility(View.VISIBLE);
                    viewHolder.leftText.setVisibility(View.GONE);

                }
                else {

                    viewHolder.leftText.setText(model.getMsgText());
                    viewHolder.rightText.setVisibility(View.GONE);
                    viewHolder.leftText.setVisibility(View.VISIBLE);

                }

            }
        };


        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int msgCount = adapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (msgCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.smoothScrollToPosition(positionStart);

                }
                adapter.notifyDataSetChanged();

            }
        });

        /*adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                Log.d(TAG, "onItemRangeInserted: fired -itemcount " +adapter.getItemCount());
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });*/

        recyclerView.setAdapter(adapter);

        adapter.startListening();

        welcomeMsg = new ChatMessage("Hey! How may I help you?","bot");
        ref.child(uid).push().setValue(welcomeMsg);

        adapter.notifyDataSetChanged();
        return view;

    }


    /*
    //Setup our Dialogflow agent
    */
    private void configureAI() {

        config = new AIConfiguration("b2a471c1338945f9844fdf71ba08ab81",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(context, config);
        aiService.setListener(this);

        aiDataService = new AIDataService(config);
        aiRequest = new AIRequest();

    }


    public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {

        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, R.anim.zoom_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);

    }


    @Override
    public void onResult(ai.api.model.AIResponse response) {

        Result result = response.getResult();

        String message = result.getResolvedQuery();
        ChatMessage chatMessage0 = new ChatMessage(message, "user");
        ref.child(uid).push().setValue(chatMessage0);


        String reply = result.getFulfillment().getSpeech();
        ChatMessage chatMessage = new ChatMessage(reply, "bot");
        ref.child(uid).push().setValue(chatMessage);

    }


    @Override
    public void onError(final AIError error) {
        //resultTextView.setText(error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

}
