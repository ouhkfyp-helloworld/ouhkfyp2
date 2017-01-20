package com.example.helloworld.ouhkfyp;

/**
 * Created by leelaiyin on 19/1/2017.
 */
public class User {
    String create_dt = "";
    String uid ="";
    String email = "";

    public User(String email,String uid,String create_dt){
        this.email = email;
        this.uid = uid;
        this.create_dt = create_dt;

    }
    public User(){

    }
    public void setUID(String uid){
        this.uid = uid;
    }
    public String getUID(){
        return uid;
    }
}