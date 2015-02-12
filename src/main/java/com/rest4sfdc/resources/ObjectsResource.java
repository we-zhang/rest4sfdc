package com.rest4sfdc.resources;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.rest4sfdc.Invoker;
import com.rest4sfdc.RequestConfig;
import com.rest4sfdc.ResponseFormat;
import com.rest4sfdc.utility.Asserts;

/**
 * Resouce group for sObjects related resources. The base url address of this resource group is something like:<br> 
 * <code>https://{instance_url}/services/data/{version}/sobjects/</code>
 * 
 * @author Wenchun
 *
 */
public class ObjectsResource extends WebResource {
    
    private Invoker invoker;
    private ResponseFormat mediaType;
    
    public ObjectsResource(Invoker invoker){
        this.invoker = invoker;
        this.mediaType = ResponseFormat.APPLICATION_JSON;
    }
    
    /**
     * Group level setting for accepted type, can be overrided in resource level.
     * 
     * @param format
     */
    public void accept(ResponseFormat format){
        this.mediaType = format;
    }
    
    /**
     * Build base Url of object resources. <br>
     * eg. https://na1.salesforce.com/services/data/{version}/sobjects/ <br>
     * 
     * @return
     */
    public String buildObjectsResourceUrl(String instanceUrl, String version){
        return this.buildBaseUrl(instanceUrl, version)+"/sobjects";
    }
    
    /**
     * Resources on <code>baseUrl:
     * https://na1.salesforce.com/services/data/{version}/sobjects/
     * 
     * @return
     */
    public String getObjects(){
        return this.getObjects(mediaType);
    }
    
    public String getObjects(ResponseFormat rspType){
        Client client = invoker.getRequestClient();
        RequestConfig config = invoker.getRequestConfig();
        
        WebTarget target = client.target(this.buildObjectsResourceUrl(config.getInstanceUrl(), config.getApiVersion()));
        
//        System.out.println("The resource Url for getObjects() method: " + target.getUri().toASCIIString());
        
        Response response = constructBuilder(rspType, target, config.getAccessToken()).get();
        
        if(response!=null){
            return response.readEntity(String.class);
        }else{
            return null;
        }
    }
    
    /**
     * Basic metadata for a sObject, resource at: <br>
     * uri: /sobjects/{object}/; <br>
     * 
     * eg. https://na1.salesforce.com/services/data/v29.0/sobjects/Account/
     * 
     * @param object object name, eg Account
     * @return
     */
    public String getObjectBasicMetadata(String object){
        Asserts.notNull(object, "the argument of getObjectBasicMetadata() should not be null");
        
        return this.getObjectBasicMetadata(mediaType, object);
    }
    
    public String getObjectBasicMetadata(ResponseFormat rspType, String object){
        Asserts.notNull(object, "the argument of getObjectBasicMetadata() should not be null");
        
        Client client = invoker.getRequestClient();
        RequestConfig config = invoker.getRequestConfig();
        
        WebTarget target = client.target(this.buildObjectsResourceUrl(config.getInstanceUrl(), config.getApiVersion()))
                .path(object);
        
//        System.out.println("The resource Url for getObjectBasicMetadata() method: " + target.getUri().toASCIIString());
        
        Response response = constructBuilder(rspType, target, config.getAccessToken()).get();
        
        if(response!=null){
            return response.readEntity(String.class);
        }else{
            return null;
        }
    }
    
    /**
     * All metadata for a object. <br>
     * uri: /{sObject}/describe <br>
     * 
     * eg. https://na1.salesforce.com/services/data/v20.0/Account/describe
     * 
     * @param object
     * @return
     */
    public String getObjectAllMetadata(String object){
        Asserts.notNull(object, "the argument of getObjectAllMetadata() should not be null");
        
        return this.getObjectAllMetadata(mediaType, object);
    }
    
    public String getObjectAllMetadata(ResponseFormat rspType, String object){
        Asserts.notNull(object, "the argument of getObjectAllMetadata() should not be null");
        
        Client client = invoker.getRequestClient();
        RequestConfig config = invoker.getRequestConfig();
        
        WebTarget target = client.target(this.buildObjectsResourceUrl(config.getInstanceUrl(), config.getApiVersion()))
                .path(object).path("describe");
        
//        System.out.println("The resource Url for getObjectAllMetadata() method: " + target.getUri().toASCIIString());
        
        Response response = constructBuilder(rspType, target, config.getAccessToken()).get();
        
        if(response!=null){
            return response.readEntity(String.class);
        }else{
            return null;
        }
    }
    
