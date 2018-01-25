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
		
		//获取页面并输入数据模拟点击
		try {
			//获取页面元素
			HtmlPage htmlPage = webClient.getPage("http://blog.java1234.com/index.html");
			HtmlForm htmlForm = htmlPage.getFormByName("myform");
			HtmlInput textField = htmlForm.getInputByName("q");
			HtmlInput button = htmlForm.getInputByName("submitButton");
			//模拟填入数据和提交,获取查询结果
			textField.setValueAttribute("htmlunit");
			HtmlPage resultPage = button.click();
			//查看输出结果
			System.out.println(resultPage.asXml());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}
	}
	
}
