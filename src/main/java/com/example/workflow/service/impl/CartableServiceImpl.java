package com.example.workflow.service.impl;

import com.example.workflow.model.*;
import com.example.workflow.service.CartableService;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartableServiceImpl implements CartableService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    //todo correct currentUser and set current username user logged in with authenticate service
    //@Autowired
    private String currentUser;

    @Override
    public List<TaskInboxItemModel> userSearchClaimableTasks(TasksSearchModel params) {
        TaskQuery taskQuery = taskService
                .createTaskQuery()
                .taskCandidateGroup(params.getEmployeePost());

        addSearchParamsCriteria(taskQuery, params);

        return inboxItemsFromTaskQuery(taskQuery, params.getPagingSorting());
    }

    @Override
    public List<TaskInboxItemModel> userAssignedTasks(TasksSearchModel searchParams) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee(currentUser);

        addSearchParamsCriteria(taskQuery, searchParams);

        return inboxItemsFromTaskQuery(taskQuery, searchParams.getPagingSorting());
    }

    @Override
    public List<TaskInboxItemModel> userCompletedTasks(TasksSearchModel searchParams) {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery()
                .variableValueEquals("employeeName", currentUser)
                .finished();

        PagingSorting pagingSorting = searchParams.getPagingSorting();
        List<HistoricProcessInstance> instances = query.listPage(pagingSorting.getFirstResult(), pagingSorting.getMaxResult());

        return instances.stream().map(this::inboxItemFromProcessInstance).collect(Collectors.toList());
    }


    private void addSearchParamsCriteria(TaskQuery query, TasksSearchModel params) {

        if (params.getEmployeeNationalNumber() != null) {
            query.processVariableValueEquals("employeeNationalNumber", params.getEmployeeNationalNumber());
        }

        if (StringUtils.isNotEmpty(params.getEmployeePost())) {
            query.processVariableValueEquals("employeePost", params.getEmployeePost());
        }
    }

    private List<TaskInboxItemModel> inboxItemsFromTaskQuery(TaskQuery taskQuery, PagingSorting pagingSorting) {
        List<Task> tasks = taskQuery.listPage(pagingSorting.getFirstResult(), pagingSorting.getMaxResult());
        return tasks.stream().map(this::inboxItemFromTask).collect(Collectors.toList());
    }

    private TaskInboxItemModel inboxItemFromTask(Task task) {
        Map<String, Object> collect = runtimeService.createVariableInstanceQuery()
                .processInstanceIdIn(task.getProcessInstanceId())
                .list().stream()
                .filter(var -> var.getName() != null)
                .filter(var -> var.getValue() != null)
                .collect(Collectors.toMap(VariableInstance::getName, VariableInstance::getValue));
        return new TaskInboxItemModel(task.getProcessInstanceId(), task.getId(), task.getName(), collect);
    }

    private TaskInboxItemModel inboxItemFromProcessInstance(HistoricProcessInstance isntance) {
        Map<String, Object> collect = historyService.createHistoricVariableInstanceQuery()
                .processInstanceIdIn(isntance.getId())
                .list().stream()
                .filter(var -> var.getName() != null)
                .filter(var -> var.getValue() != null)
                .collect(Collectors.toMap(HistoricVariableInstance::getName, HistoricVariableInstance::getValue));
        return new TaskInboxItemModel(isntance.getId(), null, null, collect);
    }

    @Override
    public void claimTask(String taskId) {
        taskService.claim(taskId, currentUser);
    }

    @Override
    public List<WorkflowStepModel> getWorkflowStepsFor(String processInstanceId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskAssigned()
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        return list.stream().map(this::workflowStepFromTask).collect(Collectors.toList());
    }

    private WorkflowStepModel workflowStepFromTask(HistoricTaskInstance taskInstance) {
        WorkflowStepModel result = new WorkflowStepModel();
        result.setTaskName(taskInstance.getName());
        result.setUsername(taskInstance.getAssignee());
        result.setInboxEntryDate(taskInstance.getStartTime());
        result.setInboxExitDate(taskInstance.getEndTime());

        List<Comment> taskComments = taskService.getTaskComments(taskInstance.getId());
        List<String> comments = taskComments.stream().map(Comment::getFullMessage).collect(Collectors.toList());
        result.setComments(comments);
        return result;
    }

}
