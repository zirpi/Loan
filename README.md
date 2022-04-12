# Loan

GET http://localhost:8080/engine-rest/engine/myengine/process-definition

POST http://localhost:8080/engine-rest/engine/myengine/process-definition/loanProcess:2:16/start

	{"variables":
		{"amount" : {"value" : 1321, "type": "long"},
		 "clientId" : {"value" : "gClient", "type": "string"}
		},
		"businessKey" : "myBusinessKey"
    }







public class HelloWorld{

     public static void main(String []args){

addRule( bils("0000-0009"), km("ALLE"), kks("12, 22"), gbm("00-19"), TRUE, "StudentenKredit" );

        System.out.println("Hello World");
     }
}

List<Rule> rules = new ArrayList();

void addRule(List<IRange> bilsRules, List<IRange> kmRules, ..., String product ) {
    rules.add( new Rule() );
}

String getProduct(int bilsVal, int kmVal, int kksVal, int gbmVal, Boolean extLimitVal) {
    for (var r : rules) {
        if (r.matches(bilsVal, kmVal, kksVal, gbmVal, extLimitVal)) {
            return r.getProduct();
        }
    }
   
    return "";
}


List<IRange> bils(String rules) {
    return parse(rules);
}


List<IRange> parse(String rules) {
    var list = new ArrayList<>();
    var parts = rules.split(",");
    for (var p : parts) {
        if (p.equals("ALLE")) {
            return List.of(new AllRange());
        }
        if (p.startsWith("NOT")) {
            list.add( createRange(p.substr(3), true) );
        } else {
            list.add( createRange(p, false) );  
        }
    }
    return list;
}

IRange createRange(String val, boolean negate) {
   
    var parts = val.split("-");
    if (parts.length > 2 || parts.lemgth < 1) {
        throw exception;
    }
    int min = parts[0];
    int max = min;
    if (parts.length == 2) {
        max = parts[1];
    }
   
    if (negate) {
        return new NotRange(min, max);
    }
   
    return new Range(min, max);
}


public interface IRange {
    boolean matches(int val);
}

public class Range implements IRange {
    int min;
    int max;
   
    public boolean matches(int val) {
        return min <= val && val <= max;
    }
}

public class NotRange implements IRange {
    int min;
    int max;
   
    public boolean matches(int val) {
        return val < min || val > max;
    }
}

public class AllRange implements IRange {
    public boolean matches(int val) {
        return true;
    }
}

public class Rule {
   
    List<IRange> bilsRanges;
    List<IRange> kmRanges;
    List<IRange> kksRanges;
    List<IRange> gbmRanges;
    Boolean extLimit;
   
    String productName;
   
    public boolean matches(int bilsVal, int kmVal, int kksVal, int gbmVal, Boolean extLimitVal) {
       
        if (extLimit != null && extLimit != extLimitVal) {
            return false;
        }
       
        return bilsRanges.any(r -> r.matches(val))
            && kmRanges.any(r -> r.matches(val));
    }
   
    public String getProduct() {
        return productName;
    }
}
