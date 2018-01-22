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

		// 根据不同条件获取DOM元素
		Document document = Jsoup.parse(html);
		Elements element_items = document.getElementsByClass("post_item");
		for (Element element_item : element_items) {
			System.out.println("==============");
			System.out.println(element_item.html());
			System.out.println("==============");
		}
		
		Elements elements_attrs = document.getElementsByAttribute("width");
		for (Element element : elements_attrs) {
			System.out.println("---------");
			System.out.println(element.toString());
			System.out.println("---------");
		}
		
		Elements elements_attr_values = document.getElementsByAttributeValue("target", "_blank");
		for (Element element : elements_attr_values) {
			System.out.println("__________");
			System.out.println(element.toString());
			System.out.println("__________");
		}

		response.close();
		httpClient.close();
	}
}
