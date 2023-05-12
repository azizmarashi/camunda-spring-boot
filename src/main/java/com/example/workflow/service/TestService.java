package com.example.workflow.service;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    public void startProcess(){

//        repositoryService.createDeployment().

        runtimeService.createProcessInstanceByKey("TestCamunda")
                .processDefinitionTenantId("hahaha")// shoud deplye handly
                .businessKey("Bkey")
                .execute();
    }

}
