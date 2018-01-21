package httpclient;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class demo {

	public static void main(String[] args) {
		//创建httpClient客户端实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		//创建httpGet请求实例
		HttpGet httpGet = new HttpGet("http://www.163.com");
		
		//执行httpGet请求实例获取返回报文
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//获取报文内容解析网页源码(转码)
		HttpEntity entity = response.getEntity();
		try {
			System.out.println(EntityUtils.toString(entity, "utf-8"));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//关闭资源
		try {
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
