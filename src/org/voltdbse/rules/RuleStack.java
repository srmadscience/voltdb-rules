package org.voltdbse.rules;

import java.util.ArrayList;
import java.util.HashMap;

public class RuleStack {

    /**
     * true is this is an 'and' operation, or 'or'
     */
    boolean isAnd = true;

    String stackName;

    protected ArrayList<Rule> theRules = new ArrayList<Rule>();

    /**
     * Construct a new RuleStack consisting of one or more rules.
     * 
     * @param isAnd     - true for 'amd', false for 'or'
     * @param stackName
     */
    public RuleStack(boolean isAnd, String stackName) {
        super();
        this.isAnd = isAnd;
        this.stackName = stackName;
    }

    /**
     * Add a rule to our stack.
     * 
     * @param newRule
     */
    public void addRule(Rule newRule) {
        theRules.add(newRule);
    }

    /**
     * See if the current parameters trip this rule
     * 
     * @param theNumericValues HashMap<String, Double> of numeric parameters.
     * @param theStringValues  HashMap<String, String> of string parameters.
     * @return null if no rule tripped, otherwise the stack name
     * @throws BadRuleException
     */
    public String evaluate(HashMap<String, Double> theNumericValues, HashMap<String, String> theStringValues)
            throws BadRuleException {

        int matchCount = 0;

        // Iterate through rules. Stop if we fail one and it's an 'and' stack.
        for (int i = 0; i < theRules.size(); i++) {
            if (theRules.get(i).evaluate(theNumericValues, theStringValues) != null) {
                matchCount++;
            } else if (isAnd) {
                // Stop doing evaluations if doing an 'And' operation
                // and one fails
                break;
            }
        }

        // If we're an 'and' and we didn't match all the rules, trip
        if (isAnd && matchCount == theRules.size()) {
            return getStackName();
        }

        // If we're an 'or' trip if any rule matched.
        if (matchCount > 0) {
            return getStackName();
        }

        // nothing matched
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
     * Convenience method to create SQL during development.
     * 
     * @param builder
     * @param stackName
     * @param andOrOr
     */
    public void toSQL(StringBuilder builder, String ruleSetName) {
        
        int andOrOr = 0;

        if (isAnd) {
            andOrOr = 1;
        }

        for (int i = 0; i < theRules.size(); i++) {
            
            theRules.get(i).toSQL(builder, ruleSetName, stackName, andOrOr);
            
        }

       
    }
    

    /**
     * @return the stackName
     */
    public String getStackName() {
        return stackName;
    }

}
