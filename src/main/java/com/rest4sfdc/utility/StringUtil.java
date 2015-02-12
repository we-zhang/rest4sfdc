package com.rest4sfdc.utility;

/**
 * @author Wenchun
 *
 */
public class StringUtil {

    public static boolean isEmpty(String str){
        return (null==str || "".equals(str.trim()));
    } 
    
}