    /**
     * Create a sObject record.<p>
     * 
     * Resource URL: https://na1.salesforce.com/services/data/{version}/sobjects/{objectName}/<br>
     * 
     * Due to the limitation of Salesforce REST API, now only support to insert one record at a call, using Bulk API to insert multiple records.
     * 
     * @param objectName
     * @param jsonEntity record json string representaion, eg.<br>
     *        {
     *          "Name" : "rest4sfdcTestAccount",
     *          "BillingCity" : "Beijing",
     *          "BillingStreet" : "Software Park, Shangid",
     *          ....
     *        }
     * @return
     */
    public String createObject(String objectName, String jsonEntity){
        Asserts.notNull(objectName, "the argument of createObject() should not be null");
        
        return createObject(mediaType, objectName, jsonEntity);
    }
    
    /**
     * 
     * @param object object entity
     * @return
     */
    public String createObject(ObjectEntity object){
        Asserts.notNull(object, "object entity can be null in createObject() method");
        
        JsonObject ob = new JsonObject();
        Map<String, JsonPrimitive> fields = object.getFields();
        
        Iterator<Entry<String, JsonPrimitive>> it = fields.entrySet().iterator();
        while(it.hasNext()){
            Entry<String, JsonPrimitive> entry = it.next();
            ob.add(entry.getKey(), entry.getValue());
        }
        
        return createObject(object.getType(), ob.toString());
    }
    
    /**
     * @param objectName
     * @param jsonEntity sObject record representaion
     * @see {@link #createObject(String, String)}
     * @return
     */
    public String createObject(String objectName, File jsonEntity){
        Asserts.notNull(objectName, "the argument of createObject() should not be null");
        return createObject(mediaType, objectName, jsonEntity);
    }
    
    /**
     * 
     * @param rspType json or xml format
     * @param objectName
     * @param jsonEntity sObject record representaion, can String or File
     * @see {@link #createObject(String objectName, String jsonEntity)}
     * @return
     */
    public String createObject(ResponseFormat rspType, String objectName, Object jsonEntity){
        Asserts.notNull(objectName, "the argument of createObject() should not be null");
        
        Client client = invoker.getRequestClient();
        RequestConfig config = invoker.getRequestConfig();
        
        WebTarget target = client.target(this.buildObjectsResourceUrl(config.getInstanceUrl(), config.getApiVersion()))
            .path(objectName);
        
        Invocation.Builder builder = constructBuilder(rspType, target, config.getAccessToken());
        
        Response response = builder.post(Entity.entity(jsonEntity, MediaType.APPLICATION_JSON));
        
        if(response!=null){
            return response.readEntity(String.class);
        }else{
            return null;
        }
    }
    
    /**
     * Get field values from a object record based on the record id. <p>
     * 
     * Resource Url: .../{apiVersion}/sobjects/{object}/{id}/.
     * @param id
     * @param fields field names, if omitted, all fields will return.
     * @return
     */
    public String getObjectFields(String objectName, String id, String...fields){
       Asserts.notNull(new String[]{objectName, id}, "the argument of getObjectFields() should not be null");
        
       return getObjectFields(mediaType, objectName, id, fields);
    }
    
    /**
     * 
     * @param format response format, json or xml
     * @param objectName
     * @param id
     * @see {@link #getObjectFields(String, String)}
     * @return
     */
    public String getObjectFields(ResponseFormat format, String objectName, String id, String...fields){
        Asserts.notNull(new String[]{objectName, id}, "the argument of getObjectFields() should not be null");
        
        Client client = invoker.getRequestClient();
        RequestConfig config = invoker.getRequestConfig();
        
        WebTarget webTarget = client.target(this.buildObjectsResourceUrl(config.getInstanceUrl(),config.getApiVersion()))
                .path(objectName).path(id);
        if(fields!=null && fields.length>0){
            // add fields in query param
            StringBuilder sb = new StringBuilder();
            int len = fields.length;
            for(int i=0; i<len; i++){
                if(fields[i]==null || "".equals(fields[i].trim())){
                    continue;
                }
                
                if(i>=len-1){
                    sb.append(fields[i]);
                }else{
                    sb.append(fields[i]).append(",");
                }
            }
            
            webTarget.queryParam("fields", sb.toString());
        }
        
        Invocation.Builder builder = this.constructBuilder(format, webTarget, config.getAccessToken());
        Response response = builder.get();
        
        if(response!=null){
            return response.readEntity(String.class);
        }else{
            return null;
        }
    }
    
