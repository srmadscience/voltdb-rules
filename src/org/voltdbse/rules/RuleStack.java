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
import java.util.HashMap;

public class RuleStack {

    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String[] RULE_COMBINATION_OPERATORS = { AND, OR };

    String isAnd;

    String stackName;

    protected ArrayList<Rule> theRules = new ArrayList<>();

    /**
     * Construct a new RuleStack consisting of one or more rules.
     *
     * @param isAnd
     * @param stackName
     * @throws BadRuleException
     */
    public RuleStack(String isAnd, String stackName) throws BadRuleException {
        super();

        isValidRuleCombination(isAnd);
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
        for (Rule theRule : theRules) {
            if (theRule.evaluate(theNumericValues, theStringValues) != null) {
                matchCount++;
            } else if (isAnd.equals(AND)) {
                // Stop doing evaluations if doing an 'And' operation
                // and one fails
                break;
            }
        }

        // If we're an 'and' and we didn't match all the rules, trip
        // If we're an 'or' trip if any rule matched.
        if ((isAnd.equals(AND) && matchCount == theRules.size()) || (matchCount > 0)) {
            return getStackName();
        }

        // nothing matched
        return null;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("RuleStack [isAnd=");
        builder.append(isAnd);
        builder.append(", stackName='");
        builder.append(stackName);

        builder.append("', theRules=");
        for (Rule theRule : theRules) {
            builder.append(theRule);
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

        for (Rule theRule : theRules) {

            theRule.toSQL(builder, ruleSetName, stackName, isAnd);

        }

    }

    /**
     * @return the stackName
     */
    public String getStackName() {
        return stackName;
    }

    public static void isValidRuleCombination(String ruleCombination) throws BadRuleException {

        for (String element : RULE_COMBINATION_OPERATORS) {

            if (element.equals(ruleCombination)) {
                return;
            }

        }

        throw new BadRuleException("unrecongized rule combination of '" + ruleCombination + "'");
    }

    public int getRuleCount() {
       
        return theRules.size();
    }

}
