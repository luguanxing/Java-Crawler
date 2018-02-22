package blogCrawler.crawler;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import blogCrawler.entity.Article;
import blogCrawler.utils.DateUtil;
import blogCrawler.utils.DbUtil;
import blogCrawler.utils.PropertiesUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Status;

public class CnBlogsCrawler {

	private static Logger logger = Logger.getLogger(CnBlogsCrawler.class);

	private static final String URL = "https://www.cnblogs.com/";

	private static Connection con = null;

	private static CacheManager manager = null; // cache管理器

	private static Cache cache = null; // cache缓存对象

	// 爬取主页
	private static String getIndexPage() {
		logger.info("开始爬取主页" + URL);
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try {
			// 创建httpClient客户端实例
			httpClient = HttpClients.createDefault();
			// 设置请求服务器的配置
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
					String html = EntityUtils.toString(entity, "utf-8");
					return html;
				} else {
					logger.error("访问主页返回状态错误");
					return null;
				}
			} else {
				logger.error("获取主页内容为空");
				return null;
			}
		} catch (Exception e) {
			logger.error("访问主页出错", e);
			e.printStackTrace();
			return null;
		} finally {
			logger.info("结束爬取主页" + URL);
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

	// 根据主页内容解析出主页的博客链接
	private static List<String> parseIndexPage(String indexHtml) {
		logger.info("开始解析主页");
		if (indexHtml == null || indexHtml.isEmpty())
			return null;
		// 根据条件选择器
		List<String> links = new ArrayList();
		Document document = Jsoup.parse(indexHtml);
		Elements elements_links = document.select("#post_list .post_item .post_item_body h3 a");
		for (Element element_link : elements_links) {
			String title = element_link.text();
			String href = element_link.attr("href");
			if (cache.get(href) != null) { // 如果缓存中存在就不插入
				logger.info(href + "已经在缓存中存在");
				continue;
			}
			links.add(href);
		}
		logger.info("结束解析主页");
		return links;
	}

	// 根据博客链接爬取博客页面内容
	private static String getBlogPage(String href) {
		logger.info("开始爬取博客" + href);
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
			HttpGet httpGet = new HttpGet(href);
			httpGet.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			httpGet.setConfig(config);
			// 执行httpGet请求实例获取返回报文
			response = httpClient.execute(httpGet);
			if (response != null) {
				// 获取报文内容获取信息
				HttpEntity entity = response.getEntity();
				if (response.getStatusLine().getStatusCode() == 200) {
					String html = EntityUtils.toString(entity, "utf-8");
					return html;
				} else {
					logger.error("访问博客" + href + "返回状态错误");
					return null;
				}
			} else {
				logger.error("获取博客" + href + "内容为空");
				return null;
			}
		} catch (Exception e) {
			logger.error("访问博客" + href + "出错", e);
			e.printStackTrace();
			return null;
		} finally {
			logger.info("结束爬取博客" + href);
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

	// 根据博客内容解析博客文章、标题、地址、内容和图片
	private static Article parseBlogPage(String blogHtml) {
		logger.info("开始解析博客");
		if (blogHtml == null || blogHtml.isEmpty())
			return null;
		// 根据条件选择器
		try {
			Article article = new Article();
			Document document = Jsoup.parse(blogHtml);
			String href = document.select("#cb_post_title_url").first().attr("href");
			String title = document.select("#cb_post_title_url").first().text();
			String content = document.select("#cnblogs_post_body").first().html();
			Elements imgElements = document.select("#cnblogs_post_body").select("img");
			List<String> imageList = new ArrayList();
			for (Element imgElement : imgElements)
				imageList.add(imgElement.attr("src"));
			article.setImageList(imageList);
			article.setContent(content);
			article.setOrUrl(href);
			article.setTitle(title);
			return article;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("解析博客出错", e);
		}
		return null;
	}

	//将一篇解析后的文章存入数据库并本地化图片
	public static void saveBlog(Article article) {
		//保存图片并替换内容
		Map<String, String> map = saveImagesToLocal(article.getImageList());
		for (String url : map.keySet()) { 
			String newUrl = map.get(url);
			System.out.println(url + "替换为");
			System.out.println(newUrl);
			System.out.println();
			article.setContent(article.getContent().replaceAll(url, newUrl));
		}
		// 插入数据库
		String sql = "insert into t_article values(null,?,?,null,now(),0,0,null,?,0,null)";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, article.getTitle());
			pstmt.setString(2, article.getContent());
			pstmt.setString(3, article.getOrUrl());
			if (pstmt.executeUpdate() == 1) {
				logger.info(article.getOrUrl() + "-成功插入数据库");
				cache.put(new net.sf.ehcache.Element(article.getOrUrl(), "ok")); // 放进缓存，避免以后重复
			} else {
				logger.info(article.getOrUrl() + "-插入数据库失败");
			}
		} catch (Exception e) {
			// 很有可能字符串中有非UTF8乱码导致出错
			logger.error("数据库保存出错", e);
			e.printStackTrace();
		}
	}
	
	//保存图片到本地并替换名称
	public static Map<String, String> saveImagesToLocal(List<String> imageList) {
		Map<String, String> map = new HashMap();
		
		for (String imgUrl : imageList) {
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
				HttpGet httpGet = new HttpGet(imgUrl);
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
							//图片新名称不实用uuid因为能从名称追踪到源网站图片的url
							List<String> urlNames = Arrays.asList(imgUrl.split("/"));
							String imageName = urlNames.get(urlNames.size()-1);
							String newImgUrl = PropertiesUtil.getValue("imageFilePath") + "/" + DateUtil.getCurrentDatePath() + "/" + imageName;
							FileUtils.copyToFile(inputStream, new File(newImgUrl));
							map.put(imgUrl, newImgUrl);
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
		
		return map;
	}
	
	//初始化
	public static void init() {
		try {
			// 初始化获取缓存
			String classPath = CnBlogsCrawler.class.getResource("/").toString();
			String cacheXmlPath = classPath + "ehcache.xml";
			if (cacheXmlPath.startsWith("file:/")) {
				cacheXmlPath = cacheXmlPath.substring(6);
			}
			manager = CacheManager.create(cacheXmlPath);
			cache = manager.getCache("cnblog");
			// 初始化获取数据库链接
			DbUtil dbUtil = new DbUtil();
			con = dbUtil.getCon();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化出错", e);
		}
	}
	
	// 抓取cnblog主页上的博客内容并存入数据库并下载图片
	public static void getIndexPageBlogs() {
		// 获取主页内容
		String indexHtml = getIndexPage();
		// 解析主页内容获取当前所有的博客链接
		List<String> links = parseIndexPage(indexHtml);
		// 获取所有博客链接的博客内容
		for (String link : links) {
			String html = getBlogPage(link);
			Article article = parseBlogPage(html);
			if (article != null) {
				saveBlog(article);
			}
		}
		// 把缓存写入文件
		if (cache.getStatus() == Status.STATUS_ALIVE) {
			cache.flush();
		}
		manager.shutdown();
	}
	
	public static void main(String[] args) {
		init();
		getIndexPageBlogs();
	}

}
