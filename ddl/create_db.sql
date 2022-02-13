

CREATE TABLE volt_rules
(RULESET_NAME varchar(30) not null
,SEQNO bigint not null
,ISAND varchar(5) not null
,STACK_NAME varchar(80) not null
,RULE_FIELD varchar(80) not null
,RULE_OPERATOR varchar(2) not null
,THRESHOLD_FLOAT float 
,THRESHOLD_STRING  varchar(256) 
,THRESHOLD_EXPRESSION  varchar(256) 
,primary key (RULESET_NAME, SEQNO));

