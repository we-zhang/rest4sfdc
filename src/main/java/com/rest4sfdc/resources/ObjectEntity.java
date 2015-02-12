package com.rest4sfdc.resources;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonPrimitive;
import com.rest4sfdc.utility.StringUtil;

/**
 * Represents a sObject
 * 
 * @author Wenchun
 *
 */
public class ObjectEntity {
    
    public final static String ID_FIELD = "Id";

    private String type;
    private String id; //set to fields map, key=Id
    
    private Map<String, JsonPrimitive> fields;
    
    public ObjectEntity(){
        fields = new HashMap<String, JsonPrimitive>();
    }
    
    public ObjectEntity(String type){
        this.type = type;
        fields = new HashMap<String, JsonPrimitive>();
    }
    
    public void addField(String field, JsonPrimitive value){
       if(StringUtil.isEmpty(field))
           //omit the null field...
           return;

        fields.put(field, value);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public void setId(String id){
        this.id = id;
        fields.put(ID_FIELD, new JsonPrimitive(id));
    }
    
    public String getId(){
        return this.id;
    }
    
    public Map<String, JsonPrimitive> getFields(){
        return this.fields ;
    }
}
