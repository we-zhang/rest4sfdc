package com.rest4sfdc.resources.test;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;

import com.google.gson.JsonParser;
import com.rest4sfdc.Invoker;
import com.rest4sfdc.RequestConfig;

/**
 * Base test class
 * 
 * @author Wenchun
 *
 */
public class BaseTest {

  protected Invoker testInvoker = null;

  protected JsonParser parser;

  @Before
  public void init() {

    RequestConfig config = new RequestConfig();
    config.setApiVersion("v29.0");
    config.setInstanceUrl("https://ap1.salesforce.com");
    config
        .setAccessToken("00D900000000qFY!AQsAQOlDz405dAsF4.1aCG3dkZEep6pA2tK607hnvo9dfViNrzB5EWTbJl1YXRdvyU3Nuxqy1EC0ZFXNbhIUVoxAP5E3P2Q8");

    if (testInvoker == null) {
      testInvoker = Invoker.newInstance(config);
    }

    parser = new JsonParser();

  }

  protected String getJsonEntity(String path) throws Exception {
    InputStreamReader reader = null;
    StringBuffer sb = new StringBuffer();
    try {
      InputStream stream = this.getClass().getResourceAsStream(path);
      reader = new InputStreamReader(stream);
      int len = 0;
      do {
        char[] buf = new char[512];
        len = reader.read(buf, 0, buf.length);
        if (len > 0)
          sb.append(buf, 0, len);
      } while (len > 0);

      reader.close();
    } catch (Exception e) {
      throw e;
    } finally {
      if (reader != null)
        reader.close();
    }

    return sb.toString();
  }

  @After
  public void clean() {
    testInvoker = null;
    parser = null;
  }
}
