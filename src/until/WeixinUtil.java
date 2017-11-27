package until;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Map;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;  

import org.slf4j.LoggerFactory;

import pojo.AccessToken;
import pojo.Menu;
import pojo.TicketJson;
import pojo.WxParams;
import thread.TokenThread;

public class WeixinUtil {
	//此处的appid与wx.config 参数appId一致   微信公众账号提供给开发者的信息，以下同理  
    public static String APPID = "wxd42462bf296501b1";  
      
    //同上  
    public static String SECRET = "35104c6c8a3a2f5cebccb1a5b6f745e3";  
    
	 private static org.slf4j.Logger log =  LoggerFactory.getLogger(WeixinUtil.class);  
	  
	    /** 
	     * 发起https请求并获取结果 
	     *  
	     * @param requestUrl 请求地址 
	     * @param requestMethod 请求方式（GET、POST） 
	     * @param outputStr 提交的数据 
	     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
	     */  
	    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {  
	        JSONObject jsonObject = null;  
	        StringBuffer buffer = new StringBuffer();  
	        try {  
	            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
	            TrustManager[] tm = { new MyX509TrustManager() };  
	            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
	            sslContext.init(null, tm, new java.security.SecureRandom());  
	            // 从上述SSLContext对象中得到SSLSocketFactory对象  
	            SSLSocketFactory ssf = sslContext.getSocketFactory();  
	  
	            URL url = new URL(requestUrl);  
	            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
	            httpUrlConn.setSSLSocketFactory(ssf);  
	  
	            httpUrlConn.setDoOutput(true);  
	            httpUrlConn.setDoInput(true);  
	            httpUrlConn.setUseCaches(false);  
	            // 设置请求方式（GET/POST）  
	            httpUrlConn.setRequestMethod(requestMethod);  
	  
	            if ("GET".equalsIgnoreCase(requestMethod))  
	                httpUrlConn.connect();  
	  
	            // 当有数据需要提交时  
	            if (null != outputStr) {  
	                OutputStream outputStream = httpUrlConn.getOutputStream();  
	                // 注意编码格式，防止中文乱码  
	                outputStream.write(outputStr.getBytes("UTF-8"));  
	                outputStream.close();  
	            }  
	  
	            // 将返回的输入流转换成字符串  
	            InputStream inputStream = httpUrlConn.getInputStream();  
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
	  
	            String str = null;  
	            while ((str = bufferedReader.readLine()) != null) {  
	                buffer.append(str);  
	            }  
	            bufferedReader.close();  
	            inputStreamReader.close();  
	            // 释放资源  
	            inputStream.close();  
	            inputStream = null;  
	            httpUrlConn.disconnect();  
	            jsonObject = JSONObject.fromObject(buffer.toString());  
	        } catch (ConnectException ce) {  
	            log.error("Weixin server connection timed out.");  
	        } catch (Exception e) {  
	            log.error("https request error:{}", e);  
	        }  
	        return jsonObject;  
	    }  
	    
	 // 获取access_token的接口地址（GET） 限200（次/天）  
	    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";  
	      
