package spider_demo.Spider_demo.repository;

import redis.clients.jedis.Jedis;
import spider_demo.Spider_demo.dbutil.RedisUtils;
import spider_demo.Spider_demo.domain.Page;

public class Redis_goods_Repository implements Repository{

	@Override
	public void add(String url) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.lpush("goodurl", url);
		RedisUtils.returnResource(jedis);
	}

	@Override
	public String pop() {
		Jedis jedis = RedisUtils.getJedis();
		String pop_url = jedis.rpop("goodurl");
		RedisUtils.returnResource(jedis);
		return pop_url;
	}
	

}
