package com.e.s.tool.config.pojo;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationData {

    private List<PolicyLocator> policyLocators;
    private List<Subscriber> subscribers;
    private List<SubscriberGroup> subscriberGroups;
    private List<Service> services;



    public ConfigurationData() {
        this.policyLocators = new ArrayList<PolicyLocator>();
        this.subscribers = new ArrayList<Subscriber>();
        this.subscriberGroups = new ArrayList<SubscriberGroup>();
        this.services = new ArrayList<Service>();
    }

    public List<PolicyLocator> getPolicyLocators() {
        return policyLocators;
    }

    public void setPolicyLocators(List<PolicyLocator> policyLocators) {
        this.policyLocators = policyLocators;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public List<SubscriberGroup> getSubscriberGroups() {
        return subscriberGroups;
    }

    public void setSubscriberGroups(List<SubscriberGroup> subscriberGroups) {
        this.subscriberGroups = subscriberGroups;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }



}
