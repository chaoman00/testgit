package wechat;

import util.ManagerSsoUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;

public class ManagerSSOServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response){

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String userId = request.getParameter("username");
        String managerPath = "http://ftdq.demo.xiaoi.com/manager/authmgr/auth!login.action?";
        System.out.println("managerPath==="+managerPath);
        String userrole = "";
        String url = ManagerSsoUtils.getManagerUrl(userId, userrole, managerPath);
        out = response.getWriter();
        out.write(url);
        out.flush();
        out.close();
    }

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse response)
//            throws ServletException, IOException {
//        this.doPost(req, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse response)
//            throws ServletException, IOException {
//        String userName = req.getParameter("username");
//        String password = req.getParameter("password");
//        String info = System.getProperty("ibot.mgr.auth.sigsecret");
//        //userName 必须在后台配置过的用户;
////        String sig = this.getMd5Str(userName + info);
//        String passWord = this.getMd5Str(password);
//        String url = "http://ftdq.demo.xiaoi.com/manager/authmgr/auth!login.action?username=" + userName + "&password=" + passWord;
//        System.out.println(url);
//        response.sendRedirect(url);
//
//    }

    private String getMd5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

}
