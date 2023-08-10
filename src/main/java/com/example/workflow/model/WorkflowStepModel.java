package com.example.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowStepModel {
    private String taskName;
    private String username;
    private Date inboxEntryDate;
    private Date inboxExitDate;
    private String comment;

    public void setComments(Collection<String> comments) {
        if (!CollectionUtils.isEmpty(comments)) {
            comment = comments.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining("\n"));
        }
    }
}
