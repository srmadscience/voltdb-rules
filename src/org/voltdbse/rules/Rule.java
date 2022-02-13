/* This file is part of VoltDB.
 * Copyright (C) 2008-2022 VoltDB Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package org.voltdbse.rules;

import java.util.HashMap;

import org.voltdb.VoltType;

public class Rule {

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

        Double ourValue = theNumericValues.get(ruleField);
        Double actualThreshold = thresholdDouble;

        // If using expressions use value referenced by expression...
        if (thresholdExpression != null) {

            actualThreshold = theNumericValues.get(thresholdExpression);

            if (actualThreshold == null) {
                throw new BadRuleException("Invalid Expression:'" + thresholdExpression + "'");
            }

        }

        // Always throw error for null
        if (ourValue == null) {
            throw new BadRuleException("No value for rulefield:'" + ruleField + "'");
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

        String ourValue = theStringValues.get(ruleField);

        // Always throw error
        if (ourValue == null) {
            throw new BadRuleException("No value for rulefield:'" + ruleField + "'");
        }

        // If using expressions use value referenced by expression...
        String actualThreshold;

        if (thresholdString == null) {

            actualThreshold = theStringValues.get(thresholdExpression);

            if (actualThreshold == null) {
                throw new BadRuleException("Invalid Expression:'" + thresholdExpression + "'");
            }

        } else {
            actualThreshold = new String(thresholdString);
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

    /**
     * Convenience method to create SQL during development.
     *
     * @param builder
     * @param stackName
     * @param andOrOr
     */
    public void toSQL(StringBuilder builder, String ruleSetName, String stackName, String andOrOr) {

        builder.append("INSERT INTO volt_rules");
        builder.append(System.lineSeparator());
        builder.append(
                "(RULESET_NAME,STACK_NAME,SEQNO,ISAND,RULE_FIELD,RULE_OPERATOR,THRESHOLD_FLOAT,THRESHOLD_STRING,THRESHOLD_EXPRESSION)");
        builder.append(System.lineSeparator());
        builder.append("VALUES");
        builder.append(System.lineSeparator());

        builder.append("('");
        builder.append(ruleSetName);
        builder.append("','");
        builder.append(stackName);
        builder.append("',");
        builder.append(seqno);
        builder.append(",");
        builder.append(andOrOr);
        builder.append(",'");
        builder.append(ruleField);
        builder.append("','");

        switch (operator) {

        case EQUALS:

            builder.append("=");
            break;

        case LESS_THAN:

            builder.append("<");
            break;

        case LESS_THAN_EQUAL:

            builder.append("<=");
            break;

        case GREATER_THAN_EQUAL:

            builder.append(">=");
            break;

        case GREATER_THAN:

            builder.append(">");
            break;

        case NOT_EQUALS:

            builder.append("!=");
            break;

        }

        builder.append("',");

        if (thresholdDouble != null) {
            builder.append(thresholdDouble);
            builder.append("'");
            builder.append("'");
        } else {
            builder.append("null,");
        }

        if (thresholdString != null) {

            builder.append("'");
            builder.append(thresholdString);
            builder.append("',");
        } else {
            builder.append("null,");
        }

        if (thresholdExpression != null) {

            builder.append("'");
            builder.append(thresholdExpression);
            builder.append("');");
        } else {
            builder.append("null);");
        }

        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());

    }

}
