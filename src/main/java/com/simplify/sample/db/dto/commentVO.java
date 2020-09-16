package com.simplify.sample.db.dto;

import java.sql.Timestamp;

public class commentVO {

    String user_id;
    int board_id;
    String content;


    public commentVO(String user_id, int board_id, String content) {
        this.user_id = user_id;
        this.board_id = board_id;
        this.content = content;
    }

    public commentVO(String user_id, String content) {
        this.user_id = user_id;
        this.content = content;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
