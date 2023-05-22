package com.example.workflow.model;

//import com.payeshgaran.coreUtility.repository.paging.PagingSorting;
//import com.payeshgaran.webGrid.lov.AjaxJson;
import lombok.Getter;
import lombok.Setter;

//import javax.annotation.Nonnull;

@Getter
@Setter
public class MyAssignedTasksSearchParams  implements QuestionnaireSearchParams {
    private Long insuranceFieldId;
    private String questionnaireCode;
    private String policyHolderIdentificationNo;
//    @Nonnull
    private PagingSorting pagingSorting;
}
