package com.example.anany.vnit_connect;

import java.util.ArrayList;

/**
 * Created by shivali on 26/3/18.
 */

public class Ques_desc {
    private String question;
    private int qid;
    private ArrayList<String> tags = new ArrayList<>();

    public Ques_desc(){}

    public Ques_desc(int qid, String question, ArrayList<String> tags){
        this.qid = qid;
        this.question = question;
        this.tags = tags;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
