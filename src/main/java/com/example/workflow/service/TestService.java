package com.example.workflow.service;

import com.example.workflow.model.*;
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
public class TestService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


//    @Override
    public List<QuestionnaireInboxItem> searchClaimableTasks(ClaimableTasksSearchParams params) {
        TaskQuery taskQuery = taskService.createTaskQuery()
//                .tenantIdIn(InsuranceSystemEnum.getCurrentSystemCode())
                .taskCandidateGroup(params.getRoleName());

        addAgentCriteria(taskQuery);
        addSearchParamsCriteria(taskQuery, params);

        return inboxItemsFromTaskQuery(taskQuery, params.getPagingSorting());
    }

    private void addAgentCriteria(TaskQuery taskQuery) {
        String currentAgentCode = InsuranceSecurityUtils.getUser().getAgentCode();//long
        if (UserRoleBean.isBranch()) {
            taskQuery.processVariableValueEquals("branchCode", currentAgentCode);
        }
        if (UserRoleBean.isProvince()) {
            taskQuery.processVariableValueEquals("provinceCode", currentAgentCode);
        }
        if (UserRoleBean.isGeneralAgent()) {
            String currentAgentSubCode = InsuranceSecurityUtils.getUser().getAgentCode();//long
            taskQuery.processVariableValueEquals("agentCode", currentAgentCode)
                    .processVariableValueEquals("agentSubCode", currentAgentSubCode);
        }
    }

//    @Override
    public List<QuestionnaireInboxItem> myAssignedTasks(MyAssignedTasksSearchParams searchParams) {
        TaskQuery taskQuery = taskService.createTaskQuery()
//                .tenantIdIn(InsuranceSystemEnum.getCurrentSystemCode())
                .taskAssignee(InsuranceSecurityUtils.getUser().getUsername());//string

        addSearchParamsCriteria(taskQuery, searchParams);

        return inboxItemsFromTaskQuery(taskQuery, searchParams.getPagingSorting());
    }

//    @Override
    public List<QuestionnaireInboxItem> myActiveInitiatedTasks(MyInitiatedSearchParams searchParams) {
        TaskQuery taskQuery = taskService.createTaskQuery()
//                .tenantIdIn(InsuranceSystemEnum.getCurrentSystemCode())
                .processVariableValueEquals("initiator", InsuranceSecurityUtils.getUser().getUsername())//string
                .active();
        addSearchParamsCriteria(taskQuery, searchParams);

        return inboxItemsFromTaskQuery(taskQuery, searchParams.getPagingSorting());
    }

//    @Override
    public List<QuestionnaireInboxItem> myCompletedInitiatedTasks(MyInitiatedSearchParams searchParams) {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery()
//                .tenantIdIn(InsuranceSystemEnum.getCurrentSystemCode())
                .variableValueEquals("initiator", InsuranceSecurityUtils.getUser().getUsername())//string
                .finished();

        if (StringUtils.isNotEmpty(searchParams.getQuestionnaireCode())) {
            query.processInstanceBusinessKey(searchParams.getQuestionnaireCode());
        }

        if (searchParams.getInsuranceFieldId() != null) {
            query.variableValueEquals("insuranceFieldId", searchParams.getInsuranceFieldId());
        }

        if (StringUtils.isNotEmpty(searchParams.getPolicyHolderIdentificationNo())) {
            query.variableValueEquals("policyHolderIdentificationNo", searchParams.getPolicyHolderIdentificationNo());
        }

        PagingSorting pagingSorting = searchParams.getPagingSorting();
        List<HistoricProcessInstance> instances = query.listPage(pagingSorting.getFirstResult(), pagingSorting.getMaxResult());

        return instances.stream().map(this::inboxItemFromProcessInstance).collect(Collectors.toList());
    }


    private void addSearchParamsCriteria(TaskQuery query, QuestionnaireSearchParams params) {
        if (StringUtils.isNotEmpty(params.getQuestionnaireCode())) {
            query.processInstanceBusinessKey(params.getQuestionnaireCode());
        }

        if (params.getInsuranceFieldId() != null) {
            query.processVariableValueEquals("insuranceFieldId", params.getInsuranceFieldId());
        }

        if (StringUtils.isNotEmpty(params.getPolicyHolderIdentificationNo())) {
            query.processVariableValueEquals("policyHolderIdentificationNo", params.getPolicyHolderIdentificationNo());
        }
    }

    private List<QuestionnaireInboxItem> inboxItemsFromTaskQuery(TaskQuery taskQuery, PagingSorting pagingSorting) {
        List<Task> tasks = taskQuery.listPage(pagingSorting.getFirstResult(), pagingSorting.getMaxResult());
        return tasks.stream().map(this::inboxItemFromTask).collect(Collectors.toList());
    }

    private QuestionnaireInboxItem inboxItemFromTask(Task task) {
        Map<String, Object> collect = runtimeService.createVariableInstanceQuery()
//                .tenantIdIn(InsuranceSystemEnum.getCurrentSystemCode())
                .processInstanceIdIn(task.getProcessInstanceId())
                .list().stream()
                .filter(var -> var.getName() != null)
                .filter(var -> var.getValue() != null)
                .collect(Collectors.toMap(VariableInstance::getName, VariableInstance::getValue));
        return new QuestionnaireInboxItem(task.getProcessInstanceId(), task.getId(), task.getName(), collect);
    }

    private QuestionnaireInboxItem inboxItemFromProcessInstance(HistoricProcessInstance isntance) {
        Map<String, Object> collect = historyService.createHistoricVariableInstanceQuery()
//                .tenantIdIn(InsuranceSystemEnum.getCurrentSystemCode())
                .processInstanceIdIn(isntance.getId())
                .list().stream()
                .filter(var -> var.getName() != null)
                .filter(var -> var.getValue() != null)
                .collect(Collectors.toMap(HistoricVariableInstance::getName, HistoricVariableInstance::getValue));
        return new QuestionnaireInboxItem(isntance.getId(), null, null, collect);
    }

//    @Override
    public void claimTask(String taskId) {
        taskService.claim(taskId, InsuranceSecurityUtils.getUser().getUsername());//string
    }

//    @Override
    public List<WorkflowStep> getWorkflowStepsFor(String processInstanceId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
//                .tenantIdIn(InsuranceSystemEnum.getCurrentSystemCode())
                .processInstanceId(processInstanceId)
                .taskAssigned()
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        return list.stream().map(this::workflowStepFromTask).collect(Collectors.toList());
    }

    private WorkflowStep workflowStepFromTask(HistoricTaskInstance taskInstance) {
        WorkflowStep result = new WorkflowStep();
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
