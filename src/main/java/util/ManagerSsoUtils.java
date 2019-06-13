package util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class ManagerSsoUtils {
    public static final String CHARSET = "utf-8";
    // manager中配置的加密串
    private final static String debugkey = "qazwsxedcrfvtgbyhnujmiko";
    static String transformation = "DESede/ECB/PKCS5Padding";
    static String algorithm = "DESede";

    private static byte[] getKey() throws Exception {
        byte[] ret = null;
        ret = debugkey.getBytes(CHARSET);
        return ret;
    }
    public static String encode(String src) throws Exception {
        return encode0(src, getKey());
    }
    private static String encode0(String src, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, algorithm);
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] b = cipher.doFinal(src.getBytes(CHARSET));
        return new String(Hex.encodeHex(b));
    }
    public String decode(String dest) throws Exception {
        return decode0(dest, getKey());
    }

    public String decode0(String dest, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, algorithm);
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] b = cipher.doFinal(Hex.decodeHex(dest.toCharArray()));
        return new String(b, CHARSET);
    }
    // for 单点
    public static  String getManagerUrl(String username, String userrole, String managerPath) {
        String path = "";
        try {
            String raw = "timestamp="
                    + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + "&userprivilege=0&username="
                    + username + "&userroles=管理员";
            String token = encode(raw);
            String digest = DigestUtils.md5Hex(token);
            /*HttpClient client = new HttpClient();
            PostMethod m = new PostMethod(managerPath);
            m.addRequestHeader("X-Requested-With", "XMLHTTPRequest");
            m.addParameter("ssotoken", token);
            m.addParameter("ssochecksum", digest);
            client.executeMethod(m);
            System.out.println("登录信息验证" + "statusCode=" + m.getStatusCode()
                    + ",responseText=" + m.getResponseBodyAsString());
             m.releaseConnection();*/

            path = String.format(managerPath
                            + "ssotoken=%s&ssochecksum=%s",
                    token, digest);
            System.out.println("error地址-------------->"+path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }
}
