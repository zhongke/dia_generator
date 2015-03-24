package com.e.s.tool.config.pojo;

import java.util.ArrayList;
import java.util.List;

public class Rule {


    private String ruleId;
    private String condition;
    private List<String> outputs;

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Rule [ruleId=" + ruleId + ", condition=" + condition);

        for (String output : outputs) {
            buffer.append(", output=");
            buffer.append(output);
        }
        buffer.append("]");

        return buffer.toString();
    }


    public Rule() {
        outputs = new ArrayList<String>();
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<String> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<String> outputs) {
        this.outputs = outputs;
    }

}
