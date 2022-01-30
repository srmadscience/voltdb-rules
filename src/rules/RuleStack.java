package rules;

import java.util.ArrayList;
import java.util.HashMap;

public class RuleStack {

    boolean isAnd = true;
    String     stackName;
    protected ArrayList<Rule> theRules = new ArrayList<Rule>();

    public RuleStack(boolean isAnd, String     stackName) {
        super();
        this.isAnd = isAnd;
        this.stackName = stackName;
    }

    
    public void addRule(Rule newRule) {
        theRules.add(newRule);
    }
    
    public String evaluate(HashMap<String, Double> theNumericValues,HashMap<String, String> theStringValues) throws BadRuleException {

        int matchCount = 0;

        for (int i = 0; i < theRules.size(); i++) {
            if (theRules.get(i).evaluate(theNumericValues,theStringValues) != null) {
                matchCount++;
            } else  if (isAnd ) {
                // Stop doing evaluations if doing an 'And' operation
                // and one fails
                break;
            }
        }

        if (isAnd && matchCount == theRules.size()) {
            return toString();
        }

        if (matchCount > 0) {
            return toString();
        }

        return null;
    }

    @Override
    public String toString() {

        String andOrOr = "OR";

        if (isAnd) {
            andOrOr = "OR";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("RuleStack [isAnd=");
        builder.append(andOrOr);
        builder.append(", stackName='");
        builder.append(stackName);
        
        builder.append("', theRules=");
        for (int i = 0; i < theRules.size(); i++) {
            builder.append(theRules.get(i));
            builder.append(",");
        }
        
        builder.append("]");
        return builder.toString();
    }


    /**
     * @return the stackName
     */
    public String getStackName() {
        return stackName;
    }

}
