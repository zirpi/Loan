# Loan

GET http://localhost:8080/engine-rest/engine/myengine/process-definition

POST http://localhost:8080/engine-rest/engine/myengine/process-definition/loanProcess:2:16/start

	{"variables":
		{"amount" : {"value" : 1321, "type": "long"},
		 "clientId" : {"value" : "gClient", "type": "string"}
		},
		"businessKey" : "myBusinessKey"
    }