	    /** 
	     * 获取access_token 
	     *  
	     * @param appid 凭证 
	     * @param appsecret 密钥 
	     * @return 
	     */  
	    public static AccessToken getAccessToken(String appid, String appsecret) {  
	        AccessToken accessToken = null;  
	      
	        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);  
	        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
	        // 如果请求成功  
	        if (null != jsonObject) {  
	            try {  
	                accessToken = new AccessToken();  
	                accessToken.setToken(jsonObject.getString("access_token"));  
	                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));  
	            } catch (JSONException e) {  
	                accessToken = null;  
	                // 获取token失败  
	                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
	            }  
	        }  
	        return accessToken;  
	    }  
	    
	 // 菜单创建（POST） 限100（次/天）  
	    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";  
	      
	    /** 
	     * 创建菜单 
	     *  
	     * @param menu 菜单实例 
	     * @param accessToken 有效的access_token 
	     * @return 0表示成功，其他值表示失败 
	     */  
	    public static int createMenu(Menu menu, String accessToken) {  
	        int result = 0;  
	      
	        // 拼装创建菜单的url  
	        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);  
	        // 将菜单对象转换成json字符串  
	        String jsonMenu = JSONObject.fromObject(menu).toString();  
	        // 调用接口创建菜单  
	        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);  
	      
	        if (null != jsonObject) {  
	            if (0 != jsonObject.getInt("errcode")) {  
	                result = jsonObject.getInt("errcode");  
	                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
	            }  
	        }  
	      
	        return result;  
	    }
	    
	    /**
	     * 调用微信JS接口的临时票据
	     * 
	     * @param access_token 接口访问凭证
	     * @return
	     */
	    public static String getJsApiTicket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	    
	    public static TicketJson  getJsApiTicket() {
	       
	        AccessToken at = TokenThread.accessToken;
	        String requestUrl = getJsApiTicket_url.replace("ACCESS_TOKEN", at.getToken());
	        // 发起GET请求获取凭证
	        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
	        TicketJson ticketObj = (TicketJson) JSONObject.toBean(jsonObject,TicketJson.class);  
	        String ticket = null;
	        if (null != jsonObject) {
	            try {
	                ticket = jsonObject.getString("ticket");
	            } catch (JSONException e) {
	                // 获取token失败
	                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
	            }
	        }
	        return ticketObj;
	    }
	    
	    /** 
	     * 获取js sdk 认证信息 
	    * @author  
	    * @date 创建时间 2016年7月28日 上午11:25:01  
	    * @param url 
	    * @return 
	     */  
	    public static Map<String, String> getSign(String url){  
	          
	        //处理token失效的问题  
	        try {  
	            long tokenTimeLong = Long.parseLong(WxParams.tokenTime);  
	            long tokenExpiresLong = Long.parseLong(WxParams.tokenExpires);  
	              
	            //时间差  
	            long differ = (System.currentTimeMillis() - tokenTimeLong) /1000;  
	            if (WxParams.token == null ||  differ > (tokenExpiresLong - 1800)) {  
	                System.out.println("token为null，或者超时，重新获取");  
	                AccessToken tokenJson = TokenThread.accessToken;
	                if (tokenJson != null) {  
	                    WxParams.token = tokenJson.getToken();  
	                    WxParams.tokenTime = System.currentTimeMillis()+"";  
	                    WxParams.tokenExpires = tokenJson.getExpiresIn()+"";  
	                }  
	            }  
	        } catch (Exception e) {  
	            // TODO: handle exception  
	            e.printStackTrace();  
	            AccessToken tokenJson = TokenThread.accessToken;  
	            if (tokenJson != null) {  
	                WxParams.token = tokenJson.getToken();  
	                WxParams.tokenTime = System.currentTimeMillis()+"";  
	                WxParams.tokenExpires = tokenJson.getExpiresIn()+"";  
	            }  
	        }  
	  
	        //处理ticket失效的问题  
	        try {  
	            long ticketTimeLong = Long.parseLong(WxParams.ticketTime);  
	            long ticketExpiresLong = Long.parseLong(WxParams.ticketExpires);  
	              
	            //时间差  
	            long differ = (System.currentTimeMillis() - ticketTimeLong) /1000;  
	            if (WxParams.ticket == null ||  differ > (ticketExpiresLong - 1800)) {  
	                System.out.println("ticket为null，或者超时，重新获取");  
	                TicketJson ticketJson = getJsApiTicket();  
	                if (ticketJson != null) {  
	                    WxParams.ticket = ticketJson.getTicket();  
	                    WxParams.ticketTime = System.currentTimeMillis()+"";  
	                    WxParams.ticketExpires = ticketJson.getExpires_in()+"";  
	                }  
	            }  
	        } catch (Exception e) {  
	            // TODO: handle exception  
	            e.printStackTrace();  
	            TicketJson ticketJson = getJsApiTicket();  
	            if (ticketJson != null) {  
	                WxParams.ticket = ticketJson.getTicket();  
	                WxParams.ticketTime = System.currentTimeMillis()+"";  
	                WxParams.ticketExpires = ticketJson.getExpires_in()+"";  
	            }  
	        }  
	  
	        Map<String, String> ret = SignUtil.sign(WxParams.ticket, url);  
	        System.out.println("计算出的签名-----------------------");  
	        for (Map.Entry entry : ret.entrySet()) {  
	            System.out.println(entry.getKey() + ", " + entry.getValue());  
	        }  
	        System.out.println("-----------------------");  
	        return ret;  
	    }  
	  
	
	    
	  
}
