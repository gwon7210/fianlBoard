package com.simplify.sample.db.validator;


import com.simplify.sample.db.dto.memberVO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MemberValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {

        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        memberVO member = (memberVO) o;

//		(이름 유효성 체크)
//        isBlank는 공백을 true로 판단하고 Empty는 공백도 false로 판단한다.
        String mId = member.getId();
        if(mId == null || mId.trim().isEmpty()) {
            System.out.println("입력하신 id가 공백입니다.");
            errors.rejectValue("id", "공백오류입니다!");
        }

        String mPass = member.getPass();
        if(mPass==null || mPass.trim().isEmpty()){
            System.out.println("입력하신 pass가 공백입니다.");
            errors.rejectValue("pass", "공백오류입니다!");
        }
    }


}
