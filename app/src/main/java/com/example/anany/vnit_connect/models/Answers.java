package com.example.anany.vnit_connect.models;

/**
 * Created by shivali on 26/3/18.
 */

public class Answers {
    private String username, answer;
    private int aid, qid, upvote, downvote;

    public Answers(){}

    public Answers(int aid, int qid, String username, String answer, int upvote, int downvote){
        this.aid = aid;
        this.qid = qid;
        this.username = username;
        this.answer = answer;
        this.upvote = upvote;
        this.downvote = downvote;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
