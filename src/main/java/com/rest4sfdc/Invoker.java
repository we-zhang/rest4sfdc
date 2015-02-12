package com.rest4sfdc;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import com.rest4sfdc.resources.ObjectsResource;
import com.rest4sfdc.resources.QueryResource;
import com.rest4sfdc.resources.SearchResource;

/**
 * @author Wenchun
 * @date Apr 10, 2014
 * @version
 */
public class Invoker {
    
    private RequestConfig config;
    
    private Client requestClient;
    
    public static Invoker newInstance(){
        return new Invoker();
    }

    public static Invoker newInstance(RequestConfig config){
       return new Invoker(config);
    }
    
    private Invoker(){
        this.requestClient = ClientBuilder.newClient();
    }
    
    private Invoker(RequestConfig config){
        this.config = config;
        this.requestClient = ClientBuilder.newClient();
    }
    
    public void setRequestConfig(RequestConfig config){
        this.config = config;
    }
    
    public RequestConfig getRequestConfig(){
        return this.config;
    }
    
    public Client getRequestClient(){
        return this.requestClient;
    }
    
    public ObjectsResource buildObjectsResources(){
        return new ObjectsResource(this);
    }
    
    public QueryResource buildQueryResource(){
        return new QueryResource(this);
    }
    
    public SearchResource buildSearchResource(){
        return new SearchResource(this);
    }
    
}
