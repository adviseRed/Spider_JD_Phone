package spider_demo.Spider_demo;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import spider_demo.Spider_demo.domain.Page;
import spider_demo.Spider_demo.download.DownLoad_HttpClientimpl;
import spider_demo.Spider_demo.process.proce_JDimpl;
import spider_demo.Spider_demo.spider.Spider;

public class Test_demo {

	@Test
	public void test() throws XPatherException {

		Spider spider = new Spider();
		String url = "https://item.jd.com/100002749549.html";

		spider.setDownload(new DownLoad_HttpClientimpl());

		Page page = spider.download(url);

		//System.out.println(page.getContext());

		spider.setProce(new proce_JDimpl());

		spider.getProce().proce(page);

		String title = spider.gettitle(page);

		System.out.println(title);
		// https://p.3.cn/prices/mgets?skuIds=J_100003395443
		
		String price = spider.getprice(page);
		
		System.out.println(price);
		
		System.out.println(spider.getimgurl(page));
		
		System.out.println(spider.getsecinfor(page));
		
		System.out.println(spider.getGoodsId(page));

	}

}
