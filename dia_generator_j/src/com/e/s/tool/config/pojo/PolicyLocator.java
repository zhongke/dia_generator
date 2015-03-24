package com.e.s.tool.config.pojo;

import java.util.ArrayList;
import java.util.List;

public class PolicyLocator {


    private String Context;
    private String Resource;
    private String Subject;

    private List<Policy> policies;

    public PolicyLocator() {
        policies = new ArrayList<Policy>();
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("PolicyLocator [Context=" + Context + ", Resource=" + Resource + ", Subject=" + Subject);

        for (Policy policy : policies) {
            buffer.append(", policy=");
            buffer.append(policy.toString());
        }

        buffer.append("]");

        return buffer.toString();
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

    public String getResource() {
        return Resource;
    }

    public void setResource(String resource) {
        Resource = resource;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }



}
