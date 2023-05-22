package com.example.workflow.model;


import lombok.Getter;
import lombok.Setter;
//import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Getter
@Setter
public class WorkflowStep {
    private String taskName;
    private String username;
    private String roleName;
    private Date inboxEntryDate;
    private Date inboxExitDate;
    private String comment;

    public void setComments(Collection<String> comments) {
        if (!CollectionUtils.isEmpty(comments)) {
            comment = comments.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining("\n"));
        }
    }
}
