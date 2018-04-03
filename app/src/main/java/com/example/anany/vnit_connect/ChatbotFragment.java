package com.example.anany.vnit_connect;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.ContentValues.TAG;
import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import com.google.gson.JsonElement;
import java.util.Map;

public class ChatbotFragment extends Fragment implements AIListener,View.OnClickListener{

    protected View mView;
    private Context context;
    private Button listenButton;
    private TextView resultTextView;
    private AIService aiService;

    public ChatbotFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "oncreateView hello!");

        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);
        this.mView = view;
        context = getActivity();
        listenButton = (Button) view.findViewById(R.id.listenButton);
        resultTextView = (TextView) view.findViewById(R.id.resultTextView);
        listenButton.setOnClickListener(this);

        final AIConfiguration config = new AIConfiguration("b2a471c1338945f9844fdf71ba08ab81",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(context, config);
        aiService.setListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        aiService.startListening();
    }

    public void onResult(final AIResponse response) {
        Result result = response.getResult();

        final String speech = result.getFulfillment().getSpeech();
        Log.i(TAG, "Speech: " + speech);

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.
        //resultTextView.setText("Query:" + result.getResolvedQuery() +
        //        "\nAction: " + result.getAction() +
        //        "\nParameters: " + parameterString);
        resultTextView.setText(speech);

    }

    @Override
    public void onError(final AIError error) {
        resultTextView.setText(error.toString());
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
