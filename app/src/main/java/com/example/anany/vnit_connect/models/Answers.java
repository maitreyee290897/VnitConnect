package com.example.anany.vnit_connect.models;

/**
 * Created by shivali on 26/3/18.
 */

public class Answers {
    private String aid, qid, username;

    public Answers(){}

    public Answers(String aid, String qid, String username){
        this.aid = aid;
        this.qid = qid;
        this.username = username;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