    /**
     * Delete a record based on record id. <p>
     * 
     * Resource Url: .../{apiVersion}/sobjects/{object}/{id}
     * @param objectName
     * @param id
     * @return
     */
    public String deleteObject(String objectName, String id){
        Asserts.notNull(new String[]{objectName, id}, "the argument of getObjectFields() should not be null");
        
        return deleteObject(mediaType, objectName,  id);
    }
    
    /**
     * 
     * @param format
     * @param objectName
     * @param id
     * @see {@link #deleteObject(String, String)}
     * @return
     */
    public String deleteObject(ResponseFormat format, String objectName, String id){
        Asserts.notNull(new String[]{objectName, id}, "the argument of getObjectFields() should not be null");
        
        Client client = invoker.getRequestClient();
        RequestConfig config = invoker.getRequestConfig();
        WebTarget webTarget = client.target(this.buildObjectsResourceUrl(config.getInstanceUrl(),config.getApiVersion()))
                .path(objectName).path(id);
        
        Invocation.Builder builder = this.constructBuilder(format, webTarget, config.getAccessToken());
        
        Response response = builder.delete();
        if(response!=null){
            return response.readEntity(String.class);
        }else{
            return null;
        }
    }
    
    /**
     * Update a object record based on record id.<p>
     * 
     * Resource Url: .../{version}/sobjects/{object}/{id}
     * 
     * @param objectName
     * @param id
     * @param jsonEntity record json string represtation which includes the updated fields.
     * @return
     */
    public String updateObject(String objectName, String id, String jsonEntity){
       
        return updateObject(mediaType, objectName, id, jsonEntity);
    }
    
    /**
     * 
     * @param objectName
     * @param id
     * @param jsonEntity file object contains json data in it
     * @see {@link #updateObject(String, String, String)}
     * @return
     */
    public String updateObject(String objectName, String id, File jsonEntity){
        
        return updateObject(mediaType, objectName, id, jsonEntity);
    }
    
    /**
     * 
     * @param object Object entity
     * @return
     */
    public String updateObject(ObjectEntity object){
        Asserts.notNull(object, "object entity can be null in updateObject() method");
        
        Asserts.notNull(object.getId(), "the Id is not set for object entity in updateObject() method");
        
        JsonObject ob = new JsonObject();
        Map<String, JsonPrimitive> fields = object.getFields();
        
        Iterator<Entry<String, JsonPrimitive>> it = fields.entrySet().iterator();
        while(it.hasNext()){
            Entry<String, JsonPrimitive> entry = it.next();
            if(ObjectEntity.ID_FIELD.equals(entry.getKey())){
                //filte Id field for updating
                continue;
            }
            ob.add(entry.getKey(), entry.getValue());
        }
        
        return updateObject(object.getType(), object.getId(), ob.toString());
    }
    
    /**
     * 
     * @param format
     * @param objectName
     * @param id
     * @param jsonEntity can be String or File
     * @see {@link #updateObject(String, String, String)}
     * @see {@link #updateObject(String, String, File)}
     * @return
     */
    public String updateObject(ResponseFormat format, String objectName, String id, Object jsonEntity){
        Client client = invoker.getRequestClient();
        RequestConfig config = invoker.getRequestConfig();
        
        // add query param "?_HttpMethod=PATCH" since Jersey doesn't support PATCH method
        WebTarget target = client.target(this.buildObjectsResourceUrl(config.getInstanceUrl(), config.getApiVersion()))
            .path(objectName).path(id).queryParam("_HttpMethod", "PATCH");
        
        Invocation.Builder builder = constructBuilder(format, target, config.getAccessToken());
        
        Response response = builder.method("POST",Entity.entity(jsonEntity, MediaType.APPLICATION_JSON)); 
        if(response!=null){
            return response.readEntity(String.class);
        }else{
            return null;
        }
    }
    
    
}
