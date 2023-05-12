package com.example.workflow.controller;

import com.example.workflow.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private TestService testService;


    @GetMapping("/start")
    public void start(){
        testService.startProcess();
    }

}
