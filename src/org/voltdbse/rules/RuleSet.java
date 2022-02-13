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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;

public class RuleSet {

    /**
     * Provide correct SQL to create rules. Order of rows is very important....
     */
    public static String GET_ALL_RULES = "SELECT RULESET_NAME, ISAND, STACK_NAME, SEQNO, RULE_FIELD, RULE_OPERATOR"
            + ", THRESHOLD_FLOAT, THRESHOLD_STRING, THRESHOLD_EXPRESSION "
            + "FROM volt_rules ORDER BY RULESET_NAME, SEQNO;";

    /**
     * Provide correct SQL to create rules. Order of rows is very important....
     */
    public static String GET_RULE_SET = "SELECT RULESET_NAME, ISAND, STACK_NAME, SEQNO, RULE_FIELD, RULE_OPERATOR"
            + ", THRESHOLD_FLOAT, THRESHOLD_STRING,THRESHOLD_EXPRESSION "
            + "FROM volt_rules WHERE RULESET_NAME = ? ORDER BY RULESET_NAME, SEQNO;";

    protected String name = null;
    protected ArrayList<RuleStack> theRuleStack = new ArrayList<>();
    protected Date expiryDate = null;

    private int lastTriggeredStackId = -1;

    /**
     * @param name       name of rule set.
     * @param ruleTable  - results of GET_SOME_RULES or GET_ALL_RULES
     * @param expiryDate - Value of VoltProcedure.getTransactionTime(). NEVER USE
     *                   System time...
     * @throws BadRuleException if there are no rules, or the rule has a bad
     *                          operator.
     */
    public RuleSet(String name, VoltTable ruleTable, Date expiryDate) throws BadRuleException {

        this.name = name;
        this.expiryDate = expiryDate;

        ruleTable.resetRowPosition();

        RuleStack tempStack = null;

        while (ruleTable.advanceRow()) {

            String ruleSetName = ruleTable.getString("RULESET_NAME");

            if (ruleSetName.equals(name)) {

                String isAnd = ruleTable.getString("ISAND");

                String stackName = ruleTable.getString("STACK_NAME");

                // Rule
                long seqno = ruleTable.getLong("SEQNO");
                String ruleField = ruleTable.getString("RULE_FIELD");
                String operatorAsString = ruleTable.getString("RULE_OPERATOR");

                RuleOperator operator = null;

                if (operatorAsString.equals("=")) {
                    operator = RuleOperator.EQUALS;
                } else if (operatorAsString.equals("!=")) {
                    operator = RuleOperator.NOT_EQUALS;
                } else if (operatorAsString.equals("<=")) {
                    operator = RuleOperator.LESS_THAN_EQUAL;
                } else if (operatorAsString.equals("<")) {
                    operator = RuleOperator.LESS_THAN;
                } else if (operatorAsString.equals(">=")) {
                    operator = RuleOperator.GREATER_THAN_EQUAL;
                } else if (operatorAsString.equals(">")) {
                    operator = RuleOperator.GREATER_THAN;
                } else {
                    throw new BadRuleException("Invalid Operator:'" + operatorAsString + "'");
                }

                double thresholdDouble = ruleTable.getDouble("THRESHOLD_FLOAT");
                String thresholdString = ruleTable.getString("THRESHOLD_STRING");
                String thresholdExpression = ruleTable.getString("THRESHOLD_EXPRESSION");

                Rule r = new Rule(seqno, ruleField, operator, thresholdDouble, thresholdString, thresholdExpression);

                if (tempStack == null) {
                    tempStack = new RuleStack(isAnd, stackName);
                } else if (!tempStack.getStackName().equalsIgnoreCase(stackName)) {
                    theRuleStack.add(tempStack);
                    tempStack = new RuleStack(isAnd, stackName);
                }

                tempStack.addRule(r);

            }

        }

        if (tempStack == null) {
            throw new BadRuleException("No rules found for '" + name + "'");
        }

        theRuleStack.add(tempStack);
    }

