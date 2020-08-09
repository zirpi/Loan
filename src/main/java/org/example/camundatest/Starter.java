package org.example.camundatest;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class Starter implements InitializingBean {

    @Autowired
    private RuntimeService runtimeService;

    public void afterPropertiesSet() throws Exception {
        runtimeService.startProcessInstanceByKey(
                "loanProcess",
                Variables
                        .putValue("clientId", "")
                        .putValue("amount", 0l)
                        .putValue("approved", false)
                        .putValue("goodClient", true)
        );
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }
}
