package com.example.anany.vnit_connect.models;

import java.util.Date;

/**
 * Created by shivali on 26/3/18.
 */

public class Answers {

    private int qid,upvotes,downvotes;
    private String answer, user,aid;
    Date timestamp = new Date();
    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    private String autoId;

    public Answers(String aid, int qid, int upvotes, int downvotes, String answer, String user, Date timestamp) {
        this.aid = aid;
        this.qid = qid;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.answer = answer;
        this.user = user;
        this.timestamp = timestamp;
    }

    public Answers(){}

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
