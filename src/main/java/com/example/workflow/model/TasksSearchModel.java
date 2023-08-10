package com.example.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TasksSearchModel {

    private String employeeName;
    private String employeeNationalNumber;
    private String employeePost;
    private PagingSorting pagingSorting;

}