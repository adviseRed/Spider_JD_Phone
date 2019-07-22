package spider_demo.Spider_demo.store;

import java.util.Date;

import spider_demo.Spider_demo.dbutil.MyDateUtils;
import spider_demo.Spider_demo.dbutil.MyDbUtils;
import spider_demo.Spider_demo.domain.Page;

public class Mysql_storeimpl implements Storeable{

	@Override
	public void store(Page page) {
		String date = MyDateUtils.formatDate2(new Date());
		MyDbUtils.update(MyDbUtils.INSERT_LOG, page.getGoodsId(), page.getUrl(), page.getImgUrl(), page.getTitle(),
				page.getPrice(), page.getHashmap().toString(), date);
	}

}
