package com.e.s.tool.config.pojo;

import java.util.ArrayList;
import java.util.List;

public class Policy {

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Policy [policyId=" + policyId + ", combineAlgorithm=" + combineAlgorithm);

        for (Rule rule : rules) {
            buffer.append(", rule=");
            buffer.append(rule.toString());
        }
        buffer.append("]");

        return buffer.toString();
    }


    private String policyId;
    private String combineAlgorithm;
    private List<Rule> rules;

    public Policy() {
        rules = new ArrayList<Rule>();
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getCombineAlgorithm() {
        return combineAlgorithm;
    }

    public void setCombineAlgorithm(String combineAlgorithm) {
        this.combineAlgorithm = combineAlgorithm;
    }

}
