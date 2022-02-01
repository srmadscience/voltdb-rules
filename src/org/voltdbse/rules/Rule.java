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

    /**
     * 
     * Create a new rule
     * 
     * @param seqno
     * @param ruleField
     * @param operator
     * @param thresholdDouble
     * @param thresholdString
     * @param thresholdExpression
     * @throws BadRuleException
     */
    public Rule(long seqno, String ruleField, RuleOperator operator, Double thresholdDouble, String thresholdString,
            String thresholdExpression) throws BadRuleException {
        super();
        this.seqno = seqno;
        this.ruleField = ruleField;
        this.operator = operator;

        // Inside VoltDB we use a special value of VoltType.NULL_FLOAT
        // to represent NULL
        if (thresholdDouble.equals(VoltType.NULL_FLOAT)) {
            thresholdDouble = null;
        }

        // We need one, and only one, threshold set
        short threshCount = 0;

        if (thresholdDouble != null) {
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
        } else if (threshCount > 1) {
            throw new BadRuleException("Rule must have only one threshold" + toString());
        }

        this.thresholdDouble = thresholdDouble;
        this.thresholdString = thresholdString;
        this.thresholdExpression = thresholdExpression;
    }

    /**
     * Evaluate a rule.
     * 
     * @param theNumericValues
     * @param theStringValues
     * @return toString() of rule if tripped, otherwise null
     * @throws BadRuleException, if rule refers to non-existent parameters
     */
    public String evaluate(HashMap<String, Double> theNumericValues, HashMap<String, String> theStringValues)
            throws BadRuleException {

        // We check for a numeric parameter. If not found we assume a String
        // parameter....
        Double testDouble = theNumericValues.get(ruleField);

        if (testDouble != null) {
            return evaluateNumbers(theNumericValues);
        }

        return evaluateStrings(theStringValues);

    }

    /**
     * See if this rule is tripped
     * 
     * @param theNumericValues
     * @return toString() if rule mapped
     * @throws BadRuleException
     */
    private String evaluateNumbers(HashMap<String, Double> theNumericValues) throws BadRuleException {

        Double ourValue = theNumericValues.getOrDefault(ruleField, Double.MIN_VALUE);
        Double actualThreshold = thresholdDouble;

        // If using expressions use value referenced by expression...
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

    /**
     * See if this rule is tripped
     * 
     * @param theStringValues
     * @return toString() if rule mapped
     * @throws BadRuleException
     */
    private String evaluateStrings(HashMap<String, String> theStringValues) throws BadRuleException {

        String ourValue = theStringValues.getOrDefault(ruleField, NO_VALUE_FOUND);

        // Always return false for null
        if (ourValue.equals(NO_VALUE_FOUND)) {
            return NO_VALUE_FOUND;
        }

        // If using expressions use value referenced by expression...
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

            if (ourValue.compareToIgnoreCase(actualThreshold) > 0) {
                return toString();
            }

            return null;

        case NOT_EQUALS:

            if (!ourValue.equalsIgnoreCase(actualThreshold)) {
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
