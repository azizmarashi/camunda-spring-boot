package com.example.workflow.controller;

import com.example.workflow.model.*;
import com.example.workflow.service.impl.CartableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/cartable")
public class CartableController {

    @Autowired
    private CartableServiceImpl cartableServiceImpl;


    @GetMapping("/search-claimable-tasks")
    public List<TaskInboxItemModel>  searchClaimableTasks(TasksSearchModel params){
        return cartableServiceImpl.userSearchClaimableTasks(params);
    }

    @GetMapping("/user-assigned-tasks")
    public List<TaskInboxItemModel> userAssignedTasks(TasksSearchModel searchParams){
        return cartableServiceImpl.userAssignedTasks(searchParams);
    }

    @GetMapping("/user-completed-tasks")
    public List<TaskInboxItemModel> myCompletedTasks(TasksSearchModel searchParams){
        return cartableServiceImpl.userCompletedTasks(searchParams);
    }

    @GetMapping("/claim-task")
    public void claimTask(String taskId){
        cartableServiceImpl.claimTask(taskId);
    }

    @GetMapping("/get-workflow-steps-for")
    public List<WorkflowStepModel> getWorkflowStepsFor(String processInstanceId){
        return cartableServiceImpl.getWorkflowStepsFor(processInstanceId);
    }

}