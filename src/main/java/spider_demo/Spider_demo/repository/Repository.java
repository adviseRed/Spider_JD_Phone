package spider_demo.Spider_demo.repository;

import spider_demo.Spider_demo.domain.Page;

public interface Repository {
	void add(String url);
	String pop();
}
