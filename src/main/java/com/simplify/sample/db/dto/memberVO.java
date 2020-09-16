package com.simplify.sample.db.dto;

import javax.validation.constraints.NotBlank;

public class memberVO {


    private String id;


    private String pass;

    public memberVO(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
