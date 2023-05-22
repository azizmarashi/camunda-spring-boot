package com.example.workflow.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class QuestionnaireInboxItem {
    private final String processInstanceId;
    private final String taskId;
    private final String taskName;
    private final QuestionnaireBpmsModel questionnaire;

    public QuestionnaireInboxItem(String processInstanceId, String taskId, String taskName, Map<String, Object> values) {
        this.processInstanceId = processInstanceId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.questionnaire = QuestionnaireBpmsModel.fromMap(values);
    }

//    public String getClaimTask() {
//        return "/inbox.shtml?dispatch=perSaveTask&model.taskId=" + taskId;
//    }
//
//    public String getCurrentPath() {
//        return "/inbox.shtml";
//    }

    public boolean isFinalized() {
        return questionnaire.isFinalized();
    }
}