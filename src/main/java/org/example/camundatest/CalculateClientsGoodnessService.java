package org.example.camundatest;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class CalculateClientsGoodnessService implements JavaDelegate {

    public void execute(DelegateExecution delegate) throws Exception {

        String clientId = (String) delegate.getVariable("clientId");
        Object amountObj = delegate.getVariable("amount");

        if (amountObj != null){
            //Long amount = (Long) amountObj;
        }

        delegate .setVariable("goodClient", clientId != null && clientId.startsWith("g"));

        System.out.println("Spring Bean invoked: " + delegate.getVariables().toString());
    }
}
