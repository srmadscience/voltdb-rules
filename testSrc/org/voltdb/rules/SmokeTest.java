package org.voltdb.rules;

import java.util.Date;
import java.util.HashMap;

import org.voltdb.VoltTable;
import org.voltdbse.rules.BadRuleException;
import org.voltdbse.rules.RuleSet;


public class SmokeTest {

    public static void main(String[] args) {
       
        

        VoltTable t =  RuleSet.getEmptyRuleTable();
        long seqno = 0;
        long stackId = 0;
        
        RuleSet.addRule(t, "TEST", true, "RuleStack"+stackId++, seqno++, "SHOESIZE", "=", (double) 50,null,null);
        RuleSet.addRule(t, "TEST", true, "RuleStack"+stackId++, seqno++, "SHOESIZE", ">=", (double) 8,null,null);
        
        RuleSet.addRule(t, "TEST", false, "RuleStack"+stackId, seqno++, "SHOESIZE", "=", (double) 1,null,null);
        RuleSet.addRule(t, "TEST", false, "RuleStack"+stackId++, seqno++, "SHOESIZE", "=", (double) 2,null,null);
        
        RuleSet.addRule(t, "TEST", false, "RuleStack"+stackId, seqno++, "COLOR", "=", null,"RED",null);
        RuleSet.addRule(t, "TEST", false, "RuleStack"+stackId++, seqno++, "COLOR", "=", null,"GREEN",null);
        
        RuleSet.addRule(t, "TEST", false, "COLOR < YELLOW", seqno++, "COLOR", "<", null,"YELLOW",null);
        
        RuleSet.addRule(t, "TEST", false, "Shoesize > IQ", seqno++, "SHOESIZE", ">", null,null,"IQ");
        
               
        try {
            RuleSet testSet = new RuleSet("TEST",t, new Date(System.currentTimeMillis() + 60000));
            
            
            HashMap<String, Double> theNumericValues = new HashMap<String, Double>();
            HashMap<String, String> theStringValues = new HashMap<String, String>();
                       
            theNumericValues.put("SHOESIZE", (double) 3);
            theNumericValues.put("IQ", (double) 2);
            theStringValues.put("COLOR", "PINK");
           
                       
            
            String result = testSet.evaluate(theNumericValues,theStringValues);
            
            System.out.println(theNumericValues + " " + theStringValues + " " + result);
            
            
            
            
            
           
        } catch (BadRuleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
