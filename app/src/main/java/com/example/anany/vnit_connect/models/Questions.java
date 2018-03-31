package com.example.anany.vnit_connect.models;

import java.util.ArrayList;

/**
 * Created by shivali on 25/3/18.
 */

public class Questions {
    private String question, username;
    private int qid;
    private ArrayList<String> tags = new ArrayList<>();

    public Questions() {
    }

    public Questions(int qid, String question, String user, ArrayList<String> tags) {
        this.question = question;
        this.qid = qid;
        this.username = user;
        this.tags = tags;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUser() {
        return username;
    }

    public void setUser(String user) {
        this.username = user;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
