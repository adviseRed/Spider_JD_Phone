package spider_demo.Spider_demo.dbutil;
/**
 * Redis工具类
 * @author xuwei.tech
 *
 */

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {
	private RedisUtils() {}
	
	private static JedisPool jedisPool = null;
	
	/**
	 * 从连接池中获取链接
	 * @return
	 */
	public static synchronized Jedis getJedis() {
		if(jedisPool==null) {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			//连接池中最大空闲连接数
			poolConfig.setMaxIdle(10);
			//连接池中最多允许创建的连接数
			poolConfig.setMaxTotal(100);
			//创建链接的最大等待时间
			poolConfig.setMaxWaitMillis(2000);
			//表示从连接池中取出链接的时候都会测试一下，这样可以保证取出的链接都是可用的
			poolConfig.setTestOnBorrow(true);
			//获取连接池
			jedisPool = new JedisPool(poolConfig, "192.168.157.10", 6379);
		}
		
		return jedisPool.getResource();
	}
	
	/**
	 * 返还连接池中的链接
	 * @param jedis
	 */
	public static void returnResource(Jedis jedis) {
		jedis.close();
	}
	
	/**
	 * 关闭连接池
	 * @param jedisPool
	 */
	public static void closePool() {
		jedisPool.close();
	}
	

}
