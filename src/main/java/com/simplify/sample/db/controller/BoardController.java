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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    TestService testService;

    @GetMapping("/test")
    public String test()throws Exception{
        return "board/makecontent";
    }

    //???이런것도 예외처리 해야하나요 ?
    @GetMapping("/gotoContent")
    public String gotocontent()throws Exception{
        return "board/makecontent";
    }

    //content 넣기
    @PostMapping("/insertContent")
    public String insertcontent(@RequestParam String title, @RequestParam String content, @RequestParam int delpass, HttpServletRequest req, Model model){

            HttpSession session = req.getSession();
            String user_id = (String)session.getAttribute("userid");

            contentVO con = new contentVO(title,delpass,user_id,content);
             try {
                testService.insertContent(con);
                return "redirect:/searchContentByContentWord";

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
            return "redirect:/searchContentByContentWord";
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
    }

    //content, title을 통해 content 검색하기
    @GetMapping("/searchContentByContentWord")
    public String searchContentByContentWord(@RequestParam(required = false) String word, Model model, @RequestParam(value = "num", required = false, defaultValue = "1") int num) throws Exception{


        contentVO con = new contentVO(word,word);
        int count;
        // 한 페이지에 출력할 게시물 갯수
        List<contentVO> list;

        List<contentVO> allConList;
        List<contentVO> conList = new ArrayList<contentVO>();
        word tossWord = new word(word);

        int postNum;
        // 하단 페이징 번호 ([ 게시물 총 갯수 ÷ 한 페이지에 출력할 갯수 ]의 올림)
        int pageNum;
        // 출력할 게시물
        int displayPost;
        // 한번에 표시할 페이징 번호의 갯수
        int pageNum_cnt;
        // 표시되는 페이지 번호 중 마지막 번호
        int endPageNum;
        // 표시되는 페이지 번호 중 첫번째 번호
        int startPageNum;
        // 마지막 번호 재계산
        int endPageNum_tmp;

        //검색단어가 을 때
        if(word != null) {
            //키워드가 저장된 map을 통해서 동적 쿼리 파라미터로 전달/ title, content 옵션 분리시를 위해 Map 이용
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("content", word);
            map.put("title", word);
            count = testService.findTotalCount(map);

            try {
                postNum = 10;
                pageNum = (int)Math.ceil((double)count/postNum);
                displayPost = (num - 1) * postNum;
                pageNum_cnt = 10;
                endPageNum = (int)(Math.ceil((double)num / (double)pageNum_cnt) * pageNum_cnt);
                startPageNum = endPageNum - (pageNum_cnt - 1);
                endPageNum_tmp = (int)(Math.ceil((double)count / (double)pageNum_cnt));

                if(endPageNum > endPageNum_tmp) {
                    endPageNum = endPageNum_tmp;
                }

                searchWord searchword = new searchWord(word,word,displayPost,postNum);
                list = testService.selectKeyWord(searchword);

            }catch (Exception e){
                e.printStackTrace();
                return "/forError"; }
        }else{
            count = testService.count();
            postNum = 10;
            pageNum = (int)Math.ceil((double)count/postNum);
            displayPost = (num - 1) * postNum;
            pageNum_cnt = 10;
            endPageNum = (int)(Math.ceil((double)num / (double)pageNum_cnt) * pageNum_cnt);
            startPageNum = endPageNum - (pageNum_cnt - 1);
            endPageNum_tmp = (int)(Math.ceil((double)count / (double)pageNum_cnt));

            if(endPageNum > endPageNum_tmp) {
                endPageNum = endPageNum_tmp;
            }

            HashMap<String, Integer> map = new HashMap<String, Integer>();
            map.put("displayPost", displayPost);
            map.put("postNum", postNum);

             list = testService.listPage(map);
        }

        boolean prev = startPageNum == 1 ? false : true;
        boolean next = endPageNum * pageNum_cnt >= count ? false : true;

        //검색된 값만
//        List list = null;
//        list = testService.listPage(displayPost, postNum);

        model.addAttribute("boardList", list);
        model.addAttribute("pageNum", pageNum);

        // 시작 및 끝 번호
        model.addAttribute("startPageNum", startPageNum);
        model.addAttribute("endPageNum", endPageNum);

        // 이전 및 다음
        model.addAttribute("prev", prev);
        model.addAttribute("next", next);

        model.addAttribute("tossWord", tossWord);

        return "board/boardlistForSearch";
      }


    //content 삭제하기
    //??? DeleteMapping 으로 받으려 시도했지만 템플렛에서 method가 post 와 get 둘 밖에 없음
    @DeleteMapping("deleteContent")
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

            return "redirect:/searchContentByContentWord";

        }catch (Exception e){
            e.printStackTrace();
            return "/forError";
        }

    }

}
