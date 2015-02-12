package com.rest4sfdc.utility.oauth;

import java.util.Scanner;

/**
 * @author Wenchun
 *
 */
public class Tester {
    
    private static String key = "3MVG9Y6d_Btp4xp4XO1SRPnHM7rJRPgLOOi.XAppH5idS2Jhvg0nAJAvSjZH6X6qC7P2WzQfMTt_p4Ew7tbuf";


    public static void main(String[] args) throws Exception {
        OAuthService service = new OAuthServiceBuilder().apiKey(key)
                .apiSecret("5165714154985794184")
                .redirectUrl("https://tokenacquire.appsp0t.com/rest/2")
                .build();
        
        System.out.println("The oauthorization URL is, copy to broswer to get the authorizationcode: \n" + service.genAuthorizeAddress());
        
        System.out.println("Paste the authorization code:");
        
        Scanner in = new Scanner(System.in);
        
        System.out.println("Oauth response: \n" +service.getAccessToken(in.nextLine()));
    }
    
}
