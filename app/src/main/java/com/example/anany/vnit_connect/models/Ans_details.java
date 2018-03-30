package com.example.anany.vnit_connect.models;

/**
 * Created by shivali on 26/3/18.
 */

public class Ans_details {
    private String aid, answer;
    private int upvote, downvote;

    public Ans_details(){}

    public Ans_details(String aid, String answer, int upvote, int downvote){
        this.aid = aid;
        this.answer = answer;
        this.upvote = upvote;
        this.downvote = downvote;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }
}
