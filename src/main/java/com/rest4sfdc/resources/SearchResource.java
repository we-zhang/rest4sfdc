package com.rest4sfdc.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.rest4sfdc.Invoker;
import com.rest4sfdc.RequestConfig;
import com.rest4sfdc.ResponseFormat;
import com.rest4sfdc.utility.Asserts;

/**
 * Use the Search resource to execute a SOSL search.
 * 
 * @author Wenchun
 *
 */
public class SearchResource extends WebResource {

    private Invoker invoker;
    private ResponseFormat mediaType;

    /**
     * @param invoker
     */
    public SearchResource(Invoker invoker) {
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
     * Execute SOSL to get search result.<br>
     * 
     * Resource Url: .../{version}/search/?q={SOSL search string}
     * 
     * @param sosl
     * @return
     * @throws Exception 
     */
    public String search(String sosl) throws Exception{
        return search(sosl, mediaType);
    }
    
    public String search(String sosl, ResponseFormat rspFormat) throws Exception{
        Asserts.notNull(sosl, "SOSL search string can't be null in search() method.");
        
        Client client = invoker.getRequestClient();
        RequestConfig config = invoker.getRequestConfig();
        
        // replace {} in sosl string with url encoded type because Jersey will
        // treat {} as template parameter
        sosl = sosl.replace("{", "%7B").replace("}", "%7D");
        
        WebTarget webTarget = client.target(this.buildBaseUrl(config.getInstanceUrl(), config.getApiVersion()))
            .path("search").queryParam("q", sosl);
        
        System.out.println(webTarget.getUri().toASCIIString());
        
        Builder builder = this.constructBuilder(mediaType, webTarget, config.getAccessToken());
        Response response = builder.get();
        
        if(response!=null){
            return response.readEntity(String.class);
        }else{
            return null;
        }
    }
    
}
