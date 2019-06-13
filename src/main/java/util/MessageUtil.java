package util;

import bean.Message;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {
	public static List<Element> xmlTOList(HttpServletRequest request) throws Exception{

		Map<String,String> map =new HashMap<String,String>();
		SAXReader reader =new SAXReader();
		InputStream ins=null;
		try {
			ins =request.getInputStream();
		} catch (IOException e) {

			e.printStackTrace();
		}
		Document docment =reader.read(ins);//读取流对象

		Element root=docment.getRootElement();
		List<Element> list =root.elements();
		for (Element element:list) {
			map.put(element.getName(),element.getText());
		}
		ins.close();
		return list;
	}


	public static Map<String,String> xmlToMap(String result) throws DocumentException {
		Map<String,String> map =new HashMap<String,String>();
		Document document = DocumentHelper.parseText(result);
		Element root=document.getRootElement();
		List<Element> list =root.elements();
		for (Element element:list) {
			map.put(element.getName(),element.getText());
			System.out.println(map.get(element.getName())+"                        "+element.getText());
		}
		return map;
	}
	public static String messageToXml(Message message){
		XStream xs = new XStream();
		xs.alias("xml", message.getClass());
		return xs.toXML(message);
	}

	public static void test(HttpServletRequest request) throws IOException, DocumentException {

		FileInputStream fis = new FileInputStream(request.getQueryString());
		byte[] b = new byte[fis.available()];
		fis.read(b);
		String str = new String(b);

		Document doc = DocumentHelper.parseText(str);

		System.out.println("-----------------------------------request"+doc.asXML());

	}
}
