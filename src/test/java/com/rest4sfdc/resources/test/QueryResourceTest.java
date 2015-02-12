package com.rest4sfdc.resources.test;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rest4sfdc.resources.QueryResource;

/**
 * @author Wenchun
 *
 */
public class QueryResourceTest extends BaseTest{

    QueryResource query;
    
    @Before
    public void init(){
        super.init();
        query = testInvoker.buildQueryResource();
    }
    
    @Test
    public void queryTest(){
        String resposne = query.firstQuery("select id, name from account limit 1", false);
        checkResult(resposne);
        
        String resposne2 = query.firstQuery("select id, name from account limit 1", true);
        checkResult(resposne2);
    }
    
    private void checkResult(String response){
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response);
        
        if(!(jsonElement instanceof JsonObject)){
            Assert.assertTrue(jsonElement.toString(), false);
            return;
        }
        
        JsonObject object = (JsonObject) jsonElement;
        JsonElement sizeEle = object.get("totalSize");
        Assert.assertEquals(1, sizeEle.getAsInt());
    }
    
    @After
    public void clean(){
        super.clean();
        query = null;
    }
}
