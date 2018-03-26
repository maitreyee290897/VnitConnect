package com.example.anany.vnit_connect;

/**
 * Created by shivali on 25/3/18.
 */

public class Question {
    private String question, user, qid;

    public Question() {
    }

    public Question(String qid, String question, String user) {
        this.question = question;
        this.qid = qid;
        this.user = user;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }
}
