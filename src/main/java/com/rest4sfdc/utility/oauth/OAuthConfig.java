package com.rest4sfdc.utility.oauth;

/**
 * @author Wenchun
 * 
 */
public class OAuthConfig {

    private String consumerKey;
    private String consumerSecret;
    
    // for step 1
    private String responseType;
    private String redirectUrl;
    
    // for step2
    private String grantType;
    private String oauthrizeCode;
    
    public OAuthConfig(){
        this.responseType = "code";
        this.grantType = "authorization_code";
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getOauthrizeCode() {
        return oauthrizeCode;
    }

    public void setOauthrizeCode(String oauthrizeCode) {
        this.oauthrizeCode = oauthrizeCode;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }
    
}
