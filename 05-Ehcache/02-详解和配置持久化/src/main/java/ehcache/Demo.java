package ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class Demo {

	public static void main(String[] args) {
		// 根据配置文件创建CacheManager
		CacheManager cacheManager = CacheManager.create("./src/main/resources/ehcache.xml");
		
		//获取指定cache
		Cache cache = cacheManager.getCache("saveToDisk");
		
		//存取元素,刷新缓存并关闭CacheManager
		Element element_put = new Element("123", "hello world");
		cache.put(element_put);
		Element element_get = cache.get("123");
		System.out.println(element_get);
		System.out.println(element_get.getValue());
		cache.flush();
		cacheManager.shutdown();
	}
	
}
