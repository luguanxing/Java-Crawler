package htmlunit;

import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Demo {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		//实例化web客户端,并设置浏览器请求头,代理IP等
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		
		//解析获取页面,执行操作
		try {
			HtmlPage htmlPage = webClient.getPage("https://www.cnblogs.com/");
			//根据id查找元素
			DomElement div = htmlPage.getElementById("site_nav_top");
			System.out.println(div.asXml());
			//根据tag获取元素列表
			DomNodeList<DomElement> tags = htmlPage.getElementsByTagName("a");
			for (DomElement tag : tags) {
				System.out.println(tag.asXml());
			}
			//使用XPath获取元素列表
			List<DomNode> items = htmlPage.getByXPath("//div[@id='cate_title_block']/ul/li");
			for (DomNode item : items) {
				System.out.println(item.asXml());
			}
			List<DomNode> item = htmlPage.getByXPath("//div[@id='cate_title_block']/ul/li[1]/a");
			System.out.println(item.get(0).asText());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}
	}
	
}
