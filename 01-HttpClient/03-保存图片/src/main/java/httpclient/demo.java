package httpclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
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
		HttpGet httpGet = new HttpGet("http://img14.360buyimg.com/n0/jfs/t4507/264/556599884/598231/ee82085c/58d161e1Ne1b0e3b3.jpg");
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");

		// 执行httpGet请求实例获取返回报文
		CloseableHttpResponse response = null;
		response = httpClient.execute(httpGet);
		
		// 获取报文内容获取信息
		HttpEntity entity = response.getEntity();
		System.out.println("关键部分:");
		System.out.println("===============");
		System.out.println("Content-Type:" + entity.getContentType().getValue());
		System.out.println("状态结果:" + response.getStatusLine().getReasonPhrase());
		if ("image".equals(entity.getContentType().getValue().split("/")[0])) {
			System.out.println("经确认类型为图片");
		} else {
			System.out.println("不是图片");
		}
		System.out.println("===============");
		if (entity != null) {
			//使用框架把输入流保存到文件
			InputStream inputStream = entity.getContent();
			FileUtils.copyToFile(inputStream, new File("D://test.jpg"));
		}

		// 关闭资源
		response.close();
		httpClient.close();
	}

}
