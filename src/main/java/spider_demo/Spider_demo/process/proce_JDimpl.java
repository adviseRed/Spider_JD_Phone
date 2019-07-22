package spider_demo.Spider_demo.process;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import spider_demo.Spider_demo.dbutil.ContextUtil;
import spider_demo.Spider_demo.dbutil.GetAttrUtil;
import spider_demo.Spider_demo.dbutil.GetContextUtil;
import spider_demo.Spider_demo.domain.Page;

public class proce_JDimpl implements Proce {

	String X_title_path = "//div[@class=\"sku-name\"]";

	@Override
	public void proce(Page page) {

		// 手机list页面
		// https://list.jd.com/list.html?cat=9987,653,655
		// https://list.jd.com/list.html?cat=9987,653,655&page=3&sort=sort_rank_asc&trans=1&JL=6_0_0#J_main
		// https://list.jd.com/list.html?cat=9987,653,655&page=4&sort=sgort_rank_asc&trans=1&JL=6_0_0#J_main
		try {
			if (page.getUrl().startsWith("https://item.jd.com/")) {
				alonePhone(page);
				
			} else if (page.getUrl().startsWith("https://list.jd.com/list.html")) {
				// //*[@id="plist"]/ul/li

				HtmlCleaner htmlCleaner = new HtmlCleaner();
				TagNode tagnode = htmlCleaner.clean(page.getContext());

				String xpath = "//*[@id=\"plist\"]/ul/li";
				Object[] evaluateXPath;

				evaluateXPath = tagnode.evaluateXPath(xpath);

				// System.out.println(evaluateXPath.length);
				for (Object evaluateXPath1 : evaluateXPath) {
					TagNode tagnode1 = (TagNode) evaluateXPath1; // //*[@id="plist"]/ul/li[2]/div
																	// //
					tagnode1.getAttributeByName("/div[@data-sku]");
					Object[] evaluateXPath2 = tagnode1.evaluateXPath("/div");
					TagNode tagnode2 = (TagNode) evaluateXPath2[0];
					String goodsId = tagnode2.getAttributeByName("data-sku");
					// System.out.println(attributeByName);

					// 手机详情页 url ： "https://item.jd.com/100003395443.html"
					String goodsIdUrl = "https://item.jd.com/" + goodsId + ".html";
					
					// System.out.println(goodsIdUrl);
					//System.out.println(goodsIdUrl+"============");
					
					page.getUrl_list().add(goodsIdUrl);
					
				}

				// //*[@id="J_bottomPage"]/span[1]/a[10]

				String nextpageurl = "//*[@id=\"J_bottomPage\"]/span[1]/a[last()]";

				// class="pn-next"

				if (GetAttrUtil.getInf(page.getContext(), nextpageurl, "class").equals("pn-next")) {
					String inf = GetAttrUtil.getInf(page.getContext(), nextpageurl, "href");
					// https://list.jd.com/list.html?cat=9987,653,655&page=2&sort=sort_rank_asc&trans=1&JL=6_0_0&ms=8#J_main
					// /list.html?cat=9987,653,655&page=3&sort=sort_rank_asc&trans=1&JL=6_0_0
					String nexturl = "https://list.jd.com" + inf;
					//System.out.println(nexturl);
					//System.out.println(nexturl+"============");
					page.getUrl_list().add(nexturl);
					
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void alonePhone(Page page) {
		String title = gettitle(page.getContext(), X_title_path);
		page.setTitle(title);
		// String price_url =
		// 价格json ： "https://p.3.cn/prices/mgets?skuIds=J_100003395443";
		// 手机详情页 url ： "https://item.jd.com/100003395443.html"

		Pattern pattern = Pattern.compile("https://item.jd.com/([0-9]+).html");
		String price = null;
		// System.out.println(page.getUrl());
		Matcher matcher = pattern.matcher(page.getUrl());
		String goodsId = null;
		if (matcher.find()) {
			goodsId = matcher.group(1);
			// System.out.println(price_url);

			page.setGoodsId(goodsId);

			page.setPriceUrl("https://p.3.cn/prices/mgets?skuIds=J_" + goodsId);

			price = getprice(page);
			// System.out.println(price+"=====");
			page.setPrice(price);
		}

		// 图片
		// //*[@id="spec-img"]
		String imgUrl = getImgUrl(page.getContext(), "//*[@id=\"spec-img\"]", "data-origin");
		// System.out.println(imgUrl);
		page.setImgUrl("https:" + imgUrl);

		getSpec(page, "//*[@id=\"detail\"]/div[2]/div[2]/div[1]/div");
	}

	private void getSpec(Page page, String xpath) {

		// //*[@id="detail"]/div[2]/div[2]/div[1]/div[1]/dl/dl[1]/dt
		// //*[@id="detail"]/div[2]/div[2]/div[1]/div[1]/dl/dl[1]/dd
		// //*[@id="detail"]/div[2]/div[2]/div[1]/div
		// //*[@id="detail"]/div[2]/div[2]/div[1]
		// //*[@id="detail"]/div[2]/div[2]/div[1]/div[1]

		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode tagNode = htmlCleaner.clean(page.getContext());
		Object[] evaluateXPath;
		try {
			evaluateXPath = tagNode.evaluateXPath(xpath);
			// System.out.println(evaluateXPath.length+"--------------0");
			for (Object evaluateXPath1 : evaluateXPath) {
				TagNode tagnode_1 = (TagNode) evaluateXPath1;
				Object[] evaluateXPath2 = tagnode_1.evaluateXPath("/dl/dl");
				//System.out.println(evaluateXPath2.length+"--------------1");

				for (Object evaluateXPath3 : evaluateXPath2) {
					TagNode tagnode_2 = (TagNode) evaluateXPath3;
					Object[] evaluateXPath4 = tagnode_2.evaluateXPath("/dt");

					TagNode Tagkey = (TagNode) evaluateXPath4[0];
					CharSequence Tkey = Tagkey.getText();
					String key = Tkey.toString();
					Object[] evaluateXPath5 = tagnode_2.evaluateXPath("/dd[last()]");
					TagNode Tagvalue = (TagNode) evaluateXPath5[0];
					CharSequence Tvalue = Tagvalue.getText();
					String value = Tvalue.toString();
					// System.out.println(key+":"+value);
					page.putHashmap(key, value);
				}

				// CharSequence text = tagnode_1.getText();
				// System.out.println(text);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(page.getHashmap().toString());
	}

	private String getImgUrl(String context, String XPath, String attr) {
		return GetAttrUtil.getInf(context, XPath, attr);
	}

	private String getprice(Page page) {
		// System.out.println(ContextUtil.getContext(page.getPriceUrl()));

		JSONArray json_price = JSONArray.parseArray(ContextUtil.getContext(page.getPriceUrl()));

		JSONObject jsonObject = json_price.getJSONObject(0);

		// System.out.println(json_price.toString());
		String price = null;

		if (jsonObject.size() > 0) {
			price = jsonObject.getString("p").replace(".00", "");
		}

		// System.out.println(price+"1");

		return price;
	}

	public String gettitle(String context, String XPath) {
		return GetContextUtil.getInf(context, XPath);

	}

}
