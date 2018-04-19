package com.example.anany.vnit_connect.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anany.vnit_connect.R;

/**
 * Created by shivali on 4/4/18.
 */

public class ChatHolder extends RecyclerView.ViewHolder  {

    public TextView leftText, rightText;

    public ChatHolder(View itemView){
        super(itemView);
        leftText = (TextView)itemView.findViewById(R.id.leftText);
        rightText = (TextView)itemView.findViewById(R.id.rightText);

    }

    /*public TextView getLeftText() {
        return leftText;
    }

    public TextView getRightText() {
        return rightText;
    }

    public void setLeftText(TextView leftText) {
        this.leftText = leftText;
    }

    public void setRightText(TextView rightText) {
        this.rightText = rightText;
    }*/

}
