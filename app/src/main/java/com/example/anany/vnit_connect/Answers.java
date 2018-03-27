package com.example.anany.vnit_connect;

/**
 * Created by shivali on 26/3/18.
 */

public class Answers {
    private String username;
    private int aid, qid;

    public Answers(){}

    public Answers(int aid, int qid, String username){
        this.aid = aid;
        this.qid = qid;
        this.username = username;
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
}
