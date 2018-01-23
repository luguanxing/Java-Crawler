package log4j;

import org.apache.log4j.Logger;

public class Demo {

	//获取logger实例
	public static Logger logger = Logger.getLogger(Demo.class);
	
	public static void main(String[] args) {
		//输出控制台信息
		logger.debug("这是一条 debug信息");
		logger.info("这是一条 info信息");
		logger.warn("这是一条 warn信息");
		logger.error("这是一条 error信息");
		logger.fatal("这是一条 fatal信息");
		//输出信息同时输出错误
		try {
			int i = 1 / 0;
		} catch (Exception e) {
			logger.error("测试错误1/0", e);
		}

	}
	
}
