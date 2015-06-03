package com.e.s.tool.config.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.e.s.tool.config.TableFormatter;
import com.e.s.tool.config.pojo.DataObject;

public abstract class AbstractConfigurationHandler< T extends DataObject> extends TableFormatter<T> implements ConfigurationHandler {

    private String[] headers = null;




    /*
     * Show all the elements following the order of headers
     */
    protected void showConfiguration(T object, List<T> objectList) {
        try {
            showHeader(object, objectList);
            List<String[]> attributeLineList;
            
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

    private void showHeader(T object,  List<T> objectList) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
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
    
    private int getMaxSizeOfElement(T object) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
        int size = 0;
        
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
                        Object result = methods[j].invoke(object, (Object[]) null);
                        Class<?> returnClass = methods[j].getReturnType();
                        if (returnClass.equals(List.class) && (((List<?>) result).size() > size )) {
                           size = ((List<?>) result).size();
                        }

                    }
                }
            }
        }
        return size;

    }

}
