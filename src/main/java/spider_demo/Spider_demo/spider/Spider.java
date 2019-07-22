package spider_demo.Spider_demo.spider;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;

import spider_demo.Spider_demo.domain.Page;
import spider_demo.Spider_demo.download.DownLoad;
import spider_demo.Spider_demo.download.DownLoad_HttpClientimpl;
import spider_demo.Spider_demo.process.Proce;
import spider_demo.Spider_demo.process.proce_JDimpl;
import spider_demo.Spider_demo.repository.Redis_goods_Repository;
import spider_demo.Spider_demo.repository.Repository;
import spider_demo.Spider_demo.store.Mysql_storeimpl;
import spider_demo.Spider_demo.store.Storeable;

public class Spider {
	
	public Spider() {
		
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);//	获取zk链接的一个重试策略
		String zookeeperConnectionString = "192.168.157.100:2181,192.168.157.101:2181,192.168.157.102:2181";//	指定zk的节点信息
		int sessionTimeoutMs = 5000;//	表示session失效时间，当链接关闭后，多长时间链接真正失效。这个值只能在4s~40s之间
		int connectionTimeoutMs = 3000;//	链接超时时间
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
		client.start();
		
		try {
			//			获取当前机器IP
			InetAddress localHost = InetAddress.getLocalHost();
			String ip = localHost.getHostAddress();
			
			client.create()
				.creatingParentsIfNeeded()//	如果父节点不存在，则创建
				.withMode(CreateMode.EPHEMERAL)//	临时节点
				.withACL(Ids.OPEN_ACL_UNSAFE)
				.forPath("/monitor/"+ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static DownLoad download = null;//new DownLoad_HttpClientimpl();

	private static Proce proce = null; //new proce_JDimpl();

	private static Repository repo = new Redis_goods_Repository();

	private static Storeable stor = new Mysql_storeimpl();

	private static Logger logger = LoggerFactory.getLogger(Spider.class);
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(5);

	public Page download(String url) {
		return download.download(url);
	}

	public String gettitle(Page page) {
		return page.getTitle();
	}

	public String getprice(Page page) {
		return page.getPrice();
	}

	public String getimgurl(Page page) {
		return "https:" + page.getImgUrl();
	}

	public String getGoodsId(Page page) {
		return page.getGoodsId();
	}

	public JSONArray getsecinfor(Page page) {
		JSONArray json = new JSONArray();
		Map<String, String> hashmap = page.getHashmap();

		Set<Entry<String, String>> entrySet = hashmap.entrySet();

		Iterator<Entry<String, String>> iterator = entrySet.iterator();

		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			json.add(next);
		}

		return json;
	}

	public DownLoad getDownload() {
		return download;
	}

	public void setDownload(DownLoad download) {
		this.download = download;
	}

	public Proce getProce() {
		return proce;
	}

	public void setProce(Proce proce) {
		this.proce = proce;
	}
	
	public static Repository getRepo() {
		return repo;
	}

	public static void setRepo(Repository repo) {
		Spider.repo = repo;
	}
	
	public static Storeable getStor() {
		return stor;
	}

	public static void setStor(Storeable stor) {
		Spider.stor = stor;
	}

	void check() {
		
		if(download!=null && proce!=null && repo!=null && stor!=null) {
			logger.info("================== 爬虫启动 ==================");
			logger.info("目前采用的DownLoad："+download.getClass().getSimpleName());
			logger.info("目前采用的Proce："+proce.getClass().getSimpleName());
			logger.info("目前采用的Repository："+repo.getClass().getSimpleName());
			logger.info("目前采用的Store："+stor.getClass().getSimpleName());
			logger.info("=============================================");
		}else {
			String msg = "配置文件出错";
			logger.error(msg);
			throw new RuntimeException(msg);
		}
		
	}

	public void start() {

		check();
		
		logger.info("开始抓取数据");
		
		while (true) {
			//System.out.println("------");
			String url = repo.pop();
			if (url != null) {
				threadPool.execute(new Runnable() {
					public void run() {
						long start_time = System.currentTimeMillis();
						Page page = download(url);
						long end_time = System.currentTimeMillis();
						logger.info(url+" 耗时："+(end_time - start_time) + " ms");
						proce.proce(page);
						
						List<String> url_list = page.getUrl_list();
						
						for (String urls : url_list) {
							//System.out.println(urls);
							repo.add(urls);
							//System.out.println(urls+"======");
						}
						if (url.startsWith("https://item.jd.com/")) {
							stor.store(page);
						}
						
						try {
							Thread.currentThread().sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				
				
			} else {
				
				logger.info("============url仓库为空,暂时停止=========");
				try {
					Thread.currentThread().sleep(5000);
				} catch (InterruptedException e) { // TODO Auto-generated catch
													// block
					e.printStackTrace();
				}
			}

		}

	}

	public static void main(String[] args) {
		
		Spider spider = new Spider();
		String url = "https://list.jd.com/list.html?cat=9987,653,655";
		spider.setDownload(new DownLoad_HttpClientimpl());
		spider.setProce(new proce_JDimpl());
		spider.setStor(new Mysql_storeimpl());
		
		repo.add(url);

		spider.start();

		// Page page = spider.start(url);

		/*
		 * System.out.println(page.getGoodsId());
		 * System.out.println(page.getTitle());
		 * System.out.println(page.getPrice());
		 * System.out.println(page.getHashmap());
		 * System.out.println(page.getUrl());
		 * System.out.println(page.getImgUrl());
		 */

		// goods_id,data_url,pic_url,title,price,param,`current_time`)

		// 将数据存入Mysql

	}

}
