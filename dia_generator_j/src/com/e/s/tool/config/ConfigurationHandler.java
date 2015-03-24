package com.e.s.tool.config;

/**
 * This interface provide 2 kinds of way to get configuration
 * 
 * LDAP or MySql
 * 
 * @author Kevin Zhong
 *
 */
public interface ConfigurationHandler {

    public void getConfiguration(String fileName);

}
