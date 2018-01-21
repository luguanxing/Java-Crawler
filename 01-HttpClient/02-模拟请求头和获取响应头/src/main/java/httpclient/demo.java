package httpclient;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class demo {

	public static void main(String[] args) throws Exception {
		// 创建httpClient客户端实例
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 创建httpGet请求实例,设置头信息
		HttpGet httpGet = new HttpGet("http://www.bilibili.com");
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");

		// 执行httpGet请求实例获取返回报文
		CloseableHttpResponse response = null;
		response = httpClient.execute(httpGet);
		Header[] allHeaders = response.getAllHeaders();
		System.out.println("响应报头:");
		System.out.println("===============");
		for (Header header : allHeaders) {
			System.out.println(header);
		}
		System.out.println("===============");
		System.out.println();

		// 获取报文内容获取信息
		HttpEntity entity = response.getEntity();
		System.out.println("关键部分:");
		System.out.println("===============");
		System.out.println("Status:" + response.getStatusLine());
		System.out.println("状态数值:" + response.getStatusLine().getStatusCode());
		System.out.println("状态结果:" + response.getStatusLine().getReasonPhrase());
		System.out.println("Content-Type:" + entity.getContentType().getValue());
		System.out.println("===============");

		// 关闭资源
		response.close();
		httpClient.close();
	}

}
