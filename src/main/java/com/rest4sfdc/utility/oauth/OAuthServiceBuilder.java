package com.rest4sfdc.utility.oauth;

/**
 * @author Wenchun
 *
 */
public class OAuthServiceBuilder {
    
    private OAuthConfig config;
    
    public OAuthServiceBuilder(){
        this.config = new OAuthConfig();
    }

    public OAuthServiceBuilder apiKey(String apiKey){
        this.config.setConsumerKey(apiKey);
        return this;
    }
    
    public OAuthServiceBuilder apiSecret(String apiSecret){
        this.config.setConsumerSecret(apiSecret);
        return this;
    }
    
    public OAuthServiceBuilder redirectUrl(String redirectUrl){
        this.config.setRedirectUrl(redirectUrl);
        return this;
    }
    
    public OAuthService build(){
        return new OAuthService(config);
    }
}
