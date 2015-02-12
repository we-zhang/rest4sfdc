package com.rest4sfdc;

/**
 * Basic request config parameter for accessing Salesforce server.<p>
 * 
 * <code>accessToken:</code> Oauth 2 access token;<br>
 * <code>apiVersion:</code> api version of Salesforce server; <br>
 * <code>instanceUrl:</code> the url of Salesforce server instance, such as https://na1.saleforce.com/.
 * 
 * @author Wenchun
 *
 */
public class RequestConfig {

    private String accessToken;
    private String apiVersion;
    private String instanceUrl;
    
    public RequestConfig(){}
    
    public RequestConfig(String accToken, String version, String instanceUrl){
        this.accessToken = accToken;
        this.apiVersion = version;
        this.instanceUrl = instanceUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    public void setInstanceUrl(String instanceUrl) {
        this.instanceUrl = instanceUrl;
    }
}
