package com.simplify.sample.db.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class allcontentVO {

    private int id;
    private String title;
    private int delpass;
    private String user_id;
    private Date writetime;
    private String content;

    public allcontentVO(int id, String title, String content, int delpass, String user_id) {
        this.title = title;
        this.content = content;
        this.id = id;
        this.delpass = delpass;
        this.user_id = user_id;
    }

}
