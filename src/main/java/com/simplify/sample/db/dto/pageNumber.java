package com.simplify.sample.db.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class pageNumber {
    int startNum;
    int endNum;

    public pageNumber(int startNum, int endNum) {
        this.startNum = startNum;
        this.endNum = endNum;
    }

}
