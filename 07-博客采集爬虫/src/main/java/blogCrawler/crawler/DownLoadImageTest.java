package blogCrawler.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import blogCrawler.utils.DateUtil;
import blogCrawler.utils.PropertiesUtil;

public class DownLoadImageTest {

	private static Logger logger = Logger.getLogger(DownLoadImageTest.class);

	private static final String link = "http://images2015.cnblogs.com/blog/952033/201705/952033-20170511210141910-342481715.png";

	private static void img(String URL) {
		logger.info("开始爬取图片" + URL);
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try {
			// 创建httpClient客户端实例
			httpClient = HttpClients.createDefault();
			// 设置代理IP
			RequestConfig config = RequestConfig.custom().setConnectTimeout(10000) // 连接到服务器超时时间
					.setSocketTimeout(10000) // 从服务器获取内容超时时间
					.build();
			// 创建httpGet请求实例,设置头信息和其它配置
			HttpGet httpGet = new HttpGet(URL);
			httpGet.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			httpGet.setConfig(config);
			// 执行httpGet请求实例获取返回报文
			response = httpClient.execute(httpGet);
			if (response != null) {
				// 获取报文内容获取信息
				HttpEntity entity = response.getEntity();
				if (response.getStatusLine().getStatusCode() == 200) {
					try {
						InputStream inputStream = entity.getContent();
						String link = entity.getContentType().getValue();
						String ext = link.split("/")[1];
						String uuid = UUID.randomUUID().toString();
						FileUtils.copyToFile(inputStream, new File(PropertiesUtil.getValue("imageFilePath") + "/"
								+ DateUtil.getCurrentDatePath() + "/" + uuid + "." + ext));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					logger.error("访问图片返回状态错误");
				}
			} else {
				logger.error("获取图片内容为空");
			}
		} catch (Exception e) {
			logger.error("访问图片出错", e);
			e.printStackTrace();
		} finally {
			logger.info("结束爬取图片" + URL);
			// 关闭资源
			try {
				if (response != null)
					response.close();
				if (httpClient != null)
					httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		img("http://images2017.cnblogs.com/blog/1141038/201802/1141038-20180212100143999-1749665914.jpg");
	}
}
