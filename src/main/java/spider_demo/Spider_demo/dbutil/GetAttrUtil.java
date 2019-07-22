package spider_demo.Spider_demo.dbutil;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class GetAttrUtil {
	public static String getInf(String context,String XPath,String attr) {
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		
		TagNode tagNode = htmlCleaner.clean(context);

		String result = null;
		
		Object[] evaluateXPath;
		
		try {
			evaluateXPath = tagNode.evaluateXPath(XPath);
			
			if(evaluateXPath.length>0) {
				TagNode tagnode_1 = (TagNode)evaluateXPath[0];
				CharSequence text = tagnode_1.getAttributeByName(attr);
				result = text.toString().trim();
				
			}
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return result;
		
	}
}
