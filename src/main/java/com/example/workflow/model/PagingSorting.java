package com.example.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagingSorting implements Serializable {
    @Serial
    private static final long serialVersionUID = -2290317647422900054L;

    private int firstResult;
    private int maxResult;
    private int pageSize;
    private boolean ascending;
    private String sortProperty;
}