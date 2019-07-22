package spider_demo.Spider_demo.dbutil;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ContextUtil {
	
	public static String getContext(String url) {
		HttpClientBuilder builder = HttpClients.custom();

		CloseableHttpClient client = builder.build();
		
		String context = null;
		try {
			CloseableHttpResponse execute = client.execute(new HttpGet(url));
			context = EntityUtils.toString(execute.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return context;
	}
	
	
	
}
