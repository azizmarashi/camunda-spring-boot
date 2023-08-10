package com.example.workflow.service.impl;

import com.example.workflow.model.VacationBpmsModel;
import com.example.workflow.service.CockpitService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ResourceDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CockpitServiceImpl implements CockpitService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void deployBpmnFile(MultipartFile multipartBpmnFile) {
        String originalFilename = multipartBpmnFile.getOriginalFilename();
        if (originalFilename == null || !originalFilename.endsWith(".bpmn"))
            throw new RuntimeException();
        try {
            repositoryService.createDeployment()
                    .addInputStream(originalFilename, multipartBpmnFile.getInputStream())
                    .name(originalFilename)
                    .deploy();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteProcess(String definitionKey) {
        repositoryService.deleteProcessDefinition(definitionKey, true);
    }

    @Override
    public List<String> deployedProcesses() {
        return repositoryService.createProcessDefinitionQuery()
                .list()
                .stream()
                .map(ResourceDefinition::getId)
                .collect(Collectors.toList());
    }

    @Override
    public String getBpmnModel(String processInstanceId) {
        String processDefinitionId = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult()
                .getProcessDefinitionId();

        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinitionId).clone();

        Collection<FlowNode> flowNodes = bpmnModelInstance.getModelElementsByType(FlowNode.class);
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        for (FlowNode flowNode : flowNodes)
            if (activeActivityIds.contains(flowNode.getId()))
                flowNode.setName(flowNode.getName() + " (Current)");

        return Bpmn.convertToString(bpmnModelInstance);
    }

    @Override
    public void startProcessInstance(VacationBpmsModel model, String processName) {
        checkNoOtherProcessExists(model, processName);
        runtimeService.createProcessInstanceByKey(processName)
                .businessKey(model.getEmployeeNationalNumber())
                .setVariables(model.createMap())
                .execute();
    }

    private void checkNoOtherProcessExists(VacationBpmsModel model, String processName){
        long count = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processName)
                .processInstanceBusinessKey(model.getEmployeeNationalNumber())
                .active().count();
        if (count > 0) throw new RuntimeException();
    }

}