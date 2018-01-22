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

		// 根据条件选择器
		Document document = Jsoup.parse(html);
		Elements elements_links = document.select("#post_list .post_item .post_item_body h3 a");
		for (Element element_link : elements_links) {
			System.out.println(element_link.text() + ":");
			System.out.println(element_link.attr("href"));
			System.out.println();
		}
		
		Elements elements_imgs = document.select("img[alt]").select("img[src$=.png]");
		for (Element element_img : elements_imgs) {
			System.out.println(element_img.toString());
			System.out.println("替代文本="+element_img.attr("alt"));
			System.out.println("链接="+element_img.attr("src"));
			System.out.println();
		}
		
		Element element_friendLink = document.select("#friend_link").first();
		System.out.println("纯文本:\n" + element_friendLink.text() + "\n");
		System.out.println("内部DOM元素:\n" + element_friendLink.html() + "\n");
		
		response.close();
		httpClient.close();
	}
}
