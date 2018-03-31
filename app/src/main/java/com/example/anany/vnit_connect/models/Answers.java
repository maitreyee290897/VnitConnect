package com.example.anany.vnit_connect.models;

/**
 * Created by shivali on 26/3/18.
 */

public class Answers {

    private int aid,qid,upvotes,downvotes;
    private String answer, user, timestamp;

    public Answers(int aid, int qid, int upvotes, int downvotes, String answer, String user, String timestamp) {
        this.aid = aid;
        this.qid = qid;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.answer = answer;
        this.user = user;
        this.timestamp = timestamp;
    }

    public Answers(){}

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

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
