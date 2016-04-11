package flyad.cx.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class MyHttpClient {
	
	/** 
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果 
     */  
    public void post() {  
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost("http://localhost:8080/myDemo/Ajax/serivceJ.action");  
        // 创建参数队列    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("type", "house"));  
        UrlEncodedFormEntity uefEntity;  
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {  
                    System.out.println("--------------------------------------");  
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
                    System.out.println("--------------------------------------");  
                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    /** 
     * 发送 get请求 
     */  
    public static String get(String url) {  
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = null;
        try {  
            // 创建httpget.    
            HttpGet httpget = new HttpGet(url);  
            System.out.println("executing request " + httpget.getURI());  
            // 执行get请求.    
            CloseableHttpResponse response = httpclient.execute(httpget);  
            try {  
                // 获取响应实体    
                HttpEntity entity = response.getEntity();  
                System.out.println("--------------------------------------");  
                // 打印响应状态    
                System.out.println(response.getStatusLine());  
                if (entity != null) {  
                    // 打印响应内容长度    
                    System.out.println("Response content length: " + entity.getContentLength());  
                    result = EntityUtils.toString(entity);
                    // 打印响应内容    
                    System.out.println("Response content: " + result);
                }  
                System.out.println("------------------------------------");  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
        return result;
    }  
    
    public static void main(String[] args) throws ClientProtocolException, IOException {
    	HashMap map = new HashMap();
    	  map.put("name", "郭强");
    	  map.put("age", new Integer(12));
    	  map.put("date", new Date());
    	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	  try
    	  {
    	   ObjectOutputStream oos = new ObjectOutputStream(baos);
    	   oos.writeObject(map);
    	  }
    	  catch (Throwable e)
    	  {
    	   e.printStackTrace();
    	  }
    	  byte[] data = baos.toByteArray();
    	  ByteArrayInputStream bis = new ByteArrayInputStream(data);
    	  CloseableHttpClient httpclient = HttpClients.createDefault();  
    	  HttpPost httppost = new HttpPost("http://localhost:8080/20160408gome/coupon/status"); 
    	  InputStream in = new ByteArrayInputStream("abcdefg".getBytes());
    	  InputStreamEntity entity = new InputStreamEntity(in);
    	  httppost.setEntity(entity);
    	  httpclient.execute(httppost);
    }
}
