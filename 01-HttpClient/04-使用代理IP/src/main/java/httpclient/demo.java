package httpclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class demo {

	public static void main(String[] args) throws Exception {
		// 创建httpClient客户端实例
		CloseableHttpClient httpClient = HttpClients.createDefault();

		//设置代理IP
		HttpHost proxy = new HttpHost("112.255.5.52", 8118);
		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();	
		
		// 创建httpGet请求实例,设置头信息和代理IP
		HttpGet httpGet = new HttpGet("http://www.ip.cn/");
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
		httpGet.setConfig(config);
		
		// 执行httpGet请求实例获取返回报文
		CloseableHttpResponse response = null;
		response = httpClient.execute(httpGet);
		
		// 获取报文内容获取信息
		HttpEntity entity = response.getEntity();
		System.out.println(EntityUtils.toString(entity, "gb2312"));
		
		// 关闭资源
		response.close();
		httpClient.close();
	}

}
