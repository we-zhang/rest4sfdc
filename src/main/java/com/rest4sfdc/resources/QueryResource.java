package com.rest4sfdc.resources;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.rest4sfdc.Invoker;
import com.rest4sfdc.RequestConfig;
import com.rest4sfdc.ResponseFormat;
import com.rest4sfdc.utility.Asserts;

/**
 *  Query resource to execute a SOQL query that returns all the results in a single response, 
 *  or if needed, returns part of the results and an identifier used to retrieve the remaining results. <p>
 *  
 *  Resource Url: <br>
 *  <code>https://{instance_url}/services/data/{version}/query/?q={SOQL query}</code> 
 *  <br> or <br>
 *  <code>https://{instance_url}/services/data/{version}/queryAll/?q={SOQL query} </code>
 *  
 * 
 * @author Wenchun
 *
 */
public class QueryResource extends WebResource {
    
    private Invoker invoker;
    private ResponseFormat mediaType;

    /**
     * @param invoker
     */
    public QueryResource(Invoker invoker) {
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
     * Build base Url of query. <br>
     * eg. https://na1.salesforce.com/services/data/{version}/query/ <br>
     * 
     * @return
     */
    public String buildQueryUrl(String instanceUrl, String version){
        return this.buildBaseUrl(instanceUrl, version)+"/query";
    }
    
    /**
     * Build base Url of queryAll. <br>
     * eg. https://na1.salesforce.com/services/data/{version}/queryAll/ <br>
     * 
     * @return
     */
    public String buildQueryAllUrl(String instanceUrl, String version){
        return this.buildBaseUrl(instanceUrl, version)+"/queryAll";
    }
    
    /**
     * Query the first batch records based on the SOQL query string. If the records amount is large, client need check the <code>"nextRecordsUrl"</code>
     * attribute in return result and using {@link #queryMore(String)} to fetch the remaining records. 
     * 
     * <p>
     * 
     * Resource url:  .../{version}/query/?q={SOQL Query}
     * @param queryString SOQL
     * @param wantDeletedRecords true, will invoke <code>queryAll</code> to get records including deleted records
     * @return string first batch result
     */
    public String firstQuery(String queryString, boolean wantDeletedRecords){
        return firstQuery(queryString, mediaType, wantDeletedRecords);
    }
    
    /**
     * 
     * @param queryString
     * @param rspFormat
     * @see {@link #firstQuery(String)}
     * @return
     */
    public String firstQuery(String queryString, ResponseFormat rspFormat, boolean wantDelRecords){
        Asserts.notNull(queryString, "the SOQL string can't be null in fristQuery() method");
        if(wantDelRecords){
            return internalQuery(queryString, rspFormat, "queryAll");
        }else{
            return internalQuery(queryString, rspFormat, "query");
        }
    }
    
    /**
     * Query remaining records based on the returned url cursor in {@link #firstQuery(String)}.
     * If records amount is large, client need check <code>"nextRecordsUrl"</code> attribute in returned result
     * and invoke this method circularly to fetch remaining records.
     * 
     * <p>
     * 
     * Resource url:  .../{version}/query/{next records url}
     * @param nextRecordsUrl 
     *        eg. /services/data/{version}/query/01gD0000002HU6KIAW-2000
     * @return
     */
    public String queryMore(String nextRecordsUrl){
        return queryMore(nextRecordsUrl, mediaType);
    }
    
    /**
     * 
     * @param nextRecordsUrl
     * @param rspFormat
     * @see {@link #queryMore(String)}
     * @return
     */
    public String queryMore(String nextRecordsUrl, ResponseFormat rspFormat){
        Asserts.notNull(nextRecordsUrl, "the \"nexteRecordsUrl\" can't be null in queryMore().");
        
        Client client = invoker.getRequestClient();
        RequestConfig config = invoker.getRequestConfig();
        
        String sufix = nextRecordsUrl.substring(nextRecordsUrl.lastIndexOf("//"));
        
        System.out.println("sufix is:  " + sufix);
        
        WebTarget webTarget = client.target(this.buildQueryUrl(config.getInstanceUrl(), config.getApiVersion()))
                   .path(sufix);
        
        Builder builder = this.constructBuilder(rspFormat, webTarget, config.getAccessToken());
        Response response = builder.get();
        
        if(response!=null){
            return response.readEntity(String.class);
        }else{
            return null;
        }
    }
    
    private String internalQuery(String queryString, ResponseFormat rspFormat, String keyword){
        Client client = invoker.getRequestClient();
        RequestConfig config = invoker.getRequestConfig();
        
        WebTarget webTarget = client.target(this.buildBaseUrl(config.getInstanceUrl(), config.getApiVersion()))
            .path(keyword).queryParam("q", queryString);

        Builder builder = this.constructBuilder(rspFormat, webTarget, config.getAccessToken());
        Response response = builder.get();
        
        if(response!=null){
            return response.readEntity(String.class);
        }else{
            return null;
        }
    }
    
}
