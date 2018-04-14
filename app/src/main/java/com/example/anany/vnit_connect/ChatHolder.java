package com.example.anany.vnit_connect;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by shivali on 4/4/18.
 */

public class ChatHolder extends RecyclerView.ViewHolder  {

    TextView leftText,rightText;

    public ChatHolder(View itemView){
        super(itemView);

        leftText = (TextView)itemView.findViewById(R.id.leftText);
        rightText = (TextView)itemView.findViewById(R.id.rightText);


    }
}
