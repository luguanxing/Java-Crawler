package jsoup;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class demo {
	public static void main(String[] args) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("https://www.cnblogs.com/");
		CloseableHttpResponse response = null;
		response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		String html = EntityUtils.toString(entity, "utf-8");

		// 解析网页内容
		Document document = Jsoup.parse(html);
		Elements elements = document.getElementsByTag("title");
		Element element_title = elements.get(0);
		String title = element_title.text();
		System.out.println("网页标题为: " + title);
		Element element_top = document.getElementById("site_nav_top");
		String top = element_top.text();
		System.out.println("网页口号为: " + top);

		response.close();
		httpClient.close();
	}
}
