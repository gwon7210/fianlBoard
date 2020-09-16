package com.simplify.sample.db.controller;

import com.simplify.sample.db.dto.Test;
import com.simplify.sample.db.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/show")
    public @ResponseBody List<Test> query() throws Exception{
        return testService.getAll();
    }
}
