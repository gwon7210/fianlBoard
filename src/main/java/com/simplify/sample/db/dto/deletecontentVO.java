package com.simplify.sample.db.dto;

import java.sql.Timestamp;

public class deletecontentVO {
    private String title;
    private String content;
    private String user_id;
    private int delpass;
    private Timestamp date;
    private int id;

    public deletecontentVO(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
