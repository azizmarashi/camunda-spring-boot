package com.example.workflow.controller;

import com.example.workflow.model.*;
import com.example.workflow.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private TestService testService;


    @GetMapping("/searchClaimableTasks")
    public List<QuestionnaireInboxItem>  searchClaimableTasks(ClaimableTasksSearchParams params){
        return testService.searchClaimableTasks(params);
    }

    @GetMapping("/myAssignedTasks")
    public List<QuestionnaireInboxItem> myAssignedTasks(MyAssignedTasksSearchParams searchParams){
        return testService.myAssignedTasks(searchParams);
    }

    @GetMapping("/myActiveInitiatedTasks")
    public List<QuestionnaireInboxItem> myActiveInitiatedTasks(MyInitiatedSearchParams searchParams){
        return testService.myActiveInitiatedTasks(searchParams);
    }

    @GetMapping("/myCompletedInitiatedTasks")
    public List<QuestionnaireInboxItem> myCompletedInitiatedTasks(MyInitiatedSearchParams searchParams){
        return testService.myCompletedInitiatedTasks(searchParams);
    }

    @GetMapping("/claimTask")
    public void claimTask(String taskId){
        testService.claimTask(taskId);
    }

    @GetMapping("/getWorkflowStepsFor")
    public List<WorkflowStep> getWorkflowStepsFor(String processInstanceId){
        return testService.getWorkflowStepsFor(processInstanceId);
    }

}
