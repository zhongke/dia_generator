package com.e.s.tool.config.pojo;

import java.util.ArrayList;
import java.util.List;


public class Subscriber {
/*
    EPC-SubscriberId    
    EPC-TrafficIds
    EPC-SubscribedServices
    EPC-BlacklistServices
    EPC-OperatorSpecificInfo
    EPC-NotificationData
    EPC-FamilyId
    EPC-GroupIds
    EPC-EnableMasc
    EPC-EventTriggers
    
    EPC-SubscriberQualificationData
*/
    private String subscriberId;
    private List<String> trafficIds;
    private List<Service> subscribedServices;
    private List<Service> blacklistServices;
    private List<String> operatorSpecificInfoList;
    private List<String> notificationData;

    private Family family;
    private List<SubscriberGroup> subscriberGroupList;

    private boolean enableMasc;
    private List<EVENT_TRIGGER> eventTriggers;



    public Subscriber() {
        this.subscribedServices = new ArrayList<Service>();
        this.blacklistServices = new ArrayList<Service>();
        this.operatorSpecificInfoList = new ArrayList<String>();
        this.notificationData = new ArrayList<String>();
        this.subscriberGroupList = new ArrayList<SubscriberGroup>();
        this.eventTriggers = new ArrayList<EVENT_TRIGGER>();
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public List<String> getTrafficIds() {
        return trafficIds;
    }

    public void setTrafficIds(List<String> trafficIds) {
        this.trafficIds = trafficIds;
    }

    public List<Service> getSubscribedServices() {
        return subscribedServices;
    }

    public void setSubscribedServices(List<Service> subscribedServices) {
        this.subscribedServices = subscribedServices;
    }

    public List<Service> getBlacklistServices() {
        return blacklistServices;
    }

    public void setBlacklistServices(List<Service> blacklistServices) {
        this.blacklistServices = blacklistServices;
    }

    public List<String> getOperatorSpecificInfoList() {
        return operatorSpecificInfoList;
    }

    public void setOperatorSpecificInfoList(List<String> operatorSpecificInfoList) {
        this.operatorSpecificInfoList = operatorSpecificInfoList;
    }

    public List<String> getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(List<String> notificationData) {
        this.notificationData = notificationData;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public List<SubscriberGroup> getSubscriberGroupList() {
        return subscriberGroupList;
    }

    public void setSubscriberGroupList(List<SubscriberGroup> subscriberGroupList) {
        this.subscriberGroupList = subscriberGroupList;
    }


    public boolean isEnableMasc() {
        return enableMasc;
    }

    public void setEnableMasc(boolean enableMasc) {
        this.enableMasc = enableMasc;
    }

    public List<EVENT_TRIGGER> getEventTriggers() {
        return eventTriggers;
    }

    public void setEventTriggers(List<EVENT_TRIGGER> eventTriggers) {
        this.eventTriggers = eventTriggers;
    }


}
