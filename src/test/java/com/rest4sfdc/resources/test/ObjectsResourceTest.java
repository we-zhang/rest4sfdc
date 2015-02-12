package com.rest4sfdc.resources.test;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.rest4sfdc.resources.ObjectEntity;
import com.rest4sfdc.resources.ObjectsResource;

/**
 * @author Wenchun
 *
 */
public class ObjectsResourceTest extends BaseTest {
    
    private ObjectsResource res;
    
    @Before
    public void init(){
        super.init();
        res = testInvoker.buildObjectsResources();
    }

    @Test
    public void testGetObjects(){
        String response = res.getObjects();
        
        Assert.assertNotNull(response);
        
        JsonElement ele =  parser.parse(response);
        if(ele instanceof JsonArray){
            System.out.println(ele.toString());
            Assert.assertFalse(true);
            return;
        }
        
        JsonObject ob = (JsonObject) ele;
        JsonElement sele = ob.get("sobjects");
        Assert.assertNotNull(sele);
    }
    
    @Test
    public void testGetObjectBasicMetadata(){
        String response = res.getObjectBasicMetadata("Account");
        
        Assert.assertNotNull(response);
        
        JsonElement ele =  parser.parse(response);
        if(ele instanceof JsonArray){
            System.out.println(ele.toString());
            Assert.assertFalse(true);
            return;
        }
        
        JsonObject ob = (JsonObject) ele;
        JsonElement sele = ob.get("objectDescribe");
        Assert.assertNotNull(sele);
    }
    
    @Test
    public void testGetObjectAllMetadata(){
        String metadata = res.getObjectAllMetadata("Account");
        
        Assert.assertNotNull(metadata);
        
        JsonElement ele =  parser.parse(metadata);
        if(ele instanceof JsonArray){
            System.out.println(ele.toString());
            Assert.assertFalse(true);
            return;
        }
        
        JsonElement nameob = ((JsonObject)ele).get("name");
        Assert.assertNotNull(nameob);
        
        Assert.assertEquals(nameob.toString(), "Account", nameob.getAsString());
    }
    
    @Test
    public void testCreateUpdataAndDeleteObject() throws Exception{
        String datapath = "/com/rest4sfdc/resources/test/data/account.json";
        
        String response = res.createObject("Account", getJsonEntity(datapath));
        Assert.assertNotNull(response);
        
        JsonElement element = parser.parse(response);
        if(element instanceof JsonArray){
            Assert.assertTrue(element.toString(), false);
        }else{
            //must be jsonObject
            JsonObject object = (JsonObject)element;
            JsonElement succ = object.get("success");
            JsonElement id = object.get("id");
            
            Assert.assertTrue(succ.getAsBoolean());
            
            //update record
            this.updateObject("Account", id.getAsString());
            
            //try to delete the dummy record
            String delStr = res.deleteObject("Account", id.getAsString());
            Assert.assertEquals("",delStr); //none returned for Salesforce, see http://www.salesforce.com/us/developer/docs/api_rest/index.htm
        }
        
        // test file entiry
       this.createObjectWithFile(datapath);
    }
    
    private void createObjectWithFile(String fielPath){
        
        String response2 = res.createObject("Account",  new File(this.getClass().getResource(fielPath).getFile()));
        Assert.assertNotNull(response2);
        
        JsonElement element2 = parser.parse(response2);
        if(element2 instanceof JsonArray){
            System.out.println(element2.toString());
            Assert.assertTrue(false);
        }else{
            //must be jsonObject
            JsonObject object = (JsonObject)element2;
            JsonElement succ = object.get("success");
            JsonElement id = object.get("id");
            
            Assert.assertTrue(succ.getAsBoolean());
            
            //try to delete
            String delStr = res.deleteObject("Account", id.getAsString());
            Assert.assertEquals(delStr, "",delStr); //none returned for Salesforce, see http://www.salesforce.com/us/developer/docs/api_rest/index.htm
        }
    }
    
    private void updateObject(String obType, String id){
        ObjectEntity record = new ObjectEntity("Account");
        record.setId(id);
        record.addField("BillingCity", new JsonPrimitive("San Francisco"));
        String updateStr = res.updateObject(record);
        
        // none returned if success
        Assert.assertEquals(updateStr, "", updateStr);
    }
    
    @Test
    public void testGetObjectFields() throws Exception{
        String datapath = "/com/rest4sfdc/resources/test/data/account.json";
        String createString = res.createObject("Account", getJsonEntity(datapath));
        
        JsonElement ele = parser.parse(createString);
        if(!(ele instanceof JsonObject)){
            throw new IllegalStateException("create dummy record failed when testing getObjectFields()");
        }
        
        JsonObject ob = (JsonObject) ele;
        JsonElement idEle = ob.get("id");
        String id = idEle.getAsString();
        
        
        // get all fields
        String response = res.getObjectFields("Account", id);
        
        JsonElement getEle = parser.parse(response);
        if(!(getEle instanceof JsonObject)){
            Assert.assertTrue(getEle.toString(), false);
            return;
        }
        
        //must be json object
        JsonObject getob = (JsonObject) getEle;
        JsonElement getidEle = getob.get("Id");
        String returnedId = getidEle.getAsString();
        Assert.assertEquals(returnedId,18,returnedId.length()); //19 chars..
        
        
        // get some fields
        String response2 = res.getObjectFields("Account", id, "Name", "","BillingCity");
        parser.parse(response2);
        JsonElement getEle2 = parser.parse(response);
        if(!(getEle instanceof JsonObject)){
            Assert.assertTrue(getEle2.toString(), false);
            return;
        }
        
        JsonObject getob2 = (JsonObject) getEle;
        JsonElement getNameEle = getob2.get("Name");
        String name = getNameEle.getAsString();
        Assert.assertEquals(name, "rest4sfdcTestAccount", name);
        
        //delete test record
        res.deleteObject("Account", returnedId);
    }
    
    @After
    public void clean(){
        super.clean();
        res = null;
    }
    
}
