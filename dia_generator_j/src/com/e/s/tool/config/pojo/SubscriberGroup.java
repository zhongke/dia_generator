package com.e.s.tool.config.pojo;

import java.util.ArrayList;
import java.util.List;

public class SubscriberGroup {

    public static String PATTERN_EPC_SUBSCRIBER_GROUP_DESCRIPTION   = "EPC-SubscriberGroupDescription";
    public static String PATTERN_EPC_SUBSCRIBED_SERVICES            = "EPC-SubscribedServices";
    public static String PATTERN_EPC_BLACKLIST_SERVICES             = "EPC-BlacklistServices";
    public static String PATTERN_EPC_NOTIFICATION_DATA              = "EPC-NotificationData";
    public static String PATTERN_EPC_EVENT_TRIGGERS                 = "EPC-EventTriggers";


/*
    EPC-SubscriberGroupId
*/    

    public static List<String> attributeList;

    static {
        attributeList = new ArrayList<String>();
        attributeList.add("GROUP");
        attributeList.add("DESCRIPTION");
        attributeList.add("SUBSCRIBED");
        attributeList.add("BLACKLIST");
        attributeList.add("TRIGGER");
        attributeList.add("NOTIFICATION");
    }

    private String subscriberGroupId;
    private String description;
    private List<String> subscribedServiceIds;
    private List<String> blacklistServiceIds;
    private List<String> eventTriggers;
    private List<String> notificationData;

    
    
    public SubscriberGroup() {
        
        this.subscribedServiceIds = new ArrayList<String>();
        this.blacklistServiceIds = new ArrayList<String>();
        this.eventTriggers = new ArrayList<String>();
        this.notificationData = new ArrayList<String>();


    }

    public String getSubscriberGroupId() {
        return subscriberGroupId;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getSubscribedServiceIds() {
        return subscribedServiceIds;
    }

    public List<String> getBlacklistServiceIds() {
        return blacklistServiceIds;
    }

    public List<String> getEventTriggers() {
        return eventTriggers;
    }

    public List<String> getNotificationData() {
        return notificationData;
    }

    public void setSubscriberGroupId(String subscriberGroupId) {
        this.subscriberGroupId = subscriberGroupId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubscribedServiceIds(List<String> subscribedServiceIds) {
        this.subscribedServiceIds = subscribedServiceIds;
    }

    public void setBlacklistServiceIds(List<String> blacklistServiceIds) {
        this.blacklistServiceIds = blacklistServiceIds;
    }

    public void setNotificationData(List<String> notificationData) {
        this.notificationData = notificationData;
    }

    public void setEventTriggers(List<String> eventTriggers) {
        this.eventTriggers = eventTriggers;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("SubscriberGroup [subscriberGroupId=");
        buffer.append(subscriberGroupId);

        buffer.append("[description=");
        buffer.append(description);


        for (String subscribedServiceId : subscribedServiceIds) {

            buffer.append(", subscribedServiceId=");
            buffer.append(subscribedServiceId);
        }

        for (String blacklistServiceId : blacklistServiceIds) {

            buffer.append(", blacklistServiceId=");
            buffer.append(blacklistServiceId);
        }

        for (String notification : notificationData) {

            buffer.append(", notificationData=");
            buffer.append(notification);
        }


        for (String eventTrigger : eventTriggers) {

            buffer.append(", eventTrigger=");
            buffer.append(eventTrigger);
        }



        buffer.append("]");

        return buffer.toString();
    }
    
}
