package com.simplify.sample.db.controller;

import com.simplify.sample.db.dto.memberVO;
import com.simplify.sample.db.service.TestService;
import com.simplify.sample.db.validator.MemberValidator;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.lang.reflect.Member;

//Restcontroller로 하면 화면이 글자로 나오는데 그 이유 알기
@Controller
@AllArgsConstructor
@Slf4j
 public class LoginController {

    @Autowired
    TestService testService;

    @GetMapping("/")
    public String firstview() {

        log.info("사용자가 입장했습니다. !");
        log.debug("사용자가 입장했습니다. !");
        log.warn("사용자가 입장했습니다. !");
        log.error("사용자가 입장했습니다. !");
        log.trace("사용자가 입장했습니다. !");

        return "login/logining.html";
    }

    @GetMapping("/newMember")
    public String goToMemberRegister() {
        return "login/registerMember.html";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String test(@RequestParam String id, @RequestParam String pass,Model model){
        return "board/list.html";
    }


    //Requestbody 사용하지 않아도 되는가 ?
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String register(memberVO memberVO, Model model, BindingResult bindingResult ) {

        //modelVO를 통해서 데이터가 왔는지 확인
        System.out.println(memberVO.getId());
        System.out.println(memberVO.getPass());

        //데이터 유효성 검사
        MemberValidator mValidator = new MemberValidator();
        mValidator.validate(memberVO, bindingResult);

        //오류가 있으면 다시 newMember로 리턴합니다.
         if(bindingResult.hasErrors()) {
             System.out.println("error occured !!!");
             System.out.println(bindingResult.getErrorCount());
             System.out.println(bindingResult.getAllErrors());
            return "redirect:/newMember";
        }

         //예외처리
         try {
             testService.insertMain(memberVO);
             return "login/logining.html";

           }catch (Exception e){
             log.error("회원 정보 등록 중 오류 발생");
             e.printStackTrace();
             return "/forError";
         }
      }

    @PostMapping("/checkUser")
    public String ckeckUser(@RequestParam("id") String id, @RequestParam("pass") String pass, ModelMap model, HttpServletRequest req){

        memberVO mv = new memberVO(id, pass);

        //예외처리
        try{
            //user_id, pass를 담은 memberVO 객체를 이용하여, 해당 값 count 함수로 카운트 -> 값이 존재하면 1, 존재하지 않으면 0 리턴
            int userinfo = testService.checkUserInfo(mv);

            //1일 경우 해당 계정으로 접속
            if (userinfo==1) {

                //session 생성 후 id 저장
                HttpSession session = req.getSession();
                String sessionid = id;
                session.setAttribute("userid",sessionid);
                return "redirect:/showList";

            } else {
                model.addAttribute("result", 0);
              //throw new Exception("어떤 오류 발생....");
                return "login/logining.html";
            }
        }catch (Exception e){
            log.error("회원 검색 중 오류 발생");
             e.printStackTrace(); //오류 출력(방법은 여러가지)
            return "/forError";
         }
      }
  }
