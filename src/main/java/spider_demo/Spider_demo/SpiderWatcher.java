package spider_demo.Spider_demo;
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * 	监控zk中/monitor节点下面子节点的变化情况
 * 
 * 	注意：这个监控功能只需要在一台服务器上运行即可。
 * 
 *
 */
public class SpiderWatcher implements Watcher {
	CuratorFramework client = null;
	List<String> childrenList = new ArrayList<String>();
	public SpiderWatcher() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);//	获取zk链接的一个重试策略
		String zookeeperConnectionString = "192.168.157.100:2181,192.168.157.101:2181,192.168.157.102:2181";//	指定zk的节点信息
		int sessionTimeoutMs = 5000;//	表示session失效时间，当链接关闭后，多长时间链接真正失效。这个值只能在4s~40s之间
		int connectionTimeoutMs = 3000;//	链接超时时间
		this.client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
		client.start();
		
		//添加监控的代码
		try {
			//	注意：这个监视器是单次有效的，想要重复使用，是需要重复注册的
			this.childrenList = client.getChildren().usingWatcher(this).forPath("/monitor");
			//System.out.println("111");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 回调函数--会被程序自动调用
	 * 当监控的节点发生变化的时候，这个函数会被触发(调用)
	 */
	@Override
	public void process(WatchedEvent event) {
		try {
			List<String> newChildrenList = client.getChildren().usingWatcher(this).forPath("/monitor");
			for (String node : childrenList) {
				if(!newChildrenList.contains(node)) {
					System.out.println("节点消失："+node);
					//TODO 给管理员发短信，邮件  【阿里云短信、腾讯云短信。。。】
					//TODO 通过java代码操作linux机器，可以实现自动启动指定机器上面的程序
				}
			}
			
			for (String node : newChildrenList) {
				if(!childrenList.contains(node)) {
					System.out.println("节点新增："+node);
					//TODO 给管理员发短信，邮件  【阿里云短信、腾讯云短信。。。】
				}
			}
			
			//这一步一定不能忘
			this.childrenList = newChildrenList;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			;
		}
	}
	
	public static void main(String[] args) {
		SpiderWatcher spiderWatcher = new SpiderWatcher();
		spiderWatcher.run();
	}


}
