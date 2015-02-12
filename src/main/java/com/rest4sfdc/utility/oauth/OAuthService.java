package com.rest4sfdc.utility.oauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Wenchun
 *
 */
public class OAuthService {
    private OAuthConfig config;
    
    private static final String baseOauthUrl = "https://login.salesforce.com/services/oauth2";
    
    private String authorizeUri = "/authorize?response_type=%s&client_id=%s&redirect_uri=%s";
    
    private String tokenUri = "/token?grant_type=%s&code=%s&client_id=%s&client_secret=%s&redirect_uri=%s";
    
    public OAuthService(OAuthConfig config){
        this.config = config;
    }

    /**
     * operation 1.
     * @return
     * @throws UnsupportedEncodingException
     */
    public String genAuthorizeAddress() throws UnsupportedEncodingException{
        
        String uri = String.format(authorizeUri, config.getResponseType(), config.getConsumerKey(), URLEncoder.encode(config.getRedirectUrl(), "utf-8"));
        String url = new StringBuilder().append(baseOauthUrl)
                .append(uri).toString();
        
        return url;
    }
    
    /**
     * operation 2.
     * @param authorizeCode
     */
    public void updateAuthorizeCode(String authorizeCode){
        this.config.setOauthrizeCode(authorizeCode);
    }
    
    public String getAccessToken(String authorizeCode) throws Exception{
        
//        String uri = URLEncoder.encode(, "utf-8");
        String uri = String.format(tokenUri, config.getGrantType(), URLDecoder.decode(authorizeCode, "utf-8"), config.getConsumerKey(), config.getConsumerSecret(), URLEncoder.encode(config.getRedirectUrl(), "utf-8"));
        String url = new StringBuilder().append(baseOauthUrl).append(uri).toString();
        
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        
        connection.connect();
        
        InputStream stream = connection.getInputStream();
        
        return getStreamContents(stream);
    }
    
    private String getStreamContents(InputStream is){
      try
      {
        final char[] buffer = new char[0x10000];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, "UTF-8");
        int read;
        do
        {
          read = in.read(buffer, 0, buffer.length);
          if (read > 0)
          {
            out.append(buffer, 0, read);
          }
        } while (read >= 0);
        in.close();
        return out.toString();
      } catch (IOException ioe)
      {
        throw new IllegalStateException("Error while reading response body", ioe);
      }
    }
}
