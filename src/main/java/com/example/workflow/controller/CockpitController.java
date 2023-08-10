package com.example.workflow.controller;

import com.example.workflow.model.VacationBpmsModel;
import com.example.workflow.service.CockpitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/cockpit")
public class CockpitController {

    @Autowired
    private CockpitService cockpitService;

    @PostMapping("/start-process/{processName}")
    public ResponseEntity<Void> startProcess(@RequestBody VacationBpmsModel model, @PathVariable String processName) {
        cockpitService.startProcessInstance(model, processName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/deployed-process")
    public ResponseEntity<List<String>> deployedProcess() {
        return ResponseEntity.ok(cockpitService.deployedProcesses());
    }

    @DeleteMapping("/delete-process/{definitionKey}")
    public ResponseEntity<Void> deleteProcess(@PathVariable String definitionKey) {
        cockpitService.deleteProcess(definitionKey);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/deploy-bpmn-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> deploy(@RequestParam("bpmnFile") MultipartFile bpmnFile) {
        cockpitService.deployBpmnFile(bpmnFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-bpmn/{processInstanceId}")
    public ResponseEntity<String> getModel(@PathVariable String processInstanceId){
        return ResponseEntity.ok(cockpitService.getBpmnModel(processInstanceId));
    }

}