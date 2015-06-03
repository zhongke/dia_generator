package com.e.s.tool.config.pojo;

import java.util.ArrayList;
import java.util.List;


public class Subscriber extends DataObject {
    public static String PATTERN_EPC_SUBSCRIBED_SERVICES       = "EPC-SubscribedServices";
    public static String PATTERN_EPC_GROUP_IDS                 = "EPC-GroupIds";
    public static String PATTERN_EPC_TRAFFIC_IDS               = "EPC-TrafficIds";
    public static String PATTERN_EPC_BLACKLIST_SERVICES        = "EPC-BlacklistServices";
    public static String PATTERN_EPC_EVENT_TRIGGERS            = "EPC-EventTriggers";
    public static String PATTERN_EPC_FAMILY_ID                 = "EPC-FamilyId";
    public static String PATTERN_EPC_ENABLE_MASC               = "EPC-EnableMasc";
    public static String PATTERN_EPC_SUBSCRIBER_QUALIFY_DATA   = "EPC-SubscriberQualificationData";
    public static String PATTERN_EPC_NOTIFICATION_DATA         = "EPC-NotificationData";
    public static String PATTERN_EPC_OPERATOR_SPECIFIC_INFO    = "EPC-OperatorSpecificInfo";
    
/*
    EPC-SubscriberId    
*/


    private static List<String> attributeList;
    
    static {
        attributeList = new ArrayList<String>();
        attributeList.add("subscriberId:SUBSCRIBER");
        attributeList.add("subscribedServiceIds:SUBSCRIBED");
        attributeList.add("subscriberGroupIds:GROUP");
        attributeList.add("trafficIds:TRAFFIC");
        attributeList.add("blacklistServiceIds:BLACKLIST");
        attributeList.add("eventTriggers:TRIGGER");
        attributeList.add("familyId:FAMILY");
        attributeList.add("enableMasc:MASC");
        attributeList.add("subscriberQualificationData:QUALIFICATION");
        attributeList.add("notificationData:NOTIFICATION");
        attributeList.add("operatorSpecificInfoList:OPERATOR");
    }


    private String subscriberId;
    private String familyId;
    private String enableMasc;
    private List<String> trafficIds;
    private List<String> subscribedServiceIds;
    private List<String> blacklistServiceIds;
    private List<String> operatorSpecificInfoList;
    private List<String> notificationData;
    private List<String> subscriberGroupIds;
    private List<String> eventTriggers;
    private List<String> subscriberQualificationData;


    public Subscriber() {
        
        this.trafficIds = new ArrayList<String>();
        this.subscribedServiceIds = new ArrayList<String>();
        this.blacklistServiceIds = new ArrayList<String>();
        this.operatorSpecificInfoList = new ArrayList<String>();
        this.notificationData = new ArrayList<String>();
        this.subscriberGroupIds = new ArrayList<String>();
        this.eventTriggers = new ArrayList<String>();
        this.subscriberQualificationData = new ArrayList<String>();
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

    public List<String> getSubscribedServiceIds() {
        return subscribedServiceIds;
    }

    public void setSubscribedServiceIds(List<String> subscribedServiceIds) {
        this.subscribedServiceIds = subscribedServiceIds;
    }

    public List<String> getBlacklistServiceIds() {
        return blacklistServiceIds;
    }

    public void setBlacklistServiceIds(List<String> blacklistServiceIds) {
        this.blacklistServiceIds = blacklistServiceIds;
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

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public List<String> getSubscriberGroupIds() {
        return subscriberGroupIds;
    }

    public void setSubscriberGroupIds(List<String> subscriberGroupIds) {
        this.subscriberGroupIds = subscriberGroupIds;
    }



    public String isEnableMasc() {
        return enableMasc;
    }

    public void setEnableMasc(String enableMasc) {
        this.enableMasc = enableMasc;
    }

    public List<String> getEventTriggers() {
        return eventTriggers;
    }

    public void setEventTriggers(List<String> eventTriggers) {
        this.eventTriggers = eventTriggers;
    }



    public List<String> getSubscriberQualificationData() {
        return subscriberQualificationData;
    }


    public void setSubscriberQualificationData(List<String> subscriberQualificationData) {
        this.subscriberQualificationData = subscriberQualificationData;
    }


    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("Subscriber [subscriberId=");
        buffer.append(subscriberId);

        for (String trafficId : trafficIds) {
            buffer.append(", trafficId=");
            buffer.append(trafficId);
        }

        for (String subscribedServiceId : subscribedServiceIds) {

            buffer.append(", subscribedServiceId=");
            buffer.append(subscribedServiceId);
        }

        for (String blacklistServiceId : blacklistServiceIds) {

            buffer.append(", blacklistServiceId=");
            buffer.append(blacklistServiceId);
        }

        for (String operatorSpecificInfo : operatorSpecificInfoList) {

            buffer.append(", operatorSpecificInfo=");
            buffer.append(operatorSpecificInfo);
        }

        for (String notification : notificationData) {

            buffer.append(", notificationData=");
            buffer.append(notification);
        }

        buffer.append(", familyId=");
        buffer.append(familyId);

        for (String subscriberGroupId : subscriberGroupIds) {

            buffer.append(", subscriberGroupId=");
            buffer.append(subscriberGroupId);
        }

        buffer.append(", enableMasc="); 
        buffer.append(enableMasc); 

        for (String eventTrigger : eventTriggers) {

            buffer.append(", eventTrigger=");
            buffer.append(eventTrigger);
        }

        for (String subscriberQualification : subscriberQualificationData) {

            buffer.append(", subscriberQualification=");
            buffer.append(subscriberQualification);
        }


        buffer.append("]");
        
        return buffer.toString();
    }

    public  List<String> getAttributeList() {
        return attributeList;
    }

}
