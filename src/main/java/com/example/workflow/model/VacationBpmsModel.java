package com.example.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ReflectionUtils;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VacationBpmsModel {

    private String employeeName;
    private String employeeNationalNumber;
    private String employeePost;
    private boolean finalized;

    public static VacationBpmsModel fromMap(Map<String, Object> map) {
        VacationBpmsModel result = new VacationBpmsModel();
        ReflectionUtils.doWithFields(VacationBpmsModel.class, field -> field.set(result, map.get(field.getName())));
        return result;
    }

    public Map<String, Object> createMap() {
        Map<String, Object> result = new HashMap<>();
        ReflectionUtils.doWithFields(VacationBpmsModel.class, field -> result.put(field.getName(), field.get(this)));
        return result;
    }

}