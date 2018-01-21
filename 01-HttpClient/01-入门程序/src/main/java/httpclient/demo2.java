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

public class demo2 {

	public static void main(String[] args) throws Exception {
		// 创建httpClient客户端实例
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 创建httpGet请求实例
		HttpGet httpGet = new HttpGet("http://www.163.com");

		// 执行httpGet请求实例获取返回报文
		CloseableHttpResponse response = null;
		response = httpClient.execute(httpGet);

		// 获取报文内容解析网页源码(转码)
		HttpEntity entity = response.getEntity();
		System.out.println(EntityUtils.toString(entity, "utf-8"));

		// 关闭资源
		response.close();
		httpClient.close();
	}

}
