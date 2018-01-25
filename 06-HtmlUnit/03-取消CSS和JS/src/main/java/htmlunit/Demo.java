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
		//实例化web客户端,并设置浏览器请求头
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		
		//可以取消CSS和JS的执行，提高速度
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		
		//解析获取页面
		try {
			HtmlPage htmlPage = webClient.getPage("https://www.bilibili.com/");
			System.out.println(htmlPage.asXml());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}
	}
	
}
