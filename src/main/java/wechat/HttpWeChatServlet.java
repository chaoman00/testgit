package wechat;

import bean.Message;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import util.CheckUtil;
import util.MessageUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HttpWeChatServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -969866750246431384L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		String signature = request.getParameter("signature");

		String timestamp = request.getParameter("timestamp");

		String nonce = request.getParameter("nonce");

		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();

		if(CheckUtil.checkSignature(signature, timestamp, nonce)){

			//如果校验成功，将得到的随机字符串原路返回

			out.print(echostr);

		}
		System.out.println("------------------------------------"+echostr);

	}


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("---------------------------------进入post");
		request.setCharacterEncoding("UTF-8");
//		response.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		try {
			List<Element> list = MessageUtil.xmlTOList(request);
			String ToUserName = list.get(0).getText();
			String FromUserName =  list.get(1).getText();
			String CreateTime =  list.get(2).getText();
			String MsgType =  list.get(3).getText();
			String MsgId = list.get(5).getText();
			String context =  list.get(4).getText();
//			String context = request.getParameter("question");
			context = URLEncoder.encode(context,"utf-8");
//			String context = "%E4%BD%A0%E5%A5%BD";
			System.out.println("--------------------------------context"+context);

			String url = "http://localhost/robot/ask.action?question="+context+"&userId=13&platform=web";
            URL uploadServlet = new URL(url);

            HttpURLConnection servletConnection = (HttpURLConnection) uploadServlet.openConnection();
            servletConnection.setRequestMethod("GET");
            servletConnection.setDoOutput(true);
            servletConnection.setDoInput(true);
            servletConnection.setAllowUserInteraction(true);


            BufferedReader reader = new BufferedReader(new InputStreamReader(servletConnection.getInputStream(),"utf-8"));
            String line,result = "";
            while (null != (line=reader.readLine())){
                result += line;
            }
			Map<String,String>  map=  MessageUtil.xmlToMap(result);

            reader.close();
            out.print(result);
			String answer = map.get("Content");
//            String[] answers = result.split("\"");
//            for (String string:answers
//            ) {
//                System.out.println(string);
//            }
//            String answer = answers[17];

			if ("text".equals(MsgType)) {
				Message message = new Message();
				message.setFromUserName(ToUserName);
				message.setToUserName(FromUserName);
				message.setMsgType("text");
				message.setCreateTime(System.currentTimeMillis());//创建当前时间为消息时间
				message.setContent(answer);
				String str = MessageUtil.messageToXml(message);
				out.print(str);
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	 public static String wechatUrl(String url,String data) throws IOException {
		 URL uploadServlet = new URL(url);
		 HttpURLConnection servletConnection = (HttpURLConnection) uploadServlet.openConnection();
		 servletConnection.setRequestMethod("POST");
		 servletConnection.setDoOutput(true);
		 servletConnection.setAllowUserInteraction(false);


		 BufferedReader reader = new BufferedReader(new InputStreamReader(servletConnection.getInputStream()));
		 String line,result = "";
		 while (null != (line=reader.readLine())){
			 result += line;
		 }
		 System.out.println(result);
		 reader.close();
		 String[] answers = result.split("\"");
		 for (String string:answers
		 ) {
			 System.out.println(string);
		 }
		 String answer = answers[31];

		 return answer;
	 }
	 
	/**
	 * 排序方法
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static String sort(String token, String timestamp, String nonce) {
	    String[] strArray = { token, timestamp, nonce };
	    Arrays.sort(strArray);
	 
	    StringBuilder sbuilder = new StringBuilder();
	    for (String str : strArray) {
	        sbuilder.append(str);
	    }
	 
	    return sbuilder.toString();
	}
}
