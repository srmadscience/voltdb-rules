package org.voltdbse.rules;

import java.util.HashMap;

import org.voltdb.VoltType;

public class Rule {

    private static final String NO_VALUE_FOUND = "No Value Found";
            
    long seqno;
    String ruleField;
    RuleOperator operator;
    Double thresholdDouble;
    String thresholdString;
    String thresholdExpression;

    public Rule(long seqno, String ruleField, RuleOperator operator, Double thresholdDouble, String thresholdString, String thresholdExpression) throws BadRuleException {
        super();
        this.seqno = seqno;
        this.ruleField = ruleField;
        this.operator = operator;
        
        if (thresholdDouble.equals(VoltType.NULL_FLOAT)) {
            thresholdDouble =  null;
        }
               
        short threshCount = 0;
        
        if (thresholdDouble != null ) {
            threshCount++;
         }
         
        if (thresholdExpression != null) {
            threshCount++;
         }
         
        if (thresholdString != null) {
            threshCount++;
         }
         
        if (threshCount == 0) {
            throw new BadRuleException("Rule must have a threshold:" + toString()); 
         }else if (threshCount > 1) {
             throw new BadRuleException("Rule must have only one threshold"+ toString()); 
          }
         
        this.thresholdDouble = thresholdDouble;
        this.thresholdString = thresholdString;
        this.thresholdExpression = thresholdExpression;
    }

    
    public  String evaluate(HashMap<String, Double> theNumericValues,HashMap<String, String> theStringValues) throws BadRuleException {
        
        
        Double testDouble = theNumericValues.get(ruleField);
 
        if (testDouble != null) {
            return evaluateNumbers(theNumericValues);
        }
        
        return evaluateStrings(theStringValues);
        
    }
    
    private String evaluateNumbers(HashMap<String, Double> theNumericValues) throws BadRuleException {

        Double ourValue = theNumericValues.getOrDefault(ruleField, Double.MIN_VALUE);
        Double actualThreshold = thresholdDouble;
        
        if (thresholdExpression != null) {
            
            actualThreshold = theNumericValues.get(thresholdExpression);
            
            if (actualThreshold == null) {
                throw new BadRuleException("Invalid Expression:'" + thresholdExpression + "'");
            }

        }

        // Always return false for null
        if (ourValue == Double.MIN_VALUE) {
            return NO_VALUE_FOUND;
        }

        switch (operator) {

        case EQUALS:

            if (ourValue.doubleValue() == actualThreshold) {
                return toString();
            }

            return null;

       case LESS_THAN:

            if (ourValue.doubleValue() < actualThreshold) {
                return toString();
            }

            return null;

        case LESS_THAN_EQUAL:

            if (ourValue.doubleValue() <= actualThreshold) {
                return toString();
            }

            return null;

        case GREATER_THAN_EQUAL:

            if (ourValue.doubleValue() >= actualThreshold) {
                return toString();
            }

            return null;
            
        case GREATER_THAN:

            if (ourValue.doubleValue() > actualThreshold) {
                return toString();
            }

            return null;
            
        case NOT_EQUALS:

            if (ourValue.doubleValue() != actualThreshold) {
                return toString();
            }

            return null;
            
      


        }

        throw new BadRuleException("Invalid Operator:'" + operator + "'");

    }

    private String evaluateStrings(HashMap<String, String> theStringValues) throws BadRuleException {

        String ourValue = theStringValues.getOrDefault(ruleField, NO_VALUE_FOUND);

        // Always return false for null
        if (ourValue.equals(NO_VALUE_FOUND)) {
            return NO_VALUE_FOUND;
        }

       String actualThreshold = new String(thresholdString);
        
        if (thresholdExpression != null) {
            
            actualThreshold = theStringValues.get(thresholdExpression);
            
            if (actualThreshold == null) {
                throw new BadRuleException("Invalid Expression:'" + thresholdExpression + "'");
            }

        }

        switch (operator) {

        case EQUALS:

            if (ourValue.equalsIgnoreCase(actualThreshold)) {
                return toString();
            }

            return null;

        case LESS_THAN:

            if (ourValue.compareToIgnoreCase(actualThreshold) < 0) {
                return toString();
            }

            return null;

        case LESS_THAN_EQUAL:
            
            if (ourValue.compareToIgnoreCase(actualThreshold) <= 0) {
                return toString();
            }

            return null;

        case GREATER_THAN_EQUAL:

            if (ourValue.compareToIgnoreCase(actualThreshold) >= 0) {
                return toString();
            }

            return null;
            
        case GREATER_THAN:

            if (ourValue.compareToIgnoreCase(actualThreshold) > 0)  {
                return toString();
            }

            return null;
            
        case NOT_EQUALS:

            if (! ourValue.equalsIgnoreCase(actualThreshold)) {
                return toString();
            }

            return null;
            
      


        }

        throw new BadRuleException("Invalid Operator:'" + operator + "'");

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Rule [seqno=");
        builder.append(seqno);
        builder.append(", ruleField=");
        builder.append(ruleField);
        builder.append(", operator=");
        builder.append(operator);
        builder.append(", thresholdNumeric=");
        builder.append(thresholdDouble);
        builder.append(", thresholdString=");
        builder.append(thresholdString);
        builder.append(", thresholdExpression=");
        builder.append(thresholdExpression);
        builder.append("]");
        return builder.toString();
    }

}
