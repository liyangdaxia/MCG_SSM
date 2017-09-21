package com.zhao.controller;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class LoginController {
	private static final Logger L = Logger.getLogger(LoginController.class);

	@RequestMapping("/login.do")
	public void Login(HttpServletRequest request,HttpServletResponse response) {
		    try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    String code=request.getParameter("code");
			PrintWriter out = null;
	        BufferedReader in = null;
			String url="https://api.weixin.qq.com/sns/jscode2session";
			try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
	        URLConnection conn = realUrl.openConnection();
	     // 设置通用的请求属性
	        conn.setRequestProperty("accept", "*/*");
	        conn.setRequestProperty("connection", "Keep-Alive");
	        conn.setRequestProperty("user-agent",
	                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	        // 发送POST请求必须设置如下两行
	        conn.setDoOutput(true);
	        conn.setDoInput(true);
	     // 获取URLConnection对象对应的输出流
	        out = new PrintWriter(conn.getOutputStream());
	        // 发送请求参数
	        String param="appid=wxb38ad0470a8e7694&secret=1f0ffae6a0ae59124863aa64c88258e5&js_code="+code+"&grant_type=authorization_code";
	        out.print(param);
	        // flush输出流的缓冲
	        out.flush();
	     // 定义BufferedReader输入流来读取URL的响应
	        in = new BufferedReader(
	                new InputStreamReader(conn.getInputStream()));
	        String line;
	        String result="";
	        while ((line = in.readLine()) != null) {
	            result += line;
	        }
	        System.out.println(result);
	        HttpSession session=request.getSession();
	        session.setMaxInactiveInterval(10*60);
	        UUID uuid = UUID.randomUUID();
	        System.out.println(uuid.toString());
	        session.setAttribute("loginSessionKey",uuid.toString());
			String loginSessionKey=(String) request.getSession().getAttribute("loginSessionKey");	
			response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().write(loginSessionKey);
			response.setStatus(200);
			L.info("系统派发session");
		  } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            L.error("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	      } finally{
	          try{
	              if(out!=null){
	                  out.close();
	              }
	              if(in!=null){
	                  in.close();
	              }
	          }
	          catch(IOException ex){
	              ex.printStackTrace();
	          }
	      }
	}
}
