package com.simplify.sample.db.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class searchWord {
     String title;
     String content;
     int displayPost;
     int postNum;

     public searchWord(String title, String content, int displayPost, int postNum) {
          this.title = title;
          this.content = content;
          this.displayPost = displayPost;
          this.postNum = postNum;
     }
}
