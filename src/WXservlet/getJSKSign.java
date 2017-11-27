package WXservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import process.CoreService;

public class getJSKSign extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）  
        request.setCharacterEncoding("UTF-8");  
        response.setCharacterEncoding("UTF-8");  
  
        // 调用核心业务类接收消息、处理消息  
        String url = request.getParameter("url");   
          
        // 响应消息  
        PrintWriter out = response.getWriter();  
        Map<String,String> sign = until.WeixinUtil.getSign(url);
        JSONObject json = JSONObject.fromObject(sign); 
        out.print(json);  
        out.close();  
    }  
}
