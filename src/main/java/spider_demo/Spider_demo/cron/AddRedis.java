package spider_demo.Spider_demo.cron;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import redis.clients.jedis.Jedis;
import spider_demo.Spider_demo.dbutil.RedisUtils;

public class AddRedis implements Job{
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Jedis jedis = RedisUtils.getJedis();
		
		java.util.List<String> urlList = jedis.lrange("startUrl", 0, -1);
		for (String url : urlList) {
			jedis.lpush("urlList", url);
		}
		
		
	}
}
