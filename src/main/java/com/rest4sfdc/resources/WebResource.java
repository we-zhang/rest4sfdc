package com.rest4sfdc.resources;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.rest4sfdc.ResponseFormat;

/**
 * 
 * @author Wenchun
 *
 */
public class WebResource {

    protected static final String OAUTH_TOKEN_PREFIX = "OAuth ";
    
    protected Invocation.Builder constructBuilder(ResponseFormat rspType, WebTarget target, String token){
        if(null==rspType){
            rspType = ResponseFormat.APPLICATION_JSON;
        }
        
        Builder builder;
        if(ResponseFormat.APPLICATION_JSON==rspType){
            builder = target.request(MediaType.APPLICATION_JSON_TYPE);
        }else{
            builder = target.request(MediaType.APPLICATION_XML_TYPE);
        }
        
        return builder.header("Authorization", OAUTH_TOKEN_PREFIX+token);
    }
    
    /**
     * eg. https://ap1.salesforce.com/services/data/v29.0
     * 
     * @param instanceUrl
     * @param version
     * @return
     */
    protected String buildBaseUrl(String instanceUrl, String version){
        StringBuilder sb = new StringBuilder();
        return sb.append(instanceUrl).append("/services/data/").append(version).toString();
    }
}
