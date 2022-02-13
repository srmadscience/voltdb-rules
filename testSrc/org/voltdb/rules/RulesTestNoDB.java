package org.voltdb.rules;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.voltdb.VoltTable;
import org.voltdbse.rules.BadRuleException;
import org.voltdbse.rules.RuleSet;
import org.voltdbse.rules.RuleStack;

class RulesTestNoDB {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void testNumberEquals() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", (double) 50, null,
                null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();

            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE", (double) 3);

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("== failed");
            }

            theNumericValues.put("SHOESIZE", (double) 50);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("== failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testNumbernotEquals() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "!=", (double) 50, null,
                null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE", (double) 3);

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("!= failed");
            }

            theNumericValues.put("SHOESIZE", (double) 50);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("!= failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testNumberGT() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", ">", (double) 50, null,
                null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE", (double) 3);

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("> failed");
            }

            theNumericValues.put("SHOESIZE", (double) 51);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("> failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testNumberGTEq() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", ">=", (double) 50, null,
                null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE", (double) 3);

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail(">= failed");
            }

            theNumericValues.put("SHOESIZE", (double) 50);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail(">= failed");
            }

            theNumericValues.put("SHOESIZE", (double) 51);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail(">= failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testNumberLT() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<", (double) 50, null,
                null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE", (double) 3);

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("> failed");
            }

            theNumericValues.put("SHOESIZE", (double) 51);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("> failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testNumberLTEq() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<=", (double) 50, null,
                null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE", (double) 3);

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("<= failed");
            }

            theNumericValues.put("SHOESIZE", (double) 50);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("<= failed");
            }

            theNumericValues.put("SHOESIZE", (double) 51);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("<= failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testStringEquals() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", null, "C", null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theStringValues.put("SHOESIZE", "A");

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("== failed");
            }

            theStringValues.put("SHOESIZE", "C");

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("== failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testStringnotEquals() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "!=", null, "C", null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theStringValues.put("SHOESIZE", "A");

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("!= failed");
            }

            theStringValues.put("SHOESIZE", "C");

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("!= failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testStringGT() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", ">", null, "C", null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theStringValues.put("SHOESIZE", "A");

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("> failed");
            }

            theStringValues.put("SHOESIZE", "D");

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("> failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testStringGTEq() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", ">=", null, "C", null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theStringValues.put("SHOESIZE", "A");

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail(">= failed");
            }

            theStringValues.put("SHOESIZE", "C");

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail(">= failed");
            }

            theStringValues.put("SHOESIZE", "D");

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail(">= failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testStringLT() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<", null, "C", null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theStringValues.put("SHOESIZE", "A");

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("> failed");
            }

            theStringValues.put("SHOESIZE", "D");

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("> failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testStringLTEq() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<=", null, "C", null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();
            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theStringValues.put("SHOESIZE", "A");

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("<= failed");
            }

            theStringValues.put("SHOESIZE", "C");

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("<= failed");
            }

            theStringValues.put("SHOESIZE", "D");

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("<= failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testWrongSetName() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<=", null, "C", null);

        try {
            RuleSet testSet = new RuleSet("TESTOTHER", t, new Date(System.currentTimeMillis() + 60000));

            fail("No rules for set");

        } catch (BadRuleException e) {
            // OK
        }

    }

    @Test
    void testLastStackIdIsNull() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<=", null, "C", null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            if (testSet.getLastTriggeredStackDetail() != null) {
                fail("Stack Id shound be null");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testExpiredDate() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<=", null, "C", null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() - 1));

            if (!testSet.expired(new Date())) {
                fail("expire Date not working");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testDodgyRuleNoThresholds() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<=", null, null, null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            fail("Failed to catch rule with no thresholds");

        } catch (BadRuleException e) {
            // expected
        }

    }

    @Test
    void testRightSet() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "test", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", (double) 49, null,
                null);
        RuleSet.addRule(t, "Test", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", (double) 51, null,
                null);
        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", (double) 50, null,
                null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            if (testSet.getRuleCount() != 1) {
                System.out.println("Too many rules " + testSet.getRuleCount());
                fail("Too many rules " + testSet.getRuleCount());
            }

            String toString = testSet.toString();
            String toSql = testSet.toSQL();

            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE", (double) 3);

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("== failed");
            }

            theNumericValues.put("SHOESIZE", (double) 49);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("== failed wrong rule");
            }

            theNumericValues.put("SHOESIZE", (double) 51);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("== failed wrong rule");
            }

            theNumericValues.put("SHOESIZE", (double) 50);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("== failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testDodgyRuleTooManyThresholds() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<=", (double) 42, "C",
                null);

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            fail("Failed to catch rule with two thresholds");

        } catch (BadRuleException e) {
            // expected
        }

        t = RuleSet.getEmptyRuleTable();

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<=", null, "C", "X");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            fail("Failed to catch rule with two thresholds");

        } catch (BadRuleException e) {
            // expected
        }

        t = RuleSet.getEmptyRuleTable();

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<=", (double) 42, null,
                "X");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            fail("Failed to catch rule with two thresholds");

        } catch (BadRuleException e) {
            // expected
        }

        t = RuleSet.getEmptyRuleTable();

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "<=", (double) 42, "C",
                "X");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            fail("Failed to catch rule with all thresholds");

        } catch (BadRuleException e) {
            // expected
        }

    }

    @Test
    void testNumberEqualsExpression() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", null, null, "IQ");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();

            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE", (double) 3);
            theNumericValues.put("IQ", (double) 100);

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("== failed");
            }

            theNumericValues.put("IQ", (double) 3);

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("== failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testNumberEqualsBadExpression() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", null, null,
                "IQ__3");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();

            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE", (double) 3);
            theNumericValues.put("IQ", (double) 100);

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("== failed");
            }

            theNumericValues.put("IQ", (double) 3);

            result = testSet.evaluate(theNumericValues, theStringValues);

            fail("Exception not thrown");

        } catch (BadRuleException e) {
            // OK
        }

    }

    @Test
    void testStringEqualsExpression() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", null, null, "IQ");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();

            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theStringValues.put("SHOESIZE", "THREE");
            theStringValues.put("IQ", "100");

            String result = testSet.evaluate(theNumericValues, theStringValues);

            if (result != null) {
                fail("== failed");
            }

            theStringValues.put("IQ", "THREE");

            result = testSet.evaluate(theNumericValues, theStringValues);

            if (result == null) {
                fail("== failed");
            }

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testNumberEqualsBadField() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", null, null, "IQ");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();

            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE_XX", (double) 3);
            theNumericValues.put("IQ", (double) 100);
            String result = testSet.evaluate(theNumericValues, theStringValues);
            fail("Should throw error");

        } catch (BadRuleException e) {
//OK
        }

    }

    @Test
    void testStringEqualsBadExpression() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", null, null, "IQ");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();

            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theStringValues.put("SHOESIZE", "THREE");
            theStringValues.put("IQ2", "100");

            String result = testSet.evaluate(theNumericValues, theStringValues);
            fail("Should throw error");

        } catch (BadRuleException e) {
            // OK
        }

    }

    @Test
    void testStringEqualsBadField() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", null, null, "IQ");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            String toString = testSet.toString();
            String toSql = testSet.toSQL();

            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theStringValues.put("SHOESIZE_XX", "THREE");
            theStringValues.put("IQ2", "100");

            String result = testSet.evaluate(theNumericValues, theStringValues);
            fail("Should throw error");

        } catch (BadRuleException e) {
            // OK
        }

    }
    
    @Test
    void testBadOperator() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "*", null, null, "IQ");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

           
            fail("Should throw error");

        } catch (BadRuleException e) {
            // OK
        }

    }
    @Test
    void testBadCombo() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND + "XXX", "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", null, null, "IQ");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

           
            fail("Should throw error");

        } catch (BadRuleException e) {
            // OK
        }

    }

    @Test
    void smokeTest2() {

        VoltTable t = RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;

        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", (double) 50, null,
                null);
        RuleSet.addRule(t, "TEST", RuleStack.AND, "RuleStack" + stackId++, seqno++, "SHOESIZE", ">=", (double) 8, null,
                null);

        RuleSet.addRule(t, "TEST", RuleStack.OR, "RuleStack" + stackId, seqno++, "SHOESIZE", "=", (double) 1, null,
                null);
        RuleSet.addRule(t, "TEST", RuleStack.OR, "RuleStack" + stackId++, seqno++, "SHOESIZE", "=", (double) 2, null,
                null);

        RuleSet.addRule(t, "TEST", RuleStack.OR, "RuleStack" + stackId, seqno++, "COLOR", "=", null, "RED", null);
        RuleSet.addRule(t, "TEST", RuleStack.OR, "RuleStack" + stackId++, seqno++, "COLOR", "=", null, "GREEN", null);

        RuleSet.addRule(t, "TEST", RuleStack.OR, "COLOR < YELLOW", seqno++, "COLOR", "<", null, "YELLOW", null);

        RuleSet.addRule(t, "TEST", RuleStack.OR, "Shoesize > IQ", seqno++, "SHOESIZE", ">", null, null, "IQ");

        try {
            RuleSet testSet = new RuleSet("TEST", t, new Date(System.currentTimeMillis() + 60000));

            HashMap<String, Double> theNumericValues = new HashMap<>();
            HashMap<String, String> theStringValues = new HashMap<>();

            theNumericValues.put("SHOESIZE", (double) 3);
            theNumericValues.put("IQ", (double) 2);
            theStringValues.put("COLOR", "PINK");

            testSet.expired(new Date());

            System.out.println(testSet.toSQL());

            String result = testSet.evaluate(theNumericValues, theStringValues);

            System.out.println(theNumericValues + " " + theStringValues + " " + result);
            System.out.println(testSet.getLastTriggeredStackDetail());

        } catch (BadRuleException e) {
            fail(e.getMessage());
        }

        // fail("Not yet implemented");
    }

}
