package com.rest4sfdc.resources.test;

import java.net.URLEncoder;

import org.junit.Test;

import com.rest4sfdc.resources.SearchResource;

/**
 * @author Wenchun
 *
 */
public class SearchResourceTest extends BaseTest {
    
    private SearchResource search;
    
    /**
     * (non-Javadoc)
     * @see com.rest4sfdc.resources.test.BaseTest#init()
     */
    @Override
    public void init() {
        super.init();
        search = testInvoker.buildSearchResource();
    }
    
    @Test
    public void testSearch() throws Exception{
        System.out.println(URLEncoder.encode("FIND {sfdc} IN Name Fields RETURNING Account (name,billingstreet)", "utf-8"));
        String string = search.search("FIND {sfdc} IN Name Fields RETURNING Account (name,billingstreet)");
        
        System.out.println(string);
    }

    
    /**
     * (non-Javadoc)
     * @see com.rest4sfdc.resources.test.BaseTest#clean()
     */
    @Override
    public void clean() {
        super.clean();
        search = null;
    }
}
