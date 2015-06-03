package com.e.s.tool.config.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.e.s.tool.config.TableFormatter;
import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.DataObject;
import com.e.s.tool.config.pojo.SubscriberGroup;

public abstract class AbstractConfigurationHandler< T extends DataObject> extends TableFormatter<T> implements ConfigurationHandler {

    private String[] headers = null;




    /*
     * Show all the elements following the order of headers
     */
    protected void showConfiguration(T object, ConfigurationData configurationData) {
        try {
            showHeader(object, configurationData);
            /*
             * Class<?> configClazz = Class.forName("com.e.s.tool.config.pojo.ConfigurationData");
             * Field[] fs = configClazz.getDeclaredFields(); // 得到所有的fields
             * 
             * for (Field f : fs) { Class<?> fieldClazz = f.getType(); // 得到field的class及类型全路径
             * 
             * if (fieldClazz.isPrimitive()) continue; // 【1】 //判断是否为基本类型
             * 
             * if (fieldClazz.getName().startsWith("java.lang")) continue; //
             * getName()返回field的类型全路径；
             * 
             * if (fieldClazz.isAssignableFrom(List.class)) // 【2】 { Type fc = f.getGenericType();
             * // 关键的地方，如果是List类型，得到其Generic的类型
             * 
             * if (fc == null) continue;
             * 
             * if (fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型 { ParameterizedType pt =
             * (ParameterizedType) fc;
             * 
             * Class<?> genericClazz = (Class<?>) (pt.getActualTypeArguments()[0]); // 【4】 //
             * 得到泛型里的class类型对象。 } } }
             */
            List<T> objectList = null;
            List<String[]> attributeLineList;
            
            if (object instanceof SubscriberGroup) {
                objectList = (List<T>) (configurationData.getSubscriberGroups());
            }
/*
            
            Method[] configMethods = configClazz.getDeclaredMethods();
            String configMethodName = null;

            for (int i = 0; i < configMethods.length; ++i) {
                configMethodName = configMethods[i].getName();

                if (configMethodName.startsWith("get")) {
                    Type genericType = (Class<?>) configMethods[i].getGenericReturnType();

                    System.out.println("genericType : " + genericType.toString());
                    if (genericType.equals(List.class.toString() + "<" + SubscriberGroup.class.toString() + ">")) {

                    }
            
                }
            }
*/            


            String[] attributeList = null;

            for (T group : objectList) {
                attributeLineList = new ArrayList<String[]>(); // Iterate every group by the maximum
                                                               // size of its elements.
                for (int i = 0; i <= getMaxSizeOfElement(object); ++i) {
                    attributeList = new String[object.getAttributeList().size()];
                    for (int k = 0; k < object.getAttributeList().size(); ++k) {
                        attributeList[k] = null;
                    }

                    if (getMaxSizeOfElement(object) > 0) {
                        if (i == getMaxSizeOfElement(object)) {
                            break;
                        }
                    }

                    Class<?> clazz = Class.forName(object.getClass().getName());
                    Method[] methods = clazz.getDeclaredMethods();
                    String methodName = null;

                    for (int j = 0; j < methods.length; ++j) {
                        methodName = methods[j].getName();

                        if (methodName.startsWith("get")) {
                            Class<?> returnClass = methods[j].getReturnType();
                            for (int k = 0; k < object.getAttributeList().size(); k++) {
                                String attr = object.getAttributeList().get(k).split(":")[0].toLowerCase();
                                String attrName = methodName.toLowerCase().substring(3, methodName.length());

                                if (attr.equals(attrName)) {

                                    Object result = methods[j].invoke(group, (Object[]) null);
                                    if (returnClass.equals(List.class)) {
                                        getAttribute(attributeList, k, (List<String>) result, i);
                                    } else {
                                        if (returnClass.equals(String.class) && (result != null) && (i == 0)) {
                                            attributeList[k] = result.toString();
                                        } else {
                                            attributeList[k] = null;

                                        }
                                    }
                                }
                            }
                        }


                    }

                    attributeLineList.add(attributeList);
                }


                showObject(attributeLineList, headers);

            }

        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void showHeader(T object, ConfigurationData configurationData) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        showLine();
        

        headers = new String[object.getAttributeList().size()];
        for (int i = 0; i < object.getAttributeList().size(); ++i) {
            headers[i] = null;
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");

        /*
         * Think about more than one subscriber with different attributes Define the order for the
         * header list
         */
        List<T> objectList = null;
        
        if (object instanceof SubscriberGroup) {
            objectList = (List<T>) (configurationData.getSubscriberGroups());
        }

        for (int i = 0; i < objectList.size(); i++) {
            T group = objectList.get(i);

            Class<?> clazz = Class.forName(object.getClass().getName());
            Method[] methods = clazz.getDeclaredMethods();
            String methodName = null;

            for (int j = 0; j < methods.length; ++j) {
                methodName = methods[j].getName();

                if (methodName.startsWith("get")) {
                    List<String> attributeList = object.getAttributeList();
                    
                    
                    for (int k = 0; k < attributeList.size(); k++) {
                        String attr = attributeList.get(k).split(":")[0].toLowerCase();
                        String attrName = methodName.toLowerCase().substring(3, methodName.length());

                        if (attr.equals(attrName)) {
                            Object result = methods[j].invoke(group, (Object[]) null);
                            Class<?> returnClass = methods[j].getReturnType();
                            boolean headerExisted = false;
                            if (returnClass.equals(List.class) && (((List<?>) result).size() > 0)) {
                                headerExisted = true;
                            } else if (returnClass.equals(String.class) && (result != null)) {
                                headerExisted = true;
                            }

                            if (headerExisted) {
                                headers[k] = attributeList.get(k).split(":")[1];
                            }
                        }
                    }
                }
            }
        }

        for (String header : headers) {
            if (null != header) {
                buffer.append(getCell(header, COLUMN_TYPE.CONTEXT));
            }
        }

        System.out.println(PREFIX + buffer.toString());
        showLine();

    }

}
