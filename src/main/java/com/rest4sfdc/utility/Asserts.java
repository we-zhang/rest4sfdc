package com.rest4sfdc.utility;

/**
 * @author Wenchun
 *
 */
public class Asserts {
    
    public static void notNull(Object[] oarray, String errormsg){
        for(Object o: oarray){
           notNull(o, errormsg);
        }
    }
    
    public static void notNull(Object o, String errorMsg){
        if(null==o){
            throw new IllegalArgumentException(errorMsg);
        }
        
        if(o instanceof String){
            String str = (String) o;
            if("".equals(str.trim())){
                throw new IllegalArgumentException(errorMsg);
            }
        }
    }

}
