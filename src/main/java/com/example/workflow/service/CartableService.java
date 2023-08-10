package com.example.workflow.service;

import com.example.workflow.model.*;
import java.util.List;

public interface CartableService {

    List<TaskInboxItemModel> userSearchClaimableTasks(TasksSearchModel searchParams);
    List<TaskInboxItemModel> userAssignedTasks(TasksSearchModel searchParams);
    List<TaskInboxItemModel> userCompletedTasks(TasksSearchModel searchParams);
    List<WorkflowStepModel> getWorkflowStepsFor(String processInstanceId);
    void claimTask(String taskId);

}