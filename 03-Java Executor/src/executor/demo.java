package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class demo {

	public static Integer count = 1;

	public static boolean flag = true;

	public static void main(String[] args) throws Exception {
		// 创建连接池，包含10个线程
		ExecutorService executorService = Executors.newFixedThreadPool(20);

		while (flag) {
			// 分配任务，如已分配完则检查是否都完成了
			if (count <= 100) {
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						System.out.println("执行第" + count + "个任务");
						count++;
					}
				});
			} else {
				if (((ThreadPoolExecutor) executorService).getActiveCount() == 0) {
					executorService.shutdown();
					System.out.println("全部都完成了");
					flag = false;
				}
			}
			// 要延迟把任务添加到队列的时间，否则会导致任务队列太长与判断不同步，需要加锁导致性能更低
			Thread.sleep(100);
		}

	}

}
