package com.simplify.sample.db.dto;

 import lombok.Getter;
 import lombok.Setter;

 import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class contentVO {

    private String title;
    private String content;
    private String user_id;
    private int delpass;
    private Timestamp writetime;
    private int id;

    public contentVO( int id, String title, int delpass, String user_id, Timestamp writetime, String content) {
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.delpass = delpass;
        this.writetime = writetime;
        this.id = id;
    }

    public contentVO(int id, String title, int delpass, String user_id, String content) {
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.delpass = delpass;
        this.id = id;
    }

    public contentVO(String title, int delpass, String user_id, String content) {
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.delpass = delpass;
     }

    public contentVO(String title, int delpass, String content , int id) {
        this.title = title;
        this.content = content;
        this.delpass = delpass;
        this.id = id;
    }

    public contentVO(String user_id, int delpass) {
        this.user_id = user_id;
        this.delpass = delpass;
    }

    public contentVO(int id) {
        this.id = id;
    }

    public contentVO() {
    }



}
