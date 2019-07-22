package spider_demo.Spider_demo.download;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import spider_demo.Spider_demo.dbutil.ContextUtil;
import spider_demo.Spider_demo.domain.Page;

public class DownLoad_HttpClientimpl implements DownLoad {

	@Override
	public Page download(String url) {
		Page page = new Page();
		page.setUrl(url);
		String context = null;
		context = ContextUtil.getContext(url);
		page.setContext(context);

		return page;
	}

}
