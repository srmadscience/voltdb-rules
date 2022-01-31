# voltdb-rules - a rules engine for Volt

voltdb-rules is a table and java package that allows you to implement arbitrary table-driven logic.

It consists of the following components:

# RuleSet 

A [RuleSet](https://github.com/srmadscience/voltdb-rules/blob/main/src/rules/RuleSet.java) is a collection of rules that represents a business problem.

It takes two values as inputs

* A HashMap<String, Double> called 'theNumericValues' that consists of named numneric aspects of the current state
* A HashMap<String, String> called 'theStringValues'that consists of named numneric aspects of the current state

Between them these hashmaps store all the state you need to run your ruleset using the '[evaluate]'(https://github.com/srmadscience/voltdb-rules/blob/main/src/rules/RuleSet.java#L111) method.

A RuleSet consists of one or more RuleStacks. Each Rulestack is independent from the others.

# RuleStack

A [RuleStack](https://github.com/srmadscience/voltdb-rules/blob/main/src/rules/RuleStack.java) consists of one or more rules and a name. the rules are subject an an AND operation or an OR operation. If you trigger a Rule Stack its name is returned, otherwise null. 

# Rule

A [Rule](https://github.com/srmadscience/voltdb-rules/blob/main/src/rules/Rule.java) is a comparison between a named input value and either a number/String or other input value. 

# VOLT_RULES

[VOLT_RULES](https://github.com/srmadscience/voltdb-rules/blob/main/ddl/create_db.sql#L3) is a table that we store the rules in:

````
iCREATE TABLE volt_rules
(RULESET_NAME varchar(30) not null
,SEQNO bigint not null
,ISAND tinyint not null
,STACK_NAME varchar(80) not null
,RULE_FIELD varchar(80) not null
,RULE_OPERATOR varchar(2) not null
,THRESHOLD_FLOAT float 
,THRESHOLD_STRING  varchar(256) 
,THRESHOLD_EXPRESSION  varchar(256) 
,primary key (RULESET_NAME, SEQNO));
````

Note that we only use one table.

* RULESET_NAME's meaning is obvious.
* SEQNO is an ascending integer within a rule set. Having seqno start at a round number(e.g. 20) and increment by one within a stack seems to work well.
* ISAND is 1 if the stack needs all the rules to be true, or 0 if any rule will trigger the stack
* STACK_NAME is the name of the stack. Note that it's denormalized.
* RULE_FIELD is the name of the field used. At runtime the HashMaps need to contain a value for this.
* RULE_OPERATOR is one of =, !=, >, <, >=, <=

One one of the three THRESHOLD fields can be populated.

* THRESHOLD_FLOAT contains a number if we are saying - for example - 'shoesize > 12'. 
* THRESHOLD_FIELD contains a text  string if we are matching strings, for example 'COLOR = RED'
* THRESHOLD_EXPRESSION contains the name of <i>another</i> entry in the hashmap we are comparing to, for example 'shoesize > max_shoesize_stocked'

Within a RULESET rules are ordered by SEQNO.

An example of DML to create two stacks would be:

````
INSERT INTO volt_rules
VALUES
('SIMBOX',32,1,'suspicious_device_has_no_incoming_calls', 'incomingCallCount','=',0,null,null);

INSERT INTO volt_rules
VALUES
('SIMBOX',33,1,'suspicious_device_has_no_incoming_calls', 'outgoingCallCount','>',0,null,null);


INSERT INTO volt_rules
VALUES
('SIMBOX',41,1,'suspiciously_moving_device', 'thisDeviceIsSuspicious','=',1,null,null);


INSERT INTO volt_rules
VALUES
('SIMBOX',51,1,'total_incoming_outgoing_ratio_bad', 'actualBusynessPercentage','>=',null,null,'busynessPercentage');

INSERT INTO volt_rules
VALUES
('SIMBOX',52,1,'total_incoming_outgoing_ratio_bad', 'outgoingIncomingRatioTrip','<',null,null,'outgoingCallCount');


````


# Using voltdb-rules

* Import the classes

````
load classes voltdb-rules.jar;
````

* Create a RuleSet variable in your procedure

````
RuleSet rs = null;
````

* Create a SQL statement to read the rules

Note that RuleSet has a static method that gives you the correct SQL:

````
public static final SQLStmt getRules = new SQLStmt(RuleSet.GET_RULE_SET);
````

* Create the RuleSet object if it is null or stale. Note that you *must* use getTransactionTime() to get the time.

````
        if (rs == null || rs.expired(getTransactionTime())) {
            try {
                rs = createRuleSet("SIMBOX");
            } catch (BadRuleException e) {
                throw new VoltAbortException("BadRuleException:"+e.getMessage());
            }
        }
````        
    
 * As part of the procedure create the HashMap's needed to use the RuleSet:
 
````
       HashMap<String, Double> theNumericValues = new  HashMap<String, Double>();
       HashMap<String, String> theStringValues = new HashMap<String, String>();
````
 
*  Add numeric or text values to the HashMap

````
theNumericValues.put("actualBusynessPercentage", (double) (actualBusyInCallPct + actualBusyOutCallPct));  
````

* Call the RuleSet. It returns null if no rule tripped, or the name of the stack.

````
           try {
                String ruleTripped = rs.evaluate(theNumericValues, theStringValues);
                
                if (ruleTripped != null) {
                    System.out.println(ruleTripped);
                    voltQueueSQL(flagDevice, ruleTripped, 42,
                            deviceId);
                } else {
                    voltQueueSQL(clearDevice, deviceId);
                }
                
            } catch (BadRuleException e) {
                System.out.println(rs.toString() + " " + e.getMessage());
                throw new VoltAbortException("BadRuleException:"+e.getMessage());
            }
````
