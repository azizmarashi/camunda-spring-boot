package com.example.workflow.model;

import lombok.*;
import java.util.Map;

@Getter
@Setter
public class TaskInboxItemModel {

    private final String processInstanceId;
    private final String taskId;
    private final String taskName;
    private final VacationBpmsModel vacationBpmsModel;

    public TaskInboxItemModel(String processInstanceId, String taskId, String taskName, Map<String, Object> values) {
        this.processInstanceId = processInstanceId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.vacationBpmsModel = VacationBpmsModel.fromMap(values);
    }

    public boolean isFinalized() {
        return vacationBpmsModel.isFinalized();
    }

}