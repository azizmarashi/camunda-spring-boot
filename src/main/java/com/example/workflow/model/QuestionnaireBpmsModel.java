package com.example.workflow.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class QuestionnaireBpmsModel {
    private Long questionnaireId;
    private String questionnaireCode;

    private Long insuranceFieldId;
    private String insuranceFieldTitle;

    private String policyHolderIdentificationNo;
    private String policyHolderName;

    private String agentCode;
    private Long agentSubCode;
    private Long agentTypeId;
    private String branchCode;
    private String provinceCode;

    private long repaymentAmount;
    private boolean multiCurrency;
    private boolean finalized;
//    private boolean multiInsurer;

    public static QuestionnaireBpmsModel fromMap(Map<String, Object> map) {
        QuestionnaireBpmsModel result = new QuestionnaireBpmsModel();
        ReflectionUtils.doWithFields(QuestionnaireBpmsModel.class, field -> field.set(result, map.get(field.getName())));
        return result;
    }

    public Map<String, Object> createMap() {
        Map<String, Object> result = new HashMap<>();
        ReflectionUtils.doWithFields(QuestionnaireBpmsModel.class, field -> result.put(field.getName(), field.get(this)));
        return result;
    }

    public String getAgentTypeTitle() {
        Map<Long, String> AGENT_TYPE_TITLES = new HashMap<>();
        AGENT_TYPE_TITLES.put(1L, "AgentType.headOffice");
        AGENT_TYPE_TITLES.put(2L, "AgentType.province");
        AGENT_TYPE_TITLES.put(3L, "AgentType.branch");
        AGENT_TYPE_TITLES.put(4L, "AgentType.realAgent");
        AGENT_TYPE_TITLES.put(5L, "AgentType.corpAgent");
        AGENT_TYPE_TITLES.put(6L, "AgentType.realBroker");
        AGENT_TYPE_TITLES.put(7L, "AgentType.corpBroker");
        return AGENT_TYPE_TITLES.get(agentTypeId);
    }

}
