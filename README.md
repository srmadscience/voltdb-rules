# voltdb-rules - a rules engine for Volt

voltdb-rules is a table and java package that allows you to implement arbitrary table-driven logic.

It consists of the following components:

# RuleSet 

A [RuleSet](https://github.com/srmadscience/voltdb-rules/blob/main/src/rules/RuleSet.java) is a collection of rules that represents a business problem.

It takes two values as inputs

* A HashMap<String, Double> called 'theNumericValues' that consists of named numneric aspects of the current state
* A HashMap<String, String> called 'theStringValues'that consists of named numneric aspects of the current state

Between them these hashmaps store all the state you need to run your ruleset using the '[evaluate](https://github.com/srmadscience/voltdb-rules/blob/main/src/rules/RuleSet.java#L111) method.

A RuleSet consists of one or more RuleStacks. Each Rulestack is independent from the others.