    /**
     * See if a current situation trips any rules.
     *
     * @param theNumericValues HashMap<String, Double> of numeric parameters.
     * @param theStringValues  HashMap<String, String> of string parameters.
     * @return null, or a String with name of the stack that triggered the rule.
     * @throws BadRuleException - if the rule refers to a parameter that doesn't
     *                          exist.
     */
    public String evaluate(HashMap<String, Double> theNumericValues, HashMap<String, String> theStringValues)
            throws BadRuleException {

        lastTriggeredStackId = -1;

        for (int i = 0; i < theRuleStack.size(); i++) {

            String result = theRuleStack.get(i).evaluate(theNumericValues, theStringValues);
            if (result != null) {
                lastTriggeredStackId = i;
                return result;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RuleSet [name=");
        builder.append(name);
        builder.append(", theRuleStack=");

        for (RuleStack element : theRuleStack) {
            builder.append(element.toString());
            builder.append(",");
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * See if RuleSet has expired
     *
     * @param logicalDate current date, as defined by Value of
     *                    VoltProcedure.getTransactionTime(). NEVER USE System
     *                    time...
     * @return true if logicalDate < date RuleSet was created with
     */
    public boolean expired(Date logicalDate) {

        if (logicalDate.compareTo(expiryDate) > 0) {
            return true;
        }

        return false;
    }

    /**
     * @return toString of last rule stack triggered. -1 means no rule triggered.
     */
    public String getLastTriggeredStackDetail() {

        if (lastTriggeredStackId > -1) {

            return (theRuleStack.get(lastTriggeredStackId).toString());
        }

        return null;

    }

    /**
     *
     * Convenience method to return this RuleSet as SQL
     *
     * @return this RuleSet as SQL
     */
    public String toSQL() {

        StringBuilder b = new StringBuilder();

        for (RuleStack element : theRuleStack) {

            element.toSQL(b, name);

        }

        return b.toString();
    }

    /**
     * Create a VoltTable for testing
     *
     * @return An empty VoltTable in the form RuleSet uses. Identical to output from
     *         GET_ALL_RULES
     */
    public static VoltTable getEmptyRuleTable() {

        VoltTable t = new VoltTable(new VoltTable.ColumnInfo("RULESET_NAME", VoltType.STRING),
                new VoltTable.ColumnInfo("ISAND", VoltType.STRING),
                new VoltTable.ColumnInfo("STACK_NAME", VoltType.STRING),
                new VoltTable.ColumnInfo("SEQNO", VoltType.BIGINT),
                new VoltTable.ColumnInfo("RULE_FIELD", VoltType.STRING),
                new VoltTable.ColumnInfo("RULE_OPERATOR", VoltType.STRING),
                new VoltTable.ColumnInfo("THRESHOLD_FLOAT", VoltType.FLOAT),
                new VoltTable.ColumnInfo("THRESHOLD_STRING", VoltType.STRING),
                new VoltTable.ColumnInfo("THRESHOLD_EXPRESSION", VoltType.STRING));

        return t;
    }

    /**
     *
     * Add a rule to a VoltTable used for testing
     *
     * @param t
     * @param ruleSetName
     * @param isAnd
     * @param stackName
     * @param seqno
     * @param ruleField
     * @param ruleOperator
     * @param thresholdFloat
     * @param thresholdString
     * @param thresholdExpression
     */
    public static void addRule(VoltTable t, String ruleSetName, String isAnd, String stackName, long seqno,
            String ruleField, String ruleOperator, Double thresholdFloat, String thresholdString,
            String thresholdExpression) {

        t.addRow(ruleSetName, isAnd, stackName, seqno, ruleField, ruleOperator, thresholdFloat, thresholdString,
                thresholdExpression);
    }

    public int getRuleCount() {

        int ruleCount = 0;

        for (RuleStack element : theRuleStack) {
            ruleCount += element.getRuleCount();
        }

        return ruleCount;

    }
}
