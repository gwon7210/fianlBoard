package com.simplify.sample.db.controller;

import com.simplify.sample.db.dto.*;
import com.simplify.sample.db.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    TestService testService;

    //???이런것도 예외처리 해야하나요 ?
    @GetMapping("/gotoContent")
    public String gotocontent()throws Exception{

        return "board/makecontent";
    }

    //list 넣기
    @GetMapping("/showList")
    public String showList(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        String user_id = (String)session.getAttribute("userid");

        try{
        List<allcontentVO> boardList = testService.getContent();
        model.addAttribute("boardList", boardList);

        return "board/boardlist";
        }catch (Exception e){
            log.error("게시판 리스트 생성중 오류 발생");
            e.printStackTrace();
            return "/forError";
        }
    }

    //content 넣기
    @PostMapping("/insertContent")
    public String insertcontent(@RequestParam String title, @RequestParam String content, @RequestParam int delpass, HttpServletRequest req, Model model){

            HttpSession session = req.getSession();
            String user_id = (String)session.getAttribute("userid");

            contentVO con = new contentVO(title,delpass,user_id,content);
             try {
                testService.insertContent(con);
                List<allcontentVO> boardList = testService.getContent();
                model.addAttribute("boardList", boardList);
                return "board/boardlist";

             }catch (ClassNotFoundException | SQLException e){

                e.printStackTrace();
                return "/forError";
            }catch (Exception e){

                e.printStackTrace();
                return "/forError";
            }
     }

    //content 수정
    //???수정 api로 바꾸는 법???
    @PutMapping("/changeContent")
    public String changeContent(@RequestParam int id, @RequestParam String title, @RequestParam String content, @RequestParam int delpass, HttpServletRequest req, Model model){

        HttpSession session = req.getSession();
        String user_id = (String)session.getAttribute("userid");

        contentVO con = new contentVO(title, delpass,content,id);

        try {
            testService.updateContent(con);
            List<allcontentVO> boardList = testService.getContent();
            model.addAttribute("boardList", boardList);
            return "board/boardlist";
        }catch (Exception e){
            e.printStackTrace();
            return "/forError";
        }
    }

    @GetMapping("/seeDetailContent")
    public String seeDetailContent(@RequestParam String contentIdB, @ModelAttribute titleVO titleVO, Model model,HttpServletRequest req){
        /*modelattribute 개념 확인하기
        System.out.println(titleVO.getid());*/

        HttpSession session = req.getSession();
        String user_id = (String)session.getAttribute("userid");

        //contentid를 통해서 content의 정보 가져오
        contentVO con = new contentVO(Integer.parseInt(contentIdB));
        contentVO resultCon = new contentVO();

        try {
            resultCon =testService.getContentDetail(con);
        }catch (Exception e){
            return "/forError";
        }


        //contentId와 sessionId를 비교하여 수정 여부 결정
        if(resultCon.getUser_id().equals(user_id)){
            model.addAttribute("contentdetail",resultCon);
            model.addAttribute("contentId",contentIdB);
            return "board/seeContentToChange";
        }

        model.addAttribute("contentdetail",resultCon);
        model.addAttribute("contentId",contentIdB);

        System.out.println("다른 사용자 입니다.");
        return "board/seeContent";
    }

    @GetMapping("/seeContent")
    public String seeContent(@RequestParam String contentId,@RequestParam String contentMaker, @ModelAttribute titleVO titleVO, Model model, HttpServletRequest req){
        HttpSession session = req.getSession();
        String user_id = (String)session.getAttribute("userid");

        contentVO con = new contentVO(Integer.parseInt(contentId));

        try {
            contentVO resultCon = testService.getContentDetail(con);
            resultCon.setId(Integer.parseInt(contentId));
            resultCon.setUser_id(contentMaker);

            //board_id를 통해서 모든 comment 가져오기
            contentVO contwo = new contentVO(Integer.parseInt(contentId));
            List<commentVO> listComment = testService.findCommentsByBoardId(contwo);

            System.out.println(resultCon);

            model.addAttribute("contentdetail",resultCon);
            model.addAttribute("comments",listComment);
            model.addAttribute("contentId",contentId);
            model.addAttribute("currentUserid",user_id);
            model.addAttribute("contentUserId",resultCon.getUser_id());

            return "board/seeContent";

        }catch (Exception e){
            e.printStackTrace();
            return "forError";
        }
     }

    @PostMapping("/inputComment")
    public String inputComment(@RequestParam String contentId, @RequestParam String commentpaper, @ModelAttribute titleVO titleVO, Model model, HttpServletRequest req){
        HttpSession session = req.getSession();
        String user_id = (String)session.getAttribute("userid");

        //id 값만 넣은 contentVO 객체 생성
        contentVO con = new contentVO(Integer.parseInt(contentId));
        contentVO resultCon = new contentVO();

        try {
            //id 값을 통해서 데이터 가져오기
            resultCon = testService.getContentDetail(con);
            resultCon.setId(Integer.parseInt(contentId));

            //전달될 comment 데이터를 db에 넣기
            commentVO commentvo = new commentVO(user_id, Integer.parseInt(contentId), commentpaper);
            testService.insertCommnet(commentvo);

            //board_id를 통해서 모든 comment 가져오기
            contentVO contwo = new contentVO(Integer.parseInt(contentId));
            List<commentVO> listComment = testService.findCommentsByBoardId(contwo);

            //템플릿으로 데이터 보내기
            model.addAttribute("contentdetail",resultCon);
            model.addAttribute("comments",listComment);
            model.addAttribute("contentId",contentId);

            return "board/seeContent";
        }catch (Exception e){
            e.printStackTrace();
            return "/forError";
        }
 /*       for(int i=0;i<listComment.size();i++){
                System.out.println(listComment.get(i).getUser_id()); }
*/
    }

    //content, title을 통해 content 검색하기
    @GetMapping("/searchContentByContentWord")
    public String searchContentByContentWord(@RequestParam String word, Model model){

        try {
            List<contentVO> conList = testService.searchContentByContentWord(word);
            model.addAttribute("boardList", conList);
            return "board/boardlist";
        }catch (Exception e){
            e.printStackTrace();
            return "/forError";
        }
    }

    //content 삭제하기
    //??? DeleteMapping 으로 받으려 시도했지만 템플렛에서 method가 post 와 get 둘 밖에 없음
    @PostMapping("deleteContent")
    public String deleteContent(@RequestParam int contentId, @RequestParam String contentUserId,@RequestParam int dlps, HttpServletRequest req, Model model){

        //세션을 통해서 접속중인 user_id 생성
        HttpSession session = req.getSession();
        String user_id = (String)session.getAttribute("userid");

        int id = contentId;

        try {
            contentVO delpassAnduserId = testService.compareWriterAndSessionUser(id);

            // ==와 equals 정확히 알기
            //contentUserId를 받는 것은 해킹의 위험이 있다. contentId를 통해서 조회 하는 방법 사용하기기
            if(delpassAnduserId.getUser_id().equals(user_id)&& delpassAnduserId.getDelpass()==dlps ){
                testService.deleteContentById(contentId);
            }else{
                //템플릿에서 1차 검증을 끝낸 후 2차 검증
                System.out.println(contentUserId);
                System.out.println("user_id :" + user_id);
                System.out.println("writerId :" + delpassAnduserId.getUser_id());
                System.out.println("다른 사용자 입니다.");
            }

            List<allcontentVO> boardList = testService.getContent();
            model.addAttribute("boardList", boardList);
            return "board/boardlist";

        }catch (Exception e){
            e.printStackTrace();
            return "/forError";
        }
    }
}
