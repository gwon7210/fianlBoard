package com.simplify.sample.db.dto;

public class commentSecondVO {
    String user_id;
    int board_id;
    String content;

    public commentSecondVO(String user_id, int board_id) {
        this.user_id = user_id;
        this.board_id = board_id;
    }

}
