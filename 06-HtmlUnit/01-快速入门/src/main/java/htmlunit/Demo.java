package htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Demo {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		//实例化web客户端,并设置浏览器请求头
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		
		//解析获取页面,使用完成后关闭
		try {
			HtmlPage htmlPage = webClient.getPage("http://www.taobao.com");
			System.out.println("===================");
			System.out.println("网页html");
			System.out.println(htmlPage.asXml());
			System.out.println("===================");
			System.out.println("===================");
			System.out.println("网页文本");
			System.out.println(htmlPage.asText());
			System.out.println("===================");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}
	}
	
}
