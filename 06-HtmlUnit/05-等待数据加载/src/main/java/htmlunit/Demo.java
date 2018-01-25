package htmlunit;

import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Demo {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		//实例化web客户端,并设置浏览器请求头
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		
		//获取页面并输入等待加载完成
		try {
			//获取页面元素
			HtmlPage htmlPage = webClient.getPage("https://pan.baidu.com/share/home?uk=305605848#category/type=0");
			//等待脚本将数据加载
			Thread.sleep(2000);
			//输出完成数据加载后的页面
			System.out.println(htmlPage.asXml());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}
	}
	
}
