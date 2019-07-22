package spider_demo.Spider_demo.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Page {
	
	private String url;
	
	private String context;
	
	private String title;
	
	private String priceUrl;
	
	private String price;
	
	private String imgUrl;
	
	private String goodsId;
	
	private List<String> url_list = new ArrayList<String>();
	
	private Map<String, String> secinfor = new HashMap<String, String>();

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPriceUrl() {
		return priceUrl;
	}

	public void setPriceUrl(String priceUrl) {
		this.priceUrl = priceUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Map<String, String> getHashmap() {
		return secinfor;
	}

	public void putHashmap(String key,String value) {
		this.secinfor.put(key, value);
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public List<String> getUrl_list() {
		return url_list;
	}

	public void setUrl_list(List<String> url_list) {
		this.url_list = url_list;
	}

	
	
	

}
